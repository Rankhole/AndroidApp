package com.example.androidapp;

import android.app.Activity;
import android.widget.TextView;

import com.example.androidapp.bt.ConnectActivity;
import com.example.androidapp.database.Word;
import com.example.androidapp.database.WordDB;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

public class Game implements Runnable {

    private final int A_isOnMove = 1;   // Team A ist immer das Server Team
    private final int B_isOnMove = 2;
    private final int RANDOM_BEGIN = 3;
    private final int NEXT_WORD = 4;
    private final int ROUND_COMPLETED = 0;
    Word word1 = null;
    private InputStream is;
    private OutputStream os;
    private DataOutputStream dos;
    private DataInputStream dis;
    private String begin;
    private Random r;
    private boolean TEAM_A = false;
    private int state;
    private String status;
    private Activity context;
    private TextView textView, wordView, forb1, forb2, forb3, forb4;
    private MultiplayerActivity multiplayerActivity;
    private WordDB wordDB;
    private int tries;
    private int enemyTries;
    private int skips;

    public Game(String begin, InputStream is, OutputStream os, MultiplayerActivity context, TextView textView, TextView textView2, TextView forb1, TextView forb2, TextView forb3, TextView forb4, final WordDB wordDb) {
        this.begin = begin;
        this.r = new Random();
        this.is = is;
        this.os = os;
        this.dis = new DataInputStream(this.is);
        this.dos = new DataOutputStream(this.os);
        this.state = 0;
        this.status = null;
        this.multiplayerActivity = context;
        this.context = context;
        this.textView = textView;
        this.wordView = textView2;
        this.forb1 = forb1;
        this.forb2 = forb2;
        this.forb3 = forb3;
        this.forb4 = forb4;
        tries = 0;
        enemyTries = 0;
        skips = 0;

        Runnable exchanger = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String msg = null;
                    try {
                        msg = dis.readUTF();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    switch (msg) {
                        case "forbiddenword":
                            enemyTries++;
                            if (enemyTries == 3) {
                                changeTeam();
                            }
                            break;
                        case "changeteam":
                            changeTeam();
                            break;
                        default:
                            try {
                                if (TEAM_A) {
                                    sendWord(wordDb.getRandomWord());
                                } else {
                                    Word temp = getWord();
                                    refreshWord(temp.getWord(), temp.getForbiddenWords());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
            }
        };

        Thread exchangerThread = new Thread(exchanger);
        exchangerThread.start();

        TEAM_A = ConnectActivity.isServer;

        this.wordDB = wordDb;

        try {
            word1 = wordDb.getRandomWord();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void negotiate() throws IOException {    //Aushandeln des Beginner-Teams
        int sendBeginner = 0;
        switch (begin) {
            case ("a"):
                sendBeginner = A_isOnMove;
                break;
            case ("b"):
                sendBeginner = B_isOnMove;
                break;
            case ("r"):
                sendBeginner = RANDOM_BEGIN;
                break;
        }
        dos.writeInt(sendBeginner);
        int readBeginner = dis.readInt();
        if (readBeginner == sendBeginner) {
            state = readBeginner;
        }
        if (state == RANDOM_BEGIN || readBeginner != sendBeginner) {
            sendRandomInt();
        }
        switch ((state)) {
            case (A_isOnMove):
                refreshStatus("A beginnt");
                break;
            case (B_isOnMove):
                refreshStatus("B beginnt");
                break;
        }
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                multiplayerActivity.startTimer();
            }
        });
        startRound();

    }

    public void startRound() {       // startet Spielrunde
        try {

            if (TEAM_A) {
                Word word = wordDB.getRandomWord();
                sendWord(word);
                refreshWord(word.getWord(), word.getForbiddenWords());

                if (state == A_isOnMove) {


                } else {
                    //passiv

                }
            } else {
                Word temp = getWord();
                refreshWord(temp.getWord(), temp.getForbiddenWords());

                if (active()) {
                    // Team B aktiv

                } else {
                    // warte, ob neues Wort gesendet wurde (wenn A Skip Word gedrueckt hat

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void forbiddenWord() {
        try {
            tries++;
            dos.writeUTF("forbiddenword");
            if (tries == 3) {
                changeTeam();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void skipWord() {
        if (skips < 3) {
            try {
                if (TEAM_A) {
                    Word temp = wordDB.getRandomWord();
                    refreshWord(temp.getWord(), temp.getForbiddenWords());
                    sendWord(temp);
                } else {
                    dos.writeUTF("skipword"); //egal welches Wort, hauptsache Server schickt neues Wort
                    Word temp = getWord();
                    refreshWord(temp.getWord(), temp.getForbiddenWords());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // liest Wort aus Inputstream
    private Word getWord() {
        Word temp = null;
        try {
            temp = word1.getWordFromString(dis.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }

    // sendet Wort ueber Outputstream
    private void sendWord(Word word) {

        try {
            dos.writeUTF(word.getStringOfWord());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRandomInt() throws IOException {
        int myRandomNumber = r.nextInt();
        dos.writeInt(myRandomNumber);
        int otherRandomNumber = dis.readInt();
        if (otherRandomNumber > myRandomNumber) {
            if (TEAM_A) {
                state = B_isOnMove;
            } else {
                state = A_isOnMove;
            }
        } else {
            if (TEAM_A) {
                state = A_isOnMove;
            } else {
                state = B_isOnMove;
            }
        }
    }

    /**
     * aktualisiert das Textfeld status
     *
     * @param msg Text, der angezeigt werden soll
     */
    public void refreshStatus(final String msg) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(msg);
            }
        });
    }

    private void refreshWord(final String word, final List<String> forbiddenWords) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                wordView.setText(word);
                forb1.setText(forbiddenWords.get(0));
                forb2.setText(forbiddenWords.get(1));
                forb3.setText(forbiddenWords.get(2));
                forb4.setText(forbiddenWords.get(3));
            }
        });

    }

    @Override
    public void run() {
        try {
            this.negotiate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getStatus() {
        return this.status;
    }

    /**
     * wechselt das aktive Team
     */
    public void changeTeam() {
        tries = 0;
        enemyTries = 0;
        try {

            dos.writeInt(404);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (dis.readInt() == 404) {
                if (state == A_isOnMove) {
                    state = B_isOnMove;
                } else {
                    state = A_isOnMove;
                }
                if (state == A_isOnMove) {
                    refreshStatus("A ist an der Reihe");
                } else {
                    refreshStatus("B ist an der Reihe");
                }
                multiplayerActivity.startTimer();
                startRound();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean active() {
        if (TEAM_A && state == A_isOnMove) {
            return true;
        }
        return !TEAM_A && state == B_isOnMove;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setTEAM_A(boolean teamA) {
        this.TEAM_A = teamA;
    }

}



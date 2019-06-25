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
    private Thread exchangerThread;

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

        TEAM_A = ConnectActivity.isServer;

        this.wordDB = wordDb;

        try {
            word1 = wordDb.getRandomWord();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTimer() throws IOException {    //Aushandeln des Beginner-Teams
        dos.writeUTF(begin);
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                multiplayerActivity.startTimer();
            }
        });
        startRound();
    }

    private void startPassiveTimer() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                multiplayerActivity.startTimer();
            }
        });
        startRound();
    }

    /**
     * Fordert den Server dazu auf, ein neues Wort zu erstellen.
     */
    public void startRound() {       // startet Spielrunde
        try {
            if (TEAM_A) {
                Word word = wordDB.getRandomWord();
                sendWord(word);
                refreshWord(word.getWord(), word.getForbiddenWords());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Signalisiert, dass ein verbotenes Wort benutzt wurde.
     */
    public void forbiddenWord() {
        try {
            if (TEAM_A) {
                if (state == A_isOnMove) {
                    tries++;
                } else {
                    enemyTries++;
                }
            } else {
                if (state == A_isOnMove) {
                    enemyTries++;
                } else {
                    tries++;
                }
            }
            dos.writeUTF("forbiddenword");
            if (tries == 3 || enemyTries == 3) {
                changeTeam();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Überspringt das Wort. Pro Runde nur 3 Skips.
     */
    public void skipWord() {
        if (skips < 3) {
            try {
                if (TEAM_A) {
                    Word temp = wordDB.getRandomWord();
                    refreshWord(temp.getWord(), temp.getForbiddenWords());
                    sendWord(temp);
                } else {
                    dos.writeUTF("skipword"); //egal welches Wort, hauptsache Server schickt neues Wort
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        /**
         * Dies ist der Thread, der zur Kommunikation zwischen
         * Server und Client dient. Hier werden sämtliche Daten
         * ausgetauscht.
         */
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
                            if (TEAM_A) {
                                if (state == A_isOnMove) {
                                    tries++;
                                } else {
                                    enemyTries++;
                                }
                            } else {
                                if (state == A_isOnMove) {
                                    enemyTries++;
                                } else {
                                    tries++;
                                }
                            }
                            if (enemyTries == 3 | tries == 3) {
                                changeTeam();
                            }
                            break;
                        case "changeteam":
                            try {
                                dos.writeUTF("changeacknowledged");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            tries = 0;
                            enemyTries = 0;
                            if (state == A_isOnMove) {
                                state = B_isOnMove;
                            } else if (state == B_isOnMove) {
                                state = A_isOnMove;
                            }
                            if (state == A_isOnMove) {
                                refreshStatus("A ist an der Reihe");
                            } else if (state == B_isOnMove) {
                                refreshStatus("B ist an der Reihe");
                            } else {
                                refreshStatus("ERROR!");
                            }
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    multiplayerActivity.startTimer();
                                    multiplayerActivity.makeInvisible();
                                }
                            });
                            startRound();
                            break;
                        case "changeacknowledged":
                            tries = 0;
                            enemyTries = 0;
                            if (state == A_isOnMove) {
                                state = B_isOnMove;
                            } else if (state == B_isOnMove) {
                                state = A_isOnMove;
                            }
                            if (state == A_isOnMove) {
                                refreshStatus("A ist an der Reihe");
                            } else if (state == B_isOnMove) {
                                refreshStatus("B ist an der Reihe");
                            } else {
                                refreshStatus("ERROR!");
                            }
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    multiplayerActivity.startTimer();
                                }
                            });
                            startRound();
                            break;
                        case "a":
                            state = A_isOnMove;
                            startPassiveTimer();
                        case "b":
                            state = B_isOnMove;
                            startPassiveTimer();
                        default:
                            try {
                                if (TEAM_A) {
                                    sendWord(wordDB.getRandomWord());
                                } else {
                                    Word temp = word1.getWordFromString(msg);
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

        exchangerThread = new Thread(exchanger);
        exchangerThread.start();

        try {
            if (TEAM_A)
                this.startTimer();
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
        try {
            dos.writeUTF("changeteam");
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
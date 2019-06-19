package com.example.androidapp;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.example.androidapp.bt.ConnectActivity;
import com.example.androidapp.database.Word;
import com.example.androidapp.database.WordDB;
import com.example.androidapp.database.WordDBImpl;
import com.example.androidapp.database.WordImpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;

public class Game implements Runnable {

    private InputStream is;
    private OutputStream os;
    private DataOutputStream dos;
    private DataInputStream dis;
    private String begin;
    private Random r;
    private final int A_isOnMove = 1;   // Team A ist immer das Server Team
    private final int B_isOnMove = 2;
    private final int RANDOM_BEGIN = 3;
    private final int NEXT_WORD = 4;
    private final int ROUND_COMPLETED = 0;
    private boolean TEAM_A = false;
    private int state;
    private String status;
    private Activity context;
    private TextView textView, wordView;
    private MultiplayerActivity multiplayerActivity;
    private WordDB wordDB;
    Word word1 = null;

    public Game(String begin, InputStream is, OutputStream os, MultiplayerActivity context, TextView textView, TextView textView2) {
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
        this.wordView=textView2;
        if (ConnectActivity.isServer) {
            TEAM_A = true;
        } else {
            TEAM_A = false;
        }

        ArrayList<String> forbiddenWord= new ArrayList<>();
        forbiddenWord.add("forbidden");
        forbiddenWord.add("forbidden");
        forbiddenWord.add("forbidden");
        forbiddenWord.add("forbidden");

        try {
            word1=new WordImpl("test", forbiddenWord);
            wordDB=new WordDBImpl();
            wordDB.add(word1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //multiplayerActivity.loadFromResource();
           // wordDB=multiplayerActivity.getWordDB();       // NullPointerException!!




        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Aushandeln des Beginner-Teams
    private void negotiate() throws IOException {
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
    // startet Spielrunde
    public void startRound() {
        try {
            if (TEAM_A) {
                Word word=wordDB.getRandomWord();
                refreshWord(word.getWord());
                if (state == A_isOnMove) {

                    sendWord(word);

                } else {
                    sendWord(word);
                    // int action = dis.readInt();
                }
            } else {
                refreshWord(getWord().getWord());
            }
        } catch (Exception e) {
            e.printStackTrace();
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
     * @param msg   Text, der angezeigt werden soll
     */
    public void refreshStatus(final String msg) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(msg);
            }
        });
    }


    private void refreshWord(final String word) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                wordView.setText(word);
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
        try {
            dos.writeInt(404);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if(dis.readInt()==404) {
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


}

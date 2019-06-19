package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp.bt.ConnectActivity;
import com.example.androidapp.database.Word;
import com.example.androidapp.database.WordDB;
import com.example.androidapp.database.WordDBImpl;
import com.example.androidapp.database.WordImpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MultiplayerActivity extends AppCompatActivity {

    private static TextView statusText, word;
    private TextView team, counter;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 10000; // Dauer einer Runde
    private boolean timerRunning;
    private Button skipWord, forbiddenWord, changeTeam;
    private Game game;
    private WordDB wordDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        this.skipWord = findViewById(R.id.buttonSkipWord);
        this.forbiddenWord = findViewById(R.id.buttonForbiddenWord);
        this.changeTeam=findViewById(R.id.buttonChangeTeam);
        changeTeam.setVisibility(View.INVISIBLE);

        changeTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.changeTeam();
                changeTeam.setVisibility(View.INVISIBLE);
            }
        });
        statusText = findViewById(R.id.textViewStatus);
        word = findViewById(R.id.textViewWord);
        this.team = findViewById(R.id.textViewTeam);
        this.counter = findViewById(R.id.textViewCounter);
        if (ConnectActivity.isServer) {
            team.setText("TEAM A");
        } else {
            team.setText("TEAM B");
        }

        Intent intent = getIntent();
        String beginner = intent.getStringExtra("Beginner");
        try {
            loadFromResource();

        } catch (Exception e) {
            e.printStackTrace();
        }

        game = new Game(beginner, ConnectActivity.is, ConnectActivity.os, this, statusText, word);
        Thread gameThread = new Thread(game);
        gameThread.start();
    }


    public void stopTimer() {
        countDownTimer.cancel();
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilliseconds = millisUntilFinished;
                updateCounter();
            }

            @Override
            public void onFinish() {
                counter.setText("0:00");
                timeLeftInMilliseconds = 10000; // Timer wird wieder zurueckgesetzt
                changeTeam.setVisibility(View.VISIBLE);
            }
        }.start();
    }


    private void updateCounter() {
        int minutes = (int) timeLeftInMilliseconds / 60000;
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;
        String timeLeftText = "" + minutes;
        timeLeftText = timeLeftText + ":";
        if (seconds < 10) {
            timeLeftText = timeLeftText + "0";
        }
        timeLeftText = timeLeftText + seconds;
        this.counter.setText(timeLeftText);
    }

    public void loadFromFile(String filename) throws Exception {

        this.wordDB = new WordDBImpl();

        FileInputStream fis = null;

        try {
            fis = openFileInput(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = br.readLine()) != null) {
                line.trim();
                String[] words = line.split(" "); //Trennzeichen: " "
                ArrayList<String> wordList = new ArrayList<String>();
                for (String e : words) {
                    wordList.add(e);
                }
                if (words.length != 4) {
                    throw new Exception("File corrupted!");
                } else {
                    Word word = new WordImpl(wordList.get(0), wordList.subList(1, wordList.size()));
                    wordDB.add(word);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            fis.close();
        }
    }

    public void loadFromResource() throws Exception {

        this.wordDB = new WordDBImpl();

        FileInputStream fis = null;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getClassLoader().getResourceAsStream("defaultDatabase.txt")));
            String line;
            while ((line = br.readLine()) != null) {
                line.trim();
                String[] words = line.split(" "); //Trennzeichen: " "
                ArrayList<String> wordList = new ArrayList<String>();
                for (String e : words) {
                    wordList.add(e);
                }
                if (words.length != 4) {
                    throw new Exception("File corrupted!");
                } else {
                    Word word = new WordImpl(wordList.get(0), wordList.subList(1, wordList.size()));
                    wordDB.add(word);
                    System.err.println(word.getWord());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            //fis.close();
        }
    }


    public void saveToFile(String filename) throws Exception {

        File file = new File("../../res" + filename);
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter bw = new BufferedWriter(osw);

        //Speichert alle Wörter im Format: Wort VerbWort1 VerbWort2 VerbWort3 VerbWort4\n
        for (int i = 0; i < wordDB.size(); i++) {
            Word w = wordDB.getList().get(i);
            bw.write(w.getWord() + " " + w.getForbiddenWords().get(0) + " " + w.getForbiddenWords().get(1) + " " + w.getForbiddenWords().get(2) + " " + w.getForbiddenWords().get(3));
            bw.newLine();
        }

        bw.close();
        osw.close();
        fos.close();

    }

    public WordDB getWordDB() {


        return this.wordDB;
    }

}

package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidapp.bt.ConnectActivity;

public class MultiplayerActivity extends AppCompatActivity {

    private TextView  team,counter;
    private static TextView statusText, word;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds=60000; // Dauer einer Runde
    private boolean timerRunning;
    private Button skipWord, forbiddenWord;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        this.skipWord=findViewById(R.id.buttonSkipWord);
        this.forbiddenWord=findViewById(R.id.buttonForbiddenWord);

        statusText = findViewById(R.id.textViewStatus);
        word=findViewById(R.id.textViewWord);
        this.team = findViewById(R.id.textViewTeam);
        this.counter=findViewById(R.id.textViewCounter);
        if (ConnectActivity.isServer) {
            team.setText("TEAM A");
        } else {
            team.setText("TEAM B");
        }

        Intent intent = getIntent();
        String beginner = intent.getStringExtra("Beginner");
        game = new Game(beginner, ConnectActivity.is, ConnectActivity.os, this, statusText, word);
        Thread gameThread = new Thread(game);
        gameThread.start();
}


    public void stopTimer(){
        countDownTimer.cancel();
    }
    public void startTimer(){
        countDownTimer=new CountDownTimer(timeLeftInMilliseconds,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilliseconds=millisUntilFinished;
                updateCounter();
            }

            @Override
            public void onFinish() {
                counter.setText("0:00");
                timeLeftInMilliseconds=10000; // Timer wird wieder zurueckgesetzt
                game.changeTeam(); // nach Ablauf der Zeit wechseln die Teams
            }
        }.start();
    }


    private void updateCounter(){
        int minutes= (int) timeLeftInMilliseconds / 60000;
        int seconds= (int) timeLeftInMilliseconds % 60000 / 1000;
        String timeLeftText= "" + minutes;
        timeLeftText=timeLeftText+ ":";
        if(seconds<10){
            timeLeftText=timeLeftText+"0";
        }
        timeLeftText=timeLeftText+seconds;
        this.counter.setText(timeLeftText);
    }


}

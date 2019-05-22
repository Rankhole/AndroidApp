package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.example.androidapp.bt.ConnectActivity;

public class MultiplayerActivity extends AppCompatActivity {

    private TextView  team,counter;
    private static TextView statusText;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds=60000;
    private boolean timerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        statusText = findViewById(R.id.textViewStatus);
        this.team = findViewById(R.id.textViewTeam);
        this.counter=findViewById(R.id.textViewCounter);
        if (ConnectActivity.isServer) {
            team.setText("TEAM A");
        } else {
            team.setText("TEAM B");
        }

        Intent intent = getIntent();
        String beginner = intent.getStringExtra("Beginner");
        final Game game = new Game(beginner, ConnectActivity.is, ConnectActivity.os, this, statusText);
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

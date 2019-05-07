package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MultiplayerActivity extends AppCompatActivity {

    private TextView statusText, team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        this.statusText = findViewById(R.id.textViewStatus);
        this.team = findViewById(R.id.textViewTeam);

        if (ConnectActivity.isServer) {
            team.setText("TEAM A");
        } else {
            team.setText("TEAM B");
        }

        Intent intent = getIntent();
        String beginner = intent.getStringExtra("Beginner");
        final Game game = new Game(beginner, ConnectActivity.is, ConnectActivity.os);
        Thread gameThread = new Thread(game);
        gameThread.start();

    }

}

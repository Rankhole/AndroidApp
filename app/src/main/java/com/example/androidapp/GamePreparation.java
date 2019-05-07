package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class GamePreparation extends AppCompatActivity {

    private CheckBox teamA, teamB, randomTeam;
    private Button start;
    private String begin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_preparation);
        this.teamA = findViewById(R.id.checkBoxA);
        this.teamB = findViewById(R.id.checkBoxB);
        this.randomTeam = findViewById(R.id.checkBoxRandom);
        this.start = findViewById(R.id.buttonStart);
        TextView textView=findViewById(R.id.textView3);

        textView.setText("Waehle aus welches Team beginnen soll:");

        this.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxes()){
                    Intent intent = new Intent(getApplicationContext(), MultiplayerActivity.class);
                    intent.putExtra("Beginner", begin);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "es muss genau eine Box ausgewaehlt werden", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean checkBoxes(){
        int count=0;
        if (teamA.isChecked()) {
            begin = "a";
            count++;
        }
        if (teamB.isChecked()) {
            begin = "b";
            count++;
        }
        if (randomTeam.isChecked()) {
            begin = "r";
            count++;
        }
        if(count==1){
            return true;
        }
        return false;
    }
}

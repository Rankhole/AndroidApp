package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    private Button multiplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        multiplayer = findViewById(R.id.button2);
        multiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConnectActivity.class);
                startActivity(intent);
            }
        });
    }

}

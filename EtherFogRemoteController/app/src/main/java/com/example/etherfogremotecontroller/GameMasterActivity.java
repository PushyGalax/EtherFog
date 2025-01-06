package com.example.etherfogremotecontroller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;

public class GameMasterActivity extends Activity {
    private static final int MODE = 0;
    private String SERVER_IP; // Replace with your server IP
    private int SERVER_PORT; // Replace with your server port

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_master);

        Button backButton = findViewById(R.id.backButton);
        Button setColorButton = findViewById(R.id.setColorButton);
        Button generateRandomNumberButton = findViewById(R.id.generateRandomNumberButton);
        Button gameStateButton = findViewById(R.id.gameStateButton);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            SERVER_IP = extras.getString("SERVER_IP");
            SERVER_PORT = extras.getInt("SERVER_PORT");
        }


        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameMasterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        setColorButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameMasterActivity.this, ColorPickerSetter.class);
            intent.putExtra("mode", MODE);
            intent.putExtra("SERVER_IP", SERVER_IP);
            intent.putExtra("SERVER_PORT", SERVER_PORT);
            intent.putExtra("CHARACTER_NAME", "");
            startActivity(intent);
        });

        generateRandomNumberButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameMasterActivity.this, RandomNumberActivity.class);
            intent.putExtra("SERVER_IP", SERVER_IP);
            intent.putExtra("SERVER_PORT", SERVER_PORT);
            startActivity(intent);
        });

        gameStateButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameMasterActivity.this, GameStateActivity.class);
            intent.putExtra("SERVER_IP", SERVER_IP);
            intent.putExtra("SERVER_PORT", SERVER_PORT);
            startActivity(intent);
        });
    }
}

package com.example.etherfogremotecontroller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class PlayerActivity extends Activity {
    private final int mode = 1;
    private String SERVER_IP;
    private int SERVER_PORT;
    private String characterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Button backButton = findViewById(R.id.backButton);
        Button setColorButton = findViewById(R.id.setColorButton);
        Button castSpellButton = findViewById(R.id.castSpellButton);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            characterName = extras.getString("CHARACTER_NAME");
            SERVER_IP = extras.getString("SERVER_IP");
            SERVER_PORT = extras.getInt("SERVER_PORT");
        }

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("SERVER_IP", SERVER_IP);
            intent.putExtra("SERVER_PORT", SERVER_PORT);
            startActivity(intent);
            finish();
        });

        setColorButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ColorPickerSetter.class);
            intent.putExtra("mode", mode);
            intent.putExtra("SERVER_IP", SERVER_IP);
            intent.putExtra("SERVER_PORT", SERVER_PORT);
            intent.putExtra("CHARACTER_NAME", characterName);
            startActivity(intent);
        });

        castSpellButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CastSpellActivity.class);
            intent.putExtra("SERVER_IP", SERVER_IP);
            intent.putExtra("SERVER_PORT", SERVER_PORT);
            startActivity(intent);
        });
    }
}

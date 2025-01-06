package com.example.etherfogremotecontroller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;

public class PlayerLoginActivity extends Activity {

    private TextInputEditText characterNameInput;
    private String SERVER_IP;
    private int SERVER_PORT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_login);

        characterNameInput = findViewById(R.id.characterNameInput);
        Button sendButton = findViewById(R.id.sendButton);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            SERVER_IP = extras.getString("SERVER_IP");
            SERVER_PORT = extras.getInt("SERVER_PORT");
        }

        sendButton.setOnClickListener(v -> {
            String characterName = characterNameInput.getText().toString();
            if (characterName.isEmpty()) {
                Toast.makeText(this, "Please enter a character name", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putExtra("CHARACTER_NAME", characterName);
            intent.putExtra("SERVER_IP", SERVER_IP);
            intent.putExtra("SERVER_PORT", SERVER_PORT);
            startActivity(intent);
            finish();
        });
    }
}
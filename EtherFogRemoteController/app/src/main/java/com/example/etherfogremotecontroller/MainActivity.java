package com.example.etherfogremotecontroller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends Activity {
    private TextInputEditText serverIpInput;
    private TextInputEditText serverPortInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serverIpInput = findViewById(R.id.serverIpInput);
        serverPortInput = findViewById(R.id.serverPortInput);
        Button gameMasterButton = findViewById(R.id.gameMasterButton);
        Button playerButton = findViewById(R.id.playerButton);

        gameMasterButton.setOnClickListener(v -> {
            String serverIp = serverIpInput.getText().toString();
            String serverPortString = serverPortInput.getText().toString();

            if (serverIp.isEmpty() || serverPortString.isEmpty()) {
                Toast.makeText(this, "Please enter both IP and Port", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int serverPort = Integer.parseInt(serverPortString);
                Intent intent = new Intent(this, GameMasterActivity.class);
                intent.putExtra("SERVER_IP", serverIp);
                intent.putExtra("SERVER_PORT", serverPort);
                startActivity(intent);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid port number", Toast.LENGTH_SHORT).show();
            }
        });

        playerButton.setOnClickListener(v -> {
            String serverIp = serverIpInput.getText().toString();
            String serverPortString = serverPortInput.getText().toString();

            if (serverIp.isEmpty() || serverPortString.isEmpty()) {
                Toast.makeText(this, "Please enter both IP and Port", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int serverPort = Integer.parseInt(serverPortString);
                Intent intent = new Intent(this, PlayerLoginActivity.class);
                intent.putExtra("SERVER_IP", serverIp);
                intent.putExtra("SERVER_PORT", serverPort);
                startActivity(intent);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid port number", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
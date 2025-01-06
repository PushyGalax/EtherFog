package com.example.etherfogremotecontroller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameStateActivity extends Activity {

    private Spinner characterSpinner;
    private List<String> characterNames;
    private String SERVER_IP;
    private int SERVER_PORT;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_state);

        Button backButton = findViewById(R.id.backButton);
        characterSpinner = findViewById(R.id.characterSpinner);
        Button sendButton = findViewById(R.id.sendButton);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            SERVER_IP = extras.getString("SERVER_IP");
            SERVER_PORT = extras.getInt("SERVER_PORT");
        }
        mainHandler = new Handler(Looper.getMainLooper());
        // Fetch character names from the database (replace with your actual logic)
        fetchCharacterNames();

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameMasterActivity.class);
            intent.putExtra("SERVER_IP", SERVER_IP);
            intent.putExtra("SERVER_PORT", SERVER_PORT);
            startActivity(intent);
            finish();
        });

        sendButton.setOnClickListener(v -> {
            String selectedCharacter = (String) characterSpinner.getSelectedItem();
            if (selectedCharacter != null) {
                NetworkManager nt = new NetworkManager(SERVER_IP, SERVER_PORT);
                nt.sendn(new NetworkManager.NetworkCallback() {
                    @Override
                    public void onSuccess(String s) {

                        // Handle success
                    }
                    @Override
                    public void onError(String message) {
                        // Handle error
                        Toast.makeText(GameStateActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }, "0 2 "+selectedCharacter);
                // Add code to send the selected character's name
            } else {
                Toast.makeText(this, "Please select a character", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchCharacterNames() {
        characterNames = new ArrayList<>();

        NetworkManager nt = new NetworkManager(SERVER_IP, SERVER_PORT);
        nt.send(new NetworkManager.NetworkCallback() {
            @Override
            public void onSuccess(String s) {
                System.out.println(s);
                characterNames.addAll(Arrays.asList(s.split(" ")));
                mainHandler.post(() -> {
                    // Create an ArrayAdapter to populate the Spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(GameStateActivity.this, android.R.layout.simple_spinner_item, characterNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    characterSpinner.setAdapter(adapter);});
            }
            @Override
            public void onError(String message) {
                // Handle error
                Toast.makeText(GameStateActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        },"0 3");
    }
}
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

public class CastSpellActivity extends Activity {
    private Spinner spellSpinner;
    private List<String> spellNames;
    private String SERVER_IP;
    private int SERVER_PORT;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_spell);

        Button backButton = findViewById(R.id.backButton);
        spellSpinner = findViewById(R.id.characterSpinner);
        Button sendButton = findViewById(R.id.sendButton);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            SERVER_IP = extras.getString("SERVER_IP");
            SERVER_PORT = extras.getInt("SERVER_PORT");
        }
        mainHandler = new Handler(Looper.getMainLooper());
        // Fetch character names from the database (replace with your actual logic)
        fetchSpellNames();

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putExtra("SERVER_IP", SERVER_IP);
            intent.putExtra("SERVER_PORT", SERVER_PORT);
            startActivity(intent);
            finish();
        });

        sendButton.setOnClickListener(v -> {
            String selectedSpell = (String) spellSpinner.getSelectedItem();
            if (selectedSpell != null) {
                NetworkManager nt = new NetworkManager(SERVER_IP, SERVER_PORT);
                nt.sendn(new NetworkManager.NetworkCallback() {
                    @Override
                    public void onSuccess(String s) {

                        // Handle success
                    }
                    @Override
                    public void onError(String message) {
                        // Handle error
                        Toast.makeText(CastSpellActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }, "1 1 "+ selectedSpell);
                // Add code to send the selected character's name
            } else {
                Toast.makeText(this, "Please select a spell", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchSpellNames() {
        spellNames = new ArrayList<>();

        NetworkManager nt = new NetworkManager(SERVER_IP, SERVER_PORT);
        nt.send(new NetworkManager.NetworkCallback() {
            @Override
            public void onSuccess(String s) {
                System.out.println(s);
                spellNames.addAll(Arrays.asList(s.split(";")));
                mainHandler.post(() -> {
                    // Create an ArrayAdapter to populate the Spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CastSpellActivity.this, android.R.layout.simple_spinner_item, spellNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spellSpinner.setAdapter(adapter);});
            }
            @Override
            public void onError(String message) {
                // Handle error
                Toast.makeText(CastSpellActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        },"1 2");
    }
}

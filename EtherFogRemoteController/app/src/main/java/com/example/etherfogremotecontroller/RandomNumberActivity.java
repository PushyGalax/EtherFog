package com.example.etherfogremotecontroller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.Random;

public class RandomNumberActivity extends Activity {

    private TextInputEditText maxRangeInput;
    private TextView resultTextView;
    private String SERVER_IP; // Replace with your server IP
    private int SERVER_PORT; // Replace with your server port

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_number);

        Button backButton = findViewById(R.id.backButton);
        maxRangeInput = findViewById(R.id.maxRangeInput);
        Button generateButton = findViewById(R.id.generateButton);
        resultTextView = findViewById(R.id.resultTextView);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            SERVER_IP = extras.getString("SERVER_IP");
            SERVER_PORT = extras.getInt("SERVER_PORT");
        }

        System.out.println(SERVER_IP);
        System.out.println(SERVER_PORT);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(RandomNumberActivity.this, GameMasterActivity.class);
            intent.putExtra("SERVER_IP", SERVER_IP);
            intent.putExtra("SERVER_PORT", SERVER_PORT);
            startActivity(intent);
            finish();
        });

        generateButton.setOnClickListener(v -> {
            String maxRangeString = maxRangeInput.getText().toString();
            if (!maxRangeString.isEmpty()) {

                try {
                    int maxRange = Integer.parseInt(maxRangeString);
                    if (maxRange > 0) {
                        NetworkManager nt = new NetworkManager(SERVER_IP, SERVER_PORT);
                        nt.send(new NetworkManager.NetworkCallback() {
                            @Override
                            public void onSuccess(String rec) {
                                System.out.println(rec);
                                resultTextView.setText(rec);
                            }
                            @Override
                            public void onError(String message) {
                                // new intent to load mainActivity
                                Intent intent = new Intent(RandomNumberActivity.this, MainActivity.class);
                                startActivity(intent);
                                //toast to inform user that failed to connect
                                Toast.makeText(RandomNumberActivity.this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                            }
                            }, "0 1 " + maxRange);



                    } else {
                        Toast.makeText(this, "Max range must be greater than 0", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid max range", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter a max range", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

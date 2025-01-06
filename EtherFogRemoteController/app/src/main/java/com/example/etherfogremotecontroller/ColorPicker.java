package com.example.etherfogremotecontroller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;

public class ColorPicker extends Activity {
    private SeekBar seekBarRed;
    private SeekBar seekBarGreen;
    private SeekBar seekBarBlue;
    private Button sendButton;
    private int colorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);

        // Initialize SeekBars
        seekBarRed = findViewById(R.id.redSeekBar);
        seekBarGreen = findViewById(R.id.greenSeekBar);
        seekBarBlue = findViewById(R.id.blueSeekBar);

        // Initialize Button
        sendButton = findViewById(R.id.sendButton);

        // Set max values for seekbars
        seekBarRed.setMax(255);
        seekBarGreen.setMax(255);
        seekBarBlue.setMax(255);

        // Set default values for seekbars
        seekBarRed.setProgress(0);
        seekBarGreen.setProgress(0);
        seekBarBlue.setProgress(0);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            colorId = extras.getInt("colorID",0);
        }

        // Set Button click listener
        sendButton.setOnClickListener(v -> {
            int redValue = seekBarRed.getProgress();
            int greenValue = seekBarGreen.getProgress();
            int blueValue = seekBarBlue.getProgress();

            // Create an Intent to start the SecondActivity
            Intent intent = new Intent();

            // Add the RGB values as extras to the Intent
            intent.putExtra("colorID", colorId);
            intent.putExtra("red", redValue);
            intent.putExtra("green", greenValue);
            intent.putExtra("blue", blueValue);

            // Start the SecondActivity
            setResult(RESULT_OK, intent);
            finish();
        });
    }

}

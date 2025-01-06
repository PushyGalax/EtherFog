package com.example.etherfogremotecontroller;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ColorPickerSetter extends Activity {
    private final List<View> colorPreviews = new ArrayList<>();
    private final ArrayList<Integer> colorValues = new ArrayList<>();

    private String SERVER_IP; // Replace with your server IP
    private int SERVER_PORT; // Replace with your server port
    private String characterName;
    private static final int NUM_COLORS = 14;
    private static final int REQUEST_CODE = 1;

    private static int mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_setter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mode = extras.getInt("mode");
            SERVER_IP = extras.getString("SERVER_IP");
            SERVER_PORT = extras.getInt("SERVER_PORT");
            characterName = extras.getString("CHARACTER_NAME");
        }

        // Initialize the submit button
        Button submitButton = findViewById(R.id.submitButton);

        // Initialize color previews list
        initializeColorPreviews();

        // Set click listeners for color previews
        setupColorPreviewClickListeners();

        //setupColorPreviewVisual();

        // Set click listener for the submit button
        submitButton.setOnClickListener(v -> {
            // Handle submit action here
            // Get the RGB value from each color preview space by " " in a String variable
            String send;
            if (characterName.isEmpty()) {
                send = mode + " 0 ";
            } else {
                send = mode + " 0 " + characterName + " ";
            }
            ///// change code par vrai valeur

            for (View colorPreview : colorPreviews) {
                //get each value separately, I want something like that 100 5 100 80 20 200 (where the first three are red, green and blue for the first led)
                int[] rgbVal = getRGBFromBackground(colorPreview);
                colorValues.add(rgbVal[0]);
                colorValues.add(rgbVal[1]);
                colorValues.add(rgbVal[2]);
                send += rgbVal[0] + " " + rgbVal[1] + " " + rgbVal[2] + " ";
            }
            //System.out.println(send);
            validate(send);
        });

        // Start AsyncTask to fetch color values from server

        NetworkManager net = new NetworkManager(SERVER_IP, SERVER_PORT);
        net.send(new NetworkManager.NetworkCallback() {
            @Override
            public void onSuccess(String rec) {
                System.out.println(rec);
                if (!Objects.equals(rec, "300")) {

                    String[] colorValueRecieve = rec.split(" ");
                    // Receive color values from the server
                    if (colorValueRecieve.length > 0) {
                        for (int i = 0; i < colorValueRecieve.length; i++) {
                            if (colorValueRecieve[i] != null) {
                                colorValues.add(Integer.parseInt(colorValueRecieve[i]));
                            } else {
                                // Handle case where server doesn't send enough values
                                colorValues.add(255); // Default to white
                            }
                        }
                    }
                    if (colorValues != null && colorValues.size() == NUM_COLORS*3) {
                        for (int i = 0; i < NUM_COLORS; i++) {
                            try {
                                colorPreviews.get(i).setBackgroundColor(android.graphics.Color.rgb(colorValues.get(i*3), colorValues.get(i*3+1), colorValues.get(i*3+2)));
                            } catch (IllegalArgumentException e) {
                                // Handle invalid color format
                                colorPreviews.get(i).setBackgroundColor(android.graphics.Color.WHITE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(String message) {
                for (int i = 0; i < NUM_COLORS*3; i++) {
                    colorValues.add(255); // Default to white
                }
            }
        }, "8");
    }

    public static int[] getRGBFromBackground(View view) {
        Drawable background = view.getBackground();
        if (background instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) background;
            int color = colorDrawable.getColor();
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
            return new int[]{red, green, blue};
        }
        return null; // Return null if background is not a ColorDrawable
    }

    private void validate(String message) {
        NetworkManager net = new NetworkManager(SERVER_IP, SERVER_PORT);
        net.sendn(new NetworkManager.NetworkCallback() {
            @Override
            public void onSuccess(String rec) {
                // new intent to load mainActivity
                if (mode == 0) {
                    Intent intent = new Intent(ColorPickerSetter.this, GameMasterActivity.class);
                    intent.putExtra("SERVER_IP", SERVER_IP);
                    intent.putExtra("SERVER_PORT", SERVER_PORT);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(ColorPickerSetter.this, PlayerActivity.class);
                    intent.putExtra("SERVER_IP", SERVER_IP);
                    intent.putExtra("SERVER_PORT", SERVER_PORT);
                    intent.putExtra("CHARACTER_NAME", characterName);
                    startActivity(intent);
                }
            }

            @Override
            public void onError(String message) {
                // new intent to load mainActivity
                Intent intent = new Intent(ColorPickerSetter.this, MainActivity.class);
                intent.putExtra("SERVER_IP", SERVER_IP);
                intent.putExtra("SERVER_PORT", SERVER_PORT);
                startActivity(intent);
                //toast to inform user that failed to connect
                Toast.makeText(ColorPickerSetter.this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
            }
            }, message);
    }

    private void initializeColorPreviews() {
        int[] colorPreviewIds = {
                R.id.colorPreview1, R.id.colorPreview2, R.id.colorPreview3, R.id.colorPreview4,
                R.id.colorPreview5, R.id.colorPreview6, R.id.colorPreview7, R.id.colorPreview8,
                R.id.colorPreview9, R.id.colorPreview10, R.id.colorPreview11, R.id.colorPreview12,
                R.id.colorPreview13, R.id.colorPreview14
        };

        for (int id : colorPreviewIds) {
            colorPreviews.add(findViewById(id));
        }
    }

    private void setupColorPreviewClickListeners() {
        for (int i = 0; i < colorPreviews.size(); i++) {
            View colorPreview = colorPreviews.get(i);
            final int colorId = i;

            colorPreview.setOnClickListener(v -> {
                Intent intent = new Intent(ColorPickerSetter.this, ColorPicker.class);
                intent.putExtra("colorID", colorId);
                startActivityForResult(intent, REQUEST_CODE);
            });
        }
    }

    /*private void setupColorPreviewVisual() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String colorId = extras.getString("colorID");
            int red = extras.getInt("red");
            int green = extras.getInt("green");
            int blue = extras.getInt("blue");
            int i = Integer.parseInt(colorId);
            View colorPreview = colorPreviews.get(i);
            colorPreview.setBackgroundColor(android.graphics.Color.rgb(red, green, blue));
        }

    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    int colorId = data.getIntExtra("colorID",0);
                    int red = data.getIntExtra("red",255);
                    int green = data.getIntExtra("green",255);
                    int blue = data.getIntExtra("blue",255);
                    View colorPreview = colorPreviews.get(colorId);
                    colorPreview.setBackgroundColor(android.graphics.Color.rgb(red, green, blue));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

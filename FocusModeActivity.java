package com.example.habitotracker;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FocusModeActivity extends AppCompatActivity {
    private TextView studyTimeTextView;
    private Button startStudyButton;
    private Button musicOption1Button;
    private Button musicOption2Button;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 1500000; // 25 minutes in ms

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_focus_mode); // Correct the typo in layout name

        // Initialize the views
        studyTimeTextView = findViewById(R.id.studyTimeTextView);
        startStudyButton = findViewById(R.id.startStudyButton);
        musicOption1Button = findViewById(R.id.musicOption1Button);
        musicOption2Button = findViewById(R.id.musicOption2Button);

        // Update the study time display
        updateStudyTimeTextView();

        // Start study session on button click
        startStudyButton.setOnClickListener(v -> startStudySession());

        // Play forest sound when musicOption1Button is clicked
        musicOption1Button.setOnClickListener(v -> playFocusMusic(1)); // Forest sound

        // Play rain sound when musicOption2Button is clicked
        musicOption2Button.setOnClickListener(v -> playFocusMusic(2)); // Rain sound
    }

    // Start the study session with a countdown timer
    private void startStudySession() {
        startStudyButton.setEnabled(false); // Disable button while session is active

        // Start countdown timer
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished; // Update the remaining time
                updateStudyTimeTextView(); // Update the time text view
            }

            @Override
            public void onFinish() {
                // When the timer finishes
                Toast.makeText(FocusModeActivity.this, "Study session finished!", Toast.LENGTH_SHORT).show();
                startStudyButton.setEnabled(true); // Re-enable the start button
            }
        };

        countDownTimer.start(); // Start the countdown
    }

    // Update the study time text view with the remaining time in minutes and seconds
    private void updateStudyTimeTextView() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String time = String.format("%02d:%02d", minutes, seconds); // Format time
        studyTimeTextView.setText("Study Time: " + time); // Set the time text
    }

    // Play selected focus music (forest or rain)
    private void playFocusMusic(int musicOption) {

        // Release any currently playing media
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        // Select the music based on the button clicked
        if (musicOption == 1) {
            mediaPlayer = MediaPlayer.create(this, R.raw.forest); // Forest sound
            Toast.makeText(this, "Playing Forest Sound", Toast.LENGTH_SHORT).show();
        } else if (musicOption == 2) {
            mediaPlayer = MediaPlayer.create(this, R.raw.rain); // Rain sound
            Toast.makeText(this, "Playing Rain Sound", Toast.LENGTH_SHORT).show();
        }

        // Start the selected music and loop it
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true); // Set music to loop
            mediaPlayer.start(); // Start the music
        }
    }

    // Release media resources when the activity stops
    @Override
    protected void onStop() {
        super.onStop();

        // Release the media player to avoid memory leaks
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}

package com.example.newapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class splash_Screen extends AppCompatActivity {

    private ImageView cloud, rock, map;
    private ImageView titre1, titre2;  // Added reference for titre2
    private MediaPlayer mediaPlayer;  // MediaPlayer for audio1
    private MediaPlayer mediaPlayer2; // MediaPlayer for audio2
    private MediaPlayer mediaPlayer3; // MediaPlayer for audio3

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen); // Ensure correct layout

        // Initialize references for ImageViews and TextView
        cloud = findViewById(R.id.cloudd);
        rock = findViewById(R.id.rock);
        map = findViewById(R.id.map);
        titre1 = findViewById(R.id.titre1);  // Initialize titre1
        titre2 = findViewById(R.id.titre2);  // Initialize titre2

        // Set initial visibility for all views
        cloud.setVisibility(View.INVISIBLE);
        rock.setVisibility(View.INVISIBLE);
        map.setVisibility(View.INVISIBLE);  // Keep map invisible initially
        titre1.setVisibility(View.INVISIBLE);  // Make titre1 invisible initially
        titre2.setVisibility(View.INVISIBLE);  // Make titre2 invisible initially

        // Initialize MediaPlayer and start audio
        mediaPlayer = MediaPlayer.create(this, R.raw.sound1);  // Load sound1.m4a
        mediaPlayer.start();  // Start the audio playback

        // Set listener to play audio2 after sound1 finishes
        mediaPlayer.setOnCompletionListener(mp -> {
            // Stop and release the first media player
            mp.release();

            // Initialize and play audio2
            mediaPlayer2 = MediaPlayer.create(splash_Screen.this, R.raw.sound2); // Load sound2.m4a
            mediaPlayer2.start(); // Start audio2 playback

            // Set listener to play audio3 after sound2 finishes
            mediaPlayer2.setOnCompletionListener(mp2 -> {
                // Stop and release the second media player
                mp2.release();

                // Initialize and play audio3
                mediaPlayer3 = MediaPlayer.create(splash_Screen.this, R.raw.sound3); // Load sound3.m4a
                mediaPlayer3.start(); // Start audio3 playback

                // Make map and titre2 visible when audio3 starts
                map.setVisibility(View.VISIBLE);  // Make map visible when audio3 starts
                titre2.setVisibility(View.VISIBLE);  // Make titre2 visible when audio3 starts
                zoomInOutAnimation(map);  // Start zoom-in/out animation on map

                // Once audio3 finishes, release the media player and enable the map click listener
                mediaPlayer3.setOnCompletionListener(mp3 -> {
                    mp3.release();
                    // Enable map click listener after audio3 finishes
                    map.setClickable(true);
                });
            });
        });

        // Delay and animate
        new Handler().postDelayed(() -> {
            // Fade in titre1 first
            fadeInAnimation(titre1);
        }, 2000); // Delay 2 seconds before starting animations

        // Set initial click listener to null (map should not be clickable initially)
        map.setClickable(false);

        // Set click listener for the map ImageView to go to SecondActivity
        map.setOnClickListener(v -> {
            // Release all MediaPlayer instances before navigating to another activity
            releaseMediaPlayer(mediaPlayer);
            releaseMediaPlayer(mediaPlayer2);
            releaseMediaPlayer(mediaPlayer3);

            // Navigate to SecondActivity
            Intent intent = new Intent(splash_Screen.this, secondpage.class);
            startActivity(intent);

            // Optionally, you can add this to finish the current activity, so the user can't return to the splash screen
            finish();
        });


    }

    private void fadeInAnimation(View view) {
        // Fade-in animation for titre1
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1000); // 1 second
        view.startAnimation(fadeIn);

        // Once fade-in is complete, make the cloud and rock visible with their animations
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                // Ensure titre1 is visible after its animation completes
                view.setVisibility(View.VISIBLE);  // Explicitly set to VISIBLE

                // Make cloud visible after 2 seconds
                new Handler().postDelayed(() -> {
                    cloud.setVisibility(View.VISIBLE);
                    Animation fadeInCloud = new AlphaAnimation(0, 1);
                    fadeInCloud.setDuration(1000);
                    cloud.startAnimation(fadeInCloud);
                }, 2000); // 2 seconds delay before showing cloud

                // Make rock visible after 4 seconds
                new Handler().postDelayed(() -> {
                    rock.setVisibility(View.VISIBLE);
                    Animation fadeInRock = new AlphaAnimation(0, 1);
                    fadeInRock.setDuration(1000);
                    rock.startAnimation(fadeInRock);
                }, 4000); // 4 seconds delay before showing rock
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    private void zoomInOutAnimation(final ImageView imageView) {
        // Zoom-In animation (scale up)
        ObjectAnimator zoomIn = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 1.5f);
        zoomIn.setDuration(1000); // Duration for zoom-in
        zoomIn.setInterpolator(new AccelerateDecelerateInterpolator());
        zoomIn.setRepeatCount(ObjectAnimator.INFINITE); // Repeat infinitely
        zoomIn.setRepeatMode(ObjectAnimator.REVERSE); // Reverse the animation after it completes

        ObjectAnimator zoomInY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 1.5f);
        zoomInY.setDuration(1000);
        zoomInY.setInterpolator(new AccelerateDecelerateInterpolator());
        zoomInY.setRepeatCount(ObjectAnimator.INFINITE); // Repeat infinitely
        zoomInY.setRepeatMode(ObjectAnimator.REVERSE); // Reverse the animation after it completes

        // Start the zoom-in animation
        zoomIn.start();
        zoomInY.start();
    }

    private void releaseMediaPlayer(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            try {
                // Check if the media player is playing before stopping
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            } catch (IllegalStateException e) {
                // Handle the exception if the mediaPlayer is not in a valid state
                e.printStackTrace();  // Optionally log this for debugging
            } finally {
                // Release the media player regardless of its state
                mediaPlayer.release();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop and release the media player when the activity is paused
        releaseMediaPlayer(mediaPlayer);
        releaseMediaPlayer(mediaPlayer2);
        releaseMediaPlayer(mediaPlayer3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the media player when the activity is destroyed
        releaseMediaPlayer(mediaPlayer);
        releaseMediaPlayer(mediaPlayer2);
        releaseMediaPlayer(mediaPlayer3);
    }
}

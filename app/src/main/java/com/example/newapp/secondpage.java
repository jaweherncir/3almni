package com.example.newapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class secondpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondpage);

        // Find the ImageView for microscop and kenz
        ImageView microscop = findViewById(R.id.microscop);
        final ImageView kenz = findViewById(R.id.kenz);

        // ImageViews that will appear after 6s of audio4
        final ImageView beb = findViewById(R.id.beb);
        final ImageView maghara = findViewById(R.id.maghara);
        final ImageView beb2 = findViewById(R.id.beb2);
        final ImageView said = findViewById(R.id.said);
        final ImageView beb3 = findViewById(R.id.beb3);
        final ImageView thou= findViewById(R.id.thou);



        // Find the ImageView for titre2
        final ImageView titre2 = findViewById(R.id.titre2);


        // Ensure that the ImageViews are not null
        if (microscop == null || kenz == null) {
            Log.e("AnimationError", "ImageViews not initialized");
            return;
        }

        // Rotation animation for microscop
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(microscop, "rotation", 0f, 45f, 45f, 0f);
        rotateAnimator.setDuration(2000);
        rotateAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        rotateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnimator.start();

        // Zoom-in and fade-in animation for kenz after 1s
        kenz.postDelayed(new Runnable() {
            @Override
            public void run() {
                kenz.setVisibility(ImageView.VISIBLE);
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(kenz, "scaleX", 0f, 1f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(kenz, "scaleY", 0f, 1f);
                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(kenz, "alpha", 0f, 1f);

                scaleX.setDuration(1000);
                scaleY.setDuration(1000);
                fadeIn.setDuration(1000);

                scaleX.start();
                scaleY.start();
                fadeIn.start();
            }
        }, 1000);

        // Start audio4 after 2 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final MediaPlayer mediaPlayer4 = MediaPlayer.create(secondpage.this, R.raw.audio4);
                mediaPlayer4.start();

                // Show beb and maghara after 6 seconds of audio4 starting
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (beb != null) beb.setVisibility(ImageView.VISIBLE);
                        if (maghara != null) maghara.setVisibility(ImageView.VISIBLE);
                    }
                }, 6000); // Delay of 6 seconds
                // Show beb and maghara after 6 seconds of audio4 starting
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (beb2 != null) beb2.setVisibility(ImageView.VISIBLE);
                        if (said != null) said.setVisibility(ImageView.VISIBLE);
                    }
                }, 8000); // Delay of 6 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (beb3 != null) beb3.setVisibility(ImageView.VISIBLE);
                        if (thou != null) thou.setVisibility(ImageView.VISIBLE);
                    }
                }, 12000); // Delay of 6 seconds

                // When audio4 finishes, start audio5
                mediaPlayer4.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        final MediaPlayer mediaPlayer5 = MediaPlayer.create(secondpage.this, R.raw.audio5);
                        mediaPlayer5.start();

                        // When audio5 finishes, show titre2
                        mediaPlayer5.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                titre2.setVisibility(ImageView.VISIBLE);
                                // Set click listener to navigate to MainActivity
                                titre2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(secondpage.this, MainActivity.class);
                                        startActivity(intent);
                                        finish(); // Optional: close current activity
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }, 2000); // Delay before audio4 starts
    }
}

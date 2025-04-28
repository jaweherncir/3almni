package com.example.newapp;

import android.content.ClipData;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class exercice1 extends AppCompatActivity {

    ImageView hajra3, hajra4, hajra5, essai1, hajra, avant;
    MediaPlayer mediaPlayer;
    boolean isAudio7Playing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercice1);

        hajra3 = findViewById(R.id.hajra3);
        hajra4 = findViewById(R.id.hajra4);
        hajra5 = findViewById(R.id.hajra5);
        essai1 = findViewById(R.id.essai1);
        hajra = findViewById(R.id.hajra);
        avant = findViewById(R.id.avant);

        // Hide all images initially
        hajra3.setVisibility(View.INVISIBLE);
        hajra4.setVisibility(View.INVISIBLE);
        hajra5.setVisibility(View.INVISIBLE);
        hajra.setVisibility(View.INVISIBLE);
        avant.setVisibility(View.INVISIBLE);

        // Play audio6 on activity start
        mediaPlayer = MediaPlayer.create(this, R.raw.audio6);
        mediaPlayer.start();

        // After audio6 ends, show draggable items
        mediaPlayer.setOnCompletionListener(mp -> {
            hajra3.setVisibility(View.VISIBLE);
            hajra4.setVisibility(View.VISIBLE);
            hajra5.setVisibility(View.VISIBLE);
        });

        hajra3.setOnTouchListener(dragTouchListener);
        hajra4.setOnTouchListener(dragTouchListener);
        hajra5.setOnTouchListener(dragTouchListener);

        essai1.setOnDragListener(dragListener);

        avant.setOnClickListener(v -> {
            if (!isAudio7Playing) {
                Intent intent = new Intent(exercice1.this, exercice1Step2.class);
                startActivity(intent);
            } else {
                Toast.makeText(exercice1.this, "الرجاء الانتظار حتى ينتهي الصوت", Toast.LENGTH_SHORT).show();
            }
        });
    }

    View.OnTouchListener dragTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDragAndDrop(data, shadowBuilder, v, 0);
                return true;
            }
            return false;
        }
    };

    View.OnDragListener dragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            if (event.getAction() == DragEvent.ACTION_DROP) {
                View draggedView = (View) event.getLocalState();

                if (draggedView.getId() == R.id.hajra5) {
                    Toast.makeText(exercice1.this, "إجابة صحيحة!", Toast.LENGTH_SHORT).show();

                    hajra5.setVisibility(View.INVISIBLE);
                    hajra.setVisibility(View.VISIBLE);
                    avant.setVisibility(View.VISIBLE);

                    // Stop any existing audio
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                    }

                    // Play audio7.m4a
                    mediaPlayer = MediaPlayer.create(exercice1.this, R.raw.audio7);
                    isAudio7Playing = true;
                    mediaPlayer.start();

                    mediaPlayer.setOnCompletionListener(mp -> {
                        isAudio7Playing = false;
                    });

                } else {
                    Toast.makeText(exercice1.this, "إجابة خاطئة", Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

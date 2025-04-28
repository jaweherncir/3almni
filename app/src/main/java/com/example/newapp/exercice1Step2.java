package com.example.newapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class exercice1Step2 extends AppCompatActivity {

    ImageView hajra4, hajra4p, avant2, essai2, arriere;
    ImageView hajra3, hajra9; // mauvaises réponses
    MediaPlayer mediaPlayer;
    boolean isAudio8Playing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exercice1_step2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        hajra4 = findViewById(R.id.hajra4);
        hajra4p = findViewById(R.id.hajra4p);
        avant2 = findViewById(R.id.avant2);
        essai2 = findViewById(R.id.essai2);
        hajra3 = findViewById(R.id.hajra3);
        hajra9 = findViewById(R.id.hajra9);
        arriere = findViewById(R.id.arriere);

        hajra4p.setVisibility(View.INVISIBLE);
        avant2.setVisibility(View.INVISIBLE);
        arriere.setVisibility(View.INVISIBLE);

        hajra4.setOnTouchListener(new MyTouchListener());
        hajra3.setOnTouchListener(new MyTouchListener());
        hajra9.setOnTouchListener(new MyTouchListener());

        essai2.setOnDragListener(new MyDragListener());

        avant2.setOnClickListener(v -> {
            if (!isAudio8Playing) {
                Intent intent = new Intent(exercice1Step2.this, Exercice1Step3.class);
                startActivity(intent);
            } else {
                Toast.makeText(exercice1Step2.this, "الرجاء الانتظار حتى ينتهي الصوت", Toast.LENGTH_SHORT).show();
            }
        });

        arriere.setOnClickListener(v -> {
            Intent intent = new Intent(exercice1Step2.this, exercice1.class);
            startActivity(intent);
        });
    }

    static final class MyTouchListener implements View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDragAndDrop(null, shadowBuilder, view, 0);
                return true;
            } else {
                return false;
            }
        }
    }

    class MyDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            if (event.getAction() == DragEvent.ACTION_DROP) {
                View draggedView = (View) event.getLocalState();
                if (draggedView.getId() == R.id.hajra4) {
                    Toast.makeText(exercice1Step2.this, "إجابة صحيحة!", Toast.LENGTH_SHORT).show();

                    hajra4.setVisibility(View.INVISIBLE);
                    hajra4p.setVisibility(View.VISIBLE);
                    avant2.setVisibility(View.VISIBLE);
                    arriere.setVisibility(View.VISIBLE);

                    // Stop any existing audio
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                    }

                    mediaPlayer = MediaPlayer.create(exercice1Step2.this, R.raw.audio7); // Assure-toi que audio8 existe
                    isAudio8Playing = true;
                    mediaPlayer.start();

                    mediaPlayer.setOnCompletionListener(mp -> {
                        isAudio8Playing = false;
                    });

                } else {
                    Toast.makeText(exercice1Step2.this, "إجابة خاطئة", Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

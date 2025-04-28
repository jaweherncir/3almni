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

public class Exercice1Step3 extends AppCompatActivity {

    ImageView hajra4, hajra3p, avant2, essai3, arriere;
    ImageView hajra3, hajra9;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exercice1_step3);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        hajra3 = findViewById(R.id.hajra3);
        hajra4 = findViewById(R.id.hajra4);
        hajra9 = findViewById(R.id.hajra9);
        hajra3p = findViewById(R.id.hajra3p);
        essai3 = findViewById(R.id.essai3);
        avant2 = findViewById(R.id.avant2);
        arriere = findViewById(R.id.arriere);

        hajra3p.setVisibility(View.INVISIBLE);
        avant2.setVisibility(View.INVISIBLE);
        arriere.setVisibility(View.INVISIBLE);

        hajra3.setOnTouchListener(new MyTouchListener());
        hajra4.setOnTouchListener(new MyTouchListener());
        hajra9.setOnTouchListener(new MyTouchListener());

        essai3.setOnDragListener(new MyDragListener());

        avant2.setOnClickListener(v -> {
            Intent intent = new Intent(Exercice1Step3.this, Exercice1Step4.class);
            startActivity(intent);
        });

        arriere.setOnClickListener(v -> {
            Intent intent = new Intent(Exercice1Step3.this, exercice1Step2.class);
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
            }
            return false;
        }
    }

    class MyDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DROP:
                    View draggedView = (View) event.getLocalState();
                    int draggedId = draggedView.getId();

                    if (draggedId == R.id.hajra3) {
                        Toast.makeText(Exercice1Step3.this, "إجابة صحيحة!", Toast.LENGTH_SHORT).show();
                        hajra3.setVisibility(View.INVISIBLE);
                        hajra3p.setVisibility(View.VISIBLE);
                        arriere.setVisibility(View.VISIBLE);

                        // ✅ Jouer l'audio et afficher "avant2" après la fin
                        playAudioAndShowButton();
                    } else {
                        Toast.makeText(Exercice1Step3.this, "إجابة خاطئة", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            return true;
        }
    }

    private void playAudioAndShowButton() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.audio7);
        mediaPlayer.start();

        // 🔁 Afficher le bouton "avant2" une fois l'audio terminé
        mediaPlayer.setOnCompletionListener(mp -> {
            avant2.setVisibility(View.VISIBLE);
            arriere.setVisibility(View.VISIBLE);
        });
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}

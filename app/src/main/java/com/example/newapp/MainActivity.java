package com.example.newapp;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private ImageView fish12, fish20, fish9, fish15, sandouk1, sandouk2, sandouk3, sandouk4;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialisation des poissons et des coffres
        fish12 = findViewById(R.id.fish12);
        fish20 = findViewById(R.id.fish20);
        fish9 = findViewById(R.id.fish9);
        fish15 = findViewById(R.id.fish15);
        sandouk1 = findViewById(R.id.sandouk1);
        sandouk2 = findViewById(R.id.sandouk2);
        sandouk3 = findViewById(R.id.sandouk3);
        sandouk4 = findViewById(R.id.sandouk4);

        // Initialement rendre sandouk2, sandouk3 et sandouk4 invisibles
        sandouk2.setVisibility(View.INVISIBLE);
        sandouk3.setVisibility(View.INVISIBLE);
        sandouk4.setVisibility(View.INVISIBLE);

        // Animation des poissons
        animateFish(fish12);
        animateFish(fish20);
        animateFish(fish9);
        animateFish(fish15);

        // Rendre les poissons glissables
        makeDraggable(fish12);
        makeDraggable(fish20);
        makeDraggable(fish9);
        makeDraggable(fish15);

        // Lecture de l'audio au démarrage
        mediaPlayer = MediaPlayer.create(this, R.raw.audio);
        mediaPlayer.start();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Fonction pour animer les poissons (mouvement vertical léger)
    private void animateFish(ImageView fish) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(fish, "translationY", -20f, 20f);
        animator.setDuration(2000);
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.start();
    }

    // Fonction pour rendre un poisson draggable et détecter le placement sur sandouk1, sandouk2, sandouk3 et sandouk4
    @SuppressLint("ClickableViewAccessibility")
    private void makeDraggable(ImageView fish) {
        fish.setOnTouchListener(new View.OnTouchListener() {
            private float dX, dY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        v.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        break;

                    case MotionEvent.ACTION_UP:
                        // Vérifier si fish9 est sur sandouk1
                        if (isViewOverlapping(v, sandouk1)) {
                            if (v.getId() == R.id.fish9) {
                                Toast.makeText(MainActivity.this, "✅ جواب صحيح! أحسنت! \uD83C\uDF89", Toast.LENGTH_SHORT).show();
                                // Rendre sandouk2 visible lorsque fish9 est sur sandouk1
                                sandouk2.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(MainActivity.this, "❌ جواب خطأ! كرر المحاولة. \uD83D\uDD04", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // Vérifier si fish12 est sur sandouk2
                        if (isViewOverlapping(v, sandouk2)) {
                            if (v.getId() == R.id.fish12) {
                                Toast.makeText(MainActivity.this, "✅ جواب صحيح! أحسنت! \uD83C\uDF89", Toast.LENGTH_SHORT).show();
                                // Rendre sandouk3 visible lorsque fish12 est sur sandouk2
                                sandouk3.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(MainActivity.this, "❌ جواب خطأ! كرر المحاولة. \uD83D\uDD04", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // Vérifier si fish15 est sur sandouk3
                        if (isViewOverlapping(v, sandouk3)) {
                            if (v.getId() == R.id.fish15) {
                                Toast.makeText(MainActivity.this, "✅ جواب صحيح! أحسنت! \uD83C\uDF89", Toast.LENGTH_SHORT).show();
                                // Rendre sandouk4 visible lorsque fish15 est sur sandouk3
                                sandouk4.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(MainActivity.this, "❌ جواب خطأ! كرر المحاولة. \uD83D\uDD04", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // Vérifier si fish20 est sur sandouk4
                        if (isViewOverlapping(v, sandouk4)) {
                            if (v.getId() == R.id.fish20) {
                                Toast.makeText(MainActivity.this, "✅ جواب صحيح! أحسنت! \uD83C\uDF89", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "❌ جواب خطأ! كرر المحاولة. \uD83D\uDD04", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    // Vérifier si deux vues se chevauchent
    private boolean isViewOverlapping(View view1, View view2) {
        int[] location1 = new int[2];
        int[] location2 = new int[2];

        view1.getLocationOnScreen(location1);
        view2.getLocationOnScreen(location2);

        int view1X = location1[0];
        int view1Y = location1[1];
        int view1Width = view1.getWidth();
        int view1Height = view1.getHeight();

        int view2X = location2[0];
        int view2Y = location2[1];
        int view2Width = view2.getWidth();
        int view2Height = view2.getHeight();

        return view1X < view2X + view2Width &&
                view1X + view1Width > view2X &&
                view1Y < view2Y + view2Height &&
                view1Y + view1Height > view2Y;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}

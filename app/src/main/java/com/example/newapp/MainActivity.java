package com.example.newapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private ImageView fish9, fish12, fish15, fish20, sandouk1, sandouk2, sandouk3, sandouk4, keygame, next;
    private ImageView fish91, fish121, fish151, fish201;
    private boolean[] sandoukValidated = new boolean[4];


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initializeViews();
        setInitialVisibility();
        setupNextButton();

        mediaPlayer = MediaPlayer.create(this, R.raw.audio);
        if (mediaPlayer != null) {
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mp -> {
                sandouk1.setVisibility(View.VISIBLE);
                animateFish(fish9);
                setupDraggableFishes();
            });
        }
    }

    private void initializeViews() {
        fish9 = findViewById(R.id.fish9);
        fish12 = findViewById(R.id.fish12);
        fish15 = findViewById(R.id.fish15);
        fish20 = findViewById(R.id.fish20);
        fish91 = findViewById(R.id.fish91);
        fish121 = findViewById(R.id.fish121);
        fish151 = findViewById(R.id.fish151);
        fish201 = findViewById(R.id.fish201);
        sandouk1 = findViewById(R.id.sandouk1);
        sandouk2 = findViewById(R.id.sandouk2);
        sandouk3 = findViewById(R.id.sandouk3);
        sandouk4 = findViewById(R.id.sandouk4);
        keygame = findViewById(R.id.keygame);
        next = findViewById(R.id.next);
    }

    private void setInitialVisibility() {
        fish91.setVisibility(View.INVISIBLE);
        fish121.setVisibility(View.INVISIBLE);
        fish151.setVisibility(View.INVISIBLE);
        fish201.setVisibility(View.INVISIBLE);
        keygame.setVisibility(View.INVISIBLE);
        sandouk1.setVisibility(View.INVISIBLE);
        sandouk2.setVisibility(View.INVISIBLE);
        sandouk3.setVisibility(View.INVISIBLE);
        sandouk4.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);
    }

    private void animateFish(ImageView fish) {
        ObjectAnimator moveX = ObjectAnimator.ofFloat(fish, "translationX", 0f, 300f);
        ObjectAnimator moveY = ObjectAnimator.ofFloat(fish, "translationY", 0f, 50f);

        moveX.setDuration(3000);
        moveX.setRepeatCount(ObjectAnimator.INFINITE);
        moveX.setRepeatMode(ObjectAnimator.REVERSE);

        moveY.setDuration(3000);
        moveY.setRepeatCount(ObjectAnimator.INFINITE);
        moveY.setRepeatMode(ObjectAnimator.REVERSE);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(moveX, moveY);
        animatorSet.start();
    }

    private void setupDraggableFishes() {
        makeDraggable(fish9, fish91, sandouk1, sandouk2, fish12, 0);
        makeDraggable(fish12, fish121, sandouk2, sandouk3, fish15, 1);
        makeDraggable(fish15, fish151, sandouk3, sandouk4, fish20, 2);
        makeDraggable(fish20, fish201, sandouk4, keygame, null, 3);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void makeDraggable(ImageView fish, ImageView nextFish, View correctSandouk, View nextSandouk, ImageView nextVisibleFish, int sandoukIndex) {
        fish.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.setTag(new float[]{v.getX() - event.getRawX(), v.getY() - event.getRawY()});
                    break;
                case MotionEvent.ACTION_MOVE:
                    float[] d = (float[]) v.getTag();
                    v.setX(event.getRawX() + d[0]);
                    v.setY(event.getRawY() + d[1]);
                    break;
                case MotionEvent.ACTION_UP:
                    if (isViewOverlapping(v, correctSandouk)) {
                        v.setVisibility(View.INVISIBLE);
                        if (nextFish != null) nextFish.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, "🐟 السمكة في مكانها الصحيح!", Toast.LENGTH_SHORT).show();

                        sandoukValidated[sandoukIndex] = true;

                        if (nextSandouk != null) {
                            nextSandouk.setVisibility(View.VISIBLE);
                            if (nextVisibleFish != null) nextVisibleFish.setVisibility(View.VISIBLE);
                        }

                        if (nextSandouk == keygame) {
                            keygame.setVisibility(View.VISIBLE);
                            next.setVisibility(View.VISIBLE); // Afficher le bouton Next
                            Toast.makeText(MainActivity.this, "🔑 لقد وجدت المفتاح!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "❌ المكان غير صحيح!", Toast.LENGTH_SHORT).show();
                        v.setX(0);
                        v.setY(0);
                    }
                    break;
            }
            return true;
        });
    }

    private boolean isViewOverlapping(View view1, View view2) {
        if (view1 == null || view2 == null) return false;

        int[] loc1 = new int[2];
        int[] loc2 = new int[2];
        view1.getLocationOnScreen(loc1);
        view2.getLocationOnScreen(loc2);

        int left1 = loc1[0], top1 = loc1[1], right1 = left1 + view1.getWidth(), bottom1 = top1 + view1.getHeight();
        int left2 = loc2[0], top2 = loc2[1], right2 = left2 + view2.getWidth(), bottom2 = top2 + view2.getHeight();

        return right1 > left2 && left1 < right2 && bottom1 > top2 && top1 < bottom2;
    }

    private void setupNextButton() {
        next.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, tableMultiplication.class);
            startActivity(intent);
        });
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

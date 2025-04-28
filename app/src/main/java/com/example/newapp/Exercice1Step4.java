package com.example.newapp;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
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

public class Exercice1Step4 extends AppCompatActivity {

    ImageView hajra2, hajra3, hajra4p, hajra3p, essai4, beb, keyHajra, arriere, nextpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exercice1_step4);

        // Gérer les paddings système
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialiser les vues
        hajra2 = findViewById(R.id.hajra2); // mauvaise réponse
        hajra3 = findViewById(R.id.hajra3); // bonne réponse
        hajra4p = findViewById(R.id.hajra4p); // image qui s'affiche si bonne réponse
        hajra3p = findViewById(R.id.hajra3p); // image qui s'affiche si bonne réponse (optionnelle)
        essai4 = findViewById(R.id.essai4);   // zone de drop
        beb = findViewById(R.id.beb);        // bouton pour passer à l'étape précédente
        keyHajra = findViewById(R.id.keyHajra); // élément à animer
        arriere = findViewById(R.id.arriere); // bouton pour revenir à l'étape précédente
        nextpage = findViewById(R.id.nextpage); // bouton pour aller vers MainActivity

        // Cacher les éléments au départ
        hajra4p.setVisibility(View.INVISIBLE);
        hajra3p.setVisibility(View.INVISIBLE);
        keyHajra.setVisibility(View.INVISIBLE);
        arriere.setVisibility(View.INVISIBLE);

        // Activer le drag sur les éléments
        hajra2.setOnTouchListener(new MyTouchListener());
        hajra3.setOnTouchListener(new MyTouchListener());

        // Activer le drop sur la cible
        essai4.setOnDragListener(new MyDragListener());

        // Retourner à l'étape précédente
        arriere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Exercice1Step4.this, Exercice1Step3.class);
                startActivity(intent);
            }
        });

        // Aller à la page principale (MainActivity) en cliquant sur l'image nextpage
        nextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Exercice1Step4.this, MainActivity.class);
                startActivity(intent);
            }
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
                        Toast.makeText(Exercice1Step4.this, "إجابة صحيحة!", Toast.LENGTH_SHORT).show();
                        hajra3.setVisibility(View.INVISIBLE);
                        hajra3p.setVisibility(View.VISIBLE);

                    } else if (draggedId == R.id.hajra2) {
                        Toast.makeText(Exercice1Step4.this, "إجابة صحيحة!", Toast.LENGTH_SHORT).show();
                        hajra2.setVisibility(View.INVISIBLE);
                        hajra4p.setVisibility(View.VISIBLE);
                        keyHajra.setVisibility(View.VISIBLE);
                        arriere.setVisibility(View.VISIBLE);

                        // Lancer la rotation pendant 4 secondes
                        startRotationAnimation(keyHajra);
                    } else {
                        Toast.makeText(Exercice1Step4.this, "إجابة خاطئة", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            return true;
        }
    }

    // Méthode pour démarrer la rotation
    private void startRotationAnimation(View view) {
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        rotateAnimator.setDuration(4000); // 4 secondes
        rotateAnimator.setRepeatCount(0); // Pas de répétition
        rotateAnimator.start();
    }
}

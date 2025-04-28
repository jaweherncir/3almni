package com.example.newapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class tableMultiplication extends AppCompatActivity {

    private final int SIZE = 10;
    private View[][] cells = new View[SIZE][SIZE];
    private GridLayout gridLayout;
    private Button btnCheck, btnRegenerate;
    private ImageView boulR, boulV, feuv, feur, next;
    private MediaPlayer mediaPlayer8, mediaPlayer9;
    private boolean audioFinished = false; // Pour activer les boutons après audio9

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_multiplication);

        gridLayout = findViewById(R.id.multiplicationGrid);
        btnCheck = findViewById(R.id.btnCheck);
        btnRegenerate = findViewById(R.id.btnRegenerate);

        boulR = findViewById(R.id.boulR);
        boulV = findViewById(R.id.boulV);
        feuv = findViewById(R.id.feuv);
        feur = findViewById(R.id.feur);
        next = findViewById(R.id.next); // ⬅️ Assurez-vous que l'ID existe dans le layout

        // Initialement invisibles
        boulR.setVisibility(View.INVISIBLE);
        boulV.setVisibility(View.INVISIBLE);
        feuv.setVisibility(View.INVISIBLE);
        feur.setVisibility(View.INVISIBLE);
        btnCheck.setVisibility(View.INVISIBLE);
        btnRegenerate.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);

        generateMultiplicationTable();

        next.setOnClickListener(v -> {
            Intent intent = new Intent(tableMultiplication.this, LastStepExercice1.class); // Replace with your next activity
            startActivity(intent);
        });
        // Jouer audio8
        mediaPlayer8 = MediaPlayer.create(this, R.raw.audio8);
        mediaPlayer8.setOnCompletionListener(mp -> {
            feuv.setVisibility(View.VISIBLE);
            feur.setVisibility(View.VISIBLE);

            // Jouer audio9
            mediaPlayer9 = MediaPlayer.create(this, R.raw.audio9);
            mediaPlayer9.setOnCompletionListener(mp2 -> {
                audioFinished = true;
                btnCheck.setVisibility(View.VISIBLE);
                btnRegenerate.setVisibility(View.VISIBLE);
            });
            mediaPlayer9.start();
        });

        mediaPlayer8.start();

        btnCheck.setOnClickListener(v -> checkUserAnswers());

        btnRegenerate.setOnClickListener(v -> {
            generateMultiplicationTable();
            boulR.setVisibility(View.INVISIBLE);
            boulV.setVisibility(View.INVISIBLE);
            next.setVisibility(View.INVISIBLE);


        });
    }


    private void generateMultiplicationTable() {
        Random random = new Random();
        gridLayout.removeAllViews();
        gridLayout.setColumnCount(SIZE);
        gridLayout.setRowCount(SIZE);

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                View view;

                if (row == 0 && col == 0) {
                    TextView cell = new TextView(this);
                    cell.setText("*");
                    cell.setGravity(Gravity.CENTER);
                    cell.setTextSize(14);
                    cell.setBackgroundColor(Color.TRANSPARENT);
                    setCellBorder(cell);
                    view = cell;
                } else if (row == 0) {
                    TextView cell = new TextView(this);
                    cell.setText(String.valueOf(col));
                    cell.setGravity(Gravity.CENTER);
                    cell.setTextSize(14);
                    cell.setBackgroundColor(Color.TRANSPARENT);
                    setCellBorder(cell);
                    view = cell;
                } else if (col == 0) {
                    TextView cell = new TextView(this);
                    cell.setText(String.valueOf(row));
                    cell.setGravity(Gravity.CENTER);
                    cell.setTextSize(14);
                    cell.setBackgroundColor(Color.TRANSPARENT);
                    setCellBorder(cell);
                    view = cell;
                } else {
                    int correctValue = row * col;
                    if (random.nextInt(100) < 20) {
                        TextView cell = new TextView(this);
                        cell.setText(String.valueOf(correctValue));
                        cell.setGravity(Gravity.CENTER);
                        cell.setTextSize(14);
                        cell.setBackgroundColor(Color.TRANSPARENT);
                        setCellBorder(cell);
                        view = cell;
                    } else {
                        EditText cell = new EditText(this);
                        cell.setGravity(Gravity.CENTER);
                        cell.setEms(2);
                        cell.setTextSize(14);
                        cell.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
                        cell.setBackgroundColor(Color.TRANSPARENT);
                        setCellBorder(cell);
                        view = cell;
                    }
                }

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 100;
                params.height = 100;
                params.setMargins(2, 2, 2, 2);
                view.setLayoutParams(params);

                gridLayout.addView(view);
                cells[row][col] = view;
            }
        }
    }

    private void setCellBorder(View view) {
        ShapeDrawable border = new ShapeDrawable(new RectShape());
        border.getPaint().setColor(Color.BLACK);
        border.getPaint().setStrokeWidth(2);
        border.getPaint().setStyle(android.graphics.Paint.Style.STROKE);
        view.setBackground(border);
    }

    private void checkUserAnswers() {
        boolean allCorrect = true;

        for (int row = 1; row < SIZE; row++) {
            for (int col = 1; col < SIZE; col++) {
                int correct = row * col;
                View cell = cells[row][col];

                if (cell instanceof EditText) {
                    String userInput = ((EditText) cell).getText().toString().trim();
                    try {
                        int val = Integer.parseInt(userInput);
                        if (val != correct) {
                            cell.setBackgroundColor(Color.parseColor("#FF0000"));
                            boulR.setVisibility(View.VISIBLE);
                            boulV.setVisibility(View.INVISIBLE);
                            allCorrect = false;
                        } else {
                            cell.setBackgroundColor(Color.parseColor("#008000"));
                        }
                    } catch (NumberFormatException e) {
                        allCorrect = false;
                    }
                }
            }
        }

        if (allCorrect) {
            boulR.setVisibility(View.INVISIBLE);
            boulV.setVisibility(View.VISIBLE);
            next.setVisibility(View.VISIBLE); // Montrer le bouton "next"
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer8 != null) {
            mediaPlayer8.release();
        }
        if (mediaPlayer9 != null) {
            mediaPlayer9.release();
        }
    }
}

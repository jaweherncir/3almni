package com.example.newapp;

import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class tableMultiplication extends AppCompatActivity {

    private final int SIZE = 10; // 0 to 9
    private View[][] cells = new View[SIZE][SIZE];
    private GridLayout gridLayout;
    private Button btnCheck, btnRegenerate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_multiplication);

        gridLayout = findViewById(R.id.multiplicationGrid);
        btnCheck = findViewById(R.id.btnCheck);
        btnRegenerate = findViewById(R.id.btnRegenerate);

        generateMultiplicationTable();

        btnCheck.setOnClickListener(v -> {
            checkUserAnswers();
        });

        btnRegenerate.setOnClickListener(v -> {
            generateMultiplicationTable();
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
                    cell.setBackgroundColor(Color.TRANSPARENT);  // Fond transparent
                    setCellBorder(cell);  // Ajouter une bordure noire
                    view = cell;
                } else if (row == 0) {
                    TextView cell = new TextView(this);
                    cell.setText(String.valueOf(col));
                    cell.setGravity(Gravity.CENTER);
                    cell.setTextSize(14);
                    cell.setBackgroundColor(Color.TRANSPARENT);  // Fond transparent
                    setCellBorder(cell);  // Ajouter une bordure noire
                    view = cell;
                } else if (col == 0) {
                    TextView cell = new TextView(this);
                    cell.setText(String.valueOf(row));
                    cell.setGravity(Gravity.CENTER);
                    cell.setTextSize(14);
                    cell.setBackgroundColor(Color.TRANSPARENT);  // Fond transparent
                    setCellBorder(cell);  // Ajouter une bordure noire
                    view = cell;
                } else {
                    int correctValue = row * col;

                    if (random.nextInt(100) < 20) {
                        TextView cell = new TextView(this);
                        cell.setText(String.valueOf(correctValue));
                        cell.setGravity(Gravity.CENTER);
                        cell.setTextSize(14);
                        cell.setBackgroundColor(Color.TRANSPARENT);  // Fond transparent
                        setCellBorder(cell);  // Ajouter une bordure noire
                        view = cell;
                    } else {
                        EditText cell = new EditText(this);
                        cell.setGravity(Gravity.CENTER);
                        cell.setEms(2);
                        cell.setTextSize(14);
                        cell.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
                        cell.setBackgroundColor(Color.TRANSPARENT);  // Fond transparent
                        setCellBorder(cell);  // Ajouter une bordure noire
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

    // Méthode pour ajouter une bordure noire à chaque cellule
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
                            cell.setBackgroundColor(Color.parseColor("#FF0000")); // rouge clair
                            allCorrect = false;
                        } else {
                            cell.setBackgroundColor(Color.parseColor("#008000")); // vert clair
                        }
                    } catch (NumberFormatException e) {
                        cell.setBackgroundColor(Color.parseColor("#FF0000")); // rouge clair
                        allCorrect = false;
                    }
                }
            }
        }


    }
}

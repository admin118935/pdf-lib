package com.guruclasses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView tvProgress, tvQNum, tvSubjectTag, tvQuestion;
    private RadioButton rbA, rbB, rbC, rbD;
    private RadioGroup rgOptions;
    private MaterialButton btnNext;
    private LinearLayout llFilter;

    private DatabaseHelper db;
    private List<String[]> questions = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;
    private String currentSubject = "All";
    private String[] subjects = {"All", "Maths", "GK", "Hindi", "English", "Science", "Biology", "Chemistry", "Physics"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        db = new DatabaseHelper(this);
        tvProgress = findViewById(R.id.tvProgress);
        tvQNum = findViewById(R.id.tvQNum);
        tvSubjectTag = findViewById(R.id.tvSubjectTag);
        tvQuestion = findViewById(R.id.tvQuestion);
        rbA = findViewById(R.id.rbA);
        rbB = findViewById(R.id.rbB);
        rbC = findViewById(R.id.rbC);
        rbD = findViewById(R.id.rbD);
        rgOptions = findViewById(R.id.rgOptions);
        btnNext = findViewById(R.id.btnNext);
        llFilter = findViewById(R.id.llSubjectFilter);

        setupFilters();
        loadQuestions();

        btnNext.setOnClickListener(v -> nextQuestion());
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
    }

    private void setupFilters() {
        for (String s : subjects) {
            MaterialButton btn = new MaterialButton(this,
                    null, com.google.android.material.R.attr.materialButtonOutlinedStyle);
            btn.setText(s);
            btn.setTextSize(12);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);
            btn.setLayoutParams(params);
            btn.setOnClickListener(v -> {
                currentSubject = s;
                currentIndex = 0;
                score = 0;
                loadQuestions();
            });
            llFilter.addView(btn);
        }
    }

    private void loadQuestions() {
        questions.clear();
        Cursor cursor = db.getQuizBySubject(currentSubject);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                questions.add(new String[]{
                        cursor.getString(cursor.getColumnIndexOrThrow("subject")),
                        cursor.getString(cursor.getColumnIndexOrThrow("question")),
                        cursor.getString(cursor.getColumnIndexOrThrow("option_a")),
                        cursor.getString(cursor.getColumnIndexOrThrow("option_b")),
                        cursor.getString(cursor.getColumnIndexOrThrow("option_c")),
                        cursor.getString(cursor.getColumnIndexOrThrow("option_d")),
                        cursor.getString(cursor.getColumnIndexOrThrow("correct_answer"))
                });
            }
            cursor.close();
        }
        if (questions.isEmpty()) {
            Toast.makeText(this, "Koi question nahi mila", Toast.LENGTH_SHORT).show();
        } else {
            showQuestion();
        }
    }

    private void showQuestion() {
        if (currentIndex >= questions.size()) {
            showResult();
            return;
        }
        String[] q = questions.get(currentIndex);
        tvProgress.setText((currentIndex + 1) + "/" + questions.size());
        tvQNum.setText("Q." + (currentIndex + 1));
        tvSubjectTag.setText(q[0]);
        tvQuestion.setText(q[1]);
        rbA.setText("A.  " + q[2]);
        rbB.setText("B.  " + q[3]);
        rbC.setText("C.  " + q[4]);
        rbD.setText("D.  " + q[5]);
        rgOptions.clearCheck();

        boolean isLast = (currentIndex == questions.size() - 1);
        btnNext.setText(isLast ? "Quiz Khatam Karein" : "Next Question");
    }

    private void nextQuestion() {
        int selectedId = rgOptions.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Pehle jawab chunein", Toast.LENGTH_SHORT).show();
            return;
        }

        String selected;
        if (selectedId == R.id.rbA) selected = "A";
        else if (selectedId == R.id.rbB) selected = "B";
        else if (selectedId == R.id.rbC) selected = "C";
        else selected = "D";

        String correct = questions.get(currentIndex)[6];
        if (selected.equals(correct)) score++;

        currentIndex++;
        if (currentIndex >= questions.size()) {
            showResult();
        } else {
            showQuestion();
        }
    }

    private void showResult() {
        SharedPreferences prefs = getSharedPreferences("GuruClasses", MODE_PRIVATE);
        int studentId = prefs.getInt("student_id", -1);
        if (studentId != -1) {
            db.saveResult(studentId, currentSubject, score, questions.size());
        }

        Intent intent = new Intent(this, QuizResultActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("total", questions.size());
        intent.putExtra("subject", currentSubject);
        startActivity(intent);
        finish();
    }
}

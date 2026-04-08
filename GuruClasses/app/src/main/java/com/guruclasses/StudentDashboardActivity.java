package com.guruclasses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class StudentDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        SharedPreferences prefs = getSharedPreferences("GuruClasses", MODE_PRIVATE);
        String name = prefs.getString("student_name", "Student");

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        tvWelcome.setText("Namaste, " + name + "!");

        // Card clicks
        CardView cardVideos = findViewById(R.id.cardVideos);
        CardView cardQuiz = findViewById(R.id.cardQuiz);
        CardView cardNotes = findViewById(R.id.cardNotes);
        CardView cardLive = findViewById(R.id.cardLive);
        CardView cardProgress = findViewById(R.id.cardProgress);
        CardView cardSubjects = findViewById(R.id.cardSubjects);

        cardVideos.setOnClickListener(v ->
                startActivity(new Intent(this, VideoLecturesActivity.class)));
        cardQuiz.setOnClickListener(v ->
                startActivity(new Intent(this, QuizActivity.class)));
        cardNotes.setOnClickListener(v ->
                startActivity(new Intent(this, NotesActivity.class)));
        cardLive.setOnClickListener(v ->
                startActivity(new Intent(this, LiveClassActivity.class)));
        cardProgress.setOnClickListener(v ->
                startActivity(new Intent(this, ProgressActivity.class)));
        cardSubjects.setOnClickListener(v ->
                startActivity(new Intent(this, SubjectsActivity.class)));

        // Logout
        findViewById(R.id.ivLogout).setOnClickListener(v -> logout());
    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Kya aap logout karna chahte hain?")
                .setPositiveButton("Haan", (d, w) -> {
                    getSharedPreferences("GuruClasses", MODE_PRIVATE).edit().clear().apply();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                })
                .setNegativeButton("Nahi", null)
                .show();
    }
}

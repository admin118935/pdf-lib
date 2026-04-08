package com.guruclasses;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminDashboardActivity extends AppCompatActivity {

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        db = new DatabaseHelper(this);

        // Stats
        ((TextView) findViewById(R.id.tvTotalStudents)).setText(String.valueOf(db.getStudentCount()));
        ((TextView) findViewById(R.id.tvTotalVideos)).setText(String.valueOf(db.getVideoCount()));
        ((TextView) findViewById(R.id.tvTotalQuiz)).setText(String.valueOf(db.getQuizCount()));

        // Cards
        CardView cardStudents = findViewById(R.id.cardManageStudents);
        CardView cardVideos = findViewById(R.id.cardManageVideos);
        CardView cardQuiz = findViewById(R.id.cardManageQuiz);
        CardView cardNotes = findViewById(R.id.cardManageNotes);
        CardView cardLive = findViewById(R.id.cardManageLive);
        CardView cardPass = findViewById(R.id.cardChangePass);

        cardStudents.setOnClickListener(v ->
                startActivity(new Intent(this, AdminStudentsActivity.class)));
        cardVideos.setOnClickListener(v ->
                startActivity(new Intent(this, AdminAddVideoActivity.class)));
        cardQuiz.setOnClickListener(v ->
                startActivity(new Intent(this, AdminAddQuizActivity.class)));
        cardNotes.setOnClickListener(v ->
                startActivity(new Intent(this, AdminAddNotesActivity.class)));
        cardLive.setOnClickListener(v ->
                startActivity(new Intent(this, AdminLiveClassActivity.class)));
        cardPass.setOnClickListener(v ->
                startActivity(new Intent(this, ChangePasswordActivity.class)));

        // Logout
        findViewById(R.id.ivLogout).setOnClickListener(v -> logout());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh stats
        ((TextView) findViewById(R.id.tvTotalStudents)).setText(String.valueOf(db.getStudentCount()));
        ((TextView) findViewById(R.id.tvTotalVideos)).setText(String.valueOf(db.getVideoCount()));
        ((TextView) findViewById(R.id.tvTotalQuiz)).setText(String.valueOf(db.getQuizCount()));
    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Admin logout karna chahte hain?")
                .setPositiveButton("Haan", (d, w) -> {
                    getSharedPreferences("GuruClasses", MODE_PRIVATE).edit().clear().apply();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                })
                .setNegativeButton("Nahi", null)
                .show();
    }
}

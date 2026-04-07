package com.guruclasses;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class QuizResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 0);

        TextView tvScore = findViewById(R.id.tvScore);
        TextView tvCorrect = findViewById(R.id.tvCorrect);
        TextView tvWrong = findViewById(R.id.tvWrong);
        TextView tvMessage = findViewById(R.id.tvMessage);
        TextView tvEmoji = findViewById(R.id.tvEmoji);

        tvScore.setText(score + " / " + total);
        tvCorrect.setText(String.valueOf(score));
        tvWrong.setText(String.valueOf(total - score));

        double percent = total > 0 ? (double) score / total * 100 : 0;
        if (percent >= 80) {
            tvEmoji.setText("\uD83C\uDFC6");
            tvMessage.setText("Shabash! Bahut zyada achha!");
        } else if (percent >= 60) {
            tvEmoji.setText("\uD83D\uDE0A");
            tvMessage.setText("Achha kiya! Aur mehnat karein");
        } else if (percent >= 40) {
            tvEmoji.setText("\uD83D\uDE10");
            tvMessage.setText("Theek hai, aur padhna hai");
        } else {
            tvEmoji.setText("\uD83D\uDCDA");
            tvMessage.setText("Himmat rakhein, aur padhein!");
        }

        MaterialButton btnRetry = findViewById(R.id.btnRetry);
        MaterialButton btnHome = findViewById(R.id.btnHome);

        btnRetry.setOnClickListener(v -> {
            startActivity(new Intent(this, QuizActivity.class));
            finish();
        });
        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(this, StudentDashboardActivity.class));
            finish();
        });
    }
}

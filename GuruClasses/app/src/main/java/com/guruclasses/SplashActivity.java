package com.guruclasses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("GuruClasses", MODE_PRIVATE);
            String role = prefs.getString("role", "");
            int studentId = prefs.getInt("student_id", -1);

            if (role.equals("admin")) {
                startActivity(new Intent(this, AdminDashboardActivity.class));
            } else if (role.equals("student") && studentId != -1) {
                startActivity(new Intent(this, StudentDashboardActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
        }, 2000);
    }
}

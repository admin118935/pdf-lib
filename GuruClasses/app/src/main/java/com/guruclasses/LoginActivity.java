package com.guruclasses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etPassword;
    private MaterialButton btnLogin, btnRegister;
    private TabLayout tabLayout;
    private DatabaseHelper db;
    private boolean isAdminMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        tabLayout = findViewById(R.id.tabLayout);

        tabLayout.addTab(tabLayout.newTab().setText("Student Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Admin Login"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isAdminMode = tab.getPosition() == 1;
                android.widget.TextView tvTitle = findViewById(R.id.tvLoginTitle);
                if (isAdminMode) {
                    tvTitle.setText("Admin Login");
                    btnRegister.setVisibility(android.view.View.GONE);
                } else {
                    tvTitle.setText("Student Login");
                    btnRegister.setVisibility(android.view.View.VISIBLE);
                }
                etUsername.setText("");
                etPassword.setText("");
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        btnLogin.setOnClickListener(v -> doLogin());
        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void doLogin() {
        String username = etUsername.getText() != null ? etUsername.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username aur password daalein", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isAdminMode) {
            if (db.validateAdmin(username, password)) {
                SharedPreferences.Editor editor = getSharedPreferences("GuruClasses", MODE_PRIVATE).edit();
                editor.putString("role", "admin");
                editor.apply();
                startActivity(new Intent(this, AdminDashboardActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Admin ID ya password galat hai", Toast.LENGTH_SHORT).show();
            }
        } else {
            Cursor cursor = db.validateStudent(username, password);
            if (cursor != null && cursor.moveToFirst()) {
                int studentId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
                cursor.close();

                SharedPreferences.Editor editor = getSharedPreferences("GuruClasses", MODE_PRIVATE).edit();
                editor.putString("role", "student");
                editor.putInt("student_id", studentId);
                editor.putString("student_name", name);
                editor.apply();

                startActivity(new Intent(this, StudentDashboardActivity.class));
                finish();
            } else {
                if (cursor != null) cursor.close();
                Toast.makeText(this, "Username ya password galat hai", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

package com.guruclasses;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etMobile, etUsername, etPassword, etClass;
    private MaterialButton btnRegister, btnBack;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);
        etFullName = findViewById(R.id.etFullName);
        etMobile = findViewById(R.id.etMobile);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etClass = findViewById(R.id.etClass);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);

        btnRegister.setOnClickListener(v -> doRegister());
        btnBack.setOnClickListener(v -> finish());
    }

    private void doRegister() {
        String fullName = getText(etFullName);
        String mobile = getText(etMobile);
        String username = getText(etUsername);
        String password = getText(etPassword);
        String className = getText(etClass);

        if (fullName.isEmpty() || mobile.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Sabhi fields bharein", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mobile.length() != 10) {
            Toast.makeText(this, "Mobile number 10 digit ka hona chahiye", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 4) {
            Toast.makeText(this, "Password kam se kam 4 characters ka hona chahiye", Toast.LENGTH_SHORT).show();
            return;
        }
        if (db.isUsernameExists(username)) {
            Toast.makeText(this, "Yeh username pehle se liya gaya hai, koi aur chunein", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.registerStudent(fullName, mobile, username, password, className)) {
            Toast.makeText(this, "Registration safal! Ab login karein", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Registration mein kuch galat hua, dobara koshish karein", Toast.LENGTH_SHORT).show();
        }
    }

    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }
}

package com.guruclasses;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextInputEditText etOldPassword, etNewPassword, etConfirmPassword;
    private MaterialButton btnChange;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        db = new DatabaseHelper(this);
        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnChange = findViewById(R.id.btnChange);

        btnChange.setOnClickListener(v -> changePassword());
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
    }

    private void changePassword() {
        String oldPass = getText(etOldPassword);
        String newPass = getText(etNewPassword);
        String confirmPass = getText(etConfirmPassword);

        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "Sabhi fields bharein", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "Naya password match nahi kar raha", Toast.LENGTH_SHORT).show();
            return;
        }
        if (newPass.length() < 4) {
            Toast.makeText(this, "Password kam se kam 4 characters ka hona chahiye", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.updateAdminPassword(oldPass, newPass)) {
            Toast.makeText(this, "Password badal gaya!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Purana password galat hai", Toast.LENGTH_SHORT).show();
        }
    }

    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }
}

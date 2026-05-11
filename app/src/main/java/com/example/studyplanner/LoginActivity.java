package com.example.studyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText edtPhone;
    Button btnSendOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtPhone = findViewById(R.id.edtPhone);
        btnSendOtp = findViewById(R.id.btnSendOtp);

        btnSendOtp.setOnClickListener(v -> {
            String phone = edtPhone.getText().toString().trim();

            if (phone.isEmpty()) {
                edtPhone.setError("Enter phone number");
                return;
            }

            if (phone.length() != 10) {
                edtPhone.setError("Enter valid 10-digit number");
                return;
            }

            Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
            intent.putExtra("phone", "+91" + phone);
            startActivity(intent);
        });
    }
}
package com.example.studyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {

    EditText edtOtp;
    Button btnVerifyOtp;
    TextView txtPhone;
    ImageView btnBack;   // ✅ added

    FirebaseAuth auth;
    FirebaseFirestore firestore;

    String phoneNumber;
    String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        edtOtp = findViewById(R.id.edtOtp);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        txtPhone = findViewById(R.id.txtPhone);
        btnBack = findViewById(R.id.btnBack);   // ✅ added

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        phoneNumber = getIntent().getStringExtra("phone");

        if (phoneNumber != null) {
            txtPhone.setText("OTP sent to " + phoneNumber);
        }

        // ✅ Back button action
        btnBack.setOnClickListener(v -> finish());

        sendOtp(phoneNumber);

        btnVerifyOtp.setOnClickListener(v -> {
            String otp = edtOtp.getText().toString().trim();

            if (otp.isEmpty()) {
                edtOtp.setError("Enter OTP");
                return;
            }

            if (verificationId == null) {
                Toast.makeText(OtpActivity.this, "OTP not sent yet", Toast.LENGTH_SHORT).show();
                return;
            }

            PhoneAuthCredential credential =
                    PhoneAuthProvider.getCredential(verificationId, otp);

            signInWithCredential(credential);
        });
    }

    private void sendOtp(String phone) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                                signInWithCredential(credential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(OtpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                super.onCodeSent(s, token);
                                verificationId = s;
                                Toast.makeText(OtpActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        String uid = auth.getCurrentUser().getUid();

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("phone", phoneNumber);
                        map.put("createdAt", System.currentTimeMillis());

                        firestore.collection("users")
                                .document(uid)
                                .set(map)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(OtpActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(OtpActivity.this, DashboardActivity.class));
                                    finishAffinity();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(OtpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show()
                                );

                    } else {
                        Toast.makeText(OtpActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
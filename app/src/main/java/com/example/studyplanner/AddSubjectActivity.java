package com.example.studyplanner;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddSubjectActivity extends AppCompatActivity {

    EditText edtSubjectName;
    Button btnSaveSubject;
    ImageView btnBack;

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        edtSubjectName = findViewById(R.id.edtSubjectName);
        btnSaveSubject = findViewById(R.id.btnSaveSubject);
        btnBack = findViewById(R.id.btnBack);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            userId = auth.getCurrentUser().getUid();
        }

        btnBack.setOnClickListener(v -> finish());

        btnSaveSubject.setOnClickListener(v -> {
            String subjectName = edtSubjectName.getText().toString().trim();

            if (subjectName.isEmpty()) {
                edtSubjectName.setError("Enter subject name");
                return;
            }

            if (userId == null) {
                Toast.makeText(AddSubjectActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            String docId = firestore.collection("subjects").document().getId();

            SubjectModel subject = new SubjectModel(docId, userId, subjectName);

            firestore.collection("subjects")
                    .document(docId)
                    .set(subject)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(AddSubjectActivity.this, "Subject Added", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(AddSubjectActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        });
    }
}
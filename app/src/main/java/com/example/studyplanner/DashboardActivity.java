package com.example.studyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    RecyclerView recyclerSubjects;
    ImageView btnAddSubject, btnLogout;

    ArrayList<SubjectModel> subjectList;
    SubjectAdapter adapter;

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recyclerSubjects = findViewById(R.id.recyclerSubjects);
        btnAddSubject = findViewById(R.id.btnAddSubject);
        btnLogout = findViewById(R.id.btnLogout);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
            return;
        }

        userId = auth.getCurrentUser().getUid();

        subjectList = new ArrayList<>();
        adapter = new SubjectAdapter(this, subjectList);

        recyclerSubjects.setLayoutManager(new LinearLayoutManager(this));
        recyclerSubjects.setAdapter(adapter);

        btnAddSubject.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, AddSubjectActivity.class)));

        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            startActivity(intent);
            finishAffinity();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSubjects();
    }

    private void loadSubjects() {
        firestore.collection("subjects")
                .whereEqualTo("userID", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    subjectList.clear();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        SubjectModel model = doc.toObject(SubjectModel.class);
                        subjectList.add(model);
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(DashboardActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
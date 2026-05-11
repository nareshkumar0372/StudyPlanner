package com.example.studyplanner;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class SubjectTasksActivity extends AppCompatActivity {

    TextView txtSubjectTitle;
    EditText edtTaskTitle, edtDeadline;
    Button btnAddTask;
    ImageView btnBack;
    RecyclerView recyclerTasks;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    String userId, subjectID, subjectName;

    ArrayList<TaskModel> taskList;
    TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_tasks);

        txtSubjectTitle = findViewById(R.id.txtSubjectTitle);
        edtTaskTitle = findViewById(R.id.edtTaskTitle);
        edtDeadline = findViewById(R.id.edtDeadline);
        btnAddTask = findViewById(R.id.btnAddTask);
        btnBack = findViewById(R.id.btnBack);
        recyclerTasks = findViewById(R.id.recyclerTasks);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        userId = auth.getCurrentUser().getUid();

        subjectID = getIntent().getStringExtra("subjectID");
        subjectName = getIntent().getStringExtra("subjectName");

        if (subjectName != null) {
            txtSubjectTitle.setText(subjectName + " Tasks");
        }

        taskList = new ArrayList<>();
        adapter = new TaskAdapter(this, taskList);

        recyclerTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerTasks.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        btnAddTask.setOnClickListener(v -> addTask());

        loadTasks();
    }

    private void addTask() {
        String title = edtTaskTitle.getText().toString().trim();
        String deadline = edtDeadline.getText().toString().trim();

        if (title.isEmpty()) {
            edtTaskTitle.setError("Enter task title");
            return;
        }

        if (deadline.isEmpty()) {
            edtDeadline.setError("Enter deadline");
            return;
        }

        if (subjectID == null || subjectID.isEmpty()) {
            Toast.makeText(this, "Invalid subject", Toast.LENGTH_SHORT).show();
            return;
        }

        String taskId = firestore.collection("tasks").document().getId();

        TaskModel task = new TaskModel(taskId, userId, subjectID, title, deadline, false);

        firestore.collection("tasks")
                .document(taskId)
                .set(task)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(SubjectTasksActivity.this, "Task Added", Toast.LENGTH_SHORT).show();
                    edtTaskTitle.setText("");
                    edtDeadline.setText("");
                    loadTasks();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(SubjectTasksActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void loadTasks() {
        if (subjectID == null || subjectID.isEmpty()) {
            Toast.makeText(this, "Invalid subject", Toast.LENGTH_SHORT).show();
            return;
        }

        firestore.collection("tasks")
                .whereEqualTo("subjectID", subjectID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    taskList.clear();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        TaskModel model = doc.toObject(TaskModel.class);
                        taskList.add(model);
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(SubjectTasksActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
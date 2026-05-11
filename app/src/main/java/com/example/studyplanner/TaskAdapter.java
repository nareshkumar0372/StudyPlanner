package com.example.studyplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    Context context;
    ArrayList<TaskModel> list;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public TaskAdapter(Context context, ArrayList<TaskModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskModel model = list.get(position);

        holder.checkTask.setText(model.getTitle() + "  |  " + model.getDeadline());
        holder.checkTask.setChecked(model.isCompleted());

        holder.checkTask.setOnCheckedChangeListener((buttonView, isChecked) ->
                firestore.collection("tasks")
                        .document(model.getTaskID())
                        .update("completed", isChecked)
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkTask = itemView.findViewById(R.id.checkTask);
        }
    }
}
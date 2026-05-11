package com.example.studyplanner;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    Context context;
    ArrayList<SubjectModel> list;

    public SubjectAdapter(Context context, ArrayList<SubjectModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        SubjectModel model = list.get(position);
        holder.txtSubjectName.setText(model.getSubjectName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SubjectTasksActivity.class);
            intent.putExtra("subjectID", model.getSubjectId());
            intent.putExtra("subjectName", model.getSubjectName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView txtSubjectName;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSubjectName = itemView.findViewById(R.id.txtSubjectName);
        }
    }
}
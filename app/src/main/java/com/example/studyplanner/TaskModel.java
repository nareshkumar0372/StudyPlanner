package com.example.studyplanner;

public class TaskModel {
    private String taskID;
    private String userID;
    private String subjectID;
    private String title;
    private String deadline;
    private boolean completed;

    public TaskModel() {
    }

    public TaskModel(String taskID, String userID, String subjectID, String title, String deadline, boolean completed) {
        this.taskID = taskID;
        this.userID = userID;
        this.subjectID = subjectID;
        this.title = title;
        this.deadline = deadline;
        this.completed = completed;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
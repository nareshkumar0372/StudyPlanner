package com.example.studyplanner;

public class SubjectModel {
    private String subjectId;
    private String userID;
    private String subjectName;

    public SubjectModel() {
    }

    public SubjectModel(String subjectId, String userID, String subjectName) {
        this.subjectId = subjectId;
        this.userID = userID;
        this.subjectName = subjectName;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
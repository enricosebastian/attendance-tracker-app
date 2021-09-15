package com.mobdeve.s15.group1.attendancetrackerstudent;
/*
    This class is the representation of a Student. It is used when performing
    Firetore database operations.
 */
public class StudentModel {
    private int imageId;
    private String studentName;
    private String username;


    public StudentModel(String studentName, String username, int imageId) {
        this.studentName = studentName;
        this.username = username;
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

package com.mobdeve.s15.group1.attendancetrackerteacher;

public class Student {

    private int imageId;
    private String studentName;
    private String username;


    public Student(String studentName, String username, int imageId) {
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

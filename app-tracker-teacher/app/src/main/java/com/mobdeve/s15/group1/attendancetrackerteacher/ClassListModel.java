package com.mobdeve.s15.group1.attendancetrackerteacher;

public class ClassListModel {

    private String courseCode, email, idNumber, sectionCode;

    public ClassListModel(String courseCode, String email, String idNumber, String sectionCode) {
        this.courseCode = courseCode;
        this.email = email;
        this.idNumber = idNumber;
        this.sectionCode = sectionCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }
}

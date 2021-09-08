package com.mobdeve.s15.group1.attendancetrackerstudent;

public class CourseModel {

    private String courseCode, sectionCode;

    public CourseModel() {

    }

    public CourseModel(String courseCode, String sectionCode) {
        this.courseCode = courseCode;
        this.sectionCode = sectionCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

}

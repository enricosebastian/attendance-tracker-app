package com.mobdeve.s15.group1.attendancetrackerstudent;

public class CourseModel {

    private String courseCode, courseName, handledBy, sectionCode;
    private boolean isPublished;

    public CourseModel() {

    }

    public CourseModel(String courseCode, String courseName, String handledBy, boolean isPublished, String sectionCode) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.handledBy = handledBy;
        this.sectionCode = sectionCode;
        this.isPublished = isPublished;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(String handledBy) {
        this.handledBy = handledBy;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public boolean getIsPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }
}

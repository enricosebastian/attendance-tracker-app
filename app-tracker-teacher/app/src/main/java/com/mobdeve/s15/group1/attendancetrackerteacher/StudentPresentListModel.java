package com.mobdeve.s15.group1.attendancetrackerteacher;

public class StudentPresentListModel {

    String courseCode, meetingCode, sectionCode, studentAttended;
    boolean isPresent;

    public StudentPresentListModel(String courseCode, String meetingCode, String sectionCode, String studentAttended, boolean isPresent) {
        this.courseCode = courseCode;
        this.meetingCode = meetingCode;
        this.sectionCode = sectionCode;
        this.studentAttended = studentAttended;
        this.isPresent = isPresent;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getMeetingCode() {
        return meetingCode;
    }

    public void setMeetingCode(String meetingCode) {
        this.meetingCode = meetingCode;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public String getStudentAttended() {
        return studentAttended;
    }

    public void setStudentAttended(String studentAttended) {
        this.studentAttended = studentAttended;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }
}

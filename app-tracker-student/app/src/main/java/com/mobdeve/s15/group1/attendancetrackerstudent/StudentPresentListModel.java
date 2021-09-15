package com.mobdeve.s15.group1.attendancetrackerstudent;
/*
    This class is the representation of a Present Student. It is used when performing
    Firetore database operations.
 */
public class StudentPresentListModel {

    String courseCode, meetingCode, sectionCode, studentAttended, firstName, lastName;
    boolean isPresent;

    public StudentPresentListModel(String courseCode, String meetingCode, String sectionCode, String studentAttended, String firstName, String lastName, boolean isPresent) {
        this.courseCode = courseCode;
        this.meetingCode = meetingCode;
        this.sectionCode = sectionCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentAttended = studentAttended;
        this.isPresent = isPresent;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

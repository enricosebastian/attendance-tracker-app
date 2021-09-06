package com.mobdeve.s15.group1.attendancetrackerteacher;

import java.util.Date;

public class MeetingModel {
    private String courseCode, sectionCode, meetingCode;
    Date meetingStart;
    boolean isOpen;
    int studentCount;

    public MeetingModel() {

    }

    public MeetingModel(String courseCode, boolean isOpen, String meetingCode, Date meetingStart, String sectionCode, int studentCount) {
        this.courseCode = courseCode;
        this.sectionCode = sectionCode;
        this.meetingCode = meetingCode;
        this.meetingStart = meetingStart;
        this.isOpen = isOpen;
        this.studentCount = studentCount;
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

    public String getMeetingCode() {
        return meetingCode;
    }

    public void setMeetingCode(String meetingCode) {
        this.meetingCode = meetingCode;
    }

    public Date getMeetingStart() {
        return meetingStart;
    }

    public void setMeetingStart(Date meetingStart) {
        this.meetingStart = meetingStart;
    }

    public boolean getIsOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }
}

package com.mobdeve.s15.group1.attendancetrackerteacher;

import java.util.Date;

public class MeetingModel {
    String courseCode, sectionCode, meetingCode; //keys could be int? or long?
    Date date;
    int studentsPresent;


    public MeetingModel(String courseCode, String sectionCode, String meetingCode, Date date, int studentsPresent) {
        this.courseCode = courseCode;
        this.sectionCode = sectionCode;
        this.meetingCode = meetingCode;
        this.date = date;
        this.studentsPresent = studentsPresent;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStudentsPresent() {
        return studentsPresent;
    }

    public void setStudentsPresent(int studentsPresent) {
        this.studentsPresent = studentsPresent;
    }
}

package com.mobdeve.s15.group1.attendancetrackerteacher;

import java.util.Date;

public class MeetingModel {
    String _id, classKey; //keys could be int? or long?
    Date date;
    int studentsPresent;


    public MeetingModel(String _id, String classKey, Date date, int studentsPresent) {
        this._id = _id;
        this.classKey = classKey;
        this.date = date;
        this.studentsPresent = studentsPresent;
    }

    public String getClassKey() {
        return classKey;
    }

    public void setClassKey(String classKey) {
        this.classKey = classKey;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

package com.mobdeve.s15.group1.attendancetrackerteacher;

public class MeetingModel {
    String month, dayName;
    int dayNumber, year, studentsPresent;

    //TODO: add meeting code

    public MeetingModel(String month, int dayNumber, int year, String dayName, int studentsPresent) {
        this.month = month;
        this.dayName = dayName;
        this.dayNumber = dayNumber;
        this.year = year;
        this.studentsPresent = studentsPresent;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getStudentsPresent() {
        return studentsPresent;
    }

    public void setStudentsPresent(int studentsPresent) {
        this.studentsPresent = studentsPresent;
    }
}

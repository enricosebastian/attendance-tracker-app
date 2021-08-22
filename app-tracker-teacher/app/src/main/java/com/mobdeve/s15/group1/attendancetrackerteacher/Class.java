package com.mobdeve.s15.group1.attendancetrackerteacher;

public class Class {

    private String classCode, sectionCode;

    public Class(String classCode, String sectionCode) {
        this.classCode = classCode;
        this.sectionCode = sectionCode;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }
}

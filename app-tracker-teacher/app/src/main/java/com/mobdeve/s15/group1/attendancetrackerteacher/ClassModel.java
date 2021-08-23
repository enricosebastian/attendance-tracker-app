package com.mobdeve.s15.group1.attendancetrackerteacher;

public class ClassModel {

    //what should a class have?
    //class code, section code, unique identifier ONLY
    //MOBDEVE, S13, MOBILE DEVELOPMENT, 00001, PUBLISHED

    private String _id, classCode, sectionCode, className;
    private boolean isPublished;

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    public ClassModel(String _id, String classCode, String sectionCode, String className, boolean isPublished) {
        this._id = _id;
        this.classCode = classCode;
        this.sectionCode = sectionCode;
        this.className = className;
        this.isPublished = isPublished;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

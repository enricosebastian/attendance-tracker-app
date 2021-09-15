package com.mobdeve.s15.group1.attendancetrackerteacher;
/*
    This class is the representation of a Course Request. It is used when performing
    Firetore database operations.
 */
public class CourseRequestModel {
    private String courseCode, firstName, idNumber, lastName, sectionCode;

    public CourseRequestModel(String courseCode, String firstName, String idNumber, String lastName, String sectionCode) {
        this.courseCode = courseCode;
        this.firstName = firstName;
        this.idNumber = idNumber;
        this.lastName = lastName;
        this.sectionCode = sectionCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }
}

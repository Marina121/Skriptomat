package ba.sum.fpmoz.skriptomat.model;

import java.util.HashMap;

public class Course {
    private String collegeId;
    private String course;
    private String timestamp;
    private String id;
    private String type;


    public Course() {
    }

    public Course(String collegeId, String course, String timestamp,String id, String type) {
        this.collegeId = collegeId;
        this.course = course;
        this.timestamp=timestamp;
        this.id = id;
        this.type = type;
    }

    public String getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(String collegeId) {
        this.collegeId = collegeId;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}


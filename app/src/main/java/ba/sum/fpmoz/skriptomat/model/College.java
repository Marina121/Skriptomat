package ba.sum.fpmoz.skriptomat.model;

public class College {
    private Long facultyId;
    private String college;
    private String id;
    private String timestamp;
    private String type;


    public College() {
    }

    public College(Long facultyId, String college, String timestamp,String id, String type) {
        this.facultyId = facultyId;
        this.college = college;
        this.timestamp=timestamp;
        this.id = id;
        this.type = type;
    }

    public Long getFacultyId() {return facultyId;}

    public void setFacultyId(Long facultyId) {this.facultyId = facultyId;}

    public String getCollege() {return college;}

    public void setCollege(String college) {this.college = college;}


    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

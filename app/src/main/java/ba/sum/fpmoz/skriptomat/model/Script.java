package ba.sum.fpmoz.skriptomat.model;

public class Script {
    private String courseId;
    private String script;
    private String timestamp;
    private String id;
    private String url;
    private boolean saved;
    private int likes;
    private  int dislikes;


    public Script(){

    }


    public Script(String courseId, String script, String timestamp,String id,String url) {
        this.courseId = courseId;
        this.script = script;
        this.timestamp=timestamp;
        this.id = id;
        this.url = url;
        this.saved = false;
        this.likes = likes;
        this.dislikes = 0;
    }

    public String getCourseId() {return courseId;}

    public void setCourseId(String courseId) {this.courseId = courseId;}

    public String getScript() {return script;}

    public void setScript(String script) {this.script = script;}

    public String getTimestamp() {return timestamp;}

    public void setTimestamp(String timestamp) {this.timestamp = timestamp;}

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getUrl() {return url;}

    public void setUrl(String url) {this.url = url;}

    public boolean isSaved() {return saved;}

    public void setSaved(boolean saved) {this.saved = saved;}

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }
}



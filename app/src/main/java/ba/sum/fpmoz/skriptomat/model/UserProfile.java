package ba.sum.fpmoz.skriptomat.model;


public class UserProfile {
    private String fullNameTxt;
    private String email;
    private String phoneTxt;
    private String image;
    private String numberIndx;

    public UserProfile(){

    }


    public UserProfile(String fullNameTxt, String email, String phoneTxt, String numberIndx, String image) {
        this.fullNameTxt = fullNameTxt;
        this.email = email;
        this.phoneTxt = phoneTxt;
        this.image = image;
        this.numberIndx = numberIndx;
    }


    public String getFullNameTxt() {
        return fullNameTxt;
    }

    public void setFullNameTxt(String fullNameTxt) {
        this.fullNameTxt = fullNameTxt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneTxt() {
        return phoneTxt;
    }

    public void setPhoneTxt(String phoneTxt) {
        this.phoneTxt = phoneTxt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNumberIndx() {return numberIndx;}

    public void setNumberIndx(String numberIndx) {this.numberIndx = numberIndx;}
}
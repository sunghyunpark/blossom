package db.model;


import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class User {

    @PrimaryKey
    private int no;
    @Required
    private String uid;
    @Required
    private String email;
    @Required
    private String birth;
    @Required
    private String gender;
    @Required
    private String created_at;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

}

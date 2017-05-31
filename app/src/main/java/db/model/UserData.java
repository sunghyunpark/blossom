package db.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class UserData extends RealmObject{


    @PrimaryKey
    private int no;
    @Required
    private String  uid;
    @Required
    private String email;
    @Required
    private String profile_img;
    @Required
    private String birth;
    @Required
    private String gender;
    @Required
    private String token;
    @Required
    private String created_at;
    private int seed_cnt;
    @Required
    private String bg_title;

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

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getSeed_cnt() {
        return seed_cnt;
    }

    public void setSeed_cnt(int seed_cnt) {
        this.seed_cnt = seed_cnt;
    }

    public String getBg_title() {
        return bg_title;
    }

    public void setBg_title(String bg_title) {
        this.bg_title = bg_title;
    }
}

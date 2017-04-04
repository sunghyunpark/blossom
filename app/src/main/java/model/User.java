package model;

/**
 * Created by sunghyun on 2017. 3. 27..
 * user singleton
 */

public class User {
    private static volatile User user = null;

    private String uid;
    private String email;
    private String birth;
    private String gender;
    private String created_at;

    public static  User getInstance(){
        if(user == null)
            synchronized (User.class){
                if(user==null){
                    user = new User();
                }
            }

        return user;
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

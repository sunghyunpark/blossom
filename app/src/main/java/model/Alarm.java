package model;

/**
 * Created by sunghyun on 2017. 5. 12..
 * 알림
 */

public class Alarm {

    private String article_id;
    private String alarlmType;
    private String alarm_txt;
    private String created_at;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getAlarlmType() {
        return alarlmType;
    }

    public void setAlarlmType(String alarlmType) {
        this.alarlmType = alarlmType;
    }

    public String getAlarm_txt() {
        return alarm_txt;
    }

    public void setAlarm_txt(String alarm_txt) {
        this.alarm_txt = alarm_txt;
    }

}

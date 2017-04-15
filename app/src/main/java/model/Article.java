package model;

/**
 * Created by sunghyun on 2017. 3. 27..
 * 아티클
 */

public class Article {

    private String article_id;
    private String uid;
    private String article_text;
    private String article_photo;
    private String like_cnt;
    private String article_created_at;

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getArticle_text() {
        return article_text;
    }

    public void setArticle_text(String article_text) {
        this.article_text = article_text;
    }

    public String getArticle_photo() {
        return article_photo;
    }

    public void setArticle_photo(String article_photo) {
        this.article_photo = article_photo;
    }

    public String getLike_cnt() {
        return like_cnt;
    }

    public void setLike_cnt(String like_cnt) {
        this.like_cnt = like_cnt;
    }

    public String getArticle_created_at() {
        return article_created_at;
    }

    public void setArticle_created_at(String article_created_at) {
        this.article_created_at = article_created_at;
    }

}

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
    private String like_state;
    private String comment_cnt;
    private String bookmark_state;
    private String private_mode;
    private String created_at;

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

    public String getLike_state() {
        return like_state;
    }

    public void setLike_state(String like_state) {
        this.like_state = like_state;
    }

    public String getComment_cnt() {
        return comment_cnt;
    }

    public void setComment_cnt(String comment_cnt) {
        this.comment_cnt = comment_cnt;
    }

    public String getBookmark_state() {
        return bookmark_state;
    }

    public void setBookmark_state(String bookmark_state) {
        this.bookmark_state = bookmark_state;
    }

    public String getPrivate_mode() {
        return private_mode;
    }

    public void setPrivate_mode(String private_mode) {
        this.private_mode = private_mode;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

}

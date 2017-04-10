package model;

/**
 * Created by sunghyun on 2017. 3. 27..
 * 아티클
 */

public class Article {

    private String article_text;
    private String article_photo;
    private String article_created_at;

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

    public String getArticle_created_at() {
        return article_created_at;
    }

    public void setArticle_created_at(String article_created_at) {
        this.article_created_at = article_created_at;
    }

}

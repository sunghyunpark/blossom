package model;

/**
 * Created by sunghyun on 2017. 3. 27..
 * 아티클
 */

public class Article {

    private String article_text;
    private String article_picture;
    private String article_created_at;

    public String getArticle_text() {
        return article_text;
    }

    public void setArticle_text(String article_text) {
        this.article_text = article_text;
    }

    public String getArticle_picture() {
        return article_picture;
    }

    public void setArticle_picture(String article_picture) {
        this.article_picture = article_picture;
    }

    public String getArticle_created_at() {
        return article_created_at;
    }

    public void setArticle_created_at(String article_created_at) {
        this.article_created_at = article_created_at;
    }

}

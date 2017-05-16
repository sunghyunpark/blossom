package model;

/**
 * Created by sunghyun on 2017. 5. 16..
 * 아티클
 */

public class FamousArticle {

    private String famous_article_id;
    private String famous_article_text;
    private String famous_article_photo;
    private String created_at;

    public String getFamous_article_id() {
        return famous_article_id;
    }

    public void setFamous_article_id(String famous_article_id) {
        this.famous_article_id = famous_article_id;
    }

    public String getFamous_article_text() {
        return famous_article_text;
    }

    public void setFamous_article_text(String famous_article_text) {
        this.famous_article_text = famous_article_text;
    }

    public String getFamous_article_photo() {
        return famous_article_photo;
    }

    public void setFamous_article_photo(String famous_article_photo) {
        this.famous_article_photo = famous_article_photo;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}

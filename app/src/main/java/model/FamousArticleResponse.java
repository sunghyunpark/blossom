package model;

import java.util.ArrayList;

/**
 * Created by sunghyun on 2017. 5. 16..
 * 아티클
 */

public class FamousArticleResponse {

    private ArrayList<FamousArticle> famous_article;
    private String last_article_id;
    private boolean error;
    private String error_msg;

    public ArrayList<FamousArticle> getFamous_article() {
        return famous_article;
    }

    public void setFamous_article(ArrayList<FamousArticle> famous_article) {
        this.famous_article = famous_article;
    }

    public String getLast_article_id() {
        return last_article_id;
    }

    public void setLast_article_id(String last_article_id) {
        this.last_article_id = last_article_id;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}

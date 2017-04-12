package model;

import java.util.ArrayList;

/**
 * Created by sunghyun on 2017. 4. 12..
 * article response
 */

public class ArticleResponse {

    private ArrayList<Article> article;
    private boolean error;
    private String error_msg;

    public ArrayList<Article> getArticle() {
        return article;
    }

    public void setArticle(ArrayList<Article> article) {
        this.article = article;
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

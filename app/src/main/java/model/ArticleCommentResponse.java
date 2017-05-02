package model;

import java.util.ArrayList;

/**
 * Created by sunghyun on 2017. 5. 2..
 * article comment response
 */

public class ArticleCommentResponse {

    private ArrayList<ArticleComment> article_comment;
    private boolean error;
    private String error_msg;

    public ArrayList<ArticleComment> getArticle_comment() {
        return article_comment;
    }

    public void setArticle_comment(ArrayList<ArticleComment> article_comment) {
        this.article_comment = article_comment;
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

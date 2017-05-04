package model;

/**
 * Created by sunghyun on 2017. 5. 4..
 * 아티클 디테일
 */

public class ArticleDetailResponse {

    private Article article_detail;
    private boolean error;
    private String error_msg;

    public Article getArticle_detail() {
        return article_detail;
    }

    public void setArticle_detail(Article article_detail) {
        this.article_detail = article_detail;
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

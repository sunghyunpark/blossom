package model;

/**
 * Created by sunghyun on 2017. 3. 27..
 */

public class UserResponse {

    private User user;
    private int article_bg_cnt;
    private boolean error;
    private String error_msg;

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getArticle_bg_cnt() {
        return article_bg_cnt;
    }

    public void setArticle_bg_cnt(int article_bg_cnt) {
        this.article_bg_cnt = article_bg_cnt;
    }


}

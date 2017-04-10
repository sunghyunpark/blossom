package model;

/**
 * Created by sunghyun on 2017. 4. 10..
 * 공통 response
 */

public class CommonResponse {

    private boolean error;
    private String error_msg;

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }


}

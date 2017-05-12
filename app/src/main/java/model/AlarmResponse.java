package model;

import java.util.ArrayList;

/**
 * Created by sunghyun on 2017. 5. 2..
 * alarm response
 */

public class AlarmResponse {

    private ArrayList<Alarm> alarm;
    private boolean error;
    private String error_msg;

    public ArrayList<Alarm> getAlarm() {
        return alarm;
    }

    public void setAlarm(ArrayList<Alarm> alarm) {
        this.alarm = alarm;
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

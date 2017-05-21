package com.yssh1020.blossom;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * 하단 탭 빨콩 상태값 저장
 */
public class AppSettingManager {
    // LogCat tag
    private static String TAG = AppSettingManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "blossom_setting";
    private static final String KEY_TAB1_ON_OFF = "tab1";
    private static final String KEY_TAB2_ON_OFF = "tab2";
    private static final String KEY_TAB4_ON_OFF = "tab4";
    private static final String KEY_TAB5_ON_OFF = "tab5";
    private static final String KEY_APP_ALARM_ON_OFF = "app_alarm";
    private static final String KEY_COMMENT_ALARM_ON_OFF = "comment_alarm";
    private static final String KEY_ARTICLE_LIKE_ALARM_ON_OFF = "article_like_alarm";


    public AppSettingManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void setTab1_State(boolean flag) {
        editor.putBoolean(KEY_TAB1_ON_OFF, flag);
        // commit changes
        editor.commit();

        Log.d(TAG, "tab1 on off state modified!");
    }
    public boolean getTab1_State(){
        return pref.getBoolean(KEY_TAB1_ON_OFF, false);
    }
    public void setTab2_State(boolean flag) {
        editor.putBoolean(KEY_TAB2_ON_OFF, flag);
        // commit changes
        editor.commit();

        Log.d(TAG, "tab2 on off state modified!");
    }
    public boolean getTab2_State(){
        return pref.getBoolean(KEY_TAB2_ON_OFF, false);
    }
    public void setTab4_State(boolean flag) {
        editor.putBoolean(KEY_TAB4_ON_OFF, flag);
        // commit changes
        editor.commit();

        Log.d(TAG, "tab4 on off state modified!");
    }
    public boolean getTab4_State(){
        return pref.getBoolean(KEY_TAB4_ON_OFF, false);
    }
    public void setTab5_State(boolean flag) {
        editor.putBoolean(KEY_TAB5_ON_OFF, flag);
        // commit changes
        editor.commit();

        Log.d(TAG, "tab5 on off state modified!");
    }
    public boolean getTab5_State(){
        return pref.getBoolean(KEY_TAB5_ON_OFF, false);
    }

    public void setAppAlarm_State(boolean flag) {
        editor.putBoolean(KEY_APP_ALARM_ON_OFF, flag);
        // commit changes
        editor.commit();

        Log.d(TAG, "app alarm on off state modified!");
    }
    public boolean getAppAlarm_State(){
        return pref.getBoolean(KEY_APP_ALARM_ON_OFF, false);
    }

    public void setCommentAlarm_State(boolean flag) {
        editor.putBoolean(KEY_COMMENT_ALARM_ON_OFF, flag);
        // commit changes
        editor.commit();

        Log.d(TAG, "comment alarm on off state modified!");
    }
    public boolean getCommentAlarm_State(){
        return pref.getBoolean(KEY_COMMENT_ALARM_ON_OFF, false);
    }

    public void setArticleLikeAlarm_State(boolean flag) {
        editor.putBoolean(KEY_ARTICLE_LIKE_ALARM_ON_OFF, flag);
        // commit changes
        editor.commit();

        Log.d(TAG, "article like alarm on off state modified!");
    }
    public boolean getArticleLikeAlarm_State(){
        return pref.getBoolean(KEY_ARTICLE_LIKE_ALARM_ON_OFF, false);
    }
}
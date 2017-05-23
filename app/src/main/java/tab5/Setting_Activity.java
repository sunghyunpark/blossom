package tab5;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yssh1020.blossom.AppController;
import com.yssh1020.blossom.AppSettingManager;
import com.yssh1020.blossom.MainActivity;
import com.yssh1020.blossom.R;
import com.yssh1020.blossom.SessionManager;

import common.CommonUtil;
import db.RealmConfig;
import db.model.UserData;
import dialog.Logout_Dialog;
import dialog.Logout_For_Not_Email_Dialog;
import io.realm.Realm;
import model.User;


public class Setting_Activity extends Activity {

    private ViewGroup logout_layout, open_source_layout;
    private ImageView back_btn, app_push_btn, comment_push_btn, article_like_push_btn;

    AppSettingManager appSettingManager;
    CommonUtil commonUtil = new CommonUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        InitView();
    }

    private void InitView(){
        appSettingManager = new AppSettingManager(getApplicationContext());

        logout_layout = (ViewGroup)findViewById(R.id.logout_layout);
        back_btn = (ImageView)findViewById(R.id.back_btn);
        back_btn.setOnTouchListener(myOnTouchListener);
        app_push_btn = (ImageView)findViewById(R.id.app_push_btn);
        app_push_btn.setOnTouchListener(myOnTouchListener);
        comment_push_btn = (ImageView)findViewById(R.id.comment_push_btn);
        comment_push_btn.setOnTouchListener(myOnTouchListener);
        article_like_push_btn = (ImageView)findViewById(R.id.article_like_push_btn);
        article_like_push_btn.setOnTouchListener(myOnTouchListener);

        open_source_layout = (ViewGroup)findViewById(R.id.open_source_layout);
        open_source_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OpenSource_Activity.class);
                startActivity(intent);
            }
        });
        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.getInstance().getEmail().equals("")){
                    //이메일 등록안한 경우
                    startActivity(new Intent(getApplicationContext(), Logout_For_Not_Email_Dialog.class));
                }else{
                    startActivity(new Intent(getApplicationContext(), Logout_Dialog.class));
                }
            }
        });

        InitAlarm();
    }

    /**
     * 알림 상태 초기화
     */
    private void InitAlarm(){
        boolean app_push = appSettingManager.getAppAlarm_State();
        boolean comment_push = appSettingManager.getCommentAlarm_State();
        boolean article_like_push = appSettingManager.getArticleLikeAlarm_State();

        if(app_push){
            //app push on
            app_push_btn.setBackgroundResource(R.mipmap.check_btn_img);
        }else{
            app_push_btn.setBackgroundResource(R.drawable.circle_shape_gray);
        }

        if(comment_push){
            //comment push on
            comment_push_btn.setBackgroundResource(R.mipmap.check_btn_img);
        }else{
            comment_push_btn.setBackgroundResource(R.drawable.circle_shape_gray);
        }

        if(article_like_push){
            //article like push on
            article_like_push_btn.setBackgroundResource(R.mipmap.check_btn_img);
        }else{
            article_like_push_btn.setBackgroundResource(R.drawable.circle_shape_gray);
        }
    }


    private View.OnTouchListener myOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Resources res = getResources();
            String push_flag;
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setAlpha(0.55f);
            }else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                v.setAlpha(1.0f);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.setAlpha(1.0f);
                switch(v.getId()){
                    case R.id.back_btn:
                        finish();
                        break;
                    case R.id.app_push_btn:
                        //앱 알림
                        if(appSettingManager.getAppAlarm_State()){
                            app_push_btn.setBackgroundResource(R.drawable.circle_shape_gray);
                            push_flag = "N";
                        }else{
                            app_push_btn.setBackgroundResource(R.mipmap.check_btn_img);
                            push_flag = "Y";
                        }
                        commonUtil.PostPushState(getApplicationContext(), "app_push", User.getInstance().getUid(), push_flag);

                        break;
                    case R.id.comment_push_btn:
                        //댓글 알림
                        if(appSettingManager.getCommentAlarm_State()){
                            comment_push_btn.setBackgroundResource(R.drawable.circle_shape_gray);
                            push_flag = "N";
                        }else{
                            comment_push_btn.setBackgroundResource(R.mipmap.check_btn_img);
                            push_flag = "Y";
                        }
                        commonUtil.PostPushState(getApplicationContext(), "comment_push", User.getInstance().getUid(), push_flag);
                        break;
                    case R.id.article_like_push_btn:
                        //공감 알림
                        if(appSettingManager.getArticleLikeAlarm_State()){
                            article_like_push_btn.setBackgroundResource(R.drawable.circle_shape_gray);
                            push_flag = "N";
                        }else{
                            article_like_push_btn.setBackgroundResource(R.mipmap.check_btn_img);
                            push_flag = "Y";
                        }
                        commonUtil.PostPushState(getApplicationContext(), "like_push", User.getInstance().getUid(), push_flag);
                        break;

                }
            }
            return true;
        }
    };

}
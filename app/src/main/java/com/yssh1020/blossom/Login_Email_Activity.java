package com.yssh1020.blossom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import api.ApiClient;
import api.ApiInterface;
import common.CommonUtil;
import db.RealmUtil;
import model.UserResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login_Email_Activity extends Activity {

    private EditText email_edit_box,pw_edit_box;
    private SessionManager mSessionManager;
    private AppSettingManager appSettingManager;
    RealmUtil realmUtil = new RealmUtil();
    CommonUtil commonUtil = new CommonUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_email_activity);

        InitView();

    }

    private void InitView(){
        mSessionManager = new SessionManager(getApplicationContext());
        appSettingManager = new AppSettingManager(getApplicationContext());

        email_edit_box = (EditText)findViewById(R.id.email_edit_box);
        pw_edit_box = (EditText)findViewById(R.id.pw_edit_box);
        ImageView back_btn = (ImageView)findViewById(R.id.back_btn);
        back_btn.setOnTouchListener(myOnTouchListener);

        Button login_btn = (Button)findViewById(R.id.login_btn);
        login_btn.setOnTouchListener(myOnTouchListener);
    }

    public void LoginEmail(String email, String password){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<UserResponse> call = apiService.LoginEmail("email_login", email, password);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                if(!userResponse.isError()){
                    Toast.makeText(getApplicationContext(), userResponse.getError_msg(), Toast.LENGTH_SHORT).show();
                    mSessionManager.setLogin(true);    //로그인 성공 시 세션 유지
                    String token = FirebaseInstanceId.getInstance().getToken();
                    //realm에 저장
                    realmUtil.InsertDB(getApplicationContext(), userResponse.getUser().getUid(), userResponse.getUser().getEmail(), userResponse.getUser().getProfile_img(), userResponse.getUser().getBirth(),
                            userResponse.getUser().getGender(), token, userResponse.getUser().getCreated_at(), Integer.parseInt(userResponse.getUser().getSeed_cnt()));
                    //push token 등록
                    commonUtil.RegisterPushToken(getApplicationContext(), userResponse.getUser().getUid(), token, "Y");

                    //push state 저장
                    if(userResponse.getUser().getApp_push().equals("Y")){
                        appSettingManager.setAppAlarm_State(true);
                    }else{
                        appSettingManager.setAppAlarm_State(false);
                    }
                    if(userResponse.getUser().getComment_push().equals("Y")){
                        appSettingManager.setCommentAlarm_State(true);
                    }else{
                        appSettingManager.setCommentAlarm_State(false);
                    }
                    if(userResponse.getUser().getLike_push().equals("Y")){
                        appSettingManager.setArticleLikeAlarm_State(true);
                    }else{
                        appSettingManager.setArticleLikeAlarm_State(false);
                    }
                    //로그인 성공 후 메인화면으로 이동
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(), userResponse.getError_msg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
            }
        });
    }

    private View.OnTouchListener myOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setAlpha(0.55f);
            }else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                v.setAlpha(1.0f);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.setAlpha(1.0f);
                switch(v.getId()){
                    case R.id.login_btn:
                        String email = email_edit_box.getText().toString();
                        String pw = pw_edit_box.getText().toString();
                        if(email.equals("")){
                            Toast.makeText(getApplicationContext(),"정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(!email.contains("@") || !email.contains(".com")){
                            Toast.makeText(getApplicationContext(), "올바른 이메일 형식이 아닙니다.",Toast.LENGTH_SHORT).show();
                        }else if(pw.length()<6){
                            Toast.makeText(getApplicationContext(), "비밀번호가 너무 짧습니다.", Toast.LENGTH_SHORT).show();
                        }else{
                            LoginEmail(email, pw);
                        }

                        break;

                    case R.id.back_btn:
                        finish();
                        break;

                }
            }
            return true;
        }
    };


}
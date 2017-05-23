package tab5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.yssh1020.blossom.AppSettingManager;
import com.yssh1020.blossom.MainActivity;
import com.yssh1020.blossom.R;
import com.yssh1020.blossom.SessionManager;

import api.ApiClient;
import api.ApiInterface;
import common.CommonUtil;
import db.RealmConfig;
import db.model.UserData;
import dialog.Logout_Dialog;
import io.realm.Realm;
import model.CommonResponse;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Register_Email_Activity extends Activity {

    private EditText email_edit_box, pw_edit_box;
    private String flag;

    CommonUtil commonUtil = new CommonUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_email_activity);

        Intent intent = getIntent();
        flag = intent.getExtras().getString("from");

        InitView();
    }

    private void InitView(){
        email_edit_box = (EditText)findViewById(R.id.email_edit_box);
        pw_edit_box = (EditText)findViewById(R.id.pw_edit_box);
        Button register_btn = (Button)findViewById(R.id.register_btn);
        register_btn.setOnTouchListener(myOnTouchListener);
        ImageView back_btn = (ImageView)findViewById(R.id.back_btn);
        back_btn.setOnTouchListener(myOnTouchListener);
    }

    private void Logout(){
        SessionManager mSessionManager = new SessionManager(getApplicationContext());
        mSessionManager.setLogin(false);
        RealmConfig realmConfig = new RealmConfig();
        Realm mRealm = Realm.getInstance(realmConfig.User_DefaultRealmVersion(getApplicationContext()));
        UserData user_db = mRealm.where(UserData.class).equalTo("no",1).findFirst();
        mRealm.beginTransaction();
        user_db.deleteFromRealm();
        mRealm.commitTransaction();
        //fcm 토큰 서버에 등록

        commonUtil.RegisterPushToken(getApplicationContext(), User.getInstance().getUid(), User.getInstance().getToken(), "N");

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void RegisterEmail(String uid, final String email, String password){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommonResponse> call = apiService.RegisterEmail("register_email", uid, email, password);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(!commonResponse.isError()){
                    Toast.makeText(getApplicationContext(), commonResponse.getError_msg(), Toast.LENGTH_SHORT).show();
                    if(flag.equals("logout")){
                        Logout();
                        finish();
                    }else if(flag.equals("register")){
                        User.getInstance().setEmail(email);
                        finish();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
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
                    case R.id.register_btn:
                        String email = email_edit_box.getText().toString();
                        String pw = pw_edit_box.getText().toString();
                        if(email.equals("")){
                            Toast.makeText(getApplicationContext(),"정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }else if(!email.contains("@") || !email.contains(".com")){
                            Toast.makeText(getApplicationContext(), "올바른 이메일 형식이 아닙니다.",Toast.LENGTH_SHORT).show();
                        }else if(pw.length()<6){
                            Toast.makeText(getApplicationContext(), "비밀번호가 너무 짧습니다.", Toast.LENGTH_SHORT).show();
                        }else{
                            RegisterEmail(User.getInstance().getUid(), email, pw);

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
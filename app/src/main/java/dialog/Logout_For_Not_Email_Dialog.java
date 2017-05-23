package dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.yssh1020.blossom.MainActivity;
import com.yssh1020.blossom.R;
import com.yssh1020.blossom.SessionManager;

import common.CommonUtil;
import db.RealmConfig;
import db.model.UserData;
import io.realm.Realm;
import model.User;
import tab5.Register_Email_Activity;

/**
 * 로그아웃 이메일 등록을 안한 경우
 */

public class Logout_For_Not_Email_Dialog extends Activity {
    CommonUtil commonUtil = new CommonUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//위의 타이틀바 제거인데 setContentView 전에 넣어줘야함 뷰가 생성되기전에 제거되어야하므로...
        setContentView(R.layout.logout_for_not_emaildialog);


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

    //button event
    public void buttonPressed(View v) {
        switch ((v.getId())){
            case R.id.logout_btn:
                Logout();
                finish();
                break;
            case R.id.go_to_register_email_btn:
                Intent intent = new Intent(getApplicationContext(), Register_Email_Activity.class);
                intent.putExtra("from", "logout");
                startActivity(intent);
                finish();
                break;

        }

    }

}
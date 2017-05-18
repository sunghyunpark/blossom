package tab5;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.yssh1020.blossom.AppController;
import com.yssh1020.blossom.MainActivity;
import com.yssh1020.blossom.R;
import com.yssh1020.blossom.SessionManager;

import common.CommonUtil;
import db.RealmConfig;
import db.model.UserData;
import io.realm.Realm;
import model.User;


public class Setting_Activity extends Activity {
    private SessionManager mSessionManager;
    //Realm
    private Realm mRealm;
    private RealmConfig realmConfig;

    CommonUtil commonUtil = new CommonUtil();

    private ViewGroup logout_layout, open_source_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        InitView();
    }

    private void InitView(){
        mSessionManager = new SessionManager(getApplicationContext());
        logout_layout = (ViewGroup)findViewById(R.id.logout_layout);
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
                Logout();
            }
        });
    }

    private void Logout(){
        mSessionManager.setLogin(false);
        realmConfig = new RealmConfig();
        mRealm = Realm.getInstance(realmConfig.User_DefaultRealmVersion(getApplicationContext()));
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

}
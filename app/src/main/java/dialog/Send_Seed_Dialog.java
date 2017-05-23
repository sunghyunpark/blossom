package dialog;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yssh1020.blossom.R;

import org.w3c.dom.Text;

import java.util.TooManyListenersException;

import common.CommonUtil;
import db.RealmConfig;
import db.model.UserData;
import event.BusProvider;
import event.MyArticleDeleteEvent;
import io.realm.Realm;
import model.User;

/**
 * 내 아티클 삭제하기 버튼 탭 시 확인용 다이얼로그 노출
 */

public class Send_Seed_Dialog extends Activity {


    private String article_user_id;
    private ViewGroup plus_btn, minus_btn;
    private TextView seed_cnt_txt, my_seed_cnt_txt;
    private ImageView cancel_btn;
    private Button send_btn;
    private int current_seed_cnt;
    private int my_seed_cnt;

    CommonUtil commonUtil = new CommonUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//위의 타이틀바 제거인데 setContentView 전에 넣어줘야함 뷰가 생성되기전에 제거되어야하므로...
        setContentView(R.layout.send_seed_dialog);

        Intent intent = getIntent();
        article_user_id = intent.getExtras().getString("article_user_id");

        InitView();

    }

    private void InitView(){
        plus_btn = (ViewGroup)findViewById(R.id.plus_btn);
        plus_btn.setOnTouchListener(myOnTouchListener);
        minus_btn = (ViewGroup)findViewById(R.id.minus_btn);
        minus_btn.setOnTouchListener(myOnTouchListener);
        seed_cnt_txt = (TextView)findViewById(R.id.seed_cnt_txt);
        my_seed_cnt_txt = (TextView)findViewById(R.id.my_seed_cnt_txt);
        cancel_btn = (ImageView)findViewById(R.id.cancel_btn);
        cancel_btn.setOnTouchListener(myOnTouchListener);
        send_btn = (Button)findViewById(R.id.send_btn);
        send_btn.setOnTouchListener(myOnTouchListener);

        InitSeedCnt(User.getInstance().getUid());
    }

    private void InitSeedCnt(String uid){
        Realm mRealm;
        RealmConfig realmConfig;
        realmConfig = new RealmConfig();
        Resources res = getResources();
        mRealm = Realm.getInstance(realmConfig.User_DefaultRealmVersion(getApplicationContext()));

        UserData user = mRealm.where(UserData.class).equalTo("uid",uid).findFirst();
        my_seed_cnt = user.getSeed_cnt();
        String current_my_seed_cnt = my_seed_cnt+"";
        my_seed_cnt_txt.setText(String.format(res.getString(R.string.send_seed_dialog_sub_txt_2),current_my_seed_cnt));
        current_seed_cnt = Integer.parseInt(current_my_seed_cnt);

    }

    private View.OnTouchListener myOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Resources res = getResources();
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setAlpha(0.55f);
            }else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                v.setAlpha(1.0f);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.setAlpha(1.0f);
                switch(v.getId()){
                    case R.id.cancel_btn:
                        finish();
                        break;
                    case R.id.plus_btn:
                        current_seed_cnt++;
                        seed_cnt_txt.setText(current_seed_cnt+"");
                        break;
                    case R.id.minus_btn:
                        if(current_seed_cnt>0){
                            current_seed_cnt--;
                            seed_cnt_txt.setText(current_seed_cnt+"");
                        }
                        break;
                    case R.id.send_btn:
                        if(my_seed_cnt >= current_seed_cnt){
                            //가능
                            commonUtil.PostSeedToArticleUser(getApplicationContext(), article_user_id, User.getInstance().getUid(),current_seed_cnt);
                            finish();
                        }else{
                            //불가능
                            Toast.makeText(getApplicationContext(), "가지고 있는 씨앗보다 적게 설정해야합니다.", Toast.LENGTH_SHORT).show();
                        }
                        break;

                }
            }
            return true;
        }
    };

}
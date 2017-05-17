package com.yssh1020.blossom;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import db.RealmConfig;
import db.model.UserData;
import io.realm.Realm;
import tab3.SelectBG_Activity;
import view.CommonTabMenu;
import model.User;
import tab1.FragmentPage1;
import tab2.FragmentPage2;
import tab3.Upload_Article;
import tab4.FragmentPage4;
import tab5.FragmentPage5;
import view.CommonTopTitle;

import static tab1.FragmentPage1.mLayout;

public class MainActivity extends FragmentActivity {

    private SessionManager session;    // session

    ImageView tab1, tab2, tab3, tab4, tab5;    //하단 탭 버튼들
    private ViewGroup bottom_tab_menu;    //하단 탭 메뉴
    private ViewGroup app_title_bar;    //상단 타이틀 바

    //현재 페이지
    private int current_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(getApplicationContext(), SplashPage.class));

        InitData();
    }

    /**
     * 단말기 정보(가로/세로 크기)
     * server ip
     */
    private void InitData(){
        Display display;
        display = ((WindowManager)getApplicationContext().getSystemService(getApplicationContext().WINDOW_SERVICE)).getDefaultDisplay();

        AppController.getInstance().setDISPLAY_WIDTH(display.getWidth());
        AppController.getInstance().setDISPLAY_HEIGHT(display.getHeight());
        AppController.getInstance().setServer_base_ip("http://sunghyun1020.cafe24.com/blossom/");
        AppController.getInstance().setServer_img_path("http://sunghyun1020.cafe24.com/blossom/img/");

        session = new SessionManager(getApplicationContext());
        if(!session.isLoggedIn()){
            //세션 만료
            startActivity(new Intent(getApplicationContext(),LoginPage.class));
            finish();
        }else{
            InitView();
        }
    }

    private void InitView(){
        //lollipop이상인 경우에만 적용
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorGray));
        }
        bottom_tab_menu = (ViewGroup)findViewById(R.id.bottom_tab_menu);
        CommonTabMenu.getInstance().setBottom_menu(bottom_tab_menu);
        app_title_bar = (ViewGroup)findViewById(R.id.app_title_bar);
        CommonTopTitle.getInstance().setTop_title(app_title_bar);
        /**
         * 최초 화면 진입 시 랜딩되는 화면 및 버튼 초기화
         */
        //하단 탭 버튼 초기화
        tab1 = (ImageView)findViewById(R.id.tab_1);
        tab2 = (ImageView)findViewById(R.id.tab_2);
        tab3 = (ImageView)findViewById(R.id.tab_3);
        tab4 = (ImageView)findViewById(R.id.tab_4);
        tab5 = (ImageView)findViewById(R.id.tab_5);
        tab1.setOnTouchListener(myOnTouchListener);
        tab2.setOnTouchListener(myOnTouchListener);
        tab3.setOnTouchListener(myOnTouchListener);
        tab4.setOnTouchListener(myOnTouchListener);
        tab5.setOnTouchListener(myOnTouchListener);

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, new FragmentPage1());
        fragmentTransaction.commit();
        tab1.setImageResource(R.mipmap.tab1_click_img);
        current_page = 1;

        SaveUserInfo();
    }

    private void SaveUserInfo(){
        RealmConfig realmConfig = new RealmConfig();
        Realm mRealm = Realm.getInstance(realmConfig.User_DefaultRealmVersion(getApplicationContext()));
        UserData user = mRealm.where(UserData.class).equalTo("no",1).findFirst();

        User.getInstance().setUid(user.getUid());
        User.getInstance().setEmail(user.getEmail());
        User.getInstance().setBirth(user.getBirth());
        User.getInstance().setGender(user.getGender());
        User.getInstance().setCreated_at(user.getCreated_at());
        User.getInstance().setProfile_img(user.getProfile_img());
        User.getInstance().setToken(user.getToken());
        Log.d("UserInfo", "UserUid : "+user.getUid()+"");
        Log.d("UserInfo", "UserEmail : "+user.getEmail());
        Log.d("UserInfo", "UserBirth : "+user.getBirth());
        Log.d("UserInfo", "UserGender : "+user.getGender());
        Log.d("UserInfo", "Created_at : "+user.getCreated_at());
        Log.d("UserInfo", "Profile_img : "+user.getProfile_img());
        Log.d("UserInfo", "Push_Token : "+user.getToken());
        Log.d("UserInfo", "Seed_Cnt : "+user.getSeed_cnt());
        Toast.makeText(getApplicationContext(),User.getInstance().getUid(),Toast.LENGTH_SHORT).show();
    }


    private View.OnTouchListener myOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            boolean is_current_page = false;
            Fragment fragment = null;
            /**
             * 3번쨰 글쓰기 버튼 탭 시 하단 탭 메뉴에서 이전에 선택한 아이콘 유지하기 위해
             */
            if(v.getId() != R.id.tab_3){
                tab1.setImageResource(R.mipmap.tab1_no_click_img);
                tab2.setImageResource(R.mipmap.tab2_no_click_img);
                tab3.setImageResource(R.mipmap.tab3_img);
                tab4.setImageResource(R.mipmap.tab4_no_click_img);
                tab5.setImageResource(R.mipmap.tab5_no_click_img);
            }

            Bundle bundle = new Bundle();

            Resources res = getResources();
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setAlpha(0.55f);
            }else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                v.setAlpha(1.0f);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.setAlpha(1.0f);
                switch(v.getId()){

                    case R.id.tab_1 :
                        tab1.setImageResource(R.mipmap.tab1_click_img);
                        CommonTabMenu.getInstance().getBottom_menu().setBackgroundColor(getResources().getColor(R.color.bottomTabMenu));
                        if(current_page == 1){
                            is_current_page = true;
                        }else{
                            fragment = new FragmentPage1();
                            bundle.putString("KEY_MSG", "replace");
                            fragment.setArguments(bundle);
                            current_page = 1;
                            is_current_page = false;
                            app_title_bar.setVisibility(View.VISIBLE);
                        }
                        break ;
                    case R.id.tab_2 :
                        CommonTabMenu.getInstance().getBottom_menu().setBackgroundColor(getResources().getColor(R.color.bottomTabMenu));
                        tab2.setImageResource(R.mipmap.tab2_click_img);
                        if(current_page == 2){
                            is_current_page = true;
                        }else{
                            fragment = new FragmentPage2();
                            bundle.putString("KEY_MSG", "replace");
                            fragment.setArguments(bundle);
                            current_page = 2;
                            is_current_page = false;
                            app_title_bar.setVisibility(View.GONE);
                        }
                        break ;
                    case R.id.tab_3 :
                        startActivity(new Intent(getApplicationContext(), Upload_Article.class));
                        app_title_bar.setVisibility(View.VISIBLE);
                        break ;
                    case R.id.tab_4:
                        tab4.setImageResource(R.mipmap.tab4_click_img);
                        CommonTabMenu.getInstance().getBottom_menu().setBackgroundColor(getResources().getColor(R.color.bottomTabMenu));
                        if(current_page == 4){
                            is_current_page = true;
                        }else{
                            fragment = new FragmentPage4();
                            bundle.putString("KEY_MSG", "replace");
                            fragment.setArguments(bundle);
                            current_page = 4;
                            is_current_page = false;
                            app_title_bar.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.tab_5:
                        tab5.setImageResource(R.mipmap.tab5_click_img);
                        CommonTabMenu.getInstance().getBottom_menu().setBackgroundColor(getResources().getColor(R.color.bottomTabMenu));
                        if(current_page == 5){
                            is_current_page = true;
                        }else{
                            fragment = new FragmentPage5();
                            bundle.putString("KEY_MSG", "replace");
                            fragment.setArguments(bundle);
                            current_page = 5;
                            is_current_page = false;
                            app_title_bar.setVisibility(View.GONE);
                        }
                        break;

                }
                // 글쓰기 버튼이 아닌경우에만...
                if((v.getId() != R.id.tab_3) && !is_current_page){
                    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.main_frame, fragment);
                    fragmentTransaction.commit();
                }
            }
            return true;
        }
    };

    @Override
    public void onBackPressed() {
        if (FragmentPage1.mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN){
            FragmentPage1.mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            CommonTabMenu.getInstance().getBottom_menu().setVisibility(View.VISIBLE);
        }else {
            super.onBackPressed();
        }
    }
}

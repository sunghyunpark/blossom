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

import com.google.firebase.iid.FirebaseInstanceId;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import api.ApiClient;
import api.ApiInterface;
import db.RealmConfig;
import db.RealmUtil;
import db.model.UserData;
import io.realm.Realm;
import model.UserResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tab3.SelectBG_Activity;
import tab5.ArticleActivity;
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
    private AppSettingManager appSettingManager;    //app setting

    ViewGroup tab1, tab2, tab3, tab4, tab5;    //하단 탭 버튼들
    ImageView tab1_img, tab2_img, tab3_img, tab4_img, tab5_img;
    ImageView tab1_new_img, tab2_new_img, tab4_new_img, tab5_new_img;
    private ViewGroup bottom_tab_menu;    //하단 탭 메뉴
    private ViewGroup app_title_bar;    //상단 타이틀 바

    //현재 페이지
    private int current_page;
    RealmUtil realmUtil = new RealmUtil();
    //로딩페이지 노출 여부
    private boolean loadingpage = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 푸시로 진입했을 경우 분기처리
         */
        try{
            String str = getIntent().getExtras().getString("from_push");
            if(str != null){
                loadingpage = false;
                if(str.equals("article_like") || str.equals("article_comment")){
                    String article_id = getIntent().getExtras().getString("article_id");
                    Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                    intent.putExtra("article_id", article_id);
                    startActivity(intent);
                }
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        //푸시로 진입하는 경우에는 로딩화면 미노출 시키기 위해...
        if(loadingpage){
            startActivity(new Intent(getApplicationContext(), SplashPage.class));
        }

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
        tab1 = (ViewGroup)findViewById(R.id.tab_1);
        tab2 = (ViewGroup)findViewById(R.id.tab_2);
        tab3 = (ViewGroup)findViewById(R.id.tab_3);
        tab4 = (ViewGroup)findViewById(R.id.tab_4);
        tab5 = (ViewGroup)findViewById(R.id.tab_5);
        tab1_img = (ImageView)findViewById(R.id.tab1_img);
        tab2_img = (ImageView)findViewById(R.id.tab2_img);
        tab3_img = (ImageView)findViewById(R.id.tab3_img);
        tab4_img = (ImageView)findViewById(R.id.tab4_img);
        tab5_img = (ImageView)findViewById(R.id.tab5_img);
        tab1_new_img = (ImageView)findViewById(R.id.tab1_new_img);
        tab2_new_img = (ImageView)findViewById(R.id.tab2_new_img);
        tab4_new_img = (ImageView)findViewById(R.id.tab4_new_img);
        tab5_new_img = (ImageView)findViewById(R.id.tab5_new_img);
        tab1.setOnTouchListener(myOnTouchListener);
        tab2.setOnTouchListener(myOnTouchListener);
        tab3.setOnTouchListener(myOnTouchListener);
        tab4.setOnTouchListener(myOnTouchListener);
        tab5.setOnTouchListener(myOnTouchListener);

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, new FragmentPage1());
        fragmentTransaction.commit();
        tab1_img.setImageResource(R.mipmap.tab1_click_img);
        current_page = 1;

        InitTabNewImg();
        SaveUserInfo();
    }

    /**
     * app setting manager를 통해 new 빨콩 초기화
     */
    private void InitTabNewImg(){
        appSettingManager = new AppSettingManager(getApplicationContext());

        if(appSettingManager.getTab1_State()){
            tab1_new_img.setVisibility(View.VISIBLE);
        }else if(appSettingManager.getTab2_State()){
            tab2_new_img.setVisibility(View.VISIBLE);
        }else if(appSettingManager.getTab4_State()){
            tab4_new_img.setVisibility(View.VISIBLE);
        }else if(appSettingManager.getTab5_State()){
            tab5_new_img.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 나중에 로그인 기능 들어가면 로그인 즉시 로컬디비에 토큰도 저장해둬야함.
     */
    private void SaveUserInfo(){
        RealmConfig realmConfig = new RealmConfig();
        Realm mRealm = Realm.getInstance(realmConfig.User_DefaultRealmVersion(getApplicationContext()));
        final UserData user = mRealm.where(UserData.class).equalTo("no",1).findFirst();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        /*
        최초 가입이므로 이메일 주소는 빈값
         */
        Call<UserResponse> call = apiService.GetUserData("load_user_data",user.getUid());
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                UserResponse userdata = response.body();
                if(!userdata.isError()){
                    //Toast.makeText(getApplicationContext(), userdata.getError_msg(),Toast.LENGTH_SHORT).show();
                    realmUtil.InsertDB(getApplicationContext(), userdata.getUser().getUid(), userdata.getUser().getEmail(), userdata.getUser().getProfile_img(), userdata.getUser().getBirth(),
                            userdata.getUser().getGender(), user.getToken(), userdata.getUser().getCreated_at(), Integer.parseInt(userdata.getUser().getSeed_cnt()));

                    //Log.d("UserInfo", "AppPush : "+userdata.getUser().getApp_push());
                    //Log.d("UserInfo", "CommentPush : "+userdata.getUser().getComment_push());
                    //Log.d("UserInfo", "LikePush : "+userdata.getUser().getLike_push());
                    //push state 저장
                    if(userdata.getUser().getApp_push().equals("Y")){
                        appSettingManager.setAppAlarm_State(true);
                    }else{
                        appSettingManager.setAppAlarm_State(false);
                    }
                    if(userdata.getUser().getComment_push().equals("Y")){
                        appSettingManager.setCommentAlarm_State(true);
                    }else{
                        appSettingManager.setCommentAlarm_State(false);
                    }
                    if(userdata.getUser().getLike_push().equals("Y")){
                        appSettingManager.setArticleLikeAlarm_State(true);
                    }else{
                        appSettingManager.setArticleLikeAlarm_State(false);
                    }

                }else{
                    Toast.makeText(getApplicationContext(), userdata.getError_msg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                /**
                 * 네트워크 off 인경우 기존 로컬데이터로 사용
                 */
                Realm mRealm;
                RealmConfig realmConfig;
                realmConfig = new RealmConfig();

                mRealm = Realm.getInstance(realmConfig.User_DefaultRealmVersion(getApplicationContext()));
                UserData user_db = mRealm.where(UserData.class).equalTo("no",1).findFirst();

                User.getInstance().setUid(user_db.getUid());
                User.getInstance().setEmail(user_db.getEmail());
                User.getInstance().setGender(user_db.getGender());
                User.getInstance().setBirth(user_db.getBirth());
                User.getInstance().setProfile_img(user_db.getProfile_img());
                User.getInstance().setToken(user_db.getToken());
                User.getInstance().setCreated_at(user_db.getCreated_at());
                User.getInstance().setSeed_cnt(String.valueOf(user_db.getSeed_cnt()));
            }
        });

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
                tab1_img.setImageResource(R.mipmap.tab1_no_click_img);
                tab2_img.setImageResource(R.mipmap.tab2_no_click_img);
                tab3_img.setImageResource(R.mipmap.tab3_img);
                tab4_img.setImageResource(R.mipmap.tab4_no_click_img);
                tab5_img.setImageResource(R.mipmap.tab5_no_click_img);
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
                        tab1_img.setImageResource(R.mipmap.tab1_click_img);
                        tab1_new_img.setVisibility(View.GONE);
                        appSettingManager.setTab1_State(false);
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
                        tab2_img.setImageResource(R.mipmap.tab2_click_img);
                        tab2_new_img.setVisibility(View.GONE);
                        appSettingManager.setTab2_State(false);
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
                        tab4_img.setImageResource(R.mipmap.tab4_click_img);
                        tab4_new_img.setVisibility(View.GONE);
                        appSettingManager.setTab4_State(false);
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
                        tab5_img.setImageResource(R.mipmap.tab5_click_img);
                        tab5_new_img.setVisibility(View.GONE);
                        appSettingManager.setTab5_State(false);
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

package com.yssh1020.blossom;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.otto.Subscribe;

import api.ApiClient;
import api.ApiInterface;
import common.CommonUtil;
import db.RealmConfig;
import db.RealmUtil;
import db.model.UserData;
import dialog.SelectBirthDialog;
import event.BusProvider;
import event.GenderSelectEvent;
import io.realm.Realm;
import model.User;
import model.UserResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginPage extends FragmentActivity {

    private SessionManager mSessionManager;
    private AppSettingManager appSettingManager;

    //Viewpager
    private ViewPager mViewPager;

    private ViewGroup male_btn, female_btn, register_layout;
    private ImageView male_check_img, female_check_img;
    private Button birth_btn;
    private String gender = "male";
    private String profile_img;

    CommonUtil commonUtil = new CommonUtil();
    RealmUtil realmUtil = new RealmUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);

        BusProvider.getInstance().register(this);

        InitView();

    }

    /**
     * 화면 초기화
     */
    private void InitView(){
        mSessionManager = new SessionManager(getApplicationContext());
        appSettingManager = new AppSettingManager(getApplicationContext());

        final ImageView select_circle1 = (ImageView) findViewById(R.id.circle_1);
        final ImageView select_circle2 = (ImageView) findViewById(R.id.circle_2);
        register_layout = (ViewGroup)findViewById(R.id.register_layout);
        male_btn = (ViewGroup)findViewById(R.id.male_btn);
        female_btn = (ViewGroup)findViewById(R.id.female_btn);
        male_check_img = (ImageView)findViewById(R.id.male_check_img);
        female_check_img = (ImageView)findViewById(R.id.female_check_img);
        birth_btn = (Button)findViewById(R.id.birth_btn);
        ViewGroup start_btn = (ViewGroup)findViewById(R.id.start_btn);
        start_btn.setOnTouchListener(myOnTouchListener);

        male_btn.setOnTouchListener(myOnTouchListener);
        female_btn.setOnTouchListener(myOnTouchListener);
        birth_btn.setOnTouchListener(myOnTouchListener);

        Button login_btn = (Button)findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login_Email_Activity.class));
            }
        });

        //viewpager
        mViewPager = (ViewPager) findViewById(R.id.intro_pager);
        PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
            }

            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                mViewPager.setCurrentItem(position);
                mViewPager.setOffscreenPageLimit(2);
                register_layout.setVisibility(View.GONE);

                switch (position) {
                    case 0:
                        select_circle1.setImageResource(R.drawable.select_circle);
                        select_circle2.setImageResource(R.drawable.no_select_circle);
                        break;
                    case 1:
                        select_circle1.setImageResource(R.drawable.no_select_circle);
                        select_circle2.setImageResource(R.drawable.select_circle);
                        register_layout.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    /**
     * 성별 선택 초기화
     * @param male
     * @param female
     */
    private void SelectGender(boolean male, boolean female){
        if(male && !female){
            male_btn.setBackgroundResource(R.color.gender_selected);
            female_btn.setBackgroundResource(R.color.gender_not_selected);
            male_check_img.setVisibility(View.VISIBLE);
            female_check_img.setVisibility(View.INVISIBLE);
            gender = "male";
        }else{
            male_btn.setBackgroundResource(R.color.gender_not_selected);
            female_btn.setBackgroundResource(R.color.gender_selected);
            male_check_img.setVisibility(View.INVISIBLE);
            female_check_img.setVisibility(View.VISIBLE);
            gender = "female";
        }
    }

    private void RegisterUser(String birth, String gender, String profile_img){

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        /*
        최초 가입이므로 이메일 주소는 빈값
         */
        Call<UserResponse> call = apiService.PostUser("register","",birth,gender,profile_img);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                UserResponse userdata = response.body();
                if(!userdata.isError()){
                    Toast.makeText(getApplicationContext(), userdata.getError_msg(),Toast.LENGTH_SHORT).show();

                    mSessionManager.setLogin(true);    //로그인 성공 시 세션 유지
                    String token = FirebaseInstanceId.getInstance().getToken();
                    //realm에 저장
                    realmUtil.InsertDB(getApplicationContext(), userdata.getUser().getUid(), userdata.getUser().getEmail(), userdata.getUser().getProfile_img(), userdata.getUser().getBirth(),
                            userdata.getUser().getGender(), token, userdata.getUser().getCreated_at(), Integer.parseInt(userdata.getUser().getSeed_cnt()));
                    //push token 등록
                    commonUtil.RegisterPushToken(getApplicationContext(), userdata.getUser().getUid(), token, "Y");

                    //push state 저장
                    appSettingManager.setAppAlarm_State(true);
                    appSettingManager.setCommentAlarm_State(true);
                    appSettingManager.setArticleLikeAlarm_State(true);

                    //로그인 성공 후 메인화면으로 이동
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(), userdata.getError_msg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
            }
        });

    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
        @Override
        public Fragment getItem(int position) {
            // 해당하는 page의 Fragment를 생성합니다.
            return Fragment_Intro.create(position);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            if (object instanceof Fragment) {
                Fragment fragment = (Fragment) object;
                android.support.v4.app.FragmentManager fm = fragment.getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.remove(fragment);
                //this.notifyDataSetChanged();
                ft.commitAllowingStateLoss();
            }
        }
        @Override
        public int getCount() {
            return 2;  // 총 2개의 page를 반환
        }

    }

    @Override
    protected void onDestroy() {
        // Always unregister when an object no longer should be on the bus.
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }
    @Subscribe
    public void FinishLoad(GenderSelectEvent mPushEvent) {
        String date_str = mPushEvent.getDate_str();
        birth_btn.setText(date_str);

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
                    case R.id.male_btn:
                        SelectGender(true,false);
                        break;
                    case R.id.female_btn:
                        SelectGender(false,true);
                        break;
                    case R.id.birth_btn:
                        startActivity(new Intent(getApplicationContext(), SelectBirthDialog.class));
                        break;
                    case R.id.start_btn:
                        if(birth_btn.getText().toString().equals(String.format(res.getString(R.string.login_birth_btn_txt)))){
                            Toast.makeText(getApplicationContext(),String.format(res.getString(R.string.no_selected_birth_alert_txt)), Toast.LENGTH_SHORT).show();
                        }else{
                            profile_img = commonUtil.GetUserProfileName();
                            RegisterUser(birth_btn.getText().toString(), gender, profile_img);
                        }
                        break;

                }
            }
            return true;
        }
    };
}

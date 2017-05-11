package tab5;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yssh1020.blossom.AppController;
import com.yssh1020.blossom.R;

import api.ApiClient;
import api.ApiInterface;
import model.MyPageResponse;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tab3.SelectBG_Activity;
import tab5.viewpager.Page1;
import tab5.viewpager.Page2;
import tab5.viewpager.Page3;

/**
 * created by sunghyun 2017-04-29
 */
public class FragmentPage5 extends Fragment {

    CollapsingToolbarLayout collapsingToolbarLayout;
    //뷰페이저
    private ViewPager mViewPager;
    private pagerAdapter adapter;
    private static final int NUM_PAGES = 3;//페이지 수
    private int temp = 0; //현재 페이지

    //상단 메뉴
    private ViewGroup story_underbar, comment_underbar, favorite_underbar;
    private Button story_btn, comment_btn, favorite_btn;


    View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragmentpage5, container, false);

        Bundle bundle = getArguments();
        if(bundle != null){
            String msg = bundle.getString("KEY_MSG");
            if(msg != null){

            }
        }


        InitView();
        return v;
    }

    private void InitView(){

        collapsingToolbarLayout = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar);
        //collapsingToolbarLayout.setTitle("사진");
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorPrimary));

        story_underbar = (ViewGroup)v.findViewById(R.id.story_underbar);
        comment_underbar = (ViewGroup)v.findViewById(R.id.comment_underbar);
        favorite_underbar = (ViewGroup)v.findViewById(R.id.favorite_underbar);
        story_btn = (Button)v.findViewById(R.id.my_story_btn);
        comment_btn = (Button)v.findViewById(R.id.my_comment_btn);
        favorite_btn = (Button)v.findViewById(R.id.my_favorite_btn);

        ImageView setting_btn = (ImageView)v.findViewById(R.id.setting_btn);
        setting_btn.setOnTouchListener(myOnTouchListener);

        story_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);
                favorite_btn.setTextColor(getResources().getColor(R.color.colorGray));
                comment_btn.setTextColor(getResources().getColor(R.color.colorGray));
                story_btn.setTextColor(getResources().getColor(R.color.colorSky));
                story_underbar.setBackgroundColor(getResources().getColor(R.color.colorSky));
                comment_underbar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                favorite_underbar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            }
        });
        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1);
                favorite_btn.setTextColor(getResources().getColor(R.color.colorGray));
                story_btn.setTextColor(getResources().getColor(R.color.colorGray));
                comment_btn.setTextColor(getResources().getColor(R.color.colorSky));
                comment_underbar.setBackgroundColor(getResources().getColor(R.color.colorSky));
                story_underbar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                favorite_underbar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            }
        });
        favorite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(2);
                story_btn.setTextColor(getResources().getColor(R.color.colorGray));
                comment_btn.setTextColor(getResources().getColor(R.color.colorGray));
                favorite_btn.setTextColor(getResources().getColor(R.color.colorSky));
                favorite_underbar.setBackgroundColor(getResources().getColor(R.color.colorSky));
                comment_underbar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                story_underbar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            }
        });

        InitViewPager();
        LoadData();
    }

    private void InitViewPager(){
        //뷰페이저
        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        adapter = new pagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(temp);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                temp = position;

                switch (temp) {
                    case 0:
                        comment_btn.setTextColor(getResources().getColor(R.color.colorGray));
                        story_btn.setTextColor(getResources().getColor(R.color.colorSky));
                        favorite_btn.setTextColor(getResources().getColor(R.color.colorGray));
                        story_underbar.setBackgroundColor(getResources().getColor(R.color.colorSky));
                        comment_underbar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        favorite_underbar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        break;
                    case 1:
                        story_btn.setTextColor(getResources().getColor(R.color.colorGray));
                        comment_btn.setTextColor(getResources().getColor(R.color.colorSky));
                        favorite_btn.setTextColor(getResources().getColor(R.color.colorGray));
                        comment_underbar.setBackgroundColor(getResources().getColor(R.color.colorSky));
                        story_underbar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        favorite_underbar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        break;
                    case 2:
                        comment_btn.setTextColor(getResources().getColor(R.color.colorGray));
                        favorite_btn.setTextColor(getResources().getColor(R.color.colorSky));
                        story_btn.setTextColor(getResources().getColor(R.color.colorGray));
                        favorite_underbar.setBackgroundColor(getResources().getColor(R.color.colorSky));
                        comment_underbar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        story_underbar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    /**
     * 상단 시드와 스토리 갯수
     * @param seed
     * @param story_cnt
     */
    private void seedAndstory(String seed, String story_cnt){
        TextView seed_txt = (TextView)v.findViewById(R.id.seed_txt);
        TextView story_cnt_txt = (TextView)v.findViewById(R.id.my_story_txt);

        seed_txt.setText(seed);
        story_cnt_txt.setText(story_cnt);
    }

    private void PixViewPagerSize(){
        //viewPager의 크기를 단말기 해상도에 맞게 맞춰줌
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) mViewPager.getLayoutParams();
        params.width = AppController.getInstance().getDISPLAY_WIDTH();
        params.height = AppController.getInstance().getDISPLAY_HEIGHT();
        mViewPager.setLayoutParams(params);
    }

    private void LoadData(){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<MyPageResponse> call = apiService.GetMyPageInfo("my_page_etc_info", User.getInstance().getUid());
        call.enqueue(new Callback<MyPageResponse>() {
            @Override
            public void onResponse(Call<MyPageResponse> call, Response<MyPageResponse> response) {

                MyPageResponse myPageResponse = response.body();

                seedAndstory(myPageResponse.getSeed_cnt(), myPageResponse.getStory_cnt());
                PixViewPagerSize();
            }

            @Override
            public void onFailure(Call<MyPageResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
            }
        });
    }

    private class pagerAdapter extends FragmentPagerAdapter {

        public pagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            if (object instanceof Fragment) {
                Fragment fragment = (Fragment) object;
                android.support.v4.app.FragmentManager fm = fragment.getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.remove(fragment);
                this.notifyDataSetChanged();
                ft.commitAllowingStateLoss();
            }
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;
            Bundle args = new Bundle();
            switch (position) {
                case 0:
                    fragment = new Page1();
                    //args.putString("user_email", user_email);
                    fragment.setArguments(args);
                    break;
                case 1:
                    fragment = new Page2();
                    break;
                case 2:
                    fragment = new Page3();
                    break;
                default:
                    return null;
            }

            return fragment;
        }


        // 생성 가능한 페이지 개수를 반환해준다.
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return NUM_PAGES;
        }
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
                    case R.id.setting_btn:
                        Intent intent = new Intent(getActivity(), Setting_Activity.class);
                        startActivity(intent);
                        break;

                }
            }
            return true;
        }
    };

}

package tab5;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.yssh1020.blossom.AppController;
import com.yssh1020.blossom.R;
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

    View v;

    public FragmentPage5() {
        // Required empty public constructor
    }


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

        InitViewPager();
    }

    private void InitViewPager(){
        //뷰페이저
        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        adapter = new pagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(temp);
        mViewPager.setOffscreenPageLimit(3);
        //viewPager의 크기를 단말기 해상도에 맞게 맞춰줌
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) mViewPager.getLayoutParams();
        params.width = AppController.getInstance().getDISPLAY_WIDTH();
        params.height = AppController.getInstance().getDISPLAY_HEIGHT();
        mViewPager.setLayoutParams(params);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                temp = position;

                switch (temp) {
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:

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

}

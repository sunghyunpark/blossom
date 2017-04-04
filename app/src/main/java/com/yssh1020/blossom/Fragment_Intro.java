package com.yssh1020.blossom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Fragment_Intro extends Fragment {

    private int position;
    ViewGroup rootView;
    public static Fragment_Intro create(int position) {
        Fragment_Intro fragment = new Fragment_Intro();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.intro_pager_childview, container, false);
        Set_FullSize_Background();
        return rootView;
    }

    /**
     * 단말기 해상도에 맞게 꽉찬 이미지 백그라운드 적용
     */
    private void Set_FullSize_Background(){

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.login_background_blur2);
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, AppController.getInstance().getDISPLAY_WIDTH(),
                AppController.getInstance().getDISPLAY_HEIGHT(), true);

        Drawable d = new BitmapDrawable(getResources(), resized);
        ViewGroup pager_child = (ViewGroup)rootView.findViewById(R.id.intro_child_img);
        pager_child.setBackground(d);
    }
}
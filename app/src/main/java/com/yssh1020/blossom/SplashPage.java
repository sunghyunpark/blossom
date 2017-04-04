package com.yssh1020.blossom;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;


public class SplashPage extends Activity {

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashpage);

        /**
         * 각 단말기 마다 해상도가 달라서 해당 단말기의 사이즈에 맞게 리사이징해서 스플래쉬를 해줌
         * 검은화면으로 나오는 이슈때문에...
         */
        int w = AppController.getInstance().getDISPLAY_WIDTH();
        int h = AppController.getInstance().getDISPLAY_HEIGHT();

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_intro_img);
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, w, h, true);

        Drawable d = new BitmapDrawable(getResources(), resized);
        ViewGroup splash_img = (ViewGroup)findViewById(R.id.splash_img);
        splash_img.setBackground(d);

        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();       // 2 초후 이미지를 닫아버림
            }
        }, 2000);
    }

}
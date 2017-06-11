package common;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.yssh1020.blossom.R;

public class ImageViewer extends Activity {

    CropView ImageViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageviewer);

        Intent intent = getIntent();
        String user_article_bg = intent.getExtras().getString("user_article_bg");
        ImageViewer = (CropView)findViewById(R.id.image_view_pic);

        Glide.with(getApplicationContext())
                .load(user_article_bg)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        ImageViewer.setImageBitmap(resource);
                    }
                });


    }

}
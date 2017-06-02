package tab3;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.yssh1020.blossom.AppController;
import com.yssh1020.blossom.R;

import java.io.File;

import api.ApiClient;
import api.ApiInterface;
import common.CommonUtil;
import event.BusProvider;
import event.SelectArticleBGEvent;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import model.CommonResponse;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Upload_Article extends Activity implements TextWatcher {

    //os6.0 permission
    private static final int REQUEST_PERMISSIONS_READ_EXTERNAL_STORAGE = 10;
    private ImageView bg_img;    //백그라운드 배경
    private EditText article_edit_box, article_edit_box2;
    private Button save_btn;
    private ViewGroup only_article_bg_layout, only_user_article_bg_layout;
    private ImageView back_btn, select_bg_btn, select_user_bg_btn, user_article_bg;
    private TextView article_length_txt;
    private String imgPath;
    private String flag = "article_bg";
    private String beforeStr;
    private int select_pos = 0;    //selectBG_Activity에서 선택한 아이템 position
    CommonUtil commonUtil = new CommonUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_article);

        BusProvider.getInstance().register(this);

        InitView();

    }

    private void InitView(){
        only_article_bg_layout = (ViewGroup)findViewById(R.id.only_article_bg_layout);
        only_user_article_bg_layout = (ViewGroup)findViewById(R.id.only_user_article_bg_layout);
        bg_img = (ImageView)findViewById(R.id.background_img);
        article_edit_box = (EditText)findViewById(R.id.article_text_edit_box);
        article_edit_box2 = (EditText)findViewById(R.id.article_text_edit_box2);
        save_btn = (Button)findViewById(R.id.save_btb);
        back_btn = (ImageView)findViewById(R.id.back_btn);
        select_bg_btn = (ImageView)findViewById(R.id.select_bg_btn);
        select_user_bg_btn = (ImageView)findViewById(R.id.select_user_bg_btn);
        user_article_bg = (ImageView)findViewById(R.id.user_article_bg_img);
        article_length_txt = (TextView)findViewById(R.id.article_length_txt);
        save_btn.setOnTouchListener(myOnTouchListener);
        back_btn.setOnTouchListener(myOnTouchListener);
        select_bg_btn.setOnTouchListener(myOnTouchListener);
        article_edit_box.addTextChangedListener(this);
        select_user_bg_btn.setOnTouchListener(myOnTouchListener);

        //키보드 자동 노출
        //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);

        imgPath = commonUtil.GetArticleBGName();
        LoadBackground("article_bg", imgPath);
    }

    /**
     * Background Img Load
     */
    private void LoadBackground(String flag, String imgPath){
        if(flag.equals("article_bg")){
            Picasso.with(getApplicationContext())
                    .load(imgPath)
                    .into(bg_img);
        }else{
            Glide.with(getApplicationContext())
                    .load(new File(imgPath))
                    .bitmapTransform(new BlurTransformation(getApplicationContext(), 50))
                    .error(null)
                    .into(bg_img);

        }
    }

    /**
     * 아티클 업로드
     * @param uid -> 사용자 id
     * @param article_text -> article 내용
     * @param article_photo -> article 사진
     */
    private void Upload_Article(String uid, String article_text, String article_photo){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<CommonResponse> call = apiService.PostArticle("upload",uid,article_text,article_photo);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                CommonResponse commonResponse = response.body();
                if(!commonResponse.isError()){
                    //Toast.makeText(getApplicationContext(), commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(getApplicationContext(), commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        // Always unregister when an object no longer should be on the bus.
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }
    @Subscribe
    public void FinishLoad(SelectArticleBGEvent mPushEvent) {
        flag = mPushEvent.getFlag();
        if(flag.equals("article_bg")){
            article_length_txt.setText("0/350");
            only_article_bg_layout.setVisibility(View.VISIBLE);
            only_user_article_bg_layout.setVisibility(View.GONE);
            imgPath = AppController.getInstance().getServer_img_path()+"article_bg/"+mPushEvent.getImgPath();
            select_pos = mPushEvent.getPos();
            LoadBackground(flag, imgPath);
        }else{
            article_length_txt.setText("0/100");
            only_article_bg_layout.setVisibility(View.GONE);
            only_user_article_bg_layout.setVisibility(View.VISIBLE);
            imgPath = mPushEvent.getImgPath();
            LoadBackground(flag, imgPath);

            Glide.clear(user_article_bg);
            Glide.with(getApplicationContext())
                    .load(new File(imgPath))
                    .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                    .into(user_article_bg);

        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.length() >= 350)
        {
            Toast.makeText(getApplicationContext(), "350자까지 입력 가능합니다.", Toast.LENGTH_SHORT).show();
            article_edit_box.setText(beforeStr);
        }
        article_length_txt.setText(s.length() + "/350자");
        article_length_txt.setTextColor(getResources().getColor(R.color.colorSky));
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        beforeStr = s.toString();

    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    /**
     * os 6.0 권한
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_READ_EXTERNAL_STORAGE:
                //권한이 있는 경우
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent_select_user_bg = new Intent(getApplicationContext(), Select_UserBG_Activity.class);
                    intent_select_user_bg.putExtra("flag", "user_article_bg");
                    startActivity(intent_select_user_bg);
                }
                //권한이 없는 경우
                else {
                    Toast.makeText(this, "퍼미션을 허용해야 이용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

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
                    case R.id.save_btb:
                        if(flag.equals("article_bg")){
                            String article_text_str = article_edit_box.getText().toString();
                            article_text_str = article_text_str.trim();
                            if(article_text_str.equals("") || article_text_str == null){
                                Toast.makeText(getApplicationContext(),String.format(res.getString(R.string.no_exist_text)),Toast.LENGTH_SHORT).show();
                            }else{
                                Upload_Article(User.getInstance().getUid(), article_text_str, imgPath);
                                finish();
                            }
                        }else{
                            String article_text_str = article_edit_box2.getText().toString();
                            article_text_str = article_text_str.trim();
                            if(article_text_str.equals("") || article_text_str == null){
                                Toast.makeText(getApplicationContext(),String.format(res.getString(R.string.no_exist_text)),Toast.LENGTH_SHORT).show();
                            }else{
                                Upload_Article(User.getInstance().getUid(), article_text_str, imgPath);
                                finish();
                            }
                        }
                        break;
                    case R.id.back_btn:
                        finish();
                        break;

                    case R.id.select_bg_btn:
                        Intent intent_select_bg = new Intent(getApplicationContext(),SelectBG_Activity.class);
                        intent_select_bg.putExtra("flag", "article_bg");
                        intent_select_bg.putExtra("select_pos", select_pos);
                        startActivity(intent_select_bg);
                        break;

                    case R.id.select_user_bg_btn:
                        /**
                         * os 6.0 권한체크 및 요청
                         */
                        if (ContextCompat.checkSelfPermission(Upload_Article.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                                .checkSelfPermission(Upload_Article.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale
                                    (Upload_Article.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                                    ActivityCompat.shouldShowRequestPermissionRationale(Upload_Article.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                                ActivityCompat.requestPermissions(Upload_Article.this,
                                        new String[]{Manifest.permission
                                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                        REQUEST_PERMISSIONS_READ_EXTERNAL_STORAGE);
                            } else {
                                ActivityCompat.requestPermissions(Upload_Article.this,
                                        new String[]{Manifest.permission
                                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                        REQUEST_PERMISSIONS_READ_EXTERNAL_STORAGE);

                            }

                        } else {
                            Intent intent_select_user_bg = new Intent(getApplicationContext(), Select_UserBG_Activity.class);
                            intent_select_user_bg.putExtra("flag", "user_article_bg");
                            startActivity(intent_select_user_bg);
                        }

                        break;

                }
            }
            return true;
        }
    };

}
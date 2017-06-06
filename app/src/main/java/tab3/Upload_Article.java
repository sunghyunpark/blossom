package tab3;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import api.ApiClient;
import api.ApiInterface;
import common.CommonUtil;
import common.Share_Activity;
import event.BusProvider;
import event.SelectArticleBGEvent;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import model.CommonResponse;
import model.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Upload_Article extends Activity implements TextWatcher {

    //os6.0 permission
    private static final int REQUEST_PERMISSIONS_READ_EXTERNAL_STORAGE = 10;
    private ImageView bg_img;    //백그라운드 배경
    private EditText article_edit_box, article_edit_box2;
    private ViewGroup only_article_bg_layout, only_user_article_bg_layout;
    private ImageView user_article_bg;
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
        Button save_btn = (Button)findViewById(R.id.save_btb);
        ImageView back_btn = (ImageView)findViewById(R.id.back_btn);
        ImageView select_bg_btn = (ImageView)findViewById(R.id.select_bg_btn);
        ImageView select_user_bg_btn = (ImageView)findViewById(R.id.select_user_bg_btn);
        user_article_bg = (ImageView)findViewById(R.id.user_article_bg_img);
        article_length_txt = (TextView)findViewById(R.id.article_length_txt);
        save_btn.setOnTouchListener(myOnTouchListener);
        back_btn.setOnTouchListener(myOnTouchListener);
        select_bg_btn.setOnTouchListener(myOnTouchListener);
        article_edit_box.addTextChangedListener(this);
        article_edit_box2.addTextChangedListener(this);
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
    private void Upload_Article(String uid, String article_text, final String article_photo){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        final String user_article_photo;
        if(flag.equals("article_bg")){
            user_article_photo = "N";
        }else{
            user_article_photo = "Y";
        }
        Call<CommonResponse> call = apiService.PostArticle("upload",uid,article_text,article_photo, user_article_photo);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                CommonResponse commonResponse = response.body();
                if(!commonResponse.isError()){
                    Toast.makeText(getApplicationContext(), "이야기가 등록되었습니다.", Toast.LENGTH_SHORT).show();
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

    /**
     * article 이미지 업로더
     * @param tag
     * @param img_name -> 이미지 파일명(랜덤으로 변환된것으로)
     * @param img_path -> 로컬에 저장된 이미지 경로
     */
    public void Upload_ArticleImage(String tag, final String img_name, final String img_path, final String article_text){

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        final File file = new File(img_path);
        final File file2 = new File(Environment.getExternalStorageDirectory()+"/Blossom/"+"resize_before.png");

        final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody Tag_body = RequestBody.create(MediaType.parse("multipart/form-data"), tag);
        MultipartBody.Part Image_body = MultipartBody.Part.createFormData("uploaded_file", img_name, requestFile);

        Call<CommonResponse> resultCall = apiService.Upload_Article_Image(Tag_body ,Image_body);

        resultCall.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                // Response Success or Fail
                if (response.isSuccessful()) {

                    if(!response.body().isError()){
                        Toast.makeText(getApplicationContext(), "이야기가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                        String img_path = AppController.getInstance().getServer_img_path()+"user_article_bg/"+img_name;
                        Upload_Article(User.getInstance().getUid(), article_text, img_path);
                        if(file.exists()){
                            file.delete();
                        }
                        if(file2.exists()){
                            file2.delete();
                        }

                    }else{
                        //Toast.makeText(getApplicationContext(), response.body().getError_msg(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    //Toast.makeText(getApplicationContext(), "response_fail", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "retrofit_fail", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
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
            //디폴트
            article_length_txt.setText("0/350");
            only_article_bg_layout.setVisibility(View.VISIBLE);
            only_user_article_bg_layout.setVisibility(View.GONE);
            imgPath = AppController.getInstance().getServer_img_path()+"article_bg/"+mPushEvent.getImgPath();
            select_pos = mPushEvent.getPos();
            LoadBackground(flag, imgPath);
        }else{
            //사용자 커스텀 사진
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

    /**
     * 비트맵을 로컬에 저장
     */
    private String getBitmapUploadImg_Path(String imagePath){

        //Bitmap resize_before;
        //resize_before = Bitmap.createScaledBitmap(mCurrentImg_bitmap, mCurrentImg_bitmap.getWidth(), mCurrentImg_bitmap.getHeight(), false);

        Bitmap image = BitmapFactory.decodeFile(imagePath);
        // 이미지를 상황에 맞게 회전시킨다
        try{
            ExifInterface exif = new ExifInterface(imagePath);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int exifDegree = commonUtil.exifOrientationToDegrees(exifOrientation);
            image = commonUtil.rotate(image, exifDegree);
        }catch (Exception e){

        }

        /**
         * 일단 resize_before비트맵을 로컬에 저장한다
         */
        File folder_path = new File(Environment.getExternalStorageDirectory()+"/Blossom/");
        if(!folder_path.exists()){
            folder_path.mkdir();
        }

        String resize_before_path = Environment.getExternalStorageDirectory()+"/Blossom/"+"resize_before.png";
        //로컬에 저장
        OutputStream outStream = null;
        File file = new File(resize_before_path);

        try{
            outStream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG,100,outStream);
            outStream.flush();
            outStream.close();
        }catch(FileNotFoundException e){

        }catch(IOException e){

        }

        /**
         * 저장된 resize_before비트맵의 가로/세로 길이를 가지고 크기에 맞게 리사이징 작업
         */
        //1080px이 최대길이, 가로/세로 중에 더 긴 길이를 기준으로 리사이징
        float size_check = 0;
        if( image.getHeight() >= image.getWidth() ) {
            size_check = image.getHeight();
        } else if( image.getHeight() < image.getWidth() ) {
            size_check = image.getWidth();
        }

        //1080px 최대길이를 넘는 이미지는 리사이징, compress 둘다 함
        if( size_check > 1080){

            String file_name = "resize_after.jpg";

            BitmapFactory.Options options = new BitmapFactory.Options();

            //리사이징 과정에서 단말기 메모리 오류 방지
            if( size_check > 4320 ){
                options.inSampleSize = 4;
            } else if( size_check > 3240 ){
                options.inSampleSize = 3;
            } else if( size_check > 2160 ){
                options.inSampleSize = 2;
            } else {
                options.inSampleSize = 1;
            }
            Bitmap resized_bitmap = BitmapFactory.decodeFile(resize_before_path, options);

            File fileCacheItem = new File(Environment.getExternalStorageDirectory()+"/Blossom/" + file_name);
            OutputStream out = null;

            try {
                float per = 1080/size_check/options.inSampleSize;

                float height=resized_bitmap.getHeight();
                float width=resized_bitmap.getWidth();
                fileCacheItem.createNewFile();
                out = new FileOutputStream(fileCacheItem);
                //resized_bitmap = Bitmap.createScaledBitmap(resized_bitmap, (int)(height*per), (int)(width*per), true);
                resized_bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return Environment.getExternalStorageDirectory()+"/Blossom/" + file_name;

        } else {
            try{
                outStream = new FileOutputStream(file);
                image.compress(Bitmap.CompressFormat.JPEG,80,outStream);
                outStream.flush();
                outStream.close();
            }catch(FileNotFoundException e){

            }catch(IOException e){

            }
            return resize_before_path;

        }

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(flag.equals("article_bg")){
            if(s.length() >= 350)
            {
                Toast.makeText(getApplicationContext(), "350자까지 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                article_edit_box.setText(beforeStr);
            }
            article_length_txt.setText(s.length() + "/350자");
            article_length_txt.setTextColor(getResources().getColor(R.color.colorSky));
        }else{
            if(s.length() >= 100)
            {
                Toast.makeText(getApplicationContext(), "100자까지 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                article_edit_box.setText(beforeStr);
            }
            article_length_txt.setText(s.length() + "/100자");
            article_length_txt.setTextColor(getResources().getColor(R.color.colorSky));
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        beforeStr = s.toString();

    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    private class ResizeBitmapTask extends AsyncTask<String, String, String> {

        Bitmap bit;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... mes){
            String path = mes[0];
            String article_text = mes[1];

            //BitmapFactory.Options bfo = new BitmapFactory.Options();
            //bfo.inSampleSize = 1;
            //bit = BitmapFactory.decodeFile(path, bfo);
            String img_path = getBitmapUploadImg_Path(path);
            Upload_ArticleImage("article", commonUtil.MakeImageName(User.getInstance().getUid()), img_path, article_text);
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
        @Override
        protected void onPostExecute(String res){
            //bit.recycle();

        }

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

                                ResizeBitmapTask resizeBitmapTask = new ResizeBitmapTask();
                                resizeBitmapTask.execute(imgPath, article_text_str);

                                //bit.recycle();
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
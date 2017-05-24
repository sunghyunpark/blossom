package common;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import com.yssh1020.blossom.AppController;
import com.yssh1020.blossom.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Share_Activity extends Activity {

    //os6.0 permission
    private static final int REQUEST_PERMISSIONS_READ_EXTERNAL_STORAGE = 10;
    private String image_path, article_text, from;
    private ViewGroup title_lay;
    private TextView app_logo_txt;

    private boolean share_flag = false;    //공유인지 아닌지 판별

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_activity);

        Intent intent = getIntent();
        image_path = intent.getExtras().getString("article_img");
        article_text = intent.getExtras().getString("article_text");
        from = intent.getExtras().getString("from");

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplicationContext());

        InitView();

    }

    private void InitView(){
        title_lay = (ViewGroup)findViewById(R.id.title_layout);

        ImageView article_picture = (ImageView)(findViewById(R.id.img));
        Picasso.with(getApplicationContext())
                .load(image_path)
                .into(article_picture);

        app_logo_txt = (TextView)findViewById(R.id.app_logo_txt);

        TextView article_text_txt = (TextView)findViewById(R.id.article_text);
        article_text_txt.setText(article_text);

        Button share_btn = (Button)findViewById(R.id.share_btn);
        share_btn.setOnTouchListener(myOnTouchListener);
        Button save_btn = (Button)findViewById(R.id.save_btn);
        save_btn.setOnTouchListener(myOnTouchListener);
        Button back_btn = (Button)findViewById(R.id.back_btn);
        back_btn.setOnTouchListener(myOnTouchListener);

        if(from.equals("save")){
            save_btn.setVisibility(View.VISIBLE);
        }else if(from.equals("share")){
            share_btn.setVisibility(View.VISIBLE);
        }
    }

    private class SaveArticle extends AsyncTask<Integer, Bitmap, Bitmap> {

        Bitmap bit;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Integer... position){

            bit = takeScreenshot();
            return bit;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
        @Override
        protected void onPostExecute(Bitmap res){
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String imageName = Environment.getExternalStorageDirectory()+"/Blossom/"+timeStamp+"_BLOSSOM.png";
            File folder_path = new File(Environment.getExternalStorageDirectory()+"/Blossom/");
            if(!folder_path.exists()){
                folder_path.mkdir();
            }
            //로컬에 저장
            OutputStream outStream = null;
            File file = new File(imageName);

            try{
                outStream = new FileOutputStream(file);
                res.compress(Bitmap.CompressFormat.PNG,100,outStream);
                outStream.flush();
                outStream.close();
            }catch(FileNotFoundException e){

            }catch(IOException e){

            }
            JMediaScanner scanner = new JMediaScanner(getApplicationContext());
            scanner.startScan(Environment.getExternalStorageDirectory()+"/Blossom/"+timeStamp+"_BLOSSOM.png");
            if(!share_flag){
                Toast.makeText(getApplicationContext(), "저장 완료", Toast.LENGTH_SHORT).show();
            }else{
                //Bitmap bit = BitmapFactory.decodeFile(imageName);
                //ShareFacebook(bit);

                ShareChooser(imageName);
            }
        }

    }
    public Bitmap takeScreenshot() {
        View rootView = findViewById(R.id.activity_main).getRootView();
        rootView.setDrawingCacheEnabled(true);
        Bitmap bmap = rootView.getDrawingCache();
        Rect statusBar = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(statusBar);
        Bitmap snapshot = Bitmap.createBitmap(bmap, 0, statusBar.top, bmap.getWidth(), bmap.getHeight() - statusBar.top, null, true);

        return snapshot;
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
                    title_lay.setVisibility(View.GONE);
                    app_logo_txt.setVisibility(View.VISIBLE);
                    try{
                        SaveArticle saveArticle = new SaveArticle();
                        saveArticle.execute();
                        finish();
                    }catch (Exception e){

                    }
                }
                //권한이 없는 경우
                else {
                    Toast.makeText(this, "퍼미션을 허용해야 이용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

        }
    }

    /**
     * sdcard에 사진을 저장 시 단말기 갤러리에 바로 갱신되게 해줌
     */
    public class JMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {
        private MediaScannerConnection mScanner;
        private String mFilepath = null;

        public JMediaScanner(Context context) {
            mScanner = new MediaScannerConnection(context, this);
        }

        public void startScan(String filepath) {
            mFilepath = filepath;
            mScanner.connect(); // 이 함수 호출 후 onMediaScannerConnected가 호출 됨.
        }

        @Override
        public void onMediaScannerConnected() {
            if(mFilepath != null) {
                String filepath = new String(mFilepath);
                mScanner.scanFile(filepath, null); // MediaStore의 정보를 업데이트
            }

            mFilepath = null;
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            mScanner.disconnect(); // onMediaScannerConnected 수행이 끝난 후 연결 해제
        }
    }

    private void ShareChooser(String mediaPath){

        String type = "image/*";
        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        // Create the URI from the media
        File media = new File(mediaPath);
        Uri uri = Uri.fromFile(media);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));

        finish();
    }

    /**
     * facebook share
     * @param image
     */
    private void ShareFacebook(Bitmap image){
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.show(content, ShareDialog.Mode.NATIVE);   //AUTOMATIC, FEED, NATIVE, WEB 등이 있으며 이는 다이얼로그 형식을 말합니다.

        finish();

    }

    private View.OnTouchListener myOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setAlpha(0.55f);
            }else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                v.setAlpha(1.0f);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.setAlpha(1.0f);
                switch(v.getId()){
                    case R.id.back_btn:
                        finish();
                        break;
                    case R.id.save_btn:
                        /**
                         * os 6.0 권한체크 및 요청
                         */
                        if (ContextCompat.checkSelfPermission(Share_Activity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                                .checkSelfPermission(Share_Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale
                                    (Share_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                                    ActivityCompat.shouldShowRequestPermissionRationale(Share_Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                                ActivityCompat.requestPermissions(Share_Activity.this,
                                        new String[]{Manifest.permission
                                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                        REQUEST_PERMISSIONS_READ_EXTERNAL_STORAGE);
                            } else {
                                ActivityCompat.requestPermissions(Share_Activity.this,
                                        new String[]{Manifest.permission
                                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                        REQUEST_PERMISSIONS_READ_EXTERNAL_STORAGE);

                            }

                        } else {
                            title_lay.setVisibility(View.GONE);
                            app_logo_txt.setVisibility(View.VISIBLE);
                            try{
                                SaveArticle saveArticle = new SaveArticle();
                                saveArticle.execute();
                                finish();
                            }catch (Exception e){

                            }
                        }
                        break;

                    case R.id.share_btn:
                        share_flag = true;
                        /**
                         * os 6.0 권한체크 및 요청
                         */
                        if (ContextCompat.checkSelfPermission(Share_Activity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                                .checkSelfPermission(Share_Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale
                                    (Share_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                                    ActivityCompat.shouldShowRequestPermissionRationale(Share_Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                                ActivityCompat.requestPermissions(Share_Activity.this,
                                        new String[]{Manifest.permission
                                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                        REQUEST_PERMISSIONS_READ_EXTERNAL_STORAGE);
                            } else {
                                ActivityCompat.requestPermissions(Share_Activity.this,
                                        new String[]{Manifest.permission
                                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                        REQUEST_PERMISSIONS_READ_EXTERNAL_STORAGE);

                            }

                        } else {
                            title_lay.setVisibility(View.GONE);
                            app_logo_txt.setVisibility(View.VISIBLE);
                            try{
                                SaveArticle saveArticle = new SaveArticle();
                                saveArticle.execute();

                            }catch (Exception e){

                            }
                        }
                        break;

                }
            }
            return true;
        }
    };
}
package common;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private String image_path, article_text;
    private ViewGroup title_lay;
    private TextView app_logo_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_activity);

        Intent intent = getIntent();
        image_path = intent.getExtras().getString("article_img");
        article_text = intent.getExtras().getString("article_text");

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

        Button save_btn = (Button)findViewById(R.id.save_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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


            }
        });
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
            Toast.makeText(getApplicationContext(), "저장 완료", Toast.LENGTH_SHORT).show();
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
}
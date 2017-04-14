package tab3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.yssh1020.blossom.AppController;
import com.yssh1020.blossom.R;
import api.ApiClient;
import api.ApiInterface;
import event.BusProvider;
import event.SelectArticleBGEvent;
import model.CommonResponse;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Upload_Article extends Activity {

    private ImageView bg_img;    //백그라운드 배경
    private EditText article_edit_box;
    private Button save_btn;
    private ImageView back_btn, select_bg_btn;
    private String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_article);

        BusProvider.getInstance().register(this);

        InitView();

    }

    private void InitView(){
        bg_img = (ImageView)findViewById(R.id.background_img);
        article_edit_box = (EditText)findViewById(R.id.article_text_edit_box);
        save_btn = (Button)findViewById(R.id.save_btb);
        back_btn = (ImageView)findViewById(R.id.back_btn);
        select_bg_btn = (ImageView)findViewById(R.id.select_bg_btn);
        save_btn.setOnTouchListener(myOnTouchListener);
        back_btn.setOnTouchListener(myOnTouchListener);
        select_bg_btn.setOnTouchListener(myOnTouchListener);

        LoadBackground(AppController.getInstance().getServer_img_path()+"/article_bg/article_bg_1.jpg");
    }

    /**
     * Background Img Load
     */
    private void LoadBackground(String imgPath){
        Picasso.with(getApplicationContext())
                .load(imgPath)
                .into(bg_img);
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
                    Toast.makeText(getApplicationContext(), commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();
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
        imgPath = mPushEvent.getImgPath();
        LoadBackground(AppController.getInstance().getServer_img_path()+"/article_bg/"+imgPath);
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
                    case R.id.save_btb:
                        String article_text_str = article_edit_box.getText().toString();
                        Upload_Article(User.getInstance().getUid(), article_text_str, "img/img3.jpg" );
                        finish();
                        break;
                    case R.id.back_btn:
                        finish();
                        break;

                    case R.id.select_bg_btn:
                        startActivity(new Intent(getApplicationContext(), SelectBG_Activity.class));
                        break;

                }
            }
            return true;
        }
    };

}
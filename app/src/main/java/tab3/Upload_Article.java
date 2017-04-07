package tab3;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yssh1020.blossom.AppController;
import com.yssh1020.blossom.R;

import model.User;


public class Upload_Article extends Activity {

    private ImageView bg_img;    //백그라운드 배경
    private EditText article_edit_box;
    private Button save_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_article);

        InitView();

    }

    private void InitView(){
        bg_img = (ImageView)findViewById(R.id.background_img);
        article_edit_box = (EditText)findViewById(R.id.article_text_edit_box);
        save_btn = (Button)findViewById(R.id.save_btb);
        save_btn.setOnTouchListener(myOnTouchListener);

        LoadBackground();
    }

    /**
     * Background Img Load
     */
    private void LoadBackground(){
        Picasso.with(getApplicationContext())
                .load(AppController.getInstance().getServer_base_ip()+"img/img3.jpg")
                .into(bg_img);
    }

    private void Upload_Article(String udi, String article_text, String article_photo){

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
                        String article_text_str = article_edit_box.getText().toString();
                        Upload_Article(User.getInstance().getUid(), article_text_str,
                                AppController.getInstance().getServer_base_ip()+"img/img3.jpg" );
                        finish();
                        break;

                }
            }
            return true;
        }
    };

}
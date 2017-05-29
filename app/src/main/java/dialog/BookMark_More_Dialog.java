package dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.yssh1020.blossom.R;

import common.Share_Activity;
import event.BusProvider;
import event.MyBookMarkCancelEvent;


/**
 * 아티클내에서 더보기 버튼 탭했을 때 노출되는 다이얼로그
 */

public class BookMark_More_Dialog extends Activity {

    private String article_id;
    private String article_img;
    private String article_text;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//위의 타이틀바 제거인데 setContentView 전에 넣어줘야함 뷰가 생성되기전에 제거되어야하므로...
        setContentView(R.layout.bookmark_more_dialog);

        Intent intent = getIntent();
        article_id = intent.getExtras().getString("article_id");
        article_img = intent.getExtras().getString("article_img");
        article_text = intent.getExtras().getString("article_text");
        pos = intent.getExtras().getInt("pos");

    }

    //button event
    public void buttonPressed(View v) {
        switch ((v.getId())){
            case R.id.cancel_bookmark_btn:
                BusProvider.getInstance().post(new MyBookMarkCancelEvent(article_id, pos));
                finish();
                break;
            case R.id.share_sns_btn:
                Intent intent = new Intent(getApplicationContext(), Share_Activity.class);
                intent.putExtra("from", "share");
                intent.putExtra("article_img", article_img);
                intent.putExtra("article_text", article_text);
                startActivity(intent);
                finish();
                break;

        }

    }


}
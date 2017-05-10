package dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.yssh1020.blossom.R;

/**
 * 북마크한 아티클 더보기 버튼 탭
 */

public class BookMark_More_Dialog extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//위의 타이틀바 제거인데 setContentView 전에 넣어줘야함 뷰가 생성되기전에 제거되어야하므로...
        setContentView(R.layout.bookmark_more_dialog);


    }

    //button event
    public void buttonPressed(View v) {
        switch ((v.getId())){
            case R.id.cancel_bookmark_btn:
                finish();
                break;
            case R.id.share_sns_btn:
                finish();
                break;

        }

    }


}
package dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.yssh1020.blossom.R;

/**
 * 내 아티클 삭제하기 버튼 탭 시 확인용 다이얼로그 노출
 */

public class Article_Delete_Cancel_Dialog extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//위의 타이틀바 제거인데 setContentView 전에 넣어줘야함 뷰가 생성되기전에 제거되어야하므로...
        setContentView(R.layout.article_delete_cancel_dialog);


    }

    //button event
    public void buttonPressed(View v) {
        switch ((v.getId())){
            case R.id.ok_btn:
                finish();
                break;
            case R.id.cancel_btn:
                finish();
                break;

        }

    }


}
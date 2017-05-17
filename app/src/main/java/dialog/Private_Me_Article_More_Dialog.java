package dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.yssh1020.blossom.R;

import common.CommonUtil;


public class Private_Me_Article_More_Dialog extends Activity {


    private String article_id;
    private int pos;
    CommonUtil commonUtil = new CommonUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//위의 타이틀바 제거인데 setContentView 전에 넣어줘야함 뷰가 생성되기전에 제거되어야하므로...
        setContentView(R.layout.private_me_article_more_dialog);

        Intent intent = getIntent();
        article_id = intent.getExtras().getString("article_id");
        pos = intent.getExtras().getInt("pos");


    }

    //button event
    public void buttonPressed(View v) {
        switch ((v.getId())){
            case R.id.change_public_btn:
                commonUtil.MyArticlePrivateModeChange(getApplicationContext(), article_id, "N");
                finish();
                break;
            case R.id.delete_btn:
                Intent intent = new Intent(getApplicationContext(), Article_Delete_Cancel_Dialog.class);
                intent.putExtra("article_id", article_id);
                intent.putExtra("pos", pos);
                startActivity(intent);
                finish();
                break;

        }

    }


}
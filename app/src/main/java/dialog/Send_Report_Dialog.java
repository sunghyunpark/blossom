package dialog;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.yssh1020.blossom.R;

import common.CommonUtil;
import model.User;

/**
 * 아티클 신고 다이얼로그
 */

public class Send_Report_Dialog extends Activity {

    private String article_id;
    CommonUtil commonUtil = new CommonUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//위의 타이틀바 제거인데 setContentView 전에 넣어줘야함 뷰가 생성되기전에 제거되어야하므로...
        setContentView(R.layout.send_report_dialog);

        Intent intent = getIntent();
        article_id = intent.getExtras().getString("article_id");


    }

    //button event
    public void buttonPressed(View v) {
        String  report_msg = "";
        Resources res = getResources();
        switch ((v.getId())){
            case R.id.send_report_1:
                report_msg = String.format(res.getString(R.string.send_report_btn_1_txt));
                finish();
                break;
            case R.id.send_report_2:
                report_msg = String.format(res.getString(R.string.send_report_btn_2_txt));
                finish();
                break;
            case R.id.send_report_3:
                report_msg = String.format(res.getString(R.string.send_report_btn_3_txt));
                finish();
                break;
            case R.id.send_report_4:
                report_msg = String.format(res.getString(R.string.send_report_btn_4_txt));
                finish();
                break;
            case R.id.send_report_5:
                report_msg = String.format(res.getString(R.string.send_report_btn_5_txt));
                finish();
                break;
        }
        commonUtil.SendArticleReport(getApplicationContext(), article_id, User.getInstance().getUid(), report_msg);
    }

}
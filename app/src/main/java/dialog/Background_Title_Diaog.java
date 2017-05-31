package dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yssh1020.blossom.MainActivity;
import com.yssh1020.blossom.R;
import com.yssh1020.blossom.SessionManager;

import common.CommonUtil;
import db.RealmConfig;
import db.model.UserData;
import event.BusProvider;
import event.EditBackgroundTitleEvent;
import io.realm.Realm;
import model.User;

/**
 * tab5에서 상단 타이틀 다이얼로그
 */

public class Background_Title_Diaog extends Activity implements TextWatcher {

    private EditText background_title_edit_box;
    private TextView title_cnt_txt;
    private String beforeStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//위의 타이틀바 제거인데 setContentView 전에 넣어줘야함 뷰가 생성되기전에 제거되어야하므로...
        setContentView(R.layout.background_title_dialog);

        InitView();

    }

    private void InitView(){
        background_title_edit_box = (EditText)findViewById(R.id.background_title_edit_box);
        title_cnt_txt = (TextView)findViewById(R.id.title_cnt_txt);
        background_title_edit_box.addTextChangedListener(this);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.length() >= 20)
        {
            background_title_edit_box.setText(beforeStr);
        }
        title_cnt_txt.setText(s.length() + "/20");
        title_cnt_txt.setTextColor(getResources().getColor(R.color.colorGray));
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        beforeStr = s.toString();

    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    //button event
    public void buttonPressed(View v) {
        switch ((v.getId())){
            case R.id.ok_btn:
                String title_str = background_title_edit_box.getText().toString();
                title_str = title_str.trim();
                if(title_str.equals("")){
                    Toast.makeText(getApplicationContext(),"내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    CommonUtil commonUtil = new CommonUtil();
                    commonUtil.EditBackgroundTitle(getApplicationContext(), User.getInstance().getUid(), title_str);
                    BusProvider.getInstance().post(new EditBackgroundTitleEvent(title_str));
                    finish();
                }
                break;
            case R.id.cancel_btn:
                finish();
                break;

        }

    }

}
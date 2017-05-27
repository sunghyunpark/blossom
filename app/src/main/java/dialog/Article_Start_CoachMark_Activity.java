package dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.yssh1020.blossom.AppSettingManager;
import com.yssh1020.blossom.R;

/**
 * 내 아티클 삭제하기 버튼 탭 시 확인용 다이얼로그 노출
 */

public class Article_Start_CoachMark_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_start_coachmark_activity);

        AppSettingManager appSettingManager = new AppSettingManager(getApplicationContext());
        appSettingManager.setCoachMarkArticleStartState(false);

        ViewGroup coach_mark_layout = (ViewGroup)findViewById(R.id.coach_mark_layout);
        coach_mark_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });
    }

}
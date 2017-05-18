package tab5;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.yssh1020.blossom.R;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class OpenSource_Activity extends Activity {

    ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opensource_activity);

        back_btn = (ImageView)findViewById(R.id.back_btn);
        back_btn.setOnTouchListener(myOnTouchListener);

        try{

            // getResources().openRawResource()로 raw 폴더의 원본 파일을 가져온다.
            // txt 파일을 InpuStream에 넣는다. (open 한다)
            InputStream in = getResources().openRawResource(R.raw.open_source);

            if(in != null){

                InputStreamReader stream = new InputStreamReader(in, "utf-8");
                BufferedReader buffer = new BufferedReader(stream);

                String read;
                StringBuilder sb = new StringBuilder("");

                while((read=buffer.readLine())!=null){
                    sb.append(read);
                    sb.append('\n');
                }

                in.close();

                // id : textView01 TextView를 불러와서
                //메모장에서 읽어온 문자열을 등록한다.
                TextView open_source_txt = (TextView)findViewById(R.id.open_source_txt);
                open_source_txt.setText(sb.toString());
            }

        }catch(Exception e){
            e.printStackTrace();
        }
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
                    case R.id.back_btn:
                        finish();
                        break;

                }
            }
            return true;
        }
    };


}
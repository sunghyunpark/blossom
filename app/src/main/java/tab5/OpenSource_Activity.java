package tab5;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.yssh1020.blossom.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class OpenSource_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opensource_activity);

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


}
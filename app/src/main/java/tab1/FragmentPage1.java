package tab1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.yssh1020.blossom.R;

import java.util.ArrayList;

import adapter.CardsDataAdapter;
import cardstack.CardStack;
import model.Article;

/**
 * created by sunghyun 2017-03-27
 * 참고 오픈소스
 * https://github.com/wenchaojiang/AndroidSwipeableCardStack
 */
public class FragmentPage1 extends Fragment {

    private CardStack mCardStack;
    private CardsDataAdapter mCardAdapter;

    private ArrayList<Article> listItems;
    View v;

    public FragmentPage1() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragmentpage1, container, false);

        Bundle bundle = getArguments();
        if(bundle != null){
            String msg = bundle.getString("KEY_MSG");
            if(msg != null){

            }
        }

        mCardStack = (CardStack) v.findViewById(R.id.container);

        mCardStack.setContentResource(R.layout.card_content);
        //mCardStack.setEnableLoop(true);
        //mCardStack.setEnableRotation(true);
//        mCardStack.setStackMargin(20);

        listItems = new ArrayList<Article>();
        Article a = new Article();
        a.setArticle_text("test1");
        a.setArticle_created_at("123");
        a.setArticle_picture("asdf");
        listItems.add(a);
        Article a2 = new Article();
        a2.setArticle_text("test2");
        a2.setArticle_created_at("123");
        a2.setArticle_picture("asdf");
        listItems.add(a2);

        mCardAdapter = new CardsDataAdapter(getActivity(), listItems);
        mCardAdapter.add(listItems.get(0));
        mCardAdapter.add(listItems.get(1));

        mCardStack.setAdapter(mCardAdapter);
        mCardStack.setVisibleCardNum(2);
        if (mCardStack.getAdapter() != null) {
            Log.i("MyActivity", "Card Stack size: " + mCardStack.getAdapter().getCount());
        }



        return v;
    }

}

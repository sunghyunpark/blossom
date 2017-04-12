package tab1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.yssh1020.blossom.R;
import java.util.ArrayList;
import adapter.CardsDataAdapter;
import api.ApiClient;
import api.ApiInterface;
import cardstack.CardStack;
import model.Article;
import model.ArticleResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        LoadArticle();

        return v;
    }

    private void LoadArticle(){
        mCardStack = (CardStack) v.findViewById(R.id.container);
        mCardStack.setContentResource(R.layout.card_content);
        //mCardStack.setEnableLoop(true);
        //mCardStack.setEnableRotation(true);
        //mCardStack.setStackMargin(20);

        listItems = new ArrayList<Article>();
        mCardAdapter = new CardsDataAdapter(getActivity(), listItems);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ArticleResponse> call = apiService.GetArticle("article");
        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {

                ArticleResponse articleResponse = response.body();
                if(!articleResponse.isError()){
                    Toast.makeText(getActivity(), articleResponse.getError_msg(),Toast.LENGTH_SHORT).show();

                    int dataSize = articleResponse.getArticle().size();
                    Article article;
                    for(int i=0;i<dataSize;i++){
                        article = new Article();
                        article.setArticle_id(articleResponse.getArticle().get(i).getArticle_id());
                        article.setUid(articleResponse.getArticle().get(i).getUid());
                        article.setArticle_text(articleResponse.getArticle().get(i).getArticle_text());
                        article.setArticle_photo(articleResponse.getArticle().get(i).getArticle_photo());
                        article.setArticle_created_at(articleResponse.getArticle().get(i).getArticle_created_at());
                        listItems.add(article);
                        mCardAdapter.add(listItems.get(i));
                    }
                    mCardStack.setAdapter(mCardAdapter);
                    mCardStack.setVisibleCardNum(2);
                    if (mCardStack.getAdapter() != null) {
                        Log.i("MyActivity", "Card Stack size: " + mCardStack.getAdapter().getCount());
                    }

                }else{
                    Toast.makeText(getActivity(), articleResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
            }
        });
    }

}

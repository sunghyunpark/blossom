package tab1;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.yssh1020.blossom.R;

import java.util.ArrayList;
import java.util.List;

import adapter.CardsDataAdapter;
import api.ApiClient;
import api.ApiInterface;
import cardstack.CardStack;
import model.Article;
import model.ArticleComment;
import model.ArticleResponse;
import model.User;
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
    public static SlidingUpPanelLayout mLayout;

    //리사이클러뷰
    RecyclerAdapter adapter;
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<ArticleComment> comment_listItems;


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
        if (bundle != null) {
            String msg = bundle.getString("KEY_MSG");
            if (msg != null) {

            }
        }
        InitCommentPanel();
        InitCommentList();

        LoadArticle();
        return v;
    }

    private void InitCommentPanel(){
        mLayout = (SlidingUpPanelLayout) v.findViewById(R.id.sliding_layout);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                // Log.i(TAG, "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        TextView t = (TextView) v.findViewById(R.id.name);
        t.setText("text");
        Button f = (Button) v.findViewById(R.id.follow);
        f.setText("button");
        f.setMovementMethod(LinkMovementMethod.getInstance());
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://www.twitter.com/umanoapp"));
                startActivity(i);
            }
        });
    }

    private void InitCommentList(){
        comment_listItems = new ArrayList<ArticleComment>();
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        adapter = new RecyclerAdapter(comment_listItems);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void LoadArticle() {
        mCardStack = (CardStack) v.findViewById(R.id.container);
        mCardStack.setContentResource(R.layout.card_content);
        //mCardStack.setEnableLoop(true);
        //mCardStack.setEnableRotation(true);
        //mCardStack.setStackMargin(20);

        listItems = new ArrayList<Article>();
        mCardAdapter = new CardsDataAdapter(getActivity(), listItems, mLayout);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ArticleResponse> call = apiService.GetArticle("article", User.getInstance().getUid());
        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {

                ArticleResponse articleResponse = response.body();
                if (!articleResponse.isError()) {
                    Toast.makeText(getActivity(), articleResponse.getError_msg(), Toast.LENGTH_SHORT).show();

                    int dataSize = articleResponse.getArticle().size();
                    Article article;
                    for (int i = 0; i < dataSize; i++) {
                        article = new Article();
                        article.setArticle_id(articleResponse.getArticle().get(i).getArticle_id());
                        article.setUid(articleResponse.getArticle().get(i).getUid());
                        article.setArticle_text(articleResponse.getArticle().get(i).getArticle_text());
                        article.setArticle_photo(articleResponse.getArticle().get(i).getArticle_photo());
                        article.setLike_cnt(articleResponse.getArticle().get(i).getLike_cnt());
                        article.setLike_state(articleResponse.getArticle().get(i).getLike_state());
                        article.setCreated_at(articleResponse.getArticle().get(i).getCreated_at());
                        listItems.add(article);
                        mCardAdapter.add(listItems.get(i));
                    }
                    mCardStack.setAdapter(mCardAdapter);
                    mCardStack.setVisibleCardNum(2);
                    if (mCardStack.getAdapter() != null) {
                        Log.i("MyActivity", "Card Stack size: " + mCardStack.getAdapter().getCount());
                    }

                } else {
                    Toast.makeText(getActivity(), articleResponse.getError_msg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
            }
        });
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_ITEM_ARTICLE_COMMENT = 0;
        private Resources res = getResources();

        List<ArticleComment> listItems;

        public RecyclerAdapter(List<ArticleComment> listItems) {
            this.listItems = listItems;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM_ARTICLE_COMMENT) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_tab5_my_article, parent, false);
                return new ArticleComment_VHitem(v);
            }
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }

        private ArticleComment getItem(int position) {
            return listItems.get(position);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            if (holder instanceof ArticleComment_VHitem) {
                final ArticleComment currentItem = getItem(position);
                final ArticleComment_VHitem VHitem = (ArticleComment_VHitem)holder;


            }
        }

        public class ArticleComment_VHitem extends RecyclerView.ViewHolder{

            private ImageView article_background_img;
            private TextView article_text;
            private TextView article_etc_text;

            public ArticleComment_VHitem(View itemView){
                super(itemView);
                article_background_img = (ImageView)itemView.findViewById(R.id.article_background_img);
                article_text = (TextView)itemView.findViewById(R.id.article_text);
                article_etc_text = (TextView)itemView.findViewById(R.id.article_etc_txt);

            }

        }
        /*
        private void removeItem(int position){
            common.DeleteMyArticle(getActivity(), user_uid, getItem(position).getArticle_id());
            listItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, listItems.size());
            if(listItems.size() == 0){
                empty_layout.setVisibility(View.VISIBLE);
            }

        }*/
        @Override
        public int getItemViewType(int position) {
            return TYPE_ITEM_ARTICLE_COMMENT;
        }
        //increasing getItemcount to 1. This will be the row of header.
        @Override
        public int getItemCount() {
            return listItems.size();
        }
    }

}
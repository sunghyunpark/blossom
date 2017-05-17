package tab1;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.otto.Subscribe;
import com.yssh1020.blossom.AppController;
import com.yssh1020.blossom.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import adapter.CardsDataAdapter;
import api.ApiClient;
import api.ApiInterface;
import cardstack.CardStack;
import common.CommonUtil;
import event.BusProvider;
import event.MyArticleDeleteEvent;
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

    public static CardStack mCardStack;
    public static CardsDataAdapter mCardAdapter;
    public static SlidingUpPanelLayout mLayout;

    private EditText comment_edit_box;
    private Button comment_send_btn;

    //리사이클러뷰
    RecyclerAdapter adapter;
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<ArticleComment> comment_listItems;
    private ViewGroup comment_empty_layout;

    public static ArrayList<Article> listItems;
    public static String LastArticleID = "";
    private static final int LOAD_COMMENT_DATA_COUNT = 10;
    View v;
    CommonUtil commonUtil = new CommonUtil();


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

        InitArticleView();
        LoadArticle("0");
        return v;
    }

    private void InitCommentPanel(){
        final ViewGroup comment_edit_layout = (ViewGroup) v.findViewById(R.id.comment_edit_layout);
        comment_edit_box = (EditText)v.findViewById(R.id.comment_edit_box);
        comment_send_btn = (Button)v.findViewById(R.id.comment_send_btn);
        comment_send_btn.setOnTouchListener(myOnTouchListener);

        mLayout = (SlidingUpPanelLayout) v.findViewById(R.id.sliding_layout);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("panel", "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i("panel", "onPanelStateChanged " + newState);

                // panel이 노출되면 댓글 입력창도 노출되게...
                if((newState == SlidingUpPanelLayout.PanelState.COLLAPSED) || (newState == SlidingUpPanelLayout.PanelState.EXPANDED)
                        || (newState == SlidingUpPanelLayout.PanelState.DRAGGING)){
                    comment_edit_layout.setVisibility(View.VISIBLE);
                }else{
                    comment_edit_layout.setVisibility(View.GONE);
                }

            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
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

    private void InitArticleView(){
        mCardStack = (CardStack) v.findViewById(R.id.container);
        mCardStack.setContentResource(R.layout.card_content);
        comment_empty_layout = (ViewGroup)v.findViewById(R.id.empty_layout);
        //mCardStack.setEnableLoop(true);
        //mCardStack.setEnableRotation(true);
        //mCardStack.setStackMargin(20);

        listItems = new ArrayList<Article>();
        mCardAdapter = new CardsDataAdapter(getActivity(), listItems, mLayout, recyclerView, adapter, comment_listItems, comment_empty_layout);
    }

    private void LoadArticle(String last_article_id) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ArticleResponse> call = apiService.GetArticle("article", User.getInstance().getUid(),last_article_id);
        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {

                ArticleResponse articleResponse = response.body();
                if (!articleResponse.isError()) {

                    int dataSize = articleResponse.getArticle().size();
                    Article article;
                    Collections.shuffle(articleResponse.getArticle());
                    for (int i = 0; i < dataSize; i++) {
                        article = new Article();
                        article.setArticle_id(articleResponse.getArticle().get(i).getArticle_id());
                        article.setUid(articleResponse.getArticle().get(i).getUid());
                        article.setArticle_text(articleResponse.getArticle().get(i).getArticle_text());
                        article.setArticle_photo(articleResponse.getArticle().get(i).getArticle_photo());
                        article.setLike_cnt(articleResponse.getArticle().get(i).getLike_cnt());
                        article.setLike_state(articleResponse.getArticle().get(i).getLike_state());
                        article.setBookmark_state(articleResponse.getArticle().get(i).getBookmark_state());
                        article.setComment_cnt(articleResponse.getArticle().get(i).getComment_cnt());
                        article.setCreated_at(articleResponse.getArticle().get(i).getCreated_at());
                        listItems.add(article);
                        mCardAdapter.add(listItems.get(i));
                    }
                    LastArticleID = articleResponse.getLast_article_id();
                    mCardAdapter.notifyDataSetChanged();
                    mCardStack.setAdapter(mCardAdapter);
                    //mCardStack.setVisibleCardNum(2);
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

    public static void LoadMoreArticle(final String last_article_id) {
        Log.d("LoadMoreArticleData", "Load More Data Start");
        Log.d("LoadMoreArticleData", "last_article_id : "+last_article_id);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ArticleResponse> call = apiService.GetArticle("article", User.getInstance().getUid(),last_article_id);
        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {

                ArticleResponse articleResponse = response.body();
                if (!articleResponse.isError()) {
                    Log.d("LoadMoreArticleData", "success");
                    int dataSize = articleResponse.getArticle().size();
                    int before_pos = FragmentPage1.listItems.size();
                    Article article;
                    Collections.shuffle(articleResponse.getArticle());
                    for (int i = 0; i < dataSize; i++) {
                        article = new Article();
                        article.setArticle_id(articleResponse.getArticle().get(i).getArticle_id());
                        article.setUid(articleResponse.getArticle().get(i).getUid());
                        article.setArticle_text(articleResponse.getArticle().get(i).getArticle_text());
                        article.setArticle_photo(articleResponse.getArticle().get(i).getArticle_photo());
                        article.setLike_cnt(articleResponse.getArticle().get(i).getLike_cnt());
                        article.setLike_state(articleResponse.getArticle().get(i).getLike_state());
                        article.setBookmark_state(articleResponse.getArticle().get(i).getBookmark_state());
                        article.setComment_cnt(articleResponse.getArticle().get(i).getComment_cnt());
                        article.setCreated_at(articleResponse.getArticle().get(i).getCreated_at());
                        FragmentPage1.listItems.add(article);
                        FragmentPage1.mCardAdapter.add(FragmentPage1.listItems.get(before_pos));
                        before_pos++;
                    }
                    Log.d("LoadMoreArticleData", "listSize : "+FragmentPage1.listItems.size());
                    FragmentPage1.LastArticleID = articleResponse.getLast_article_id();
                    FragmentPage1.mCardAdapter.notifyDataSetChanged();
                    FragmentPage1.mCardStack.setAdapter(mCardAdapter);
                    //mCardStack.setVisibleCardNum(2);
                    if (FragmentPage1.mCardStack.getAdapter() != null) {
                        Log.i("MyActivity", "Card Stack size: " + FragmentPage1.mCardStack.getAdapter().getCount());
                    }

                } else {
                    Log.d("LoadMoreArticleData", "not exist");
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
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_article_comment, parent, false);
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


                Glide.clear(VHitem.user_profile_img);
                Glide.with(getActivity())
                        .load(res.getIdentifier(currentItem.getProfile_img(), "mipmap", "com.yssh1020.blossom"))
                        .error(null)
                        .into(VHitem.user_profile_img);

                VHitem.comment_txt.setText(currentItem.getComment_text());


                Date to = null;
                try{
                    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    to = transFormat.parse(currentItem.getCreated_at());
                }catch (ParseException p){
                    p.printStackTrace();
                }
                VHitem.created_at_txt.setText(commonUtil.formatTimeString(to));

                if(currentItem.getUid().equals(User.getInstance().getUid())){
                    VHitem.comment_seed_btn.setVisibility(View.GONE);
                }else{
                    if(CurrentLikeState(position)){
                        //댓글 공감 상태
                        VHitem.comment_seed_btn.setBackgroundResource(R.mipmap.seed_click_img);
                    }else{
                        VHitem.comment_seed_btn.setBackgroundResource(R.mipmap.seed_img);
                    }
                }
                VHitem.comment_seed_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(CurrentLikeState(position)){
                            Toast.makeText(getActivity(), "이미 공감한 댓글입니다.", Toast.LENGTH_SHORT).show();
                        }else{
                            ChangeLikeState(false,position);
                            VHitem.comment_seed_btn.setBackgroundResource(R.mipmap.seed_click_img);
                        }
                        commonUtil.CommentLike(getActivity(), currentItem.getComment_id(), currentItem.getUid(), User.getInstance().getUid());
                    }
                });
            }
        }

        public class ArticleComment_VHitem extends RecyclerView.ViewHolder{

            private ImageView user_profile_img;
            private TextView comment_txt;
            private TextView created_at_txt;
            private ImageView comment_seed_btn;

            public ArticleComment_VHitem(View itemView){
                super(itemView);
                user_profile_img = (ImageView)itemView.findViewById(R.id.user_profile_img);
                comment_txt = (TextView)itemView.findViewById(R.id.comment_txt);
                created_at_txt = (TextView)itemView.findViewById(R.id.created_at_txt);
                comment_seed_btn = (ImageView)itemView.findViewById(R.id.comment_seed_btn);

            }

        }

        private boolean ChangeLikeState(boolean state, int position){
            if(state){
                state = false;
                getItem(position).setLike_state("N");
                return state;
            }else{
                state = true;
                getItem(position).setLike_state("Y");
                return state;
            }
        }
        private boolean CurrentLikeState(int position){
            boolean state = true;
            String state_str = getItem(position).getLike_state();

            if(state_str.equals("Y")){
                state = true;
            }else{
                state = false;
            }
            return state;
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
    private abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {


        private int previousTotal = 0; // The total number of items in the dataset after the last load
        private boolean loading = true; // True if we are still waiting for the last set of data to load.
        private int visibleThreshold = LOAD_COMMENT_DATA_COUNT; // The minimum amount of items to have below your current scroll position before loading more.
        int firstVisibleItem, visibleItemCount, totalItemCount;

        private int current_page = 1;

        private LinearLayoutManager mLinearLayoutManager;

        public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
            this.mLinearLayoutManager = linearLayoutManager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = mLinearLayoutManager.getItemCount();
            firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount)
                    <= (firstVisibleItem + visibleThreshold)) {
                // End has been reached

                // Do something
                current_page++;

                onLoadMore(current_page);

                loading = true;
            }
        }

        public abstract void onLoadMore(int current_page);
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
                    case R.id.comment_send_btn:
                        String comment_text_str = comment_edit_box.getText().toString();
                        if(!comment_text_str.equals("")){
                            commonUtil.InsertArticleComment(getActivity(), User.getInstance().getUid(),
                                    mCardAdapter.CurrentArticleID(), comment_text_str);
                            comment_edit_box.setText("");
                            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                            mCardAdapter.LoadArticleComment(mCardAdapter.CurrentArticleID(),"0");
                        }else{
                            Toast.makeText(getActivity(),String.format(res.getString(R.string.article_comment_empty_toast_txt)),Toast.LENGTH_SHORT).show();
                        }

                        break;

                }
            }
            return true;
        }
    };

}
package tab5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
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

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.yssh1020.blossom.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import api.ApiClient;
import api.ApiInterface;
import common.CommonUtil;
import model.Article;
import model.ArticleComment;
import model.ArticleCommentResponse;
import model.ArticleDetailResponse;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import view.CommonTabMenu;
import view.CommonTopTitle;


public class ArticleActivity extends Activity {

    private SlidingUpPanelLayout mLayout;
    private EditText comment_edit_box;
    private Button comment_send_btn;
    //리사이클러뷰
    RecyclerAdapter adapter;
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<ArticleComment> comment_listItems;
    CommonUtil commonUtil = new CommonUtil();

    private Article article = new Article();
    private String articleID;
    private ImageView background_img, article_comment_btn, article_like_btn, back_btn;
    private TextView article_text, created_at_txt, article_comment_txt, article_like_txt, top_title_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_activity);

        Intent intent = getIntent();
        articleID = intent.getExtras().getString("article_id");

        InitView();
        InitCommentPanel();
        InitCommentList();
        LoadArticleData(articleID);

    }

    private void InitView(){
        background_img = (ImageView)findViewById(R.id.background_img);
        article_comment_btn = (ImageView)findViewById(R.id.article_comment_btn);
        article_like_btn = (ImageView)findViewById(R.id.article_like_btn);
        article_text = (TextView)findViewById(R.id.article_text);
        created_at_txt = (TextView)findViewById(R.id.created_at_txt);
        article_comment_txt = (TextView)findViewById(R.id.article_comment_txt);
        article_like_txt = (TextView)findViewById(R.id.article_like_txt);
        top_title_txt = (TextView)findViewById(R.id.top_title_txt);
        back_btn = (ImageView)findViewById(R.id.back_btn);
        back_btn.setOnTouchListener(myOnTouchListener);
    }

    private void InitCommentPanel(){
        final ViewGroup comment_edit_layout = (ViewGroup) findViewById(R.id.comment_edit_layout);
        comment_edit_box = (EditText)findViewById(R.id.comment_edit_box);
        comment_send_btn = (Button)findViewById(R.id.comment_send_btn);
        comment_send_btn.setOnTouchListener(myOnTouchListener);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
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
                    top_title_txt.setVisibility(View.GONE);
                    back_btn.setVisibility(View.GONE);
                }else{
                    top_title_txt.setVisibility(View.VISIBLE);
                    back_btn.setVisibility(View.VISIBLE);
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
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        adapter = new RecyclerAdapter(comment_listItems);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void LoadArticleData(String article_id){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ArticleDetailResponse> call = apiService.GetArticleDetailData("article_detail", User.getInstance().getUid(), article_id);
        call.enqueue(new Callback<ArticleDetailResponse>() {
            @Override
            public void onResponse(Call<ArticleDetailResponse> call, Response<ArticleDetailResponse> response) {

                ArticleDetailResponse articleDetailResponse = response.body();
                if(!articleDetailResponse.isError()){
                    article.setUid(articleDetailResponse.getArticle_detail().getUid());
                    article.setArticle_text(articleDetailResponse.getArticle_detail().getArticle_text());
                    article.setArticle_photo(articleDetailResponse.getArticle_detail().getArticle_photo());
                    article.setComment_cnt(articleDetailResponse.getArticle_detail().getComment_cnt());
                    article.setLike_cnt(articleDetailResponse.getArticle_detail().getLike_cnt());
                    article.setLike_state(articleDetailResponse.getArticle_detail().getLike_state());
                    article.setCreated_at(articleDetailResponse.getArticle_detail().getCreated_at());

                    Picasso.with(getApplicationContext())
                            .load(article.getArticle_photo())
                            .into(background_img);
                    background_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN){
                                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                            }
                        }
                    });

                    article_text.setText(article.getArticle_text());

                    if(CurrentLikeState(article)){
                        //좋아요 일때
                        article_like_btn.setBackgroundResource(R.mipmap.like_btn);    //article_like_btn_img
                    }else{
                        article_like_btn.setBackgroundResource(R.mipmap.no_like_btn);    //article_not_like_btn_img
                    }
                    article_like_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            commonUtil.LikeArticle(getApplicationContext(), User.getInstance().getUid(), articleID, article.getLike_state());

                            if(article.getLike_state().equals("Y")){
                                ChangeLikeState(true, article);
                                article_like_btn.setBackgroundResource(R.mipmap.no_like_btn);
                            }else{
                                ChangeLikeState(false, article);
                                article_like_btn.setBackgroundResource(R.mipmap.like_btn);
                            }

                            article_like_txt.setText(article.getLike_cnt());

                        }
                    });
                    article_like_txt.setText(article.getLike_cnt());

                    article_comment_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //댓글 버튼 탭했을 때 패널
                            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            LoadArticleComment(articleID);
                        }
                    });

                    article_comment_txt.setText(article.getComment_cnt());

                    Date to = null;
                    try{
                        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        to = transFormat.parse(article.getCreated_at());
                    }catch (ParseException p){
                        p.printStackTrace();
                    }
                    created_at_txt.setText(commonUtil.formatTimeString(to));
                }else{
                    Toast.makeText(getApplicationContext().getApplicationContext(), articleDetailResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArticleDetailResponse> call, Throwable t) {
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

                VHitem.comment_txt.setText(currentItem.getComment_text());


            }
        }

        public class ArticleComment_VHitem extends RecyclerView.ViewHolder{

            private ImageView user_profile_img;
            private TextView comment_txt;
            private ImageView comment_seed_btn;

            public ArticleComment_VHitem(View itemView){
                super(itemView);
                user_profile_img = (ImageView)itemView.findViewById(R.id.user_profile_img);
                comment_txt = (TextView)itemView.findViewById(R.id.comment_txt);
                comment_seed_btn = (ImageView)itemView.findViewById(R.id.comment_seed_btn);

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

    private boolean CurrentLikeState(Article article){
        boolean state = true;
        String state_str = article.getLike_state();

        if(state_str.equals("Y")){
            state = true;
        }else{
            state = false;
        }
        return state;
    }

    private boolean ChangeLikeState(boolean state, Article article){
        int like_cnt = Integer.parseInt(article.getLike_cnt());
        if(state){
            state = false;
            like_cnt -= 1;
            article.setLike_state("N");
            article.setLike_cnt(""+like_cnt);
            return state;
        }else{
            like_cnt += 1;
            state = true;
            article.setLike_state("Y");
            article.setLike_cnt(""+like_cnt);
            return state;
        }
    }

    public void LoadArticleComment(String article_id) {
        if(comment_listItems != null){
            comment_listItems.clear();
        }
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ArticleCommentResponse> call = apiService.GetArticleComment("comment", article_id);
        call.enqueue(new Callback<ArticleCommentResponse>() {
            @Override
            public void onResponse(Call<ArticleCommentResponse> call, Response<ArticleCommentResponse> response) {

                ArticleCommentResponse articleCommentResponse = response.body();
                if (!articleCommentResponse.isError()) {
                    //Toast.makeText(getActivity(), articleCommentResponse.getError_msg(),Toast.LENGTH_SHORT).show();

                    int dataSize = articleCommentResponse.getArticle_comment().size();
                    ArticleComment articleComment;
                    for (int i = 0; i < dataSize; i++) {
                        articleComment = new ArticleComment();
                        articleComment.setComment_id(articleCommentResponse.getArticle_comment().get(i).getComment_id());
                        articleComment.setUid(articleCommentResponse.getArticle_comment().get(i).getUid());
                        articleComment.setUser_profile_img(articleCommentResponse.getArticle_comment().get(i).getUser_profile_img());
                        articleComment.setComment_text(articleCommentResponse.getArticle_comment().get(i).getComment_text());
                        articleComment.setLike_state(articleCommentResponse.getArticle_comment().get(i).getLike_state());
                        articleComment.setLike_cnt(articleCommentResponse.getArticle_comment().get(i).getLike_cnt());
                        articleComment.setCreated_at(articleCommentResponse.getArticle_comment().get(i).getCreated_at());
                        comment_listItems.add(articleComment);
                    }
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    //Toast.makeText(getActivity(), articleCommentResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ArticleCommentResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
            }
        });
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
                            commonUtil.InsertArticleComment(getApplicationContext(), User.getInstance().getUid(),
                                    articleID, comment_text_str);
                            comment_edit_box.setText("");
                            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                            LoadArticleComment(articleID);
                        }else{
                            Toast.makeText(getApplicationContext(),String.format(res.getString(R.string.article_comment_empty_toast_txt)),Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case R.id.back_btn:
                        finish();
                        break;

                }
            }
            return true;
        }
    };

}
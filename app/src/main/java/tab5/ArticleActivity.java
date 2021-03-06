package tab5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import common.ImageViewer;
import common.Share_Activity;
import dialog.Other_ArticleMoreDialog;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
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
    private String articleUID;
    private ImageView background_img, article_comment_btn, article_like_btn, back_btn, save_btn, more_btn;
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
        save_btn = (ImageView)findViewById(R.id.save_btn_img);
        more_btn = (ImageView)findViewById(R.id.more_btn);
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
        recyclerView.setAdapter(adapter);
    }

    private void LoadArticleData(String article_id){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ArticleDetailResponse> call = apiService.GetArticleDetailData("article_detail", User.getInstance().getUid(), article_id);
        call.enqueue(new Callback<ArticleDetailResponse>() {
            @Override
            public void onResponse(Call<ArticleDetailResponse> call, Response<ArticleDetailResponse> response) {

                final ArticleDetailResponse articleDetailResponse = response.body();
                if(!articleDetailResponse.isError()){
                    article.setUid(articleDetailResponse.getArticle_detail().getUid());
                    article.setArticle_text(articleDetailResponse.getArticle_detail().getArticle_text());
                    article.setArticle_photo(articleDetailResponse.getArticle_detail().getArticle_photo());
                    article.setComment_cnt(articleDetailResponse.getArticle_detail().getComment_cnt());
                    article.setLike_cnt(articleDetailResponse.getArticle_detail().getLike_cnt());
                    article.setLike_state(articleDetailResponse.getArticle_detail().getLike_state());
                    article.setCreated_at(articleDetailResponse.getArticle_detail().getCreated_at());
                    article.setBookmark_state(articleDetailResponse.getArticle_detail().getBookmark_state());

                    if(articleDetailResponse.getArticle_detail().getUser_article_photo().equals("Y")){
                        ImageView user_article_bg = (ImageView)(findViewById(R.id.user_article_bg_img));
                        user_article_bg.setVisibility(View.VISIBLE);
                        int dp = dpToPx(getApplicationContext(), 190);
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp);
                        params.setMargins(0,dpToPx(getApplicationContext(),250),0,0);
                        article_text.setLayoutParams(params);

                        Glide.clear(background_img);
                        Glide.with(getApplicationContext())
                                .load(articleDetailResponse.getArticle_detail().getArticle_photo())
                                .bitmapTransform(new BlurTransformation(getApplicationContext(), 50))
                                .into(background_img);

                        Glide.clear(user_article_bg);
                        Glide.with(getApplicationContext())
                                .load(articleDetailResponse.getArticle_detail().getArticle_photo())
                                .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                                .into(user_article_bg);

                        user_article_bg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getApplicationContext(), ImageViewer.class);
                                intent.putExtra("user_article_bg", articleDetailResponse.getArticle_detail().getArticle_photo());
                                startActivity(intent);
                            }
                        });


                    }else{
                        Picasso.with(getApplicationContext())
                                .load(article.getArticle_photo())
                                .into(background_img);
                    }

                    background_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN){
                                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                            }
                        }
                    });

                    articleUID = articleDetailResponse.getArticle_detail().getUid();

                    article_text.setText(article.getArticle_text());

                    //더보기 버튼
                    if(IsMyArticle(articleDetailResponse.getArticle_detail().getUid())){
                        more_btn.setVisibility(View.GONE);
                    }else{
                        more_btn.setVisibility(View.VISIBLE);
                    }
                    more_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //내 아티클이 아닌 경우
                            Intent intent_other = new Intent(getApplicationContext(), Other_ArticleMoreDialog.class);
                            intent_other.putExtra("article_id", articleDetailResponse.getArticle_detail().getArticle_id());
                            intent_other.putExtra("article_user_id", articleDetailResponse.getArticle_detail().getUid());
                            startActivity(intent_other);
                        }
                    });

                    //공유 버튼
                    Button article_share_btn = (Button)findViewById(R.id.article_share_btn);
                    article_share_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), Share_Activity.class);
                            intent.putExtra("from", "share");
                            intent.putExtra("user_article_photo", articleDetailResponse.getArticle_detail().getUser_article_photo());
                            intent.putExtra("article_img", articleDetailResponse.getArticle_detail().getArticle_photo());
                            intent.putExtra("article_text", articleDetailResponse.getArticle_detail().getArticle_text());
                            startActivity(intent);
                        }
                    });


                    //저장 버튼
                    save_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), Share_Activity.class);
                            intent.putExtra("article_img", articleDetailResponse.getArticle_detail().getArticle_photo());
                            intent.putExtra("article_text", articleDetailResponse.getArticle_detail().getArticle_text());
                            intent.putExtra("user_article_photo", articleDetailResponse.getArticle_detail().getUser_article_photo());
                            intent.putExtra("from", "save");
                            startActivity(intent);
                        }
                    });

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

                    created_at_txt.setText(commonUtil.formatTimeAMPM(article.getCreated_at()));
                }else{
                    //Toast.makeText(getApplicationContext().getApplicationContext(), articleDetailResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArticleDetailResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
            }
        });

    }
    private int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
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
                if(IsMyArticle(position)){
                    Glide.clear(VHitem.user_profile_img);
                    Glide.with(getApplicationContext())
                            .load(res.getIdentifier("my_article_user_profile", "mipmap", "com.yssh1020.blossom"))
                            .error(R.mipmap.user_profile_img1)
                            .into(VHitem.user_profile_img);
                }else{
                    Glide.clear(VHitem.user_profile_img);
                    Glide.with(getApplicationContext())
                            .load(res.getIdentifier(currentItem.getProfile_img(), "mipmap", "com.yssh1020.blossom"))
                            .error(R.mipmap.user_profile_img1)
                            .into(VHitem.user_profile_img);
                }


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

                VHitem.comment_like_cnt_txt.setText(currentItem.getLike_cnt());

                VHitem.comment_seed_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(CurrentLikeState(position)){
                            Toast.makeText(getApplicationContext(), "이미 공감한 댓글입니다.", Toast.LENGTH_SHORT).show();
                        }else{
                            ChangeLikeState(false,position);
                            VHitem.comment_seed_btn.setBackgroundResource(R.mipmap.seed_click_img);
                            commonUtil.CommentLike(getApplicationContext(), currentItem.getComment_id(), currentItem.getUid(), User.getInstance().getUid());
                        }

                    }
                });


            }
        }

        public class ArticleComment_VHitem extends RecyclerView.ViewHolder{

            private ImageView user_profile_img;
            private TextView comment_txt;
            private TextView created_at_txt;
            private ImageView comment_seed_btn;
            private TextView comment_like_cnt_txt;

            public ArticleComment_VHitem(View itemView){
                super(itemView);
                user_profile_img = (ImageView)itemView.findViewById(R.id.user_profile_img);
                comment_txt = (TextView)itemView.findViewById(R.id.comment_txt);
                created_at_txt = (TextView)itemView.findViewById(R.id.created_at_txt);
                comment_seed_btn = (ImageView)itemView.findViewById(R.id.comment_seed_btn);
                comment_like_cnt_txt = (TextView)itemView.findViewById(R.id.comment_like_cnt_txt);

            }

        }

        private boolean IsMyArticle(int position){
            boolean flag = true;

            if(articleUID.equals(getItem(position).getUid())){
                //아티클 작성자가 나 인경우
                flag = true;
            }else{
                flag = false;
            }
            return flag;
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

        Call<ArticleCommentResponse> call = apiService.GetArticleComment("comment", article_id, "0", User.getInstance().getUid());
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
                        articleComment.setProfile_img(articleCommentResponse.getArticle_comment().get(i).getProfile_img());
                        articleComment.setComment_text(articleCommentResponse.getArticle_comment().get(i).getComment_text());
                        articleComment.setLike_state(articleCommentResponse.getArticle_comment().get(i).getLike_state());
                        articleComment.setLike_cnt(articleCommentResponse.getArticle_comment().get(i).getLike_cnt());
                        articleComment.setCreated_at(articleCommentResponse.getArticle_comment().get(i).getCreated_at());
                        comment_listItems.add(articleComment);
                    }
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

    /**
     * 현재 아티클이 내가 작성한 아티클인지 판별
     * @param uid
     * @return
     */
    private boolean IsMyArticle(String uid){
        boolean flag = true;
        if(uid.equals(User.getInstance().getUid())){
            //아티클 작성자가 나 인경우
            flag = true;
        }else{
            flag = false;
        }
        return flag;
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
    @Override
    public void onBackPressed() {
        if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN){
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        }else {
            super.onBackPressed();
        }
    }

}
package adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.yssh1020.blossom.R;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import api.ApiClient;
import api.ApiInterface;
import common.CommonUtil;
import common.ImageViewer;
import common.Share_Activity;
import dialog.Me_ArticleMoreDialog;
import dialog.Other_ArticleMoreDialog;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import model.Article;
import model.ArticleComment;
import model.ArticleCommentResponse;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tab1.FragmentPage1;
import view.CommonTabMenu;

public class CardsDataAdapter extends ArrayAdapter<Article> {

    private ArrayList<Article> items;
    private Context mContext;
    private SlidingUpPanelLayout mLayout;
    private FragmentPage1.RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private String currentArticleID = "";
    private String currentArticlePHOTO = "";
    private String currentArticleTEXT = "";
    private String currentArticleUID = "";
    private String currentUserArticlePhoto = "";
    CommonUtil commonUtil = new CommonUtil();
    private ArrayList<ArticleComment> comment_listItems;
    private ViewGroup comment_empty_layout;

    public CardsDataAdapter(Context context, ArrayList<Article> items, SlidingUpPanelLayout mLayout, RecyclerView recyclerView,
                            FragmentPage1.RecyclerAdapter adapter, ArrayList<ArticleComment> comment_listItems, ViewGroup comment_empty_layout) {
        super(context, R.layout.card_content);
        this.mContext = context;
        this.items = items;
        this.mLayout = mLayout;
        this.adapter = adapter;
        this.recyclerView = recyclerView;
        this.comment_listItems = comment_listItems;
        this.comment_empty_layout = comment_empty_layout;

    }

    @Override
    public View getView(final int position, final View contentView, ViewGroup parent){
        View v = contentView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.card_content, null);
        }

        Article article = items.get(position);
        final TextView like_cnt_txt = (TextView)(v.findViewById(R.id.article_like_txt));
        if(article != null){
            TextView article_text = (TextView)(v.findViewById(R.id.article_text));
            article_text.setText(getItem(position).getArticle_text());

            //아티클 사진

            final ImageView article_picture = (ImageView)(v.findViewById(R.id.background_img));

            if(IsUserArticleBg(position)){
                ImageView user_article_bg = (ImageView)(v.findViewById(R.id.user_article_bg_img));
                user_article_bg.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(getContext(), 190));
                params.setMargins(0,dpToPx(getContext(),250),0,0);
                article_text.setLayoutParams(params);

                Glide.clear(article_picture);
                Glide.with(getContext())
                        .load(getItem(position).getArticle_photo())
                        .bitmapTransform(new BlurTransformation(getContext(), 50))
                        .into(article_picture);

                Glide.clear(user_article_bg);
                Glide.with(getContext())
                        .load(getItem(position).getArticle_photo())
                        .bitmapTransform(new CropCircleTransformation(getContext()))
                        .into(user_article_bg);
                user_article_bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), ImageViewer.class);
                        intent.putExtra("user_article_bg", getItem(position).getArticle_photo());
                        getContext().startActivity(intent);
                    }
                });


            }else{
                Picasso.with(getContext())
                        .load(getItem(position).getArticle_photo())
                        //.transform(PicassoTransformations.resizeTransformation)
                        .into(article_picture);
            }

            final ImageView bookmark_btn = (ImageView)(v.findViewById(R.id.bookmark_btn));
            if(CurrentBookMarkState(position)){
                //북마크 상태
                bookmark_btn.setBackgroundResource(R.mipmap.bookmark_click_btn);
            }else{
                bookmark_btn.setBackgroundResource(R.mipmap.bookmark_no_click_btn_img);
            }
            bookmark_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Resources res = mContext.getResources();
                    String toast_msg;
                    String state = getItem(position).getBookmark_state();
                    if(getItem(position).getBookmark_state().equals("Y")){
                        ChangeBookMarkState(true, position);
                        bookmark_btn.setBackgroundResource(R.mipmap.bookmark_no_click_btn_img);
                        toast_msg = String.format(res.getString(R.string.toast_bookmark_off));
                    }else{
                        ChangeBookMarkState(false, position);
                        bookmark_btn.setBackgroundResource(R.mipmap.bookmark_click_btn);
                        toast_msg = String.format(res.getString(R.string.toast_bookmark_on));
                    }
                    commonUtil.BookMarkArticle(mContext, User.getInstance().getUid(), getItem(position).getArticle_id(), state, toast_msg);

                }
            });

            //좋아요 버튼
            final ImageView like_btn = (ImageView)(v.findViewById(R.id.article_like_btn));
            if(CurrentLikeState(position)){
                //좋아요 일때
                like_btn.setBackgroundResource(R.mipmap.like_btn);    //article_like_btn_img
            }else{
                like_btn.setBackgroundResource(R.mipmap.no_like_btn);    //article_not_like_btn_img
            }
            like_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    commonUtil.LikeArticle(mContext, User.getInstance().getUid(), getItem(position).getArticle_id(), getItem(position).getLike_state());

                    if(getItem(position).getLike_state().equals("Y")){
                        ChangeLikeState(true, position);
                        like_btn.setBackgroundResource(R.mipmap.no_like_btn);
                    }else{
                        ChangeLikeState(false, position);
                        like_btn.setBackgroundResource(R.mipmap.like_btn);
                    }

                    like_cnt_txt.setText(getItem(position).getLike_cnt());

                }
            });
            //좋아요 갯수
            like_cnt_txt.setText(getItem(position).getLike_cnt());

            //댓글 버튼
            ImageView comment_btn = (ImageView)(v.findViewById(R.id.article_comment_btn));
            comment_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //댓글 버튼 탭했을 때 패널 노출시키면서 하단 공통 탭 미노출, 상단 타이틀 미노출
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    CommonTabMenu.getInstance().getBottom_menu().setVisibility(View.GONE);
                    currentArticleID = getItem(position).getArticle_id();
                    currentArticlePHOTO = getItem(position).getArticle_photo();
                    currentArticleTEXT = getItem(position).getArticle_text();
                    currentArticleUID = getItem(position).getUid();
                    currentUserArticlePhoto = getItem(position).getUser_article_photo();
                    LoadArticleComment(currentArticleID, "0");
                    recyclerView.setAdapter(adapter);
                }
            });

            ImageView save_btn = (ImageView)(v.findViewById(R.id.save_btn_img));
            save_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), Share_Activity.class);
                    intent.putExtra("article_img", getItem(position).getArticle_photo());
                    intent.putExtra("article_text", getItem(position).getArticle_text());
                    intent.putExtra("from", "save");
                    intent.putExtra("user_article_photo", getItem(position).getUser_article_photo());
                    mContext.startActivity(intent);
                }
            });


            TextView comment_txt = (TextView)(v.findViewById(R.id.article_comment_txt));
            comment_txt.setText(getItem(position).getComment_cnt());

            //더보기 버튼
            ImageView more_btn = (ImageView)(v.findViewById(R.id.more_btn));
            if(IsMyArticle(position)){
                more_btn.setVisibility(View.GONE);
            }else{
                more_btn.setVisibility(View.VISIBLE);
            }
            more_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //내 아티클이 아닌 경우
                    Intent intent_other = new Intent(getContext(), Other_ArticleMoreDialog.class);
                    intent_other.putExtra("article_id", getItem(position).getArticle_id());
                    intent_other.putExtra("article_user_id", getItem(position).getUid());
                    getContext().startActivity(intent_other);
                }
            });

            //작성 날짜
            TextView created_at_txt = (TextView)(v.findViewById(R.id.created_at_txt));

            created_at_txt.setText(commonUtil.formatTimeAMPM(getItem(position).getCreated_at()));

        }

        return v;
    }

    public String CurrentArticleUID(){
        return currentArticleUID;
    }
    public String CurrentArticleID(){
        return currentArticleID;
    }
    public String CurrentArticlePHOTO(){
        return currentArticlePHOTO;
    }
    public String CurrentArticleTEXT(){
        return currentArticleTEXT;
    }
    public String CurrentUserArticlePhto(){
        return currentUserArticlePhoto;
    }

    private int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    private int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    /**
     * 현재 아티클이 내가 작성한 아티클인지 판별
     * @param position
     * @return
     */
    private boolean IsMyArticle(int position){
        boolean flag = true;
        String article_writer_uid = getItem(position).getUid();

        if(article_writer_uid.equals(User.getInstance().getUid())){
            //아티클 작성자가 나 인경우
            flag = true;
        }else{
            flag = false;
        }
        return flag;
    }

    private boolean IsUserArticleBg(int position){
        boolean flag = false;
        String user_article_bg = getItem(position).getUser_article_photo();

        if(user_article_bg.equals("N")){
            flag = false;
        }else{
            flag = true;
        }
        return flag;
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

    private boolean CurrentBookMarkState(int position){
        boolean state = true;
        String state_str = getItem(position).getBookmark_state();

        if(state_str.equals("Y")){
            state = true;
        }else{
            state = false;
        }
        return state;
    }

    private boolean ChangeLikeState(boolean state, int position){
        int like_cnt = Integer.parseInt(getItem(position).getLike_cnt());
        if(state){
            state = false;
            like_cnt -= 1;
            getItem(position).setLike_state("N");
            getItem(position).setLike_cnt(""+like_cnt);
            return state;
        }else{
            like_cnt += 1;
            state = true;
            getItem(position).setLike_state("Y");
            getItem(position).setLike_cnt(""+like_cnt);
            return state;
        }
    }

    private boolean ChangeBookMarkState(boolean state, int position){
        if(state){
            state = false;
            getItem(position).setBookmark_state("N");
            return state;
        }else{
            state = true;
            getItem(position).setBookmark_state("Y");
            return state;
        }
    }
    /**
     * 해당 아티클 댓글 불러오기
     * @param article_id -> 해당 아이클의 id
     */
    public void LoadArticleComment(String article_id, String last_comment_id) {
        if(comment_listItems != null){
            comment_listItems.clear();
        }
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ArticleCommentResponse> call = apiService.GetArticleComment("comment", article_id, last_comment_id, User.getInstance().getUid());
        call.enqueue(new Callback<ArticleCommentResponse>() {
            @Override
            public void onResponse(Call<ArticleCommentResponse> call, Response<ArticleCommentResponse> response) {

                ArticleCommentResponse articleCommentResponse = response.body();
                if (!articleCommentResponse.isError()) {
                    comment_empty_layout.setVisibility(View.GONE);

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
                    comment_empty_layout.setVisibility(View.VISIBLE);
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
}
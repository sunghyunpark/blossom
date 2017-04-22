package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yssh1020.blossom.AppController;
import com.yssh1020.blossom.R;

import java.util.ArrayList;

import common.CommonUtil;
import common.PicassoTransformations;
import model.Article;
import model.User;

public class CardsDataAdapter extends ArrayAdapter<Article> {

    private ArrayList<Article> items;
    private Context mContext;
    CommonUtil commonUtil = new CommonUtil();


    public CardsDataAdapter(Context context, ArrayList<Article> items) {
        super(context, R.layout.card_content);
        this.mContext = context;
        this.items = items;

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

            ImageView article_picture = (ImageView)(v.findViewById(R.id.background_img));
            Picasso.with(getContext())
                    .load(getItem(position).getArticle_photo())
                    //.transform(PicassoTransformations.resizeTransformation)
                    .into(article_picture);

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

        }

        return v;
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
}
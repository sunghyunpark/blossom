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
        if(article != null){
            TextView article_text = (TextView)(v.findViewById(R.id.content));
            article_text.setText(getItem(position).getArticle_text());

            //아티클 사진

            ImageView article_picture = (ImageView)(v.findViewById(R.id.background_img));
            Picasso.with(getContext())
                    .load(getItem(position).getArticle_photo())
                    //.transform(PicassoTransformations.resizeTransformation)
                    .into(article_picture);

            //좋아요 버튼
            ImageView like_btn = (ImageView)(v.findViewById(R.id.like_btn));
            like_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    commonUtil.LikeArticle(mContext, getItem(position).getUid(), getItem(position).getArticle_id());

                }
            });
            //좋아요 갯수
            TextView like_cnt_txt = (TextView)(v.findViewById(R.id.like_txt));
            like_cnt_txt.setText(getItem(position).getLike_cnt());

        }

        return v;
    }

    private boolean ChangeLikeState(boolean state, int position){
        int like_cnt = Integer.parseInt(getItem(position).getLike_cnt());
        if(state){
            state = false;
            like_cnt -= 1;
            //getItem(position).setArticle_like_state("N");
            getItem(position).setLike_cnt(""+like_cnt);
            return state;
        }else{
            like_cnt += 1;
            state = true;
            //getItem(position).setArticle_like_state("Y");
            getItem(position).setLike_cnt(""+like_cnt);
            return state;
        }
    }
}
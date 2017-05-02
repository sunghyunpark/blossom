package tab5.viewpager;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yssh1020.blossom.AppController;
import com.yssh1020.blossom.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import api.ApiClient;
import api.ApiInterface;
import common.CommonUtil;
import jp.wasabeef.glide.transformations.BlurTransformation;
import model.Article;
import model.ArticleResponse;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * created by sunghyun 2017-03-27
 */
public class Page1 extends Fragment {

    //리사이클러뷰
    RecyclerAdapter adapter;
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Article> listItems;
    View v;

    CommonUtil commonUtil = new CommonUtil();

    public Page1() {
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
        v = inflater.inflate(R.layout.tab5_viewpager_page1, container, false);

        Bundle bundle = getArguments();
        if(bundle != null){
            String msg = bundle.getString("KEY_MSG");
            if(msg != null){

            }
        }
        InitView();
        return v;
    }

    private void InitView(){
        listItems = new ArrayList<Article>();
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        adapter = new RecyclerAdapter(listItems);
        recyclerView.setLayoutManager(linearLayoutManager);

        GetMyArticleData(User.getInstance().getUid());
    }

    private void GetMyArticleData(String uid){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ArticleResponse> call = apiService.GetMyArticle("my_article", uid);
        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {

                ArticleResponse articleResponse = response.body();
                if(!articleResponse.isError()){
                    int dataSize = articleResponse.getArticle().size();
                    Article article;
                    for(int i=0;i<dataSize;i++){
                        article = new Article();
                        article.setArticle_id(articleResponse.getArticle().get(i).getArticle_id());
                        article.setUid(articleResponse.getArticle().get(i).getUid());
                        article.setArticle_text(articleResponse.getArticle().get(i).getArticle_text());
                        article.setArticle_photo(articleResponse.getArticle().get(i).getArticle_photo());
                        article.setLike_cnt(articleResponse.getArticle().get(i).getLike_cnt());
                        article.setLike_state(articleResponse.getArticle().get(i).getLike_state());
                        article.setComment_cnt(articleResponse.getArticle().get(i).getComment_cnt());
                        article.setCreated_at(articleResponse.getArticle().get(i).getCreated_at());
                        listItems.add(article);
                    }
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity().getApplicationContext(), articleResponse.getError_msg(),Toast.LENGTH_SHORT).show();


                }else{
                    recyclerView.setNestedScrollingEnabled(false);
                    TextView my_story_empty_txt = (TextView)v.findViewById(R.id.my_story_empty_txt);
                    my_story_empty_txt.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity().getApplicationContext(), articleResponse.getError_msg(),Toast.LENGTH_SHORT).show();
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

        private static final int TYPE_ITEM_USER_ARTICLE = 0;
        private Resources res = getResources();

        List<Article> listItems;

        public RecyclerAdapter(List<Article> listItems) {
            this.listItems = listItems;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM_USER_ARTICLE) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_tab5_my_article, parent, false);
                return new MyArticle_VHitem(v);
            }
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }

        private Article getItem(int position) {
            return listItems.get(position);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            if (holder instanceof MyArticle_VHitem) {
                final Article currentItem = getItem(position);
                final MyArticle_VHitem VHitem = (MyArticle_VHitem)holder;

                Glide.with(getActivity())
                        .load(currentItem.getArticle_photo())
                        .bitmapTransform(new BlurTransformation(getActivity(), 50))
                        .error(null)
                        .into(VHitem.article_background_img);

                VHitem.article_text.setText(currentItem.getArticle_text());

                /**
                 * 서버에서 받아온 생성날짜 string을 Date타입으로 변환
                 */
                Date to = null;
                try{
                    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    to = transFormat.parse(currentItem.getCreated_at());
                }catch (ParseException p){
                    p.printStackTrace();
                }

                VHitem.article_etc_text.setText(String.format(res.getString(R.string.tab5_my_public_article_etc_txt),
                        commonUtil.formatTimeString(to), currentItem.getLike_cnt(), currentItem.getComment_cnt()));
            }
        }

        public class MyArticle_VHitem extends RecyclerView.ViewHolder{

            private ImageView article_background_img;
            private TextView article_text;
            private TextView article_etc_text;

            public MyArticle_VHitem(View itemView){
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
            return TYPE_ITEM_USER_ARTICLE;
        }
        //increasing getItemcount to 1. This will be the row of header.
        @Override
        public int getItemCount() {
            return listItems.size();
        }
    }
}

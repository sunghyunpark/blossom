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
import com.squareup.otto.Subscribe;
import com.yssh1020.blossom.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import api.ApiClient;
import api.ApiInterface;
import common.CommonUtil;
import dialog.BookMark_More_Dialog;
import dialog.Public_Me_Article_More_Dialog;
import event.BusProvider;
import event.MyBookMarkCancelEvent;
import jp.wasabeef.glide.transformations.BlurTransformation;
import model.Article;
import model.ArticleResponse;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tab5.ArticleActivity;

/**
 * created by sunghyun 2017-03-27
 */
public class Page3 extends Fragment {
    //리사이클러뷰
    RecyclerAdapter adapter;
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Article> listItems;
    View v;

    ViewGroup my_bookmark_empty_layout;
    CommonUtil commonUtil = new CommonUtil();

    private String bookmark_cancel_article_id;
    private int list_pos;

    public Page3() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BusProvider.getInstance().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.tab5_viewpager_page3, container, false);

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
        my_bookmark_empty_layout = (ViewGroup)v.findViewById(R.id.my_bookmark_empty_layout);

        LoadBookMarkData(User.getInstance().getUid());
    }

    private void LoadBookMarkData(String uid){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ArticleResponse> call = apiService.GetBookMarkData("get_bookmark_data", uid, "0");
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
                    my_bookmark_empty_layout.setVisibility(View.VISIBLE);
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

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_ITEM_BOOKMARK = 0;
        private Resources res = getResources();

        List<Article> listItems;

        public RecyclerAdapter(List<Article> listItems) {
            this.listItems = listItems;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM_BOOKMARK) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_tab5_bookmark, parent, false);
                return new MyBookMark_VHitem(v);
            }
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }

        private Article getItem(int position) {
            return listItems.get(position);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            if (holder instanceof MyBookMark_VHitem) {
                final Article currentItem = getItem(position);
                final MyBookMark_VHitem VHitem = (MyBookMark_VHitem)holder;

                VHitem.my_article_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), ArticleActivity.class);
                        intent.putExtra("article_id", currentItem.getArticle_id());
                        startActivity(intent);
                    }
                });
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

                VHitem.more_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), BookMark_More_Dialog.class);
                        intent.putExtra("article_id", currentItem.getArticle_id());
                        intent.putExtra("pos", position);
                        startActivity(intent);

                    }
                });
            }
        }

        public class MyBookMark_VHitem extends RecyclerView.ViewHolder{

            private ViewGroup my_article_layout;
            private ImageView article_background_img;
            private TextView article_text;
            private TextView article_etc_text;
            private ImageView more_btn;

            public MyBookMark_VHitem(View itemView){
                super(itemView);
                my_article_layout = (ViewGroup)itemView.findViewById(R.id.my_article_layout);
                article_background_img = (ImageView)itemView.findViewById(R.id.article_background_img);
                article_text = (TextView)itemView.findViewById(R.id.article_text);
                article_etc_text = (TextView)itemView.findViewById(R.id.article_etc_txt);
                more_btn = (ImageView)itemView.findViewById(R.id.more_btn);

            }

        }

        private void removeItem(int position, String article_id){
            listItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, listItems.size());
            commonUtil.BookMarkArticle(getActivity(), User.getInstance().getUid(), article_id, "Y");
            if(listItems.size() == 0){
                my_bookmark_empty_layout.setVisibility(View.VISIBLE);
            }

        }
        @Override
        public int getItemViewType(int position) {
            return TYPE_ITEM_BOOKMARK;
        }
        //increasing getItemcount to 1. This will be the row of header.
        @Override
        public int getItemCount() {
            return listItems.size();
        }
    }

    @Override
    public void onDestroy() {
        // Always unregister when an object no longer should be on the bus.
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }
    @Subscribe
    public void FinishLoad(MyBookMarkCancelEvent mPushEvent) {
        bookmark_cancel_article_id = mPushEvent.getArticle_id();
        list_pos = mPushEvent.getList_pos();

        adapter.removeItem(list_pos, bookmark_cancel_article_id);
    }
}

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
import android.widget.TextView;
import android.widget.Toast;

import com.yssh1020.blossom.R;
import java.util.ArrayList;
import java.util.List;

import api.ApiClient;
import api.ApiInterface;
import model.ArticleComment;
import model.ArticleCommentResponse;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tab5.ArticleActivity;

/**
 * created by sunghyun 2017-05-9
 */
public class Page2 extends Fragment {
    //리사이클러뷰
    RecyclerAdapter adapter;
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<ArticleComment> listItems;
    View v;
    ViewGroup my_comment_empty_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.tab5_viewpager_page2, container, false);

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
        listItems = new ArrayList<ArticleComment>();
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        adapter = new RecyclerAdapter(listItems);
        recyclerView.setLayoutManager(linearLayoutManager);

        my_comment_empty_layout = (ViewGroup) v.findViewById(R.id.my_comment_empty_layout);

        LoadMyCommentData(User.getInstance().getUid());
    }

    private void LoadMyCommentData(String uid){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ArticleCommentResponse> call = apiService.GetMyCommentData("my_comment", uid, "0");
        call.enqueue(new Callback<ArticleCommentResponse>() {
            @Override
            public void onResponse(Call<ArticleCommentResponse> call, Response<ArticleCommentResponse> response) {

                ArticleCommentResponse articleCommentResponse = response.body();
                if(!articleCommentResponse.isError()){
                    int dataSize = articleCommentResponse.getArticle_comment().size();
                    ArticleComment articleComment;
                    for(int i=0;i<dataSize;i++){
                        articleComment = new ArticleComment();
                        articleComment.setArticle_id(articleCommentResponse.getArticle_comment().get(i).getArticle_id());
                        articleComment.setComment_text(articleCommentResponse.getArticle_comment().get(i).getComment_text());
                        articleComment.setComment_id(articleCommentResponse.getArticle_comment().get(i).getComment_id());
                        articleComment.setArticle_text(articleCommentResponse.getArticle_comment().get(i).getArticle_text());
                        articleComment.setLike_cnt(articleCommentResponse.getArticle_comment().get(i).getLike_cnt());
                        articleComment.setCreated_at(articleCommentResponse.getArticle_comment().get(i).getCreated_at());
                        listItems.add(articleComment);
                    }
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }else{
                    recyclerView.setNestedScrollingEnabled(false);
                    my_comment_empty_layout.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity().getApplicationContext(), articleCommentResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArticleCommentResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
            }
        });
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_ITEM_COMMENT = 0;
        private Resources res = getResources();

        List<ArticleComment> listItems;

        public RecyclerAdapter(List<ArticleComment> listItems) {
            this.listItems = listItems;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM_COMMENT) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_tab5_my_comment, parent, false);
                return new MyComment_VHitem(v);
            }
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }

        private ArticleComment getItem(int position) {
            return listItems.get(position);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            if (holder instanceof MyComment_VHitem) {
                final ArticleComment currentItem = getItem(position);
                final MyComment_VHitem VHitem = (MyComment_VHitem)holder;

                VHitem.comment_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ArticleActivity.class);
                        intent.putExtra("article_id", currentItem.getArticle_id());
                        startActivity(intent);
                    }
                });

                VHitem.article_text.setText(currentItem.getArticle_text());

                VHitem.comment_text.setText(currentItem.getComment_text());

                VHitem.comment_etc_text.setText(currentItem.getCreated_at());

            }
        }

        public class MyComment_VHitem extends RecyclerView.ViewHolder{

            private ViewGroup comment_layout;
            private TextView article_text;
            private TextView comment_text;
            private TextView comment_etc_text;

            public MyComment_VHitem(View itemView){
                super(itemView);

                comment_layout = (ViewGroup)itemView.findViewById(R.id.my_comment_layout);
                article_text = (TextView)itemView.findViewById(R.id.article_txt);
                comment_text = (TextView)itemView.findViewById(R.id.my_comment_txt);
                comment_etc_text = (TextView)itemView.findViewById(R.id.my_comment_etc_txt);

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
            return TYPE_ITEM_COMMENT;
        }
        //increasing getItemcount to 1. This will be the row of header.
        @Override
        public int getItemCount() {
            return listItems.size();
        }
    }

}

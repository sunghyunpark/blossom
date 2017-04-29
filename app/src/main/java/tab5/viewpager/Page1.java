package tab5.viewpager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yssh1020.blossom.LoginPage;
import com.yssh1020.blossom.R;

import java.util.ArrayList;
import java.util.List;

import model.Article;
import tab5.FragmentPage5;

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

        Article article;

        for(int i=0;i<20;i++){
            article = new Article();
            article.setArticle_text(i+"");
            listItems.add(article);
        }
        recyclerView.setAdapter(adapter);
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_ITEM_USER_ATTICLE = 0;
        List<Article> listItems;

        public RecyclerAdapter(List<Article> listItems) {
            this.listItems = listItems;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM_USER_ATTICLE) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_tab5_my_article, parent, false);
                return new RecyclerAdapter.MyArticle_VHitem(v);
            }
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }

        private Article getItem(int position) {
            return listItems.get(position);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            if (holder instanceof RecyclerAdapter.MyArticle_VHitem) {
                final Article currentItem = getItem(position);
                final RecyclerAdapter.MyArticle_VHitem VHitem = (RecyclerAdapter.MyArticle_VHitem)holder;


            }
        }

        public class MyArticle_VHitem extends RecyclerView.ViewHolder{

            private TextView article_text;

            public MyArticle_VHitem(View itemView){
                super(itemView);
                article_text = (TextView)itemView.findViewById(R.id.article_text);

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
            return TYPE_ITEM_USER_ATTICLE;
        }
        //increasing getItemcount to 1. This will be the row of header.
        @Override
        public int getItemCount() {
            return listItems.size();
        }
    }
}

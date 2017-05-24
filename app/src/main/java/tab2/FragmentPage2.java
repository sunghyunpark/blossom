package tab2;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.yssh1020.blossom.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import api.ApiClient;
import api.ApiInterface;
import dialog.BookMark_More_Dialog;
import jp.wasabeef.glide.transformations.BlurTransformation;
import model.Article;
import model.ArticleResponse;
import model.FamousArticle;
import model.FamousArticleResponse;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tab5.ArticleActivity;

/**
 * created by sunghyun 2017-05-16
 */
public class FragmentPage2 extends Fragment {

    //리사이클러뷰
    RecyclerAdapter adapter;
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<FamousArticle> listItems;
    private ViewGroup retry_network_layout;
    private String LastArticleID = "";
    View v;
    private static final int LOAD_DATA_COUNT = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragmentpage2, container, false);

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
        listItems = new ArrayList<FamousArticle>();
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        adapter = new RecyclerAdapter(listItems);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        retry_network_layout = (ViewGroup)v.findViewById(R.id.retry_network_layout);

        Button retry_network_btn = (Button)v.findViewById(R.id.retry_network_btn);
        retry_network_btn.setOnTouchListener(myOnTouchListener);

        GetFamousArticleData("0");

        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                // do something...
                //Toast.makeText(getActivity(),"불러오는중...", Toast.LENGTH_SHORT).show();
                GetFamousArticleData(LastArticleID);

            }
        });
    }

    private void GetFamousArticleData(String last_id){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<FamousArticleResponse> call = apiService.GetFamousArticleData("famous_article", last_id);
        call.enqueue(new Callback<FamousArticleResponse>() {
            @Override
            public void onResponse(Call<FamousArticleResponse> call, Response<FamousArticleResponse> response) {

                FamousArticleResponse famousArticleResponse = response.body();
                if(!famousArticleResponse.isError()){
                    retry_network_layout.setVisibility(View.GONE);
                    int dataSize = famousArticleResponse.getFamous_article().size();
                    FamousArticle article;
                    for(int i=0;i<dataSize;i++){
                        article = new FamousArticle();
                        article.setFamous_article_id(famousArticleResponse.getFamous_article().get(i).getFamous_article_id());
                        article.setFamous_article_text(famousArticleResponse.getFamous_article().get(i).getFamous_article_text());
                        article.setFamous_article_photo(famousArticleResponse.getFamous_article().get(i).getFamous_article_photo());
                        article.setCreated_at(famousArticleResponse.getFamous_article().get(i).getCreated_at());

                        listItems.add(article);
                    }
                    LastArticleID = famousArticleResponse.getLast_article_id();
                    adapter.notifyDataSetChanged();


                }else{
                    //Toast.makeText(getActivity().getApplicationContext(), famousArticleResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FamousArticleResponse> call, Throwable t) {
                // Log error here since request failed
                Resources res = getResources();
                retry_network_layout.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), String.format(res.getString(R.string.toast_network_error)), Toast.LENGTH_SHORT).show();
                Log.e("tag", t.toString());
            }
        });
    }

    private abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {


        private int previousTotal = 0; // The total number of items in the dataset after the last load
        private boolean loading = true; // True if we are still waiting for the last set of data to load.
        private int visibleThreshold = LOAD_DATA_COUNT; // The minimum amount of items to have below your current scroll position before loading more.
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

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_ITEM_BOOKMARK = 0;
        private Resources res = getResources();

        List<FamousArticle> listItems;

        public RecyclerAdapter(List<FamousArticle> listItems) {
            this.listItems = listItems;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM_BOOKMARK) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_tab2_famous_article, parent, false);
                return new Famous_Article_VHitem(v);
            }
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }

        private FamousArticle getItem(int position) {
            return listItems.get(position);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            if (holder instanceof Famous_Article_VHitem) {
                final FamousArticle currentItem = getItem(position);
                final Famous_Article_VHitem VHitem = (Famous_Article_VHitem)holder;


                Glide.with(getActivity())
                        .load(currentItem.getFamous_article_photo())
                        .bitmapTransform(new BlurTransformation(getActivity(), 50))
                        .error(null)
                        .into(VHitem.article_background_img);

                VHitem.article_text.setText(currentItem.getFamous_article_text());
            }
        }

        public class Famous_Article_VHitem extends RecyclerView.ViewHolder{

            private ViewGroup famous_article_layout;
            private ImageView article_background_img;
            private TextView article_text;

            public Famous_Article_VHitem(View itemView){
                super(itemView);
                famous_article_layout = (ViewGroup)itemView.findViewById(R.id.famous_article_layout);
                article_background_img = (ImageView)itemView.findViewById(R.id.article_background_img);
                article_text = (TextView)itemView.findViewById(R.id.article_text);

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
                    case R.id.retry_network_btn:
                        InitView();
                        break;

                }
            }
            return true;
        }
    };
}

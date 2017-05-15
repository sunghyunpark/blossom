package tab3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.yssh1020.blossom.AppController;
import com.yssh1020.blossom.R;

import java.util.ArrayList;

import event.BusProvider;
import event.SelectArticleBGEvent;
import model.ArticleBg;


public class SelectBG_Activity extends Activity {

    private ArrayList<ArticleBg> listItems;
    //리사이클러뷰
    RecyclerAdapter adapter;
    RecyclerView recyclerView;

    private int select_pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectbg_activity);

        Intent intent = getIntent();
        select_pos = intent.getExtras().getInt("select_pos");

        InitView();

    }

    private void InitView(){
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        GridLayoutManager lLayout = new GridLayoutManager(getApplicationContext(),4);
        recyclerView.setLayoutManager(lLayout);

        listItems = new ArrayList<ArticleBg>();
        adapter = new RecyclerAdapter(listItems);
        recyclerView.setAdapter(adapter);

        SetList();
    }

    private void SetList(){
        ArticleBg item;
        for(int i=1;i<=91;i++){
            item = new ArticleBg();
            item.setArticle_background_url("article_bg_"+i+".jpg");
            listItems.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_ITEM = 1;
        ArrayList<ArticleBg> listItems;

        public RecyclerAdapter(ArrayList<ArticleBg> listItems) {
            this.listItems = listItems;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_article_bg, parent, false);
                return new ArticleBG_VHitem(v);
            }
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }

        private ArticleBg getItem(int position) {
            return listItems.get(position);
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            if (holder instanceof ArticleBG_VHitem)//아이템(게시물)
            {
                final ArticleBg currentItem = getItem(position);
                final ArticleBG_VHitem VHitem = (ArticleBG_VHitem)holder;

                VHitem.article_bg_img_layout.setLayoutParams(Set_HalfSize_Display(getApplicationContext()));
                VHitem.article_bg_img_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BusProvider.getInstance().post(new SelectArticleBGEvent(currentItem.getArticle_background_url(), position));
                        finish();
                    }
                });

                Glide.clear(VHitem.article_bg_img);
                Glide.with(getApplicationContext())
                        .load(AppController.getInstance().getServer_img_path()+"article_bg/"+currentItem.getArticle_background_url())
                        //.override(display_width/3, display_width/3)
                        .error(null)
                        .into(VHitem.article_bg_img);



            }
        }
        class ArticleBG_VHitem extends RecyclerView.ViewHolder{
            ViewGroup article_bg_img_layout;
            ImageView article_bg_img;

            public ArticleBG_VHitem(View itemView){
                super(itemView);
                article_bg_img = (ImageView) itemView.findViewById(R.id.article_bg_img);
                article_bg_img_layout = (ViewGroup)itemView.findViewById(R.id.article_bg_img_layout);
            }
        }
        @Override
        public int getItemViewType(int position) {
            return TYPE_ITEM;
        }
        //increasing getItemcount to 1. This will be the row of header.
        @Override
        public int getItemCount() {
            return listItems.size();
        }
    }

    /**
     * 그리드 형태의 썸네일 크기
     * @param context
     * @return
     */
    private FrameLayout.LayoutParams Set_HalfSize_Display(Context context){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(AppController.getInstance().getDISPLAY_WIDTH()/4,
                AppController.getInstance().getDISPLAY_WIDTH()/4);
        return params;
    }

}
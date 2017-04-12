package tab3;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.yssh1020.blossom.AppController;
import com.yssh1020.blossom.R;

import java.util.ArrayList;

import model.ArticleBg;


public class SelectBG_Activity extends Activity {

    private ArrayList<ArticleBg> listItems;
    //리사이클러뷰
    RecyclerAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashpage);


    }

    private void InitView(){
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        GridLayoutManager lLayout = new GridLayoutManager(getApplicationContext(),4);
        recyclerView.setLayoutManager(lLayout);

        listItems = new ArrayList<ArticleBg>();
        adapter = new RecyclerAdapter(listItems);
        recyclerView.setAdapter(adapter);
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_ITEM = 1;
        private int display_width = AppController.getInstance().getDISPLAY_WIDTH();

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



            }
        }
        class ArticleBG_VHitem extends RecyclerView.ViewHolder{
            ViewGroup article_bg_img_layout;
            ImageView article_bg_img;

            public ArticleBG_VHitem(View itmeView){
                super(itmeView);
                article_bg_img = (ImageView) itemView.findViewById(R.id.article_bg_img);
                article_bg_img_layout = (ViewGroup)itmeView.findViewById(R.id.article_bg_img_layout);
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
     * 프리뷰 이미지 밑에 그리드 형식으로 되어있는 썸네일들의 크기
     * @param context
     * @return
     */
    private FrameLayout.LayoutParams Set_HalfSize_Display(Context context){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(AppController.getInstance().getDISPLAY_WIDTH()/3,
                AppController.getInstance().getDISPLAY_WIDTH()/3);
        return params;
    }

}
package tab4;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yssh1020.blossom.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dialog.Public_Me_Article_More_Dialog;
import jp.wasabeef.glide.transformations.BlurTransformation;
import model.Alarm;
import model.Article;
import tab5.ArticleActivity;
import tab5.viewpager.Page1;

/**
 * created by sunghyun 2017-03-27
 */
public class FragmentPage4 extends Fragment {

    //리사이클러뷰
    RecyclerAdapter adapter;
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Alarm> listItems;
    View v;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragmentpage4, container, false);

        Bundle bundle = getArguments();
        if(bundle != null){
            String msg = bundle.getString("KEY_MSG");
            if(msg != null){

            }
        }


        return v;
    }

    private void InitView(){
        listItems = new ArrayList<Alarm>();
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        adapter = new RecyclerAdapter(listItems);
        recyclerView.setLayoutManager(linearLayoutManager);
    }


    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_ITEM_ALARM = 0;
        private Resources res = getResources();

        List<Alarm> listItems;

        public RecyclerAdapter(List<Alarm> listItems) {
            this.listItems = listItems;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM_ALARM) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_tab4_alram, parent, false);
                return new Alarm_VHitem(v);
            }
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }

        private Alarm getItem(int position) {
            return listItems.get(position);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            if (holder instanceof Alarm_VHitem) {
                final Alarm currentItem = getItem(position);
                final Alarm_VHitem VHitem = (Alarm_VHitem)holder;


            }
        }

        public class Alarm_VHitem extends RecyclerView.ViewHolder{

            private ViewGroup alarm_layout;
            private ImageView alarm_img;
            private TextView alarm_txt;
            private TextView created_at_txt;

            public Alarm_VHitem(View itemView){
                super(itemView);

                alarm_layout = (ViewGroup)itemView.findViewById(R.id.alarm_layout);
                alarm_img = (ImageView)itemView.findViewById(R.id.alarm_img);
                alarm_txt = (TextView)itemView.findViewById(R.id.alarm_txt);
                created_at_txt = (TextView)itemView.findViewById(R.id.created_at_txt);

            }

        }

        private void removeItem(int position, String article_id){
            listItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, listItems.size());
            //commonUtil.DeleteArticle(getActivity(), article_id);
            if(listItems.size() == 0){
                //my_story_empty_layout.setVisibility(View.VISIBLE);
            }

        }
        @Override
        public int getItemViewType(int position) {
            return TYPE_ITEM_ALARM;
        }
        //increasing getItemcount to 1. This will be the row of header.
        @Override
        public int getItemCount() {
            return listItems.size();
        }
    }
}

package tab4;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.yssh1020.blossom.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import api.ApiClient;
import api.ApiInterface;
import common.CommonUtil;
import dialog.Public_Me_Article_More_Dialog;
import jp.wasabeef.glide.transformations.BlurTransformation;
import model.Alarm;
import model.AlarmResponse;
import model.Article;
import model.ArticleResponse;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tab5.ArticleActivity;
import tab5.viewpager.Page1;

/**
 * created by sunghyun 2017-03-27
 */
public class FragmentPage4 extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    //리사이클러뷰
    RecyclerAdapter adapter;
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Alarm> listItems;
    View v;

    CommonUtil commonUtil = new CommonUtil();
    ViewGroup tab4_empty_layout;
    //리프레쉬
    private SwipeRefreshLayout mSwipeRefresh;


    //리프레쉬
    @Override
    public void onRefresh() {
        InitView();
        mSwipeRefresh.setRefreshing(false);
    }


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

        InitView();
        return v;
    }

    private void InitView(){
        listItems = new ArrayList<Alarm>();
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        adapter = new RecyclerAdapter(listItems);
        recyclerView.setLayoutManager(linearLayoutManager);

        //리프레쉬
        mSwipeRefresh = (SwipeRefreshLayout)v.findViewById(R.id.swipe_layout);
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorSky), getResources().getColor(R.color.colorSky),
                getResources().getColor(R.color.colorSky), getResources().getColor(R.color.colorSky));

        tab4_empty_layout = (ViewGroup)v.findViewById(R.id.tab4_empty_layout);

        GetAlarmData(User.getInstance().getUid());
    }

    private void GetAlarmData(String uid){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<AlarmResponse> call = apiService.GetAlarmData("get_alarm_data", uid);
        call.enqueue(new Callback<AlarmResponse>() {
            @Override
            public void onResponse(Call<AlarmResponse> call, Response<AlarmResponse> response) {

                AlarmResponse alarmResponse = response.body();
                if(!alarmResponse.isError()){
                    int dataSize = alarmResponse.getAlarm().size();
                    Alarm alarm;
                    for(int i=0;i<dataSize;i++){
                        alarm = new Alarm();
                        alarm.setArticle_id(alarmResponse.getAlarm().get(i).getArticle_id());
                        alarm.setCategory(alarmResponse.getAlarm().get(i).getCategory());
                        alarm.setInfo(alarmResponse.getAlarm().get(i).getInfo());
                        alarm.setCreated_at(alarmResponse.getAlarm().get(i).getCreated_at());
                        listItems.add(alarm);
                    }
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }else{
                    recyclerView.setNestedScrollingEnabled(false);
                    tab4_empty_layout.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity().getApplicationContext(), alarmResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AlarmResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
            }
        });
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

                VHitem.alarm_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ArticleActivity.class);
                        intent.putExtra("article_id", currentItem.getArticle_id());
                        startActivity(intent);
                    }
                });


                Glide.clear(VHitem.alarm_img);
                Glide.with(getActivity())
                        .load(res.getIdentifier(SetAlarmImg(position), "mipmap", "com.yssh1020.blossom"))
                        .error(null)
                        .into(VHitem.alarm_img);

                VHitem.alarm_txt.setText(SetAlarmText(position));

                Date to = null;
                try{
                    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    to = transFormat.parse(getItem(position).getCreated_at());
                }catch (ParseException p){
                    p.printStackTrace();
                }
                VHitem.created_at_txt.setText(commonUtil.formatTimeString(to));

            }
        }

        //말줄임 처리
        private String ellipsis(String text, int length){
            String ellipsisString = "...";
            String outputString = text;
            outputString = outputString.replace("\n"," ");    //줄바꿈이 있을 경우 띄어쓰기로 변경

            if(text.length()>0 && length>0){
                if(text.length() > length){
                    outputString = text.substring(0, length);
                    outputString += ellipsisString;
                }
            }
            return outputString;
        }

        private String SetAlarmImg(int position){
            String type = getItem(position).getCategory();
            String ImgName = "";
            if(type.equals("like")){
                ImgName = "alarm_like_img";
            }else if(type.equals("comment")){
                ImgName = "alarm_comment_img";
            }else if(type.equals("bookmark")){
                ImgName = "alarm_bookmark_img";
            }
            return ImgName;
        }

        private String SetAlarmText(int position){
            String type = getItem(position).getCategory();
            String alarmStr = "";
            if(type.equals("like")){
                alarmStr = String.format(res.getString(R.string.tab4_alarm_type_like_txt),
                        "\""+ellipsis(getItem(position).getInfo(),20)+"\"");
            }else if(type.equals("comment")){
                alarmStr = String.format(res.getString(R.string.tab4_alarm_type_comment_txt),
                        "\""+ellipsis(getItem(position).getInfo(),20)+"\"");
            }else if(type.equals("bookmark")){
                alarmStr = String.format(res.getString(R.string.tab4_alarm_type_bookmark_txt),
                        "\""+ellipsis(getItem(position).getInfo(),20)+"\"");
            }
            return alarmStr;
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

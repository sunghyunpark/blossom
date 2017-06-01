package common;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.yssh1020.blossom.AppController;
import com.yssh1020.blossom.AppSettingManager;

import java.util.Date;

import api.ApiClient;
import api.ApiInterface;
import db.RealmUtil;
import model.CommonResponse;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sunghyun park on 2017. 4. 17..
 */

public class CommonUtil {

    /**
     * edit background title
     * @param context
     * @param uid
     * @param title
     */
    public void EditBackgroundTitle(final Context context, final String uid, final String title){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommonResponse> call = apiService.EditBackgroundTitle("edit_background_title", uid, title);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(!commonResponse.isError()){
                    Toast.makeText(context, commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                    User.getInstance().setBg_title(title);
                    RealmUtil realmUtil = new RealmUtil();
                    realmUtil.UpdateBackgroundTitle(context, uid, title);
                }else{
                    Toast.makeText(context, commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Toast.makeText(context.getApplicationContext(), "네트워크 연결상태를 확인해주세요.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * push state 변졍
     * @param context
     * @param tag
     * @param uid
     * @param push_state
     */
    public void PostPushState(final Context context, final String tag, String uid, final String push_state){

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommonResponse> call = apiService.PostPushState(tag, uid, push_state);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(!commonResponse.isError()){
                    //Toast.makeText(context, commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                    boolean state;
                    AppSettingManager appSettingManager = new AppSettingManager(context);
                    if(push_state.equals("Y")){
                        state = true;
                    }else{
                        state = false;
                    }

                    if(tag.equals("app_push")){
                        appSettingManager.setAppAlarm_State(state);
                    }else if(tag.equals("comment_push")){
                        appSettingManager.setCommentAlarm_State(state);
                    }else if(tag.equals("like_push")){
                        appSettingManager.setArticleLikeAlarm_State(state);
                    }
                }else{
                    //Toast.makeText(context, commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Toast.makeText(context.getApplicationContext(), "네트워크 연결상태를 확인해주세요.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * send article report
     * @param context
     * @param article_id
     * @param uid
     * @param report_text
     */
    public void SendArticleReport(final Context context, String article_id, String uid, String report_text){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommonResponse> call = apiService.PostArticleReport("send_report", article_id, uid, report_text);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(!commonResponse.isError()){
                    Toast.makeText(context, commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Toast.makeText(context.getApplicationContext(), "네트워크 연결상태를 확인해주세요.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 내 아티클 공개 설정 변경
     * @param context
     * @param article_id
     * @param private_mode
     */
    public void MyArticlePrivateModeChange(final Context context, String article_id, String private_mode){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommonResponse> call = apiService.ChangePrivateMode("change_private_mode", article_id, private_mode);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(!commonResponse.isError()){
                    Toast.makeText(context, "공개설정을 변경했습니다.",Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(context, commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Toast.makeText(context.getApplicationContext(), "네트워크 연결상태를 확인해주세요.",Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * comment like
     * @param context
     * @param comment_id
     * @param uid_other
     * @param uid_me
     */
    public void CommentLike(final Context context, String comment_id, String uid_other, final String uid_me){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommonResponse> call = apiService.CommentLike("comment_like", comment_id, uid_other, uid_me);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(!commonResponse.isError()){
                    //Toast.makeText(context, commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(context, commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Toast.makeText(context.getApplicationContext(), "네트워크 연결상태를 확인해주세요.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 아티클 작성자에게 씨앗을 보냄
     * @param context
     * @param article_user_id
     * @param uid_me
     * @param seed_cnt
     */
    public void PostSeedToArticleUser(final Context context, String article_user_id, String uid_me, int seed_cnt){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommonResponse> call = apiService.PostSeedToArticleUser("post_seed_to_article_user", article_user_id, uid_me, seed_cnt);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(!commonResponse.isError()){
                    Toast.makeText(context, "씨앗을 선물했습니다.", Toast.LENGTH_SHORT).show();
                }else{

                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Toast.makeText(context.getApplicationContext(), "네트워크 연결상태를 확인해주세요.",Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * Push Token 등록
     * @param context
     * @param uid
     * @param token
     * @param login_state
     */
    public void RegisterPushToken(final Context context, String uid, String token, String login_state){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommonResponse> call = apiService.RegisterPushToken("token_register", uid, token, login_state);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(!commonResponse.isError()){

                }else{

                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Toast.makeText(context.getApplicationContext(), "네트워크 연결상태를 확인해주세요.",Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 아티클 삭제
     * @param context
     * @param article_id
     */
    public void DeleteArticle(final Context context, String article_id){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommonResponse> call = apiService.DeleteArticle("delete_article", article_id);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                CommonResponse commonResponse = response.body();
                if(!commonResponse.isError()){
                    Toast.makeText(context.getApplicationContext(), "이야기를 삭제했습니다.",Toast.LENGTH_SHORT).show();

                }else{
                    //Toast.makeText(context.getApplicationContext(), commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Toast.makeText(context.getApplicationContext(), "네트워크 연결상태를 확인해주세요.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 아티클 북마크
     * @param context
     * @param uid
     * @param article_id
     * @param bookmark_state
     */
    public void BookMarkArticle(final Context context, String uid, String article_id, String bookmark_state, final String toast_msg){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommonResponse> call = apiService.PostBookMark("bookmark_article", uid, article_id, bookmark_state);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                CommonResponse commonResponse = response.body();
                if(!commonResponse.isError()){
                    Toast.makeText(context.getApplicationContext(), toast_msg,Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(context.getApplicationContext(), toast_msg,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Toast.makeText(context.getApplicationContext(), "네트워크 연결상태를 확인해주세요.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * article 좋아요
     */
    public void LikeArticle(final Context context, String uid, String article_id, String like_state){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommonResponse> call = apiService.PostArticleLike("like", uid, article_id, like_state);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                CommonResponse commonResponse = response.body();
                if(!commonResponse.isError()){
                    //Toast.makeText(context.getApplicationContext(), commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();

                }else{
                    //Toast.makeText(context.getApplicationContext(), commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Toast.makeText(context.getApplicationContext(), "네트워크 연결상태를 확인해주세요.",Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 아티클 댓글 전송
     * @param context
     * @param uid
     * @param article_id
     * @param comment_text
     */
    public void InsertArticleComment(final Context context, String uid, String article_id, String comment_text){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommonResponse> call = apiService.PostArticleComment("insert_comment", uid, article_id, comment_text);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                CommonResponse commonResponse = response.body();
                if(!commonResponse.isError()){
                    //Toast.makeText(context.getApplicationContext(), commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();

                }else{
                    //Toast.makeText(context.getApplicationContext(), commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Toast.makeText(context.getApplicationContext(), "네트워크 연결상태를 확인해주세요.",Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * upload article bg 이미지 랜덤
     * @return
     */
    public String GetArticleBGName(){
        int randomNum = (int)(Math.random() * AppController.getInstance().getArticle_Bg_Cnt())+1;
        String ImgName = AppController.getInstance().getServer_img_path()+"/article_bg/article_bg_"+randomNum+".jpg";
        return ImgName;
    }

    /**
     * user profile 이미지 랜덤
     * @return
     */
    public String GetUserProfileName(){
        int randomNum = (int)(Math.random() * 7)+1;
        String ImgName = "user_profile_img"+randomNum;
        return ImgName;
    }

    public String formatTimeAMPM(String date){
        String format_str;
        int hour;
        int min;
        String min_str;
        String ampm;

        hour = Integer.parseInt(date.substring(11,13));
        min = Integer.parseInt(date.substring(14,16));
        min_str = min+"";
        if(hour >= 12){
            ampm = "PM";
            if(hour > 12){
                hour -= 12;
            }
        }else{
            ampm = "AM";
        }
        if(min < 10){
            min_str = "0"+min;
        }
        format_str = hour+":"+min_str+" "+ampm;



        return format_str;
    }
    /**
     * Date타입의 시간을 변환해줌
     */
    private static class TIME_MAXIMUM{
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }
    public static String formatTimeString(Date tempDate) {

        long curTime = System.currentTimeMillis();
        long regTime = tempDate.getTime();
        long diffTime = (curTime - regTime) / 1000;

        String msg = null;
        if (diffTime < TIME_MAXIMUM.SEC) {
            // sec
            msg = "방금 전";
        } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
            // min
            msg = diffTime + "분 전";
        } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
            // hour
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
            // day
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
            // day
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }

        return msg;
    }
}

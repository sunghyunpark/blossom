package common;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

import api.ApiClient;
import api.ApiInterface;
import model.CommonResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sunghyun park on 2017. 4. 17..
 */

public class CommonUtil {


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
                    Toast.makeText(context.getApplicationContext(), commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(context.getApplicationContext(), commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
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
                    Toast.makeText(context.getApplicationContext(), commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(context.getApplicationContext(), commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
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
    public void BookMarkArticle(final Context context, String uid, String article_id, String bookmark_state){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommonResponse> call = apiService.PostBookMark("bookmark_article", uid, article_id, bookmark_state);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                CommonResponse commonResponse = response.body();
                if(!commonResponse.isError()){
                    Toast.makeText(context.getApplicationContext(), commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(context.getApplicationContext(), commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
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
                    Toast.makeText(context.getApplicationContext(), commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(context.getApplicationContext(), commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
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
                    Toast.makeText(context.getApplicationContext(), commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(context.getApplicationContext(), commonResponse.getError_msg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
            }
        });

    }

    /**
     * user profile 이미지 랜덤
     * @return
     */
    public String GetUserProfileName(){
        int randomNum = (int)(Math.random() * 10)+1;
        String ImgName = "user_profile_img"+randomNum;
        return ImgName;
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

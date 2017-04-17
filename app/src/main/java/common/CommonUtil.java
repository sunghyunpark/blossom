package common;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
     * article 좋아요
     */
    public void LikeArticle(final Context context, String uid, String article_id){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CommonResponse> call = apiService.PostArticleLike("like", uid, article_id);
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
}

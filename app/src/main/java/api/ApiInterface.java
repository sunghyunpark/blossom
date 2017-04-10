package api;

import model.CommonResponse;
import model.UserResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by sunghyun on 2017. 3. 30..
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login/login.php")
    Call<UserResponse> post_user(@Field("tag") String tag, @Field("email") String email,
                                 @Field("birth") String birth, @Field("gender") String gender);

    @FormUrlEncoded
    @POST("upload/upload.php")
    Call<CommonResponse> post_article(@Field("tag") String tag, @Field("uid") String uid,
                                      @Field("article_text") String article_text, @Field("article_photo") String article_photo);
}

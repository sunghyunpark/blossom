package api;

import model.Article;
import model.ArticleResponse;
import model.CommonResponse;
import model.UserResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sunghyun on 2017. 3. 30..
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login/login.php")
    Call<UserResponse> PostUser(@Field("tag") String tag, @Field("email") String email,
                                 @Field("birth") String birth, @Field("gender") String gender);

    @FormUrlEncoded
    @POST("upload/upload.php")
    Call<CommonResponse> PostArticle(@Field("tag") String tag, @Field("uid") String uid,
                                      @Field("article_text") String article_text, @Field("article_photo") String article_photo);

    @GET("article/article.php")
    Call<ArticleResponse> GetArticle(@Query("tag") String tag);
}

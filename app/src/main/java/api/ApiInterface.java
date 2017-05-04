package api;

import model.Article;
import model.ArticleCommentResponse;
import model.ArticleDetailResponse;
import model.ArticleResponse;
import model.CommonResponse;
import model.MyPageResponse;
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

    /**
     * 로그인 api
     * @param tag -> register
     * @param email -> 최초 가입시엔 이메일이 없으므로 빈값
     * @param birth -> 생년 월일
     * @param gender -> 성별
     * @return
     */
    @FormUrlEncoded
    @POST("login/login.php")
    Call<UserResponse> PostUser(@Field("tag") String tag, @Field("email") String email,
                                @Field("birth") String birth, @Field("gender") String gender);

    /**
     * article upload
     * @param tag -> upload
     * @param uid -> 유저 uid
     * @param article_text -> 아티클 내용
     * @param article_photo -> 아티클 사진 path
     * @return
     */
    @FormUrlEncoded
    @POST("upload/upload.php")
    Call<CommonResponse> PostArticle(@Field("tag") String tag, @Field("uid") String uid,
                                     @Field("article_text") String article_text, @Field("article_photo") String article_photo);

    /**
     * article
     * @param tag -> article
     * @return
     */
    @GET("article/article.php")
    Call<ArticleResponse> GetArticle(@Query("tag") String tag, @Query("uid") String uid);

    /**
     * article like
     * @param tag -> like
     * @param uid
     * @param article_id
     * @param like_state
     * @return
     */
    @FormUrlEncoded
    @POST("article/like.php")
    Call<CommonResponse> PostArticleLike(@Field("tag") String tag, @Field("uid") String uid,
                                         @Field("article_id") String article_id, @Field("like_state") String like_state);

    /**
     * my article
     * @param tag -> my_article
     * @param uid
     * @return
     */
    @GET("article/article.php")
    Call<ArticleResponse> GetMyArticle(@Query("tag") String tag, @Query("uid") String uid);

    /**
     * tab5화면에서 상단 시드/스토리 수
     * @param tag
     * @param uid
     * @return
     */
    @GET("article/article.php")
    Call<MyPageResponse> GetMyPageInfo(@Query("tag") String tag, @Query("uid") String uid);

    /**
     * article comment insert
     * @param tag -> insert_comment
     * @param uid
     * @param article_id
     * @param comment_text
     * @return
     */
    @FormUrlEncoded
    @POST("article/comment.php")
    Call<CommonResponse> PostArticleComment(@Field("tag") String tag, @Field("uid") String uid,
                                            @Field("article_id") String article_id, @Field("comment_text") String comment_text);

    /**
     * article comment 불러오기
     * @param tag -> comment
     * @param article_id
     * @return
     */
    @GET("article/comment.php")
    Call<ArticleCommentResponse> GetArticleComment(@Query("tag") String tag, @Query("article_id") String article_id);

    /**
     * article detail
     * @param tag -> article_detail
     * @param uid
     * @param article_id
     * @return
     */
    @GET("article/article.php")
    Call<ArticleDetailResponse> GetArticleDetailData(@Query("tag") String tag, @Query("uid") String uid,
                                                     @Query("article_id") String article_id);
}

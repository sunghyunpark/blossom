package api;

import model.AlarmResponse;
import model.ArticleCommentResponse;
import model.ArticleDetailResponse;
import model.ArticleResponse;
import model.CommonResponse;
import model.FamousArticleResponse;
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
                                @Field("birth") String birth, @Field("gender") String gender,
                                @Field("profile_img") String profile_img);

    /**
     * load user data
     * @param tag -> load_user_data
     * @param uid
     * @return
     */
    @GET("login/login.php")
    Call<UserResponse> GetUserData(@Query("tag") String tag, @Query("uid") String uid);
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
    Call<ArticleResponse> GetArticle(@Query("tag") String tag, @Query("uid") String uid,
                                     @Query("last_article_id") String last_article_id);

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
    Call<ArticleCommentResponse> GetArticleComment(@Query("tag") String tag, @Query("article_id") String article_id,
                                                   @Query("last_comment_id") String last_comment_id, @Query("uid") String uid);

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

    /**
     * my comment
     * @param tag -> my_comment
     * @param uid
     * @param last_comment_id
     * @return
     */
    @GET("article/comment.php")
    Call<ArticleCommentResponse> GetMyCommentData(@Query("tag") String tag, @Query("uid") String uid,
                                                  @Query("last_comment_id") String last_comment_id);

    /**
     * insert bookmark
     * @param tag -> bookmark_article
     * @param uid
     * @param article_id
     * @return
     */
    @FormUrlEncoded
    @POST("article/bookmark.php")
    Call<CommonResponse> PostBookMark(@Field("tag") String tag, @Field("uid") String uid,
                                      @Field("article_id") String article_id, @Field("bookmark_state") String bookmark_state);

    /**
     * bookmark article
     * @param tag -> get_bookmark_data
     * @param uid
     * @param last_article_id
     * @return
     */
    @GET("article/bookmark.php")
    Call<ArticleResponse> GetBookMarkData(@Query("tag") String tag, @Query("uid") String uid,
                                          @Query("last_article_id") String last_article_id);

    /**
     * delete article
     * @param tag -> delete_article
     * @param article_id
     * @return
     */
    @FormUrlEncoded
    @POST("article/article.php")
    Call<CommonResponse> DeleteArticle(@Field("tag") String tag, @Field("article_id") String article_id);

    /**
     * register push token
     * @param tag -> token_register
     * @param uid
     * @param token
     * @param login_state
     * @return
     */
    @FormUrlEncoded
    @POST("push/push.php")
    Call<CommonResponse> RegisterPushToken(@Field("tag") String tag, @Field("uid") String uid,
                                           @Field("token") String token, @Field("login_state") String login_state);

    /**
     * alarm data
     * @param tag -> get_alarm_data
     * @param uid
     * @return
     */
    @GET("push/push.php")
    Call<AlarmResponse> GetAlarmData(@Query("tag") String tag, @Query("uid") String uid);

    /**
     * famous article
     * @param tag -> famous_article
     * @param last_famous_article_id
     * @return
     */
    @GET("article/article.php")
    Call<FamousArticleResponse> GetFamousArticleData(@Query("tag") String tag, @Query("last_famous_article_id") String last_famous_article_id);

    /**
     * post seed
     * @param tag -> insert_seed
     * @param uid
     * @param seed_cnt
     * @return
     */
    @FormUrlEncoded
    @POST("article/article.php")
    Call<CommonResponse> PostSeed(@Field("tag") String tag, @Field("uid") String uid,
                                  @Field("seed_cnt") int seed_cnt);

    /**
     * comment like
     * @param tag -> comment_like
     * @param uid_other
     * @param uid_me
     * @return
     */
    @FormUrlEncoded
    @POST("article/comment.php")
    Call<CommonResponse> CommentLike(@Field("tag") String tag, @Field("comment_id") String comment_id,
                                     @Field("uid_other") String uid_other, @Field("uid_me") String uid_me);

    /**
     * change private mode
     * @param tag -> change_private_mode
     * @param article_id
     * @param private_mode
     * @return
     */
    @FormUrlEncoded
    @POST("article/article.php")
    Call<CommonResponse> ChangePrivateMode(@Field("tag") String tag, @Field("article_id") String article_id,
                                           @Field("private_mode") String private_mode);

    /**
     * send report
     * @param tag -> send_report
     * @param article_id
     * @param uid
     * @param report_text
     * @return
     */
    @FormUrlEncoded
    @POST("report/article_report.php")
    Call<CommonResponse> PostArticleReport(@Field("tag") String tag, @Field("article_id") String article_id,
                                           @Field("uid") String uid, @Field("report_text") String report_text);

    /**
     * post push state
     * @param tag -> app_push / comment_push / like_push
     * @param uid
     * @param push_state
     * @return
     */
    @FormUrlEncoded
    @POST("login/login.php")
    Call<CommonResponse> PostPushState(@Field("tag") String tag, @Field("uid") String uid,
                                       @Field("push_state") String push_state);

    /**
     * 아티클 작성자에게 씨앗보내기
     * @param tag -> post_seed_to_article_user
     * @param article_user_id
     * @param uid_me
     * @param seed_cnt
     * @return
     */
    @FormUrlEncoded
    @POST("article/article.php")
    Call<CommonResponse> PostSeedToArticleUser(@Field("tag") String tag, @Field("article_user_id") String article_user_id,
                                               @Field("uid_me") String uid_me, @Field("seed_cnt") int seed_cnt);

    /**
     * register email
     * @param tag -> register_email
     * @param uid
     * @param email
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("login/login.php")
    Call<CommonResponse> RegisterEmail(@Field("tag") String tag, @Field("uid") String uid,
                                       @Field("email") String email, @Field("password") String password);

    /**
     * login email
     * @param tag -> email_login
     * @param email
     * @param password
     * @return
     */
    @GET("login/login.php")
    Call<UserResponse> LoginEmail(@Query("tag") String tag, @Query("email") String email,
                                  @Query("password") String password);
}

package OSS.geteatwithme.Connection;

import java.util.LinkedList;
import java.util.List;

import OSS.geteatwithme.AlarmInfo.Alarm;
import OSS.geteatwithme.PostInfo.Post;
import OSS.geteatwithme.UserInfo.UserProfile;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserProfileAPI {
    @GET("/user/all")
    Call<List<UserProfile>> getAllUserProfile();
    @GET("/user/idcheck/{id}")
    Call<Integer> checkPassword(
            @Path("id") String id
    );
    @GET("/user/nickcheck/{id}")
    Call<Integer> checkNick(
            @Path("id") String id
    );
    @FormUrlEncoded
    @PUT("/user/{id}")
    Call<UserProfile> createPost(
            @Path("id") String id,
            @Field("name") String name,
            @Field("gender") int gender,
            @Field("password") String password,
            @Field("age") int age,
            @Field("nickname") String nickname,
            @Field("token_id")String token_id
    );
    @GET("/user/{id}")
    Call<UserProfile> getUserProfile(
            @Path("id")String id
    );
    @GET("/user/login/{id}/{password}")
    Call<Integer> checkLogin(
            @Path("id")String id,
            @Path("password")String password
    );
    @GET("/post/all")
    Call<LinkedList<Post>> getAllPost();
    @GET("/post/{category}/alllist")
    Call<LinkedList<Post>> getCategoryPost(
            @Path("category")int category
    );
    @FormUrlEncoded
    @PUT("/post/{id}")
    Call<Integer> PutUserPost(
            @Path("id")String id,
            @Field("restaurant")String restaurant,
            @Field("meeting_place")String meeting_place,
            @Field("category")int category,
            @Field("max_people")int max_people,
            @Field("cur_people")int cur_people,
            @Field("meeting_date")String meeting_date,
            @Field("meeting_time")String meeting_time,
            @Field("contents")String contents,
            @Field("Longtitude")Double Longitutde,
            @Field("Latitude")Double Latitude,
            @Field("meet_x")Double meet_x,
            @Field("meet_y")Double meet_y,
            @Field("restaurant_id")int restaurant_id,
            @Field("visible")int visible,
            @Field("sec")int sec
    );
    @GET("/post/search/{name}")
    Call<LinkedList<Post>> getSearchingPost(
            @Path("name")String name
    );
    @GET("/post/{id}/all")
    Call<LinkedList<Post>> getUserAllPost(
            @Path("id")String id
    );
    @GET("/post/find_by_post_id/{id}")
    Call<Post>getPostByPost_id(
            @Path("id")int id
    );
    @GET("v2/local/search/keyword.json")
    Call<ResultSearchKeyword>GetSearchKeyword(
            @Header("Authorization")String key, @Query("query")String query, @Query("x")String x,@Query("y")String y, @Query("radius")String radius
    );
    @POST("/user/token/{id}/{token}")
    Call<Integer>UpdateToken(
            @Path("id")String id,
            @Path("token")String token
    );
    @POST("/notification/data")
    Call<NotificationResponse>PutNotification(
            @Body NotificationRequest notificationRequest
    );
    @FormUrlEncoded
    @POST("/update/user/{id}")
    Call<Integer>UpdateUserProfile(
            @Path("id")String id,
            @Field("password")String password,
            @Field("age")int age,
            @Field("nickname")String nickname
    );
    @FormUrlEncoded
    @POST("/update/user/withoutpw/{id}")
    Call<Integer>UpdateUserProfileWithoutPw(
            @Path("id")String id,
            @Field("age")int age,
            @Field("nickname")String nickname
    );
    @FormUrlEncoded
    @PUT("/alarm/{id}")
    Call<Integer>InsertAlarm(
            @Path("id")String id,
            @Field("request")int request,
            @Field("opposite_id")String opposite_id,
            @Field("post_id")int post_id,
            @Field("view")int view,
            @Field("nickname")String nickname,
            @Field("opposite_nickname")String opposite_nickname,
            @Field("id_token_id")String id_token_id,
            @Field("opposite_token_id")String opposite_token_id,
            @Field("restaurant")String restaurant,
            @Field("date")String date
    );
    @GET("/getalarm/{id}")
    Call<LinkedList<Alarm>>GetAlarm(
            @Path("id")String id
    );
    @POST("/update/alarm/{id}")
    Call<Integer>UpdateViewAlarm(
            @Path("id")int id
    );
    @POST("/update/post/curpeople/{id}")
    Call<Void>UpdateCurPeople(
            @Path("id")int id
    );
    @FormUrlEncoded
    @POST("/update/post/data/{post_id}")
    Call<Integer>UpdatePostData(
      @Path("post_id")int post_id,
      @Field("restaurant")String restaurant,
      @Field("meeting_place")String meeting_place,
      @Field("category")int category,
      @Field("max_people")int max_people,
      @Field("cur_people")int cur_people,
      @Field("meeting_date")String meeting_date,
      @Field("meeting_time")String meeting_time,
      @Field("contents")String contents,
      @Field("Longtitude")Double Longtitude,
      @Field("Latitude")Double Latitude,
      @Field("meet_x")Double meet_x,
      @Field("meet_y")Double meet_y,
      @Field("restaurant_id")int restaurant_id,
      @Field("visible")int visible,
      @Field("sec")int sec
    );
    @POST("/delete/post/{post_id}")
    Call<Void>DeletePostData(
            @Path("post_id")int post_id
    );
    @POST("/update/visible/{post_id}")
    Call<Void>updateVisible(
            @Path("post_id")int post_id
    );
}

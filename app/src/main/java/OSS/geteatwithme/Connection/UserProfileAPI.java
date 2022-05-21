package OSS.geteatwithme.Connection;

import java.util.LinkedList;
import java.util.List;

import OSS.geteatwithme.PostInfo.Post;
import OSS.geteatwithme.UserInfo.UserProfile;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
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
            @Field("visible")int visible
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
    @PUT("/update/token/{id}/{token}")
    Call<Integer>UpdateToken(
      @Path("id")String id,@Path("token")String token
    );
    @PUT("/insert/token/{id}/{token}")
    Call<Integer>InsertToken(
            @Path("id")String id,@Path("token")String token
    );
}

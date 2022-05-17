package OSS.geteatwithme.Connection;

import java.util.LinkedList;
import java.util.List;

import OSS.geteatwithme.PostInfo.Post;
import OSS.geteatwithme.UserInfo.UserProfile;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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
            @Field("nickname") String nickname
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
    Call<Post> PutUserPost(
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
            @Field("Latitude")Double Latitude
    );
}

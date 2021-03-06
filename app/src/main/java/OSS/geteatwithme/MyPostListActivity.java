package OSS.geteatwithme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import OSS.geteatwithme.Connection.RetrofitService;
import OSS.geteatwithme.Connection.UserProfileAPI;
import OSS.geteatwithme.PostInfo.MyPostView;
import OSS.geteatwithme.PostInfo.Post;
import OSS.geteatwithme.UIInfo.Utils;
import OSS.geteatwithme.UserInfo.UserProfile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPostListActivity extends AppCompatActivity {
    private static double degtoRad(double deg){
        return deg*Math.PI/180.0;
    }
    private static double radtoDeg(double rad){
        return rad*180/Math.PI;
    }
    public static double getDistance(Post p, double longitude, double latitude){
        double theta = longitude - p.getLongitude();
        double distance = Math.sin(degtoRad(latitude)) * Math.sin(degtoRad(p.getLatitude())) + Math.cos(degtoRad(latitude)) * Math.cos(degtoRad(p.getLatitude())) * Math.cos(degtoRad(theta));
        distance = Math.acos(distance);
        distance = radtoDeg(distance);
        distance = distance*60*1.1515;
        distance = distance*1609.344;
        return distance;
    }
    LinkedList<Post> my_posts = new LinkedList<Post>();
    LinearLayout linearlayout = null;

    private class GetTask extends AsyncTask<Call,Void, UserProfile> {
        @Override
        protected UserProfile doInBackground(Call... calls) {
            try{
                Call<UserProfile> call=calls[0];
                Response<UserProfile> response=call.execute();
                return response.body();
            }catch(IOException e){

            }
            return null;
        }
    }
    UserProfile user_info = new UserProfile();

    void showMyPosts() {
        SharedPreferences auto = getSharedPreferences("LoginSource", Activity.MODE_PRIVATE);
        String longitude=auto.getString("longitude",null);
        String latitude=auto.getString("latitude",null);
        for(Post p : my_posts) {
            p.setDistance(getDistance(p,Double.parseDouble(longitude), Double.parseDouble(latitude)));
        }
        if (linearlayout != null)
            ((ViewGroup) linearlayout.getParent()).removeView(linearlayout);
        linearlayout = new LinearLayout(this);
        linearlayout.setOrientation(LinearLayout.VERTICAL);

        // ????????? ?????????
        int idx = 0;
        for (Post p : my_posts) {
            MyPostView postView = new MyPostView(this);
            if(p.getPost_visible()==1){
                postView.set(p);
                postView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(getApplicationContext(), EditPostActivity.class);
                        myIntent.putExtra("postID", postView.getPostID());
                        startActivity(myIntent);
                        finish();
                    }
                });
                linearlayout.addView(postView, idx++);
            }
        }

        ScrollView view = (ScrollView)findViewById(R.id.my_posts_scroll);
        view.addView(linearlayout, 0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        setContentView(R.layout.activity_my_post_list);
        Utils.setStatusBarColor(this, Utils.StatusBarColorType.MAIN_ORANGE_STATUS_BAR);
        linearlayout = (LinearLayout)findViewById(R.id.mypost_layout);

        // ?????? post ????????????
        RetrofitService retrofitService = new RetrofitService();
        UserProfileAPI userProfileAPI = retrofitService.getRetrofit().create(UserProfileAPI.class);

        SharedPreferences auto = getSharedPreferences("LoginSource", Activity.MODE_PRIVATE);
        Call<UserProfile> calls= userProfileAPI.getUserProfile(auto.getString("ID",null));
        try {
            user_info=new MyPostListActivity.GetTask().execute(calls).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // ??? ?????? - ?????? ????????????
        userProfileAPI.getUserAllPost(user_info.getId())
                .enqueue(new Callback<LinkedList<Post>>() {
                    @Override
                    public void onResponse(Call<LinkedList<Post>> call, Response<LinkedList<Post>> response) {
                        my_posts=response.body();
                        showMyPosts();
                    }

                    @Override
                    public void onFailure(Call<LinkedList<Post>> call, Throwable t) {

                    }
                });
    }

}
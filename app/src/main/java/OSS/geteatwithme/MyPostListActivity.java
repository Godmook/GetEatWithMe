package OSS.geteatwithme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import OSS.geteatwithme.Connection.RetrofitService;
import OSS.geteatwithme.Connection.UserProfileAPI;
import OSS.geteatwithme.PostInfo.MyPostView;
import OSS.geteatwithme.PostInfo.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPostListActivity extends AppCompatActivity {

    LinkedList<Post> my_posts = new LinkedList<Post>();
    LinearLayout linearlayout = null;

    void showMyPosts() {

        if (linearlayout != null)
            ((ViewGroup) linearlayout.getParent()).removeView(linearlayout);
        linearlayout = new LinearLayout(this);
        linearlayout.setOrientation(LinearLayout.VERTICAL);

        // 게시글 만들기
        int idx = 0;
        for (Post p : my_posts) {
            MyPostView postView = new MyPostView(this);
            postView.set(p);
            postView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(getApplicationContext(), EditPostActivity.class);
                    myIntent.putExtra("postID", postView.getPostID());
                    //Toast.makeText(MyPostListActivity.this,postView.getPostID(),Toast.LENGTH_SHORT).show();
                    startActivity(myIntent);
                    finish();
                }
            });
            linearlayout.addView(postView, idx++);
        }

        ScrollView view = (ScrollView)findViewById(R.id.my_posts_scroll);
        view.addView(linearlayout, 0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        setContentView(R.layout.activity_my_post_list);

        linearlayout = (LinearLayout)findViewById(R.id.mypost_layout);

        // 전체 post 보여주기
        RetrofitService retrofitService = new RetrofitService();
        UserProfileAPI userProfileAPI = retrofitService.getRetrofit().create(UserProfileAPI.class);
        // 첫 화면 - 전체 보여주기
        userProfileAPI.getUserAllPost("abcd")
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
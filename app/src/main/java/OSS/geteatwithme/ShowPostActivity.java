package OSS.geteatwithme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import OSS.geteatwithme.Chatting.ChatRoom;
import OSS.geteatwithme.Connection.NotificationRequest;
import OSS.geteatwithme.Connection.NotificationResponse;
import OSS.geteatwithme.Connection.RetrofitService;
import OSS.geteatwithme.Connection.UserProfileAPI;
import OSS.geteatwithme.PostInfo.MyPostView;
import OSS.geteatwithme.PostInfo.Post;
import OSS.geteatwithme.UIInfo.Utils;
import OSS.geteatwithme.UserInfo.UserProfile;
import OSS.geteatwithme.UserInfo.user;
import lombok.var;
import okhttp3.internal.cache.DiskLruCache;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowPostActivity extends AppCompatActivity {
    int POST_ID;
    Post post;
    UserProfile myUp;
    private class PostTask extends AsyncTask<Call,Void,Post> {
        @Override
        protected Post doInBackground(Call... calls) {
            try{
                Call<Post> call=calls[0];
                Response<Post> response=call.execute();
                return response.body();
            }catch(IOException e){

            }
            return null;
        }
    }
    private class GetTokenTask extends AsyncTask<Call,Void,UserProfile>{
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
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);
        Utils.setStatusBarColor(this, Utils.StatusBarColorType.MAIN_ORANGE_STATUS_BAR);
        // post id ????????????
        Intent myIntent = getIntent();
        POST_ID = myIntent.getIntExtra("postID", 0);
        SharedPreferences auto = getSharedPreferences("LoginSource", Activity.MODE_PRIVATE);
        String user_nickname=auto.getString("Nickname",null);
        myUp=new UserProfile();

        // ?????? ????????? ????????? ??????
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://geteatwithme-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference userinfo = database.getReference().child("chatrooms").child("Room"+POST_ID).child("userInfo");
        userinfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot user : snapshot.getChildren()){
                    String nickname = user.getValue(String.class);
                    if(user_nickname.equals(nickname)){
                        Toast.makeText(ShowPostActivity.this, "?????? ????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // post ????????????
        RetrofitService retrofitService = new RetrofitService();
        UserProfileAPI userProfileAPI = retrofitService.getRetrofit().create(UserProfileAPI.class);
        Call<Post>call=userProfileAPI.getPostByPost_id(POST_ID);
        try {
            post=new PostTask().execute(call).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        userProfileAPI.getUserProfile(post.getId())
                .enqueue(new Callback<UserProfile>() {
                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                        myUp=response.body();
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {

                    }
                });

        // post ?????? ??????
        double longitude = ((user)getApplication()).getLongitude();
        double latitude = ((user)getApplication()).getLatitude();
        post.setDistance(MainActivity.getDistance(post, longitude, latitude));

        // MyPostView ??????
        MyPostView postView = (MyPostView) findViewById(R.id.postView);
        postView.set(post);

        // posting contents ????????????
        TextView contentsView = (TextView) findViewById(R.id.textView11);
        contentsView.setText(post.getContents());

        // ?????? ??????
        TextView restaurant = (TextView)findViewById(R.id.textView12);
        restaurant.setText( "??? ????????? ?????? : " + post.getRestaurant());

        // ?????? ?????? ??????
        MapView mapView = new MapView(this);
        // ?????? ??????
        MapPoint myPoint = MapPoint.mapPointWithGeoCoord(post.getLatitude(), post.getLongitude());
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("?????? : " + post.getRestaurant());
        marker.setTag(0);
        marker.setMapPoint(myPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        mapView.addPOIItem(marker);
        // ?????? ?????? ??????

        MapPoint myPoint2 = MapPoint.mapPointWithGeoCoord(post.getMeet_y(),post.getMeet_x() );
        MapPOIItem marker1 = new MapPOIItem();
        marker1.setItemName("?????? ?????? : "+ post.getMeeting_place());
        marker1.setTag(0);
        marker1.setMapPoint(myPoint2);
        marker1.setMarkerType(MapPOIItem.MarkerType.RedPin);
        mapView.addPOIItem(marker1);
        // ?????? ?????? ??????
        MapPoint myPoint3 = MapPoint.mapPointWithGeoCoord(latitude, longitude);
        MapPOIItem marker2 = new MapPOIItem();
        marker2.setItemName("?????? ??????");
        marker2.setTag(0);
        marker2.setMapPoint(myPoint3);
        marker2.setMarkerType(MapPOIItem.MarkerType.YellowPin);
        mapView.addPOIItem(marker2);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        MapPolyline polyline = new MapPolyline();
        polyline.setTag(1000);
        polyline.setLineColor(Color.argb(128, 255, 51, 0)); // Polyline ?????? ??????.

        // Polyline ?????? ??????.
        polyline.addPoint(myPoint); // ?????? ??????
        polyline.addPoint(myPoint2); // ?????? ??????
        polyline.addPoint(myPoint3); // ?????? ??????

        // Polyline ????????? ?????????.
        mapView.addPolyline(polyline);

        // ???????????? ??????????????? ???????????? Polyline??? ?????? ???????????? ??????.
        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
        int padding = 100; // px
        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

        // ?????? ?????? ????????? ??????
        ScrollView scrollView = findViewById(R.id.scrollView3);
        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }
        });
        // ?????? ?????? ?????? ??????
        Button showRestaurantInfo = (Button) findViewById(R.id.button3);
        showRestaurantInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://place.map.kakao.com/"+post.getRestaurant_id()));
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myintent);
            }
        });

        // ??????
        TextView distance = (TextView)findViewById(R.id.textView10);
        distance.setText( "??? ?????? : ??? " + (int)post.getDistance() + "m");

        // ??????
        TextView people = (TextView)findViewById(R.id.textView13);
        people.setText( "??? ?????? : " + post.getCur_people() + " / " + post.getMax_people());

        // ?????? ??????
        TextView meetPlace = (TextView)findViewById(R.id.textView14);
        meetPlace.setText( "??? ?????? ?????? : " + post.getMeeting_place());

        // ?????? ??????
        TextView meetDate = (TextView)findViewById(R.id.textView16);
        meetDate.setText( "??? ?????? ?????? : " + post.getMeeting_date());

        // ?????? ??????
        TextView meetTime = (TextView)findViewById(R.id.textView19);
        meetTime.setText( "??? ?????? ?????? : " + post.getMeeting_time());

        // ?????? ??????
        Button button1 = (Button) findViewById(R.id.button4);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // ?????? ??????
        Button button2 = (Button) findViewById(R.id.button6);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences auto = getSharedPreferences("LoginSource", Activity.MODE_PRIVATE);
                String user_id=auto.getString("ID",null);
                String token_id=auto.getString("Token_id",null);
                UserProfile tokens=new UserProfile();
                UserProfile myProfile=new UserProfile();
                RetrofitService retrofitService = new RetrofitService();
                UserProfileAPI userProfileAPI = retrofitService.getRetrofit().create(UserProfileAPI.class);
                Call<UserProfile> calls= userProfileAPI.getUserProfile(post.getId());
                try {
                    tokens=new GetTokenTask().execute(calls).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Call<UserProfile> call= userProfileAPI.getUserProfile(user_id);
                try {
                    myProfile=new GetTokenTask().execute(call).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                NotificationRequest notificationRequest=new NotificationRequest("?????? ?????? ??? ?????????..?","???????????? ????????? ?????? ?????? ?????? ?????????!",tokens.getToken_id(),"FCM_EXE_ACTIVITY");
                userProfileAPI.PutNotification(notificationRequest)
                        .enqueue(new Callback<NotificationResponse>() {
                            @Override
                            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {

                            }

                            @Override
                            public void onFailure(Call<NotificationResponse> call, Throwable t) {

                            }
                        });

                userProfileAPI.InsertAlarm(tokens.getId(),1,user_id,post.getPostID(),0,myProfile.getNickname(),post.getNickname(),token_id,myUp.getToken_id(),post.getRestaurant(),post.getMeeting_date())
                        .enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                Toast.makeText(ShowPostActivity.this, "?????? ??????!", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {

                            }
                        });
            }

        });
    }
}
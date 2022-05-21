package OSS.geteatwithme;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import java.util.concurrent.ExecutionException;

import OSS.geteatwithme.Connection.NotificationRequest;
import OSS.geteatwithme.Connection.NotificationResponse;
import OSS.geteatwithme.Connection.RetrofitService;
import OSS.geteatwithme.Connection.UserProfileAPI;
import OSS.geteatwithme.PostInfo.MyPostView;
import OSS.geteatwithme.PostInfo.Post;
import OSS.geteatwithme.UserInfo.UserProfile;
import OSS.geteatwithme.UserInfo.user;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowPostActivity extends AppCompatActivity {
    int POST_ID;
    Post post;
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

        Intent myIntent = getIntent();
        POST_ID = myIntent.getIntExtra("postID", 1);

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
        // 서버에 post 정보 요청
        // test-
        /*
        post = new Post();
        post.setId("MinI0123");
        post.setRestaurant("국빈");
        post.setLongitude(126.7728);
        post.setLatitude(37.6926);
        post.setMeeting_place("현산초등학교");
        post.setCategory(1);
        post.setMax_people(4);
        post.setCur_people(2);
        post.setMeeting_date("22/05/17");
        post.setMeeting_time("18:30");
        post.setContents("짜장면, 짬뽕, 탕수육");
        post.setGender(2);
        post.setPostID(1);
        post.setAge(22);
        */
        // -test


        // post 거리 세팅
        double longitude = ((user)getApplication()).getLongitude();
        double latitude = ((user)getApplication()).getLatitude();
        post.setDistance(MainActivity.getDistance(post, longitude, latitude));

        // MyPostView 세팅
        MyPostView postView = (MyPostView) findViewById(R.id.postView);
        postView.set(post);

        // posting contents 보여주기
        TextView contentsView = (TextView) findViewById(R.id.textView11);
        contentsView.setText(post.getContents());

        // 식당 이름
        TextView restaurant = (TextView)findViewById(R.id.textView12);
        restaurant.setText( "● 음식점 이름 : " + post.getRestaurant());

        // 식당 위치 지도
        MapView mapView = new MapView(this);
        // 식당 마커
        MapPoint myPoint = MapPoint.mapPointWithGeoCoord(post.getLatitude(), post.getLongitude());
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("식당 : " + post.getRestaurant());
        marker.setTag(0);
        marker.setMapPoint(myPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        mapView.addPOIItem(marker);
        // 만날 장소 마커
        // test-
        double meet_latitude = post.getMeet_y();
        double meet_longitude = post.getMeet_x();
        // -test
        MapPoint myPoint2 = MapPoint.mapPointWithGeoCoord(meet_latitude,meet_longitude );
        MapPOIItem marker1 = new MapPOIItem();
        marker1.setItemName("만날 장소 : "+ post.getMeeting_place());
        marker1.setTag(0);
        marker1.setMapPoint(myPoint2);
        marker1.setMarkerType(MapPOIItem.MarkerType.RedPin);
        mapView.addPOIItem(marker1);
        // 현재 위치 마커
        MapPoint myPoint3 = MapPoint.mapPointWithGeoCoord(latitude, longitude);
        MapPOIItem marker2 = new MapPOIItem();
        marker2.setItemName("현재 위치");
        marker2.setTag(0);
        marker2.setMapPoint(myPoint3);
        marker2.setMarkerType(MapPOIItem.MarkerType.YellowPin);
        mapView.addPOIItem(marker2);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        MapPolyline polyline = new MapPolyline();
        polyline.setTag(1000);
        polyline.setLineColor(Color.argb(128, 255, 51, 0)); // Polyline 컬러 지정.

        // Polyline 좌표 지정.
        polyline.addPoint(myPoint); // 현재 위치
        polyline.addPoint(myPoint2); // 만날 위치
        polyline.addPoint(myPoint3); // 식당 위치

        // Polyline 지도에 올리기.
        mapView.addPolyline(polyline);

        // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
        int padding = 100; // px
        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

        // 지도 터치 이벤트 설정
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
        Toast.makeText(ShowPostActivity.this,post.getId(),Toast.LENGTH_SHORT).show();
        // 식당 정보 보기 버튼
        int restaurant_id = post.getRestaurant_id();
        Button showRestaurantInfo = (Button) findViewById(R.id.button3);
        showRestaurantInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://place.map.kakao.com/"+restaurant_id));
                startActivity(myintent);
            }
        });

        // 거리
        TextView distance = (TextView)findViewById(R.id.textView10);
        distance.setText( "● 거리 : 약 " + (int)post.getDistance() + "m");

        // 인원
        TextView people = (TextView)findViewById(R.id.textView13);
        people.setText( "● 인원 : " + post.getCur_people() + " / " + post.getMax_people());

        // 만날 장소
        TextView meetPlace = (TextView)findViewById(R.id.textView14);
        meetPlace.setText( "● 만날 장소 : " + post.getMeeting_place());

        // 만날 날짜
        TextView meetDate = (TextView)findViewById(R.id.textView16);
        meetDate.setText( "● 만날 날짜 : " + post.getMeeting_date());

        // 만날 시간
        TextView meetTime = (TextView)findViewById(R.id.textView19);
        meetTime.setText( "● 만날 시간 : " + post.getMeeting_time());

        // 취소 버튼
        Button button1 = (Button) findViewById(R.id.button4);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 신청 버튼
        Button button2 = (Button) findViewById(R.id.button6);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfile tokens=new UserProfile();
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
                NotificationRequest notificationRequest=new NotificationRequest("나랑 같이 밥 먹을래..?","누군가가 당신과 같이 밥을 먹고 싶어요!",tokens.getToken_id());
                userProfileAPI.PutNotification(notificationRequest)
                        .enqueue(new Callback<NotificationResponse>() {
                            @Override
                            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {

                            }

                            @Override
                            public void onFailure(Call<NotificationResponse> call, Throwable t) {

                            }
                        });
            }

        });
    }
}
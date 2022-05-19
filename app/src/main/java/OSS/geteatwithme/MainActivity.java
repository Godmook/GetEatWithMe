package OSS.geteatwithme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import OSS.geteatwithme.Connection.RetrofitService;
import OSS.geteatwithme.Connection.UserProfileAPI;
import OSS.geteatwithme.PostInfo.MyPostView;
import OSS.geteatwithme.PostInfo.Post;
import OSS.geteatwithme.UserInfo.user;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RadioButton[] radioButtons = new RadioButton[8];
    LinkedList<Post> posts = new LinkedList<Post>();
    LinearLayout linearlayout = null;
    // 현재 위치
    double longitude;
    double latitude;

    private double degtoRad(double deg){
        return deg*Math.PI/180.0;
    }
    private double radtoDeg(double rad){
        return rad*180/Math.PI;
    }

    // post 식당과 현재 위치 사이 거리
    public double getDistance(Post p, double longitude, double latitude){
        double theta = longitude - p.getLongitude();
        double distance = Math.sin(degtoRad(latitude)) * Math.sin(degtoRad(p.getLatitude())) + Math.cos(degtoRad(latitude)) * Math.cos(degtoRad(p.getLatitude())) * Math.cos(degtoRad(theta));
        distance = Math.acos(distance);
        distance = radtoDeg(distance);
        distance = distance*60*1.1515;
        distance = distance*1609.344;
        return distance;
    }

    void setAllRadioButtonOff() {
        for (int i = 0; i < 8; i++)
            radioButtons[i].setChecked(false);
    }

    void showPosts() {
        for(Post p : posts)
            p.setDistance(getDistance(p, longitude, latitude));

        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post o1, Post o2) {
                return (int)(o1.getDistance() - o2.getDistance());
            }
        });

        if (linearlayout != null)
            ((ViewGroup) linearlayout.getParent()).removeView(linearlayout);
        linearlayout = new LinearLayout(this);
        linearlayout.setOrientation(LinearLayout.VERTICAL);

        // 게시글 만들기
        int idx = 0;
        for (Post p : posts) {
            MyPostView postView = new MyPostView(this);
            postView.set(p);
            postView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(getApplicationContext(), ShowPostActivity.class);
                    myIntent.putExtra("postID", postView.getPostID());
                    startActivity(myIntent);
                }
            });
            linearlayout.addView(postView, idx++);

        }

        ScrollView view = (ScrollView)findViewById(R.id.scrollView2);
        view.addView(linearlayout, 0);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        setContentView(R.layout.activity_main);

        // test-
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // gps 권한 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        ((user)getApplication()).setLatitude(location.getLatitude());
        ((user)getApplication()).setLogitude(location.getLongitude());

        ((user)getApplication()).setUserID("user id");
        // -test


        // 경도, 위도를 주소로 변환
        longitude = ((user)getApplication()).getLongitude();
        latitude = ((user)getApplication()).getLatitude();
        Geocoder g = new Geocoder(this);
        List<Address> address = null;
        try {
            address = g.getFromLocation(latitude, longitude, 10);
            TextView textView = (TextView) findViewById(R.id.textview);
            textView.setText(address.get(2).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }

        radioButtons[0] = (RadioButton) findViewById(R.id.radioButton11);
        radioButtons[1] = (RadioButton) findViewById(R.id.radioButton12);
        radioButtons[2] = (RadioButton) findViewById(R.id.radioButton13);
        radioButtons[3] = (RadioButton) findViewById(R.id.radioButton14);
        radioButtons[4] = (RadioButton) findViewById(R.id.radioButton15);
        radioButtons[5] = (RadioButton) findViewById(R.id.radioButton16);
        radioButtons[6] = (RadioButton) findViewById(R.id.radioButton18);
        radioButtons[7] = (RadioButton) findViewById(R.id.radioButton19);

        // 전체 post 보여주기
        RetrofitService retrofitService = new RetrofitService();
        UserProfileAPI userProfileAPI = retrofitService.getRetrofit().create(UserProfileAPI.class);

        // 한식
        radioButtons[0].setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[0].setChecked(true);

            }
        });

        // 중식
        radioButtons[1].setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[1].setChecked(true);
                userProfileAPI.getCategoryPost(1)
                        .enqueue(new Callback<LinkedList<Post>>() {
                            @Override
                            public void onResponse(Call<LinkedList<Post>> call, Response<LinkedList<Post>> response) {
                                posts=response.body();
                                showPosts();
                            }

                            @Override
                            public void onFailure(Call<LinkedList<Post>> call, Throwable t) {

                            }
                        });
            }
        });

        // 일식
        radioButtons[2].setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[2].setChecked(true);
            }
        });

        // 양식
        radioButtons[3].setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[3].setChecked(true);
            }
        });

        // 분식
        radioButtons[4].setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[4].setChecked(true);
            }
        });

        // 아시안
        radioButtons[5].setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[5].setChecked(true);
            }
        });

        // 패스트 푸드
        radioButtons[6].setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[6].setChecked(true);
            }
        });

        // 전체
        radioButtons[7].setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[7].setChecked(true);
                userProfileAPI.getAllPost()
                        .enqueue(new Callback<LinkedList<Post>>() {
                            @Override
                            public void onResponse(Call<LinkedList<Post>> call, Response<LinkedList<Post>> response) {
                                posts=response.body();
                                showPosts();
                            }

                            @Override
                            public void onFailure(Call<LinkedList<Post>> call, Throwable t) {

                            }
                        });
            }
        });

        // 첫 화면 - 전체 보여주기
        userProfileAPI.getAllPost()
                .enqueue(new Callback<LinkedList<Post>>() {
                    @Override
                    public void onResponse(Call<LinkedList<Post>> call, Response<LinkedList<Post>> response) {
                        posts=response.body();
                        showPosts();
                    }

                    @Override
                    public void onFailure(Call<LinkedList<Post>> call, Throwable t) {

                    }
                });

        Button search = (Button) findViewById(R.id.searchButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.editTextRestaurantName);
                String str = editText.getText().toString(); // 검색 문자열
                userProfileAPI.getSearchingPost(str)
                        .enqueue(new Callback<LinkedList<Post>>() {
                            @Override
                            public void onResponse(Call<LinkedList<Post>> call, Response<LinkedList<Post>> response) {
                                posts=response.body();
                                showPosts();
                            }

                            @Override
                            public void onFailure(Call<LinkedList<Post>> call, Throwable t) {

                            }
                        });
                // posts 업데이트 필요
                //showPosts();
            }
        });

        // 모집 글 등록 버튼
        Button registration = (Button) findViewById(R.id.registrationButton);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), PostingActivity.class);
                startActivity(myIntent);
            }
        });

        // 채팅 버튼
        ImageButton chat = (ImageButton) findViewById(R.id.chatButton);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), MyPageActivity.class);    // test
                startActivity(myIntent);
            }
        });

        // 알람 버튼
        ImageButton notice = (ImageButton) findViewById(R.id.noticeButton);
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), MyPageActivity.class);    // test
                startActivity(myIntent);
            }
        });

        // 마이 페이지 버튼
        ImageButton myPage = (ImageButton) findViewById(R.id.MyPageButton);
        myPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), MyPageActivity.class);
                startActivity(myIntent);
            }
        });
    }

}
package OSS.geteatwithme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.chip.ChipGroup;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import OSS.geteatwithme.Connection.RetrofitService;
import OSS.geteatwithme.Connection.UserProfileAPI;
import OSS.geteatwithme.PostInfo.MyPostView;
import OSS.geteatwithme.PostInfo.Post;
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

    void setAllRadioButtonOff() {
        for (int i = 0; i < 8; i++)
            radioButtons[i].setChecked(false);
    }

    void showPosts() {
        if (linearlayout != null)
            ((ViewGroup) linearlayout.getParent()).removeView(linearlayout);
        linearlayout = new LinearLayout(this);
        linearlayout.setOrientation(LinearLayout.VERTICAL);

        // 게시글 만들기
        int idx = 0;
        for (Post p : posts) {
            MyPostView postView = new MyPostView(this);
            postView.set(p);
            String bg = null;
            int age = p.getAge(); //test 값
            // 나이별 배경 색
            if (age < 20) bg = "#66FFB2";    // 10대
            else if (age < 30) bg = "#33FF99";    // 20대
            else if (age < 40) bg = "#00FF80";    // 30대
            else if (age < 50) bg = "#00CC66";    // 40대
            else if (age < 60) bg = "#00994C";    // 50대
            else bg = "#006633";    // 60대 이상
            postView.setDistance(p, longitude, latitude); // 거리 값 세팅
            postView.setBackgroundColor(Color.parseColor(bg));  // 배경 색 세팅
            postView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(getApplicationContext(), ShowPosting.class);
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

        // 현재 경도, 위도 가져오기
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // gps 권한 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = location.getLongitude();
        latitude = location.getLatitude();

        Geocoder g = new Geocoder(this);
        List<Address> address = null;
        try {
            address = g.getFromLocation(latitude, longitude, 10);
            System.out.println(address.get(0).getAddressLine(0));
            TextView textView = (TextView) findViewById(R.id.textview);
            textView.setText(address.get(2).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }


        // test-
        // -test

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
            }
        });

        Button registration = (Button) findViewById(R.id.registrationButton);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), PostingActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
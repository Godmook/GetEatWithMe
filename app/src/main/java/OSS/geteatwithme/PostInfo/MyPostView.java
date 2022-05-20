package OSS.geteatwithme.PostInfo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import OSS.geteatwithme.R;

public class MyPostView extends ConstraintLayout {
    int POST_ID;
    TextView textRestaurant;
    TextView textCategory;
    TextView textDate;
    TextView textTime;
    TextView textID;
    TextView textGender;
    TextView textPeople;
    TextView textDistance;
    ConstraintLayout background;

    public MyPostView(@NonNull Context context) {
        super(context);
        initView();
    }

    public MyPostView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public MyPostView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttrs(attrs);
    }

    public MyPostView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
        getAttrs(attrs);
    }

    private void initView(){
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.my_post_view, this, false);
        addView(v);

        textRestaurant = (TextView) findViewById(R.id.textRestaurant);
        textCategory= (TextView) findViewById(R.id.textCategory);
        textDate= (TextView) findViewById(R.id.textDate);
        textTime= (TextView) findViewById(R.id.textTime);
        textID= (TextView) findViewById(R.id.textID);
        textGender= (TextView) findViewById(R.id.textGender);
        textPeople= (TextView) findViewById(R.id.textPeople);
        textDistance= (TextView) findViewById(R.id.textDistance);
        background = (ConstraintLayout) findViewById(R.id.background);
    }

    private void getAttrs(AttributeSet attrs){
        String restaurant = attrs.getAttributeValue(9);
        String category = attrs.getAttributeValue(3);
        String date = attrs.getAttributeValue(4);
        String time = attrs.getAttributeValue(10);
        String id = attrs.getAttributeValue(7);
        String gender = attrs.getAttributeValue(6);
        String people = attrs.getAttributeValue(8);
        String distance = attrs.getAttributeValue(5);

        textRestaurant.setText(restaurant);
        textCategory.setText(category);
        textDate.setText(date);
        textTime.setText(time);
        textID.setText(id);
        textGender.setText(gender);
        textPeople.setText(people);
        textDistance.setText(distance);
    }

    private void getAttrs(AttributeSet attrs, int defStyle){
        getAttrs(attrs);
    }

    public void set(Post p){
        POST_ID = p.getPostID();

        textRestaurant.setText(p.getRestaurant());
        String category = null;
        switch(p.getCategory()){
            case 0: category = "한식"; break;
            case 1: category = "중식"; break;
            case 2: category = "일식"; break;
            case 3: category = "양식"; break;
            case 4: category = "분식"; break;
            case 5: category = "아시안"; break;
            case 6: category = "패스트푸드"; break;
        }
        textCategory.setText("# "+category);
        textDate.setText(p.getMeeting_date());
        textTime.setText(p.getMeeting_time());
        textID.setText("작성자 : "+p.getId());
        String gender = "비공개";
        if(p.getGender() == 0) gender = "남자";
        else if (p.getGender() == 1)  gender = "여자";
        textGender.setText("성별 : " + gender);    // 성별 to string 변환 필요
        textPeople.setText("인원 : "+p.getCur_people()+"/"+p.getMax_people());
        textDistance.setText("거리 : 100 m"); // 거리 계산 필요

        String bg = null;
        int age = p.getAge(); //test 값
        // 나이별 배경 색
        if (age < 20) bg = "#A9E2F3";    // 10대
        else if (age < 30) bg = "#58D3F7";    // 20대
        else if (age < 40) bg = "#00BFFF";    // 30대
        else if (age < 50) bg = "#01A9DB";    // 40대
        else if (age < 60) bg = "#0489B1";    // 50대
        else bg = "#086A87";    // 60대 이상
        background.setBackgroundColor(Color.parseColor(bg)); // 배경 색 세팅

        textDistance.setText("거리 : 약 "+ (int)p.getDistance() +" m");
    }

    public int getPostID(){ return this.POST_ID;}
}

package OSS.geteatwithme.PostInfo;

import android.content.Context;
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
    TextView textAge;
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
        textAge = (TextView) findViewById(R.id.textAge);
    }

    private void getAttrs(AttributeSet attrs){
        String restaurant = attrs.getAttributeValue(8);
        String category = attrs.getAttributeValue(2);
        String date = attrs.getAttributeValue(3);
        String time = attrs.getAttributeValue(9);
        String id = attrs.getAttributeValue(6);
        String gender = attrs.getAttributeValue(5);
        String age = attrs.getAttributeValue(10);
        String people = attrs.getAttributeValue(7);
        String distance = attrs.getAttributeValue(4);

        textRestaurant.setText(restaurant);
        textCategory.setText(category);
        textDate.setText(date);
        textTime.setText(time);
        textID.setText(id);
        textGender.setText(gender);
        textPeople.setText(people);
        textDistance.setText(distance);
        textAge.setText(age);
    }
    private void getAttrs(AttributeSet attrs, int defStyle){
        getAttrs(attrs);
    }

    public void set(Post p){
        POST_ID = p.getPostID();

        textRestaurant.setText(p.getRestaurant());
        String category = null;
        switch(p.getCategory()){
            case 0: category = "??????"; break;
            case 1: category = "??????"; break;
            case 2: category = "??????"; break;
            case 3: category = "??????"; break;
            case 4: category = "??????"; break;
            case 5: category = "?????????"; break;
            case 6: category = "???????????????"; break;
        }
        textCategory.setText("# "+category);
        textDate.setText(p.getMeeting_date());
        textTime.setText(p.getMeeting_time());
        textID.setText("????????? : "+p.getNickname());
        String gender = null;
        if(p.getGender() == 0) gender = "??????";
        else if (p.getGender() == 1)  gender = "??????";
        if(p.getVisible() == 0) gender = "?????????";
        
        textGender.setText("?????? : " + gender);
        textPeople.setText("?????? : "+p.getCur_people()+"/"+p.getMax_people());

        String bg = null;
        int age = p.getAge(); //test ???
        // ????????? ?????? ???
        if (age < 20) bg = "#A9E2F3";    // 10???
        else if (age < 30) bg = "#58D3F7";    // 20???
        else if (age < 40) bg = "#00BFFF";    // 30???
        else if (age < 50) bg = "#01A9DB";    // 40???
        else if (age < 60) bg = "#0489B1";    // 50???
        else bg = "#086A87";    // 60??? ??????
        background.setBackgroundColor(Color.parseColor(bg)); // ?????? ??? ??????
        textAge.setText("?????? : "+age/10 + "0 ???");

        textDistance.setText("?????? : ??? "+ (int)p.getDistance() +" m");
    }

    public int getPostID(){ return this.POST_ID;}
}

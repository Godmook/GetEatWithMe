package OSS.geteatwithme.AlarmInfo;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import OSS.geteatwithme.Connection.RetrofitService;
import OSS.geteatwithme.Connection.UserProfileAPI;
import OSS.geteatwithme.PostInfo.Post;
import OSS.geteatwithme.R;
import OSS.geteatwithme.ShowPostActivity;
import retrofit2.Call;
import retrofit2.Response;

public class MyAlarmView extends ConstraintLayout {
    public TextView text;
    ImageView image;
    LinearLayout background;
    public Alarm alarm;

    public MyAlarmView(@NonNull Context context) {
        super(context);
        initView();
    }

    public MyAlarmView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyAlarmView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public MyAlarmView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    void initView(){
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.my_alarm_view, this, false);
        addView(v);

        text = (TextView) findViewById(R.id.alarmText);
        image = (ImageView) findViewById(R.id.image);
        background = (LinearLayout) findViewById(R.id.alarmbackground);
    }

    public void set(Alarm alarm){

        this.alarm = alarm;
        if(alarm.getRequest() == 0){
            image.setImageResource(R.drawable.accept);
            text.setText(alarm.getNickname() + "님이 당신의 <"+alarm.getRestaurant()+"> 같이 먹기 신청을 수락하였습니다.");
        }
        else if (alarm.getRequest() == 1){
            image.setImageResource(R.drawable.application);
            text.setText(alarm.getNickname() + "님이 당신의 <"+alarm.getRestaurant()+"> 게시글에 같이 먹기를 신청하였습니다.");
        }
        else if(alarm.getRequest() == 2){
            image.setImageResource(R.drawable.reject);
            text.setText(alarm.getNickname() + "님이 당신의 <"+alarm.getRestaurant()+"> 같이 먹기 신청을 거절하였습니다.");
        }

        if(alarm.getView() == 0)
            background.setBackgroundColor(Color.parseColor("#F5A9A9"));
        else
            background.setBackgroundColor(Color.parseColor("#F7F8E0"));

    }

    public void updateBackground(){
        if(alarm.getView() == 0)
            background.setBackgroundColor(Color.parseColor("#F5A9A9"));
        else
            background.setBackgroundColor(Color.parseColor("#F7F8E0"));
    }
}

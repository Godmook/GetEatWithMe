package OSS.geteatwithme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import OSS.geteatwithme.AlarmInfo.Alarm;
import OSS.geteatwithme.AlarmInfo.MyAlarmView;
import OSS.geteatwithme.PostInfo.MyPostView;
import OSS.geteatwithme.PostInfo.Post;

public class alarmAcitivity extends AppCompatActivity {
    alarmAcitivity activity = null;
    LinkedList<Alarm> alarms = new LinkedList<>();
    LinearLayout linearLayout;
    void showAlarms() {
        if(linearLayout != null)
            ((ViewGroup) linearLayout.getParent()).removeView(linearLayout);
        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // 알람 만들기
        int idx = 0;
        for (Alarm a : alarms) {
            MyAlarmView alarmView = new MyAlarmView(this);
            alarmView.set(a);
            alarmView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 해당 알람 view 1로 변경(서버)

                    if(alarmView.alarm.getRequest() == 1 && alarmView.alarm.getView() == 0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("신청 확인");
                        builder.setMessage(alarmView.text.getText());
                        builder.setNegativeButton("거절",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 거절 눌렀을 때
                                    }
                                });
                        builder.setPositiveButton("수락",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 수락 눌렀을 때
                                    }
                                });
                        builder.show();
                    }
                    alarmView.alarm.setView(1);
                    showAlarms();
                }
            });
            linearLayout.addView(alarmView, idx++);
        }
        if(idx == 0){
            TextView textView = new TextView(this);
            textView.setText("알림이 없습니다.");
            textView.setGravity(1);
            textView.setTextSize(20);
            linearLayout.addView(textView);
        }

        ScrollView view = (ScrollView)findViewById(R.id.alarmScrollView);
        view.addView(linearLayout, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_acitivity);
        linearLayout = (LinearLayout) findViewById(R.id.alarmLinearLayout);
        activity = this;

        // alarms 서버에서 가져오기

        showAlarms();

    }
}
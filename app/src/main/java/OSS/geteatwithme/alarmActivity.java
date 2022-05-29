package OSS.geteatwithme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import OSS.geteatwithme.AlarmInfo.Alarm;
import OSS.geteatwithme.AlarmInfo.MyAlarmView;
import OSS.geteatwithme.Chatting.ChatModel;
import OSS.geteatwithme.Connection.NotificationRequest;
import OSS.geteatwithme.Connection.NotificationResponse;
import OSS.geteatwithme.Connection.RetrofitService;
import OSS.geteatwithme.Connection.UserProfileAPI;
import OSS.geteatwithme.PostInfo.Post;
import OSS.geteatwithme.UIInfo.Utils;
import OSS.geteatwithme.UserInfo.UserProfile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class alarmActivity extends AppCompatActivity {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    Post post;
    public void FirebaseDatabaseHelper(){
        mDatabase=FirebaseDatabase.getInstance("https://geteatwithme-default-rtdb.asia-southeast1.firebasedatabase.app");
        mReference=mDatabase.getReference().child("chatrooms");
    }
    public void ReadChatRoomData(){
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> keys= new ArrayList<>();
                List<ChatModel> models=new ArrayList<>();
                for(DataSnapshot keyNode: snapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    models.add(snapshot.getValue(ChatModel.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private class GetInformation extends AsyncTask<Call,Void, Post>{
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
    private class GetAlarmTask extends AsyncTask<Call,Void,LinkedList<Alarm>> {
        @Override
        protected LinkedList<Alarm> doInBackground(Call... calls) {
            try{
                Call<LinkedList<Alarm>> call=calls[0];
                Response<LinkedList<Alarm>> response=call.execute();
                return response.body();
            }catch(IOException e){

            }
            return null;
        }
    }
    alarmActivity activity = null;
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
                    RetrofitService retrofitService = new RetrofitService();
                    UserProfileAPI userProfileAPI = retrofitService.getRetrofit().create(UserProfileAPI.class);
                    userProfileAPI.UpdateViewAlarm(a.getAlarm_id())
                            .enqueue(new Callback<Integer>() {
                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {

                                }

                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {

                                }
                            });
                    if(alarmView.alarm.getRequest() == 1 && alarmView.alarm.getView() == 0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("신청 확인");
                        builder.setMessage(alarmView.text.getText());
                        builder.setNegativeButton("거절",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 거절 눌렀을 때
                                        NotificationRequest notificationRequest=new NotificationRequest("나랑 같이 밥 먹을래..?","작성자가 당신의 요청을 거절했어요 ㅠㅠ",a.getOpposite_token_id(),"FCM_EXE_ACTIVITY");
                                        userProfileAPI.PutNotification(notificationRequest)
                                                .enqueue(new Callback<NotificationResponse>() {
                                                    @Override
                                                    public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {

                                                    }

                                                    @Override
                                                    public void onFailure(Call<NotificationResponse> call, Throwable t) {

                                                    }
                                                });
                                        userProfileAPI.InsertAlarm(a.getOpposite_id(),2,a.getId(),a.getPost_id(),0,a.getOpposite_nickname(),a.getNickname(),a.getOpposite_token_id(),a.getId_token_id(),a.getRestaurant(),a.getDate())
                                                .enqueue(new Callback<Integer>() {
                                                    @Override
                                                    public void onResponse(Call<Integer> call, Response<Integer> response) {

                                                    }

                                                    @Override
                                                    public void onFailure(Call<Integer> call, Throwable t) {

                                                    }
                                                });
                                    }
                                });
                        builder.setPositiveButton("수락",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        RetrofitService retrofitService = new RetrofitService();
                                        UserProfileAPI userProfileAPI = retrofitService.getRetrofit().create(UserProfileAPI.class);
                                        userProfileAPI.UpdateCurPeople(a.getPost_id())
                                                .enqueue(new Callback<Void>() {
                                                    @Override
                                                    public void onResponse(Call<Void> call, Response<Void> response) {

                                                    }

                                                    @Override
                                                    public void onFailure(Call<Void> call, Throwable t) {

                                                    }
                                                });
                                        SharedPreferences auto = getSharedPreferences("LoginSource", Activity.MODE_PRIVATE);
                                        String user_id=auto.getString("ID",null);
                                        ChatModel chatModel=new ChatModel();
                                        FirebaseDatabase.getInstance("https://geteatwithme-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("users").child(a.getOpposite_nickname()).setValue(a.getOpposite_token_id());
                                        String room_number="Room"+a.getPost_id();
                                        // 수락 눌렀을 때
                                        FirebaseDatabase.getInstance("https://geteatwithme-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("chatrooms").child(room_number).child("userInfo").push().setValue(a.getNickname());
                                        FirebaseDatabase.getInstance("https://geteatwithme-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("chatrooms").child(room_number).child("userInfo").push().setValue(a.getOpposite_nickname());
                                        ChatModel.Comment comment=new ChatModel.Comment();
                                        comment.uid="Admin";
                                        //닉네임 위치 잘 기억 안남 나인지 쟤인지 모르겠음
                                        comment.message=a.getNickname()+"님이 입장하셨습니다.";
                                        //나도 잘 모르겠음 어떻게 해야 할지
                                        comment.timestamp= ServerValue.TIMESTAMP;
                                        FirebaseDatabase.getInstance("https://geteatwithme-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("chatrooms").child(room_number).child("comment").push().setValue(comment);
                                        Map<String,String> map= new HashMap<>();
                                        map.put("Can Use Chat","true");
                                        map.put("Restaurant_name",a.getRestaurant());
                                        map.put("Meeting_date",a.getDate());
                                        FirebaseDatabase.getInstance("https://geteatwithme-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("chatrooms").child(room_number).child("Chatting").setValue(map);
                                        NotificationRequest notificationRequest=new NotificationRequest("나랑 같이 밥 먹을래..?","작성자가 당신의 요청을 수락했어요!",a.getId_token_id(),"FCM_EXE_ACTIVITY");
                                        userProfileAPI.PutNotification(notificationRequest)
                                                .enqueue(new Callback<NotificationResponse>() {
                                                    @Override
                                                    public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {

                                                    }

                                                    @Override
                                                    public void onFailure(Call<NotificationResponse> call, Throwable t) {

                                                    }
                                                });
                                        userProfileAPI.InsertAlarm(a.getOpposite_id(),0,a.getId(),a.getPost_id(),0,a.getOpposite_nickname(),a.getNickname(),a.getOpposite_token_id(),a.getId_token_id(),a.getRestaurant(),a.getDate())
                                                .enqueue(new Callback<Integer>() {
                                                    @Override
                                                    public void onResponse(Call<Integer> call, Response<Integer> response) {

                                                    }

                                                    @Override
                                                    public void onFailure(Call<Integer> call, Throwable t) {

                                                    }
                                                });
                                    }
                                });
                        builder.show();
                    }
                    alarmView.alarm.setView(1);
                    alarmView.updateBackground();
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
        Utils.setStatusBarColor(this, Utils.StatusBarColorType.MAIN_ORANGE_STATUS_BAR);
        SharedPreferences auto = getSharedPreferences("LoginSource", Activity.MODE_PRIVATE);
        String user_id=auto.getString("ID",null);
        FirebaseDatabaseHelper();
        RetrofitService retrofitService = new RetrofitService();
        UserProfileAPI userProfileAPI = retrofitService.getRetrofit().create(UserProfileAPI.class);
        Call<LinkedList<Alarm>> calls= userProfileAPI.GetAlarm(user_id);
        try {
            alarms= new GetAlarmTask().execute(calls).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // alarms 서버에서 가져오기

        showAlarms();

    }
}
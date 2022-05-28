package OSS.geteatwithme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import OSS.geteatwithme.Chatting.ChatModel;
import OSS.geteatwithme.Chatting.ChatRoom;
import OSS.geteatwithme.Chatting.MyChatRoomView;
import OSS.geteatwithme.PostInfo.MyPostView;
import OSS.geteatwithme.PostInfo.Post;
import OSS.geteatwithme.UIInfo.Utils;


public class ChattingRoomActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://geteatwithme-default-rtdb.asia-southeast1.firebasedatabase.app");
    List<String> chatrooms_id = new ArrayList<>();
    LinkedList<ChatRoom> chatRooms = new LinkedList<>();
    LinearLayout linear = null;


    void showChatRooms(){
        if (linear != null)
            ((ViewGroup) linear.getParent()).removeView(linear);
        linear = new LinearLayout(this);
        linear.setOrientation(LinearLayout.VERTICAL);

        // 채팅방 만들기
        int idx = 0;
        for (ChatRoom c : chatRooms) {
            MyChatRoomView chatRoomView = new MyChatRoomView(this);;
            chatRoomView.set(c);
            chatRoomView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(getApplicationContext(), GroupMessageActivity.class); // test - Activity 변경 필요
                    myIntent.putExtra("chatRoomID", c.chatRoomId);
                    startActivity(myIntent);
                }
            });
            linear.addView(chatRoomView, idx++);

        }

        ScrollView view = (ScrollView)findViewById(R.id.chatScroll);
        view.addView(linear, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_room);
        linear = findViewById(R.id.chatLinear);
        Utils.setStatusBarColor(this, Utils.StatusBarColorType.MAIN_ORANGE_STATUS_BAR);
        SharedPreferences auto = getSharedPreferences("LoginSource", Activity.MODE_PRIVATE);
        String user_nickname=auto.getString("Nickname",null);
        // 채팅방 정보 가져오기
        DatabaseReference getChatrooms_id=database.getReference("chatrooms");
        getChatrooms_id.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatrooms_id.clear();
                chatRooms.clear();

                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    String chatroom_id = snapshot1.getKey();
                    // 채팅방 사용 가능 여부 가져오기
                    String available = snapshot.child(chatroom_id).child("Chatting").child("Can Use Chat").getValue(String.class);

                    if(available.equals("true")){
                        // 내 닉네임이 포함되는지 검사
                        boolean include = false;

                        for(DataSnapshot snapshot2 :snapshot1.child("userInfo").getChildren()){
                            String nicknameId = snapshot2.getKey();
                            if(snapshot1.child("userInfo").child(nicknameId).getValue().equals(user_nickname)){    // test nickname
                                include = true;
                                break;
                            }
                        }
                        // 자신이 사용가능한 채팅방인 경우 추가
                        if(include == true){
                            chatrooms_id.add(chatroom_id);

                            String last_message = "";   // 마지막 메시지
                            long last_time = 0;
                            String last_msg_id = null;

                            for(DataSnapshot msg : snapshot1.child("comment").getChildren())
                                last_msg_id = msg.getKey();

                            last_message = snapshot1.child("comment").child(last_msg_id).child("message").getValue(String.class);
                            last_time = snapshot1.child("comment").child(last_msg_id).child("timestamp").getValue(Long.class);

                            Date date = new java.util.Date(last_time);
                            SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
                            String str_date = sdf.format(date); // 마지막 메시지 시간

                            // 채팅방 이름 (식당명 + 날짜)
                            String restaurant = snapshot1.child("Chatting").child("Restaurant_name").getValue(String.class);
                            String d = snapshot1.child("Chatting").child("Meeting_date").getValue(String.class);

                            ChatRoom cr = new ChatRoom();
                            cr.chatRoomId = chatroom_id;
                            cr.last_message = last_message;
                            cr.last_time = str_date;
                            cr.chatRoomName = restaurant + "(" + d + ")";

                            chatRooms.add(cr);
                        }
                    }
                    break; //test
                }
                showChatRooms(); // 사용가능한 채팅방 사용자에게 보여주기
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
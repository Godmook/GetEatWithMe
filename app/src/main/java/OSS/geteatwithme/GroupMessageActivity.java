package OSS.geteatwithme;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import OSS.geteatwithme.Chatting.ChatModel;
import OSS.geteatwithme.Connection.NotificationRequest;
import OSS.geteatwithme.Connection.NotificationResponse;
import OSS.geteatwithme.Connection.RetrofitService;
import OSS.geteatwithme.Connection.UserProfileAPI;
import OSS.geteatwithme.UIInfo.Utils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupMessageActivity extends AppCompatActivity {
    String destinationRoom,Roomname;
    String uid;
    EditText editText;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
    private RecyclerView recyclerView;

    List<ChatModel.Comment> comments = new ArrayList<>();

    int peopleCount = 0;
    List<String> names=new ArrayList<>();
    Map<String, String> user_token = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message);
        Utils.setStatusBarColor(this, Utils.StatusBarColorType.MAIN_ORANGE_STATUS_BAR);
        Intent myIntent=new Intent();
        destinationRoom = getIntent().getStringExtra("chatRoomID");
        Roomname=getIntent().getStringExtra("chatRoomName");
        SharedPreferences auto = getSharedPreferences("LoginSource", Activity.MODE_PRIVATE);
        uid=auto.getString("Nickname",null);
        editText = (EditText)findViewById(R.id.groupMessageActivity_editText);
        names=new ArrayList<>();
        FirebaseDatabase.getInstance("https://geteatwithme-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("chatrooms").child(destinationRoom).child("userInfo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() == false) finish();
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    if(!names.contains(item.getValue(String.class))) {
                        names.add(item.getValue(String.class));
                    }
                }
                Roomname+="\n?????????: "+ names.size();
                TextView textView= (TextView) findViewById(R.id.textView18);
                textView.setText(Roomname);
                FirebaseDatabase.getInstance("https://geteatwithme-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot item : snapshot.getChildren()){
                            if(names.contains(item.getKey())) {
                                user_token.put(item.getKey(), item.getValue(String.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                init();
                recyclerView = (RecyclerView) findViewById(R.id.groupMessageActivity_recyclerview);
                recyclerView.setAdapter(new GroupMessageRecyclerViewAdapter());
                recyclerView.setLayoutManager(new LinearLayoutManager(GroupMessageActivity.this));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://geteatwithme-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference masterInfo = database.getReference().child("chatrooms").child(destinationRoom).child("master");
        masterInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot host : snapshot.getChildren()){
                    if(host.getValue(String.class).equals(uid)){
                        Button button = new Button(getApplicationContext());
                        button.setText("??????");
                        button.setBackgroundColor(Color.parseColor("#FD9F28")); // ?????? ??? ??????
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // ??? ?????????
                                DatabaseReference rRef = database.getReference("chatrooms").child(destinationRoom);
                                rRef.removeValue();
                                RetrofitService retrofitService = new RetrofitService();
                                UserProfileAPI userProfileAPI = retrofitService.getRetrofit().create(UserProfileAPI.class);
                                String substr=destinationRoom.substring(4);
                                userProfileAPI.updateVisible(Integer.parseInt(substr))
                                        .enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                finish();
                                            }

                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {

                                            }
                                        });
                            }
                        });
                        ((LinearLayout)findViewById(R.id.top_layout)).addView(button);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    void init(){
        Button button = (Button) findViewById(R.id.groupMessageActivity_button);
        button.setEnabled(false);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText.getText().toString().equals(""))
                    button.setEnabled(false);
                else
                    button.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatModel.Comment comment = new ChatModel.Comment();
                comment.uid = uid;
                comment.message = editText.getText().toString();
                comment.timestamp = ServerValue.TIMESTAMP;
                FirebaseDatabase.getInstance("https://geteatwithme-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("chatrooms").child(destinationRoom).child("comment").push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseDatabase.getInstance("https://geteatwithme-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("chatrooms").child(destinationRoom).child("userInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for(DataSnapshot item : dataSnapshot.getChildren()){
                                    if(!names.contains(item.getValue(String.class))) {
                                        names.add(item.getValue(String.class));
                                    }
                                }
                                Roomname=getIntent().getStringExtra("chatRoomName");
                                Roomname+="\n?????????: "+ names.size();
                                TextView textView= (TextView) findViewById(R.id.textView18);
                                textView.setText(Roomname);
                                FirebaseDatabase.getInstance("https://geteatwithme-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot item : snapshot.getChildren()){
                                            if(names.contains(item.getKey())) {
                                                user_token.put(item.getKey(), item.getValue(String.class));
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        FirebaseDatabase.getInstance("https://geteatwithme-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("chatrooms").child(destinationRoom).child("userInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for(DataSnapshot item : dataSnapshot.getChildren()){
                                    if(item.getValue(String.class).equals(uid)){
                                        continue;
                                    }
                                    // push message
                                    RetrofitService retrofitService = new RetrofitService();
                                    UserProfileAPI userProfileAPI = retrofitService.getRetrofit().create(UserProfileAPI.class);
                                    String tmp=comment.uid+":"+comment.message;
                                    NotificationRequest notificationRequest=new NotificationRequest("?????? ?????? ??? ?????????..?",tmp,user_token.get(item.getValue(String.class)),"FCM_EXE_ACTIVITY");
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
                                editText.setText("");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

            }
        });
    }
    class GroupMessageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        public GroupMessageRecyclerViewAdapter(){
            getMessageList();
        }
        // ??? ???????????? ????????? ???????????? ??????
        void getMessageList() {
            if(FirebaseDatabase.getInstance("https://geteatwithme-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("chatrooms").child(destinationRoom)==null){
                finish();
            }
            databaseReference = FirebaseDatabase.getInstance("https://geteatwithme-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("chatrooms").child(destinationRoom).child("comment");
            valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    comments.clear();
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        ChatModel.Comment comment_origin = item.getValue(ChatModel.Comment.class);
                        comments.add(comment_origin);
                    }
                    if(comments.size()==0){
                        finish();
                    }
                    notifyDataSetChanged();
                    recyclerView
                            .scrollToPosition(comments.size()-1);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }



        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);
            return new GroupMessageViewHodler(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            GroupMessageViewHodler messageViewHolder = ((GroupMessageViewHodler) holder);

            //???????????? ?????????
            if (comments.get(position).uid.equals(uid)) {
                messageViewHolder.textView_message.setText(comments.get(position).message);
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.rightbubble2);
                messageViewHolder.linearLayout_destination.setVisibility(View.INVISIBLE);
                messageViewHolder.textView_message.setTextSize(15);
                messageViewHolder.linearLayout_main.setGravity(Gravity.RIGHT);

                //???????????? ?????? ?????????

            } else {
                if(comments.get(position).uid.matches("Admin")){
                    messageViewHolder.textview_name.setText("");
                    messageViewHolder.linearLayout_destination.setVisibility(View.VISIBLE);
                    messageViewHolder.textView_message.setBackgroundResource(R.drawable.centerbubble2);
                    messageViewHolder.textView_message.setText(comments.get(position).message);
                    messageViewHolder.textView_message.setTextSize(15);
                    messageViewHolder.linearLayout_main.setGravity(Gravity.CENTER);
                }
                else {
                    messageViewHolder.textview_name.setText(comments.get(position).uid);
                    messageViewHolder.linearLayout_destination.setVisibility(View.VISIBLE);
                    messageViewHolder.textView_message.setBackgroundResource(R.drawable.leftbubble2);
                    messageViewHolder.textView_message.setText(comments.get(position).message);
                    messageViewHolder.textView_message.setTextSize(15);
                    messageViewHolder.linearLayout_main.setGravity(Gravity.LEFT);
                }


            }
            long unixTime = (long) comments.get(position).timestamp;
            Date date = new Date(unixTime);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            String time = simpleDateFormat.format(date);
            messageViewHolder.textView_timestamp.setText(time);
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        private class GroupMessageViewHodler extends RecyclerView.ViewHolder {

            public TextView textView_message;
            public TextView textview_name;
            public LinearLayout linearLayout_destination;
            public LinearLayout linearLayout_main;
            public TextView textView_timestamp;
            public TextView textView_readCounter_left;
            public TextView textView_readCounter_right;
            public GroupMessageViewHodler(View view) {
                super(view);

                textView_message = (TextView) view.findViewById(R.id.messageItem_textView_message);
                textview_name = (TextView) view.findViewById(R.id.messageItem_textview_name);
                linearLayout_destination = (LinearLayout) view.findViewById(R.id.messageItem_linearlayout_destination);
                linearLayout_main = (LinearLayout) view.findViewById(R.id.messageItem_linearlayout_main);
                textView_timestamp = (TextView) view.findViewById(R.id.messageItem_textview_timestamp);
                textView_readCounter_left = (TextView) view.findViewById(R.id.messageItem_textview_readCounter_left);
                textView_readCounter_right = (TextView) view.findViewById(R.id.messageItem_textview_readCounter_right);
            }
        }
    }
}
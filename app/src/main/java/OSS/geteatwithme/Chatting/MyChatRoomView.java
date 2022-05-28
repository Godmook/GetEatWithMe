package OSS.geteatwithme.Chatting;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import OSS.geteatwithme.ChattingRoomActivity;
import OSS.geteatwithme.R;


public class MyChatRoomView extends ConstraintLayout {
    TextView title;
    TextView message;
    TextView time;
    String chatRoomID;

    public MyChatRoomView(@NonNull Context context) {
        super(context);
        init();
    }

    public MyChatRoomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyChatRoomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MyChatRoomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    void init(){
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.my_chatroom_view, this, false);
        addView(v);

        title = (TextView) findViewById(R.id.chatRoomName);
        message = (TextView) findViewById(R.id.lastText);
        time = (TextView) findViewById(R.id.timeText);
    }

    public void set(ChatRoom cr){
        title.setText(cr.chatRoomName);
        message.setText(cr.last_message);
        time.setText(cr.last_time);
        chatRoomID = cr.chatRoomId;
    }
}

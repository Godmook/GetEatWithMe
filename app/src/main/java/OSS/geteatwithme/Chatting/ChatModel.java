package OSS.geteatwithme.Chatting;

import java.util.HashMap;
import java.util.Map;

public class ChatModel {

    public Map<String,Boolean> users=new HashMap<>();
    public Map<String,Comment> comments= new HashMap<>();

    public static class Comment{
        public String uid;
        public String message;
        public Object timestamp;
        public Map<String,Object> readers= new HashMap<>();
    }
}
package OSS.geteatwithme.AlarmInfo;

public class Alarm {
    String id;
    String nickname;
    int request;
    String opposite_id;
    String opposite_nickname;
    int post_id;
    int view;
    int alarm_id;
    String id_token_id;
    String opposite_token_id;

    public String getId_token_id() {
        return id_token_id;
    }

    public void setId_token_id(String id_token_id) {
        this.id_token_id = id_token_id;
    }

    public String getOpposite_token_id() {
        return opposite_token_id;
    }

    public void setOpposite_token_id(String opposite_token_id) {
        this.opposite_token_id = opposite_token_id;
    }

    public int getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(int alarm_id) {
        this.alarm_id = alarm_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getRequest() {
        return request;
    }

    public void setRequest(int request) {
        this.request = request;
    }

    public String getOpposite_id() {
        return opposite_id;
    }

    public void setOpposite_id(String opposite_id) {
        this.opposite_id = opposite_id;
    }

    public String getOpposite_nickname() {
        return opposite_nickname;
    }

    public void setOpposite_nickname(String opposite_nickname) {
        this.opposite_nickname = opposite_nickname;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }
}

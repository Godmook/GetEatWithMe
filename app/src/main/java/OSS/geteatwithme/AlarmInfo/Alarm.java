package OSS.geteatwithme.AlarmInfo;

public class Alarm {
    String id;
    String nickname;
    int request;
    String opposite_id;
    String opposite_nickname;
    int post_id;
    int view;

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

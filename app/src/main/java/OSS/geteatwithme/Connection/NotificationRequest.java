package OSS.geteatwithme.Connection;

public class NotificationRequest {
    private String title;
    private String message;
    private String topic;
    private String token;
    private String click_action;

    public String getClick_action() {
        return click_action;
    }

    public void setClick_action(String click_action) {
        this.click_action = click_action;
    }

    public String getTitle() {
        return title;
    }
    public NotificationRequest(String title, String message, String token,String click_action){
        this.title=title;
        this.message=message;
        this.token=token;
        this.click_action=click_action;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

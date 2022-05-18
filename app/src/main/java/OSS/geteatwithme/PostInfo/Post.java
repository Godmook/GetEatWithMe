package OSS.geteatwithme.PostInfo;

public class Post {
    String id;
    String restaurant;
    double longitude;
    double latitude;
    String meeting_place;
    int category;
    int max_people;
    int cur_people;
    int gender;
    String meeting_date;
    String meeting_time;
    String contents;
    int age;
    int post_id;
    double distance;

    public double getDistance(){ return distance;}

    public void setDistance(double distance){this.distance = distance;}

    public int getPostID(){ return post_id;}

    public void setPostID(int post_id){ this.post_id = post_id;}

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public double getLongitude() {return longitude; }

    public void setLongitude(double longitude){ this.longitude = longitude;}

    public double getLatitude() { return latitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public String getMeeting_date() { return meeting_date; }

    public void setMeeting_date(String meeting_date) { this.meeting_date = meeting_date;}

    public String getMeeting_place() {return meeting_place;}

    public void setMeeting_place(String meeting_place) {
        this.meeting_place = meeting_place;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getMax_people() {
        return max_people;
    }

    public void setMax_people(int max_people) {
        this.max_people = max_people;
    }

    public int getCur_people() {
        return cur_people;
    }

    public void setCur_people(int cur_people) {
        this.cur_people = cur_people;
    }

    public String getMeeting_time() {
        return meeting_time;
    }

    public void setMeeting_time(String meeting_time) {
        this.meeting_time = meeting_time;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getGender(){ return gender;}

    public void setGender(int gender){ this.gender = gender;}
}
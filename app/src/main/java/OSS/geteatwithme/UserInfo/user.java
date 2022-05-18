package OSS.geteatwithme.UserInfo;

import android.app.Application;

public class user  extends Application {
    private String user_ID;
    private double longitude;
    private double latitude;

    public String getUserID(){return user_ID;}
    public void setUserID(String id){user_ID = id;}
    public double getLongitude(){return longitude;}
    public void setLogitude(double longitude){ this.longitude = longitude;}
    public double getLatitude(){return latitude;}
    public void setLatitude(double latitude){ this.latitude = latitude;}

}

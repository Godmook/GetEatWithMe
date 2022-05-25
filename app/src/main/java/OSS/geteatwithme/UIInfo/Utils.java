package OSS.geteatwithme.UIInfo;

import android.os.Build;

import androidx.core.content.ContextCompat;

import OSS.geteatwithme.EditPostActivity;
import OSS.geteatwithme.EditUserProfileActivity;
import OSS.geteatwithme.MainActivity;
import OSS.geteatwithme.MyPageActivity;
import OSS.geteatwithme.MyPostListActivity;
import OSS.geteatwithme.PostInfo.Post;
import OSS.geteatwithme.PostingActivity;
import OSS.geteatwithme.R;
import OSS.geteatwithme.SearchMeetingPlaceActivity;
import OSS.geteatwithme.SearchRestaurantActivity;
import OSS.geteatwithme.ShowPostActivity;
import OSS.geteatwithme.SignInActivity;
import OSS.geteatwithme.SignUpActivity;

public class Utils {
    public enum StatusBarColorType {
        MAIN_ORANGE_STATUS_BAR(R.color.main_orange);

        private int backgroundColorId;

        StatusBarColorType(int backgroundColorId){
            this.backgroundColorId = backgroundColorId;
        }

        public int getBackgroundColorId() {
            return backgroundColorId;
        }
    }

    public static void setStatusBarColor(EditPostActivity activity, StatusBarColorType colorType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, colorType.getBackgroundColorId()));
        }
    }
    public static void setStatusBarColor(EditUserProfileActivity activity, StatusBarColorType colorType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, colorType.getBackgroundColorId()));
        }
    }
    public static void setStatusBarColor(MainActivity activity, StatusBarColorType colorType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, colorType.getBackgroundColorId()));
        }
    }
    public static void setStatusBarColor(MyPageActivity activity, StatusBarColorType colorType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, colorType.getBackgroundColorId()));
        }
    }
    public static void setStatusBarColor(MyPostListActivity activity, StatusBarColorType colorType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, colorType.getBackgroundColorId()));
        }
    }
    public static void setStatusBarColor(PostingActivity activity, StatusBarColorType colorType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, colorType.getBackgroundColorId()));
        }
    }
    public static void setStatusBarColor(SearchRestaurantActivity activity, StatusBarColorType colorType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, colorType.getBackgroundColorId()));
        }
    }
    public static void setStatusBarColor(SearchMeetingPlaceActivity activity, StatusBarColorType colorType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, colorType.getBackgroundColorId()));
        }
    }
    public static void setStatusBarColor(ShowPostActivity activity, StatusBarColorType colorType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, colorType.getBackgroundColorId()));
        }
    }
    public static void setStatusBarColor(SignInActivity activity, StatusBarColorType colorType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, colorType.getBackgroundColorId()));
        }
    }
    public static void setStatusBarColor(SignUpActivity activity, StatusBarColorType colorType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, colorType.getBackgroundColorId()));
        }
    }
}
package OSS.geteatwithme;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import OSS.geteatwithme.Connection.RetrofitService;
import OSS.geteatwithme.Connection.UserProfileAPI;
import OSS.geteatwithme.PostInfo.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("Firebase", "FirebaseInstanceIDService : " + s);
        /*
        RetrofitService retrofitService = new RetrofitService();
        UserProfileAPI userProfileAPI = retrofitService.getRetrofit().create(UserProfileAPI.class);
        try {
            userProfileAPI.UpdateToken(ID, s)
                    .enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {

                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {

                        }
                    });
            //if(ID!=NULL) -> 아직 구현 X 아이디 있을때 NewToken 값 들어오면 처리하도록 만듬
        }catch(Exception e){

        }

         */
    }


    /**
     * 메세지를 받았을 경우 그 메세지에 대하여 구현하는 부분입니다.
     * **/
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage != null && remoteMessage.getData().size() > 0) {
            sendNotification(remoteMessage);
        }
    }

    /**
     * remoteMessage 메세지 안애 getData와 getNotification이 있습니다.
     *
     * **/
    private void sendNotification(RemoteMessage remoteMessage) {
        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");
    }
}

package OSS.geteatwithme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import OSS.geteatwithme.Connection.RetrofitService;
import OSS.geteatwithme.Connection.UserProfileAPI;
import OSS.geteatwithme.PostInfo.Post;
import OSS.geteatwithme.UserInfo.UserProfile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageActivity extends AppCompatActivity {
    private class GetTask extends AsyncTask<Call,Void,UserProfile> {
        @Override
        protected UserProfile doInBackground(Call... calls) {
            try{
                Call<UserProfile> call=calls[0];
                Response<UserProfile> response=call.execute();
                return response.body();
            }catch(IOException e){

            }
            return null;
        }
    }
    UserProfile user_info = new UserProfile();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        TextView nickname, id, name, age, gender;
        //Button editInfo, view_myPost, logout;

        nickname = (TextView)findViewById(R.id.myPage_editNickname);
        id = (TextView)findViewById(R.id.myPage_editId);
        name = (TextView)findViewById(R.id.myPage_editName);
        age = (TextView)findViewById(R.id.myPage_editAge);
        gender = (TextView)findViewById(R.id.myPage_editGender);
        //editInfo = (Button)findViewById(R.id.btn_editInfo);
        //view_myPost = (Button)findViewById(R.id.btn_myPostView);
        //logout = (Button)findViewById(R.id.btn_logout);
        RetrofitService retrofitService = new RetrofitService();
        UserProfileAPI userProfileAPI = retrofitService.getRetrofit().create(UserProfileAPI.class);

        Call<UserProfile> calls= userProfileAPI.getUserProfile("yejinbb");
        try {
            user_info=new GetTask().execute(calls).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int tmp_int;
        String tmp_String;
        tmp_String = user_info.getNickname();
        nickname.setText(tmp_String);
        tmp_String = user_info.getId();
        id.setText(tmp_String);
        tmp_String = user_info.getName();
        name.setText(tmp_String);
        tmp_int=user_info.getGender();
        if(tmp_int == 0){tmp_String = "남성";}
        else {tmp_String="여성";}
        gender.setText(tmp_String);
        tmp_int=user_info.getAge();
        tmp_String=Integer.toString(tmp_int);
        age.setText(tmp_String);




    }
}
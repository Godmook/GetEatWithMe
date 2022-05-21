package OSS.geteatwithme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import OSS.geteatwithme.Connection.RetrofitService;
import OSS.geteatwithme.Connection.UserProfileAPI;
import OSS.geteatwithme.UserInfo.UserProfile;
import retrofit2.Call;
import retrofit2.Response;

public class EditUserProfileActivity extends AppCompatActivity {

    TextView back, name, id, gender;
    EditText pw,pw2,age,nickname;
    Button pwcheck, submit, idcheck, nicknamecheck;
    String token;
    boolean nickname_check_result=false;
    UserProfile user_info = new UserProfile();
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        //뒤로 가기 버튼
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed() );
        /*back 버튼 눌리면 로그인 화면으로 돌아감*/

        //기입 항목
        name = findViewById(R.id.edit_profile_Name);
        id=findViewById(R.id.edit_profile_ID);
        pw=findViewById(R.id.edit_profile_PW);
        pw2=findViewById(R.id.edit_profile_PW2);
        age=findViewById(R.id.edit_profile_age);
        nickname=findViewById(R.id.edit_profile_Nickname);
        gender=findViewById(R.id.edit_profile_gender_text);
        RetrofitService retrofitService = new RetrofitService();
        UserProfileAPI userProfileAPI = retrofitService.getRetrofit().create(UserProfileAPI.class);

        SharedPreferences auto = getSharedPreferences("LoginSource", Activity.MODE_PRIVATE);
        String user_id=auto.getString("ID",null);
        Call<UserProfile> calls= userProfileAPI.getUserProfile(user_id);
        try {
            user_info=new GetTask().execute(calls).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        name.setText(user_info.getName());
        id.setText(user_info.getId());
        age.setText(user_info.getAge());
        nickname.setText(user_info.getNickname());
        gender.setText(user_info.getGender());
    }
}
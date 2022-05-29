package OSS.geteatwithme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import OSS.geteatwithme.Connection.RetrofitService;
import OSS.geteatwithme.Connection.UserProfileAPI;
import OSS.geteatwithme.UIInfo.Utils;
import OSS.geteatwithme.UserInfo.UserProfile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserProfileActivity extends AppCompatActivity {
    Boolean check_editing=true;
    TextView back, name, id, gender, nickname;
    EditText pw,pw2,age;
    Button pwcheck, submit;
    String token;
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
    private class NickCheckTask extends AsyncTask<Call,Void,Integer> {
        @Override
        protected Integer doInBackground(Call... calls) {
            try{
                Call<Integer> call=calls[0];
                Response<Integer> response=call.execute();
                return response.body();
            }catch(IOException e){

            }
            return null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        check_editing=true;
        setContentView(R.layout.activity_edit_user_profile);
        Utils.setStatusBarColor(this, Utils.StatusBarColorType.MAIN_ORANGE_STATUS_BAR);
        //뒤로 가기 버튼
        back = findViewById(R.id.back);
        //back.setOnClickListener(v -> onBackPressed() );

        //기입 항목
        name = findViewById(R.id.edit_profile_Name);
        id=findViewById(R.id.edit_profile_ID);
        pw=findViewById(R.id.edit_profile_PW);
        pw2=findViewById(R.id.edit_profile_PW2);
        age=findViewById(R.id.edit_profile_age);
        nickname=findViewById(R.id.edit_profile_nickname_text);
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
        age.setText(Integer.toString(user_info.getAge()));
        nickname.setText(user_info.getNickname());
        if(user_info.getGender()==0)gender.setText("남자");
        else gender.setText("여자");
        /*
        수정 안한 정보는 그대로 가지고 가는거죠
         */
        //비밀번호 확인 버튼
        pwcheck = findViewById(R.id.edit_profile_pwcheckbutton);
        pwcheck.setOnClickListener(v -> {
            EditText userPasswordEditText = (EditText) findViewById(R.id.edit_profile_PW);
            String PW_str = userPasswordEditText.getText().toString();
            EditText userPassword2EditText = (EditText) findViewById(R.id.edit_profile_PW2);
            String PW2_str = userPassword2EditText.getText().toString();
            if (PW_str.matches("") || PW2_str.matches("")) {
                Toast.makeText(this, "You did not enter a password", Toast.LENGTH_SHORT).show();
            }
            else {
                if(pw.getText().toString().equals(pw2.getText().toString())){
                    Toast.makeText(EditUserProfileActivity.this, "비밀번호가 일치합니다.", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(EditUserProfileActivity.this, "비밀번호가 다릅니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

        submit=(Button) findViewById(R.id.edit_profile_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_info.setAge(Integer.parseInt(age.getText().toString()));
                if(check_editing){
                    if(pw.getText().toString().matches("")||pw2.getText().toString().matches("")){
                        userProfileAPI.UpdateUserProfileWithoutPw(user_info.getId(),user_info.getAge(),user_info.getNickname())
                                .enqueue(new Callback<Integer>() {
                                    @Override
                                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                                        Toast.makeText(EditUserProfileActivity.this, "수정 성공!", Toast.LENGTH_SHORT).show();
                                        Intent myIntent = new Intent(getApplicationContext(), MyPageActivity.class);
                                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(myIntent);
                                    }

                                    @Override
                                    public void onFailure(Call<Integer> call, Throwable t) {

                                    }
                                });
                    }
                    else{
                        if(pw.getText().toString().equals(pw2.getText().toString())) {
                            user_info.setPassword(pw.getText().toString());
                            userProfileAPI.UpdateUserProfile(user_info.getId(),user_info.getPassword(),user_info.getAge(),user_info.getNickname())
                                    .enqueue(new Callback<Integer>() {
                                        @Override
                                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                                            Toast.makeText(EditUserProfileActivity.this, "수정 성공!", Toast.LENGTH_SHORT).show();
                                            Intent myIntent = new Intent(getApplicationContext(), MyPageActivity.class);
                                            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(myIntent);
                                        }

                                        @Override
                                        public void onFailure(Call<Integer> call, Throwable t) {

                                        }
                                    });
                        }
                    }
                }
            }
        });
    }
}
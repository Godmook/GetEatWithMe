package OSS.geteatwithme;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.io.IOException;

import OSS.geteatwithme.Connection.RetrofitService;
import OSS.geteatwithme.Connection.UserProfileAPI;
import OSS.geteatwithme.UIInfo.Utils;
import OSS.geteatwithme.UserInfo.UserProfile;
import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpActivity extends AppCompatActivity {
    private class LogincheckTask extends AsyncTask<Call,Void,Void>{
        @Override
        protected Void doInBackground(Call... calls) {
            try{
                Call<UserProfile> call=calls[0];
                Response<UserProfile> response=call.execute();
            }catch(IOException e){

            }
            return null;
        }
    }
    private class TokenTask extends AsyncTask<Call,Void,Void>{
        @Override
        protected Void doInBackground(Call... calls) {
            try{
                Call<Integer> call=calls[0];
                Response<Integer> response=call.execute();
            }catch(IOException e){

            }
            return null;
        }
    }
    TextView back;
    EditText name,id,pw,pw2,age,nickname;
    RadioGroup radioGroup;
    Button pwcheck, submit, idcheck, nicknamecheck;
    String token;
    int gender;
    boolean id_check_result=true;
    boolean nickname_check_result=false;
    int id_check_tmp=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Utils.setStatusBarColor(this, Utils.StatusBarColorType.MAIN_ORANGE_STATUS_BAR);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        // Get new FCM registration token
                        token = task.getResult();
                    }
                });
        //?????? ?????? ??????
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed() );
        /*back ?????? ????????? ????????? ???????????? ?????????*/

        //?????? ??????
        name = findViewById(R.id.signName);
        id=findViewById(R.id.signID);
        pw=findViewById(R.id.signPW);
        pw2=findViewById(R.id.signPW2);
        age=findViewById(R.id.signage);
        nickname=findViewById(R.id.signNickname);
        radioGroup = findViewById(R.id.signup_radio_group);
        //??????????????? ??????
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.btn_signup_male:
                        gender=0;
                        break;
                    case R.id.btn_signup_female:
                        gender=1;
                        break;
                }
            }
        });
        //????????? ?????? ?????? ??????
        idcheck=findViewById(R.id.idcheck);
        idcheck.setOnClickListener(v->{
            EditText userIdEditText = (EditText) findViewById(R.id.signID);
            String id_str = userIdEditText.getText().toString();
            if (id_str.matches("")) {
                Toast.makeText(this, "You did not enter a id", Toast.LENGTH_SHORT).show();
            }
            else {
                RetrofitService retrofitService= new RetrofitService();
                UserProfileAPI userProfileAPI= retrofitService.getRetrofit().create(UserProfileAPI.class);
                userProfileAPI.checkPassword(id_str)
                        .enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                id_check_result=true;
                                if(response.body()==1)id_check_result=false;
                                if(id_check_result==true){  // ??????????????? ??????
                                    Toast.makeText(SignUpActivity.this, "?????? ????????? ????????? ?????????.", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(SignUpActivity.this, "?????? ???????????? ????????? ?????????.", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {

                            }
                        });
            }
        });
        //???????????? ?????? ??????
        pwcheck = findViewById(R.id.pwcheckbutton);
        pwcheck.setOnClickListener(v -> {
            EditText userPasswordEditText = (EditText) findViewById(R.id.signPW);
            String PW_str = userPasswordEditText.getText().toString();
            EditText userPassword2EditText = (EditText) findViewById(R.id.signPW2);
            String PW2_str = userPassword2EditText.getText().toString();
            if (PW_str.matches("") || PW2_str.matches("")) {
                Toast.makeText(this, "You did not enter a password", Toast.LENGTH_SHORT).show();
            }
            else {
                if(pw.getText().toString().equals(pw2.getText().toString())){
                    Toast.makeText(SignUpActivity.this, "??????????????? ???????????????.", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(SignUpActivity.this, "??????????????? ????????????.", Toast.LENGTH_LONG).show();
                }
            }
        });
        //????????? ?????? ?????? ??????
        nicknamecheck=findViewById(R.id.nicknamecheck);
        nicknamecheck.setOnClickListener(v->{
            EditText userNicknameEditText = (EditText) findViewById(R.id.signNickname);
            String nickname_str = userNicknameEditText.getText().toString();
            if (nickname_str.matches("") || nickname_str.matches("")) {
                Toast.makeText(this, "You did not enter a nickname", Toast.LENGTH_SHORT).show();
            }
            else {

                RetrofitService retrofitService= new RetrofitService();
                UserProfileAPI userProfileAPI= retrofitService.getRetrofit().create(UserProfileAPI.class);
                userProfileAPI.checkNick(nickname_str)
                        .enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                nickname_check_result=true;
                                if(response.body()>0)nickname_check_result=false;
                                if(nickname_check_result==true){  // ??????????????? ??????
                                    Toast.makeText(SignUpActivity.this, "?????? ????????? ????????? ?????????.", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(SignUpActivity.this, "?????? ???????????? ????????? ?????????.", Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {

                            }
                        });
            }
        });

        //???????????? ?????? ??????
        submit = findViewById(R.id.signupbutton);
        submit.setOnClickListener(v -> {
            // ?????? check ??? false??? ?????? ??? id??????, ???????????? ??????, nickname?????? ?????? ?????? -> ???????????? ?????? ??????
            id_check_result=true;
            nickname_check_result=true;
            // 1) id ????????????
            EditText userIdEditText = (EditText) findViewById(R.id.signID);
            String id_str = userIdEditText.getText().toString();
            if (id_str.matches("")) {
                Toast.makeText(this, "You did not enter a id", Toast.LENGTH_SHORT).show();
            }
            else
            {
                /// ???????????? ????????? ?????? ?????? ??????
                RetrofitService retrofitService= new RetrofitService();
                UserProfileAPI userProfileAPI= retrofitService.getRetrofit().create(UserProfileAPI.class);
                userProfileAPI.checkPassword(id_str)
                        .enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                id_check_result=true;
                                if(response.body()==1)id_check_result=false;
                                if(id_check_result==true){  // ??????????????? ??????
                                    // 2) ???????????? ?????? ??????
                                    EditText userPasswordEditText = (EditText) findViewById(R.id.signPW);
                                    String PW_str = userPasswordEditText.getText().toString();
                                    EditText userPassword2EditText = (EditText) findViewById(R.id.signPW2);
                                    String PW2_str = userPassword2EditText.getText().toString();
                                    if (PW_str.matches("") || PW2_str.matches("")) {
                                        Toast.makeText(SignUpActivity.this, "You did not enter a password", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        if(pw.getText().toString().equals(pw2.getText().toString())){
                                            // 3) ????????? ?????? ??????
                                            EditText userNicknameEditText = (EditText) findViewById(R.id.signNickname);
                                            String nickname_str = userNicknameEditText.getText().toString();
                                            if (nickname_str.matches("") || nickname_str.matches("")) {
                                                Toast.makeText(SignUpActivity.this, "You did not enter a nickname", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                /*
                                                 *   user??? ????????? nickname, ????????? ??????
                                                 *   database??? nickname?????? ?????? ??? ?????? ??? nickname_check_result ??????
                                                 */
                                                userProfileAPI.checkNick(nickname_str)
                                                        .enqueue(new Callback<Integer>() {
                                                            @Override
                                                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                                                nickname_check_result=true;
                                                                if(response.body()==1)nickname_check_result=false;
                                                                if(nickname_check_result==true){  // ??????????????? ??????
                                                                    // 1), 2), 3) ?????? ?????? ??? ?????? ??????.
                                                                    // ????????? ?????? ?????? ??????.
                                                                    userProfileAPI.createPost(id_str,name.getText().toString(),gender,PW_str,Integer.parseInt(age.getText().toString()),nickname_str,token)
                                                                            .enqueue(new Callback<UserProfile>() {
                                                                                @Override
                                                                                public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                                                                                }

                                                                                @Override
                                                                                public void onFailure(Call<UserProfile> call, Throwable t) {

                                                                                }
                                                                            });
                                                                    Toast.makeText(SignUpActivity.this, "????????? ?????????????????????.", Toast.LENGTH_LONG).show();
                                                                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                    startActivity(intent);
                                                                }
                                                                else{
                                                                    Toast.makeText(SignUpActivity.this, "?????? ???????????? ????????? ?????????.", Toast.LENGTH_LONG).show();
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<Integer> call, Throwable t) {

                                                            }
                                                        });

                                            }
                                        }
                                        else{
                                            Toast.makeText(SignUpActivity.this, "??????????????? ????????????.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                                else{
                                    Toast.makeText(SignUpActivity.this, "?????? ???????????? ID ?????????.", Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {

                            }
                        });
                // ???????????? ????????? ?????? ?????? ?????? ???
            }
        });
    }

}


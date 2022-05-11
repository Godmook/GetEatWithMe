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

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import OSS.geteatwithme.Connection.RetrofitService;
import OSS.geteatwithme.Connection.UserProfileAPI;
import OSS.geteatwithme.UserInfo.UserProfile;
import lombok.val;
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
    TextView back;
    EditText name,id,pw,pw2,age,nickname;
    RadioGroup radioGroup;
    Button pwcheck, submit, idcheck, nicknamecheck;
    int gender;
    boolean id_check_result=true;
    boolean nickname_check_result=false;
    int id_check_tmp=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //뒤로 가기 버튼
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed() );
        /*back 버튼 눌리면 로그인 화면으로 돌아감*/

        //기입 항목
        name = findViewById(R.id.signName);
        id=findViewById(R.id.signID);
        pw=findViewById(R.id.signPW);
        pw2=findViewById(R.id.signPW2);
        age=findViewById(R.id.signage);
        nickname=findViewById(R.id.signNickname);
        radioGroup = findViewById(R.id.signup_radio_group);
        //라디오버튼 확인
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
        //아이디 중복 확인 버튼
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
                                if(id_check_result==true){  // 중복아이디 없음
                                    Toast.makeText(SignUpActivity.this, "사용 가능한 아이디 입니다.", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(SignUpActivity.this, "이미 존재하는 아이디 입니다.", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {

                            }
                        });
            }
        });
        //비밀번호 확인 버튼
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
                    pwcheck.setText("일치");
                }
                else{
                    Toast.makeText(SignUpActivity.this, "비밀번호가 다릅니다.", Toast.LENGTH_LONG).show();
                }
            }
        });
        //닉네임 중복 확인 버튼
        nicknamecheck=findViewById(R.id.nicknamecheck);
        nicknamecheck.setOnClickListener(v->{
            EditText userNicknameEditText = (EditText) findViewById(R.id.signNickname);
            String nickname_str = userNicknameEditText.getText().toString();
            if (nickname_str.matches("") || nickname_str.matches("")) {
                Toast.makeText(this, "You did not enter a nickname", Toast.LENGTH_SHORT).show();
            }
            else {
                /*
                 *   user가 입력한 nickname, 서버로 전송
                 *   database에 nickname중복 확인 후 결과 값 nickname_check_result 받기
                 */
                RetrofitService retrofitService= new RetrofitService();
                UserProfileAPI userProfileAPI= retrofitService.getRetrofit().create(UserProfileAPI.class);
                userProfileAPI.checkNick(nickname_str)
                        .enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                nickname_check_result=true;
                                if(response.body()>0)nickname_check_result=false;
                                if(nickname_check_result==true){  // 중복아이디 없음
                                    Toast.makeText(SignUpActivity.this, "사용 가능한 닉네임 입니다.", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(SignUpActivity.this, "이미 존재하는 닉네임 입니다.", Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {

                            }
                        });
            }
        });

        //회원가입 완료 버튼
        submit = findViewById(R.id.signupbutton);
        submit.setOnClickListener(v -> {
            // 모든 check 값 false로 변경 후 id중복, 비밀번호 일치, nickname중복 모두 검사 -> 통과하면 가입 가능
            id_check_result=true;
            nickname_check_result=true;
            // 1) id 중복확인
            EditText userIdEditText = (EditText) findViewById(R.id.signID);
            String id_str = userIdEditText.getText().toString();
            if (id_str.matches("")) {
                Toast.makeText(this, "You did not enter a id", Toast.LENGTH_SHORT).show();
            }
            else
            {
                /// 레트로핏 아이디 중복 확인 작업
                RetrofitService retrofitService= new RetrofitService();
                UserProfileAPI userProfileAPI= retrofitService.getRetrofit().create(UserProfileAPI.class);
                userProfileAPI.checkPassword(id_str)
                        .enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                id_check_result=true;
                                if(response.body()==1)id_check_result=false;
                                if(id_check_result==true){  // 중복아이디 없음
                                    // 2) 비밀번호 일치 확인
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
                                            // 3) 닉네임 일치 확인
                                            EditText userNicknameEditText = (EditText) findViewById(R.id.signNickname);
                                            String nickname_str = userNicknameEditText.getText().toString();
                                            if (nickname_str.matches("") || nickname_str.matches("")) {
                                                Toast.makeText(SignUpActivity.this, "You did not enter a nickname", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                /*
                                                 *   user가 입력한 nickname, 서버로 전송
                                                 *   database에 nickname중복 확인 후 결과 값 nickname_check_result 받기
                                                 */
                                                userProfileAPI.checkNick(nickname_str)
                                                        .enqueue(new Callback<Integer>() {
                                                            @Override
                                                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                                                nickname_check_result=true;
                                                                if(response.body()==1)nickname_check_result=false;
                                                                if(nickname_check_result==true){  // 중복아이디 없음
                                                                    // 1), 2), 3) 모두 충족 시 가입 가능.
                                                                    // 서버에 모든 정보 전달.
                                                                    Call<UserProfile> calls= userProfileAPI.createPost(id_str,name.getText().toString(),gender,PW_str,Integer.parseInt(age.getText().toString()),nickname_str);
                                                                    new LogincheckTask().execute(calls);
                                                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                                                    startActivity(intent);
                                                                    Toast.makeText(SignUpActivity.this, "가입이 완료되었습니다.", Toast.LENGTH_LONG).show();
                                                                }
                                                                else{
                                                                    Toast.makeText(SignUpActivity.this, "이미 존재하는 닉네임 입니다.", Toast.LENGTH_LONG).show();
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<Integer> call, Throwable t) {

                                                            }
                                                        });

                                            }
                                        }
                                        else{
                                            Toast.makeText(SignUpActivity.this, "비밀번호가 다릅니다.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                                else{
                                    Toast.makeText(SignUpActivity.this, "이미 존재하는 ID 입니다.", Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {

                            }
                        });
                // 레트로핏 아이디 중복 확인 완료 후
            }
        });
    }

}


package OSS.geteatwithme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import java.util.List;

import OSS.geteatwithme.Connection.RetrofitService;
import OSS.geteatwithme.Connection.UserProfileAPI;
import OSS.geteatwithme.UserInfo.UserProfile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    boolean login_result = false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();
    }
    private void initializeComponents(){

    }
    // 회원가입 버튼
    public void onSignUpClicked(View v){
        Intent myIntent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(myIntent);
    }

    // 로그인 버튼
    public void onLogInClicked(View v){
        EditText editText_id = (EditText)findViewById(R.id.editTextTextID);
        EditText editText_password = (EditText)findViewById(R.id.editTextTextPassword);
        String id = editText_id.getText().toString();
        String password = editText_password.getText().toString();
        if (id.matches("") || password.matches("")) {
            Toast.makeText(this, "You did not enter a nickname", Toast.LENGTH_SHORT).show();
        }
        else {
            RetrofitService retrofitService = new RetrofitService();
            UserProfileAPI userProfileAPI = retrofitService.getRetrofit().create(UserProfileAPI.class);
            userProfileAPI.checkLogin(id, password)
                    .enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            login_result = true;
                            if (response.body() < 1) login_result = false;
                            //로그인 성공
                            if (login_result == true) {
                                Toast.makeText(getApplicationContext(), "로그인 성공!", Toast.LENGTH_LONG).show();
                                // 수정 필요
                                //Intent myIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                                //startActivity(myIntent);
                            }
                            // 로그인 실패
                            else {
                                Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 올바르지 않습니다.", Toast.LENGTH_LONG).show();
                                editText_id.setText(null);
                                editText_password.setText(null);
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {

                        }
                    });
        }
        // test-

    }
}
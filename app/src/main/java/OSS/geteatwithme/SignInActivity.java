package OSS.geteatwithme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import OSS.geteatwithme.Connection.RetrofitService;
import OSS.geteatwithme.Connection.UserProfileAPI;
import OSS.geteatwithme.UIInfo.Utils;
import OSS.geteatwithme.UserInfo.UserProfile;
import OSS.geteatwithme.UserInfo.user;
import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    boolean login_result = false;
    private static final String TAG="SignInActivity";
    String token;
    UserProfile us;
    private class GetTokenTask extends AsyncTask<Call,Void, UserProfile> {
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
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_main);
        Utils.setStatusBarColor(this, Utils.StatusBarColorType.MAIN_ORANGE_STATUS_BAR);
        // ?????? ??????, ?????? ????????????
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // gps ?????? ??????
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        ((user)getApplication()).setLatitude(location.getLatitude());
        ((user)getApplication()).setLogitude(location.getLongitude());

        // ?????? ?????????
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
        SharedPreferences auto = getSharedPreferences("LoginSource", Activity.MODE_PRIVATE);
        String user_id=auto.getString("ID",null);
        String user_password=auto.getString("Password",null);
        if(user_id!=null&&user_password!=null){
            RetrofitService retrofitService = new RetrofitService();
            UserProfileAPI userProfileAPI = retrofitService.getRetrofit().create(UserProfileAPI.class);
            userProfileAPI.checkLogin(user_id, user_password)
                    .enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if(response.body()>=1){
                                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(myIntent);
                                finish();
                            }
                            else{
                                Toast.makeText(SignInActivity.this,"?????? ????????? ??????!",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {

                        }
                    });
        }
    }

    // ???????????? ??????
    public void onSignUpClicked(View v){
        Intent myIntent = new Intent(getApplicationContext(), SignUpActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
    }

    // ????????? ??????
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
                            //????????? ??????
                            if (login_result == true) {
                                RetrofitService retrofitService = new RetrofitService();
                                UserProfileAPI userProfileAPI = retrofitService.getRetrofit().create(UserProfileAPI.class);
                                Call<UserProfile> calls =userProfileAPI.getUserProfile(id);
                                us=new UserProfile();
                                try {
                                    us=new GetTokenTask().execute(calls).get();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                SharedPreferences auto = getSharedPreferences("LoginSource", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor autoLoginEdit = auto.edit();
                                autoLoginEdit.putString("ID", id);
                                autoLoginEdit.putString("Password", password);
                                autoLoginEdit.putString("Token_id",token);
                                autoLoginEdit.putString("Nickname",us.getNickname());
                                autoLoginEdit.commit();
                                Toast.makeText(getApplicationContext(), "????????? ??????!", Toast.LENGTH_LONG).show();
                                RetrofitService retrofitServices = new RetrofitService();
                                UserProfileAPI uk = retrofitServices.getRetrofit().create(UserProfileAPI.class);
                                uk.UpdateToken(id,token)
                                        .enqueue(new Callback<Integer>() {
                                            @Override
                                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                                FirebaseDatabase.getInstance("https://geteatwithme-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("users").child(us.getNickname()).setValue(us.getToken_id());
                                                ((user)getApplication()).setUserID(id);
                                                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                                                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(myIntent);
                                                finish();
                                            }

                                            @Override
                                            public void onFailure(Call<Integer> call, Throwable t) {

                                            }
                                        });
                                // id ??????
                            }
                            // ????????? ??????
                            else {
                                Toast.makeText(getApplicationContext(), "????????? ?????? ??????????????? ???????????? ????????????.", Toast.LENGTH_LONG).show();
                                editText_id.setText(null);
                                editText_password.setText(null);
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {

                        }
                    });
        }

    }
}
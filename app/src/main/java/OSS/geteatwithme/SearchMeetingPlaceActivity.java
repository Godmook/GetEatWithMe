package OSS.geteatwithme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import OSS.geteatwithme.Connection.KaKaofitService;
import OSS.geteatwithme.Connection.ResultSearchKeyword;
import OSS.geteatwithme.Connection.UserProfileAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMeetingPlaceActivity extends AppCompatActivity {

    EditText search_word;
    Button search_start, search_cancel, search_end;
    String tmp_String;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_meeting_place);

        search_word=(EditText) findViewById(R.id.search_meeting_place_text);
        search_start=(Button)findViewById(R.id.btn_search_start2);
        search_cancel=(Button)findViewById(R.id.btn_search_cancel2);
        search_end=(Button)findViewById(R.id.btn_search_end2);
        search_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KaKaofitService kaKaofitService=new KaKaofitService();
                UserProfileAPI userProfileAPI= kaKaofitService.getKakaoFit().create(UserProfileAPI.class);
                userProfileAPI.GetSearchKeyword("KakaoAK f9e3926b054ba1f5d08a2672f49e8869","세종대학교","37.55053128987321","127.07343336371858","1000")
                        .enqueue(new Callback<ResultSearchKeyword>() {
                            @Override
                            public void onResponse(Call<ResultSearchKeyword> call, Response<ResultSearchKeyword> response) {
                                if(response.isSuccessful()){
                                    if(response.body()!=null){
                                        for(int i=0;i<response.body().documentList.size();i++){
                                            tmp_String=response.body().documentList.get(i).getPlace_name();
                                            Toast.makeText(SearchMeetingPlaceActivity.this,response.body().documentList.get(i).getPlace_name(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResultSearchKeyword> call, Throwable t) {

                            }
                        });
            }
        });
        search_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        search_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = tmp_String;
                Intent intent = new Intent();//startActivity()를 할것이 아니므로 그냥 빈 인텐트로 만듦
                intent.putExtra("my_data",data);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
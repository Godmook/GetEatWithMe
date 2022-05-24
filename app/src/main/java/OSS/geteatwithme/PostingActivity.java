package OSS.geteatwithme;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import OSS.geteatwithme.Connection.RetrofitService;
import OSS.geteatwithme.Connection.UserProfileAPI;
import OSS.geteatwithme.PostInfo.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostingActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> resultLauncher;
    ActivityResultLauncher<Intent> resultLauncher2;
    String []number_of_people1 = {"2", "3", "4", "5", "6", "7", "8"};
    String []number_of_people2 = {"1", "2", "3", "4", "5", "6", "7"};
    TextView restaurant_view, meeting_place_view, restaurant_address_view, meeting_place_address_view;
    EditText editPosting;
    Button cancel, post;
    Switch gender_visible;
    TextView gender_open_text;
    Button Restaurant_search, meeting_place_search;
    int sec;
    // 라디오 버튼(카테고리 선택)
    RadioButton[] radioButtons = new RadioButton[7];
    void setAllRadioButtonOff(){
        for(int i=0; i< 7; i++)
            radioButtons[i].setChecked(false);
    }

    // 캘린더
    Calendar myCalendar = Calendar.getInstance();
    Calendar minDate = Calendar.getInstance();
    Calendar maxDate = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";    // 출력형식   2021/07/26
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText et_date = (EditText) findViewById(R.id.edit_Date);
        et_date.setText(sdf.format(myCalendar.getTime()));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        Post InputPost=new Post();
            /*
        test
        //아이디 값을 전역적으로 가지고 오는 변수가 존재하지 않아서 우선은 id 값은 보류
        */
        SharedPreferences auto = getSharedPreferences("LoginSource", Activity.MODE_PRIVATE);
        InputPost.setId(auto.getString("ID",null));
        // 모일 인원 및 모인 인원 spinner 설정
        Spinner spinner_1 = findViewById(R.id.edit_max_people_spinner);
        Spinner spinner_2 = findViewById(R.id.edit_cur_people_spinner);
        //모일 인원 spinner 설정
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, number_of_people1);
        adapter1.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner_1.setAdapter(adapter1);
        spinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                InputPost.setMax_people(Integer.parseInt(number_of_people1[position]));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        // 모인 인원 spinner 설정
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, number_of_people2);
        adapter2.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner_2.setAdapter(adapter2);
        spinner_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                InputPost.setCur_people(Integer.parseInt(number_of_people2[position]));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        // 카테고리 선택
        radioButtons[0] = (RadioButton) findViewById(R.id.radioButton_0);
        radioButtons[1] = (RadioButton) findViewById(R.id.radioButton_1);
        radioButtons[2] = (RadioButton) findViewById(R.id.radioButton_2);
        radioButtons[3] = (RadioButton) findViewById(R.id.radioButton_3);
        radioButtons[4] = (RadioButton) findViewById(R.id.radioButton_4);
        radioButtons[5] = (RadioButton) findViewById(R.id.radioButton_5);
        radioButtons[6] = (RadioButton) findViewById(R.id.radioButton_6);
        // 한식
        radioButtons[0].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[0].setChecked(true);
                InputPost.setCategory(0);
            }
        });
        // 중식
        radioButtons[1].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[1].setChecked(true);
                InputPost.setCategory(1);
            }
        });
        // 일식
        radioButtons[2].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[2].setChecked(true);
                InputPost.setCategory(2);
            }
        });
        // 양식
        radioButtons[3].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[3].setChecked(true);
                InputPost.setCategory(3);
            }
        });
        // 분식
        radioButtons[4].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[4].setChecked(true);
                InputPost.setCategory(4);
            }
        });
        // 아시안
        radioButtons[5].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[5].setChecked(true);
                InputPost.setCategory(5);
            }
        });
        // 패스트 푸드
        radioButtons[6].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[6].setChecked(true);
                InputPost.setCategory(6);
            }
        });
        // 날짜 설정
        EditText et_Date = (EditText) findViewById(R.id.edit_Date);
        et_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PostingActivity.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // 시간 설정
        final EditText et_time = (EditText) findViewById(R.id.edit_Time);
        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);                //한국시간 : +9
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(PostingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        // EditText에 출력할 형식 지정
                        String tmp= selectedHour + ":" + selectedMinute;
                        sec=(selectedHour*3600)+(selectedMinute*60);
                        et_time.setText(tmp);
                    }
                }, hour, minute, true); // true의 경우 24시간 형식의 TimePicker 출현
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        // posting text 관리
        editPosting = (EditText)findViewById(R.id.edit_editPostingText);

        // button 관리
        cancel = (Button)findViewById(R.id.btn_edit_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);
            }
        });
        post = (Button)findViewById(R.id.btn_edit_post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputPost.setContents(editPosting.getText().toString());
                InputPost.setMeeting_date(et_Date.getText().toString());
                InputPost.setMeeting_time(et_time.getText().toString());

                if(InputPost.getMax_people() <= InputPost.getCur_people())
                {
                    Toast.makeText(getApplicationContext(), "모인인원의 수는 모일 인원의 수보다 작아야 합니다.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    // 정보 서버에 posting
                    RetrofitService retrofitService = new RetrofitService();
                    UserProfileAPI userProfileAPI = retrofitService.getRetrofit().create(UserProfileAPI.class);
                    userProfileAPI.PutUserPost(
                            InputPost.getId(),
                            InputPost.getRestaurant(),
                            InputPost.getMeeting_place(),
                            InputPost.getCategory(),
                            InputPost.getMax_people(),
                            InputPost.getCur_people(),
                            InputPost.getMeeting_date(),
                            InputPost.getMeeting_time(),
                            InputPost.getContents(),
                            InputPost.getLongitude(),
                            InputPost.getLatitude(),
                            InputPost.getMeet_x(),
                            InputPost.getMeet_y(),
                            InputPost.getRestaurant_id(),
                            InputPost.getVisible()
                    )
                            .enqueue(new Callback<Integer>() {
                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                    Toast.makeText(PostingActivity.this,"성공!",Toast.LENGTH_SHORT).show();
                                    Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(myIntent);
                                }

                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {
                                    Toast.makeText(PostingActivity.this,"실패!",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        resultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        InputPost.setRestaurant(data.getStringExtra("place"));
                        restaurant_view.setText(data.getStringExtra("place"));
                        InputPost.setLongitude(data.getDoubleExtra("placeX", 0));
                        InputPost.setLatitude(data.getDoubleExtra("placeY", 0));
                        restaurant_address_view.setText(data.getStringExtra("place_address"));
                        InputPost.setRestaurant_id(data.getIntExtra("place_id",0));
                    }
                });
        resultLauncher2=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        InputPost.setMeeting_place(data.getStringExtra("place"));
                        InputPost.setMeet_x(data.getDoubleExtra("placeX", 0));
                        InputPost.setMeet_y(data.getDoubleExtra("placeY", 0));
                        meeting_place_view.setText(data.getStringExtra("place"));
                        meeting_place_address_view.setText(data.getStringExtra("place_address"));
                    }
                });

        restaurant_view=(TextView)findViewById(R.id.edit_restaurant_view);
        meeting_place_view=(TextView)findViewById(R.id.edit_meeting_place_view);
        restaurant_address_view=(TextView)findViewById(R.id.edit_restaurant_address_view);
        meeting_place_address_view=(TextView)findViewById(R.id.edit_meeting_place_address_view);

        // 음식점 선택
        Restaurant_search=(Button)findViewById(R.id.restaurant_find_button);
        Restaurant_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), SearchRestaurantActivity.class);
                resultLauncher.launch(myIntent);
            }
        });

        // 만날 장소 선택
        meeting_place_search=(Button)findViewById(R.id.meeting_place_find_button);
        meeting_place_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), SearchRestaurantActivity.class);
                resultLauncher2.launch(myIntent);
            }
        });

        gender_visible=(Switch) findViewById(R.id.edit_sch_gender_visible);
        gender_open_text=(TextView)findViewById(R.id.edit_text_gender_visible);
        gender_visible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    gender_open_text.setText("성별을 공개합니다");
                    InputPost.setVisible(1);
                }
                else {
                    gender_open_text.setText("성별을 비공개합니다");
                    InputPost.setVisible(0);
                }
            }
        });
    }

}
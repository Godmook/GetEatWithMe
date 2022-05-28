package OSS.geteatwithme;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import OSS.geteatwithme.Connection.RetrofitService;
import OSS.geteatwithme.Connection.UserProfileAPI;
import OSS.geteatwithme.PostInfo.Post;
import OSS.geteatwithme.UIInfo.Utils;
import OSS.geteatwithme.UserInfo.UserProfile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPostActivity extends AppCompatActivity {
    private class GetPostTask extends AsyncTask<Call,Void, Post> {
        @Override
        protected Post doInBackground(Call... calls) {
            try{
                Call<Post> call=calls[0];
                Response<Post> response=call.execute();
                return response.body();
            }catch(IOException e){

            }
            return null;
        }
    }
    ActivityResultLauncher<Intent> resultLauncher;
    ActivityResultLauncher<Intent> resultLauncher2;
    String []number_of_people1 = {"2", "3", "4", "5", "6", "7", "8"};
    String []number_of_people2 = {"1", "2", "3", "4", "5", "6", "7"};
    EditText editPosting;
    Button cancel, delete, edit;
    Switch gender_visible;
    TextView gender_open_text;
    TextView restaurant_view, meetingplace_view;
    DatePickerDialog datePickerDialog;
    LinearLayout lay1, lay2;
    int POST_ID;
    Post EditPost=new Post();
    // 라디오 버튼(카테고리 선택)
    RadioButton[] radioButtons = new RadioButton[7];
    void setAllRadioButtonOff(){
        for(int i=0; i< 7; i++)
            radioButtons[i].setChecked(false);
    }

    // 캘린더
    Calendar myCalendar = Calendar.getInstance();
    Calendar minDate=Calendar.getInstance();
    Calendar maxDate=Calendar.getInstance();

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";    // 출력형식   2021/07/26
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        EditText et_date = (EditText) findViewById(R.id.edit_Date);
        et_date.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        Intent secondIntent=getIntent();
        POST_ID = secondIntent.getIntExtra("postID", 0);

        // post 가져오기
        RetrofitService retrofitService = new RetrofitService();
        UserProfileAPI userProfileAPI = retrofitService.getRetrofit().create(UserProfileAPI.class);
        Call<Post>call=userProfileAPI.getPostByPost_id(POST_ID);
        try {
            EditPost =new GetPostTask().execute(call).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //post 가져오기
        String tmp_String;
        // 모일 인원 및 모인 인원 spinner 설정
        Spinner spinner_1 = findViewById(R.id.edit_max_people_spinner);
        Spinner spinner_2 = findViewById(R.id.edit_cur_people_spinner);
        spinner_1.setSelection(EditPost.getMax_people()-2);
        spinner_2.setSelection(EditPost.getCur_people()-1);
        //모일 인원 spinner 설정
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, number_of_people1);
        adapter1.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner_1.setAdapter(adapter1);
        spinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EditPost.setMax_people(Integer.parseInt(number_of_people1[position]));
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
                EditPost.setCur_people(Integer.parseInt(number_of_people2[position]));
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

        // posting에서 선택된 category check
        radioButtons[EditPost.getCategory()].setChecked(true);

        // 장소 setting
        restaurant_view=(TextView)findViewById(R.id.edit_restaurant_view);
        meetingplace_view=(TextView)findViewById(R.id.edit_meeting_place_view);
        restaurant_view.setText(EditPost.getRestaurant());
        meetingplace_view.setText(EditPost.getMeeting_place());

        resultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        EditPost.setRestaurant(data.getStringExtra("place"));
                        restaurant_view.setText(data.getStringExtra("place"));
                        EditPost.setLongitude(data.getDoubleExtra("placeX", 0));
                        EditPost.setLatitude(data.getDoubleExtra("placeY", 0));
                        EditPost.setRestaurant_id(data.getIntExtra("place_id",0));
                    }
                });
        resultLauncher2=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        EditPost.setMeeting_place(data.getStringExtra("place"));
                        EditPost.setMeet_x(data.getDoubleExtra("placeX", 0));
                        EditPost.setMeet_y(data.getDoubleExtra("placeY", 0));
                        meetingplace_view.setText(data.getStringExtra("place"));
                    }
                });


        editPosting=findViewById(R.id.edit_editPostingText);
        editPosting.setText(EditPost.getContents());

        lay1=(LinearLayout)findViewById(R.id.layout1_1);
        lay2=(LinearLayout)findViewById(R.id.layout2_2);
        // 음식점 선택
        lay1.setOnClickListener(v -> {
            Intent myIntent = new Intent(EditPostActivity.this, SearchRestaurantActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            resultLauncher.launch(myIntent);
        });
        // 만날 장소 선택
        lay2.setOnClickListener(v -> {
            Intent myIntent = new Intent(EditPostActivity.this, SearchRestaurantActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            resultLauncher2.launch(myIntent);
        });


        // 한식
        radioButtons[0].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[0].setChecked(true);
                EditPost.setCategory(0);
            }
        });
        // 중식
        radioButtons[1].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[1].setChecked(true);
                EditPost.setCategory(1);
            }
        });
        // 일식
        radioButtons[2].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[2].setChecked(true);
                EditPost.setCategory(2);
            }
        });
        // 양식
        radioButtons[3].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[3].setChecked(true);
                EditPost.setCategory(3);
            }
        });
        // 분식
        radioButtons[4].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[4].setChecked(true);
                EditPost.setCategory(4);
            }
        });
        // 아시안
        radioButtons[5].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[5].setChecked(true);
                EditPost.setCategory(5);
            }
        });
        // 패스트 푸드
        radioButtons[6].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[6].setChecked(true);
                EditPost.setCategory(6);
            }
        });

        // 날짜 설정
        EditText et_Date = (EditText) findViewById(R.id.edit_Date);
        et_Date.setText(EditPost.getMeeting_date());
        et_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog=new DatePickerDialog(
                        EditPostActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, month);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                updateLabel();
                            }
                        },
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                );
                minDate.set(Calendar.YEAR, myCalendar.get(Calendar.YEAR));
                minDate.set(Calendar.MONTH,  myCalendar.get(Calendar.MONTH));
                minDate.set(Calendar.DAY_OF_MONTH, myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(minDate.getTime().getTime());
                maxDate.set(Calendar.YEAR, myCalendar.get(Calendar.YEAR));
                maxDate.set(Calendar.MONTH, myCalendar.get(Calendar.MONTH));
                maxDate.set(Calendar.DAY_OF_MONTH, myCalendar.get(Calendar.DAY_OF_MONTH)+7);
                datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        // 시간 설정
        EditText et_time = (EditText) findViewById(R.id.edit_Time);
        et_time.setText(EditPost.getMeeting_time());
        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);                //한국시간 : +9
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditPostActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String state = "AM";
                        // 선택한 시간이 12를 넘을경우 "PM"으로 변경 및 -12시간하여 출력 (ex : PM 6시 30분)
                        if (selectedHour > 12) {
                            selectedHour -= 12;
                            state = "PM";
                        }
                        // EditText에 출력할 형식 지정
                        String str="";
                        if(selectedMinute<10)str="0";
                        String tmp= selectedHour + ":" + str+selectedMinute;
                        int sec=(3600*selectedHour)+(60*selectedMinute);
                        EditPost.setSec(sec);
                        et_time.setText(tmp);
                    }
                }, hour, minute, false); // true의 경우 24시간 형식의 TimePicker 출현
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        // posting text 관리
        editPosting = (EditText)findViewById(R.id.edit_editPostingText);
        // 기존 posting 내용 넣기
        editPosting.setText(EditPost.getContents());

        // 성별 공개 비공개 설정
        gender_visible=(Switch) findViewById(R.id.edit_sch_gender_visible);
        gender_open_text=(TextView)findViewById(R.id.edit_text_gender_visible);
        if(EditPost.getVisible()==1)
        {
            gender_visible.setChecked(true);
            gender_open_text.setText("성별을 공개합니다");
        }
        else
        {
            gender_visible.setChecked(false);
            gender_open_text.setText("성별을 비공개합니다");
        }

        gender_visible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    gender_open_text.setText("성별을 공개합니다");
                else
                    gender_open_text.setText("성별을 비공개합니다");
            }
        });

        // button 관리
        delete=(Button)findViewById(R.id.btn_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 삭제 후
                finish();
            }
        });
        cancel = (Button)findViewById(R.id.btn_edit_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edit = (Button)findViewById(R.id.btn_edit_post);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPost.setContents(editPosting.getText().toString());
                EditPost.setMeeting_date(et_Date.getText().toString());
                EditPost.setMeeting_time(et_time.getText().toString());
                if(EditPost.getMax_people() <= EditPost.getCur_people())
                {
                    Toast.makeText(getApplicationContext(), "모인인원의 수는 모일 인원의 수보다 작아야 합니다.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    // 정보 서버에 posting
                    RetrofitService retrofitService = new RetrofitService();
                    UserProfileAPI userProfileAPI = retrofitService.getRetrofit().create(UserProfileAPI.class);
                    userProfileAPI.PutUserPost(
                            EditPost.getId(),
                            EditPost.getRestaurant(),
                            EditPost.getMeeting_place(),
                            EditPost.getCategory(),
                            EditPost.getMax_people(),
                            EditPost.getCur_people(),
                            EditPost.getMeeting_date(),
                            EditPost.getMeeting_time(),
                            EditPost.getContents(),
                            EditPost.getLongitude(),
                            EditPost.getLatitude(),
                            EditPost.getMeet_x(),
                            EditPost.getMeet_y(),
                            EditPost.getRestaurant_id(),
                            EditPost.getVisible(),
                            EditPost.getSec()
                    )
                            .enqueue(new Callback<Integer>() {
                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                    Toast.makeText(EditPostActivity.this,"성공!",Toast.LENGTH_SHORT).show();
                                    Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(myIntent);
                                }

                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {
                                    Toast.makeText(EditPostActivity.this,"실패!",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }
}
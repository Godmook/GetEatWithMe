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

import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
    EditText editPosting;
    Button cancel, delete, edit;
    Switch gender_visible;
    TextView gender_open_text, cur_people_text;
    TextView restaurant_view, meetingplace_view;
    DatePickerDialog datePickerDialog;
    LinearLayout lay1, lay2;
    int POST_ID;
    Post EditPost=new Post();
    // ????????? ??????(???????????? ??????)
    RadioButton[] radioButtons = new RadioButton[7];
    void setAllRadioButtonOff(){
        for(int i=0; i< 7; i++)
            radioButtons[i].setChecked(false);
    }

    // ?????????
    Calendar myCalendar = Calendar.getInstance();
    Calendar minDate=Calendar.getInstance();
    Calendar maxDate=Calendar.getInstance();

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";    // ????????????   2021/07/26
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        EditText et_date = (EditText) findViewById(R.id.edit_Date);
        et_date.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        Utils.setStatusBarColor(this, Utils.StatusBarColorType.MAIN_ORANGE_STATUS_BAR);
        Intent secondIntent=getIntent();
        POST_ID = secondIntent.getIntExtra("postID", 0);

        // post ????????????
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
        //post ????????????
        String tmp_String;
        // ?????? ?????? ??? ?????? ?????? spinner ??????
        Spinner spinner_1 = findViewById(R.id.edit_max_people_spinner);
        cur_people_text=(TextView)findViewById(R.id.edit_cur_people_spinner_text);
        //?????? ?????? spinner ??????
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, number_of_people1);
        adapter1.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner_1.setAdapter(adapter1);
        spinner_1.setSelection(EditPost.getMax_people()-2);
        spinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EditPost.setMax_people(Integer.parseInt(number_of_people1[position]));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // ?????? ?????? ??????
        cur_people_text.setText(Integer.toString(EditPost.getCur_people()));


        // ???????????? ??????
        radioButtons[0] = (RadioButton) findViewById(R.id.radioButton_0);
        radioButtons[1] = (RadioButton) findViewById(R.id.radioButton_1);
        radioButtons[2] = (RadioButton) findViewById(R.id.radioButton_2);
        radioButtons[3] = (RadioButton) findViewById(R.id.radioButton_3);
        radioButtons[4] = (RadioButton) findViewById(R.id.radioButton_4);
        radioButtons[5] = (RadioButton) findViewById(R.id.radioButton_5);
        radioButtons[6] = (RadioButton) findViewById(R.id.radioButton_6);

        // posting?????? ????????? category check
        radioButtons[EditPost.getCategory()].setChecked(true);

        // ?????? setting
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
        // ????????? ??????
        lay1.setOnClickListener(v -> {
            Intent myIntent = new Intent(EditPostActivity.this, SearchRestaurantActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            resultLauncher.launch(myIntent);
        });
        // ?????? ?????? ??????
        lay2.setOnClickListener(v -> {
            Intent myIntent = new Intent(EditPostActivity.this, SearchRestaurantActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            resultLauncher2.launch(myIntent);
        });


        // ??????
        radioButtons[0].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[0].setChecked(true);
                EditPost.setCategory(0);
            }
        });
        // ??????
        radioButtons[1].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[1].setChecked(true);
                EditPost.setCategory(1);
            }
        });
        // ??????
        radioButtons[2].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[2].setChecked(true);
                EditPost.setCategory(2);
            }
        });
        // ??????
        radioButtons[3].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[3].setChecked(true);
                EditPost.setCategory(3);
            }
        });
        // ??????
        radioButtons[4].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[4].setChecked(true);
                EditPost.setCategory(4);
            }
        });
        // ?????????
        radioButtons[5].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[5].setChecked(true);
                EditPost.setCategory(5);
            }
        });
        // ????????? ??????
        radioButtons[6].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[6].setChecked(true);
                EditPost.setCategory(6);
            }
        });

        // ?????? ??????
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

        // ?????? ??????
        EditText et_time = (EditText) findViewById(R.id.edit_Time);
        et_time.setText(EditPost.getMeeting_time());
        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);                //???????????? : +9
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditPostActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String state = "AM";
                        // ????????? ????????? 12??? ???????????? "PM"?????? ?????? ??? -12???????????? ?????? (ex : PM 6??? 30???)
                        if (selectedHour > 12) {
                            selectedHour -= 12;
                            state = "PM";
                        }
                        // EditText??? ????????? ?????? ??????
                        String str="";
                        if(selectedMinute<10)str="0";
                        String tmp= selectedHour + ":" + str+selectedMinute;
                        int sec=(3600*selectedHour)+(60*selectedMinute);
                        EditPost.setSec(sec);
                        et_time.setText(tmp);
                    }
                }, hour, minute, false); // true??? ?????? 24?????? ????????? TimePicker ??????
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        // posting text ??????
        editPosting = (EditText)findViewById(R.id.edit_editPostingText);
        // ?????? posting ?????? ??????
        editPosting.setText(EditPost.getContents());

        // ?????? ?????? ????????? ??????
        gender_visible=(Switch) findViewById(R.id.edit_sch_gender_visible);
        gender_open_text=(TextView)findViewById(R.id.edit_text_gender_visible);
        if(EditPost.getVisible()==1)
        {
            gender_visible.setChecked(true);
            gender_open_text.setText("????????? ???????????????");
        }
        else
        {
            gender_visible.setChecked(false);
            gender_open_text.setText("????????? ??????????????????");
        }

        gender_visible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    gender_open_text.setText("????????? ???????????????");
                else
                    gender_open_text.setText("????????? ??????????????????");
            }
        });

        // button ??????
        delete=(Button)findViewById(R.id.btn_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userProfileAPI.DeletePostData(POST_ID)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                // ?????? ???
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
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
                    Toast.makeText(getApplicationContext(), "??????????????? ?????? ?????? ????????? ????????? ????????? ?????????.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    // ?????? ????????? posting
                    RetrofitService retrofitService = new RetrofitService();
                    UserProfileAPI userProfileAPI = retrofitService.getRetrofit().create(UserProfileAPI.class);
                    userProfileAPI.UpdatePostData(
                            EditPost.getPostID(),
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
                                    Toast.makeText(EditPostActivity.this,"?????? ??????!",Toast.LENGTH_SHORT).show();
                                    Map<String,String> map= new HashMap<>();
                                    map.put("Can Use Chat","true");
                                    map.put("Restaurant_name",EditPost.getRestaurant());
                                    map.put("Meeting_date",EditPost.getMeeting_date());
                                    String room_number="Room"+POST_ID;
                                    FirebaseDatabase.getInstance("https://geteatwithme-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("chatrooms").child(room_number).child("Chatting").setValue(map);
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {

                                }
                            });
                }
            }
        });

    }
}
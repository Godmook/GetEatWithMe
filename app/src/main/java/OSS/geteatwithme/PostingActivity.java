package OSS.geteatwithme;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lombok.val;

public class PostingActivity extends AppCompatActivity {

    String []number_of_people1 = {"2", "3", "4", "5", "6", "7", "8"};
    String []number_of_people2 = {"1", "2", "3", "4", "5", "6", "7"};
    String posting_text = new String("");
    int category = 0;
    int num_of_people1 = 0;
    int num_of_people2 = 1;
    EditText editPosting;
    Button cancel, post;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);


        // 모일 인원 및 모인 인원 spinner 설정
        Spinner spinner_1 = findViewById(R.id.spinner1);
        Spinner spinner_2 = findViewById(R.id.spinner2);
        //모일 인원 spinner 설정
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, number_of_people1);
        adapter1.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner_1.setAdapter(adapter1);
        spinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                num_of_people1 = Integer.parseInt(number_of_people1[position]);
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
                num_of_people2 = Integer.parseInt(number_of_people2[position]);
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
                category = 0;
            }
        });
        // 중식
        radioButtons[1].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[1].setChecked(true);
                category = 1;
            }
        });
        // 일식
        radioButtons[2].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[2].setChecked(true);
                category = 2;
            }
        });
        // 양식
        radioButtons[3].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[3].setChecked(true);
                category = 3;
            }
        });
        // 분식
        radioButtons[4].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[4].setChecked(true);
                category = 4;
            }
        });
        // 아시안
        radioButtons[5].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[5].setChecked(true);
                category = 5;
            }
        });
        // 패스트 푸드
        radioButtons[6].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[6].setChecked(true);
                category = 6;
            }
        });


        // 날짜 설정
        EditText et_Date = (EditText) findViewById(R.id.Date);
        et_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PostingActivity.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        // 시간 설정
        final EditText et_time = (EditText) findViewById(R.id.Time);
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
                        String state = "AM";
                        // 선택한 시간이 12를 넘을경우 "PM"으로 변경 및 -12시간하여 출력 (ex : PM 6시 30분)
                        if (selectedHour > 12) {
                            selectedHour -= 12;
                            state = "PM";
                        }
                        // EditText에 출력할 형식 지정
                        et_time.setText(state + " " + selectedHour + "시 " + selectedMinute + "분");
                    }
                }, hour, minute, true); // true의 경우 24시간 형식의 TimePicker 출현
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        // posting text 관리
        editPosting = (EditText)findViewById(R.id.editPostingText);
        posting_text = editPosting.getText().toString();


        // button 관리
        cancel = (Button)findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 취소
            }
        });
        post = (Button)findViewById(R.id.btn_post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num_of_people1 <= num_of_people2)
                {
                    Toast.makeText(getApplicationContext(), "모인인원의 수는 모일 인원의 수보다 작아야 합니다.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    // 정보 서버에 posting
                }
            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";    // 출력형식   2021/07/26
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText et_date = (EditText) findViewById(R.id.Date);
        et_date.setText(sdf.format(myCalendar.getTime()));
    }

}
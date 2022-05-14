package OSS.geteatwithme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.chip.ChipGroup;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import OSS.geteatwithme.PostInfo.Post;

public class MainActivity extends AppCompatActivity {
    RadioButton[] radioButtons = new RadioButton[8];
    LinkedList<Post> posts = new LinkedList<Post>();
    LinearLayout linearlayout = null;

    void setAllRadioButtonOff(){
        for(int i=0; i< 8; i++)
            radioButtons[i].setChecked(false);
    }

    void showPosts(){
        if(linearlayout != null)
            ((ViewGroup)linearlayout.getParent()).removeView(linearlayout);
        linearlayout = new LinearLayout(this);
        linearlayout.setOrientation(LinearLayout.VERTICAL);

        // test-
        int idx = 0;
        for(Post p : posts){
            Button b = new Button(this);
            b.setLayoutParams(new androidx.appcompat.app.ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT ));
            b.setText(p.getRestaurant());
            linearlayout.addView(b, idx++);
        }
        // -test

        ScrollView view = (ScrollView)findViewById(R.id.scrollView2);
        view.addView(linearlayout, 0);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioButtons[0] = (RadioButton) findViewById(R.id.radioButton11);
        radioButtons[1] = (RadioButton) findViewById(R.id.radioButton12);
        radioButtons[2] = (RadioButton) findViewById(R.id.radioButton13);
        radioButtons[3] = (RadioButton) findViewById(R.id.radioButton14);
        radioButtons[4] = (RadioButton) findViewById(R.id.radioButton15);
        radioButtons[5] = (RadioButton) findViewById(R.id.radioButton16);
        radioButtons[6] = (RadioButton) findViewById(R.id.radioButton18);
        radioButtons[7] = (RadioButton) findViewById(R.id.radioButton19);

        // 전체 post 보여주기
        // posts <- 전체 포스트
        showPosts();

        // 한식
        radioButtons[0].setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[0].setChecked(true);

            }
        });

        // 중식
        radioButtons[1].setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[1].setChecked(true);
            }
        });

        // 일식
        radioButtons[2].setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[2].setChecked(true);
            }
        });

        // 양식
        radioButtons[3].setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[3].setChecked(true);
            }
        });

        // 분식
        radioButtons[4].setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[4].setChecked(true);
            }
        });

        // 아시안
        radioButtons[5].setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[5].setChecked(true);
            }
        });

        // 패스트 푸드
        radioButtons[6].setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[6].setChecked(true);
            }
        });

        // 전체
        radioButtons[7].setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                setAllRadioButtonOff();
                radioButtons[7].setChecked(true);
            }
        });
    }
}
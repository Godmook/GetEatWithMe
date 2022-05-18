package OSS.geteatwithme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ShowPostActivity extends AppCompatActivity {
    int POST_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);

        Intent myIntent = getIntent();
        POST_ID = myIntent.getIntExtra("postID", 0);

        // test-
        TextView textView = (TextView) findViewById(R.id.textView8);
        textView.setText("post id : " + POST_ID);
        // -test
    }
}
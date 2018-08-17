package com.babylone.alex.studentorganizer;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.babylone.alex.studentorganizer.Adapters.MarksAdapter;
import com.babylone.alex.studentorganizer.Adapters.SessionAdapter;
import com.babylone.alex.studentorganizer.Classes.Mark;
import com.babylone.alex.studentorganizer.Classes.Session;

import java.util.ArrayList;
import java.util.List;

public class SelectedChartActivity extends AppCompatActivity implements ChangeInterface {
    String lesson;
    float value;
    TextView lessonTextView, markTextView;
    LinearLayout ll;

    List<Mark> data = new ArrayList<>();
    DatabaseHelper db;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_chart);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lesson = getIntent().getStringExtra("Lesson");
        value = getIntent().getFloatExtra("Avg", 0);
        lessonTextView = (TextView) findViewById(R.id.textView16);
        markTextView = (TextView) findViewById(R.id.textView17);
        ll = (LinearLayout) findViewById(R.id.linearLayout2);
        lessonTextView.setText(lesson);
        setMark(value);

        list = (ListView) findViewById(R.id.marksList);
        db = new DatabaseHelper(this);
        refreshData();
    }
    private void refreshData() {
        data = db.getMarksByLesson(lesson);
        MarksAdapter adapter = new MarksAdapter(this,data,this);
        list.setAdapter(adapter);
    }
    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public void update() {
        int sum = 0;
        for (int i = 0; i< data.size();i++){
            sum +=Integer.valueOf(data.get(i).getMark());
        }
        if(data.size()>0) {
            setMark(sum / data.size());
        }else{
            setMark(0);
        }
    }

    public void setMark(float value){
        markTextView.setText(String.valueOf(value));
        int color;
        if(value<3){
            color = Color.parseColor("#ff6061");
        }else if(value>=3 && value<4){
            color = Color.parseColor("#f9a55c");
        }else{
            color = Color.parseColor("#6acc68");
        }
        ll.setBackgroundColor(color);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

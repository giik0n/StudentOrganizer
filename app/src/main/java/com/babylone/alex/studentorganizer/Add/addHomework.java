package com.babylone.alex.studentorganizer.Add;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.babylone.alex.studentorganizer.Classes.Homework;
import com.babylone.alex.studentorganizer.DatabaseHelper;
import com.babylone.alex.studentorganizer.Fragments.LessonsFragment;
import com.babylone.alex.studentorganizer.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class addHomework extends AppCompatActivity {
    SharedPreferences sp;
    DatabaseHelper db;
    Button button;
    EditText text;
    Spinner spinner;
    DatePicker dp;
    String date, time;
    int day, month, year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("SettingsActivity",Context.MODE_PRIVATE);
        setContentView(R.layout.activity_add_homework);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinner = (Spinner)findViewById(R.id.homeworkSpinner);
        button = (Button) findViewById(R.id.addHomeworkButton);
        text = (EditText) findViewById(R.id.editTextHomework);
        dp = (DatePicker) findViewById(R.id.datePickerHomework);
        db = new DatabaseHelper(this);
        String[] lessons = db.getUniqueLessons().toArray(new String[0]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lessons);
        spinner.setAdapter(adapter);

        day = dp.getDayOfMonth();
        month = dp.getMonth();
        year = dp.getYear();

        date = day+"-"+month+"-"+year;
        dp.init(dp.getYear(), dp.getMonth(),  dp.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
           @Override
           public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
               day = i2;
               month = i1;
               year = i;

           }
       });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sp.getString("Notify",null).equals("true")){
                time = sp.getString("Time","no time");
                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                time = sp.getString("Time",null);
                int hours = Integer.valueOf(time.split(":")[0]);
                int minutes = Integer.valueOf(time.split(":")[1]);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, day-Integer.valueOf(sp.getString("Before",null)));
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.HOUR_OF_DAY, hours);
                calendar.set(Calendar.MINUTE, minutes);
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                db.addHomework(new Homework(0,spinner.getSelectedItem().toString(),text.getText().toString(),df.format(calendar.getTime()),"false"));
                    Toast.makeText(addHomework.this, getString(R.string.added), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("singh.ajit.action.DISPLAY_NOTIFICATION");
                intent.putExtra("Title",getString(R.string.strHomework));
                intent.putExtra("Text",getString(R.string.homeworkTime));
                PendingIntent broadcast = PendingIntent.getBroadcast(getApplication(),100,intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),broadcast);
                }
            }
        });

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

package com.babylone.alex.studentorganizer.Add;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
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
import com.babylone.alex.studentorganizer.MainActivity;
import com.babylone.alex.studentorganizer.R;
import com.tapadoo.alerter.Alerter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class addHomework extends AppCompatActivity {
    SharedPreferences sp;
    DatabaseHelper db;
    Button button, pickDateButton;
    EditText text;
    Spinner spinner;
    String time;
    int day, month, year;
    Calendar dateAndTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("SettingsActivity",Context.MODE_PRIVATE);
        setContentView(R.layout.activity_add_homework);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinner = (Spinner)findViewById(R.id.homeworkSpinner);
        button = (Button) findViewById(R.id.addHomeworkButton);
        pickDateButton = (Button) findViewById(R.id.pickDateButton);
        text = (EditText) findViewById(R.id.editTextHomework);

        db = new DatabaseHelper(this);
        String[] lessons = db.getUniqueLessons().toArray(new String[0]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lessons);
        spinner.setAdapter(adapter);
        day = dateAndTime.get(Calendar.DAY_OF_MONTH);
        month = dateAndTime.get(Calendar.MONTH);
        year = dateAndTime.get(Calendar.YEAR);
        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(addHomework.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        day = i2;
                        month = i1;
                        year = i;
                        String monthStr = String.valueOf(month);
                        String dayStr = String.valueOf(day);
                        if (i1<10){
                            monthStr = "0"+i1+1;
                        }
                        if (i2<10){
                            dayStr = "0"+i2;
                        }
                        pickDateButton.setText(year+"-"+monthStr+"-"+dayStr);
                    }
                },
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH))
                        .show();
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
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.HOUR_OF_DAY, hours);
                calendar.set(Calendar.MINUTE, minutes);
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                if (text.getText().length()!=0) {
                    db.addHomework(new Homework(0, spinner.getSelectedItem().toString(), text.getText().toString(), df.format(calendar.getTime()), "false"));
                    calendar.set(Calendar.DAY_OF_MONTH, day-Integer.valueOf(sp.getString("Before",null)));
                    Alerter.create(addHomework.this)
                            .setText(R.string.added)
                            .setBackgroundColorRes(R.color.greenTag)
                            .setIcon(R.drawable.ic_check_white_24dp)
                            .show();
                    Intent intent = new Intent("singh.ajit.action.DISPLAY_NOTIFICATION");
                    intent.putExtra("Title", getString(R.string.strHomework));
                    intent.putExtra("Text", getString(R.string.homeworkTime));
                    PendingIntent broadcast = PendingIntent.getBroadcast(getApplication(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);
                }else{
                    Alerter.create(addHomework.this)
                            .setText(R.string.fillInAllTheFields)
                            .setBackgroundColorRes(R.color.redTag)
                            .show();
                    }
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

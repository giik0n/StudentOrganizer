package com.babylone.alex.studentorganizer.Add;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.babylone.alex.studentorganizer.Classes.CalendarDay;
import com.babylone.alex.studentorganizer.DatabaseHelper;
import com.babylone.alex.studentorganizer.R;
import com.tapadoo.alerter.Alerter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class addCalendarDay extends AppCompatActivity {

    EditText name, description;
    Calendar calendar;
    int day, month, year;
    String time;
    Button button, chooseDateButtonCalendar,chooseTimeButtonCalendar;
    DatabaseHelper db;
    String hour, minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_calendar_day);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name = (EditText)findViewById(R.id.editText4);
        description = (EditText)findViewById(R.id.editText5);
        button = (Button)findViewById(R.id.button3);
        chooseDateButtonCalendar = (Button)findViewById(R.id.chooseDateButtonCalendar);
        chooseTimeButtonCalendar = (Button)findViewById(R.id.chooseTimeButtonCalendar);
        db = new DatabaseHelper(this);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);


        chooseDateButtonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(addCalendarDay.this, new DatePickerDialog.OnDateSetListener() {
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
                        chooseDateButtonCalendar.setText(year+"-"+monthStr+"-"+dayStr);
                    }
                },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });


        hour = String.valueOf(calendar.get(Calendar.HOUR));
        minute = String.valueOf(calendar.get(Calendar.MINUTE));

        if(Integer.valueOf(hour)<10){
            hour = "0"+hour;
        }

        if(Integer.valueOf(minute)<10){
            minute = "0"+minute;
        }
        time = hour+":"+minute;

        chooseTimeButtonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(addCalendarDay.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        hour = String.valueOf(i);
                        minute = String.valueOf(i1);
                        if(Integer.valueOf(hour)<10){
                            hour = "0"+hour;
                        }

                        if(Integer.valueOf(minute)<10){
                            minute = "0"+minute;
                        }
                        time = hour+":"+minute;
                        chooseTimeButtonCalendar.setText(time);
                    }
                },calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true).show();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                if (name.getText().length()!=0 && description.getText().length()!=0) {
                db.addDay(new CalendarDay(0, name.getText().toString(), description.getText().toString(),format.format(calendar.getTime()),time));
                Alerter.create(addCalendarDay.this)
                        .setText(R.string.added)
                        .setBackgroundColorRes(R.color.greenTag)
                        .setIcon(R.drawable.ic_check_white_24dp)
                        .show();
            }else{
                    Alerter.create(addCalendarDay.this)
                            .setText(R.string.fillInAllTheFields)
                            .setBackgroundColorRes(R.color.redTag)
                            .show();
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

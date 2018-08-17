package com.babylone.alex.studentorganizer.Add;

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
    DatePicker date;
    TimePicker tp;
    String time;
    Button button;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_calendar_day);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name = (EditText)findViewById(R.id.editText4);
        description = (EditText)findViewById(R.id.editText5);
        date = (DatePicker)findViewById(R.id.datePicker3);
        tp = (TimePicker)findViewById(R.id.timePicker3);
        button = (Button)findViewById(R.id.button3);
        db = new DatabaseHelper(this);
        String hour = String.valueOf(tp.getCurrentHour()),
                minute = String.valueOf(tp.getCurrentMinute());
        time = hour+":"+minute;
        tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                String hour = String.valueOf(tp.getCurrentHour()),
                        minute = String.valueOf(tp.getCurrentMinute());

                if(tp.getCurrentHour()<10){
                    hour = "0"+String.valueOf(tp.getCurrentHour());
                }

                if(tp.getCurrentHour()>12){
                    hour = String.valueOf(Integer.valueOf(hour));
                }

                if(tp.getCurrentMinute()<10){
                    minute = "0"+String.valueOf(tp.getCurrentMinute());
                }
                time = hour+":"+minute;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, date.getYear());
                calendar.set(Calendar.MONTH, date.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());
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

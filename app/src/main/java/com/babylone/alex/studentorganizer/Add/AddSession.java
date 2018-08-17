package com.babylone.alex.studentorganizer.Add;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.babylone.alex.studentorganizer.Classes.Session;
import com.babylone.alex.studentorganizer.DatabaseHelper;
import com.babylone.alex.studentorganizer.R;
import com.tapadoo.alerter.Alerter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddSession extends AppCompatActivity {

    Spinner spinner;
    EditText lesson, cab;
    DatePicker dp;
    TimePicker tp;
    Button button;
    String time;
    SimpleDateFormat df;
    DatabaseHelper db;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_session);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinner = (Spinner) findViewById(R.id.spinnerAddSesType);
        cab = (EditText) findViewById(R.id.editTextAddSesCab);
        lesson = (EditText) findViewById(R.id.editTextAddSesLesson);
        dp = (DatePicker) findViewById(R.id.datePicker2);
        tp = (TimePicker) findViewById(R.id.timePicker2);
        button = (Button) findViewById(R.id.button);
        db = new DatabaseHelper(this);
        final String[] lessonTypes = {getString(R.string.exam), getString(R.string.test), getString(R.string.reassembly)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lessonTypes);
        spinner.setAdapter(adapter);

        calendar = Calendar.getInstance();

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
                calendar.set(Calendar.DAY_OF_MONTH, dp.getDayOfMonth());
                calendar.set(Calendar.MONTH, dp.getMonth());
                calendar.set(Calendar.YEAR, dp.getYear());
                df = new SimpleDateFormat("yyyy-MM-dd");
                if (lesson.getText().length()!=0 && cab.getText().length()!=0) {
                db.addSession(new Session(0,lesson.getText().toString(), spinner.getSelectedItem().toString(),df.format(calendar.getTime()),time,cab.getText().toString()));
                Alerter.create(AddSession.this)
                        .setText(R.string.added)
                        .setBackgroundColorRes(R.color.greenTag)
                        .setIcon(R.drawable.ic_check_white_24dp)
                        .show();
            }else{
                    Alerter.create(AddSession.this)
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

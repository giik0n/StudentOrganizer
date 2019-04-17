package com.babylone.alex.studentorganizer.Add;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.babylone.alex.studentorganizer.Classes.Mark;
import com.babylone.alex.studentorganizer.DatabaseHelper;
import com.babylone.alex.studentorganizer.R;
import com.tapadoo.alerter.Alerter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class addMarkSession extends AppCompatActivity {

    Spinner lesson;
    EditText mark;
    Button button,chooseDateButtonSessionMark;
    SimpleDateFormat df;
    DatabaseHelper db;
    Calendar calendar;
    int day, month, year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mark_session);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lesson = (Spinner) findViewById(R.id.spinner_add_mark_session);
        mark = (EditText) findViewById(R.id.editText2);
        button = (Button) findViewById(R.id.button2);
        chooseDateButtonSessionMark = (Button) findViewById(R.id.chooseDateButtonSessionMark);
        db = new DatabaseHelper(this);

        String[] lessons = db.getUniqueSession().toArray(new String[0]);
        if(lessons.length == 0){
            Toast.makeText(this, getString(R.string.firstAddTheExams), Toast.LENGTH_SHORT).show();
            finish();
        }else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, lessons);
            lesson.setAdapter(adapter);
        }

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        chooseDateButtonSessionMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(addMarkSession.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        day = i2;
                        month = i1;
                        year = i;
                        String monthStr = String.valueOf(month);
                        String dayStr = String.valueOf(day);
                        if (i1<10){
                            monthStr = "0"+(i1+1);
                        }
                        if (i2<10){
                            dayStr = "0"+i2;
                        }
                        chooseDateButtonSessionMark.setText(year+"-"+monthStr+"-"+dayStr);
                    }
                },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });


        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        df = new SimpleDateFormat("yyyy-MM-dd");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mark.getText().length()>0){
                db.addMark(new Mark(0, lesson.getSelectedItem().toString(), mark.getText().toString(),df.format(calendar.getTime())));
                    Alerter.create(addMarkSession.this)
                            .setText(R.string.added)
                            .setBackgroundColorRes(R.color.greenTag)
                            .setIcon(R.drawable.ic_check_white_24dp)
                            .show();
                }else{
                    Alerter.create(addMarkSession.this)
                            .setText(R.string.added)
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

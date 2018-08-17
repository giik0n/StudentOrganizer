package com.babylone.alex.studentorganizer.Add;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class addMarkSession extends AppCompatActivity {

    Spinner lesson;
    EditText mark;
    DatePicker date;
    Button button;
    SimpleDateFormat df;
    DatabaseHelper db;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mark_session);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lesson = (Spinner) findViewById(R.id.spinner_add_mark_session);
        mark = (EditText) findViewById(R.id.editText2);
        date = (DatePicker) findViewById(R.id.datePicker);
        button = (Button) findViewById(R.id.button2);
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
        calendar.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());
        calendar.set(Calendar.MONTH, date.getMonth());
        calendar.set(Calendar.YEAR, date.getYear());
        df = new SimpleDateFormat("yyyy-MM-dd");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mark.getText().length()>0){
                db.addMark(new Mark(0, lesson.getSelectedItem().toString(), mark.getText().toString(),df.format(calendar.getTime())));
                Toast.makeText(addMarkSession.this, getString(R.string.added), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(addMarkSession.this, getString(R.string.fillInAllTheFields), Toast.LENGTH_SHORT).show();
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

package com.babylone.alex.studentorganizer.Add;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tapadoo.alerter.Alerter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddSession extends AppCompatActivity {

    Spinner spinner;
    EditText lesson, cab;

    Button button, chooseDateButton, chooseTimeButton;
    String time;
    SimpleDateFormat df;
    DatabaseHelper db;
    int day, month, year;
    Calendar calendar;
    String hour, minute;
    Spinner faculty, branch, course;
    DatabaseReference sessionRef = FirebaseDatabase.getInstance().getReference().child("Session");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_session);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinner = findViewById(R.id.spinnerAddSesType);
        cab = findViewById(R.id.editTextAddSesCab);
        lesson = findViewById(R.id.editTextAddSesLesson);

        button = findViewById(R.id.button);
        chooseDateButton = findViewById(R.id.chooseDateButton);
        chooseTimeButton = findViewById(R.id.chooseTimeButton);
        faculty = findViewById(R.id.facultySpinner);
        branch = findViewById(R.id.branchSpinner);
        course = findViewById(R.id.courseSpiner);
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        String[] faculties = {"IT"};
        String[] branches = {"PI","KN","ST"};
        String[] courses = {"1","2","3","4","5"};

        faculty.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, faculties));
        branch.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, branches));
        course.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, courses));

        chooseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddSession.this, new DatePickerDialog.OnDateSetListener() {
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
                        chooseDateButton.setText(year+"-"+monthStr+"-"+dayStr);
                    }
                },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        db = new DatabaseHelper(this);
        final String[] lessonTypes = {getString(R.string.exam), getString(R.string.test), getString(R.string.reassembly)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lessonTypes);
        spinner.setAdapter(adapter);

        hour = String.valueOf(calendar.get(Calendar.HOUR));
        minute = String.valueOf(calendar.get(Calendar.MINUTE));

        if(Integer.valueOf(hour)<10){
            hour = "0"+hour;
        }

        if(Integer.valueOf(minute)<10){
            minute = "0"+minute;
        }
        time = hour+":"+minute;

        chooseTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(AddSession.this, new TimePickerDialog.OnTimeSetListener() {
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
                        chooseTimeButton.setText(time);
                    }
                },calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.YEAR, year);
                df = new SimpleDateFormat("yyyy-MM-dd");
                if (lesson.getText().length()!=0 && cab.getText().length()!=0) {
                    //db.addSession(new Session("",lesson.getText().toString(), spinner.getSelectedItem().toString(),df.format(calendar.getTime()),time,cab.getText().toString()));
                    addSession();
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

    public void addSession(){
        HashMap postMap = new HashMap();
        postMap.put("lesson", lesson.getText().toString());
        postMap.put("type", spinner.getSelectedItem().toString());
        postMap.put("date", df.format(calendar.getTime()));
        postMap.put("time", time);
        postMap.put("classroom", cab.getText().toString());
        postMap.put("group", faculty.getSelectedItem().toString()+"_"+branch.getSelectedItem().toString()+"_"+course.getSelectedItem().toString());
        String key = sessionRef.child(mAuth.getUid()).push().getKey();
        sessionRef.child(key).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {// додаємо рецепт в бд
                if (task.isSuccessful()){
                    Toast.makeText(AddSession.this, "Added", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(AddSession.this, "Error: "+ task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

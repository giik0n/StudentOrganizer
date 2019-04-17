package com.babylone.alex.studentorganizer.Add;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.babylone.alex.studentorganizer.DatabaseHelper;
import com.babylone.alex.studentorganizer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tapadoo.alerter.Alerter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class addHomework extends AppCompatActivity {
    SharedPreferences sp;
    DatabaseHelper db;
    Button button, pickDateButton;
    EditText text, lesson;
    //Spinner spinner;
    String time;
    int day, month, year;
    Calendar dateAndTime = Calendar.getInstance();
    Spinner faculty, branch, course;
    DatabaseReference homeworkRef = FirebaseDatabase.getInstance().getReference().child("Homework");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("SettingsActivity",Context.MODE_PRIVATE);
        setContentView(R.layout.activity_add_homework);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        button = findViewById(R.id.addHomeworkButton);
        pickDateButton = findViewById(R.id.pickDateButton);
        text = findViewById(R.id.editTextHomework);
        lesson = findViewById(R.id.addHomeworkLesson);

        db = new DatabaseHelper(this);
        day = dateAndTime.get(Calendar.DAY_OF_MONTH);
        month = dateAndTime.get(Calendar.MONTH);
        year = dateAndTime.get(Calendar.YEAR);
        faculty = findViewById(R.id.spinnerHomewrokFaculty);
        branch = findViewById(R.id.spinnerHomeworkBranch);
        course = findViewById(R.id.spinnerHomeworkCourse);
        String[] faculties = {"IT"};
        String[] branches = {"PI","KN","ST"};
        String[] courses = {"1","2","3","4","5"};

        faculty.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, faculties));
        branch.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, branches));
        course.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses));
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
                            monthStr = "0"+(i1+1);
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

                    HashMap postMap = new HashMap();
                    postMap.put("lesson", lesson.getText().toString());
                    postMap.put("text", text.getText().toString());
                    postMap.put("date", df.format(calendar.getTime()));
                    postMap.put("group", faculty.getSelectedItem().toString()+"_"+branch.getSelectedItem().toString()+"_"+course.getSelectedItem().toString());
                    String key = homeworkRef.push().getKey();
                    homeworkRef.child(key).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {// додаємо рецепт в бд
                            if (task.isSuccessful()){
                                Toast.makeText(addHomework.this, "Added", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(addHomework.this, "Error: "+ task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                    calendar.set(Calendar.DAY_OF_MONTH, day-Integer.valueOf(sp.getString("Before",null)));
                    Alerter.create(addHomework.this)
                            .setText(R.string.added)
                            .setBackgroundColorRes(R.color.greenTag)
                            .setIcon(R.drawable.ic_check_white_24dp)
                            .show();
                    if(sp.getString("Notify",null).equals("true")) {
                        Intent intent = new Intent("singh.ajit.action.DISPLAY_NOTIFICATION");
                        intent.putExtra("Title", getString(R.string.strHomework));
                        intent.putExtra("Text", getString(R.string.homeworkTime));
                        PendingIntent broadcast = PendingIntent.getBroadcast(getApplication(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);
                    }
                }else{
                    Alerter.create(addHomework.this)
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

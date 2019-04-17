package com.babylone.alex.studentorganizer.Add;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.babylone.alex.studentorganizer.Classes.Lesson;
import com.babylone.alex.studentorganizer.DatabaseHelper;
import com.babylone.alex.studentorganizer.MainActivity;
import com.babylone.alex.studentorganizer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class addLesson extends AppCompatActivity {
    DatabaseHelper db;
    EditText name, position, cab;
    Spinner spinner, spinner2;
    RadioButton rb;
    RadioGroup rg;
    Button button;
    String variation;
    boolean num;
    SharedPreferences sp;
    DatabaseReference scheduleRef = FirebaseDatabase.getInstance().getReference().child("Schedule");
    Spinner faculty, branch, course;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lesson);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        faculty = findViewById(R.id.spinnerLessonFaculty);
        branch = findViewById(R.id.spinnerLessonBranch);
        course = findViewById(R.id.spinnerLessonCourse);
        String[] faculties = {"IT"};
        String[] branches = {"PI","KN","ST"};
        String[] courses = {"1","2","3","4","5"};

        faculty.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, faculties));
        branch.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, branches));
        course.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses));
        sp = getSharedPreferences("SettingsActivity",Context.MODE_PRIVATE);
        db = new DatabaseHelper(this);
        name = (EditText)findViewById(R.id.addLessonText);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
        position = (EditText)findViewById(R.id.addLessonPosition);
        cab = (EditText)findViewById(R.id.addLessonCab);
        spinner = (Spinner)findViewById(R.id.addLessonSpinner);
        spinner2 = (Spinner) findViewById(R.id.addLessonSpinner2);
        button = (Button)findViewById(R.id.addLessonButton);
        rg = (RadioGroup)findViewById(R.id.radioGroup) ;
        if (sp.getString("twoWeeks","false").equals("false")){
            rg.setVisibility(View.INVISIBLE);
            rg.getLayoutParams().height= 1;
            rg.requestLayout();
        }
        num = getIntent().getExtras().getBoolean("Numerator");
        variation = getString(R.string.numerator);

        final ArrayList<String> daysOfWeek = new ArrayList<String>(Arrays.asList(getString(R.string.monday), getString(R.string.tuesday), getString(R.string.wednesday), getString(R.string.thursday), getString(R.string.friday)));
        if (sp.getString("isSaturnday","false").equals("true")) daysOfWeek.add(getString(R.string.isSaturnday));
        final String[] lessonTypes = {getString(R.string.talk), getString(R.string.lab), getString(R.string.practicalWork)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, daysOfWeek);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lessonTypes);
        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter2);
        if(num){
            rg.check(R.id.radioButton3);
        }else {
            rg.check(R.id.radioButton4);
        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rb = (RadioButton)findViewById(rg.getCheckedRadioButtonId());
                variation = rb.getText().toString();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().length()==0 || cab.getText().length()==0 || position.getText().length()==0){
                    Alerter.create(addLesson.this)
                            .setText(R.string.fillInAllTheFields)
                            .setBackgroundColorRes(R.color.redTag)
                            .show();

                }else {
                    //Lesson lesson = new Lesson(0,Integer.valueOf(position.getText().toString()),name.getText().toString()+" (" + spinner2.getSelectedItem() .toString()+")",spinner.getSelectedItem().toString(), variation, cab.getText().toString());
                    //db.addLesson(lesson);

                    HashMap postMap = new HashMap();
                    postMap.put("lesson", name.getText().toString());
                    postMap.put("position", position.getText().toString());
                    postMap.put("type", spinner2.getSelectedItem().toString());
                    postMap.put("day", spinner.getSelectedItem().toString());
                    postMap.put("week", variation);
                    postMap.put("classroom", cab.getText().toString());
                    postMap.put("group", faculty.getSelectedItem().toString()+"_"+branch.getSelectedItem().toString()+"_"+course.getSelectedItem().toString());
                    String key = scheduleRef.push().getKey();
                    scheduleRef.child(key).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {// додаємо рецепт в бд
                            if (task.isSuccessful()){
                                Toast.makeText(addLesson.this, "Added", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(addLesson.this, "Error: "+ task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                    Alerter.create(addLesson.this)
                            .setText(R.string.added)
                            .setBackgroundColorRes(R.color.greenTag)
                            .setIcon(R.drawable.ic_check_white_24dp)
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

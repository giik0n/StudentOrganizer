package com.babylone.alex.studentorganizer.Add;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.babylone.alex.studentorganizer.Classes.Lesson;
import com.babylone.alex.studentorganizer.DatabaseHelper;
import com.babylone.alex.studentorganizer.R;

public class addLesson extends AppCompatActivity {
    DatabaseHelper db;
    EditText name, position, cab;
    Spinner spinner, spinner2;
    RadioButton rb;
    RadioGroup rg;
    Button button;
    String variation;
    boolean num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lesson);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new DatabaseHelper(this);
        name = (EditText)findViewById(R.id.addLessonText);
        position = (EditText)findViewById(R.id.addLessonPosition);
        cab = (EditText)findViewById(R.id.addLessonCab);
        spinner = (Spinner)findViewById(R.id.addLessonSpinner);
        spinner2 = (Spinner) findViewById(R.id.addLessonSpinner2);
        button = (Button)findViewById(R.id.addLessonButton);
        rg = (RadioGroup)findViewById(R.id.radioGroup) ;
        num = getIntent().getExtras().getBoolean("Numerator");
        variation = getString(R.string.numerator);

        final String[] daysOfWeek = {getString(R.string.monday), getString(R.string.tuesday), getString(R.string.wednesday), getString(R.string.thursday), getString(R.string.friday)};
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
                    Toast.makeText(addLesson.this, getString(R.string.fillInAllTheFields), Toast.LENGTH_SHORT).show();

                }else {
                    Lesson lesson = new Lesson(0,Integer.valueOf(position.getText().toString()),name.getText().toString()+" (" + spinner2.getSelectedItem() .toString()+")",spinner.getSelectedItem().toString(), variation, cab.getText().toString());
                    db.addLesson(lesson);
                    Toast.makeText(addLesson.this, getString(R.string.added), Toast.LENGTH_SHORT).show();
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

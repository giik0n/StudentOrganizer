package com.babylone.alex.studentorganizer;

import android.app.AlarmManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Switch switcher, saturndaySwitcher, twoWeeksSwitcher;
    boolean notify;
    AlarmManager alarmManager;
    EditText editText;
    TextView textView7, settings_time;
    Button settings_chooseTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        switcher = (Switch) findViewById(R.id.switch1);
        saturndaySwitcher = (Switch) findViewById(R.id.switch2);
        twoWeeksSwitcher = (Switch) findViewById(R.id.switch3);
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        editText = (EditText)findViewById(R.id.editText);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        sp = getPreferences(Context.MODE_PRIVATE);
        textView7 = (TextView)findViewById(R.id.textView7);
        settings_time = (TextView)findViewById(R.id.settings_time);
        settings_time.setText(sp.getString("Time", null));
        settings_chooseTime = (Button) findViewById(R.id.settings_chooseTime);
        editor = sp.edit();
        notify = Boolean.valueOf(sp.getString("Notify","false"));
        switcher.setChecked(notify);
        saturndaySwitcher.setChecked(Boolean.valueOf(sp.getString("isSaturnday","false")));
        twoWeeksSwitcher.setChecked(Boolean.valueOf(sp.getString("twoWeeks","false")));
        checkSwitcher();
        editText.setText(sp.getString("Before",null));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!editText.getText().toString().equals("") || editText.getText().toString() == null){
                    if(Integer.parseInt(editText.getText().toString()) == 0){
                        textView7.setText(getString(R.string.moreDays) +" "+ getString(R.string.beforeVerification));
                    }else if(Integer.parseInt(editText.getText().toString()) == 1){
                        textView7.setText(getString(R.string.oneDay) +" "+ getString(R.string.beforeVerification));
                    } else if (Integer.parseInt(editText.getText().toString()) >=2 && Integer.parseInt(editText.getText().toString()) <=4 ) {
                        textView7.setText(getString(R.string.twoDays) +" "+ getString(R.string.beforeVerification));
                    }else if(Integer.parseInt(editText.getText().toString()) >= 5){
                        textView7.setText(getString(R.string.moreDays) +" "+ getString(R.string.beforeVerification));
                    }
                    editor.putString("Before", editText.getText().toString());
                    editor.commit();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putString("Notify", String.valueOf(b));
                editor.commit();
                notify = b;
                checkSwitcher();
            }
        });

        saturndaySwitcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putString("isSaturnday", String.valueOf(b));
                editor.commit();
            }
        });

        twoWeeksSwitcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putString("twoWeeks", String.valueOf(b));
                editor.commit();
            }
        });

        settings_chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hour = String.valueOf(selectedHour),
                                minute = String.valueOf(selectedMinute);

                        if(selectedHour<10){
                            hour = "0"+String.valueOf(selectedHour);
                        }

                        if(selectedHour>12){
                            hour = String.valueOf(Integer.valueOf(hour));
                        }

                        if(selectedMinute<10){
                            minute = "0"+String.valueOf(selectedMinute);
                        }

                        settings_time.setText( hour + ":" + minute);
                        editor.putString("Time", hour+":"+minute);
                        editor.commit();
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle(getString(R.string.chooseTime));
                mTimePicker.show();
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

    void checkSwitcher(){
        settings_chooseTime.setEnabled(notify);
        editText.setEnabled(notify);
    }
}

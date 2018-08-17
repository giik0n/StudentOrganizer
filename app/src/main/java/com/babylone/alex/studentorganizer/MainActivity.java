package com.babylone.alex.studentorganizer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.babylone.alex.studentorganizer.Add.AddSession;
import com.babylone.alex.studentorganizer.Add.addCalendarDay;
import com.babylone.alex.studentorganizer.Add.addHomework;
import com.babylone.alex.studentorganizer.Add.addLesson;
import com.babylone.alex.studentorganizer.Add.addMark;
import com.babylone.alex.studentorganizer.Fragments.CalendarFragment;
import com.babylone.alex.studentorganizer.Fragments.CalendarFragments.DayFragment;
import com.babylone.alex.studentorganizer.Fragments.CalendarFragments.MonthFragment;
import com.babylone.alex.studentorganizer.Fragments.CalendarFragments.WeekFragment;
import com.babylone.alex.studentorganizer.Fragments.HomeWorkFragment;
import com.babylone.alex.studentorganizer.Fragments.LessonsFragment;
import com.babylone.alex.studentorganizer.Fragments.MarksFragment;
import com.babylone.alex.studentorganizer.Fragments.SessionFragment;

import java.io.File;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    LessonsFragment lessonsFrag;
    HomeWorkFragment homeWorkFrag;
    SessionFragment sessionFrag;
    MarksFragment marksFrag;
    MonthFragment monthFrag;
    WeekFragment weekFragment;
    DayFragment dayFragment;
    CalendarFragment calendarFragment;
    FloatingActionButton fab;
    android.support.v4.app.FragmentTransaction ft;
    String currentFragment;
    DatabaseHelper db;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        currentFragment = "";
        db = new DatabaseHelper(this);
        sp = getSharedPreferences("SettingsActivity", Context.MODE_PRIVATE);
        editor = sp.edit();
        if(!sp.contains("Notify")){
            editor.putString("Notify","true");
            editor.commit();
        }
        if(!sp.contains("Before")){
            editor.putString("Before","1");
            editor.commit();
        }
        if(!sp.contains("Time")){
            editor.putString("Time","18:00");
            editor.commit();
        }
        lessonsFrag = new LessonsFragment();
        homeWorkFrag = new HomeWorkFragment();
        sessionFrag = new SessionFragment();
        marksFrag = new MarksFragment();
        monthFrag = new MonthFragment();
        weekFragment = new WeekFragment();
        dayFragment = new DayFragment();
        calendarFragment = new CalendarFragment();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentFragment){
                    case "LESSONS_FRAGMENT":
                        addLesson(lessonsFrag.getNumerator());

                        break;
                    case "HOMEWORK_FRAGMENT":
                        if(db.getUniqueLessons().toArray(new String[0]).length == 0){
                            Toast.makeText(MainActivity.this, getString(R.string.firstAddLessons), Toast.LENGTH_SHORT).show();
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.container,lessonsFrag);
                            setTitle(getString(R.string.strLessons));
                            currentFragment = "LESSONS_FRAGMENT";
                            ft.commit();
                            navigationView.setCheckedItem(R.id.nav_lessons);
                            fab.show();
                        }else{
                            addHomework();
                        }
                        break;
                    case "SESSION_FRAGMENT":
                        addSession();
                        break;
                    case "MARKS_FRAGMENT":
                        addMark();
                        break;
                    case "CALENDAR_FRAGMENT":
                        addCalendarDay();
                        break;
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        ft = getSupportFragmentManager().beginTransaction();
        if(ft.isEmpty()){
            drawer.openDrawer(Gravity.START);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        ft = getSupportFragmentManager().beginTransaction();
        ImageView img = (ImageView)findViewById(R.id.imageView2);
        img.setImageAlpha(64);
        switch (id){
            case R.id.nav_lessons:
                ft.replace(R.id.container,lessonsFrag);
                setTitle(getString(R.string.strLessons));
                currentFragment = "LESSONS_FRAGMENT";
                fab.show();
                break;
            case R.id.nav_homework:
                ft.replace(R.id.container,homeWorkFrag);
                setTitle(getString(R.string.strHomework));
                currentFragment = "HOMEWORK_FRAGMENT";
                fab.show();
                break;
            case R.id.nav_session:
                ft.replace(R.id.container,sessionFrag);
                setTitle(getString(R.string.strSession));
                currentFragment = "SESSION_FRAGMENT";
                fab.show();
                break;
            case R.id.nav_marks:
                ft.replace(R.id.container,marksFrag);
                setTitle(getString(R.string.strMarks));
                currentFragment = "MARKS_FRAGMENT";
                fab.hide();
                break;
            case R.id.nav_calendar:
                ft.replace(R.id.container,calendarFragment);
                currentFragment = "CALENDAR_FRAGMENT";
                fab.show();
                break;
        }ft.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void addLesson(boolean num){
                Intent intent = new Intent(this, addLesson.class);
                intent.putExtra("Numerator", num);
                startActivity(intent);
    }
    public void addHomework(){
        Intent intent = new Intent(this, addHomework.class);
        startActivity(intent);
    }
    public void addSession(){
        Intent intent = new Intent(this, AddSession.class);
        startActivity(intent);
    }
    public void addMark(){
        Intent intent = new Intent(this, addMark.class);
        startActivity(intent);
    }
    private void addCalendarDay() {
        Intent intent = new Intent(this, addCalendarDay.class);
        startActivity(intent);
    }

}

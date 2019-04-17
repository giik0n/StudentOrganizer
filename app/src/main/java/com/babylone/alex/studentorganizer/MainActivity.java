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
import android.widget.TextView;
import android.widget.Toast;

import com.babylone.alex.studentorganizer.Add.AddSession;
import com.babylone.alex.studentorganizer.Add.addCalendarDay;
import com.babylone.alex.studentorganizer.Add.addHomework;
import com.babylone.alex.studentorganizer.Add.addLesson;
import com.babylone.alex.studentorganizer.Add.addMark;
import com.babylone.alex.studentorganizer.Classes.AppRater;
import com.babylone.alex.studentorganizer.Fragments.CalendarFragment;
import com.babylone.alex.studentorganizer.Fragments.CalendarFragments.DayFragment;
import com.babylone.alex.studentorganizer.Fragments.CalendarFragments.MonthFragment;
import com.babylone.alex.studentorganizer.Fragments.CalendarFragments.WeekFragment;
import com.babylone.alex.studentorganizer.Fragments.ChatFragment;
import com.babylone.alex.studentorganizer.Fragments.HomeWorkFragment;
import com.babylone.alex.studentorganizer.Fragments.LessonsFragment;
import com.babylone.alex.studentorganizer.Fragments.MarksFragment;
import com.babylone.alex.studentorganizer.Fragments.SessionFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

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
    ChatFragment chatFragment;
    FloatingActionButton fab;
    android.support.v4.app.FragmentTransaction ft;
    String currentFragment;
    DatabaseHelper db;
    SharedPreferences sp, userInfo;
    SharedPreferences.Editor editor;
    NavigationView navigationView;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
    CircleImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }else{
            checkUserExistence();
        }
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppRater.app_launched(this);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        currentFragment = "";
        db = new DatabaseHelper(this);
        sp = getSharedPreferences("SettingsActivity", Context.MODE_PRIVATE);
        userInfo = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
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
        if(!sp.contains("twoWeeks")){
            editor.putString("twoWeeks","false");
            editor.commit();
        }
        if(!sp.contains("isSaturnday")){
            editor.putString("isSaturnday","false");
            editor.commit();
        }
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        lessonsFrag = new LessonsFragment();
        homeWorkFrag = new HomeWorkFragment();
        sessionFrag = new SessionFragment();
        marksFrag = new MarksFragment();
        monthFrag = new MonthFragment();
        weekFragment = new WeekFragment();
        dayFragment = new DayFragment();
        calendarFragment = new CalendarFragment();
        chatFragment = new ChatFragment();
        fab = findViewById(R.id.fab);
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentFragment){
                    case "LESSONS_FRAGMENT":
                        addLesson(lessonsFrag.getNumerator());
                        break;
                    case "HOMEWORK_FRAGMENT":
                        addHomework();
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

    private void checkUserExistence() {
        try {
            final String currentUserId = mAuth.getCurrentUser().getUid();
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(currentUserId)){
                        startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                    }else {
                        int numtries = 20;
                        while(numtries-- != 0){
                            TextView studentsName = findViewById(R.id.studentsName);
                            TextView studentsGroup = findViewById(R.id.studentsGroup);
                            profileImage = findViewById(R.id.profileImageNav);
                        try {
                            if (dataSnapshot.child(currentUserId).hasChild("profileimage")) {
                                String image = dataSnapshot.child(currentUserId).child("profileimage").getValue().toString();
                                    Picasso.get().load(image).into(profileImage);
                            }
                            if (dataSnapshot.child(currentUserId).hasChild("last_name") && dataSnapshot.child(mAuth.getUid()).hasChild("first_name")) {
                                    studentsName.setText(dataSnapshot.child(currentUserId).child("last_name").getValue().toString() + " " + dataSnapshot.child(currentUserId).child("first_name").getValue().toString());
                            }
                            if (dataSnapshot.child(currentUserId).hasChild("group")) {
                                    studentsGroup.setText(dataSnapshot.child(currentUserId).child("group").getValue().toString().replaceAll("_", " "));
                            }
                            break;
                        }catch (IllegalArgumentException | NullPointerException e){
                            e.printStackTrace();
                            continue;
                            }
                        }
                        editor = userInfo.edit();
                        editor.putString("userGroup",dataSnapshot.child(currentUserId).child("group").getValue().toString());
                        editor.putString("userRole",dataSnapshot.child(currentUserId).child("role").getValue().toString());
                        editor.commit();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (NullPointerException e){
            e.printStackTrace();
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
        }else if (id == R.id.action_logout){
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }else if (id == R.id.action_profile){
            startActivity(new Intent(this, ProfileActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        ft = getSupportFragmentManager().beginTransaction();
        ImageView img = findViewById(R.id.imageView2);
        img.setImageAlpha(64);
        switch (id){
            case R.id.nav_lessons:
                ft.replace(R.id.container,lessonsFrag);
                setTitle(getString(R.string.strLessons));
                currentFragment = "LESSONS_FRAGMENT";
                if (userInfo.getString("userRole","student").equals("teacher")){
                    fab.show();
                }else fab.hide();
                break;
            case R.id.nav_homework:
                ft.replace(R.id.container,homeWorkFrag);
                setTitle(getString(R.string.strHomework));
                currentFragment = "HOMEWORK_FRAGMENT";
                if (userInfo.getString("userRole","student").equals("teacher")){
                    fab.show();
                }else fab.hide();
                break;
            case R.id.nav_session:
                ft.replace(R.id.container,sessionFrag);
                setTitle(getString(R.string.strSession));
                currentFragment = "SESSION_FRAGMENT";
                if (userInfo.getString("userRole","student").equals("teacher")){
                    fab.show();
                }else fab.hide();
                break;
            /*case R.id.nav_marks:
                ft.replace(R.id.container,marksFrag);
                setTitle(getString(R.string.strMarks));
                currentFragment = "MARKS_FRAGMENT";
                fab.hide();
                break;*/
            case R.id.nav_calendar:
                ft.replace(R.id.container,calendarFragment);
                currentFragment = "CALENDAR_FRAGMENT";
                if (userInfo.getString("userRole","student").equals("teacher")){
                    fab.show();
                }else fab.hide();
                break;
            case R.id.nav_chat:
                ft.replace(R.id.container,chatFragment);
                currentFragment = "CHAT_FRAGMENT";
                setTitle("Chat");
                fab.hide();
                break;
        }ft.commit();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
    @Override
    protected void onResume() {
        super.onResume();
        checkUserExistence();
    }
}

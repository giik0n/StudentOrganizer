package com.babylone.alex.studentorganizer;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.widget.Toast;

import com.babylone.alex.studentorganizer.Adapters.SessionAdapter;
import com.babylone.alex.studentorganizer.Classes.CalendarDay;
import com.babylone.alex.studentorganizer.Classes.Homework;
import com.babylone.alex.studentorganizer.Classes.Lesson;
import com.babylone.alex.studentorganizer.Classes.Mark;
import com.babylone.alex.studentorganizer.Classes.PieObject;
import com.babylone.alex.studentorganizer.Classes.Session;
import com.babylone.alex.studentorganizer.Fragments.SessionFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Organizer";
    public static final String TABLE_LESSONS = "Lessons";
    public static final String TABLE_HOMEWORK = "Homework";
    public static final String TABLE_SESSION = "Session";
    public static final String TABLE_MARKS = "Marks";
    public static final String TABLE_CALENDAR = "Calendar";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DAY = "day";
    public static final String KEY_POSITION = "position";
    public static final String KEY_VARIATION = "variation";
    public static final String KEY_LESSON = "lesson";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DATE = "date";
    public static final String KEY_DONE = "done";
    public static final String KEY_TYPE = "type";
    public static final String KEY_MARK = "mark";
    public static final String KEY_CAB= "cab";
    public static final String KEY_TIME= "time";

    Context context;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference sessionRef = database.getReference().child("session");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override


    public void onCreate(SQLiteDatabase db) {

        String[] create_tables = {
                "CREATE TABLE "+TABLE_LESSONS+" ("+KEY_ID+
                        " INTEGER PRIMARY KEY AUTOINCREMENT,"+KEY_POSITION+" INTEGER,"+KEY_NAME+" TEXT," + KEY_DAY+" TEXT,"+
                        KEY_VARIATION+" TEXT,"+KEY_CAB+" TEXT)",
                "CREATE TABLE "+TABLE_HOMEWORK+" ("+KEY_ID+
                        " INTEGER PRIMARY KEY AUTOINCREMENT,"+KEY_LESSON+" TEXT," + KEY_DESCRIPTION +
                        " TEXT,"+ KEY_DATE+" TEXT,"+ KEY_DONE +" TEXT)",
                "CREATE TABLE "+TABLE_SESSION+" ("+KEY_ID+
                        " INTEGER PRIMARY KEY AUTOINCREMENT,"+KEY_LESSON+" TEXT," + KEY_TYPE+
                        " TEXT,"+ KEY_DATE+" TEXT,"+KEY_TIME+" TEXT,"+KEY_CAB+" TEXT)",
                "CREATE TABLE "+TABLE_MARKS+" ("+KEY_ID+
                        " INTEGER PRIMARY KEY AUTOINCREMENT,"+KEY_LESSON+" TEXT," + KEY_MARK+
                        " TEXT,"+ KEY_DATE+" TEXT)",
                "CREATE TABLE "+TABLE_CALENDAR+" ("+KEY_ID+
                        " INTEGER PRIMARY KEY AUTOINCREMENT,"+KEY_NAME+" TEXT," + KEY_DESCRIPTION+
                        " TEXT,"+ KEY_DATE+" TEXT,"+KEY_TIME+" TEXT)"
        };

        for (int i =0;i<create_tables.length;i++){
            db.execSQL(create_tables[i]);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public  void addLesson(Lesson lesson) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.putNull(KEY_ID);
        values.put(KEY_POSITION,lesson.getPosition());
        values.put(KEY_NAME,lesson.getName());
        values.put(KEY_DAY,lesson.getDay());
        values.put(KEY_VARIATION,lesson.getVariation());
        values.put(KEY_CAB,lesson.getCab());
        db.insert(TABLE_LESSONS,null,values);
        db.close();
    }
    public List<Lesson> getLessonsByDay(String day, String var){
        List<Lesson> lessons = new ArrayList<>();
        String sqlQuery = "SELECT * FROM " + TABLE_LESSONS + " WHERE "+KEY_DAY +" = \""+ day+"\" AND "+KEY_VARIATION+" = \""+var+"\" ORDER BY position ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery,null);
        if(cursor.moveToFirst()){
            do{
                Lesson lesson = new Lesson(
                        cursor.getString(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));
                lessons.add(lesson);
            }while(cursor.moveToNext());
        }
        return lessons;
    }
    public void deleteLesson(Lesson lesson){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LESSONS,KEY_ID+" =?",new String[]{String.valueOf(lesson.getId())});
        db.close();
    }
    public List<String> getUniqueLessons(){
        List<String> lessons = new ArrayList<>();
        String sqlQuery = "SELECT DISTINCT "+ KEY_NAME +" FROM " + TABLE_LESSONS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery,null);
        if(cursor.moveToFirst()){
            do{
                lessons.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }
        return lessons;
    }
    public  void addHomework(Homework homework) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.putNull(KEY_ID);
        values.put(KEY_LESSON,homework.getLesson());
        values.put(KEY_DESCRIPTION,homework.getDescription());
        values.put(KEY_DATE,homework.getDate());
        values.put(KEY_DONE,homework.getIsDone());
        db.insert(TABLE_HOMEWORK,null,values);
        db.close();
    }
    public List<Homework> getAllHomework(){
        List<Homework> homeworks = new ArrayList<>();
        String sqlQuery = "SELECT * FROM " + TABLE_HOMEWORK;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery,null);
        if(cursor.moveToFirst()){
            do{
                Homework homework = new Homework(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4));
                homeworks.add(homework);
            }while(cursor.moveToNext());
        }
        return homeworks;
    }
    public void updateHomeworkDone(Homework homework){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID,homework.getId());
        values.put(KEY_LESSON,homework.getLesson());
        values.put(KEY_DESCRIPTION,homework.getDescription());
        values.put(KEY_DATE, homework.getDate());
        values.put(KEY_DONE,homework.getIsDone());
        db.update(TABLE_HOMEWORK,values,KEY_ID+" =?",new String[]{String.valueOf(homework.getId())});
        db.close();
    }
    public void removeDoneHomework(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_HOMEWORK+" WHERE " +KEY_DONE +" = \"true\"");
        db.close();
    }
    public List<Session> getSession(){
        List<Session> sessions = new ArrayList<>();
        String sqlQuery = "SELECT * FROM " + TABLE_SESSION + " ORDER BY date("+KEY_DATE+") ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery,null);
        if(cursor.moveToFirst()){
            do{
                Session session = new Session(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));
                sessions.add(session);
            }while(cursor.moveToNext());
        }
        return sessions;
    }
    public  void addSession(Session session) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.putNull(KEY_ID);
        values.put(KEY_LESSON,session.getLesson());
        values.put(KEY_TYPE,session.getType());
        values.put(KEY_DATE,session.getDate());
        values.put(KEY_TIME,session.getTime());
        values.put(KEY_CAB,session.getClassroom());
        db.insert(TABLE_SESSION,null,values);
        db.close();
    }
    public void deleteSession(Session session){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SESSION,KEY_ID+" =?",new String[]{String.valueOf(session.getId())});
        db.close();
    }
    public List<String> getUniqueSession(){
        List<String> session = new ArrayList<>();
        String sqlQuery = "SELECT DISTINCT "+ KEY_LESSON +" FROM " + TABLE_SESSION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery,null);
        if(cursor.moveToFirst()){
            do{
                session.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }
        return session;
    }
    public void deleteMark(Mark mark){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MARKS,KEY_ID+" =?",new String[]{String.valueOf(mark.getId())});
        db.close();
    }
    public  void addMark(Mark mark) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.putNull(KEY_ID);
        values.put(KEY_LESSON,mark.getLesson());
        values.put(KEY_MARK,mark.getMark());
        values.put(KEY_DATE,mark.getDate());
        db.insert(TABLE_MARKS,null,values);
        db.close();
    }
    public ArrayList<PieObject> getPieDate(){
        ArrayList<PieObject> pie = new ArrayList<>();
        String sqlQuery = "SELECT DISTINCT "+KEY_LESSON+", avg("+KEY_MARK+") FROM "+TABLE_MARKS+" GROUP BY "+KEY_LESSON;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery,null);
        if(cursor.moveToFirst()){
            do{
                int color;
                float tmp = Float.parseFloat(cursor.getString(1));
                if(tmp<3){
                    color = Color.parseColor("#ff6061");
                }else if(tmp>=3 && tmp<4){
                    color = Color.parseColor("#f9a55c");
                }else{
                    color = Color.parseColor("#6acc68");
                }

                PieObject obj = new PieObject(
                        cursor.getString(0),
                        cursor.getFloat(1),
                        color);
                pie.add(obj);
            }while(cursor.moveToNext());
        }
        return pie;
    }
    public List<Mark> getMarksByLesson(String lesson){
        List<Mark> marks = new ArrayList<>();
        String sqlQuery = "SELECT * FROM " + TABLE_MARKS + " WHERE "+KEY_LESSON+" = '"+lesson+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery,null);
        if(cursor.moveToFirst()){
            do{
                Mark mark = new Mark(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3));
                marks.add(mark);
            }while(cursor.moveToNext());
        }
        return marks;
    }
    public List<CalendarDay> getDayByDate(String date){
        List<CalendarDay> days = new ArrayList<>();
        String sqlQuery = "SELECT * FROM " + TABLE_CALENDAR + " WHERE "+KEY_DATE+" = '"+date+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery,null);
        if(cursor.moveToFirst()){
            do{
                CalendarDay day = new CalendarDay(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4));
                days.add(day);
            }while(cursor.moveToNext());
        }
        return days;
    }
    public List<CalendarDay> getDayBetween(String date1, String date2){
        List<CalendarDay> days = new ArrayList<>();
        String sqlQuery = "SELECT * FROM " + TABLE_CALENDAR + " WHERE "+KEY_DATE+" BETWEEN '"+date1+"' AND '"+date2+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery,null);
        if(cursor.moveToFirst()){
            do{
                CalendarDay day = new CalendarDay(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4));
                days.add(day);
            }while(cursor.moveToNext());
        }
        return days;
    }
    public void deleteDay(CalendarDay day){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CALENDAR,KEY_ID+" =?",new String[]{String.valueOf(day.getId())});
        db.close();
    }
    public  void addDay(CalendarDay day) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.putNull(KEY_ID);
        values.put(KEY_NAME,day.getName());
        values.put(KEY_DESCRIPTION,day.getDescription());
        values.put(KEY_DATE,day.getDate());
        values.put(KEY_TIME,day.getTime());
        db.insert(TABLE_CALENDAR,null,values);
        db.close();
    }
    public ArrayList<String> getUniqueDays(){
        ArrayList<String> lessons = new ArrayList<>();
        String sqlQuery = "SELECT DISTINCT "+ KEY_DATE +" FROM " + TABLE_CALENDAR;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery,null);
        if(cursor.moveToFirst()){
            do{
                lessons.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }
        return lessons;
    }

    public void deleteHomework(Homework homework) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HOMEWORK,KEY_ID+" =?",new String[]{String.valueOf(homework.getId())});
        db.close();
    }

    public ArrayList<Session> getSessionFromFirebase(final SessionFragment f){
        final ArrayList<Session> arrayList = new ArrayList<>();
        sessionRef.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    arrayList.add(new Session(//додавання елементу
                            data.getKey().toString(),
                            data.child("lesson").getValue().toString(),
                            data.child("type").getValue().toString(),
                            data.child("date").getValue().toString(),
                            data.child("time").getValue().toString(),
                            data.child("classroom").getValue().toString()));
                    f.update();
                    Toast.makeText(f.getContext(), "Loaded", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return arrayList;
    }
}

package com.babylone.alex.studentorganizer.Classes;

/**
 * Created by Alex on 26.03.2018.
 */

public class Session {

    int id;
    String date, lesson, time, classroom, type;

    public Session(int id, String lesson,String type, String date, String time, String classroom ) {
        this.id = id;
        this.date = date;
        this.lesson = lesson;
        this.time = time;
        this.classroom = classroom;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

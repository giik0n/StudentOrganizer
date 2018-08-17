package com.babylone.alex.studentorganizer.Classes;

/**
 * Created by Alex on 01.04.2018.
 */

public class Mark {
    int id;
    String lesson, mark, date;

    public Mark(int id, String lesson, String mark, String date) {
        this.id = id;
        this.lesson = lesson;
        this.mark = mark;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

package com.babylone.alex.studentorganizer.Classes;

/**
 * Created by Alex on 16.03.2018.
 */

public class Homework {
    int id;
    String lesson, description, date, isDone;

    public Homework(int id, String lesson, String description, String date, String isDone) {
        this.id = id;
        this.lesson = lesson;
        this.description = description;
        this.date = date;
        this.isDone = isDone;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIsDone() {
        return isDone;
    }

    public void setIsDone(String isDone) {
        this.isDone = isDone;
    }
}

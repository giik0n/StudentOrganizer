package com.babylone.alex.studentorganizer.Classes;

/**
 * Created by Alex on 02.04.2018.
 */

public class CalendarDay {
    String id;
    String name, description, date, time;

    public CalendarDay(String id, String name, String description, String date, String time) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

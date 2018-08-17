package com.babylone.alex.studentorganizer.Classes;

/**
 * Created by Alex on 08.03.2018.
 */

public class Lesson {
    private int id, position;
    private String name, day, variation, cab;

    public Lesson(int id, int position, String name, String day, String variation, String cab) {
        this.id = id;
        this.position = position;
        this.name = name;
        this.day = day;
        this.variation = variation;
        this.cab = cab;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getVariation() {
        return variation;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }

    public String getCab() {
        return cab;
    }

    public void setCab(String cab) {
        this.cab = cab;
    }
}

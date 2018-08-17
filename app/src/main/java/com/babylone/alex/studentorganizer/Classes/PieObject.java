package com.babylone.alex.studentorganizer.Classes;

/**
 * Created by Alex on 01.04.2018.
 */

public class PieObject {
    int color;
    float value;
    String name;

    public PieObject(String name, float value, int color) {
        this.color = color;
        this.value = value;
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

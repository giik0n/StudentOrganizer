package com.babylone.alex.studentorganizer.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.babylone.alex.studentorganizer.Classes.Homework;
import com.babylone.alex.studentorganizer.Classes.Session;
import com.babylone.alex.studentorganizer.DatabaseHelper;
import com.babylone.alex.studentorganizer.R;

import java.util.List;

/**
 * Created by Alex on 26.03.2018.
 */

public class SessionAdapter extends BaseAdapter {

    Activity activity;
    List<Session> list;
    LayoutInflater inflater;
    DatabaseHelper db;
    TextView lesson, date, type, time, classroom;
    String id;

    public SessionAdapter(Activity activity, List<Session> list) {
        this.activity = activity;
        this.list = list;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db = new DatabaseHelper(activity);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.layout,null);
        lesson = (TextView)view.findViewById(R.id.textViewLessonSession);
        date = (TextView)view.findViewById(R.id.textViewDateSession);
        type = (TextView)view.findViewById(R.id.textViewTypeSession);
        time = (TextView)view.findViewById(R.id.textViewTimeSession);
        classroom = (TextView)view.findViewById(R.id.textViewCabSession);

        lesson.setText(list.get(i).getLesson());
        date.setText(list.get(i).getDate());
        type.setText(list.get(i).getType());
        time.setText(list.get(i).getTime());
        classroom.setText(list.get(i).getClassroom());
        id = list.get(i).getId();

        return view;
    }
}

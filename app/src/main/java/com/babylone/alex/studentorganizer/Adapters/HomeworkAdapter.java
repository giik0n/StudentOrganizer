package com.babylone.alex.studentorganizer.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.babylone.alex.studentorganizer.Classes.Homework;
import com.babylone.alex.studentorganizer.DatabaseHelper;
import com.babylone.alex.studentorganizer.R;

import java.util.List;

public class HomeworkAdapter extends BaseAdapter {
    Activity activity;
    List<Homework>list;
    LayoutInflater inflater;
    DatabaseHelper db;
    TextView lesson, date, descripion;
    String id;

    public HomeworkAdapter(Activity activity, List<Homework> list) {
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
        view = inflater.inflate(R.layout.item_homewrok,null);
        lesson = (TextView) view.findViewById(R.id.textView3);
        date = (TextView) view.findViewById(R.id.textView4);
        descripion = (TextView) view.findViewById(R.id.textView5);
        lesson.setText(list.get(i).getLesson());
        date.setText(list.get(i).getDate());
        descripion.setText(list.get(i).getDescription());
        id = list.get(i).getId();
        return view;
    }
}

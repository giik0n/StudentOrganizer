package com.babylone.alex.studentorganizer.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.babylone.alex.studentorganizer.ChangeInterface;
import com.babylone.alex.studentorganizer.Classes.CalendarDay;
import com.babylone.alex.studentorganizer.DatabaseHelper;
import com.babylone.alex.studentorganizer.R;

import java.util.List;

/**
 * Created by Alex on 02.04.2018.
 */

public class CalendarAdapter extends BaseAdapter {

    Activity activity;
    List<CalendarDay> list;
    LayoutInflater inflater;
    DatabaseHelper db;

    TextView name, time, date, description;
    Boolean show = false;

    int id;
    public CalendarAdapter(Activity activity, List<CalendarDay> list, boolean show) {
        this.activity = activity;
        this.list = list;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.db = new DatabaseHelper(activity);
        this.show = show;
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
        view = inflater.inflate(R.layout.item_calendar, null);
        name = (TextView)view.findViewById(R.id.textView18);
        description = (TextView) view.findViewById(R.id.editText3);
        time = (TextView)view.findViewById(R.id.textView19);
        date = (TextView)view.findViewById(R.id.textView24);
        id = list.get(i).getId();
        name.setText(list.get(i).getName());
        description.setText(list.get(i).getDescription());
        time.setText(list.get(i).getTime());
        if(show){
            date.setText(list.get(i).getDate());
        }
        return view;
    }

}

package com.babylone.alex.studentorganizer.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.babylone.alex.studentorganizer.ChangeInterface;
import com.babylone.alex.studentorganizer.Classes.Mark;
import com.babylone.alex.studentorganizer.Classes.Session;
import com.babylone.alex.studentorganizer.DatabaseHelper;
import com.babylone.alex.studentorganizer.R;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Alex on 01.04.2018.
 */

public class MarksAdapter extends BaseAdapter{

    Activity activity;
    List<Mark> list;
    LayoutInflater inflater;
    DatabaseHelper db;
    TextView lesson, mark, date;
    ImageButton delete;
    ChangeInterface responder;
    int id;

    public MarksAdapter(Activity activity, List<Mark> list, ChangeInterface responder) {
        this.activity = activity;
        this.list = list;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db = new DatabaseHelper(activity);
        this.responder = responder;
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
        view = inflater.inflate(R.layout.item_mark,null);
        mark = (TextView) view.findViewById(R.id.textViewIdMark);
        date = (TextView) view.findViewById(R.id.textViewMarkSession);
        delete = (ImageButton)view.findViewById(R.id.deleteMarkButton);
        date.setText(list.get(i).getDate());
        mark.setText(list.get(i).getMark());
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteMark(new Mark(list.get(i).getId(), list.get(i).getLesson(), list.get(i).getMark(), list.get(i).getDate()));
                db.close();
                list.remove(i);
                responder.update();
                notifyDataSetChanged();
            }
        });
        return view;
    }
}

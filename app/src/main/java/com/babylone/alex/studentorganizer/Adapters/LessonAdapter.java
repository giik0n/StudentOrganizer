package com.babylone.alex.studentorganizer.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.babylone.alex.studentorganizer.Classes.Lesson;
import com.babylone.alex.studentorganizer.DatabaseHelper;
import com.babylone.alex.studentorganizer.R;

import java.util.List;

/**
 * Created by Alex on 10.03.2018.
 */

public class LessonAdapter extends BaseAdapter {

    Activity activity;
    List<Object> lstLessons;
    LayoutInflater inflater;
    TextView textViewLesson, textViewId, cab;
    ImageButton delBtn;
    DatabaseHelper db;
    public static final int LESSON_ITEM = 0;
    public static final int HEADER = 1;
    public LessonAdapter(Activity activity, List<Object> lstLessons) {
        this.activity = activity;
        this.lstLessons = lstLessons;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db = new DatabaseHelper(activity);

    }

    @Override
    public int getItemViewType(int position) {
        if(lstLessons.get(position) instanceof Lesson ) {
            return LESSON_ITEM;
        }else{
        return HEADER;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return lstLessons.size();
    }

    @Override
    public Object getItem(int i) {
        return lstLessons.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {

        if(view == null){
            switch (getItemViewType(i)){
                case LESSON_ITEM:
                    view = inflater.inflate(R.layout.item_lesson,null);
                    break;
            case HEADER:
                view = inflater.inflate(R.layout.item_lesson_header,null);
                break;
            }
        }

        switch (getItemViewType(i)){
            case LESSON_ITEM:
                textViewLesson = (TextView)view.findViewById(R.id.textViewLessonSession);
                textViewId = (TextView)view.findViewById(R.id.textViewId);
                textViewLesson.setText(((Lesson)lstLessons.get(i)).getName());
                textViewId.setText(Integer.toString(((Lesson)lstLessons.get(i)).getPosition()));
                cab = (TextView) view.findViewById(R.id.textView2);
                cab.setText(((Lesson)lstLessons.get(i)).getCab());
                break;
            case HEADER:
                TextView title = (TextView) view.findViewById(R.id.lessonHeader);
                title.setText(((String)lstLessons.get(i)));
                break;
        }
    return view;
    }
}

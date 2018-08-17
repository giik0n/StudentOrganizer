package com.babylone.alex.studentorganizer.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.babylone.alex.studentorganizer.Adapters.LessonAdapter;
import com.babylone.alex.studentorganizer.Classes.Lesson;
import com.babylone.alex.studentorganizer.DatabaseHelper;
import com.babylone.alex.studentorganizer.R;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class LessonsFragment extends Fragment {


    private SwipeMenuListView lessonsListView;

    DatabaseHelper db;
    List<Lesson> monday = new ArrayList<Lesson>();
    List<Lesson> tuesday = new ArrayList<Lesson>();
    List<Lesson> wednesday = new ArrayList<Lesson>();
    List<Lesson> thursday = new ArrayList<Lesson>();
    List<Lesson> friday = new ArrayList<Lesson>();
    List<Object> data;
    boolean numerator = true;
    Calendar calendar;

    String var;
    Button chis,znam;
    int day;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_WEEK);
        var = getString(R.string.numerator);
        db = new DatabaseHelper(getActivity());
        data = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_lessons, container, false);
        chis = (Button)view.findViewById(R.id.chisButton);
        znam = (Button)view.findViewById(R.id.znamButton);
        lessonsListView = (SwipeMenuListView)view.findViewById(R.id.lessonsListView);
        chis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                var = getString(R.string.numerator);
                chis.setTextColor(getResources().getColor(R.color.colorWhite));
                znam.setTextColor(getResources().getColor(R.color.colorWhiteSemi));
                numerator = true;
                refreshData();
            }
        });
        znam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                var = getString(R.string.denominator);
                znam.setTextColor(getResources().getColor(R.color.colorWhite));
                chis.setTextColor(getResources().getColor(R.color.colorWhiteSemi));
                numerator = false;
                refreshData();

            }
        });
        refreshData();
        return view;
    }

    private void refreshData(){
        data.clear();
        monday = db.getLessonsByDay(getString(R.string.monday),var);
        tuesday = db.getLessonsByDay(getString(R.string.tuesday),var);
        wednesday = db.getLessonsByDay(getString(R.string.wednesday),var);
        thursday = db.getLessonsByDay(getString(R.string.thursday),var);
        friday = db.getLessonsByDay(getString(R.string.friday),var);

        if (monday.size() != 0){
            data.add(getString(R.string.monday));
            for(int i =0; i<monday.size();i++){
                data.add(monday.get(i));
            }
        }
        if (tuesday.size() != 0) {
            data.add(getString(R.string.tuesday));
            for (int i = 0; i < tuesday.size(); i++) {
                data.add(tuesday.get(i));
            }
        }
        if (wednesday.size() != 0){
            data.add(getString(R.string.wednesday));
            for(int i =0; i<wednesday.size();i++){
                data.add(wednesday.get(i));
            }
        }
        if (thursday.size() != 0){
            data.add(getString(R.string.thursday));
            for(int i =0; i<thursday.size();i++){
                data.add(thursday.get(i));
            }
        }
        if (friday.size() != 0) {
            data.add(getString(R.string.friday));
            for (int i = 0; i < friday.size(); i++) {
                data.add(friday.get(i));
            }
        }

        final LessonAdapter adapter1 = new LessonAdapter(getActivity(), data);
        lessonsListView.setAdapter(adapter1);

        lessonsListView.post( new Runnable() {
            @Override
            public void run() {

                switch (day) {
                    case Calendar.TUESDAY:
                        lessonsListView.smoothScrollToPositionFromTop(monday.size()+1,0,500);
                        break;
                    case Calendar.WEDNESDAY:
                        lessonsListView.smoothScrollToPositionFromTop(monday.size() + tuesday.size()+2,0,500);
                        break;
                    case Calendar.THURSDAY:
                        lessonsListView.smoothScrollToPositionFromTop(monday.size() + tuesday.size() + wednesday.size()+3,0,500);
                        break;
                    case Calendar.FRIDAY:
                        lessonsListView.smoothScrollToPositionFromTop(monday.size() + tuesday.size() + wednesday.size() + thursday.size()+4,0,500);
                        break;
                    default:
                        break;
                }}
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                if(menu.getViewType() == 0){
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getContext().getApplicationContext());
                deleteItem.setBackground(getResources().getDrawable(R.drawable.delete_button));
                deleteItem.setWidth(170);
                deleteItem.setIcon(R.mipmap.ic_delete);
                menu.addMenuItem(deleteItem);
                }
            }
        };
        lessonsListView.setMenuCreator(creator);
        lessonsListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                    switch (index) {
                        case 0:
                            new AlertDialog.Builder(getActivity())
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle(R.string.deleting)
                                    .setMessage(((Lesson) data.get(position)).getName()+"\n"+getString(R.string.aYouSure))
                                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            db.deleteLesson((Lesson)data.get(position));
                                            data.remove(position);
                                            lessonsListView.setAdapter(adapter1);
                                        }
                                    })
                                    .setNegativeButton(R.string.nope, null)
                                    .show();
                            break;
                    }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    public boolean getNumerator(){
        return numerator;
    }
}

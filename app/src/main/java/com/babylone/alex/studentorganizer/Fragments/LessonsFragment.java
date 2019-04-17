package com.babylone.alex.studentorganizer.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.babylone.alex.studentorganizer.Adapters.LessonAdapter;
import com.babylone.alex.studentorganizer.Classes.Lesson;
import com.babylone.alex.studentorganizer.Classes.Session;
import com.babylone.alex.studentorganizer.DatabaseHelper;
import com.babylone.alex.studentorganizer.R;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public class LessonsFragment extends Fragment {


    private SwipeMenuListView lessonsListView;

    DatabaseHelper db;
    List<Lesson> monday = new ArrayList<Lesson>();
    List<Lesson> tuesday = new ArrayList<Lesson>();
    List<Lesson> wednesday = new ArrayList<Lesson>();
    List<Lesson> thursday = new ArrayList<Lesson>();
    List<Lesson> friday = new ArrayList<Lesson>();
    List<Lesson> saturnday = new ArrayList<Lesson>();
    DatabaseReference scheduleRef = FirebaseDatabase.getInstance().getReference().child("Schedule");
    SharedPreferences userInfo;
    String group;
    List<Object> arrayList;
    boolean numerator = true;
    Calendar calendar;
    SharedPreferences sp;
    String var;
    Button chis,znam;
    int day;
    LinearLayout linearLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_WEEK);
        var = getString(R.string.numerator);
        db = new DatabaseHelper(getActivity());
        arrayList = new ArrayList<>();
        sp = getActivity().getSharedPreferences("SettingsActivity", Context.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.fragment_lessons, container, false);
        lessonsListView = (SwipeMenuListView)view.findViewById(R.id.lessonsListView);
        linearLayout = view.findViewById(R.id.linearLayout);
        userInfo = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        group = userInfo.getString("userGroup","");

        chis = (Button)view.findViewById(R.id.chisButton);
        znam = (Button)view.findViewById(R.id.znamButton);
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

        final LessonAdapter adapter1 = new LessonAdapter(getActivity(), arrayList);
        scheduleRef.orderByChild("group").equalTo(group).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                monday.clear();
                tuesday.clear();
                wednesday.clear();
                thursday.clear();
                friday.clear();
                saturnday.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    if (data.child("week").getValue().toString().equals(var)) {

                        if (data.child("day").getValue().toString().equals(getString(R.string.monday))) {
                            monday.add(new Lesson(//додавання елементу
                                    data.getKey(),
                                    Integer.valueOf(data.child("position").getValue().toString()),
                                    data.child("lesson").getValue().toString() + "(" + data.child("type").getValue().toString() + ")",
                                    data.child("day").getValue().toString(),
                                    data.child("week").getValue().toString(),
                                    data.child("classroom").getValue().toString()));
                        } else if (data.child("day").getValue().toString().equals(getString(R.string.tuesday))) {
                            tuesday.add(new Lesson(//додавання елементу
                                    data.getKey(),
                                    Integer.valueOf(data.child("position").getValue().toString()),
                                    data.child("lesson").getValue().toString() + "(" + data.child("type").getValue().toString() + ")",
                                    data.child("day").getValue().toString(),
                                    data.child("week").getValue().toString(),
                                    data.child("classroom").getValue().toString()));
                        } else if (data.child("day").getValue().toString().equals(getString(R.string.wednesday))) {
                            wednesday.add(new Lesson(//додавання елементу
                                    data.getKey(),
                                    Integer.valueOf(data.child("position").getValue().toString()),
                                    data.child("lesson").getValue().toString() + "(" + data.child("type").getValue().toString() + ")",
                                    data.child("day").getValue().toString(),
                                    data.child("week").getValue().toString(),
                                    data.child("classroom").getValue().toString()));
                        } else if (data.child("day").getValue().toString().equals(getString(R.string.thursday))) {
                            thursday.add(new Lesson(//додавання елементу
                                    data.getKey(),
                                    Integer.valueOf(data.child("position").getValue().toString()),
                                    data.child("lesson").getValue().toString() + "(" + data.child("type").getValue().toString() + ")",
                                    data.child("day").getValue().toString(),
                                    data.child("week").getValue().toString(),
                                    data.child("classroom").getValue().toString()));
                        } else if (data.child("day").getValue().toString().equals(getString(R.string.friday))) {
                            friday.add(new Lesson(//додавання елементу
                                    data.getKey(),
                                    Integer.valueOf(data.child("position").getValue().toString()),
                                    data.child("lesson").getValue().toString() + "(" + data.child("type").getValue().toString() + ")",
                                    data.child("day").getValue().toString(),
                                    data.child("week").getValue().toString(),
                                    data.child("classroom").getValue().toString()));
                        } else if (data.child("day").getValue().toString().equals(getString(R.string.isSaturnday))) {
                            saturnday.add(new Lesson(//додавання елементу
                                    data.getKey(),
                                    Integer.valueOf(data.child("position").getValue().toString()),
                                    data.child("lesson").getValue().toString() + "(" + data.child("type").getValue().toString() + ")",
                                    data.child("day").getValue().toString(),
                                    data.child("week").getValue().toString(),
                                    data.child("classroom").getValue().toString()));
                        }
                    }



                }
                if (monday.size() != 0){
                    Collections.sort(monday);
                    arrayList.add(getString(R.string.monday));
                    for(int i =0; i<monday.size();i++){
                        arrayList.add(monday.get(i));
                    }
                }
                if (tuesday.size() != 0) {
                    Collections.sort(tuesday);
                    arrayList.add(getString(R.string.tuesday));
                    for (int i = 0; i < tuesday.size(); i++) {
                        arrayList.add(tuesday.get(i));
                    }
                }
                if (wednesday.size() != 0){
                    Collections.sort(wednesday);
                    arrayList.add(getString(R.string.wednesday));
                    for(int i =0; i<wednesday.size();i++){
                        arrayList.add(wednesday.get(i));
                    }
                }
                if (thursday.size() != 0){
                    Collections.sort(thursday);
                    arrayList.add(getString(R.string.thursday));
                    for(int i =0; i<thursday.size();i++){
                        arrayList.add(thursday.get(i));
                    }
                }
                if (friday.size() != 0) {
                    Collections.sort(friday);
                    arrayList.add(getString(R.string.friday));
                    for (int i = 0; i < friday.size(); i++) {
                        arrayList.add(friday.get(i));
                    }
                }
                if (saturnday.size() != 0) {
                    Collections.sort(saturnday);
                    arrayList.add(getString(R.string.isSaturnday));
                    for (int i = 0; i < saturnday.size(); i++) {
                        arrayList.add(saturnday.get(i));
                    }
                }

                lessonsListView.setAdapter(adapter1);

                lessonsListView.post( new Runnable() {
                    @Override
                    public void run() {

                        switch (day) {
                            case Calendar.TUESDAY:
                                if (tuesday.size()>0)lessonsListView.smoothScrollToPositionFromTop(monday.size()+1,0,500);
                                break;
                            case Calendar.WEDNESDAY:
                                if (wednesday.size()>0)lessonsListView.smoothScrollToPositionFromTop(monday.size() + tuesday.size()+2,0,500);
                                break;
                            case Calendar.THURSDAY:
                                if (thursday.size()>0)lessonsListView.smoothScrollToPositionFromTop(monday.size() + tuesday.size() + wednesday.size()+3,0,500);
                                break;
                            case Calendar.FRIDAY:
                                if (friday.size()>0)lessonsListView.smoothScrollToPositionFromTop(monday.size() + tuesday.size() + wednesday.size() + thursday.size()+4,0,500);
                                break;
                            case Calendar.SATURDAY:
                                if (sp.getString("isSaturnday","false").equals("true")) {
                                    if (saturnday.size()>0)lessonsListView.smoothScrollToPositionFromTop(monday.size() + tuesday.size() + wednesday.size() + thursday.size() + saturnday.size() + 5, 0, 500);
                                }
                                break;
                            default:
                                break;
                        }}
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                if(menu.getViewType() == 0){
                    if (userInfo.getString("userRole","student").equals("teacher")){
                        SwipeMenuItem deleteItem = new SwipeMenuItem(
                                getContext().getApplicationContext());
                        deleteItem.setBackground(getResources().getDrawable(R.drawable.delete_button));
                        deleteItem.setWidth(170);
                        deleteItem.setIcon(R.mipmap.ic_delete);
                        menu.addMenuItem(deleteItem);
                    }
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
                                    .setMessage(((Lesson) arrayList.get(position)).getName()+"\n"+getString(R.string.aYouSure))
                                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            scheduleRef.child(((Lesson) arrayList.get(position)).getId()).removeValue();
                                            arrayList.remove(position);
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
        if (sp.getString("twoWeeks", "false").equals("false")){
            linearLayout.setVisibility(View.INVISIBLE);
            lessonsListView.setPadding(0,0,0,0);
        }else{
            linearLayout.setVisibility(View.VISIBLE);
            lessonsListView.setPadding(0,(int)(50 * getResources().getDisplayMetrics().density + 0.5f),0,0);
        }
    }

    public boolean getNumerator(){
        return numerator;
    }
}

package com.babylone.alex.studentorganizer.Fragments;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.babylone.alex.studentorganizer.Adapters.HomeworkAdapter;
import com.babylone.alex.studentorganizer.Adapters.SessionAdapter;
import com.babylone.alex.studentorganizer.Classes.Homework;
import com.babylone.alex.studentorganizer.Classes.Session;
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

import static android.content.Context.ALARM_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeWorkFragment extends Fragment {


    public HomeWorkFragment() {
        // Required empty public constructor
    }
    ArrayList<Homework> arrayList = new ArrayList<>();
    SwipeMenuListView list;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference homeworkRef = database.getReference().child("Homework");
    SharedPreferences userInfo;
    String group;
    HomeworkAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_work, container, false);
        list = view.findViewById(R.id.homeworkList);
        userInfo = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        group = userInfo.getString("userGroup","");

        refreshData();
        return view;
    }

    private void refreshData() {
        adapter = new HomeworkAdapter(getActivity(),arrayList);
        homeworkRef.orderByChild("group").equalTo(group).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    arrayList.add(new Homework(//додавання елементу
                            data.getKey(),
                            data.child("lesson").getValue().toString(),
                            data.child("text").getValue().toString(),
                            data.child("date").getValue().toString(),
                            "false"));
                }
                list.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        list.setAdapter(adapter);

        list.setMenuCreator(new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                if(menu.getViewType() == 0){
                    SwipeMenuItem editItem = new SwipeMenuItem(
                            getContext().getApplicationContext());
                    editItem.setBackground(getResources().getDrawable(R.drawable.nofity_button));
                    editItem.setWidth(170);
                    editItem.setIcon(R.mipmap.ic_notification_white);
                    editItem.setTitleSize(18);
                    menu.addMenuItem(editItem);
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
        });
        list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        View view = getLayoutInflater().inflate(R.layout.notify_layout,null);
                        final TextView time = (TextView) view.findViewById(R.id.notifyTime);
                        final TextView date = (TextView) view.findViewById(R.id.notifyDate);
                        Button button = (Button) view.findViewById(R.id.notifyButton1);
                        builder.setView(view);
                        final AlertDialog dialog = builder.create();
                        dialog.setTitle(R.string.addNotification);
                        dialog.show();
                        time.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                Calendar mcurrentTime = Calendar.getInstance();
                                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                                int minute = mcurrentTime.get(Calendar.MINUTE);
                                TimePickerDialog mTimePicker;
                                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                        String hour = String.valueOf(selectedHour),
                                                minute = String.valueOf(selectedMinute);

                                        if(selectedHour<10){
                                            hour = "0"+String.valueOf(selectedHour);
                                        }

                                        if(selectedHour>12){
                                            hour = String.valueOf(Integer.valueOf(hour));
                                        }

                                        if(selectedMinute<10){
                                            minute = "0"+String.valueOf(selectedMinute);
                                        }

                                        time.setText( hour + ":" + minute);
                                    }
                                }, hour, minute, true);//Yes 24 hour time
                                mTimePicker.setTitle(R.string.chooseTime);
                                mTimePicker.show();

                            }
                        });

                        date.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Calendar mcurrentTime = Calendar.getInstance();
                                int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
                                int month = mcurrentTime.get(Calendar.MONTH);
                                int year = mcurrentTime.get(Calendar.YEAR);
                                DatePickerDialog mDatePicker;
                                mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                        String month = String.valueOf(i1+1);
                                        String day = String.valueOf(i2);
                                        if (i1 + 1 < 10) {
                                            month = "0"+String.valueOf(i1+1);
                                        }
                                        if(i2<10){
                                            day = "0"+String.valueOf(i2);
                                        }
                                        date.setText(i+"-"+month+"-"+day);
                                    }
                                }, year, month, day);
                                mDatePicker.setTitle(R.string.chooseDate);
                                mDatePicker.show();
                            }
                        });

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(!time.getText().toString().equals("") && !date.getText().toString().equals("")){
                                    String dateText = date.getText().toString();
                                    int day = Integer.valueOf(dateText.split("-")[2]);
                                    int month = Integer.valueOf(dateText.split("-")[1]);
                                    int year = Integer.valueOf(dateText.split("-")[0]);

                                    String timeText = time.getText().toString();
                                    AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(ALARM_SERVICE);
                                    int hours = Integer.valueOf(timeText.split(":")[0]);
                                    int minutes = Integer.valueOf(timeText.split(":")[1]);
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.set(Calendar.DAY_OF_MONTH, day);
                                    calendar.set(Calendar.MONTH, month-1);
                                    calendar.set(Calendar.YEAR, year);
                                    calendar.set(Calendar.HOUR_OF_DAY, hours);
                                    calendar.set(Calendar.MINUTE, minutes);
                                    calendar.set(Calendar.SECOND,0);
                                    calendar.set(Calendar.MILLISECOND,0);
                                    Toast.makeText(getActivity(), "Додано\n"+calendar.getTime(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent("singh.ajit.action.DISPLAY_NOTIFICATION");
                                    intent.putExtra("Title","Homework");
                                    intent.putExtra("Text",arrayList.get(position).getLesson());
                                    PendingIntent broadcast = PendingIntent.getBroadcast(getActivity(),100,intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),broadcast);
                                }
                            }
                        });

                        break;
                    case 1:

                        new AlertDialog.Builder(getActivity())
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle(R.string.deleting)
                                .setMessage(((Homework)arrayList.get(position)).getLesson()+"\n"+getString(R.string.aYouSure))
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        homeworkRef.child(arrayList.get(position).getId()).removeValue();
                                        list.setAdapter(adapter);
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
}

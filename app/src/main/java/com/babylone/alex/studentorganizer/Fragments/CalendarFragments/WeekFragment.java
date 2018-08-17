package com.babylone.alex.studentorganizer.Fragments.CalendarFragments;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.babylone.alex.studentorganizer.Adapters.CalendarAdapter;
import com.babylone.alex.studentorganizer.Classes.CalendarDay;
import com.babylone.alex.studentorganizer.Classes.Lesson;
import com.babylone.alex.studentorganizer.DatabaseHelper;
import com.babylone.alex.studentorganizer.R;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;


public class WeekFragment extends Fragment {

    ImageButton back, forward;
    Calendar calendar;
    SimpleDateFormat format, day;
    DatabaseHelper db;
    SwipeMenuListView list;
    List<CalendarDay> data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week, container, false);
        back = (ImageButton) view.findViewById(R.id.weekBack);
        forward = (ImageButton) view.findViewById(R.id.weekForward);
        list = (SwipeMenuListView)view.findViewById(R.id.weekList) ;
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        format = new SimpleDateFormat("yyyy-MM-dd");
        day = new SimpleDateFormat("EE dd");
        db = new DatabaseHelper(getActivity());

        refresh();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DAY_OF_MONTH,-7);
                refresh();
                String day1 = day.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_MONTH,6);
                String day2 = day.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_MONTH,-6);

                getActivity().setTitle(day1+" - "+day2);
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DAY_OF_MONTH,7);
                refresh();
                String day1 = day.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_MONTH,6);
                String day2 = day.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_MONTH,-6);

                getActivity().setTitle(day1+" - "+day2);
            }
        });

        return view;
    }

    void refresh(){
        String date1 = format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH,7);
        String date2 = format.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH,-7);


        data = db.getDayBetween(date1, date2);
        final CalendarAdapter adapter = new CalendarAdapter(getActivity(),data,true);
        list.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                if(menu.getViewType() == 0){
                    SwipeMenuItem editItem = new SwipeMenuItem(
                            getContext().getApplicationContext());
                    //editItem.setBackground(new ColorDrawable(Color.rgb(255, 153,51)));
                    editItem.setBackground(getResources().getDrawable(R.drawable.nofity_button));
                    editItem.setWidth(170);
                    editItem.setIcon(R.mipmap.ic_notification_white);
                    editItem.setTitleSize(18);
                    menu.addMenuItem(editItem);

                    SwipeMenuItem deleteItem = new SwipeMenuItem(
                            getContext().getApplicationContext());
                    //deleteItem.setBackground(new ColorDrawable(Color.rgb(255,51, 0)));
                    deleteItem.setBackground(getResources().getDrawable(R.drawable.delete_button));
                    deleteItem.setWidth(170);
                    deleteItem.setIcon(R.mipmap.ic_delete);
                    menu.addMenuItem(deleteItem);
                }
            }
        };

        list.setMenuCreator(creator);
        list.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                list.smoothOpenMenu(position);
            }

            @Override
            public void onSwipeEnd(int position) {

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
                        dialog.setTitle(getString(R.string.addNotification));
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
                                mTimePicker.setTitle(getString(R.string.chooseTime));
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
                                mDatePicker.setTitle(getString(R.string.chooseDate));
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
                                    Toast.makeText(getActivity(), getString(R.string.added), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent("singh.ajit.action.DISPLAY_NOTIFICATION");
                                    intent.putExtra("Title","Notification");
                                    intent.putExtra("Text",data.get(position).getName());
                                    PendingIntent broadcast = PendingIntent.getBroadcast(getActivity(),100,intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),broadcast);
                                }
                            }
                        });
                        break;
                    case 1:
                        new AlertDialog.Builder(getActivity())
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle(getString(R.string.deleting))
                                .setMessage(((CalendarDay)data.get(position)).getName()+"\n"+getString(R.string.aYouSure))
                                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        db.deleteDay((CalendarDay)data.get(position));
                                        data.remove(position);
                                        list.setAdapter(adapter);
                                    }
                                })
                                .setNegativeButton(getString(R.string.nope), null)
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
        refresh();
    }


}

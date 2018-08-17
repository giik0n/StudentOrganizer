package com.babylone.alex.studentorganizer.Fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.babylone.alex.studentorganizer.Adapters.ViewPagerAdapter;
import com.babylone.alex.studentorganizer.Classes.CustomViewPager;
import com.babylone.alex.studentorganizer.Fragments.CalendarFragments.DayFragment;
import com.babylone.alex.studentorganizer.Fragments.CalendarFragments.MonthFragment;
import com.babylone.alex.studentorganizer.Fragments.CalendarFragments.WeekFragment;
import com.babylone.alex.studentorganizer.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {
        private TabLayout tabLayout;
        private CustomViewPager viewPager;
        private ViewPagerAdapter adapter;
                Calendar calendar;
                SimpleDateFormat  day, week, month;


    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendar = Calendar.getInstance();
        day = new SimpleDateFormat("EEEE dd");
        week = new SimpleDateFormat("EE dd");
        month = new SimpleDateFormat("MMMM");

        getActivity().setTitle(day.format(calendar.getTime()));

        tabLayout = (TabLayout)view.findViewById(R.id.tab_layout);
        viewPager = (CustomViewPager) view.findViewById(R.id.viewpager);
        viewPager.setPagingEnabled(false);
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new DayFragment(), "Today");
        adapter.addFragment(new WeekFragment(), "Week");
        adapter.addFragment(new MonthFragment(), "Month");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        getActivity().setTitle(day.format(calendar.getTime()));
                        break;
                    case 1:
                        calendar = Calendar.getInstance();
                        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                        String day1 = week.format(calendar.getTime());
                        calendar.add(Calendar.DAY_OF_MONTH,6);
                        String day2 = week.format(calendar.getTime());
                        calendar.add(Calendar.DAY_OF_MONTH,-6);
                        getActivity().setTitle(day1+" - "+day2);
                        calendar = Calendar.getInstance();
                        break;
                    case 2:
                        getActivity().setTitle(month.format(calendar.getTime()));
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

}

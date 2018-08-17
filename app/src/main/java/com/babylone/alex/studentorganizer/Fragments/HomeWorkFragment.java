package com.babylone.alex.studentorganizer.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.babylone.alex.studentorganizer.Adapters.HomeworkAdapter;
import com.babylone.alex.studentorganizer.Classes.Homework;
import com.babylone.alex.studentorganizer.Classes.Lesson;
import com.babylone.alex.studentorganizer.DatabaseHelper;
import com.babylone.alex.studentorganizer.R;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;


public class HomeWorkFragment extends Fragment {
    List<Homework> data = new ArrayList<>();
    DatabaseHelper db;
    ListView list;
    ImageButton removeButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_work, container, false);
        list = (ListView) view.findViewById(R.id.homeworkListView);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        removeButton = (ImageButton) view.findViewById(R.id.homeworkImageButton);
        db = new DatabaseHelper(getActivity());
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.removeDoneHomework();
                refreshData();
            }
        });
        refreshData();
        return view;
    }

    private void refreshData() {
        data = db.getAllHomework();
        final HomeworkAdapter adapter = new HomeworkAdapter(getActivity(), data);
        list.setAdapter(adapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

}



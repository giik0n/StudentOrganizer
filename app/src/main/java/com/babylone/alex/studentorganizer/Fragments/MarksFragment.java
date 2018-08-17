package com.babylone.alex.studentorganizer.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.babylone.alex.studentorganizer.Add.addMark;
import com.babylone.alex.studentorganizer.Add.addMarkSession;
import com.babylone.alex.studentorganizer.Classes.PieObject;
import com.babylone.alex.studentorganizer.DatabaseHelper;
import com.babylone.alex.studentorganizer.R;
import com.babylone.alex.studentorganizer.SelectedChartActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class MarksFragment extends Fragment {

    public MarksFragment() {
    }
    PieChart chart;
    DatabaseHelper db;
    FloatingActionButton fab1, fab2;
    FloatingActionMenu fam;
    FrameLayout fl;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marks, container, false);
        fl = (FrameLayout)view.findViewById(R.id.marksLayout) ;
        db = new DatabaseHelper(getActivity());
        fab1 = (FloatingActionButton)view.findViewById(R.id.material_design_floating_action_menu_item1);
        fab2 = (FloatingActionButton)view.findViewById(R.id.material_design_floating_action_menu_item2);
        fam = (FloatingActionMenu)view.findViewById(R.id.material_design_android_floating_action_menu);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fam.close(true);
                startActivity(new Intent(getActivity(),addMark.class));
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fam.close(true);
                startActivity(new Intent(getActivity(),addMarkSession.class));
            }
        });


        chart = (PieChart) view.findViewById(R.id.chart);
        chart.setHoleRadius(60f);
        chart.setTransparentCircleAlpha(0);
        chart.setCenterText(getString(R.string.strMarks));
        chart.setRotationEnabled(true);
        chart.getDescription().setEnabled(false);

        addDataSet();

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                fam.close(true);
                PieEntry pe = (PieEntry) e;
                Intent intent = new Intent(getActivity(), SelectedChartActivity.class);
                intent.putExtra("Lesson", pe.getLabel());
                intent.putExtra("Avg", pe.getValue());
                startActivity(intent);
            }
            @Override
            public void onNothingSelected() {
            }
        });
        return view;
    }

    private void addDataSet() {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        ArrayList<PieObject> pieObj = db.getPieDate();

        for (int i = 0; i<pieObj.size();i++){
            yEntrys.add(new PieEntry((pieObj.get(i).getValue()),pieObj.get(i).getName()));

            colors.add(pieObj.get(i).getColor());
        }
        PieDataSet pieDataSet = new PieDataSet(yEntrys,getString(R.string.lessons));
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextColor(Color.BLACK);

        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData = new PieData(pieDataSet);
        chart.setData(pieData);
        chart.animateY(1000);
        chart.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
        addDataSet();
    }
}

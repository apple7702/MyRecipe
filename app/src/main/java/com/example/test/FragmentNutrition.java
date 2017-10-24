package com.example.test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentNutrition.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentNutrition#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentNutrition extends Fragment {

    private PieChart mChart;
    private int id;
    private float[] materials;

    private DatabaseHelper dbHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);

        dbHelper = new DatabaseHelper(view.getContext());


        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        materials = dbHelper.getNutrition(id);
        ((TextView) view.findViewById(R.id.tv_calories)).setText(String.valueOf(materials[0] + " kcal"));
        ((TextView) view.findViewById(R.id.tv_fat)).setText(String.valueOf(materials[1] + " g"));
        ((TextView) view.findViewById(R.id.tv_carbohydrate)).setText(String.valueOf(materials[2] + " g"));
        ((TextView) view.findViewById(R.id.tv_protein)).setText(String.valueOf(materials[3] + " g"));
        ((TextView) view.findViewById(R.id.tv_cholesterol)).setText(String.valueOf(materials[4] + " mg"));
        ((TextView) view.findViewById(R.id.tv_sodium)).setText(String.valueOf(materials[5] + " mg"));


        ((TextView) view.findViewById(R.id.tv_fat_table)).setText(String.valueOf(materials[1]));
        ((TextView) view.findViewById(R.id.tv_carbohydrate_table)).setText(String.valueOf(materials[2]));
        ((TextView) view.findViewById(R.id.tv_protein_table)).setText(String.valueOf(materials[3]));


        mChart = (PieChart) view.findViewById(R.id.spread_pie_chart);
        PieData mPieData = getPieData(3, 100);
        showChart(mChart, mPieData);


        return view;
    }

    private void showChart(PieChart pieChart, PieData pieData) {

        pieChart.setHoleRadius(60f);  //半径
        pieChart.setTransparentCircleRadius(64f); // 半透明圈
        pieChart.setHoleRadius(0);  //实心圆

        pieChart.setDescription("");

        pieChart.setDrawCenterText(true);

        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90);
        pieChart.setRotationEnabled(true);

        pieChart.setUsePercentValues(true);
        pieChart.setData(pieData);
        Legend mLegend = pieChart.getLegend();
        mLegend.setEnabled(false);
        pieChart.animateXY(1000, 1000);
    }


    public PieData getPieData(int count, float range) {

        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容

        float quarterly1 = materials[3];
        float quarterly2 = materials[2];
        float quarterly3 = materials[1];
        xValues.add("Protein: "+materials[3]+"g");
        xValues.add("Carbohydrate: "+materials[2]+"g");
        xValues.add("Fat: "+materials[1]+"g");


        ArrayList<Entry> yValues = new ArrayList<>();  //yVals用来表示封装每个饼块的实际数据


        yValues.add(new Entry(quarterly1, 0));
        yValues.add(new Entry(quarterly2, 1));
        yValues.add(new Entry(quarterly3, 2));


        PieDataSet pieDataSet = new PieDataSet(yValues, ""/*显示在比例图上*/);
        pieDataSet.setValueTextSize(10);


        ArrayList<Integer> colors = new ArrayList<Integer>();

        colors.add(Color.rgb(53, 185, 200));
        colors.add(Color.rgb(152, 196, 41));
        colors.add(Color.rgb(129, 132, 132));

        pieDataSet.setColors(colors);


        PieData pieData = new PieData(xValues, pieDataSet);

        return pieData;
    }


}

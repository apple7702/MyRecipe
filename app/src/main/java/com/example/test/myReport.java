package com.example.test;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

public class myReport extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private PieChart mChart;
    private TextView tv_age, tv_weight, tv_height, tv_bmi, tv_bmr, tv_tdee, tv_protein, tv_carbohydrate, tv_fat;
    private String account_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_report);
        dbHelper = new DatabaseHelper(getApplicationContext());
        account_id = getSharedPreferences("config", Context.MODE_PRIVATE).getString("account_id", "");

        init();


        mChart = (PieChart) findViewById(R.id.spread_pie_chart);
        PieData mPieData = getPieData(3, 100);
        showChart(mChart, mPieData);

    }


    @Override
    protected void onResume() {
        super.onResume();
        initView();

    }

    public void init() {
        tv_age = (TextView) findViewById(R.id.age_user);
        tv_weight = (TextView) findViewById(R.id.weight_user);
        tv_height = (TextView) findViewById(R.id.height_user);
        tv_bmi = (TextView) findViewById(R.id.BMI_user);
        tv_bmr = (TextView) findViewById(R.id.BMR_user);
        tv_tdee = (TextView) findViewById(R.id.TDEE_user);

        tv_protein = (TextView) findViewById(R.id.tv_protein_table);
        tv_carbohydrate = (TextView) findViewById(R.id.tv_carbohydrate_table);
        tv_fat = (TextView) findViewById(R.id.tv_fat_table);


    }

    public void initView() {
        int paras[] = dbHelper.getPersonalParas(account_id);
        tv_age.setText(paras[1] + "");
        tv_weight.setText(paras[3] + "");
        tv_height.setText(paras[2] + "");


        tv_bmi.setText(String.valueOf(calBMI(paras[2], paras[3])));
        tv_bmr.setText(String.valueOf(calBMR(paras[3], paras[2], paras[1], paras[0])));
        tv_tdee.setText(String.valueOf(calOptimalWeight((double) paras[2])));

    }

    public double calBMI(int height, int weight) {
        double BMI;
        double dheight = (double) height / 100.0;
        BMI = ((double) weight) / (dheight * dheight);
        return BMI;
    }

    public double calBMR(int weight, int height, int age, int gender) {
        double BMR;
        if (gender == 0) {
            BMR = 13.7 * weight + 5 * height - 6.8 * age + 66;
        } else {
            BMR = 9.6 * weight + 1.8 * height - 4.7 * age + 655;
        }
        return BMR;
    }

    public double calOptimalWeight(double height) {
        height = height / 100.0;
        return 22.0 * height * height;

    }

    public static double calCalories(int[] para) {

        double calorie;
        if (para[4] == 0) {
            if (para[7] == 0) {
                if (para[0] == 1) {
                    calorie = (10 * para[3] + 6.25 * para[2] - 5 * para[1] - 161) * 1.2 - (para[5] - para[3]) * 7700 / para[6];
                } else {
                    calorie = (10 * para[3] + 6.25 * para[2] - 5 * para[1] + 5) * 1.2 - (para[5] - para[3]) * 7700 / para[6];
                }
            } else if (para[7] == 2) {
                if (para[0] == 1) {
                    calorie = (10 * para[3] + 6.25 * para[2] - 5 * para[1] - 161) * 1.3 - (para[5] - para[3]) * 7700 / para[6];
                } else {
                    calorie = (10 * para[3] + 6.25 * para[2] - 5 * para[1] + 5) * 1.3 - (para[5] - para[3]) * 7700 / para[6];
                }
            } else {
                if (para[0] == 1) {

                    calorie = (10 * para[3] + 6.25 * para[2] - 5 * para[1] - 161) * 1.75 - (para[5] - para[3]) * 7700 / para[6];
                } else {
                    calorie = (10 * para[3] + 6.25 * para[2] - 5 * para[1] + 5) * 1.75 - (para[5] - para[3]) * 7700 / para[6];
                }
            }
        } else {
            if (para[7] == 0) {
                if (para[0] == 1) {

                    calorie = (10 * para[3] + 6.25 * para[2] - 5 * para[1] - 161) * 1.2;
                } else {
                    calorie = (10 * para[3] + 6.25 * para[2] - 5 * para[1] + 5) * 1.2;
                }
            } else if (para[7] == 2) {
                if (para[0] == 1) {

                    calorie = (10 * para[3] + 6.25 * para[2] - 5 * para[1] - 161) * 1.3;
                } else {
                    calorie = (10 * para[3] + 6.25 * para[2] - 5 * para[1] + 5) * 1.3;
                }

            } else {
                if (para[0] == 1) {

                    calorie = (10 * para[3] + 6.25 * para[2] - 5 * para[1] - 161) * 1.75;
                } else {
                    calorie = (10 * para[3] + 6.25 * para[2] - 5 * para[1] + 5) * 1.75;
                }

            }
        }

        return calorie;
    }

    //my report
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

    private PieData getPieData(int count, float range) {

        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容


        xValues.add("Protein");
        xValues.add("Carbohydrate");
        xValues.add("Fat");


        ArrayList<Entry> yValues = new ArrayList<>();  //yVals用来表示封装每个饼块的实际数据


        float quarterly1 = 15;
        float quarterly2 = 55;
        float quarterly3 = 30;


        yValues.add(new Entry(quarterly1, 0));
        yValues.add(new Entry(quarterly2, 1));
        yValues.add(new Entry(quarterly3, 2));


        PieDataSet pieDataSet = new PieDataSet(yValues, "");
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

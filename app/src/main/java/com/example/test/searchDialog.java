package com.example.test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by yuexiao on 5/7/16.
 */
public class searchDialog extends Dialog {
    private AutoCompleteTextView search;
    private ListView resultListView;
    private DatabaseHelper dbHelper;
    private String account_id;

    class resultAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<Map<String, Object>> list;
        private int layoutID;
        private DatabaseHelper dbHelper;

        public resultAdapter(Context context, List<Map<String, Object>> list,
                             int layoutID) {
            this.mInflater = LayoutInflater.from(context);
            this.list = list;
            this.layoutID = layoutID;
            dbHelper = new DatabaseHelper(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = mInflater.inflate(layoutID, null);
            account_id=convertView.getContext().getSharedPreferences("config", Context.MODE_PRIVATE).getString("account_id","");


            TextView name = (TextView) convertView.findViewById(R.id.tv_dialogresult);
            name.setText(list.get(position).get("recipe_name").toString());


            addListener(convertView, position);
            this.notifyDataSetChanged();

            return convertView;
        }


        public void addListener(final View convertView, final int position) {

            ((TextView) convertView.findViewById(R.id.tv_dialogresult)).setOnClickListener(new View.OnClickListener() {
                TextView tv_recipename, tv_quantity;
                Button btn_add, btn_delete, btn_cancel, btn_ok;
                PieChart pieChart;
                private float[] materials;


                @Override
                public void onClick(View v) {


                    final AlertDialog alertDialog = new AlertDialog.Builder(convertView.getContext()).create();
                    alertDialog.show();
                    final Window window = alertDialog.getWindow();
                    window.setContentView(R.layout.add_plan_dialog);


                    init(window.getDecorView());
                    final int recipe_id = Integer.valueOf(list.get(position).get("recipe_id").toString());
                    final int[] quantity = {100};
                    materials = dbHelper.getNutrition(recipe_id);
                    PieData pieData = getPieData(3, 100);
                    showChart(pieChart, pieData);


                    tv_recipename.setText(dbHelper.getName(recipe_id));
                    btn_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            quantity[0] += 50;
                            tv_quantity.setText(String.valueOf(quantity[0]));

                        }
                    });
                    btn_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (quantity[0] > 0) quantity[0] -= 50;
                            tv_quantity.setText(String.valueOf(quantity[0]));

                        }
                    });
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            float fquantity = (float) (((float) quantity[0]) / 100.0);

                            dbHelper.addPlan(account_id,tab_Plan.day, tab_Plan.time, recipe_id, fquantity);
                            alertDialog.dismiss();
                            tab_Plan.plan.onResume();

                        }
                    });


                }

                private void showChart(PieChart pieChart, PieData pieData) {

                    pieChart.setHoleRadius(40f);  //半径
                    pieChart.setTransparentCircleRadius(43f); // 半透明圈
                    //pieChart.setHoleRadius(0);  //实心圆

                    pieChart.setDescription("");

                    pieChart.setDrawCenterText(true);

                    pieChart.setDrawHoleEnabled(true);

                    pieChart.setRotationAngle(90);
                    pieChart.setRotationEnabled(true);

                    pieChart.setUsePercentValues(true);
                    pieChart.setData(pieData);
                    Legend mLegend = pieChart.getLegend();
                    mLegend.setEnabled(true);
                    mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  //最右边显示
//      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形
                    mLegend.setXEntrySpace(10f);
                    mLegend.setYEntrySpace(10f);
                    mLegend.setTextSize(13f);


                    pieChart.animateXY(1000, 1000);
                }

                public PieData getPieData(int count, float range) {

                    ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容
                    float quarterly1 = materials[3];
                    float quarterly2 = materials[2];
                    float quarterly3 = materials[1];

                    xValues.add("Protein: " + materials[3] + "g");
                    xValues.add("Carbohydrate: " + materials[2] + "g");
                    xValues.add("Fat: " + materials[1] + "g");


                    ArrayList<Entry> yValues = new ArrayList<>();  //yVals用来表示封装每个饼块的实际数据


                    yValues.add(new Entry(quarterly1, 0));
                    yValues.add(new Entry(quarterly2, 1));
                    yValues.add(new Entry(quarterly3, 2));


                    PieDataSet pieDataSet = new PieDataSet(yValues, ""/*显示在比例图上*/);
                    pieDataSet.setValueTextSize(10);
                    //pieDataSet.setValueFormatter();


                    ArrayList<Integer> colors = new ArrayList<Integer>();

                    colors.add(Color.rgb(53, 185, 200));
                    colors.add(Color.rgb(152, 196, 41));
                    colors.add(Color.rgb(129, 132, 132));

                    pieDataSet.setColors(colors);


                    PieData pieData = new PieData(xValues, pieDataSet);

                    return pieData;
                }

                public void init(View convertView) {
                    tv_recipename = (TextView) convertView.findViewById(R.id.tv_plandialog_recipename);
                    tv_quantity = (TextView) convertView.findViewById(R.id.tv_plandialog_quantity);
                    btn_add = (Button) convertView.findViewById(R.id.btn_plandialot_add);
                    btn_delete = (Button) convertView.findViewById(R.id.btn_plandialog_delete);
                    btn_cancel = (Button) convertView.findViewById(R.id.btn_plandialog_cancel);
                    btn_ok = (Button) convertView.findViewById(R.id.btn_plandialog_ok);
                    pieChart = (PieChart) convertView.findViewById(R.id.spread_pie_chart);

                }


            });


        }

    }


    public searchDialog(final Context context) {
        super(context, R.style.search_dialog);
        dbHelper = new DatabaseHelper(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.search_dialog, null);
        search = (AutoCompleteTextView) view.findViewById(R.id.actv_search);
        resultListView = (ListView) view.findViewById(R.id.listview_result);

        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(searchDialog.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    AutoCompleteTextView search = (AutoCompleteTextView) findViewById(R.id.actv_search);
                    int[] resultList = dbHelper.searchRecipeName(search.getText().toString());

                    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                    for (int i = 0; i < resultList.length; i++) {

                        if (resultList[i] != 0) {
                            Map<String, Object> map = new Hashtable<String, Object>();
                            map.put("recipe_id", resultList[i]);
                            map.put("recipe_name", dbHelper.getName(resultList[i]));
                            list.add(map);
                        } else break;
                    }
                    resultAdapter adapter = new resultAdapter(context, list, R.layout.search_dialog_result);

                    resultListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
                return false;
            }
        });


        setContentView(view);


    }
}

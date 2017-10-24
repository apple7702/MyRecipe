package com.example.test;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class tab_Plan extends AppCompatActivity

{


    private ImageButton back, next;
    private DatabaseHelper dbHelper;
    ListView lv_breakfast, lv_lunch, lv_dinner, lv_snack;
    ImageButton ib_breakfast, ib_lunch, ib_dinner, ib_snack;
    TextView tv_ca1, tv_ca2, tv_ca3, tv_ca4,
            tv_recommend1, tv_recommend2, tv_recommend3, tv_recommend4,
            tv_alarm1, tv_alarm2, tv_alarm3, tv_alarm4,
            tv_percent1, tv_percent2, tv_percent3, tv_percent4;
    ImageView iv_cart1, iv_cart2, iv_cart3, iv_cart4;
    public static int day = 0;
    public static int time = 0;
    public static tab_Plan plan;
    public  resultAdapter breakfastAdapter, lunchAdapter, dinnerAdapter, snackAdapter;
    public  List<Map<String, Object>> listBreakfast, listLunch, listDinner, listSnack;
    String[] weekdays={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};

    private String account_id;

    TextView date;

    class resultAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<Map<String, Object>> list;

        public resultAdapter(Context context, List<Map<String, Object>> list) {
            this.mInflater = LayoutInflater.from(context);
            this.list = list;
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

            convertView = mInflater.inflate(R.layout.listview_detailplan, null);
            Map<String, Object> map = list.get(position);

            ((TextView) convertView.findViewById(R.id.tv_recipename)).setText(map.get("name").toString());
            ((TextView) convertView.findViewById(R.id.tv_quantity)).setText(map.get("quantity").toString() + "g");
            ((TextView) convertView.findViewById(R.id.tv_calories)).setText(map.get("energy") + "kcal");


            // addListener(convertView, position);
          //  this.notifyDataSetChanged();

            return convertView;
        }




    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_plan_layout);
        plan = this;
        dbHelper = new DatabaseHelper(getApplicationContext());
        account_id=getSharedPreferences("config", Context.MODE_PRIVATE).getString("account_id","");
        init();


    }


    @Override
    protected void onResume() {
        super.onResume();
        day=weekDay();

        initView(day);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (day > 1) {
                    day -= 1;
                    date.setText(Integer.toString(day));

                }


                initView(day);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (day < 6) {
                    day += 1;
                    date.setText(Integer.toString(day));
                }
                initView(day);

            }
        });




    }

    public void init() {
        date = (TextView) findViewById(R.id.plan_date);

        back = (ImageButton) findViewById(R.id.rb_plan_back);
        next = (ImageButton) findViewById(R.id.rb_plan_next);

        lv_breakfast = (ListView) findViewById(R.id.lv_breakfast);
        lv_lunch = (ListView) findViewById(R.id.lv_lunch);
        lv_dinner = (ListView) findViewById(R.id.lv_dinner);
        lv_snack = (ListView) findViewById(R.id.lv_snack);
        ib_breakfast = (ImageButton) findViewById(R.id.ib_plan_breakfast);
        ib_lunch = (ImageButton) findViewById(R.id.ib_plan_lunch);
        ib_dinner = (ImageButton) findViewById(R.id.ib_plan_dinner);
        ib_snack = (ImageButton) findViewById(R.id.ib_plan_snack);

        tv_ca1 = (TextView) findViewById(R.id.kcal_user);
        tv_ca2 = (TextView) findViewById(R.id.kcal_user2);
        tv_ca3 = (TextView) findViewById(R.id.kcal_user3);
        tv_ca4 = (TextView) findViewById(R.id.kcal_user4);

        tv_recommend1 = (TextView) findViewById(R.id.recommendkcal_user);
        tv_recommend2 = (TextView) findViewById(R.id.recommendkcal_user2);
        tv_recommend3 = (TextView) findViewById(R.id.recommendkcal_user3);
        tv_recommend4 = (TextView) findViewById(R.id.recommendkcal_user4);

        tv_alarm1 = (TextView) findViewById(R.id.time_user);
        tv_alarm2 = (TextView) findViewById(R.id.time_user2);
        tv_alarm3 = (TextView) findViewById(R.id.time_user3);
        tv_alarm4 = (TextView) findViewById(R.id.time_user4);

        tv_percent1 = (TextView) findViewById(R.id.recommend_percent);
        tv_percent2 = (TextView) findViewById(R.id.recommend_percent2);
        tv_percent3 = (TextView) findViewById(R.id.recommend_percent3);
        tv_percent4 = (TextView) findViewById(R.id.recommend_percent4);

        iv_cart1 = (ImageView) findViewById(R.id.image);
        iv_cart2 = (ImageView) findViewById(R.id.image2);
        iv_cart3 = (ImageView) findViewById(R.id.image3);
        iv_cart4 = (ImageView) findViewById(R.id.image4);

    }

    public void initView(int day) {
        date.setText(String.valueOf(weekdays[day]));

        if (!account_id.equals("")){
            listBreakfast = dbHelper.searchPlanbyCode(account_id,(day - 1) * 4 + 1);
            listLunch = dbHelper.searchPlanbyCode(account_id,(day - 1) * 4 + 2);
            listDinner = dbHelper.searchPlanbyCode(account_id,(day - 1) * 4 + 3);
            listSnack = dbHelper.searchPlanbyCode(account_id,(day - 1) * 4 + 4);


            breakfastAdapter = new resultAdapter(this, listBreakfast);
            lunchAdapter = new resultAdapter(this, listLunch);
            dinnerAdapter = new resultAdapter(this, listDinner);
            snackAdapter = new resultAdapter(this, listSnack);

            lv_breakfast.setAdapter(breakfastAdapter);
            lv_lunch.setAdapter(lunchAdapter);
            lv_dinner.setAdapter(dinnerAdapter);
            lv_snack.setAdapter(snackAdapter);

            breakfastAdapter.notifyDataSetChanged();
            lunchAdapter.notifyDataSetChanged();
            dinnerAdapter.notifyDataSetChanged();
            snackAdapter.notifyDataSetChanged();


            //text initialize
            initTextView();

            initButtonListenner();


        }else {
            Toast.makeText(this,"PLEASE LOG IN!",Toast.LENGTH_SHORT).show();}

    }

    public void initTextView() {

        tv_ca1.setText(calculateCalories(listBreakfast));
        tv_ca2.setText(calculateCalories(listLunch));
        tv_ca3.setText(calculateCalories(listDinner));
        tv_ca4.setText(calculateCalories(listSnack));

    }

    public String calculateCalories(List<Map<String, Object>> list) {
        String currentCal;
        float fcurrentCal = 0;
        Map<String, Object> map;
        for (int i = 0; i < list.size(); i++) {
            map = list.get(i);
            float temp = Float.valueOf(map.get("energy").toString());
            fcurrentCal += temp;
        }
        currentCal = String.valueOf(fcurrentCal);

        return currentCal;
    }


    public void initButtonListenner() {


        ib_breakfast.setOnClickListener(addButton);
        ib_lunch.setOnClickListener(addButton);
        ib_dinner.setOnClickListener(addButton);
        ib_snack.setOnClickListener(addButton);
        
        iv_cart1.setOnClickListener(addCart);
        iv_cart2.setOnClickListener(addCart);
        iv_cart3.setOnClickListener(addCart);
        iv_cart4.setOnClickListener(addCart);
    }

    //// TODO: 5/11/16 add to cart 
    View.OnClickListener addCart=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.image:
                    time=1;
                    iv_cart1.setImageResource(R.drawable.shoppingcart_on);
                    break;
                case R.id.image2:
                    time=2;
                    iv_cart2.setImageResource(R.drawable.shoppingcart_on);

                    break;
                case R.id.image3:
                    time=3;
                    iv_cart3.setImageResource(R.drawable.shoppingcart_on);

                    break;
                case R.id.image4:
                    time=4;
                    iv_cart4.setImageResource(R.drawable.shoppingcart_on);

                    break;
                default:
                    break;
            }
            addToCart();

        }
    };
    public void addToCart(){
        dbHelper.updateCartRecipe(day,time,account_id);
        
    }

    View.OnClickListener addButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            searchDialog sDialog = new searchDialog(v.getContext());
            sDialog.show();

            switch (v.getId()) {
                case R.id.ib_plan_breakfast:
                    time = 1;
                    break;
                case R.id.ib_plan_lunch:
                    time = 2;
                    break;
                case R.id.ib_plan_dinner:
                    time = 3;
                    break;
                case R.id.ib_plan_snack:
                    time = 4;
                    break;

            }


        }
    };

    //新写
    private int weekDay() {
//0-7  sun----sat
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        return dayOfWeek;
    }


}

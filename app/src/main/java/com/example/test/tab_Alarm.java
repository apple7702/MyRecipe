package com.example.test;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class tab_Alarm extends AppCompatActivity {


    public static AlarmAdapter adapter;
    private ListView listView;
    public static List<Map<String, Object>> list;
    public static tab_Alarm tab_alarm;
    public static AlarmManager ams[];
    public static PendingIntent pis[];
    public static Intent its[];
    private DatabaseHelper dbHelper;
    private String account_id;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.tab_alarm_layout);
        dbHelper = new DatabaseHelper(getApplicationContext());
        tab_alarm = this;

        SharedPreferences sp=this.getSharedPreferences("config", Context.MODE_PRIVATE);
        account_id=sp.getString("account_id","");


        //从数据库初始化闹钟数据


    }

    @Override
    protected void onResume() {
        super.onResume();
        account_id=getSharedPreferences("config", Context.MODE_PRIVATE).getString("account_id","");
        if (!account_id.equals("")){
            list = dbHelper.readAllAlarm(account_id);
            listView = (ListView) findViewById(R.id.alarm_listview);
            adapter = new AlarmAdapter(this, list, R.layout.alarm,
                    new String[]{
                            "time", "loop", "date1", "date2", "date3", "date4", "date5", "date6", "date7", "title"},
                    new int[]{
                            R.id.tv_time, R.id.ib_loop, R.id.tv_date1, R.id.tv_date2, R.id.tv_date3, R.id.tv_date4, R.id.tv_date5, R.id.tv_date6, R.id.tv_date7, R.id.tv_title});
            listView.setAdapter(adapter);

            findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addAlarm(list);
                    adapter.notifyDataSetChanged();
                }
            });

            adapter.notifyDataSetChanged();
            setAlarm();
        } else {
            Toast.makeText(this, "PLEASE LOG IN", Toast.LENGTH_SHORT).show();
        }





    }

    private void setAlarm() {
        ams = new AlarmManager[list.size()];
        pis = new PendingIntent[list.size()];
        its = new Intent[list.size()];

        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < list.size(); i++) {
            its[i] = new Intent();
            its[i].setClass(tab_Alarm.this, AlarmReceiver.class);
            its[i].putExtra("alarmId", i);
            pis[i] = PendingIntent.getBroadcast(tab_Alarm.this, i, its[i], PendingIntent.FLAG_CANCEL_CURRENT);
            String time[] = list.get(i).get("time").toString().split(":");
            int hour = Integer.parseInt(time[0]);
            int min = Integer.parseInt(time[1]);

            calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            calendar.set(Calendar.MINUTE, min);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            if (System.currentTimeMillis() > calendar.getTimeInMillis()) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            ams[i] = (AlarmManager) getSystemService(ALARM_SERVICE);

            ams[i].setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pis[i]);


        }
    }

    private void addAlarm(List<Map<String, Object>> list) {
        Map<String, Object> map = new HashMap<String, Object>();

        Time t = new Time();
        t.setToNow();
        int hour = t.hour;
        int minute = t.minute;
        Integer h = (Integer) hour;
        String sHour;
        if (h < 10) {
            sHour = "0" + h.toString();
        } else {
            sHour = h.toString();
        }
        Integer m = (Integer) minute;
        String sMin;
        if (m < 10) {
            sMin = "0" + m.toString();
        } else {
            sMin = m.toString();
        }
        String time = sHour + ":" + sMin;

        map.put("time", time);
        map.put("loop", false);
        map.put("date1", false);
        map.put("date2", false);
        map.put("date3", false);
        map.put("date4", false);
        map.put("date5", false);
        map.put("date6", false);
        map.put("date7", false);
        map.put("title", "");
        map.put("ringtone", "");
        map.put("volume", 100);

        list.add(map);
        boolean booleans[] = {false, false, false, false, false, false, false, false};
        dbHelper.addAlarm(time, booleans, "", "", 100, account_id);


    }


}
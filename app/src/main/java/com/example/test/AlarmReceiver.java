package com.example.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yuexiao on 4/19/16.
 */

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {


        int position = intent.getIntExtra("alarmId", 100);

        Intent it = new Intent(context, AlarmActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(it);


    }



    private int weekDay() {

//0-7  sun----sat
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        return dayOfWeek;
    }

}
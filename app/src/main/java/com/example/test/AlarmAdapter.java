package com.example.test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import java.util.Map;

/**
 * Created by yuexiao on 4/16/16.
 */
public class AlarmAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Map<String, Object>> list;
    private int layoutID;
    private String flag[];
    private int ItemIDs[];
    private DatabaseHelper dbHelper;
    private String account_id;

    public AlarmAdapter(Context context, List<Map<String, Object>> list,
                        int layoutID, String flag[], int ItemIDs[]) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.layoutID = layoutID;
        this.flag = flag;
        this.ItemIDs = ItemIDs;
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


        TextView time = (TextView) convertView.findViewById(ItemIDs[0]);
        time.setText(list.get(position).get(flag[0]).toString());


        ImageButton loop = (ImageButton) convertView.findViewById(ItemIDs[1]);
        if (list.get(position).get(flag[1]).equals(true)) {
            loop.setImageResource(R.drawable.loop1);
        }

        for (int i = 2; i < 9; i++) {
            TextView day = (TextView) convertView.findViewById(ItemIDs[i]);
            if (list.get(position).get(flag[i]).equals(true)) {
                day.setTextColor(Color.parseColor("#336699"));
            } else {
                day.setTextColor(Color.parseColor("#555555"));
            }
        }


        TextView title = (TextView) convertView.findViewById(ItemIDs[9]);
        title.setText(tab_Alarm.list.get(position).get(flag[9]).toString());


        addListener(convertView, position);
        tab_Alarm.adapter.notifyDataSetChanged();

        return convertView;
    }


    public void addListener(final View convertView, final int position) {
        account_id=convertView.getContext().getSharedPreferences("config", Context.MODE_PRIVATE).getString("account_id","");

        ((LinearLayout) convertView.findViewById(R.id.ll_date)).

                setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           Intent intent = new Intent();
                                           intent.setClass(tab_Alarm.tab_alarm, AlarmSettings.class);
                                           Bundle bundle = new Bundle();
                                           bundle.putInt("position", position);
                                           intent.putExtras(bundle);
                                           tab_Alarm.tab_alarm.startActivity(intent);


                                       }

                                   }

                );


        ((TextView) convertView.findViewById(R.id.tv_time)).

                setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           Intent intent = new Intent();
                                           intent.setClass(tab_Alarm.tab_alarm, AlarmSettings.class);
                                           Bundle bundle = new Bundle();
                                           bundle.putInt("position", position);
                                           intent.putExtras(bundle);
                                           tab_Alarm.tab_alarm.startActivity(intent);


                                       }
                                   }

                );


        ((ImageButton) convertView.findViewById(R.id.ib_loop)).

                setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           Intent intent = new Intent();
                                           intent.setClass(tab_Alarm.tab_alarm, AlarmSettings.class);
                                           Bundle bundle = new Bundle();
                                           bundle.putInt("position", position);
                                           intent.putExtras(bundle);
                                           tab_Alarm.tab_alarm.startActivity(intent);

                                       }
                                   }

                );
        ((TextView) convertView.findViewById(R.id.tv_title)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(tab_Alarm.tab_alarm, AlarmSettings.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                tab_Alarm.tab_alarm.startActivity(intent);

            }
        });


        ((ImageButton) convertView.findViewById(R.id.ib_delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteAlarm(account_id,position);
                tab_Alarm.list.remove(position);
                tab_Alarm.adapter.notifyDataSetChanged();
            }
        });

    }

}

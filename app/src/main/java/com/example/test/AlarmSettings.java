package com.example.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;


import java.util.Map;
import java.util.jar.Manifest;


public class AlarmSettings extends AppCompatActivity {
    private static final int Alarm = 1;
    String ringTonePath = "null";
    private String account_id="";
private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_settings);


        dbHelper = new DatabaseHelper(getApplicationContext());
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        int position = bundle.getInt("position");
        account_id=getSharedPreferences("config", Context.MODE_PRIVATE).getString("account_id","");

        initialize(position);

        setListener(position);

    }

    private void initialize(int position) {
        Map<String, Object> map = tab_Alarm.list.get(position);

        String time = map.get("time").toString();
        String arr[] = time.split(":");
        Integer hour = Integer.valueOf(arr[0]);
        Integer min = Integer.valueOf(arr[1]);
        ((TimePicker) findViewById(R.id.tp_settime)).setCurrentHour(hour);
        ((TimePicker) findViewById(R.id.tp_settime)).setCurrentMinute(min);
        ((CheckBox) findViewById(R.id.cbMon)).setChecked(map.get("date1").equals(true));
        ((CheckBox) findViewById(R.id.cbTue)).setChecked(map.get("date2").equals(true));
        ((CheckBox) findViewById(R.id.cbWed)).setChecked(map.get("date3").equals(true));
        ((CheckBox) findViewById(R.id.cbThur)).setChecked(map.get("date4").equals(true));
        ((CheckBox) findViewById(R.id.cbFri)).setChecked(map.get("date5").equals(true));
        ((CheckBox) findViewById(R.id.cbSat)).setChecked(map.get("date6").equals(true));
        ((CheckBox) findViewById(R.id.cbSun)).setChecked(map.get("date7").equals(true));
        ((CheckBox) findViewById(R.id.cbRepeat)).setChecked(map.get("loop").equals(true));
        ((TextView) findViewById(R.id.tvRingtone)).setText(map.get("ringtone").toString());
        ((SeekBar) findViewById(R.id.sbVolume)).setProgress(Integer.valueOf(map.get("volume").toString()));
        ((EditText) findViewById(R.id.etTitle)).setText(map.get("title").toString());
    }

    private void setListener(final int position) {
        ((TableRow) findViewById(R.id.trRingtone1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectRingtone();
            }
        });
        ((TableRow) findViewById(R.id.trRingtone2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectRingtone();
            }
        });
        findViewById(R.id.btnsave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(position);
            }
        });
        findViewById(R.id.btncancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    private void cancel() {
        finish();

    }


    private void save(int alarmposition) {

        int hour = ((TimePicker) findViewById(R.id.tp_settime)).getCurrentHour();
        int min = ((TimePicker) findViewById(R.id.tp_settime)).getCurrentMinute();

        Integer h = (Integer) hour;
        String sHour;
        if (h < 10) {
            sHour = "0" + h.toString();
        } else {
            sHour = h.toString();
        }
        Integer m = (Integer) min;
        String sMin;
        if (m < 10) {
            sMin = "0" + m.toString();
        } else {
            sMin = m.toString();
        }
        String time = sHour + ":" + sMin;

        boolean booleans[]={((CheckBox) findViewById(R.id.cbRepeat)).isChecked(),
                ((CheckBox) findViewById(R.id.cbMon)).isChecked(),
                ((CheckBox) findViewById(R.id.cbTue)).isChecked(),
                ((CheckBox) findViewById(R.id.cbWed)).isChecked(),
                ((CheckBox) findViewById(R.id.cbThur)).isChecked(),
                ((CheckBox) findViewById(R.id.cbFri)).isChecked(),
                ((CheckBox) findViewById(R.id.cbSat)).isChecked(),
                ((CheckBox) findViewById(R.id.cbSun)).isChecked()};

        String title=((EditText)findViewById(R.id.etTitle)).getText().toString();

       dbHelper.updateAlarm(alarmposition, time, booleans, title, ringTonePath, ((SeekBar) findViewById(R.id.sbVolume)).getProgress(), account_id);
        finish();
    }


    public void selectRingtone() {

        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "设置闹铃铃声");
        startActivityForResult(intent, Alarm);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        try {
            Uri pickedUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (pickedUri != null) {
                RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM, pickedUri);
                ringTonePath = pickedUri.toString();
                ((TextView) findViewById(R.id.tvRingtone)).setText(ringTonePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


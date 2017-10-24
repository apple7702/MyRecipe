package com.example.test;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.Toast;


public class MainActivity extends TabActivity{
    private TabHost tabhost;
    private RadioGroup main_radiogroup;
    private RadioButton tab_icon_menu, tab_icon_plan, tab_icon_alarm, tab_icon_me;


    static private Boolean bool = true;

    private String account_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        if (bool) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, Initial.class);
            startActivity(intent);
            finish();

            bool = false;
        }


        Intent intent = this.getIntent();
        int content = intent.getIntExtra("content", 0);
        account_id = intent.getStringExtra("account_id");

        if(account_id!=null){
            SharedPreferences sp=this.getSharedPreferences("config", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            editor.putString("account_id",account_id);
            editor.commit();
        }



        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        main_radiogroup = (RadioGroup) findViewById(R.id.main_radiogroup);

        tab_icon_menu = (RadioButton) findViewById(R.id.tab_icon_menu);
        tab_icon_plan = (RadioButton) findViewById(R.id.tab_icon_plan);
        tab_icon_alarm = (RadioButton) findViewById(R.id.tab_icon_alarm);
        tab_icon_me = (RadioButton) findViewById(R.id.tab_icon_me);


        tabhost = getTabHost();
        tabhost.addTab(tabhost.newTabSpec("tag1").setIndicator("0").setContent(new Intent(this, tab_Menu.class)));
        tabhost.addTab(tabhost.newTabSpec("tag2").setIndicator("1").setContent(new Intent(this, tab_Plan.class)));
        tabhost.addTab(tabhost.newTabSpec("tag3").setIndicator("2").setContent(new Intent(this, tab_Alarm.class)));
        tabhost.addTab(tabhost.newTabSpec("tag4").setIndicator("3").setContent(new Intent(this, tab_Me.class)));


        checkListener checkradio = new checkListener();
        main_radiogroup.setOnCheckedChangeListener(checkradio);


        switch (content) {
            case 0:
                tab_icon_menu.setChecked(true);
                tabhost.setCurrentTab(0);
                break;
            case 1:

                tab_icon_plan.setChecked(true);
                tabhost.setCurrentTab(1);
                break;
            case 2:
                tab_icon_alarm.setChecked(true);
                tabhost.setCurrentTab(2);
                break;
            case 3:
                tab_icon_me.setChecked(true);
                tabhost.setCurrentTab(3);
                break;
            default:
                break;
        }
    }



    public class checkListener implements OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.tab_icon_menu:
                    tabhost.setCurrentTab(0);
                    break;
                case R.id.tab_icon_plan:
                    tabhost.setCurrentTab(1);
                    break;
                case R.id.tab_icon_alarm:
                    tabhost.setCurrentTab(2);
                    break;
                case R.id.tab_icon_me:
                    tabhost.setCurrentTab(3);
                    break;
            }


        }
    }


}

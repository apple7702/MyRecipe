package com.example.test;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;




import java.util.TimerTask;

public class Initial extends AppCompatActivity {

   // public static DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

       // dbManager = new DBManager(this);


        java.util.Timer timer = new java.util.Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //判断是否有账号登陆
                if (false) {
                    //case1:有账号登陆，跳转主界面
                    finish();
                } else {
                    //case2:无账户登录信息，跳转登录注册部分
                    Intent intent=new Intent();
                    intent.setClass(Initial.this,AccountInit.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 2000);
    }
}

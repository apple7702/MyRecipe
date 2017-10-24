package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class AccountInit extends AppCompatActivity {

    static AccountInit instance;

    private class clickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_accountInit_skip:
                    visitorMode();
                    break;
                case R.id.btn_accountInit_logIn:
                    logIn();
                    break;
                case R.id.btn_accountInit_signUp:
                    signUp();

                    break;
            }

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_init);

        findViewById(R.id.btn_accountInit_skip).setOnClickListener(new clickListener());
        findViewById(R.id.btn_accountInit_signUp).setOnClickListener(new clickListener());
        findViewById(R.id.btn_accountInit_logIn).setOnClickListener(new clickListener());


    }

    private void visitorMode() {
        //告诉mainactivity是否登陆模式
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    private void logIn() {
        //进入 登陆界面
        Intent intent = new Intent();
        intent.setClass(this, LogIn.class);
        startActivity(intent);


    }

    private void signUp() {
        //进入 注册界面
        Intent intent = new Intent();
        intent.setClass(this, SignUp.class);
        startActivity(intent);

    }


}

package com.example.test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class SignUp extends AppCompatActivity {
    private Button btnGetStart;
    private ImageView ivBack;
    private TextView tvLogin;
    DatabaseHelper dbHelper;
    private AutoCompleteTextView accountId;
    private EditText password, confirmpassword;


    private class clickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_signup_back:
                    back();
                    break;
                case R.id.tv_signup_login:
                    logIn();
                    break;
                case R.id.btn_signup_getstart:
                    getStart();
                    break;

            }
        }
    }

    private void back() {

        finish();
    }

    private void logIn() {
        Intent it = new Intent();
        it.setClass(this, LogIn.class);
        startActivity(it);
        finish();
    }

    private void getStart() {
        if (password.getText().toString().equals(confirmpassword.getText().toString())) {
            if (dbHelper.addUser(accountId.getText().toString(), password.getText().toString())) {
                Dialog alertDialog = new AlertDialog.Builder(this).setTitle("对话框").setMessage("Complete your Information Now?")
                        .setPositiveButton("Now", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(SignUp.this, personal1.class);
                                intent.putExtra("account_id",accountId.getText().toString());
                                startActivity(intent);
                                AccountInit.instance.finish();
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(SignUp.this, MainActivity.class);
                                intent.putExtra("account_id",accountId.getText().toString());
                                startActivity(intent);
                                AccountInit.instance.finish();
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .setIcon(R.drawable.tab_menu).create();
                alertDialog.show();


            } else {


                Toast.makeText(this, "ACCOUNT EXIST! YOU CAN LOG IN WITH THIS ACCOUNT!", Toast.LENGTH_SHORT).show();
            }


        } else {


            Toast.makeText(this, "WRONG! NOT THE SAME PASSWORD!", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        dbHelper = new DatabaseHelper(getApplicationContext());

        btnGetStart = (Button) findViewById(R.id.btn_signup_getstart);
        tvLogin = (TextView) findViewById(R.id.tv_signup_login);
        ivBack = (ImageView) findViewById(R.id.iv_signup_back);

        accountId = (AutoCompleteTextView) findViewById(R.id.actv_signup_account);
        password = (EditText) findViewById(R.id.et_signup_password);
        confirmpassword = (EditText) findViewById(R.id.et_signup_confirm);

        btnGetStart.setOnClickListener(new clickListener());
        tvLogin.setOnClickListener(new clickListener());
        ivBack.setOnClickListener(new clickListener());
    }
}

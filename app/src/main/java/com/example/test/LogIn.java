package com.example.test;
import android.app.Activity;
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



public class LogIn extends AppCompatActivity {

    private Button btnGetStart;
    private ImageView ivBack;
    private TextView tvSignUp;
    private AutoCompleteTextView accountId;
    private EditText password;
    DatabaseHelper dbHelper;

    private class clickListener implements View.OnClickListener {


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_login_back:
                    back();
                    break;
                case R.id.tv_login_signup:
                    signUp();
                    break;
                case R.id.btn_login_getstart:
                    getStart();


            }
        }
    }

    private void back() {
        finish();
    }

    private void signUp() {
        Intent intent = new Intent();
        intent.setClass(this, SignUp.class);
        startActivity(intent);
        finish();

    }

    private void getStart() {


        //新写

        if (!dbHelper.checkUserExist(accountId.getText().toString())) {
            if (dbHelper.checkUserPassword(accountId.getText().toString(),password.getText().toString())){

                Intent intent = new Intent();
                intent.setClass(this, MainActivity.class);

                intent.putExtra("account_id", accountId.getText().toString());
                intent.putExtra("password", password.getText().toString());
                startActivity(intent);

                AccountInit.instance.finish();

                finish();

            }else {

                Toast.makeText(this,"WRONG PASSWORD! ENTER AGAIN!",Toast.LENGTH_SHORT).show();
            }



        } else {

            Toast.makeText(this, "ACCOUNT DOES NOT EXIST! PLEASE SIGN UP!", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        dbHelper=new DatabaseHelper(getApplicationContext());


        btnGetStart = (Button) findViewById(R.id.btn_login_getstart);
        tvSignUp = (TextView) findViewById(R.id.tv_login_signup);
        ivBack = (ImageView) findViewById(R.id.iv_login_back);
        accountId=(AutoCompleteTextView)findViewById(R.id.actv_login_account);
        password=(EditText)findViewById(R.id.et_login_password);


        btnGetStart.setOnClickListener(new clickListener());
        tvSignUp.setOnClickListener(new clickListener());
        ivBack.setOnClickListener(new clickListener());



    }

}

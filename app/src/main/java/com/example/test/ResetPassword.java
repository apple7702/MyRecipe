package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ResetPassword extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    EditText et_password, et_confirmation;
    Button btn_cancel, btn_ok;
    TextView tv_nickname;
    ImageView iv_icon;
    String account_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);
        dbHelper = new DatabaseHelper(getApplicationContext());


        tv_nickname = (TextView) findViewById(R.id.tv_resetpassword_nickname);
        et_password = (EditText) findViewById(R.id.et_resetpassword_password);
        et_confirmation = (EditText) findViewById(R.id.et_resetpassword_confirmation);
        btn_cancel = (Button) findViewById(R.id.btn_resetpassword_cancel);
        btn_ok = (Button) findViewById(R.id.btn_resetpassword_ok);
        iv_icon = (ImageView) findViewById(R.id.iv_resetpassword_icon);

        Intent it = getIntent();
        account_id = it.getStringExtra("account_id");


        initView();


    }

    public void initView() {
        tv_nickname.setText(account_id);
        btn_cancel.setOnClickListener(resetListenner);
        btn_ok.setOnClickListener(resetListenner);


    }

    View.OnClickListener resetListenner = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_resetpassword_cancel:
                    finish();
                    break;
                case R.id.btn_resetpassword_ok:
                    resetPassword(et_password.getText().toString(), et_confirmation.getText().toString());

                    break;

                default:
                    break;

            }
        }
    };

    public void resetPassword(String password, String confirm) {
        if (password.equals(confirm)) {
            dbHelper.resetPassword(account_id, et_password.getText().toString());
            finish();


        } else {
            Toast.makeText(this, "PLEASE ENTER THE SAME PASSWORD!", Toast.LENGTH_SHORT).show();

        }

    }
}

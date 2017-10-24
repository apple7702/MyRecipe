package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


public class personal1 extends AppCompatActivity {
    static personal1 instance;
    int gender;
    int age;
    int height;
    int weight;
    private String account_id;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal1);
        dbHelper = new DatabaseHelper(getApplicationContext());

        Intent intent = getIntent();
        account_id = intent.getStringExtra("account_id");


        instance = this;

        findViewById(R.id.ib_personal1_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    storeInformation();
                    Intent it = new Intent();
                    it.setClass(personal1.this, personal2.class);
                    it.putExtra("account_id", account_id);
                    startActivity(it);


                } catch (Exception e) {
                    Toast.makeText(personal1.this, "ENTER INCORRECTLLY!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public void storeInformation() {

        RadioButton radioButton = (RadioButton) findViewById(R.id.rb_personal1_female);
        EditText et_age = (EditText) findViewById(R.id.et_personal1_age);
        EditText et_height = (EditText) findViewById(R.id.et_personal1_height);
        EditText et_weight = (EditText) findViewById(R.id.et_personal1_weight);


        if (radioButton.isChecked() == true) gender = 1;
        else gender = 0;

        age = Integer.parseInt(et_age.getText().toString());
        height = Integer.parseInt(et_height.getText().toString());
        weight = Integer.parseInt(et_weight.getText().toString());

        dbHelper.UserInfoStep1(account_id, gender, age, height, weight);

    }
}

package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class personal2 extends AppCompatActivity {

    static personal2 instance;
    private String account_id;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal2);
        Intent intent = getIntent();
        account_id = intent.getStringExtra("account_id");
        dbHelper = new DatabaseHelper(getApplicationContext());


        findViewById(R.id.ib_personal2_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.ib_personal2_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                try{
                    storeInformation();  Intent it = new Intent();
                    it.putExtra("account_id", account_id);
                    it.setClass(personal2.this, personal3.class);
                    startActivity(it);


                }catch (Exception e){
                    Toast.makeText(personal2.this, "ENTER INCORRECTLLY!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void storeInformation() {
        //0:REDUCE FAT    1:BUILD MUSCLE      2:KEEP FIT
        int aim = ((Spinner) findViewById(R.id.spn_personal2_aim)).getSelectedItemPosition();
        int expected_weight = Integer.parseInt(((EditText) findViewById(R.id.et_personal2_expectedweight)).getText().toString());
        int exercise_duration = Integer.parseInt(((TextView) findViewById(R.id.et_personal2_duration)).getText().toString());


        dbHelper.UserInfoStep2(account_id, aim, expected_weight, exercise_duration);


    }

}

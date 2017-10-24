package com.example.test;

import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class personal3 extends AppCompatActivity {
    private String account_id;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal3);
        dbHelper=new DatabaseHelper(getApplicationContext());

        Intent intent=getIntent();
        account_id=intent.getStringExtra("account_id");

        findViewById(R.id.ib_personal3_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.ib_personal3_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    storeInformation();
                    Intent intent=new Intent();
                    intent.setClass(personal3.this,MainActivity.class);
                    intent.putExtra("account_id",account_id);
                    startActivity(intent);
                    personal1.instance.finish();
                    personal2.instance.finish();
                    finish();

                }catch (Exception e){
                    Toast.makeText(personal3.this, "ENTER INCORRECTLLY!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void storeInformation() {
        int occupation = ((Spinner) findViewById(R.id.spn_personal3_occupation)).getSelectedItemPosition();
        int type=((Spinner)findViewById(R.id.spn_personal3_exercisetype)).getSelectedItemPosition();
        int frequency=((Spinner)findViewById(R.id.spn_personal3_exercisefrequency)).getSelectedItemPosition();
        int exercise_duration=((Spinner)findViewById(R.id.spn_personal3_exerciseduration)).getSelectedItemPosition();
        int exercise_time=((Spinner)findViewById(R.id.spn_personal3_exercisetime)).getSelectedItemPosition();
        dbHelper.UserInfoStep3(account_id, occupation, type, frequency, exercise_duration, exercise_time);
    }

}

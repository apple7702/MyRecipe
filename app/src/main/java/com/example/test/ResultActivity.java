package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {
    public int[] result;
    public List<Map<String, Object>> result_list = new ArrayList<Map<String, Object>>();
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        dbHelper = new DatabaseHelper(getApplicationContext());

        Intent it = getIntent();


        result = it.getIntArrayExtra("result");



        if (result[0] == 0) {
            TextView textView=((TextView)findViewById(R.id.tv_result));
            textView.setText("no result");



        } else {
            ListView result_ListView = (ListView) findViewById(R.id.filterresult_listview);
            result_list = dbHelper.readRecipe(result);

            recipeInfoAdapter resultAdapter = new recipeInfoAdapter(ResultActivity.this, result_list);
            result_ListView.setAdapter(resultAdapter);
        }


    }
}

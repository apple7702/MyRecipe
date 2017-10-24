package com.example.test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class myFavourite extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    String account_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_favourite);
        dbHelper=new DatabaseHelper(getApplicationContext());

        SharedPreferences sp=this.getSharedPreferences("config", Context.MODE_PRIVATE);
        account_id=sp.getString("account_id","");




    }
    @Override
    protected void onResume(){
        super.onResume();

        List<Map<String,Object>> list_favorites=new ArrayList<>();
        int[] favorites = new int[40];

        int i=0;
        try{
            for (String s:dbHelper.getFavorite(account_id)){
                try {
                    favorites[i]=Integer.parseInt(s);
                }catch (Exception e){
                    favorites[i]=0;
                }finally {
                    i++;
                }

            }
        }catch (Exception e){
        }


        ListView listView_favorites=(ListView)findViewById(R.id.favorite_listview);
        list_favorites=dbHelper.readRecipe(favorites);

        recipeInfoAdapter adapterFavorites=new recipeInfoAdapter(this,list_favorites);
        listView_favorites.setAdapter(adapterFavorites);
        adapterFavorites.notifyDataSetChanged();



    }
}

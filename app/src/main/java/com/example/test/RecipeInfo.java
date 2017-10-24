package com.example.test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RecipeInfo extends AppCompatActivity {
    int id = 0;
    int _id = 0;
    Bundle data = new Bundle();
    private DatabaseHelper dbHelper;
    private String account_id;


    class infoAdapter extends FragmentStatePagerAdapter {

        int number;

        public infoAdapter(FragmentManager fm, int number) {
            super(fm);
            this.number = number;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    FragmentIngredient ingredient = new FragmentIngredient();

                    data.putInt("id", _id);
                    ingredient.setArguments(data);
                    return ingredient;
                case 1:
                    FragmentNutrition nutrition = new FragmentNutrition();

                    data.putInt("id", _id);
                    nutrition.setArguments(data);
                    return nutrition;
                case 2:
                    FragmentCookSteps fragmentCookSteps = new FragmentCookSteps();
                    data.putInt("id", _id);
                    fragmentCookSteps.setArguments(data);
                    return fragmentCookSteps;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return number;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);
        dbHelper = new DatabaseHelper(getApplicationContext());
        final Intent intent = getIntent();
        id = intent.getIntExtra("_id", 0);
        _id = id;
        account_id=getSharedPreferences("config", Context.MODE_PRIVATE).getString("account_id","");

        int resID = getResources().getIdentifier("recipe" + id, "drawable", "com.example.test");
        ((ImageView) findViewById(R.id.iv_recipepic)).setImageResource(resID);


        ((TextView) findViewById(R.id.tv_recipename)).setText(dbHelper.getName(id));
        ((TextView) findViewById(R.id.tv_cooktime)).setText(dbHelper.getCooktime(id) + "min");
        ((TextView) findViewById(R.id.tv_fat)).setText(dbHelper.getFat(id) + "g");


        ((ImageButton) findViewById(R.id.ib_recipeinfo_return)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //新写
        final ImageView favorite = ((ImageView) findViewById(R.id.iv_favorite));

        favorite.setImageResource(R.drawable.star);
        if (!account_id.equals("")){
            final String[][] favorites = {dbHelper.checkFavorite(id, account_id)};
            if (favorites[0] != null) {
                favorite.setImageResource(R.drawable.yellow_star);
            }
            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favorites[0] = dbHelper.checkFavorite(id, account_id);

                    if (favorites[0] != null) {
                        favorite.setImageResource(R.drawable.star);
                        dbHelper.deleteFavorite(id, account_id, favorites[0]);
                        dbHelper.changeTotalFavorite(id, -1);
                    }

                    if (favorites[0] == null) {
                        favorite.setImageResource(R.drawable.yellow_star);
                        dbHelper.addFavorite(id,account_id);
                        dbHelper.changeTotalFavorite(id, 1);
                    }

                }
            });

        }




        ((ImageView) findViewById(R.id.iv_addtoplan)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(RecipeInfo.this).create();
                alertDialog.show();
                final Window window = alertDialog.getWindow();
                window.setContentView(R.layout.add_to_plan);

                final TextView quant = (TextView) window.getDecorView().findViewById(R.id.tv_addtoplan_quantity);
                Button add = (Button) window.getDecorView().findViewById(R.id.btn_addtoplan_add);
                Button delete = (Button) window.getDecorView().findViewById(R.id.btn_addtoplan_delete);

                final CheckBox day1 = (CheckBox) window.getDecorView().findViewById(R.id.cb_addtoplan_day1);
                final CheckBox day2 = (CheckBox) window.getDecorView().findViewById(R.id.cb_addtoplan_day2);
                final CheckBox day3 = (CheckBox) window.getDecorView().findViewById(R.id.cb_addtoplan_day3);
                final CheckBox day4 = (CheckBox) window.getDecorView().findViewById(R.id.cb_addtoplan_day4);
                final CheckBox day5 = (CheckBox) window.getDecorView().findViewById(R.id.cb_addtoplan_day5);
                final CheckBox day6 = (CheckBox) window.getDecorView().findViewById(R.id.cb_addtoplan_day6);
                final CheckBox breakfast = (CheckBox) window.getDecorView().findViewById(R.id.cb_addtoplan_breakfast);
                final CheckBox lunch = (CheckBox) window.getDecorView().findViewById(R.id.cb_addtoplan_lunch);
                final CheckBox dinner = (CheckBox) window.getDecorView().findViewById(R.id.cb_addtoplan_dinner);
                final CheckBox snack = (CheckBox) window.getDecorView().findViewById(R.id.cb_addtoplan_snack);


                final float[] quantity = {Float.parseFloat(quant.getText().toString())};


                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        quantity[0] += 0.5;
                        quant.setText(String.valueOf(quantity[0]));


                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (quantity[0] >= 0.5) {
                            quantity[0] -= 0.5;
                        } else quantity[0] = 0;
                        quant.setText(String.valueOf(quantity[0]));
                    }
                });

                ((Button) window.getDecorView().findViewById(R.id.btn_addtoplan_cancel)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                ((Button) window.getDecorView().findViewById(R.id.btn_addtoplan_confirm)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (day1.isChecked()) {
                            checkTime(1, breakfast, lunch, dinner, snack);
                        }
                        if (day2.isChecked()) {
                            checkTime(2, breakfast, lunch, dinner, snack);
                        }
                        if (day3.isChecked()) {
                            checkTime(3, breakfast, lunch, dinner, snack);
                        }
                        if (day4.isChecked()) {
                            checkTime(4, breakfast, lunch, dinner, snack);
                        }
                        if (day5.isChecked()) {
                            checkTime(5, breakfast, lunch, dinner, snack);
                        }
                        if (day6.isChecked()) {
                            checkTime(6, breakfast, lunch, dinner, snack);
                        }


                        Intent it = new Intent(RecipeInfo.this, MainActivity.class);
                        it.putExtra("content", 1);
                        it.putExtra("account_id", account_id);
                        alertDialog.dismiss();



                        window.getContext().startActivity(it);


                    }

                    public void checkTime(int day, CheckBox breakfast, CheckBox lunch, CheckBox dinner, CheckBox snack) {
                        if (breakfast.isChecked()) {
                            dbHelper.addPlan(account_id,day, 1, _id, quantity[0]);
                        }

                        if (lunch.isChecked()) {
                            dbHelper.addPlan(account_id,day, 2, _id, quantity[0]);
                        }
                        if (dinner.isChecked()) {
                            dbHelper.addPlan(account_id,day, 3, _id, quantity[0]);
                        }
                        if (snack.isChecked()) {
                            dbHelper.addPlan(account_id,day, 4, _id, quantity[0]);
                        }
                    }
                });


            }
        });


        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Ingredients"));
        tabLayout.addTab(tabLayout.newTab().setText("Nutrition"));
        tabLayout.addTab(tabLayout.newTab().setText("Cook Steps"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);


        final infoAdapter adapter = new infoAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
}

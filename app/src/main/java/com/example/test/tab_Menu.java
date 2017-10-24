package com.example.test;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class tab_Menu extends AppCompatActivity {


    PopupWindow popWin;
    private DatabaseHelper dbHelper;


    public static List<Map<String, Object>> list_latest = new ArrayList<Map<String, Object>>();
    public static List<Map<String, Object>> list_recommend = new ArrayList<Map<String, Object>>();

    public Intent intent = new Intent();


    private ViewPager ad_viewPage;

    private LinearLayout ll_dian;

    List<ReInfo> listReInfo;


    private int[] ids = {1, 2, 3, 4, 5};


    private Context mContext;
    private TuTu tu;

    class PopWin extends PopupWindow {

        private View view;
        View.OnClickListener popListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.iv_filterpop_return:
                        dismiss();
                        break;
                    case R.id.btn_filterpop_clear:
                        clear();
                        break;
                    case R.id.btn_filterpop_confirm:
                        search();
                        dismiss();
                        break;

                }

            }
        };

        public void clear() {

            for (int i = 1; i < 18; i++) {
                String is = Integer.toString(i);
                int resID = view.getResources().getIdentifier("cb_filterpop_" + is, "id", "com.example.test");
                ((CheckBox) view.findViewById(resID)).setChecked(false);

            }
        }

        public void search() {
            char c_kind[] = new char[4];
            char c_times[] = new char[4];
            char c_ingredients[] = new char[6];
            char c_types[] = new char[3];
            String kind, times, ingredients, types;
            int[] list=new int[DatabaseHelper.LISTNUM];
            for (int i=0;i<list.length;i++){
                list[i]=i+1;

            }


            for (int i = 0; i < 4; i++) {
                int resID = view.getResources().getIdentifier("cb_filterpop_" + (i + 1), "id", "com.example.test");
                if (((CheckBox) view.findViewById(resID)).isChecked()) {
                    c_kind[i] = '1';
                } else {
                    c_kind[i] = '0';
                }
            }
            kind = String.valueOf(c_kind);
            for (int i = 0; i < 4; i++) {
                int resID = view.getResources().getIdentifier("cb_filterpop_" + (i + 5), "id", "com.example.test");
                if (((CheckBox) view.findViewById(resID)).isChecked()) {
                    c_times[i] = '1';
                } else {
                    c_times[i] = '0';
                }
            }
            times = String.valueOf(c_times);
            for (int i = 0; i < 6; i++) {
                int resID = view.getResources().getIdentifier("cb_filterpop_" + (i + 9), "id", "com.example.test");
                if (((CheckBox) view.findViewById(resID)).isChecked()) {
                    c_ingredients[i] = '1';
                } else {
                    c_ingredients[i] = '0';
                }
            }
            ingredients = String.valueOf(c_ingredients);
            for (int i = 0; i < 3; i++) {
                int resID = view.getResources().getIdentifier("cb_filterpop_" + (i + 15), "id", "com.example.test");
                if (((CheckBox) view.findViewById(resID)).isChecked()) {
                    c_types[i] = '1';
                } else {
                    c_types[i] = '0';
                }
            }
            types = String.valueOf(c_types);

            list = dbHelper.searchKind(list, kind);

            list = dbHelper.searchTimes(list, times);

            list = dbHelper.searchIngredients(list, ingredients);

            list = dbHelper.searchTypes(list, types);


            intent.setClass(view.getContext(), ResultActivity.class);
            intent.putExtra("result", list);
            view.getContext().startActivity(intent);

        }


        public PopWin(Context mContext) {

            this.view = LayoutInflater.from(mContext).inflate(R.layout.filter_pop, null);

            this.setOutsideTouchable(true);

            this.view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {

                    int height = view.findViewById(R.id.pop_layout).getTop();

                    int y = (int) event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y < height) {

                            dismiss();
                        }
                    }
                    return true;
                }
            });


            this.setContentView(this.view);
            this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
            this.setWidth(800);
            this.setFocusable(true);

            //实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置弹出窗体的背景
            this.setBackgroundDrawable(dw);

            this.setAnimationStyle(R.style.filter_anim);
            getContentView().findViewById(R.id.iv_filterpop_return).setOnClickListener(popListener);
            getContentView().findViewById(R.id.btn_filterpop_confirm).setOnClickListener(popListener);
            getContentView().findViewById(R.id.btn_filterpop_clear).setOnClickListener(popListener);

        }

    }

    public View contentview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_menu_layout);




        dbHelper = new DatabaseHelper(getApplicationContext());

        mContext = this;
        initView();
        initAD();

        findViewById(R.id.iv_main_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow(v);
            }
        });


        //// TODO: 16/5/2 明确下推荐与最新


        ((AutoCompleteTextView) findViewById(R.id.actv_search)).setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(tab_Menu.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    AutoCompleteTextView search = (AutoCompleteTextView) findViewById(R.id.actv_search);
                    int[] resultList = dbHelper.searchRecipeName(search.getText().toString());


                    intent.setClass(tab_Menu.this, ResultActivity.class);
                    intent.putExtra("result", resultList);
                    startActivity(intent);

                }
                return false;
            }
        });


    }

    private void initAD() {
        contentview = LayoutInflater.from(mContext).inflate(R.layout.tab_menu_layout, null);


        listReInfo = new ArrayList<ReInfo>();
        for (int i = 0; i < 5; i++) {
            ReInfo bean = new ReInfo(contentview);
            bean.setId(i + "");
            bean.setImgPath(ids[i]);
            listReInfo.add(bean);
        }
        tu = new TuTu(ad_viewPage, ll_dian, mContext, listReInfo);
        tu.startViewPager(2000);//动态设置滑动间隔，并且开启轮播图
    }

    private void initView() {
        ad_viewPage = (ViewPager) findViewById(R.id.ad_viewPage);
        ll_dian = (LinearLayout) findViewById(R.id.ll_dian);

    }


    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    @Override
    protected void onResume() {
        super.onResume();

        int[] a = new int[]{1, 2, 14, 0, 0, 0, 0};

        ListView latest_listview = (ListView) findViewById(R.id.latest_listview);
        ListView recommend_listview = (ListView) findViewById(R.id.recommend_listview);

        list_latest = dbHelper.readRecipe(a);
        list_recommend = dbHelper.readRecipe(a);

        recipeInfoAdapter latestAdapter = new recipeInfoAdapter(tab_Menu.this, list_latest);
        recipeInfoAdapter recommendAdapter = new recipeInfoAdapter(tab_Menu.this, list_recommend);

        latest_listview.setAdapter(latestAdapter);
        recommend_listview.setAdapter(recommendAdapter);

        latestAdapter.notifyDataSetChanged();
        recommendAdapter.notifyDataSetChanged();

        setListViewHeightBasedOnChildren(latest_listview);
        setListViewHeightBasedOnChildren(recommend_listview);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        tu.destroyView();

        super.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void showPopWindow(View view) {
        popWin = new PopWin(this);
        popWin.showAsDropDown(findViewById(R.id.ll_activitymain_1), 0, 0, Gravity.RIGHT);


    }


}

package com.example.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class shoppingCart extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private String account_id;

    class itemAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private List<String> list;
        private Map<String, Material> map;

        public itemAdapter(Context context, Map<String, Material> map, List<String> list) {
            this.mInflater = LayoutInflater.from(context);
            this.list = list;
            this.map = map;
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = mInflater.inflate(R.layout.listview_shoppingcart, null);

            ((TextView) convertView.findViewById(R.id.tv_splv_recipename)).setText(list.get(position));
            Material material = map.get(list.get(position));
            ((TextView) convertView.findViewById(R.id.tv_splv_recipequantity)).setText(String.valueOf(material.getQuantity()) + material.getUnit());

            final View finalConvertView = convertView;
            ((LinearLayout) convertView.findViewById(R.id.ll_lvsp)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((LinearLayout) finalConvertView.findViewById(R.id.ll_lvsp)).setBackgroundResource(R.drawable.deleteline);


                }
            });



            return convertView;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart);
        dbHelper = new DatabaseHelper(getApplicationContext());

        account_id = getSharedPreferences("config", Context.MODE_PRIVATE).getString("account_id", "");


        if (account_id.equals("")) {
        } else {
            ListView spcart_list = (ListView) findViewById(R.id.shoppingcart_listview);

                Map map = dbHelper.material(account_id);
                List list = new ArrayList();
                for (Object o : map.keySet()) {
                    list.add(String.valueOf(o));
                }
                itemAdapter adapter = new itemAdapter(this, map, list);
                spcart_list.setAdapter(adapter);
                adapter.notifyDataSetChanged();

        }


    }


    @Override
    protected void onResume() {
        super.onResume();


    }


}

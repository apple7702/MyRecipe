package com.example.test;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by yuexiao on 16/5/2.
 */
public class recipeInfoAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    private List<Map<String, Object>> list;
    private int layoutID=R.layout.listview_recipe_info;
    private int ItemIDs[]=new int[]{R.id.iv_recipeinfo_pic, R.id.tv_recipeinfo_title, R.id.tv_recipeinfo_cooktime, R.id.tv_recipeinfo_calorie, R.id.tv_recipeinfo_favorite};


    public recipeInfoAdapter(Context context, List<Map<String, Object>> list) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final View view=mInflater.inflate(layoutID, null);

        convertView = mInflater.inflate(layoutID, null);


        int resID = convertView.getResources().getIdentifier("recipe"+list.get(position).get("_id").toString(), "drawable", "com.example.test");
        ((ImageView) convertView.findViewById(ItemIDs[0])).setImageResource(resID);

        ((TextView) convertView.findViewById(ItemIDs[1])).setText(list.get(position).get("name").toString());
        ((TextView) convertView.findViewById(ItemIDs[2])).setText(list.get(position).get("cook_time").toString());
        String nutrition=list.get(position).get("nutrition").toString();
        String[] nutritions=nutrition.split(", ");

                ((TextView) convertView.findViewById(ItemIDs[3])).setText(nutritions[0]);
        ((TextView) convertView.findViewById(ItemIDs[4])).setText(list.get(position).get("favourite").toString());


        ((ImageView) convertView.findViewById(ItemIDs[0])).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RecipeInfo.class);
                int id = Integer.valueOf(list.get(position).get("_id").toString());
                intent.putExtra("_id", id);
                view.getContext().startActivity(intent);
            }
        });


        return convertView;
    }
}

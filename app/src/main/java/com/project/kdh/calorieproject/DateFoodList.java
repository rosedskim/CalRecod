package com.project.kdh.calorieproject;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 동현 on 2017-11-30.
 */

public class DateFoodList extends BaseAdapter {
    ArrayList<Record> list=new ArrayList<Record>();
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Record getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context=viewGroup.getContext();
        if(view==null)
        {
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.date_food_list_layout,viewGroup,false);
        }
        TextView txt_time=(TextView)view.findViewById(R.id.txt_date_food_time);
        TextView txt_name=(TextView)view.findViewById(R.id.txt_date_food_name);
        TextView txt_cal=(TextView)view.findViewById(R.id.txt_date_food_cal);

        Record r=getItem(i);
        txt_time.setText(r.getTime());
        txt_name.setText(r.getFood());
        txt_cal.setText(String.valueOf(r.getCal()));

        return view;
    }

    public void addItem(String time, String food, int cal)
    {
        Record temp=new Record("","","",food,cal,time);
        list.add(temp);
    }
}

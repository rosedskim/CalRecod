package com.project.kdh.calorieproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by 동현 on 2017-11-26.
 */

public class KoreaFoodAdapter extends BaseAdapter {
    private LinkedList<Food> mItems=new LinkedList<>();
    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Food getItem(int i) {
        return mItems.get(i);
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
            view=inflater.inflate(R.layout.food_listview,viewGroup,false);
        }

        TextView txt_name=(TextView)view.findViewById(R.id.food_name);
        TextView txt_cal=(TextView)view.findViewById(R.id.food_cal);

        Food f=getItem(i);
        Log.d("name0",f.getName());
        Log.d("cal0",""+f.getCal());
        txt_name.setText(f.getName());
        txt_cal.setText(String.valueOf(f.getCal()));

        return view;
    }

    public void addItem(String name, int cal)
    {
        Food mFood=new Food(name,cal);
        mItems.add(mFood);
    }


}

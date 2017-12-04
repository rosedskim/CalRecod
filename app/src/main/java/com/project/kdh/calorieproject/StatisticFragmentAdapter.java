package com.project.kdh.calorieproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 동현 on 2017-12-01.
 */

public class StatisticFragmentAdapter extends BaseAdapter {
    ArrayList<StatisticRecord> mItems =new ArrayList<StatisticRecord>();
    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public StatisticRecord getItem(int i) {
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
            view=inflater.inflate(R.layout.statistic_record_list_layout,viewGroup,false);
        }

        TextView txt_date=(TextView)view.findViewById(R.id.txt_statistic_list_date);
        TextView txt_food=(TextView)view.findViewById(R.id.txt_statistic_list_cal);
        TextView txt_cal=(TextView)view.findViewById(R.id.txt_statistic_list_average);

        StatisticRecord sr=getItem(i);

        txt_date.setText(sr.getDate());
        txt_food.setText(String.valueOf(sr.getCal()));
        txt_cal.setText(String.valueOf(sr.getAverage()));

        return view;
    }
    public void addItem(String date, int cal, int average)
    {
        StatisticRecord st=new StatisticRecord(date,cal,average);
        mItems.add(st);
    }
}

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
 * Created by 동현 on 2017-11-29.
 */

public class SearchedRecordAdapter extends BaseAdapter {
    private LinkedList<Record> mItems = new LinkedList<>();
    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Record getItem(int i) {
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
            view=inflater.inflate(R.layout.record_listview,viewGroup,false);
        }
        Log.d("????","4");
        TextView txt_date=(TextView)view.findViewById(R.id.record_list_date);
        TextView txt_food=(TextView)view.findViewById(R.id.record_list_name);
        TextView txt_cal=(TextView)view.findViewById(R.id.record_list_cal);

        Record r=getItem(i);

        txt_date.setText(r.getYear()+"-"+r.getMonth()+"-"+r.getDate());
        txt_food.setText(r.getFood());
        txt_cal.setText(String.valueOf(r.getCal()));

        return view;
    }

    public void addItem(Record record)
    {
        Log.d("????","3");
        Record temp=new Record(record.getYear(),record.getMonth(),record.getDate(),record.getFood(),record.getCal(),"");
        mItems.add(temp);
    }
}

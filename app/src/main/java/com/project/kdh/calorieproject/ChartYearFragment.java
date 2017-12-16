package com.project.kdh.calorieproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 동현 on 2017-12-15.
 */

public class ChartYearFragment extends Fragment {

    BarChart barChart;
    ArrayList<YearRecord> arrayList=new ArrayList<>();
    DBHelper dbHelper;
    int index=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.chart_fragment_year_layout,container,false);
        barChart=(BarChart)rootView.findViewById(R.id.year_barchart);

        dataSetting();
        return rootView;
    }

    public void dataSetting()
    {
        arrayList.clear();
        dbHelper= new DBHelper(getActivity(), "RECORD.db",null,1);
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT year, cal FROM RECORD" ,null);
        while(cursor.moveToNext())
        {
            Log.d("cursor.get(0)",""+cursor.getInt(0));
            Log.d("cursor.get(1)",""+cursor.getInt(1));
            YearRecord yearRecord=new YearRecord(cursor.getInt(0),cursor.getInt(1));
            if(isCheck(cursor.getInt(0)))   //이미 있는 년도이면
            {
                arrayList.get(index).setCal(arrayList.get(index).getCal()+yearRecord.getCal());
            }
            else
            {
                arrayList.add(yearRecord);
            }
        }

        List<BarEntry> entries = new ArrayList<>();
        for(int i=0; i<arrayList.size(); i++)
        {
            entries.add(new BarEntry(arrayList.get(i).getYear(),arrayList.get(i).getCal()));
        }
        //기본 5개 이상있어야 하나씩 해주는듯
        entries.add(new BarEntry(2013f, 0));
        entries.add(new BarEntry(2014f, 0));
        entries.add(new BarEntry(2015f, 0));

        // gap of 2f

        BarDataSet set = new BarDataSet(entries, "연도별 칼로리");

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        barChart.setData(data);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate(); // refresh

    }
    boolean isCheck(int y)       //이미 있는 거인지 안닌지 체크
    {
        for(int i=0; i<arrayList.size(); i++)
        {
            if(arrayList.get(i).getYear()==y)
            {
                index=i;
                return true;
            }
        }
        return false;
    }
    private class YearRecord
    {
        int year;
        int cal;

        public YearRecord(int _year, int _cal)
        {
            year=_year;
            cal=_cal;
        }
        public int getYear()
        {
            return year;
        }
        public int getCal()
        {
            return cal;
        }
        public void setCal(int _cal)
        {
            cal=_cal;
        }
    }
}

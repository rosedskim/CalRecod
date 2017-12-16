package com.project.kdh.calorieproject;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 동현 on 2017-12-15.
 */

public class ChartMonthFragment extends Fragment implements View.OnClickListener{

    Button button;
    BarChart barChart;

    DBHelper dbHelper;
    ArrayList<MonthRecord> arrayList=new ArrayList<>();
    int index=0;

    private String[] yearStr={"2013","2014","2015","2016","2017"};
    int selectItem=0;

    int year;
    int month;
    int date;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.chart_fragment_month_layout,container,false);

        button=(Button)rootView.findViewById(R.id.btn_year_select);
        button.setOnClickListener(this);
        barChart=(BarChart)rootView.findViewById(R.id.month_barchart);

        Calendar cal=Calendar.getInstance();
        int temp_year=cal.get(Calendar.YEAR);

        dataSetting(temp_year);
        return rootView;
    }

    public void dataSetting(int y)
    {
        arrayList.clear();
        dbHelper= new DBHelper(getActivity(), "RECORD.db",null,1);
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT year,month,cal FROM RECORD WHERE year='"+y+"';" ,null);
        while(cursor.moveToNext())
        {
            Log.d("cursor.get(0)",""+cursor.getInt(0));
            Log.d("cursor.get(1)",""+cursor.getInt(1));
            Log.d("cursor.get(2)",""+cursor.getInt(2));
            MonthRecord monthRecord=new MonthRecord(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2));
            if(isCheck(cursor.getInt(1)))   //이미 있는 월이면
            {
                arrayList.get(index).setCal(arrayList.get(index).getCal()+monthRecord.getCal());
            }
            else
            {
                arrayList.add(monthRecord);
            }
        }

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 0));
        entries.add(new BarEntry(2, 0));
        entries.add(new BarEntry(3, 0));
        entries.add(new BarEntry(4, 0));
        entries.add(new BarEntry(5, 0));
        entries.add(new BarEntry(6, 0));
        entries.add(new BarEntry(7, 0));
        entries.add(new BarEntry(8, 0));
        entries.add(new BarEntry(9, 0));
        entries.add(new BarEntry(10,0));
        entries.add(new BarEntry(11,0));
        entries.add(new BarEntry(12,0));
        for(int i=0; i<arrayList.size(); i++)
        {
            for(int j=0; j<entries.size(); j++)
            {
                if (entries.get(j).getX() == arrayList.get(i).getMonth())
                {
                    entries.get(j).setY(entries.get(j).getY() + arrayList.get(i).getCal());
                }
            }
        }
        BarDataSet set = new BarDataSet(entries, "월별 칼로리");

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        barChart.setData(data);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate(); // refresh

    }
    boolean isCheck(int m)       //이미 있는 거인지 안닌지 체크
    {
        for(int i=0; i<arrayList.size(); i++)
        {
            if(arrayList.get(i).getMonth()==m)
            {
                index=i;
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_year_select:
            {
                final AlertDialog.Builder dlg=new AlertDialog.Builder(view.getContext());
                dlg.setTitle("날짜 설정");
                dlg.setCancelable(false);
                dlg.setSingleChoiceItems(yearStr, selectItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectItem=i;
                    }
                });
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (selectItem)
                        {
                            case 0: //2013
                            {
                                button.setText("2013년");
                                dataSetting(2013);
                                break;
                            }
                            case 1: //2014
                            {
                                button.setText("2014년");
                                dataSetting(2014);
                                break;
                            }
                            case 2: //2015
                            {
                                button.setText("2015년");
                                dataSetting(2015);
                                break;
                            }
                            case 3: //2016
                            {
                                button.setText("2016년");
                                dataSetting(2016);
                                break;
                            }
                            case 4: //2017
                            {
                                button.setText("2017년");
                                dataSetting(2017);
                                break;
                            }
                        }
                    }
                });
                dlg.show();
            }
        }
    }

    private class MonthRecord
    {
        int year;
        int month;
        int cal;

        public MonthRecord(int _year,int _month, int _cal)
        {
            year=_year;
            month=_month;
            cal=_cal;
        }
        public int getYear()
        {
            return year;
        }
        public int getMonth()
        {
            return month;
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

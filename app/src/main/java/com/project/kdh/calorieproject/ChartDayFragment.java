package com.project.kdh.calorieproject;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 동현 on 2017-12-15.
 */

public class ChartDayFragment extends Fragment implements View.OnClickListener {

    Button button;
    BarChart barChart;

    DBHelper dbHelper;
    ArrayList<DateRecord> arrayList=new ArrayList<>();
    int index=0;

    private String[] monthStr={"1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"};
    int selectItem=0;

    int year;
    int month;
    int date;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.chart_fragment_day_layout,container,false);

        button=(Button)rootView.findViewById(R.id.btn_month_select);
        button.setOnClickListener(this);
        barChart=(BarChart)rootView.findViewById(R.id.day_barchart);

        Calendar cal=Calendar.getInstance();
        int temp_month=cal.get(Calendar.MONTH)+1;

        dataSetting(temp_month);
        return rootView;
    }

    public void dataSetting(int m)
    {
        arrayList.clear();
        dbHelper= new DBHelper(getActivity(), "RECORD.db",null,1);
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Calendar cc=Calendar.getInstance();
        int yy=cc.get(Calendar.YEAR);
        Cursor cursor=db.rawQuery("SELECT year,month,date,cal FROM RECORD WHERE year='"+yy+"' and month='"+m+"';" ,null);
        while(cursor.moveToNext())
        {
            Log.d("cursor.get(0)",""+cursor.getInt(0));
            Log.d("cursor.get(1)",""+cursor.getInt(1));
            Log.d("cursor.get(2)",""+cursor.getInt(2));
            Log.d("cursor.get(3)",""+cursor.getInt(3));
            DateRecord dateRecord=new DateRecord(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),cursor.getInt(3));
            if(isCheck(cursor.getInt(2)))   //이미 있는 일이면
            {
                arrayList.get(index).setCal(arrayList.get(index).getCal()+dateRecord.getCal());
            }
            else
            {
                arrayList.add(dateRecord);
            }
        }
        Calendar temp_cal=Calendar.getInstance();
        temp_cal.set(Calendar.MONTH,m-1); //월을 선택한 월로 바꾸기
        int size=temp_cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.d("값",""+size);
        List<BarEntry> entries = new ArrayList<>();
        for(int i=1; i<=size; i++)
        {
            entries.add(new BarEntry(i,0));
        }

        for(int i=0; i<arrayList.size(); i++)
        {
            for(int j=0; j<entries.size(); j++)
            {
                if (entries.get(j).getX() == arrayList.get(i).getDate())
                {
                    entries.get(j).setY(entries.get(j).getY() + arrayList.get(i).getCal());
                }
            }
        }
        BarDataSet set = new BarDataSet(entries, "일별 칼로리");

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        barChart.setData(data);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate(); // refresh

    }
    boolean isCheck(int d)       //이미 있는 거인지 안닌지 체크
    {
        for(int i=0; i<arrayList.size(); i++)
        {
            if(arrayList.get(i).getDate()==d)
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
            case R.id.btn_month_select:
            {
                final AlertDialog.Builder dlg=new AlertDialog.Builder(view.getContext());
                dlg.setTitle("날짜 설정");
                dlg.setCancelable(false);
                dlg.setSingleChoiceItems(monthStr, selectItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectItem=i;
                    }
                });
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("값",""+(selectItem+1));
                        dataSetting(selectItem+1);      //파라미터로 월 전달
                        button.setText(""+(selectItem+1)+"월");
                    }
                });
                dlg.show();
            }
        }
    }

    private class DateRecord
    {
        int year;
        int month;
        int date;
        int cal;

        public DateRecord(int _year,int _month,int _date, int _cal)
        {
            year=_year;
            month=_month;
            date=_date;
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
        public int getDate()
        {
            return date;
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

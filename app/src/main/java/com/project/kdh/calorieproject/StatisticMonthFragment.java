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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by 동현 on 2017-12-01.
 */

public class StatisticMonthFragment extends Fragment{
    DBHelper dbHelper;

    ListView mListView;
    StatisticFragmentAdapter mAdapter;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.statistic_fragment_month_layout,container,false);
        dbHelper= new DBHelper(getActivity(), "RECORD.db",null,1);


        mListView=(ListView)rootView.findViewById(R.id.li_statistic_month);
        mAdapter=new StatisticFragmentAdapter();

        dataSetting();
        return rootView;
    }
    public void dataSetting()
    {
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT year, month, cal FROM RECORD;",null);

        ArrayList<pair> list=new ArrayList<pair>();

        int total_cal=0;
        while(cursor.moveToNext())
        {
            pair p=new pair(cursor.getString(0),cursor.getString(1),cursor.getInt(2));
            list.add(p);
            total_cal+=cursor.getInt(2);

        }
        Descending descending=new Descending();
        Collections.sort(list,descending);

        ArrayList<pair> year_list=new ArrayList<pair>();
        for(int i=0; i<list.size(); i++)
        {
            if(year_list.size()==0)
            {
                pair temp=new pair(list.get(i).getYear(),list.get(i).getMonth(),list.get(i).getCal());
                Log.d("월",list.get(i).getYear());
                Log.d("월",list.get(i).getMonth());
                Log.d("월",""+list.get(i).getCal());
                year_list.add(temp);
            }
            else {
                boolean check=false;
                for (int j = 0; j < year_list.size(); j++)
                {
                    //연도 같고 월도 같으면
                    if (year_list.get(j).getYear().equals(list.get(i).getYear()) && year_list.get(j).getMonth().equals(list.get(i).getMonth()) ) {
                        //칼로리 추가
                        year_list.get(j).setCal(year_list.get(j).getCal() + list.get(i).getCal());
                        check = true;
                        break;
                    }
                }
                //연도 다른거나 월 다르면 새로추가
                if(check==false)
                {
                    pair temp = new pair(list.get(i).getYear(), list.get(i).getMonth(), list.get(i).getCal());
                    year_list.add(temp);
                }
            }
        }
        int average=0;
        if(year_list.size()==0) {
            average=0;
        }
        else {
            average = total_cal / year_list.size();
        }
        for(int i=0; i<year_list.size(); i++) {
            mAdapter.addItem(year_list.get(i).getYear()+"."+year_list.get(i).getMonth(),year_list.get(i).getCal(), year_list.get(i).getCal()-average);
        }
        mListView.setAdapter(mAdapter);

    }
    public class pair{
        String year;
        String month;
        int cal;
        public pair(String y, String m, int c)
        {
            year=y;
            month=m;
            cal=c;
        }
        public String getYear()
        {
            return year;
        }
        public String getMonth(){return month;}
        public int getCal()
        {
            return cal;
        }

        public void setCal(int c)
        {
            cal=c;
        }
    }
    class Descending implements Comparator<pair> {

        @Override
        public int compare(pair p1, pair p2) {
            if(p2.getYear().compareTo(p1.getYear()) < 0)
            {
                return p2.getYear().compareTo(p1.getYear());
            }
            else if(p2.getYear().compareTo(p1.getYear()) > 0)
            {
                return p2.getYear().compareTo(p1.getYear());
            }
            else //같을때
            {
                if(p2.getMonth().compareTo(p1.getMonth()) < 0)
                {
                    return p2.getMonth().compareTo(p1.getMonth());
                }
                else if(p2.getMonth().compareTo(p1.getMonth()) > 0)
                {
                    return p2.getMonth().compareTo(p1.getMonth());
                }
                else
                {
                    return 0;
                }
            }
        }
    }
}

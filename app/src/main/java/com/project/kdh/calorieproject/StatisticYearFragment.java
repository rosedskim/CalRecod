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

public class StatisticYearFragment extends Fragment {

    DBHelper dbHelper;

    ListView mListView;
    StatisticFragmentAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.statistic_fragment_year_layout,container,false);
        dbHelper= new DBHelper(getActivity(), "RECORD.db",null,1);


        mListView=(ListView)rootView.findViewById(R.id.li_statistic_year);
        mAdapter=new StatisticFragmentAdapter();

        dataSetting();
        return rootView;
    }
    public void dataSetting()
    {
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT year, cal FROM RECORD;",null);

        ArrayList<pair> list=new ArrayList<pair>();

        int total_cal=0;
        while(cursor.moveToNext())
        {
            pair p=new pair(cursor.getString(0),cursor.getInt(1));
            list.add(p);
            total_cal+=cursor.getInt(1);
        }
        Descending descending=new Descending();
        Collections.sort(list,descending);

        ArrayList<pair> year_list=new ArrayList<pair>();
        for(int i=0; i<list.size(); i++)
        {
            if(year_list.size()==0)
            {
                pair temp=new pair(list.get(i).getYear(),list.get(i).getCal());
                year_list.add(temp);
            }
            else {
                for (int j = 0; j < year_list.size(); j++) {
                    //연도 같은거면
                    if (year_list.get(j).getYear().equals(list.get(i).getYear())) {
                        //칼로리 추가
                        year_list.get(j).setCal(year_list.get(j).getCal() + list.get(i).getCal());
                        break;
                    }
                    //연도 다른거나오면 넣기
                    else {
                        pair temp = new pair(list.get(i).getYear(), list.get(i).getCal());
                        year_list.add(temp);
                        break;
                    }
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
            Log.d("리스트",year_list.get(i).getYear());
            mAdapter.addItem(year_list.get(i).getYear(),year_list.get(i).getCal(), average-year_list.get(i).getCal());
        }
        mListView.setAdapter(mAdapter);

    }
    public class pair{
        String year;
        int cal;
        public pair(String y, int c)
        {
            year=y;
            cal=c;
        }
        public String getYear()
        {
            return year;
        }
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
                return 0;
            }
        }
    }
}

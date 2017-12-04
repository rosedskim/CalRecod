package com.project.kdh.calorieproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 동현 on 2017-11-27.
 */

public class StatisticFragment extends Fragment implements View.OnClickListener{

    Button btn_week;
    Button btn_month;
    Button btn_year;

    StatisticWeekFragment frg_week;
    StatisticMonthFragment frg_month;
    StatisticYearFragment frg_year;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.statistic_fragment_layout,container,false);

        btn_week=(Button)rootView.findViewById(R.id.btn_week);
        btn_month=(Button)rootView.findViewById(R.id.btn_month);
        btn_year=(Button)rootView.findViewById(R.id.btn_year);
        btn_week.setOnClickListener(this);
        btn_month.setOnClickListener(this);
        btn_year.setOnClickListener(this);

        frg_week=new StatisticWeekFragment();
        frg_month=new StatisticMonthFragment();
        frg_year=new StatisticYearFragment();
        //defalut로 기록내역을 보여준다.
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.statistic_container,frg_week).commit();


        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_week:
            {
                statistic_day();
                break;
            }
            case R.id.btn_month:
            {
                statistic_month();
                break;
            }
            case R.id.btn_year:
            {
                statistic_year();
                break;
            }
        }
    }

    public void statistic_day()
    {
        Fragment selected=null;
        selected=frg_week;
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.statistic_container,frg_week).commit();
    }
    public void statistic_month()
    {
        Fragment selected=null;
        selected=frg_month;
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.statistic_container,frg_month).commit();
    }
    public void statistic_year()
    {
        Fragment selected=null;
        selected=frg_year;
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.statistic_container,frg_year).commit();
    }
}

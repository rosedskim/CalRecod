package com.project.kdh.calorieproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by 동현 on 2017-11-27.
 */

public class ChartFragment extends Fragment implements View.OnClickListener{

    Button btn_day_chart;
    Button btn_month_chart;
    Button btn_year_chart;

    ChartDayFragment frg_day;
    ChartMonthFragment frg_month;
    ChartYearFragment frg_year;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.chart_fragment_layout,container,false);

        btn_day_chart=(Button)rootView.findViewById(R.id.btn_day_chart);
        btn_month_chart=(Button)rootView.findViewById(R.id.btn_month_chart);
        btn_year_chart=(Button)rootView.findViewById(R.id.btn_year_chart);
        btn_day_chart.setOnClickListener(this);
        btn_month_chart.setOnClickListener(this);
        btn_year_chart.setOnClickListener(this);

        frg_day=new ChartDayFragment();
        frg_month=new ChartMonthFragment();
        frg_year=new ChartYearFragment();

        //default 일별 칼로리 Fragment
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.chart_container,frg_month).commit();

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.btn_day_chart:
            {
                chart_day();
                break;
            }
            case R.id.btn_month_chart:
            {
                chart_month();
                break;
            }
            case R.id.btn_year_chart:
            {
                chart_year();
                break;
            }
        }
    }
    public void chart_day()
    {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.chart_container,frg_day).commit();
    }
    public void chart_month()
    {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.chart_container,frg_month).commit();
    }
    public void chart_year()
    {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.chart_container,frg_year).commit();
    }
}

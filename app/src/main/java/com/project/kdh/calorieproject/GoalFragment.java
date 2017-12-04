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

public class GoalFragment extends Fragment implements View.OnClickListener{
    Button btn_day_goal;
    Button btn_month_goal;
    Button btn_year_goal;

    GoalDayFragment frg_day;
    GoalMonthFragment frg_month;
    GoalYearFragment frg_year;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.goal_fragment_layout,container,false);

        btn_day_goal=(Button)rootView.findViewById(R.id.btn_day_goal);
        btn_month_goal=(Button)rootView.findViewById(R.id.btn_month_goal);
        btn_year_goal=(Button)rootView.findViewById(R.id.btn_year_goal);
        btn_day_goal.setOnClickListener(this);
        btn_month_goal.setOnClickListener(this);
        btn_year_goal.setOnClickListener(this);

        frg_day=new GoalDayFragment();
        frg_month=new GoalMonthFragment();
        frg_year=new GoalYearFragment();

        //default 일별 칼로리 Fragment
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.goal_container,frg_day).commit();

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.btn_day_goal:
            {
                goal_day();
                break;
            }
            case R.id.btn_month_goal:
            {
                goal_month();
                break;
            }
            case R.id.btn_year_goal:
            {
                goal_year();
                break;
            }
        }
    }
    public void goal_day()
    {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.goal_container,frg_day).commit();
    }
    public void goal_month()
    {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.goal_container,frg_month).commit();
    }
    public void goal_year()
    {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.goal_container,frg_year).commit();
    }
}

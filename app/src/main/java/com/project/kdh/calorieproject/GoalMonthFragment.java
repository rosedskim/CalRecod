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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by 동현 on 2017-12-03.
 */

public class GoalMonthFragment extends Fragment implements View.OnClickListener{

    Button btn_month_left;
    Button btn_month_right;
    Button btn_month_date;
    Button btn_set_goal2;

    TextView txt_month_goal;
    TextView txt_month_cal;
    TextView txt_month_remain;
    TextView txt_month_possible;

    DBHelper dbHelper;

    Calendar cal;

    int year;
    int month;
    int day;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.goal_fragment_month_layout,container,false);

        btn_month_left=(Button)rootView.findViewById(R.id.btn_month_left);
        btn_month_right=(Button)rootView.findViewById(R.id.btn_month_right);
        btn_month_date=(Button)rootView.findViewById(R.id.btn_month_date);
        btn_month_left.setOnClickListener(this);
        btn_month_right.setOnClickListener(this);

        btn_set_goal2=(Button)rootView.findViewById(R.id.btn_set_goal2);
        btn_set_goal2.setOnClickListener(this);

        txt_month_goal=(TextView)rootView.findViewById(R.id.txt_month_goal);
        txt_month_cal=(TextView)rootView.findViewById(R.id.txt_month_cal);
        txt_month_remain=(TextView)rootView.findViewById(R.id.txt_month_remain);
        txt_month_possible=(TextView)rootView.findViewById(R.id.txt_month_possible);

        cal=Calendar.getInstance();

        year=cal.get(Calendar.YEAR);
        month=cal.get(Calendar.MONTH);
        day=cal.get(Calendar.DATE);

        String date=String.valueOf(cal.get(Calendar.YEAR)) + "-" + String.format("%02d",cal.get(Calendar.MONTH)+1);
        btn_month_date.setText(date);

        dataSetting();

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_month_left:
            {
                //month가 1월일떄 연도 감소
                if(month==0)
                {
                    year--;  //연도감소
                    month=11;   //month는 12월로
                    cal.set(Calendar.YEAR,year);
                    cal.set(Calendar.MONTH, month);
                }
                else
                {
                    month--;    //월 감소
                    cal.set(Calendar.YEAR,year);
                    cal.set(Calendar.MONTH, month);
                }
                btn_month_date.setText(""+String.format("%04d",year)+"-"+String.format("%02d",(month+1)));
                dataSetting();
                break;
            }
            case R.id.btn_month_right:
            {
                //month가 12월일떄 연도 증가
                if(month==11)
                {
                    year++;  //연도증가
                    month=0;   //month는 1월로
                    cal.set(Calendar.YEAR,year);
                    cal.set(Calendar.MONTH, month);
                }
                else
                {
                    month++;    //월 증가
                    cal.set(Calendar.YEAR,year);
                    cal.set(Calendar.MONTH, month);
                }
                btn_month_date.setText(""+String.format("%04d",year)+"-"+String.format("%02d",(month+1)));
                dataSetting();
                break;
            }
            case R.id.btn_set_goal2:
            {
                final LinearLayout layout=(LinearLayout)View.inflate(getActivity(), R.layout.goal_set_layout,null);

                final AlertDialog.Builder dlg=new AlertDialog.Builder(view.getContext());
                dlg.setTitle("목표 설정");
                dlg.setView(layout);

                final EditText et_set_goal=(EditText)layout.findViewById(R.id.et_set_goal);
                final String _year=String.valueOf(cal.get(Calendar.YEAR));
                final String _month=String .format("%02d",cal.get(Calendar.MONTH)+1);

                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int cal=Integer.parseInt(et_set_goal.getText().toString());
                        dbHelper= new DBHelper(getActivity(), "MONTHRECORD.db",null,1);
                        SQLiteDatabase db=dbHelper.getWritableDatabase();
                        db.execSQL("INSERT INTO MONTHRECORD VALUES(null, '" + _year + "', '" + _month + "', " + cal + ");");
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.setCancelable(false);
                dlg.show();

                break;
            }
        }
    }
    public void dataSetting()
    {
        final String _year=String.valueOf(cal.get(Calendar.YEAR));
        final String _month=String .format("%02d",cal.get(Calendar.MONTH)+1);
        //목표 칼로리 표시
        dbHelper= new DBHelper(getActivity(), "MONTHRECORD.db",null,1);
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT goal FROM MONTHRECORD WHERE year='"+_year+"' AND month='"+_month+"';",null);
        String goal="";
        while(cursor.moveToNext())
        {
            Log.d("cursor.getString(0)",""+cursor.getInt(0));
            goal=String.valueOf(cursor.getInt(0));
        }
        Log.d("목표",goal);
        txt_month_goal.setText(goal+"Kcal");

        //사용 칼로리 표시
        dbHelper=new DBHelper(getActivity(), "RECORD.db",null,1);
        SQLiteDatabase db2=dbHelper.getReadableDatabase();
        Cursor cursor2=db2.rawQuery("SELECT cal FROM RECORD WHERE year='"+_year+"' AND month='"+_month+"';",null);
        int total_cal=0;
        String goal2="";
        while (cursor2.moveToNext())
        {
            Log.d("cursor2.getString(0)",""+cursor2.getInt(0));
            total_cal+=cursor2.getInt(0);
        }
        goal2=String.valueOf(total_cal);
        Log.d("목표2",goal2);
        txt_month_cal.setText(goal2+"Kcal");

        int max_day=cal.getActualMaximum(Calendar.DATE); //현재 달의 최대날짜
        int now_day=cal.get(Calendar.DATE);

        Calendar cal2=Calendar.getInstance();

        Log.d("aa",goal);
        Log.d("aa",goal2);
        //저장했을떄만
        if(!goal.equals("") &&!goal2.equals("0") ) {
            //남은 칼로리 표시
            int temp = Integer.parseInt(goal) - Integer.parseInt(goal2);
            Log.d("tt",""+cal.get(Calendar.YEAR));
            Log.d("tt",""+cal.get(Calendar.MONTH));
            Log.d("tt",""+cal.get(Calendar.DATE));

            Log.d("tt2",""+cal2.get(Calendar.YEAR));
            Log.d("tt2",""+cal2.get(Calendar.MONTH));
            Log.d("tt2",""+cal2.get(Calendar.DATE));
            txt_month_remain.setText(String.valueOf(temp) + "Kcal");

            //실제 현재 날짜(월)보다 전일때는 사용가능금액 0
            if(cal.get(Calendar.YEAR) <= cal2.get(Calendar.YEAR) && cal.get(Calendar.MONTH)+1 < cal2.get(Calendar.MONTH)+1)
            {
                Log.d("??",""+1);
                txt_month_possible.setText("0Kcal");
            }
            else if(cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal.get(Calendar.MONTH)+1 == cal2.get(Calendar.MONTH)+1)//현재달일때
            {
                Log.d("??",""+2);
                //평균 하루 가능 칼로리
                txt_month_possible.setText(String.valueOf(temp / (max_day - now_day) + "Kcal"));
            }
            else if(cal.get(Calendar.YEAR) >= cal2.get(Calendar.YEAR))//실제 현시간보다 클경우
            {
                int num=cal.getActualMaximum(Calendar.DATE);
                Log.d("??",""+3+num);
                txt_month_possible.setText(String.valueOf(temp/num));
            }
            else if(cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal.get(Calendar.MONTH)+1 > cal2.get(Calendar.MONTH)+1)
            {
                int num=cal.getActualMaximum(Calendar.DATE);
                Log.d("??",""+3+num);
                txt_month_possible.setText(String.valueOf(temp/num));
            }
        }
        else if(!goal.equals("") && goal2.equals("0"))  //목표는 설정했는데 먹은게 없을떄
        {
            Log.d("aa",""+max_day);
            Log.d("aa",""+now_day);
            txt_month_remain.setText(goal+"Kcal");

            if(cal.get(Calendar.YEAR) >= cal2.get(Calendar.YEAR))
            {
                txt_month_possible.setText(Integer.parseInt(goal)/(max_day)+"Kcal");
            }
            else if(cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal.get(Calendar.MONTH)+1 > cal2.get(Calendar.MONTH)+1)
            {
                txt_month_possible.setText(Integer.parseInt(goal)/(max_day)+"Kcal");
            }
            else {
                txt_month_possible.setText(Integer.parseInt(goal) / (max_day - now_day) + "Kcal");
            }

        }
        else //목표설정안했고 먹은거만있을때 또는 목표도설정안했고 먹은것도 없을때
        {
            txt_month_remain.setText("0");
            txt_month_possible.setText("0");
        }
    }
}

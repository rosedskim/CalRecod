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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 동현 on 2017-12-03.
 */

public class GoalDayFragment extends Fragment implements View.OnClickListener {

    Button btn_day_left;
    Button btn_day_right;
    Button btn_day_date;
    Button btn_set_goal;

    TextView txt_day_goal;
    TextView txt_day_cal;
    TextView txt_day_remain;

    DBHelper dbHelper;

    Calendar cal;

    String date;
    String[] temp;
    int year;
    int month;
    int day;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.goal_fragment_day_layout,container,false);

        btn_day_left=(Button)rootView.findViewById(R.id.btn_day_left);
        btn_day_right=(Button)rootView.findViewById(R.id.btn_day_right);
        btn_day_date=(Button)rootView.findViewById(R.id.btn_day_date);
        btn_day_left.setOnClickListener(this);
        btn_day_right.setOnClickListener(this);

        btn_set_goal=(Button)rootView.findViewById(R.id.btn_set_goal);
        btn_set_goal.setOnClickListener(this);

        txt_day_goal=(TextView)rootView.findViewById(R.id.txt_day_goal);
        txt_day_cal=(TextView)rootView.findViewById(R.id.txt_day_cal);
        txt_day_remain=(TextView)rootView.findViewById(R.id.txt_day_remain);

        Date mDate;
        long mNow;
        SimpleDateFormat mFormat=new SimpleDateFormat("yyyy-MM-dd");
        mNow=System.currentTimeMillis();
        mDate=new Date(mNow);
        date=mFormat.format(mDate);
        temp=date.split("-");
        year=Integer.parseInt(temp[0]);
        month=Integer.parseInt(temp[1])-1;
        day=Integer.parseInt(temp[2]);

        cal=Calendar.getInstance();

        btn_day_date.setText(date);

        dataSetting();

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_day_left:
            {
                //day가 1일때 왼쪽누르면 지난달의 마지막 날이된다
                if(day==1)
                {
                    //month도 1일때는 연도를 감소
                    if(month==0)
                    {
                        year--; //연도감소
                        month=11;   //month는 12월
                        day=31;     //어짜피 12월이니까 day는 31일
                        cal.set(Calendar.YEAR,year);
                        cal.set(Calendar.MONTH, month);
                        cal.set(Calendar.DATE,day);
                    }
                    else    //month가 1이 아닐떄
                    {
                        month--;    //month 감소
                        cal.set(Calendar.YEAR,year);
                        cal.set(Calendar.MONTH, month);
                        day=cal.getActualMaximum(Calendar.DATE);    //day는 감소한 달의 최대 날
                        cal.set(Calendar.DATE,day);
                    }
                }
                else    //day가 1이 아닐때
                {
                    day--;  //day감소
                    cal.set(Calendar.YEAR,year);
                    cal.set(Calendar.MONTH, month);
                    cal.set(Calendar.DATE,day);
                }
                btn_day_date.setText(""+String.format("%04d",year)+"-"+String.format("%02d",(month+1))+"-"+String.format("%02d",day));
                Log.d("날짜",""+cal.get(Calendar.YEAR));
                Log.d("날짜",""+cal.get(Calendar.MONTH));
                Log.d("날짜",""+cal.get(Calendar.DATE));
                dataSetting();
                break;
            }
            case R.id.btn_day_right:
            {
                //day가 마지막날일때 오른쪽누르면 다음달의 1일이된다
                if(day== cal.getActualMaximum(Calendar.DATE))
                {
                    //month도 12월일때는 연도를 증가
                    if(month==11)
                    {
                        year++; //연도증가
                        month=0;   //month는 1월
                        day=1;     //새로운달 1일
                        cal.set(Calendar.YEAR,year);
                        cal.set(Calendar.MONTH, month);
                        cal.set(Calendar.DATE,day);
                    }
                    else    //month가 12월 아닐때
                    {
                        month++;    //month 증가
                        day=1;    //day는 1일
                        cal.set(Calendar.YEAR,year);
                        cal.set(Calendar.MONTH, month);
                        cal.set(Calendar.DATE,day);
                    }
                }
                else    //day가 마지막날이 아닐때는 증가만
                {
                    day++;  //day증가
                    cal.set(year,month,day);
                }
                btn_day_date.setText(""+String.format("%04d",year)+"-"+String.format("%02d",(month+1))+"-"+String.format("%02d",day));
                dataSetting();
                break;
            }
            case R.id.btn_set_goal:
            {
                final LinearLayout layout=(LinearLayout)View.inflate(getActivity(), R.layout.goal_set_layout,null);

                final AlertDialog.Builder dlg=new AlertDialog.Builder(view.getContext());
                dlg.setTitle("목표 설정");
                dlg.setView(layout);

                final EditText et_set_goal=(EditText)layout.findViewById(R.id.et_set_goal);
                final String _year=String.valueOf(cal.get(Calendar.YEAR));
                final String _month=String .format("%02d",cal.get(Calendar.MONTH)+1);
                final String _day=String.format("%02d",cal.get(Calendar.DATE));
                Log.d("목표2",_year);
                Log.d("목표2",_month);
                Log.d("목표2",_day);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int cal=Integer.parseInt(et_set_goal.getText().toString());
                        dbHelper= new DBHelper(getActivity(), "DAYRECORD.db",null,1);
                        SQLiteDatabase db=dbHelper.getWritableDatabase();
                        db.execSQL("INSERT INTO DAYRECORD VALUES(null, '" + _year + "', '" + _month + "', '" + _day + "', " + cal + ");");
                        Log.d("???","insert 되었");
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
        final String _day=String.format("%02d",cal.get(Calendar.DATE));
        Log.d("목표",_year);
        Log.d("목표",_month);
        Log.d("목표",_day);
        //목표 칼로리 표시
        dbHelper= new DBHelper(getActivity(), "DAYRECORD.db",null,1);
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT goal FROM DAYRECORD WHERE year='"+_year+"' AND month='"+_month+"' AND day='"+_day+"';",null);
        String goal="";
        while(cursor.moveToNext())
        {
            Log.d("cursor.getString(0)",""+cursor.getInt(0));
            goal=String.valueOf(cursor.getInt(0));
        }
        Log.d("목표",goal);
        txt_day_goal.setText(goal+"Kcal");

        //사용 칼로리 표시
        dbHelper=new DBHelper(getActivity(), "RECORD.db",null,1);
        SQLiteDatabase db2=dbHelper.getReadableDatabase();
        Cursor cursor2=db2.rawQuery("SELECT cal FROM RECORD WHERE year='"+_year+"' AND month='"+_month+"' AND date='"+_day+"';",null);
        String goal2="";
        while (cursor2.moveToNext())
        {
            Log.d("cursor2.getString(0)",cursor2.getString(0));
            goal2=cursor2.getString(0);
        }
        Log.d("목표2",goal2);
        txt_day_cal.setText(goal2+"Kcal");

        //저장했을떄만
        if(!goal.equals("") &&!goal2.equals("") ) {
            //남은 칼로리 표시
            int temp = Integer.parseInt(goal) - Integer.parseInt(goal2);
            Log.d("tt",goal);
            Log.d("tt",goal2);
            Log.d("tt",""+temp);
            txt_day_remain.setText(String.valueOf(temp) + "Kcal");
        }
        else if(!goal.equals("") && goal2.equals(""))
        {
            txt_day_remain.setText(goal+"Kcal");
        }
        else
        {
            txt_day_remain.setText("0");
        }
    }
}

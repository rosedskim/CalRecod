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

public class GoalYearFragment extends Fragment implements View.OnClickListener{

    Button btn_year_left;
    Button btn_year_right;
    Button btn_year_date;
    Button btn_set_goal3;

    TextView txt_year_goal;
    TextView txt_year_cal;
    TextView txt_year_remain;
    TextView txt_year_possible;

    DBHelper dbHelper;

    Calendar cal;

    int year;
    int month;
    int day;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.goal_fragment_year_layout,container,false);

        btn_year_left=(Button)rootView.findViewById(R.id.btn_year_left);
        btn_year_right=(Button)rootView.findViewById(R.id.btn_year_right);
        btn_year_date=(Button)rootView.findViewById(R.id.btn_year_date);
        btn_year_left.setOnClickListener(this);
        btn_year_right.setOnClickListener(this);

        btn_set_goal3=(Button)rootView.findViewById(R.id.btn_set_goal3);
        btn_set_goal3.setOnClickListener(this);

        txt_year_goal=(TextView)rootView.findViewById(R.id.txt_year_goal);
        txt_year_cal=(TextView)rootView.findViewById(R.id.txt_year_cal);
        txt_year_remain=(TextView)rootView.findViewById(R.id.txt_year_remain);
        txt_year_possible=(TextView)rootView.findViewById(R.id.txt_year_possible);

        cal=Calendar.getInstance();

        year=cal.get(Calendar.YEAR);
        month=cal.get(Calendar.MONTH);
        day=cal.get(Calendar.DATE);

        String date=String.valueOf(cal.get(Calendar.YEAR));
        btn_year_date.setText(date);

        dataSetting();

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_year_left:
            {
                //year감소
                year--;
                cal.set(Calendar.YEAR,year);
                btn_year_date.setText(""+String.format("%04d",year));
                dataSetting();
                break;
            }
            case R.id.btn_year_right:
            {
                year++;
                cal.set(Calendar.YEAR,year);
                btn_year_date.setText(""+String.format("%04d",year));
                dataSetting();
                break;
            }
            case R.id.btn_set_goal3:
            {
                final LinearLayout layout=(LinearLayout)View.inflate(getActivity(), R.layout.goal_set_layout,null);

                final AlertDialog.Builder dlg=new AlertDialog.Builder(view.getContext());
                dlg.setTitle("목표 설정");
                dlg.setView(layout);

                final EditText et_set_goal=(EditText)layout.findViewById(R.id.et_set_goal);
                final String _year=String.valueOf(cal.get(Calendar.YEAR));

                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int cal=Integer.parseInt(et_set_goal.getText().toString());
                        dbHelper= new DBHelper(getActivity(), "YEARRECORD.db",null,1);
                        SQLiteDatabase db=dbHelper.getWritableDatabase();
                        db.execSQL("INSERT INTO YEARRECORD VALUES(null, '" + _year + "', " + cal + ");");
                        dataSetting();
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

        Log.d("목표",_year);
        //목표 칼로리 표시
        dbHelper= new DBHelper(getActivity(), "YEARRECORD.db",null,1);
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT goal FROM YEARRECORD WHERE year='"+_year+"';",null);
        String goal="";
        while(cursor.moveToNext())
        {
            Log.d("cursor.getString(0)",""+cursor.getInt(0));
            goal=String.valueOf(cursor.getInt(0));
        }
        Log.d("목표",goal);
        txt_year_goal.setText(goal+"Kcal");

        //사용 칼로리 표시
        dbHelper=new DBHelper(getActivity(), "RECORD.db",null,1);
        SQLiteDatabase db2=dbHelper.getReadableDatabase();
        Cursor cursor2=db2.rawQuery("SELECT cal FROM RECORD WHERE year='"+_year+"';",null);
        int total_cal=0;
        String goal2="";
        while (cursor2.moveToNext())
        {
            Log.d("cursor2.getInt(0)",""+cursor2.getInt(0));
            total_cal+=cursor2.getInt(0);
        }
        goal2=String.valueOf(total_cal);
        Log.d("목표2",goal2);
        txt_year_cal.setText(goal2+"Kcal");

        Calendar c1=Calendar.getInstance();
        year=Integer.parseInt(_year);
        month=c1.get(Calendar.MONTH)+1;
        day=c1.get(Calendar.DATE);
        Calendar c2=Calendar.getInstance();
        c1.set(year,month,day);
        c2.set(c2.get(Calendar.YEAR),12,31);

        long d1=c1.getTime().getTime();
        long d2=c2.getTime().getTime();

        int days=(int)((d2-d1)/(1000*60*60*24));

        //저장했을떄만    //목표설정했고 먹은 칼로리가 0이 아닐때
        if(!goal.equals("") &&!goal2.equals("0") ) {
            //남은 칼로리 표시
            int temp = Integer.parseInt(goal) - Integer.parseInt(goal2);
            txt_year_remain.setText(String.valueOf(temp) + "Kcal");
            txt_year_possible.setText(String.valueOf(temp/days)+"Kcal");
        }
        else if(!goal.equals("") && goal2.equals("0"))  //목표는 설정했는데 먹은게 없을떄
        {
            int temp = Integer.parseInt(goal) - Integer.parseInt(goal2);
            txt_year_remain.setText(goal+"Kcal");
            txt_year_possible.setText(String.valueOf(temp/days)+"Kcal");
        }
        else //목표설정안했고 먹은거만있을때 또는 목표도설정안했고 먹은것도 없을때
        {
            txt_year_remain.setText("0");
            txt_year_possible.setText("0");
        }
    }
}

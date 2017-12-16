package com.project.kdh.calorieproject;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 동현 on 2017-11-27.
 */

public class RecordFragment  extends Fragment implements View.OnClickListener{
    @Nullable
    DBHelper dbHelper;

    //날짜 이동 버튼
    private Button btn_left;
    private Button btn_right;
    private Button btn_date;
    private Button btn_cal;

    Calendar cal;

    Date mDate;
    long mNow;
    SimpleDateFormat mFormat=new SimpleDateFormat("yyyy-MM-dd");

    String date;
    String[] temp;
    int year;
    int month;
    int real_month;
    int day;

    Integer total_cal=0;

    private ListView mListView;
    private LinkedList<Pair<String ,Integer>> list;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("호출","???");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.record_fragment_layout,container,false);
        dbHelper= new DBHelper(getActivity(), "RECORD.db",null,1);

        //날짜이동 버튼 연결
        btn_left=(Button)rootView.findViewById(R.id.btn_left);
        btn_right=(Button)rootView.findViewById(R.id.btn_right);
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        btn_date=(Button)rootView.findViewById(R.id.btn_date);
        btn_date.setOnClickListener(this);
        btn_cal=(Button)rootView.findViewById(R.id.btn_cal);
        btn_cal.setOnClickListener(this);

        //날짜 현재로 바꾸기
        mNow=System.currentTimeMillis();
        mDate=new Date(mNow);
        date=mFormat.format(mDate);
        temp=date.split("-");
        year=Integer.parseInt(temp[0]);
        month=Integer.parseInt(temp[1])-1;
        real_month=month+1;
        day=Integer.parseInt(temp[2]);

        btn_date.setText(""+year+"년 "+real_month+"월");

        //요일 구하기
        cal=Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, day);
        int day_of_week=cal.get(Calendar.DAY_OF_WEEK);


        mListView=(ListView)rootView.findViewById(R.id.li_record);
        list=new LinkedList<Pair<String, Integer>>();

        dataSetting();

        return rootView;
    }

    @Override
    public void onResume() {
        Log.d("재호출","??");
        dataSetting();
        super.onResume();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_left:
            {
                //1월 일때
                if(month==0)
                {
                    year=year-1;
                    month=11;
                    real_month=12;
                    cal.set(Calendar.YEAR,year);
                    cal.set(Calendar.MONTH, month);
                }
                //다른 달일떄
                else
                {
                    cal.set(Calendar.YEAR,year);
                    cal.set(Calendar.MONTH, month);
                    month--;
                    real_month--;
                }
                btn_date.setText(""+year+"년 "+real_month+"월");

                dataSetting();


                break;
            }
            case R.id.btn_right:
            {
                //12월달 일떄 month==11, real_month==12
                if(month==11)
                {
                    month=0;
                    real_month=1;
                    year=year+1;
                    cal.set(Calendar.YEAR,year);
                    cal.set(Calendar.MONTH, month);
                }
                else
                {
                    month++;
                    real_month++;
                    cal.set(Calendar.YEAR,year);
                    cal.set(Calendar.MONTH, month);
                }
                btn_date.setText(""+year+"년 "+real_month+"월");

                dataSetting();
                break;
            }


        }
    }

    public void dataSetting()
    {
        RecordFragmentAdapter mAdpater=new RecordFragmentAdapter(getActivity());

        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH, month);
        int size=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for(int i=size; i>=1; i--)
        {
            total_cal=0;
            cal.set(Calendar.DATE, i);
            int day_of_week=cal.get(Calendar.DAY_OF_WEEK);

            String temp_date_day2="";
            switch (day_of_week)
            {
                case 1:
                {
                    temp_date_day2="일";
                    break;
                }
                case 2:
                {
                    temp_date_day2="월";
                    break;
                }
                case 3:
                {
                    temp_date_day2="화";
                    break;
                }
                case 4:
                {
                    temp_date_day2="수";
                    break;
                }
                case 5:
                {
                    temp_date_day2="목";
                    break;
                }
                case 6:
                {
                    temp_date_day2="금";
                    break;
                }
                case 7:
                {
                    temp_date_day2="토";
                    break;
                }

            }

            String result=String.valueOf(cal.get(Calendar.YEAR)) + "-" +String.format("%02d",cal.get(Calendar.MONTH)+1)+"-"+String.format("%02d",cal.get(Calendar.DATE));
            String year=String.valueOf(cal.get(Calendar.YEAR));
            String month=String.format("%02d",cal.get(Calendar.MONTH)+1);
            String date=String.format("%02d",cal.get(Calendar.DATE));

            Pair<String, Integer> temp_pair=new Pair<String, Integer>(result, total_cal);
            list.add(temp_pair);

            SQLiteDatabase db=dbHelper.getReadableDatabase();
            Cursor cursor=db.rawQuery("SELECT cal FROM RECORD WHERE year='"+year+"' AND month='"+month+"' AND date='"+date+"';",null);

            while(cursor.moveToNext())
            {
                Log.d("커서", cursor.getString(0));
                total_cal+=cursor.getInt(0);
            }


            mAdpater.addItem(String.valueOf(cal.get(Calendar.YEAR)),String.format("%02d",cal.get(Calendar.MONTH)+1),String.format("%02d",cal.get(Calendar.DATE)),temp_date_day2,total_cal);
        }
        mListView.setAdapter(mAdpater);

    }

}

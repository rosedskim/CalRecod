package com.project.kdh.calorieproject;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
/**
 * Created by 동현 on 2017-11-27.
 */

public class RecordFragmentAdapter extends BaseAdapter {
    private LinkedList<Pair<String,Integer>> mItems=new LinkedList<>();
    DBHelper dbHelper;
    private Context maincon;
    public RecordFragmentAdapter(Context context)
    {
        this.maincon=context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Pair<String,Integer> getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        dbHelper= new DBHelper(maincon, "RECORD.db",null,1);
        final Context context=viewGroup.getContext();
        if(view==null)
        {
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.record_fragment_list_layout,viewGroup,false);
        }
        LinearLayout li=(LinearLayout)view.findViewById(R.id.linear_list);
        TextView record_date=(TextView)view.findViewById(R.id.record_date);
        TextView record_cal=(TextView)view.findViewById(R.id.record_cal);

        li.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFoodList adapter=new DateFoodList();

                ListView listView;
                final LinearLayout layout=(LinearLayout)View.inflate(context, R.layout.date_food_layout,null);
                final AlertDialog.Builder dlg=new AlertDialog.Builder(view.getContext());
                dlg.setTitle("음식 기록 정보");
                dlg.setView(layout);
                dlg.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    public boolean onKey(DialogInterface dialog,
                                         int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            dialog.dismiss();
                            return true;
                        }
                        return false;
                    }
                });

                listView=(ListView)layout.findViewById(R.id.food_list_record);

                //기록에 대한 날짜
                String temp_date=mItems.get(i).first;
                String record_str1=temp_date.substring(0,4);
                String record_str2=temp_date.substring(5,7);
                String record_str3=temp_date.substring(8,10);
                String total=record_str1+"-"+record_str2+"-"+record_str3;

                //시간 음식이름 칼로리 리스트로 표시해주기
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                Cursor cursor=db.rawQuery("SELECT time, food, cal FROM RECORD WHERE year='"+record_str1+"' AND month='"+record_str2+"' AND date='"+record_str3+"';",null);


                while(cursor.moveToNext())
                {
                    Log.d("커서", cursor.getString(0));
                    String str1=cursor.getString(0);
                    String str2=cursor.getString(1);
                    int str3=cursor.getInt(2);
                    adapter.addItem(str1,str2,str3);
                }

                listView.setAdapter(adapter);
                dlg.setCancelable(false);
                dlg.show();

            }
        });

        li.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final LinearLayout layout=(LinearLayout)View.inflate(context, R.layout.add_food_layout,null);

                final AlertDialog.Builder dlg=new AlertDialog.Builder(view.getContext());
                dlg.setTitle("음식내역");
                dlg.setView(layout);


                Button food_date=(Button)layout.findViewById(R.id.food_date);
                Button food_time=(Button)layout.findViewById(R.id.food_time);


                //현재 날짜 현재시간으로 기록날짜 초기화
                long tNow=System.currentTimeMillis();
                Date tDate=new Date(tNow);
                SimpleDateFormat tFormat=new SimpleDateFormat("yyyy-MM-dd");
                String str=tFormat.format(tDate);
                String now_year=str.substring(0,4);
                String now_month=str.substring(5,7);
                String now_date=str.substring(8,10);

                //기록에 대한 날짜
                String temp_date=mItems.get(i).first;
                final String record_str1=temp_date.substring(0,4);
                final String record_str2=temp_date.substring(5,7);
                final String record_str3=temp_date.substring(8,10);
                String total=record_str1+"-"+record_str2+"-"+record_str3;
                String time="";
                //날짜 같으면 현재시간으로
                if(now_year.equals(record_str1) && now_month.equals(record_str2) && now_date.equals(record_str3))
                {
                    SimpleDateFormat tFormat2=new SimpleDateFormat("HH:mm");
                    String str2=tFormat2.format(tDate);
                    Log.d("??", "true");
                    food_date.setText(str);
                    food_time.setText(str2);
                    time=str2;
                }
                else //날짜가 현재랑 다르면 시간을 모두 00:00으로
                {
                    Log.d("??", "false");
                    food_date.setText(total);
                    food_time.setText(String.format("%02d:%02d",0,0));
                    time=String.format("%02d:%02d",0,0);
                }

                final String finalTime = time;
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                    // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                    public void onClick(DialogInterface dialog, int whichButton){
                        //원하는 클릭 이벤트를 넣으시면 됩니다.
                        EditText et_food_name=(EditText)layout.findViewById(R.id.et_food_name);
                        EditText et_food_cal=(EditText)layout.findViewById(R.id.et_food_cal);
                        String food_name=et_food_name.getText().toString();

                        int food_cal=Integer.parseInt(et_food_cal.getText().toString());

                        SQLiteDatabase db=dbHelper.getWritableDatabase();

                        Record record=new Record(record_str1, record_str2,record_str3, food_name, food_cal, finalTime);

                        db.execSQL("INSERT INTO RECORD VALUES(null, '" + record.getYear() + "', '" + record.getMonth() + "', '" + record.getDate() + "', '" + record.getFood() + "', " + String.valueOf(record.getCal()) + ", '"+record.getTime()+"');");


                    }
                });
                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener(){
                    // 취소 버튼 클릭시 설정, 왼쪽 버튼입니다.
                    public void onClick(DialogInterface dialog, int whichButton){
                        //원하는 클릭 이벤트를 넣으시면 됩니다.
                    }
                });


                dlg.show();

                return false;
            }
        });


        Pair<String,Integer> temp=getItem(i);

        record_date.setText(temp.first);
        record_cal.setText(String.valueOf(temp.second)+"kcal");

        return view;
    }

    public void addItem(String date_year, String date_month, String date_day, String date_day2, int cal)
    {
        String total=date_year+"-"+date_month+"-"+date_day+" ("+date_day2+")";
        Log.d("date",total);
        Pair<String, Integer> s=new Pair<String, Integer>(total,cal);
        mItems.add(s);
    }
}

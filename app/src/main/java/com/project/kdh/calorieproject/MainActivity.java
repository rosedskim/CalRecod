package com.project.kdh.calorieproject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private DBHelper dbHelper;

    //큰 분류 4가지
    private Button btn_record_list;
    private Button btn_statistic;
    private Button btn_chart;
    private Button btn_goal;

    //Fragment 4개
    RecordFragment frg_record;
    StatisticFragment frg_statistic;
    ChartFragment frg_chart;
    GoalFragment frg_goal;

    //search_food_dialog 쓰이는 변수들
    private boolean[] foodChkList={false,false,false,false,false};
    private String[] foodStrList={"한식","중식","일식","양식","기타"};
    private int selectItem;

    //음식 정보
    private HashMap<String, LinkedList<Food>> foodInfo=new HashMap<String, LinkedList<Food>>();
    private LinkedList<Food> korea_food=new LinkedList<Food>();
    private LinkedList<Food> china_food=new LinkedList<Food>();
    private LinkedList<Food> japan_food=new LinkedList<Food>();
    private LinkedList<Food> western_food=new LinkedList<Food>();
    private LinkedList<Food> etc_food=new LinkedList<Food>();
    private String[] korea_foodName={"쌀밥","튀김","김치찌개","비빔밥","김치볶음밥","김밥","달걀말이","김","미역국","찜닭","갈비탕","현미밥","흑미밥","보리밥","된장찌개","두부","콩나물국","전","떡국","잡채","삼겹살","부대찌개","호박"};
    private int[] korea_foodCal={300,260,250,430,500,636,2001,20,150,400,500,250,200,230,250,200,150,400,600,250,600,450,100};
    private String[] china_foodName={"짜장면","짬뽕","잡채밥","볶음밥","탕수육","우동","마파두부"};
    private int[] china_foodCal={670,404,715,692,616,385,358};
    private String[] japan_foodName={"초밥","회덮밥","메밀국수","어묵","낫토",};
    private int[] japan_foodCal={600,520,430,100,212};
    private String[] western_foodName={"스테이크","오믈렛","스파게티","스프","그라탕",};
    private int[] western_foodCal={800,150,630,80,414};
    private String[] etc_foodName={"햄버거","피자","치킨","탄산음료","과자"};
    private int[] etc_foodCal={700,1000,800,200,300};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        dbHelper= new DBHelper(this, "RECORD.db",null,1);
        dbHelper.test();
        //Button 연결
        btn_record_list=(Button) findViewById(R.id.btn_record_list);
        btn_statistic=(Button)findViewById(R.id.btn_statistic);
        btn_chart=(Button)findViewById(R.id.btn_chart);
        btn_goal=(Button)findViewById(R.id.btn_goal);
        btn_record_list.setOnClickListener(this);
        btn_statistic.setOnClickListener(this);
        btn_chart.setOnClickListener(this);
        btn_goal.setOnClickListener(this);

        //Fragment 연결
        frg_record=new RecordFragment();
        frg_statistic=new StatisticFragment();
        frg_chart=new ChartFragment();
        frg_goal=new GoalFragment();

        //defalut로 기록내역을 보여준다.
        getSupportFragmentManager().beginTransaction().replace(R.id.container,frg_record).commit();

        //한국 음식 정보 넣기
        for(int i=0; i<korea_foodName.length; i++)
        {
            korea_food.add(new Food(korea_foodName[i], korea_foodCal[i]));
        }
        //중국 음식 정보 넣기
        for(int i=0; i<china_foodName.length; i++)
        {
            china_food.add(new Food(china_foodName[i], china_foodCal[i]));
        }
        //일식 음식 정보 넣기
        for(int i=0; i<japan_foodName.length; i++)
        {
            japan_food.add(new Food(japan_foodName[i], japan_foodCal[i]));
        }
        //양식 음식 정보 넣기
        for(int i=0; i<western_foodName.length; i++)
        {
            western_food.add(new Food(western_foodName[i], western_foodCal[i]));
        }
        //기타 음식 정보 넣기
        for(int i=0; i<etc_foodName.length; i++)
        {
            etc_food.add(new Food(etc_foodName[i], etc_foodCal[i]));
        }

        //음식정보 넣기
        foodInfo.put("한식",korea_food);
        foodInfo.put("중식",china_food);
        foodInfo.put("일식",japan_food);
        foodInfo.put("양식",western_food);
        foodInfo.put("기타",etc_food);

        //칼로리 바꾸기
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.search_menu:
            {
                search_menu_dialog();
                break;
            }
            case R.id.food_menu:
            {
                search_food_dialog();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_record_list:
            {
                //다른 버튼들은 다 색깔 다르게
                btn_statistic.setTextColor(getResources().getColorStateList(R.color.not_selector_text));
                btn_chart.setTextColor(getResources().getColorStateList(R.color.not_selector_text));
                btn_goal.setTextColor(getResources().getColorStateList(R.color.not_selector_text));

                btn_record_list.setTextColor(getResources().getColorStateList(R.color.selector_text));
                //record Fragment 보여주기
                Fragment selected=null;
                selected=frg_record;
                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
                break;
            }
            case R.id.btn_statistic:
            {
               //다른 버튼들은 다 색깔 다르게
                btn_record_list.setTextColor(getResources().getColorStateList(R.color.not_selector_text));
                btn_chart.setTextColor(getResources().getColorStateList(R.color.not_selector_text));
                btn_goal.setTextColor(getResources().getColorStateList(R.color.not_selector_text));

                btn_statistic.setTextColor(getResources().getColorStateList(R.color.selector_text));
                //statistic Fragment 보여주기
                Fragment selected=null;
                selected=frg_statistic;
                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
                break;
            }
            case R.id.btn_chart:
            {
                //다른 버튼들은 다 색깔 다르게
                btn_record_list.setTextColor(getResources().getColorStateList(R.color.not_selector_text));
                btn_statistic.setTextColor(getResources().getColorStateList(R.color.not_selector_text));
                btn_goal.setTextColor(getResources().getColorStateList(R.color.not_selector_text));

                btn_chart.setTextColor(getResources().getColorStateList(R.color.selector_text));
                //chart Fragment 보여주기
                Fragment selected=null;
                selected=frg_chart;
                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
                break;
            }
            case R.id.btn_goal:
            {
                //다른 버튼들은 다 색깔 다르게
                btn_record_list.setTextColor(getResources().getColorStateList(R.color.not_selector_text));
                btn_statistic.setTextColor(getResources().getColorStateList(R.color.not_selector_text));
                btn_chart.setTextColor(getResources().getColorStateList(R.color.not_selector_text));

                btn_goal.setTextColor(getResources().getColorStateList(R.color.selector_text));
                //goal Fragment 보여주기
                Fragment selected=null;
                selected=frg_goal;
                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
                break;
            }
        }
    }
    //검색 메뉴바 눌렀을떄 나타나는 dialog
    public void search_menu_dialog()
    {
        final LinearLayout layout=(LinearLayout)View.inflate(this, R.layout.search_record,null);

        //검색메뉴에서 입력텍스트
        final EditText et_search=(EditText)layout.findViewById(R.id.et_search_record);
        //검색 메뉴에서 검색 날짜 버튼
        final Button btn_date_start=(Button)layout.findViewById(R.id.btn_date_start);
        final Button btn_date_end=(Button)layout.findViewById(R.id.btn_date_end);

        AlertDialog.Builder dlg=new AlertDialog.Builder(this);
        dlg.setTitle("정보 검색");
        dlg.setView(layout);
        dlg.setIcon(R.drawable.search);
        dlg.setCancelable(false);
        dlg.setPositiveButton("검색", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //SQlite db를 이용하여 검색
                String input=et_search.getText().toString();
                String total=btn_date_start.getText().toString().substring(0,4)+btn_date_start.getText().toString().substring(5,7)+btn_date_start.getText().toString().charAt(8)+btn_date_start.getText().toString().charAt(9);
                int start=Integer.parseInt(total);

                String total2=btn_date_end.getText().toString().substring(0,4)+btn_date_end.getText().toString().substring(5,7)+btn_date_end.getText().toString().charAt(8)+btn_date_end.getText().toString().charAt(9);
                int end=Integer.parseInt(total2);

                LinkedList<Record> temp_record_list=new LinkedList<Record>();

                SQLiteDatabase db=dbHelper.getReadableDatabase();
                Cursor cursor=db.rawQuery("SELECT _id, year, month, date, food, cal FROM RECORD;",null);


                while(cursor.moveToNext())
                {

                    String total3=cursor.getString(1)+cursor.getString(2)+cursor.getString(3);
                    int _date=Integer.parseInt(total3);

                    String _food=cursor.getString(4).toString();
                    Log.d("검색음식", _food);
                    Log.d("input음식", input);
                    //음식 이름 같고 날짜가 그 사이
                    if((start<=_date && _date<=end) && input.equals(_food))
                    {
                        Log.d("찾은음식", _food);
                        Record record = new Record(cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4),cursor.getInt(5),"");
                        temp_record_list.add(record);
                    }

                }
                Descending descending=new Descending();
                Collections.sort(temp_record_list,descending);

                Log.d("크기", ""+temp_record_list.size());
                RecordList r_list=new RecordList(temp_record_list);
                Intent intent=new Intent(MainActivity.this, SearchedRecordActivity.class);
                intent.putExtra("list",r_list);

                startActivity(intent);

            }
        });
        dlg.setNegativeButton("취소",null);
        dlg.show();


        btn_date_start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener mDateSetListener=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        if(1<=i1+1 && i1+1<=9 )
                        {
                            if(1<=i2 && i2<=9)
                            {
                                btn_date_start.setText("" + i + "-0" + (i1 + 1) + "-0" + i2);
                            }
                            else
                            {
                                btn_date_start.setText("" + i + "-0" + (i1 + 1) + "-" + i2);
                            }
                        }
                        else
                        {
                            if(1<=i2 && i2<=9)
                            {
                                btn_date_start.setText("" + i + "-" + (i1 + 1) + "-0" + i2);
                            }
                            else
                            {
                                btn_date_start.setText("" + i + "-" + (i1 + 1) + "-" + i2);
                            }
                        }
                    }
                };
                Date mDate;
                long mNow;
                SimpleDateFormat mFormat=new SimpleDateFormat("yyyy-MM-dd");
                mNow=System.currentTimeMillis();
                mDate=new Date(mNow);
                String date=mFormat.format(mDate);
                String[] temp=date.split("-");
                int year=Integer.parseInt(temp[0]);
                int month=Integer.parseInt(temp[1])-1;
                int day=Integer.parseInt(temp[2]);

                DatePickerDialog date_dlg=new DatePickerDialog(MainActivity.this,mDateSetListener,year,month,day);
                date_dlg.show();
            }
        });
        btn_date_end.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener mDateSetListener=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        if(1<=i1+1 && i1+1<=9 )
                        {
                            if(1<=i2 && i2<=9)
                            {
                                btn_date_end.setText("" + i + "-0" + (i1 + 1) + "-0" + i2);
                            }
                            else
                            {
                                btn_date_end.setText("" + i + "-0" + (i1 + 1) + "-" + i2);
                            }
                        }
                        else
                        {
                            if(1<=i2 && i2<=9)
                            {
                                btn_date_end.setText("" + i + "-" + (i1 + 1) + "-0" + i2);
                            }
                            else
                            {
                                btn_date_end.setText("" + i + "-" + (i1 + 1) + "-" + i2);
                            }
                        }
                    }
                };
                Date mDate;
                long mNow;
                SimpleDateFormat mFormat=new SimpleDateFormat("yyyy-MM-dd");
                mNow=System.currentTimeMillis();
                mDate=new Date(mNow);
                String date=mFormat.format(mDate);
                String[] temp=date.split("-");
                int year=Integer.parseInt(temp[0]);
                int month=Integer.parseInt(temp[1])-1;
                int day=Integer.parseInt(temp[2]);

                DatePickerDialog date_dlg=new DatePickerDialog(MainActivity.this,mDateSetListener,year,month,day);
                date_dlg.show();
            }
        });

    }
    //음식 메뉴바 눌렀을때 나타나는 dialog
    public void search_food_dialog()
    {
        AlertDialog.Builder dlg=new AlertDialog.Builder(this);
        dlg.setTitle("음식 검색");
        dlg.setIcon(R.drawable.food);
        dlg.setCancelable(false);
        dlg.setSingleChoiceItems(foodStrList, selectItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectItem=i;
            }
        });
        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String temp=foodStrList[selectItem]; //음식 분류 선택한 값
                //확인 눌렀을때 temp에 해당하는 음식 종류들 가져와서 보여주기

                LinkedList<Food> list_food=foodInfo.get(temp);
                FoodList foodList=new FoodList(list_food); //리스트를 담은 객체를 만들어 intent로 전달한다.
                Intent intent=new Intent(MainActivity.this, KoreaFoodActivity.class);
                intent.putExtra("list",foodList);
                startActivity(intent);

        }
        });
        dlg.setNegativeButton("취소",null);
        dlg.show();
    }

    class Descending implements Comparator<Record> {

        @Override
        public int compare(Record p1, Record p2) {
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
                    if(p2.getDate().compareTo(p1.getDate()) < 0)
                    {
                        return p2.getDate().compareTo(p1.getDate());
                    }
                    else if(p2.getDate().compareTo(p1.getDate()) > 0)
                    {
                        return p2.getDate().compareTo(p1.getDate());
                    }
                    else
                    {
                        return 0;
                    }
                }
            }
        }
    }
}

package com.project.kdh.calorieproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by 동현 on 2017-11-27.
 */

public class DBHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;
    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {

        super(context, name, factory, version);
    }

    //DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE RECORD (_id INTEGER PRIMARY KEY AUTOINCREMENT, year TEXT, month TEXT, date TEXT, food TEXT, cal INTEGER, time TEXT);");
        sqLiteDatabase.execSQL("CREATE TABLE YEARRECORD (_id INTEGER PRIMARY KEY AUTOINCREMENT, year TEXT, goal INTEGER);");
        sqLiteDatabase.execSQL("CREATE TABLE MONTHRECORD (_id INTEGER PRIMARY KEY AUTOINCREMENT, year TEXT, month TEXT, goal INTEGER);");
        sqLiteDatabase.execSQL("CREATE TABLE DAYRECORD (_id INTEGER PRIMARY KEY AUTOINCREMENT, year TEXT, month TEXT, day TEXT, goal INTEGER);");
        Log.d("onCreatecall","생성되었습니다");
    }

    //DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    /*
    public void insert(String date, String food, int cal)
    {
            db = getWritableDatabase();

            //DB에 입력한 값으로 행 추가
            db.execSQL("INSERT INTO RECORD VALUES(null, '" + date + "', '" + food + "', " + cal + ");");
            Log.d("addinsert","추가되었습니다");
            db.close();
    }

    public void delete(String item)
    {
        db=getWritableDatabase();
        //입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM RECORD WHERE item='"+item+"';");
        db.close();
    }
    public ArrayList<Record> getResult(String input, String date_start, String date_end)
    {
        //읽기가 가능하게 DB 열기
        SQLiteDatabase db=getReadableDatabase();
        ArrayList<Record> list=new ArrayList<Record>();
        String total=date_start.substring(0,4)+date_start.substring(5,7)+date_start.substring(8,10);
        int start=Integer.parseInt(total);

        String total2=date_end.substring(0,4)+date_end.substring(5,7)+date_end.substring(8,10);
        int end=Integer.parseInt(total2);

        //DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor=db.rawQuery("SELECT * FROM RECORD;",null);
        Log.d("커서", cursor.getString(0));
        Log.d("커서", cursor.getString(1));
        Log.d("커서", ""+cursor.getInt(2));
        while(cursor.moveToNext())
        {
            String total3=cursor.getString(0).substring(0,4)+cursor.getString(0).substring(5,7)+cursor.getString(0).substring(8,10);
            int _date=Integer.parseInt(total3);

            String _food=cursor.getString(1).toString();
            Log.d("검색음식", _food);
            //음식 이름 같고 날짜가 그 사이
            if((start<=_date && _date<=end) && input.equals(_food))
            {
                Log.d("찾은음식", _food);
                Record record = new Record(cursor.getString(0), cursor.getString(1), cursor.getInt(2));
                list.add(record);
            }
        }
        return list;
    }
    */
    public void test()
    {
        db=getWritableDatabase();
    }
}
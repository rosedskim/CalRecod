package com.project.kdh.calorieproject;

import java.io.Serializable;

/**
 * Created by 동현 on 2017-11-27.
 */

public class Record implements Serializable{
    private String year;
    private String month;
    private String date;
    private String food;
    private int cal;
    private String time;
    public Record(String year, String month, String date, String food, int cal,String time)
    {
        this.year=year;
        this.month=month;
        this.date=date;
        this.food=food;
        this.cal=cal;
        this.time=time;
    }
    public String getYear()
    {
        return year;
    }
    public String getMonth()
    {
        return month;
    }
    public String getDate()
    {
        return date;
    }
    public String getFood()
    {
        return food;
    }
    public int getCal()
    {
        return cal;
    }
    public String getTime(){return time;}
}

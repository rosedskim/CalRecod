package com.project.kdh.calorieproject;

import java.io.Serializable;

/**
 * Created by 동현 on 2017-12-01.
 */

public class StatisticRecord{
    private String date;
    private int cal;
    private int average;

    StatisticRecord(String date, int cal, int average)
    {
        this.date=date;
        this.cal=cal;
        this.average=average;
    }

    public String getDate()
    {
        return date;
    }
    public int getCal() {
        return cal;
    }
    public int getAverage()
    {
        return average;
    }
}

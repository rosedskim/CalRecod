package com.project.kdh.calorieproject;

import java.io.Serializable;

/**
 * Created by 동현 on 2017-11-26.
 */

public class Food implements Serializable{
    String Name;
    int Cal;

    public Food(String Name, int Cal)
    {
        this.Name=Name;
        this.Cal=Cal;
    }
    public String getName()
    {
        return Name;
    }
    public int getCal()
    {
        return Cal;
    }

}

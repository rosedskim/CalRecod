package com.project.kdh.calorieproject;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by 동현 on 2017-11-26.
 */

public class FoodList implements Serializable{
    private LinkedList<Food> list;

    public FoodList(LinkedList<Food> food)
    {
        this.list=food;
    }

    public LinkedList<Food> getList()
    {
        return this.list;
    }
}

package com.project.kdh.calorieproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by 동현 on 2017-11-29.
 */

public class RecordList implements Serializable {
    private LinkedList<Record> list;

    public RecordList(LinkedList<Record> record_list)
    {
        this.list=record_list;
    }
    public LinkedList<Record> getList()
    {
        return this.list;
    }

}

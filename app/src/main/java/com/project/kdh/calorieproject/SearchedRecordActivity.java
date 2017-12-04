package com.project.kdh.calorieproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by 동현 on 2017-11-29.
 */

public class SearchedRecordActivity extends AppCompatActivity {

    private ListView mListView;
    private RecordList recordList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searched_record_layout);
        mListView=(ListView)findViewById(R.id.searched_list);

        Intent intent=new Intent(this.getIntent());
        recordList=(RecordList)intent.getSerializableExtra("list");
        Log.d("????","1");
        dataSetting();
    }
    public void dataSetting()
    {
        Log.d("????","2");
        SearchedRecordAdapter sAdapter=new SearchedRecordAdapter();

        LinkedList<Record> list=recordList.getList();
        Log.d("?????",""+list.size());
        for(int i=0; i<list.size(); i++)
        {
            Record temp=list.get(i);
            Log.d("?????",temp.getDate());
            Log.d("?????",temp.getFood());
            Log.d("?????",""+temp.getCal());
            sAdapter.addItem(temp);
        }
        mListView.setAdapter(sAdapter);
    }
}

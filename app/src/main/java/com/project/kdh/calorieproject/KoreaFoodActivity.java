package com.project.kdh.calorieproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by 동현 on 2017-11-26.
 */

public class KoreaFoodActivity extends AppCompatActivity {
    private ListView mListView;
    private FoodList foodList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.korea_food_layout);
        mListView=(ListView)findViewById(R.id.li_korea_food);

        //전달된 foodlist 받기
        Intent intent=new Intent(this.getIntent());
        foodList=(FoodList)intent.getSerializableExtra("list");


        dataSetting();
    }

    public void dataSetting()
    {
        KoreaFoodAdapter mAdpater=new KoreaFoodAdapter();
        LinkedList<Food> list = foodList.getList();
        Iterator<Food> it=list.iterator();
        for(int i=0; i<list.size(); i++)
        {
            Food food=it.next();
            mAdpater.addItem(food.getName(), food.getCal());
        }
        mListView.setAdapter(mAdpater);
    }


}

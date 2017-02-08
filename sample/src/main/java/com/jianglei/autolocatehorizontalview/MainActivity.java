package com.jianglei.autolocatehorizontalview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jianglei.view.AutoLocateHorizontalView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String[]ages = new String[]{"12","13","14","15","16","17","18","19","20","21","22","23","24"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AutoLocateHorizontalView recyclerView = (AutoLocateHorizontalView) findViewById(R.id.recyleview);
        List<String>list = new ArrayList<>();
        for(String age : ages){
            list.add(age);
        }
        AgeAdapter ageAdapter = new AgeAdapter(this,list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(ageAdapter);
    }
}

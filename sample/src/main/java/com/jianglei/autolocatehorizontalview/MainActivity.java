package com.jianglei.autolocatehorizontalview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jianglei.view.AutoLocateHorizontalView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    String[]ages = new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12"};
    //String[]ages = new String[]{"0","1","2","3"};
    List<String>ageList;
    EditText etPos;
    EditText etAge;
    private AgeAdapter ageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AutoLocateHorizontalView recyclerView = (AutoLocateHorizontalView) findViewById(R.id.recyleview);
        etAge = (EditText)findViewById(R.id.et_value);
        etPos = (EditText)findViewById(R.id.et_pos);
        Button btnAdd = (Button)findViewById(R.id.btn_add);
        Button btnRemove = (Button)findViewById(R.id.btn_remove);
        btnAdd.setOnClickListener(this);
        btnRemove.setOnClickListener(this);
         ageList = new ArrayList<>();
        for(String age : ages){
            ageList.add(age);
        }
        ageAdapter = new AgeAdapter(this,ageList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(ageAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                int pos = Integer.valueOf(etPos.getText().toString());
                String age = etAge.getText().toString();
                ageList.add(pos,age);
                ageAdapter.notifyItemInserted(pos);
                //ageAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_remove:
                int pos1 = Integer.valueOf(etPos.getText().toString());
                ageList.remove(pos1);
                ageAdapter.notifyItemRangeRemoved(pos1,1);
                //ageAdapter.notifyDataSetChanged();
                break;
        }
    }
}

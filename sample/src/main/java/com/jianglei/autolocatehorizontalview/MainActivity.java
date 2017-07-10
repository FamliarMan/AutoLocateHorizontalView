package com.jianglei.autolocatehorizontalview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jianglei.view.AutoLocateHorizontalView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String[] ages = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    //String[]ages = new String[]{"0","1","2","3"};
    List<String> ageList;
    EditText etPos;
    EditText etAge;
    private AgeAdapter ageAdapter;
    private AutoLocateHorizontalView  mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContent = (AutoLocateHorizontalView) findViewById(R.id.recyleview);
        etAge = (EditText) findViewById(R.id.et_value);
        etPos = (EditText) findViewById(R.id.et_pos);
        Button btnAdd = (Button) findViewById(R.id.btn_add);
        Button btnRemove = (Button) findViewById(R.id.btn_remove);
        Button btnAnother = (Button) findViewById(R.id.btn_another_sample);
        Button btnMove = (Button)findViewById(R.id.btn_move);
        btnAnother.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnRemove.setOnClickListener(this);
        btnMove.setOnClickListener(this);
        ageList = new ArrayList<>();
        for (String age : ages) {
            ageList.add(age);
        }
        ageAdapter = new AgeAdapter(this, ageList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mContent.setLayoutManager(linearLayoutManager);
        mContent.setOnSelectedPositionChangedListener(new AutoLocateHorizontalView.OnSelectedPositionChangedListener() {
            @Override
            public void selectedPositionChanged(int pos) {
                Toast.makeText(MainActivity.this, "pos:" + pos, Toast.LENGTH_SHORT).show();
            }
        });
        mContent.setInitPos(5);
        mContent.setAdapter(ageAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:

                String posStr = etPos.getText().toString();
                if (TextUtils.isEmpty(posStr)) {
                    Toast.makeText(MainActivity.this, "添加的位置没有设置", Toast.LENGTH_SHORT).show();
                    return;
                }
                String age = etAge.getText().toString();
                if (TextUtils.isEmpty(age)) {
                    Toast.makeText(MainActivity.this, "年龄没有设置", Toast.LENGTH_SHORT).show();
                    return;
                }
                int pos = Integer.parseInt(etPos.getText().toString());
                if (pos > ageList.size() - 1) {
                    Toast.makeText(MainActivity.this, "位置设置太大，数组会越界哦", Toast.LENGTH_SHORT).show();
                    return;
                }
                ageList.add(pos, age);
                //ageAdapter.notifyItemInserted(pos);
                ageAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_remove:
                String posStr1 = etPos.getText().toString();
                if (TextUtils.isEmpty(posStr1)) {
                    Toast.makeText(MainActivity.this, "删除的位置没有设置", Toast.LENGTH_SHORT).show();
                    return;
                }
                int pos1 = Integer.parseInt(etPos.getText().toString());
                if (pos1 > ageList.size() - 1) {
                    Toast.makeText(MainActivity.this, "位置设置太大，数组会越界哦", Toast.LENGTH_SHORT).show();
                    return;
                }
                ageList.remove(pos1);
                ageAdapter.notifyItemRangeRemoved(pos1, 1);
                break;
            case R.id.btn_another_sample:
                Intent intent  = new Intent(MainActivity.this,BarActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_move:
                mContent.moveToPosition(1);

                break;
            default:
                break;
        }
    }
}

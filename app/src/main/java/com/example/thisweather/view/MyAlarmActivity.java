package com.example.thisweather.view;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.thisweather.R;
import com.example.thisweather.adapter.AlarmAdapter;
import com.example.thisweather.util.AlarmDBHandler;

import java.util.ArrayList;

public class MyAlarmActivity extends AppCompatActivity {
    AlarmDBHandler mHandler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_alarm);

        mHandler = AlarmDBHandler.open(this);
        setRecyclerView();

        ImageView iv_add_alarm = findViewById(R.id.iv_add_alarm);
        iv_add_alarm.setOnClickListener(onClickListener);
        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_add_alarm:
                    Intent intent = new Intent(MyAlarmActivity.this, AddAlarmActivity.class);
                    startActivityForResult(intent, 100);
                    break;
                case R.id.iv_back:
                    onBackPressed();
                    break;
            }
        }
    };

    private void setRecyclerView() {
        ArrayList<AlarmAdapter.AlarmItem> data = new ArrayList<>();
        data = getAlarmData();
//        data.add(new AlarmAdapter.AlarmItem("월 수 목", "오전", "8시 30분"));
//        data.add(new AlarmAdapter.AlarmItem("금", "오전", "10시 20분"));

        RecyclerView recyclerView = findViewById(R.id.rv_alarm);
        AlarmAdapter adapter = new AlarmAdapter(data, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100 && resultCode == 100){
            setRecyclerView();
        }
    }

    private ArrayList<AlarmAdapter.AlarmItem> getAlarmData() {
        ArrayList<AlarmAdapter.AlarmItem> list = new ArrayList<>();
        Cursor cursor = mHandler.select();
        while (cursor.moveToNext()) {
//            int index2 = cursor.getColumnIndex("day");
//            int index3 = cursor.getColumnIndex("ampm");
//            int index4 = cursor.getColumnIndex("hour");
            int _id = cursor.getInt(0);
            String day = cursor.getString(1);
            String ampm = cursor.getString(2);
            String hour = cursor.getString(3);
            int isOn = cursor.getInt(4);
            AlarmAdapter.AlarmItem data = new AlarmAdapter.AlarmItem(_id, isOn, day, ampm, hour);
            list.add(data);
        }
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.close();
    }
}
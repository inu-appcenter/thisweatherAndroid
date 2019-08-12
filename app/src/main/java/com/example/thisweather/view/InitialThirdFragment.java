package com.example.thisweather.view;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dpro.widgets.OnWeekdaysChangeListener;
import com.dpro.widgets.WeekdaysPicker;
import com.example.thisweather.R;
import com.example.thisweather.util.AlarmDBHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InitialThirdFragment  extends Fragment {

    LinearLayout ll_time;
    TextView AmPm, Time;
    WeekdaysPicker weekdaysPicker;
    ArrayList<Integer> days;
    String ampm, hour;
    AlarmDBHandler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_initial_third, null);

        ll_time = view.findViewById(R.id.ll_time);
        AmPm = view.findViewById(R.id.tv_ampm);
        Time = view.findViewById(R.id.tv_time);
        ampm = "오전";
        hour = "8시 30분";

        weekdaysPicker = (WeekdaysPicker) view.findViewById(R.id.wp_weekdays);
//        Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"sf_pro_text_semibold.ttf");
//        weekdaysPicker.setT
        weekdaysPicker.selectDay(0);

        weekdaysPicker.setOnWeekdaysChangeListener(new OnWeekdaysChangeListener() {
            @Override
            public void onChange(View view, int i, List<Integer> list) {
//                Log.d("day", "i: " + i + " list: " + list);
                days = new ArrayList<>(list);
                sendInfo();
            }
        });

        ll_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        setTV(hourOfDay, minute);
                    }
                },8,30,false);
                timePickerDialog.show();
            }
        });

        return view;
    }

    private void setTV(int hourOfDay, int minute) {
        if (hourOfDay <= 12){
            ampm = "오전";
            AmPm.setText(ampm);
            hour = hourOfDay + "시 " + minute + "분";
            Time.setText(hour);
        }
        else {
            ampm = "오후";
            AmPm.setText(ampm);
            hour = (hourOfDay - 12) + "시 " + minute + "분";
            Time.setText(hour);
        }
        sendInfo();
    }

    private void sendInfo() {
        Intent intent = new Intent("init");
        intent.putIntegerArrayListExtra("days", days);
        intent.putExtra("ampm", ampm);
        intent.putExtra("hour", hour);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }
}
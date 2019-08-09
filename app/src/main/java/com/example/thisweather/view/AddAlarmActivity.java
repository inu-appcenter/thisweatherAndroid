package com.example.thisweather.view;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dpro.widgets.WeekdaysPicker;
import com.example.thisweather.R;
import com.example.thisweather.util.AlarmDBHandler;

import java.util.Calendar;
import java.util.List;

public class AddAlarmActivity  extends AppCompatActivity {

    LinearLayout ll_time;
    TextView AmPm, Time, tv_add;
    WeekdaysPicker weekdaysPicker;
    String day, ampm, hour;
    AlarmDBHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        tv_add = findViewById(R.id.tv_add);
        tv_add.setOnClickListener(onClickListener);

        ll_time = findViewById(R.id.ll_time);
        ll_time.setOnClickListener(onClickListener);
        AmPm = findViewById(R.id.tv_ampm);
        Time = findViewById(R.id.tv_time);
        ampm = "오전";
        hour = "8시 30분";

        weekdaysPicker = findViewById(R.id.wp_weekdays);
//        Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"sf_pro_text_semibold.ttf");
//        weekdaysPicker.setT
        weekdaysPicker.selectDay(0);

        mHandler = AlarmDBHandler.open(this);
    }

    View.OnClickListener onClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_add:
                    setDay();
                    mHandler.insert(day, ampm, hour, 1);
                    setResult(100);
                    finish();
                    break;
                case R.id.ll_time:
                    TimePickerDialog timePickerDialog = new TimePickerDialog(AddAlarmActivity.this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                            setTV(hourOfDay, minute);
                        }
                    },8,30,false);
                    timePickerDialog.show();
            }
        }
    };

    private void setDay() {
        List<Integer> selectedDays = weekdaysPicker.getSelectedDays();
        String temp = "";
        for (int i = 0; i < selectedDays.size(); i++){
            switch (selectedDays.get(i)){
                case Calendar
                        .SUNDAY:
                    temp += "일 ";
                    break;
                case Calendar
                        .MONDAY:
                    temp += "월 ";
                    break;
                case Calendar
                        .TUESDAY:
                    temp += "화 ";
                    break;
                case Calendar
                        .WEDNESDAY:
                    temp += "수 ";
                    break;
                case Calendar
                        .THURSDAY:
                    temp += "목 ";
                    break;
                case Calendar
                        .FRIDAY:
                    temp += "금 ";
                    break;
                case Calendar
                        .SATURDAY:
                    temp += "토 ";
                    break;
            }
        }
        day = temp;
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.close();
    }
}
package com.example.thisweather.view;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.dpro.widgets.WeekdaysPicker;
import com.example.thisweather.R;

public class InitialThirdFragment  extends Fragment {
    TextView AmPm;
    TextView Time;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_initial_third, null);

        LinearLayout setTime = (LinearLayout) view.findViewById(R.id.ll_time);
        AmPm = view.findViewById(R.id.tv_ampm);
        Time = view.findViewById(R.id.tv_time);

        setTime.setOnClickListener(new View.OnClickListener() {
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

        WeekdaysPicker weekdaysPicker = (WeekdaysPicker) view.findViewById(R.id.wp_weekdays);
//        Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"sf_pro_text_semibold.ttf");
//        weekdaysPicker.setT
        weekdaysPicker.selectDay(0);

        return view;
    }

    private void setTV(int hourOfDay, int minute) {
        if (hourOfDay <= 12){
            AmPm.setText("오전");
            Time.setText(hourOfDay + "시 " + minute + "분");
        }
        else {
            AmPm.setText("오후");
            int newTime = hourOfDay - 12;
            Time.setText(newTime + "시 " + minute + "분");
        }
    }

}
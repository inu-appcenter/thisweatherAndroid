package com.example.thisweather.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thisweather.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.ViewHolder> {

    ArrayList<WeekItem> data;

    public WeekAdapter(ArrayList<WeekItem> data){
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeekItem item = data.get(position);
        Log.d("testt","position, " + position);
        holder.temp.setText(item.getTemp() + "°");

        switch (item.getWeather()){
            case "맑음": {
                holder.icon.setImageResource(R.drawable.small_sunny);
                break;
            }
            case "구름조금": {
                holder.icon.setImageResource(R.drawable.small_sunny);
                break;
            }
            case "구름많음": {
                holder.icon.setImageResource(R.drawable.small_cloudy);
                break;
            }
            case "구름많고 비": {
                holder.icon.setImageResource(R.drawable.small_rainy);
                break;
            }
            case "구름많고 비/눈": {
                holder.icon.setImageResource(R.drawable.small_rainsnow);
                break;
            }
            case "구름많고 눈/비": {
                holder.icon.setImageResource(R.drawable.small_rainsnow);
                break;
            }
            case "구름많고 눈": {
                holder.icon.setImageResource(R.drawable.small_snowy);
                break;
            }
            case "흐림": {
                holder.icon.setImageResource(R.drawable.small_cloudy);
                break;
            }
            case "흐리고 비": {
                holder.icon.setImageResource(R.drawable.small_rainy);
                break;
            }
            case "흐리고 비/눈": {
                holder.icon.setImageResource(R.drawable.small_rainsnow);
                break;
            }
            case "흐리고 눈/비": {
                holder.icon.setImageResource(R.drawable.small_rainsnow);
                break;
            }
            case "흐리고 눈": {
                holder.icon.setImageResource(R.drawable.small_snowy);
                break;
            }
            default: {
                break;
            }
        }
        if (position == 0 || position == 1){
            holder.hour.setText(item.getHour());
        }
        else {
            try {
                holder.hour.setText(getDateDay(item.getHour(), "yyyy-MM-dd 00:00"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //출처: https://kanzler.tistory.com/35
    public static String getDateDay(String date, String dateType) throws Exception {

        String day = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateType);
        Date nDate = dateFormat.parse(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);
        Log.d("testt","dayNum, " + dayNum);

        switch (dayNum) {
            case 1:
                day = "일";
                break;
            case 2:
                day = "월";
                break;
            case 3:
                day = "화";
                break;
            case 4:
                day = "수";
                break;
            case 5:
                day = "목";
                break;
            case 6:
                day = "금";
                break;
            case 7:
                day = "토";
                break;
        }

        return day;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView temp, hour;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);

            temp = itemView.findViewById(R.id.tv_temp);
            icon = itemView.findViewById(R.id.iv_icon);
            hour = itemView.findViewById(R.id.tv_hour);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class WeekItem {
        private String temp, weather, hour;

        public String getTemp() {
            return temp;
        }

        String getWeather() {
            return weather;
        }

        String getHour() {
            return hour;
        }

        public WeekItem(String temp, String weather, String hour) {
            this.temp = temp;
            this.weather = weather;
            this.hour = hour;
        }
    }
}

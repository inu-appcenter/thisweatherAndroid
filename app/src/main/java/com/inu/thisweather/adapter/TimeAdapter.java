package com.inu.thisweather.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inu.thisweather.R;

import java.util.ArrayList;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {

    ArrayList<TimeItem> data;

    public TimeAdapter(ArrayList<TimeItem> data){
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
        TimeItem item = data.get(position);
        holder.temp.setText(item.getTemp() + "°");
        switch (item.getWeather()){
            case "맑음": {
                holder.icon.setImageResource(R.drawable.small_sunny);
                break;
            }
            case "구름 조금": {
                holder.icon.setImageResource(R.drawable.small_sunny);
                break;
            }
            case "구름 많음": {
                holder.icon.setImageResource(R.drawable.small_cloudy);
                break;
            }
            case "흐림": {
                holder.icon.setImageResource(R.drawable.small_cloudy);
                break;
            }
            case "비": {
                holder.icon.setImageResource(R.drawable.small_rainy);
                break;
            }
            case "눈/비": {
                holder.icon.setImageResource(R.drawable.small_rainsnow);
                break;
            }
            case "눈": {
                holder.icon.setImageResource(R.drawable.small_snowy);
                break;
            }
            default: {
                break;
            }
        }
        if (Integer.parseInt(item.getHour()) < 10){
            holder.hour.setText("0" + item.getHour() + "시");
        }
        else {
            holder.hour.setText(item.getHour() + "시");
        }
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


    public static class TimeItem {
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

        public TimeItem(String temp, String weather, String hour) {
            this.temp = temp;
            this.weather = weather;
            this.hour = hour;
        }
    }
}

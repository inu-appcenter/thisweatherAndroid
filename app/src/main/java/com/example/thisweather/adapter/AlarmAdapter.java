package com.example.thisweather.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.thisweather.R;
import com.example.thisweather.util.AlarmBroadcastReceiver;
import com.example.thisweather.util.AlarmDBHandler;

import java.util.ArrayList;

import static android.content.Context.ALARM_SERVICE;

public class AlarmAdapter  extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    private ArrayList<AlarmItem> mData;
    private Context mContext;
    private AlarmDBHandler mHandler;
    private AlarmManager manager;
    private PendingIntent pendingIntent;

    public AlarmAdapter(ArrayList<AlarmItem> data, Context context){
        this.mData = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, null);
        mHandler = AlarmDBHandler.open(mContext);
        manager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmBroadcastReceiver.class);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AlarmItem item = mData.get(position);
        holder.day.setText(item.getDay());
        holder.ampm.setText(item.getAmpm());
        holder.hour.setText(item.getHour());
        boolean b = (item.getIsOn() != 0);
        holder.isOn.setChecked(b);

//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 8);
//        calendar.set(Calendar.MINUTE, 30);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.remove(position);
                notifyDataSetChanged();
                mHandler.delete(item.get_id());
            }
        });
        holder.isOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int i = (isChecked)? 1 : 0;
                mHandler.update(item.get_id(), i);
                if (isChecked){
                    startAlarm();
                }
                else{
                    cancelAlarm();
                }
            }
        });
    }

    private void startAlarm() {
        Log.d("AlarmAdapter", "start alarm");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 1, pendingIntent); //1분
//            Log.d("AlarmAdapter", "1");
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            manager.set(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
//            Log.d("AlarmAdapter", "2");
//        } else {
//            manager.set(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
//            Log.d("AlarmAdapter", "3");
//        }
    }

    private void cancelAlarm() {
        Log.d("AlarmAdapter", "cancel alarm");
        manager.cancel(pendingIntent);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView day, ampm, hour;
        ImageView delete;
        Switch isOn;

        public ViewHolder(View itemView) {
            super(itemView);

            day = itemView.findViewById(R.id.tv_day);
            ampm = itemView.findViewById(R.id.tv_ampm);
            hour = itemView.findViewById(R.id.tv_hour);
            delete = itemView.findViewById(R.id.iv_delete);
            isOn = itemView.findViewById(R.id.switch_alarm);
        }
    }

    @Override
    public int getItemCount() {
//        return mCursor.getCount();
        return mData.size();
    }

//    @Override
//    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
//        super.onDetachedFromRecyclerView(recyclerView);
//        mHandler.close();
//    }

    public static class AlarmItem {
        private int _id, isOn;
        private String day, ampm, hour;

        public int getIsOn() {
            return isOn;
        }

        public int get_id() {
            return _id;
        }

        public String getDay() {
            return day;
        }

        public String getAmpm() {
            return ampm;
        }

        public String getHour() {
            return hour;
        }

        public AlarmItem(int _id, int isOn, String day, String ampm, String hour) {
            this._id = _id;
            this.isOn = isOn;
            this.day = day;
            this.ampm = ampm;
            this.hour = hour;
        }
    }
}

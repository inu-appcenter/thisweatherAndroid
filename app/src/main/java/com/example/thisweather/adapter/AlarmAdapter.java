package com.example.thisweather.adapter;

import android.content.Context;
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
import com.example.thisweather.util.AlarmDBHandler;

import java.util.ArrayList;

public class AlarmAdapter  extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    private ArrayList<AlarmItem> mData;
    private Context mContext;
    private AlarmDBHandler mHandler;

    public AlarmAdapter(ArrayList<AlarmItem> data, Context context){
        this.mData = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, null);
        mHandler = AlarmDBHandler.open(mContext);
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
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_delete:
                    break;
                case R.id.switch_alarm:
                    break;
            }
        }
    };

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


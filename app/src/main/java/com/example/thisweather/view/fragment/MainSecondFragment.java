package com.example.thisweather.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.thisweather.R;
import com.example.thisweather.adapter.TimeAdapter;
import com.example.thisweather.network.RetrofitService;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.thisweather.util.Utils.getNoPoint;
import static com.example.thisweather.util.Utils.setChart;

public class MainSecondFragment extends Fragment {
    Retrofit retrofit;
    RetrofitService service;
    RecyclerView recyclerView;
    LineChart timeChart;
    ArrayList<TimeAdapter.TimeItem> data;

    @Override
    public void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        Log.d("MainThirdFragment", "onCreate");

        data = new ArrayList<>();

        retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitService.baseUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();
        service = retrofit.create(RetrofitService.class);
        getLocalForecast();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_second,container,false);
        Log.d("MainThirdFragment", "onCreateView");

        recyclerView = view.findViewById(R.id.rv_time);
        timeChart = view.findViewById(R.id.lc_time);

        return view;
    }

    private void getLocalForecast() {
        Call<JsonObject> localForecast = service.localForecast();
        localForecast.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray array = response.body().get("rss").getAsJsonObject().get("channel").getAsJsonArray().get(0).getAsJsonObject().get("item").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsJsonArray().get(0).getAsJsonObject().get("body").getAsJsonArray().get(0).getAsJsonObject().get("data").getAsJsonArray();
                Log.d("MainSecondFragment","local, " + array);
                setTimeTab(array);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("MainSecondFragment","local, " + t +"");
            }
        });
    }

    private void setTimeTab(JsonArray array) {
        ArrayList<TimeAdapter.TimeItem> data = new ArrayList<>();
        for (int i = 0; i < 8; i++){
            data.add(new TimeAdapter.TimeItem(getNoPoint(array.get(i).getAsJsonObject().get("temp").getAsString()), array.get(i).getAsJsonObject().get("wfKor").getAsString(), array.get(i).getAsJsonObject().get("hour").getAsString()));
        }

        TimeAdapter adapter = new TimeAdapter(data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));

        ArrayList<Entry> value = new ArrayList<>();
        for (int i = 0; i < data.size(); i++){
            value.add(new Entry(i, Float.parseFloat(data.get(i).getTemp())));
            Log.d("MainActivity","value, " + Float.parseFloat(data.get(i).getTemp()));
        }
        setChart(value, timeChart, getActivity());
    }

}

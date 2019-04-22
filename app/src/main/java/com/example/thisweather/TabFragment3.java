package com.example.thisweather;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class TabFragment3 extends Fragment {
    Retrofit retrofit;
    RetrofitService service;
    JsonArray array;
    RecyclerView recyclerView;
    LineChart weekChart;
    WeekAdapter adapter;
    ArrayList<WeekAdapter.WeekItem> data;
    ArrayList<Entry> value;
    MainActivity mainActivity;
    String tomorrowTemp, tomorrowWeather, secondTomorrowTemp, secondTomorrowWeather;

    @Override
    public void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        Log.d("testt", "onCreate");

        data = new ArrayList<>();
        mainActivity = new MainActivity();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://117.16.231.66:7002/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();
        service = retrofit.create(RetrofitService.class);
        getLocalForecast();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_3,container,false);
        Log.d("testt", "onCreateView");

        recyclerView = view.findViewById(R.id.rv_week);
        weekChart = view.findViewById(R.id.lc_week);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("testt", "onResume");
        setView();
    }

    public void getLocalForecast() {
        Call<JsonObject> localForecast = service.localForecast();
        localForecast.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray timeArray = response.body().get("rss").getAsJsonObject().get("channel").getAsJsonArray().get(0).getAsJsonObject().get("item").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsJsonArray().get(0).getAsJsonObject().get("body").getAsJsonArray().get(0).getAsJsonObject().get("data").getAsJsonArray();
                Log.d("testt","local, " + timeArray);
                getTime(timeArray);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("testt","local, " + t +"");
            }
        });
    }

    private void getTime(JsonArray array) {
        String temp1, temp2;
        for (int i = 0; i < array.size(); i++){
            temp1 = array.get(i).getAsJsonObject().get("day").getAsString();
            temp2 = array.get(i).getAsJsonObject().get("hour").getAsString();
            if (temp1.equals("1") && temp2.equals("12")){
                tomorrowTemp = divisionTwo(array.get(i).getAsJsonObject().get("tmn").getAsString(), array.get(i).getAsJsonObject().get("tmx").getAsString());
                tomorrowWeather = array.get(i).getAsJsonObject().get("wfKor").getAsString();
            }
            else if (temp1.equals("2") && temp2.equals("12")){
                secondTomorrowTemp = divisionTwo(array.get(i).getAsJsonObject().get("tmn").getAsString(), array.get(i).getAsJsonObject().get("tmx").getAsString());
                secondTomorrowWeather = array.get(i).getAsJsonObject().get("wfKor").getAsString();
            }
        }
        getWeekForecast();
    }

    private void getWeekForecast() {
        Call<JsonObject> weekForecast = service.weekForecast();
        weekForecast.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                array = response.body().get("data").getAsJsonArray();
                Log.d("testt","week, " + array);
                setWeekTab(array);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("testt","week, " + t);
            }
        });
    }

    private void setWeekTab(JsonArray array) {

        data.add(new WeekAdapter.WeekItem(mainActivity.getNoPoint(tomorrowTemp), tomorrowWeather, "내일"));
        data.add(new WeekAdapter.WeekItem(mainActivity.getNoPoint(secondTomorrowTemp), secondTomorrowWeather, "모레"));

        for (int i = 0; i < 12; i++){
            if (i % 2 == 0) {
                Log.d("testt",mainActivity.getNoPoint(array.get(i).getAsJsonObject().get("tmn").getAsString()));
                data.add(new WeekAdapter.WeekItem(mainActivity.getNoPoint(divisionTwo(array.get(i).getAsJsonObject().get("tmn").getAsString(), array.get(i).getAsJsonObject().get("tmx").getAsString())), array.get(i).getAsJsonObject().get("wf").getAsString(), array.get(i).getAsJsonObject().get("tmEf").getAsString()));
            }
        }

        adapter = new WeekAdapter(data);

        value = new ArrayList<>();
        for (int i = 0; i < data.size(); i++){
            value.add(new Entry(i, Float.parseFloat(data.get(i).getTemp())));
            Log.d("testt","value, " + Float.parseFloat(data.get(i).getTemp()));
        }
        setView();
    }

    private void setView() {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        mainActivity.setChart(value, weekChart, getActivity());
    }

    private String divisionTwo(String tmn, String tmx){
        if (!tmn.equals("-999.0") && !tmx.equals("-999.0")) {
            double num1 = Double.parseDouble(tmn);
            double num2 = Double.parseDouble(tmx);
            return String.valueOf((num2 + num1) / 2);
        }
        else
            return "0.0";
    }

}
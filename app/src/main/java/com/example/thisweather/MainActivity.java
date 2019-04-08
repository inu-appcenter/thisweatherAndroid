package com.example.thisweather;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Retrofit retrofit;
    RetrofitService service;
    DrawerLayout drawer;
    int dust;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setDate();
        setToolbar();
        setViewpager();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://117.16.231.66:7002/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();
        service = retrofit.create(RetrofitService.class);

        getLocalForecast();
        getWeekForecast();
        getFineDust();
    }

    private void getWeekForecast() {
        Call<JsonObject> weekForecast = service.weekForecast();
        weekForecast.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray array = response.body().get("data").getAsJsonArray();
                Log.d("test","week, " + array);
                getWeekData(array);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("test","week, " + t);
            }
        });
    }

    private void getWeekData(JsonArray array) {
        String data1 = array.get(0).getAsJsonObject().get("tmEf").getAsString();
        String data2 = array.get(0).getAsJsonObject().get("wf").getAsString();
        String data3 = array.get(0).getAsJsonObject().get("tmn").getAsString();
        String data4 = array.get(0).getAsJsonObject().get("tmx").getAsString();
        Log.d("testt", data1);
        Log.d("testt", data2);
        Log.d("testt", data3);
        Log.d("testt", data4);
    }

    private void setViewpager() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.weatherPager);

        FragmentManager fm = getSupportFragmentManager();
        PagerAdapter pagerAdapter = new PagerAdapter(fm, 3);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
    }

    private void getLocalForecast() {
        Call<JsonObject> localForecast = service.localForecast();
        localForecast.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray array = response.body().get("rss").getAsJsonObject().get("channel").getAsJsonArray().get(0).getAsJsonObject().get("item").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsJsonArray().get(0).getAsJsonObject().get("body").getAsJsonArray().get(0).getAsJsonObject().get("data").getAsJsonArray();
                Log.d("test","local, " + array);
                getLocalData(array);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("test","local, " + t +"");
                getLocalData(null);
            }
        });
    }

    private void setToolbar() {
        drawer = findViewById(R.id.drawer_layout);
        ImageView menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        ImageView retry = findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
                getLocalForecast();
                getFineDust();
            }
        });
    }

    private void getLocalData(JsonArray array) {
        String[] data = new String[6];
        if (array != null) {
            data[0] = array.get(0).getAsJsonObject().get("temp").getAsString();
            data[1] = array.get(0).getAsJsonObject().get("wfKor").getAsString();
            data[2] = array.get(0).getAsJsonObject().get("tmx").getAsString();
            data[3] = array.get(0).getAsJsonObject().get("tmn").getAsString();
            data[4] = array.get(0).getAsJsonObject().get("ws").getAsString();
            data[5] = array.get(0).getAsJsonObject().get("pop").getAsString();
            Log.d("test", data[0]);
            Log.d("test", data[1]);
            Log.d("test", data[2]);
            Log.d("test", data[3]);
            Log.d("test", data[4]);
            Log.d("test", data[5]);
        }
        else{
            for (int i = 0; i < 6; i++){
                data[i] = "";
            }
        }

        Intent intent = new Intent("data");

        String str = getNoPoint(data[0]);
        TextView textView = findViewById(R.id.temp);
        textView.setText(str + "°");

        int temper = 0;
        if (!str.equals("")) {
            temper = Integer.parseInt(str);
        }
        setLionBody(temper);
        setLionHead(data[1]);

        int i = 0;
        while (data[2].equals("-999.0")){
            i++;
            data[2] = array.get(i).getAsJsonObject().get("tmx").getAsString();
        }
        intent.putExtra("max", getNoPoint(data[2]) + "°");

        int j = 0;
        while (data[3].equals("-999.0")){
            j++;
            data[3] = array.get(j).getAsJsonObject().get("tmn").getAsString();
        }
        intent.putExtra("min", getNoPoint(data[3]) + "°");

        if (data[1].equals("맑음") || data[1].equals("구름 조금") || data[1].equals("구름 많음") || data[1].equals("흐림")) {
            if (Double.parseDouble(data[4]) >= 10.0) {
                setLionHead("바람");
                intent.putExtra("windspeed", getNoPoint(data[4]) + "m/s");
            }
            else {
                intent.putExtra("windspeed", getNoPoint(data[4]) + "m/s");
            }
            if (dust > 35) {
                setLionHead("미세먼지");
            }
        }
        else {
            intent.putExtra("windspeed", getNoPoint(data[4]) + "m/s");
        }

        intent.putExtra("rainfall", data[5] + "%");

        intent.putExtra("sensetemp", getWindChill(data[0], data[4]) + "°");

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private String getWindChill(String data1, String data2) {
        if (!data1.equals("") && !data2.equals("")) {
            double temp = Double.parseDouble(data1);
            double ws = Double.parseDouble(data2);

            double result = 13.12 + 0.6215 * temp - 11.37 * Math.pow(ws * 3.6, 0.16) + 0.3965 * temp * Math.pow(ws * 3.6, 0.16);
            return getNoPoint(result + "");
        }
        else {
            return "";
        }
    }

    private String getNoPoint(String data) {
        if (!data.equals("")) {
            double n = Double.parseDouble(data);
            return Math.round(n) + "";
        }
        else{
            return "";
        }
    }

    private void setLionHead(String data) {
        ImageView head = findViewById(R.id.lion_head);
        ImageView heart = findViewById(R.id.heart);
        ConstraintLayout relativeLayout = findViewById(R.id.rl_main);
        ImageView icon = findViewById(R.id.wthIcon);
        TextView textView = findViewById(R.id.wthtext);
        ImageView menu = findViewById(R.id.menu);
        TextView time = findViewById(R.id.Time);
        ImageView retry = findViewById(R.id.retry);
        View lineLeft = findViewById(R.id.line_left);
        View lineRight = findViewById(R.id.line_right);
        TextView info = findViewById(R.id.wthInfo);
        ImageView wind = findViewById(R.id.wind);
        ImageView rain = findViewById(R.id.rain);
        ImageView snow = findViewById(R.id.snow);
        ImageView dust = findViewById(R.id.dust);

        heart.setImageResource(R.drawable.heart);
        wind.setImageResource(R.drawable.wind);
        rain.setImageResource(R.drawable.rain);
        snow.setImageResource(R.drawable.snow);
        dust.setImageResource(R.drawable.dust);

        switch (data){
            case  "미세먼지": {
                relativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundDust));
                heart.setVisibility(View.INVISIBLE);
                menu.setImageResource(R.drawable.icon_menu_black);
                time.setTextColor(ContextCompat.getColor(this, R.color.mainText));
                retry.setImageResource(R.drawable.icon_retry_black);
                lineLeft.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundDust));
                lineRight.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundDust));
                info.setTextColor(ContextCompat.getColor(this, R.color.backgroundDust));
                info.setText("최악이야 미세먼지");
                wind.setVisibility(View.INVISIBLE);
                snow.setVisibility(View.INVISIBLE);
                rain.setVisibility(View.INVISIBLE);
                dust.setVisibility(View.VISIBLE);
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusDust));
                break;
            }
            case "바람": {
                relativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.mainBlue));
                head.setPadding(Math.round(57 * getResources().getDisplayMetrics().density), 0, 0, 0);
                head.setImageResource(R.drawable.head_cloudy);
                heart.setVisibility(View.INVISIBLE);
                menu.setImageResource(R.drawable.icon_menu_white);
                time.setTextColor(ContextCompat.getColor(this, R.color.backgroundDefault));
                retry.setImageResource(R.drawable.icon_retry_white);
                lineLeft.setBackgroundColor(ContextCompat.getColor(this, R.color.mainBlue));
                lineRight.setBackgroundColor(ContextCompat.getColor(this, R.color.mainBlue));
                info.setTextColor(ContextCompat.getColor(this, R.color.mainBlue));
                info.setText("장난아닌 송도풍");
                wind.setVisibility(View.VISIBLE);
                snow.setVisibility(View.INVISIBLE);
                rain.setVisibility(View.INVISIBLE);
                dust.setVisibility(View.INVISIBLE);
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusBlue));
                break;
            }
            case "맑음": {
                relativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundSunny));
                head.setPadding(0, 0, 0, 0);
                head.setImageResource(R.drawable.head_sunny);
                heart.setVisibility(View.VISIBLE);
                icon.setImageResource(R.drawable.icon_sunny);
                textView.setText(data);
                lineLeft.setBackgroundColor(ContextCompat.getColor(this, R.color.mainOrange));
                lineRight.setBackgroundColor(ContextCompat.getColor(this, R.color.mainOrange));
                info.setTextColor(ContextCompat.getColor(this, R.color.mainOrange));
                info.setText("기적같이 맑은 날");
                wind.setVisibility(View.INVISIBLE);
                snow.setVisibility(View.INVISIBLE);
                rain.setVisibility(View.INVISIBLE);
                dust.setVisibility(View.INVISIBLE);
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusSunny));
                break;
            }
            case "구름 조금": {
                relativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundSunny));
                head.setPadding(0, 0, 0, 0);
                head.setImageResource(R.drawable.head_sunny);
                heart.setVisibility(View.VISIBLE);
                icon.setImageResource(R.drawable.icon_sunny);
                textView.setText(data);
                lineLeft.setBackgroundColor(ContextCompat.getColor(this, R.color.mainOrange));
                lineRight.setBackgroundColor(ContextCompat.getColor(this, R.color.mainOrange));
                info.setTextColor(ContextCompat.getColor(this, R.color.mainOrange));
                info.setText("기적같이 맑은 날");
                wind.setVisibility(View.INVISIBLE);
                snow.setVisibility(View.INVISIBLE);
                rain.setVisibility(View.INVISIBLE);
                dust.setVisibility(View.INVISIBLE);
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusSunny));
                break;
            }
            case "구름 많음": {
                relativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.mainBlue));
                head.setPadding(0, 0, 0, 0);
                head.setImageResource(R.drawable.head_sunny);
                heart.setVisibility(View.VISIBLE);
                icon.setImageResource(R.drawable.icon_cloudy);
                textView.setText(data);
                menu.setImageResource(R.drawable.icon_menu_white);
                time.setTextColor(ContextCompat.getColor(this, R.color.backgroundDefault));
                retry.setImageResource(R.drawable.icon_retry_white);
                lineLeft.setBackgroundColor(ContextCompat.getColor(this, R.color.mainBlue));
                lineRight.setBackgroundColor(ContextCompat.getColor(this, R.color.mainBlue));
                info.setTextColor(ContextCompat.getColor(this, R.color.mainBlue));
                info.setText("꾸리꾸리 흐린 날");
                wind.setVisibility(View.INVISIBLE);
                snow.setVisibility(View.INVISIBLE);
                rain.setVisibility(View.INVISIBLE);
                dust.setVisibility(View.INVISIBLE);
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusBlue));
                break;
            }
            case "흐림": {
                relativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.mainBlue));
                head.setPadding(0, 0, 0, 0);
                head.setImageResource(R.drawable.head_sunny);
                heart.setVisibility(View.VISIBLE);
                icon.setImageResource(R.drawable.icon_cloudy);
                textView.setText(data);
                menu.setImageResource(R.drawable.icon_menu_white);
                time.setTextColor(ContextCompat.getColor(this, R.color.backgroundDefault));
                retry.setImageResource(R.drawable.icon_retry_white);
                lineLeft.setBackgroundColor(ContextCompat.getColor(this, R.color.mainBlue));
                lineRight.setBackgroundColor(ContextCompat.getColor(this, R.color.mainBlue));
                info.setTextColor(ContextCompat.getColor(this, R.color.mainBlue));
                info.setText("꾸리꾸리 흐린 날");
                wind.setVisibility(View.INVISIBLE);
                snow.setVisibility(View.INVISIBLE);
                rain.setVisibility(View.INVISIBLE);
                dust.setVisibility(View.INVISIBLE);
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusBlue));
                break;
            }
            case "비": {
                relativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundRainy));
                head.setPadding(0, 0, 0, 0);
                head.setImageResource(R.drawable.head_rain);
                heart.setVisibility(View.INVISIBLE);
                icon.setImageResource(R.drawable.icon_rain);
                textView.setText(data);
                menu.setImageResource(R.drawable.icon_menu_white);
                time.setTextColor(ContextCompat.getColor(this, R.color.backgroundDefault));
                retry.setImageResource(R.drawable.icon_retry_white);
                lineLeft.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundRainy));
                lineRight.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundRainy));
                info.setTextColor(ContextCompat.getColor(this, R.color.backgroundRainy));
                info.setText("추적추적 비와요");
                wind.setVisibility(View.INVISIBLE);
                snow.setVisibility(View.INVISIBLE);
                rain.setVisibility(View.VISIBLE);
                dust.setVisibility(View.INVISIBLE);
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusRainy));
                break;
            }
            case "눈/비": {
                relativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundRainy));
                head.setPadding(0, 0, 0, 0);
                head.setImageResource(R.drawable.head_rain);
                heart.setVisibility(View.INVISIBLE);
                icon.setImageResource(R.drawable.icon_rainsnow);
                textView.setText(data);
                menu.setImageResource(R.drawable.icon_menu_white);
                time.setTextColor(ContextCompat.getColor(this, R.color.backgroundDefault));
                retry.setImageResource(R.drawable.icon_retry_white);
                lineLeft.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundRainy));
                lineRight.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundRainy));
                info.setTextColor(ContextCompat.getColor(this, R.color.backgroundRainy));
                info.setText("추적추적 비와요");
                wind.setVisibility(View.INVISIBLE);
                snow.setVisibility(View.INVISIBLE);
                rain.setVisibility(View.VISIBLE);
                dust.setVisibility(View.INVISIBLE);
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusRainy));
                break;
            }
            case "눈": {
                relativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundSnowy));
                head.setPadding(0, 0, 0, 0);
                head.setImageResource(R.drawable.head_snow);
                heart.setVisibility(View.INVISIBLE);
                icon.setImageResource(R.drawable.icon_snow);
                textView.setText(data);
                lineLeft.setBackgroundColor(ContextCompat.getColor(this, R.color.subText));
                lineRight.setBackgroundColor(ContextCompat.getColor(this, R.color.subText));
                info.setTextColor(ContextCompat.getColor(this, R.color.subText));
                info.setText("포근포근 눈와요");
                wind.setVisibility(View.INVISIBLE);
                snow.setVisibility(View.VISIBLE);
                rain.setVisibility(View.INVISIBLE);
                dust.setVisibility(View.INVISIBLE);
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusSnowy));
                break;
            }
            default: {
                relativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundSunny));
                head.setPadding(0, 0, 0, 0);
                head.setImageResource(R.drawable.head_sunny);
                icon.setImageResource(R.drawable.icon_sunny);
                textView.setText(data);
                heart.setVisibility(View.INVISIBLE);
                wind.setVisibility(View.INVISIBLE);
                snow.setVisibility(View.INVISIBLE);
                rain.setVisibility(View.INVISIBLE);
                dust.setVisibility(View.INVISIBLE);
                break;
            }
        }
    }

    private void setLionBody(int temper) {
        ImageView imageView = findViewById(R.id.lion_body);
        if (temper <= -5){
            imageView.setImageResource(R.drawable.body_minus5);
        }
        else if (temper <= 5){
            imageView.setImageResource(R.drawable.body_5);
        }
        else if (temper <= 10){
            imageView.setImageResource(R.drawable.body_10);
        }
        else if (temper <= 15){
            imageView.setImageResource(R.drawable.body_15);
        }
        else if (temper <= 20){
            imageView.setImageResource(R.drawable.body_20);
        }
        else if (temper <= 25){
            imageView.setImageResource(R.drawable.body_25);
        }
        else if (temper <= 30){
            imageView.setImageResource(R.drawable.body_30);
        }
        else{
            imageView.setImageResource(R.drawable.body_30plus);
        }
    }

    private void setDate() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("M월 d일 EEEE");
        SimpleDateFormat sdf2 = new SimpleDateFormat("k");
        String getDay = sdf.format(date);

        TextView textView = findViewById(R.id.Time);
        textView.setText(getDay);
    }

    private void getFineDust() {
        Call<ResponseBody> fineDust = service.fineDust();
        fineDust.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String res = response.body().string();
                    setFineDust(res);
                    Log.d("test","dust, " + res);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("test","dust, " + t);
            }
        });
    }

    private void setFineDust(String string) {
        Log.d("test", "setFineDust, " + string);
        dust = Integer.parseInt(string);
        Intent intent = new Intent("dust");
        if (dust <= 15){
            intent.putExtra("dust", "좋음");
        }
        else if (dust <= 35){
            intent.putExtra("dust", "보통");
        }
        else if (dust <= 75){
            intent.putExtra("dust", "나쁨");
        }
        else{
            intent.putExtra("dust", "매우나쁨");
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public class TimeItem {

    }

    public class WeekItem {

    }
}
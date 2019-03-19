package com.example.thisweather;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
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

        Call<JsonObject> weekForecast = service.weekForecast();
        weekForecast.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("test","week, " + response.body().toString());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("test","week, " + t);
            }
        });

        getFineDust();
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
                JsonArray array = response.body().get("wid").getAsJsonObject().get("body").getAsJsonArray().get(0).getAsJsonObject().get("data").getAsJsonArray();
                Log.d("test","local, " + array);
                getData(array);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("test","local, " + t +"");
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

    private void getData(JsonArray array) {
        String data = array.get(0).getAsJsonObject().get("temp").getAsString();
        String data2 = array.get(0).getAsJsonObject().get("wfKor").getAsString();
        String data3 = array.get(0).getAsJsonObject().get("tmx").getAsString();
        String data4 = array.get(0).getAsJsonObject().get("tmn").getAsString();
        String data5 = array.get(0).getAsJsonObject().get("ws").getAsString();
        String data6 = array.get(0).getAsJsonObject().get("pop").getAsString();
        Log.d("test", data);
        Log.d("test", data2);
        Log.d("test", data3);
        Log.d("test", data4);
        Log.d("test", data5);
        Log.d("test", data6);

        Intent intent = new Intent("data");

        String str = getNoPoint(data);
        TextView textView = findViewById(R.id.temp);
        textView.setText(str + "°");

        int temper = Integer.parseInt(str);
        setLionBody(temper);
        setLionHead(data2);

        if (!data3.equals("-999.0")){
            intent.putExtra("max", getNoPoint(data3) + "°");
        }
        else
            intent.putExtra("max", "");
        if (!data4.equals("-999.0")){
            intent.putExtra("min", getNoPoint(data4) + "°");
        }
        else
            intent.putExtra("min", "");

        if (Double.parseDouble(data5) >= 5.0) {
            setLionHead("바람");
            intent.putExtra("windspeed", getNoPoint(data5) + "m/s");
        }
        else
            intent.putExtra("windspeed", getNoPoint(data5) + "m/s");

        intent.putExtra("rainfall", data6 + "%");

        intent.putExtra("sensetemp", getWindChill(data, data5) + "°");

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private String getWindChill(String data, String data5) {
        double temp = Double.parseDouble(data);
        double ws = Double.parseDouble(data5);

        double result = 13.12 + 0.6215 * temp - 11.37 * Math.pow(ws * 3.6, 0.16) + 0.3965 * temp * Math.pow(ws * 3.6, 0.16);
        return getNoPoint(result + "");
    }

    private String getNoPoint(String data) {
        double n = Double.parseDouble(data);
        return Math.round(n) + "";
    }

    private void setLionHead(String data2) {
        ImageView imageView = findViewById(R.id.lion_head);
        ImageView heart = findViewById(R.id.heart);
        ConstraintLayout relativeLayout = findViewById(R.id.rl_main);
        ImageView icon = findViewById(R.id.wthIcon);
        TextView textView = findViewById(R.id.wthtext);
        ImageView menu = findViewById(R.id.menu);
        TextView time = findViewById(R.id.Time);
        ImageView retry = findViewById(R.id.retry);
        ImageView lineLeft = findViewById(R.id.line_left);
        ImageView lineRight = findViewById(R.id.line_right);
        TextView info = findViewById(R.id.wthInfo);
        ImageView wind = findViewById(R.id.wind);
        ImageView rain = findViewById(R.id.rain);
        ImageView snow = findViewById(R.id.snow);

        heart.setImageResource(R.drawable.heart);
        wind.setImageResource(R.drawable.wind);
        rain.setImageResource(R.drawable.rain);
        snow.setImageResource(R.drawable.snow);

        switch (data2){
            case "바람": {
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.mainBlue));
                imageView.setImageResource(R.drawable.head_cloudy);
                heart.setVisibility(View.INVISIBLE);
                menu.setImageResource(R.drawable.icon_menu_white);
                time.setTextColor(getResources().getColor(R.color.backgroundDefault));
                retry.setImageResource(R.drawable.icon_retry_white);
                lineLeft.setImageResource(R.drawable.line_windy);
                lineRight.setImageResource(R.drawable.line_windy);
                info.setTextColor(getResources().getColor(R.color.mainBlue));
                info.setText("장난아닌 송도풍");
                wind.setVisibility(View.VISIBLE);
                snow.setVisibility(View.INVISIBLE);
                rain.setVisibility(View.INVISIBLE);
                getWindow().setStatusBarColor(getResources().getColor(R.color.statusBlue));
                break;
            }
            case "맑음": {
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.backgroundSunny));
                imageView.setImageResource(R.drawable.head_sunny);
                heart.setVisibility(View.VISIBLE);
                icon.setImageResource(R.drawable.icon_sunny);
                textView.setText(data2);
                lineLeft.setImageResource(R.drawable.line_sunny);
                lineRight.setImageResource(R.drawable.line_sunny);
                info.setTextColor(getResources().getColor(R.color.mainOrange));
                info.setText("기적같이 맑은 날");
                wind.setVisibility(View.INVISIBLE);
                snow.setVisibility(View.INVISIBLE);
                rain.setVisibility(View.INVISIBLE);
                getWindow().setStatusBarColor(getResources().getColor(R.color.statusSunny));
                break;
            }
            case "구름 조금": {
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.backgroundSunny));
                imageView.setImageResource(R.drawable.head_sunny);
                heart.setVisibility(View.VISIBLE);
                icon.setImageResource(R.drawable.icon_sunny);
                textView.setText(data2);
                lineLeft.setImageResource(R.drawable.line_sunny);
                lineRight.setImageResource(R.drawable.line_sunny);
                info.setTextColor(getResources().getColor(R.color.mainOrange));
                info.setText("기적같이 맑은 날");
                wind.setVisibility(View.INVISIBLE);
                snow.setVisibility(View.INVISIBLE);
                rain.setVisibility(View.INVISIBLE);
                getWindow().setStatusBarColor(getResources().getColor(R.color.statusSunny));
                break;
            }
            case "구름 많음": {
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.mainBlue));
                imageView.setImageResource(R.drawable.head_sunny);
                heart.setVisibility(View.VISIBLE);
                icon.setImageResource(R.drawable.icon_cloudy);
                textView.setText(data2);
                menu.setImageResource(R.drawable.icon_menu_white);
                time.setTextColor(getResources().getColor(R.color.backgroundDefault));
                retry.setImageResource(R.drawable.icon_retry_white);
                lineLeft.setImageResource(R.drawable.line_windy);
                lineRight.setImageResource(R.drawable.line_windy);
                info.setTextColor(getResources().getColor(R.color.mainBlue));
                info.setText("꾸리꾸리 흐린 날");
                wind.setVisibility(View.INVISIBLE);
                snow.setVisibility(View.INVISIBLE);
                rain.setVisibility(View.INVISIBLE);
                getWindow().setStatusBarColor(getResources().getColor(R.color.statusBlue));
                break;
            }
            case "흐림": {
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.mainBlue));
                imageView.setImageResource(R.drawable.head_sunny);
                heart.setVisibility(View.VISIBLE);
                icon.setImageResource(R.drawable.icon_cloudy);
                textView.setText(data2);
                menu.setImageResource(R.drawable.icon_menu_white);
                time.setTextColor(getResources().getColor(R.color.backgroundDefault));
                retry.setImageResource(R.drawable.icon_retry_white);
                lineLeft.setImageResource(R.drawable.line_windy);
                lineRight.setImageResource(R.drawable.line_windy);
                info.setTextColor(getResources().getColor(R.color.mainBlue));
                info.setText("꾸리꾸리 흐린 날");
                wind.setVisibility(View.INVISIBLE);
                snow.setVisibility(View.INVISIBLE);
                rain.setVisibility(View.INVISIBLE);
                getWindow().setStatusBarColor(getResources().getColor(R.color.statusBlue));
                break;
            }
            case "비": {
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.backgroundRainy));
                imageView.setImageResource(R.drawable.head_rain);
                heart.setVisibility(View.INVISIBLE);
                icon.setImageResource(R.drawable.icon_rain);
                textView.setText(data2);
                menu.setImageResource(R.drawable.icon_menu_white);
                time.setTextColor(getResources().getColor(R.color.backgroundDefault));
                retry.setImageResource(R.drawable.icon_retry_white);
                lineLeft.setImageResource(R.drawable.line_rainy);
                lineRight.setImageResource(R.drawable.line_rainy);
                info.setTextColor(getResources().getColor(R.color.backgroundRainy));
                info.setText("추적추적 비와요");
                wind.setVisibility(View.INVISIBLE);
                snow.setVisibility(View.INVISIBLE);
                rain.setVisibility(View.VISIBLE);
                getWindow().setStatusBarColor(getResources().getColor(R.color.statusRainy));
                break;
            }
            case "눈/비": {
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.backgroundRainy));
                imageView.setImageResource(R.drawable.head_rain);
                heart.setVisibility(View.INVISIBLE);
                icon.setImageResource(R.drawable.icon_rainsnow);
                textView.setText(data2);
                menu.setImageResource(R.drawable.icon_menu_white);
                time.setTextColor(getResources().getColor(R.color.backgroundDefault));
                retry.setImageResource(R.drawable.icon_retry_white);
                lineLeft.setImageResource(R.drawable.line_rainy);
                lineRight.setImageResource(R.drawable.line_rainy);
                info.setTextColor(getResources().getColor(R.color.backgroundRainy));
                info.setText("추적추적 비와요");
                wind.setVisibility(View.INVISIBLE);
                snow.setVisibility(View.INVISIBLE);
                rain.setVisibility(View.VISIBLE);
                getWindow().setStatusBarColor(getResources().getColor(R.color.statusRainy));
                break;
            }
            case "눈": {
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.backgroundSnowy));
                imageView.setImageResource(R.drawable.head_snow);
                heart.setVisibility(View.INVISIBLE);
                icon.setImageResource(R.drawable.icon_snow);
                textView.setText(data2);
                lineLeft.setImageResource(R.drawable.line_snowy);
                lineRight.setImageResource(R.drawable.line_snowy);
                info.setTextColor(getResources().getColor(R.color.subText));
                info.setText("포근포근 눈와요");
                wind.setVisibility(View.INVISIBLE);
                snow.setVisibility(View.VISIBLE);
                rain.setVisibility(View.INVISIBLE);
                getWindow().setStatusBarColor(getResources().getColor(R.color.statusSnowy));
                break;
            }
            default: {
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.backgroundSunny));
                imageView.setImageResource(R.drawable.head_sunny);
                icon.setImageResource(R.drawable.icon_sunny);
                textView.setText(data2);
                heart.setVisibility(View.INVISIBLE);
                wind.setVisibility(View.INVISIBLE);
                snow.setVisibility(View.INVISIBLE);
                rain.setVisibility(View.INVISIBLE);
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
        int dust = Integer.parseInt(string);
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
}
package com.example.thisweather.network;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitService {

    @GET("midterm")
    public Call<JsonObject> weekForecast();

    @GET("weather")
    public Call<JsonObject> localForecast();

    @GET("PM10")
    public Call<ResponseBody> fineDust();

}

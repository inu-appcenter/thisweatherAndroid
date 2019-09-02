package com.inu.thisweather.network;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitService {
    String baseUrl = "http://117.16.191.242:7004/";

    @GET("midterm")
    Call<JsonObject> weekForecast();

    @GET("weather")
    Call<JsonObject> localForecast();

    @GET("PM10")
    Call<ResponseBody> fineDust();

}

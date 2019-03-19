package com.example.thisweather;

import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET("week")
    public Call<JsonObject> weekForecast();

    @GET("weather")
    public Call<JsonObject> localForecast();

    @GET("dust")
    public Call<ResponseBody> fineDust();

}

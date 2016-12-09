package com.example.savagavran.sunshine.sync;

import com.example.savagavran.sunshine.data.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("forecast/daily?")
    @Headers("Content-Type: application/json")
    Call<WeatherResponse> getDailyWeatherData(@Query("id") String id, @Query("mode") String mode,
                                              @Query("units") String units, @Query("cnt") int count,
                                              @Query("APPID") String appid);
}
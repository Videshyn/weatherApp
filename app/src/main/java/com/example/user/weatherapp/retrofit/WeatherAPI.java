package com.example.user.weatherapp.retrofit;

import android.util.Log;


import com.example.user.weatherapp.pojo.city_pojo.ExampleCity;
import com.example.user.weatherapp.pojo.coords_pojo.Example;
import com.example.user.weatherapp.pojo.pojo_robot.OpenWeatherMapJSON;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by User on 17.10.2017
 */

public class WeatherAPI {

    private static final String TAG = "logs";
    public static String KEY = "9f6dc9e441d2f589ff7e472d7d352a61";
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static Retrofit retrofit = null;

    public interface ApiInterface {
        @GET("weather")
        Observable<ExampleCity> getCityWeather(
                @Query("q") String q,
                @Query("appid") String appid
        );

    }

    public interface WeatherInterface {
        @GET("find")
        Observable<Example> getAll(
                @Query("lat") Double lat,
                @Query("lon") Double lon,
                @Query("cnt") Integer cnt,
                @Query("appid") String appid
        );
    }

    public interface FiveDaysWeather{
        @GET("forecast")
        Observable<OpenWeatherMapJSON> getFiveDaysWeather(
                @Query("id") int id,
                @Query("appid") String appid
        );
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

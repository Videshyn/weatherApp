package com.example.user.weatherapp.utils;

import com.example.user.weatherapp.retrofit.WeatherAPI;

/**
 * Created by User on 29.11.2017
 */

public class Const {

    //WeatherDescriptionFragment
    public static final int COUNT_DAYS = 5;
    public static final String JSON = "JSON";
    public static final String POSITION = "POSITION";
    public static final String CHECK_PARAM = "CHECK_PARAM";

    //WeatherAdapter
    public static final String PNG = ".png";
    public static final String ICON_URL = "http://openweathermap.org/img/w/";

    //CityListFragment
    public static final  String LAT = "lat";
    public static final  String LNG = "lng";
    public static final int DEFAULT_CNT = 50;
    public static final int ONE_ELEMENT = 200;
    public static final int MANY_ELEMENTS = 100;
    public static final String KEY = WeatherAPI.KEY;

    //WeatherPagerFragment
    public static final String RESPONSE = "RESPONSE";
    public static final String PAGER_POSITION = "PAGER_POSITION";

}

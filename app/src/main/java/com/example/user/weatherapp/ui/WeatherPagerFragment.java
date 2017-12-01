package com.example.user.weatherapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.weatherapp.R;
import com.example.user.weatherapp.pojo.pojo_robot.OpenWeatherMapJSON;
import com.example.user.weatherapp.utils.Const;
import com.google.gson.Gson;

import java.math.BigDecimal;

import static com.example.user.weatherapp.utils.Const.LAT;
import static com.example.user.weatherapp.utils.Const.LNG;
import static com.example.user.weatherapp.utils.Const.PAGER_POSITION;
import static com.example.user.weatherapp.utils.Const.PNG;
import static com.example.user.weatherapp.utils.Const.RESPONSE;

/**
 * Created by User on 30.11.2017
 */

public class WeatherPagerFragment extends Fragment {

    private static final String TAG = WeatherPagerFragment.class.getSimpleName();
    private int pagerPosition;
    private String response;
    private ImageView img;
    private OpenWeatherMapJSON weatherMapJSON;
    private TextView temperature, temperatureMax, temperatureMin, pressure, humidity, description, wind;


    public static WeatherPagerFragment newInstance(String responce, int pagerPosition) {
        Log.d(TAG, "call newInstance:");
        Bundle args = new Bundle();
        args.putString(RESPONSE, responce);
        args.putInt(PAGER_POSITION, pagerPosition);
        WeatherPagerFragment fragment = new WeatherPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

        private void initUI(View view){
//        toolbar = view.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        img = view.findViewById(R.id.img_full_fragment);
        temperature = view.findViewById(R.id.temperature_full_fragment);
        temperatureMin = view.findViewById(R.id.min_temperature_full_fragment);
        temperatureMax = view.findViewById(R.id.max_temperature_full_fragment);
        pressure = view.findViewById(R.id.pressure_full_fragment);
        humidity = view.findViewById(R.id.humidity_full_fragment);
        description = view.findViewById(R.id.description_full_fragment);
        wind = view.findViewById(R.id.wind_full_fragment);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        pagerPosition = getArguments().getInt(PAGER_POSITION);
        response = getArguments().getString(RESPONSE);
        Log.d(TAG, "response = " + response);
        weatherMapJSON = new Gson().fromJson(response, OpenWeatherMapJSON.class);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pager_fragment, container, false);
        initUI(view);

        switch (pagerPosition){
            case 0:
                Glide.with(img.getContext()).load(Const.ICON_URL + weatherMapJSON.getList().get(0).getWeather().get(0).getIcon() + PNG).into(img);
                double temp = new BigDecimal(weatherMapJSON.getList().get(0).getMain().getTemp() - 273.15).setScale(2, BigDecimal.ROUND_UP).doubleValue();
                temperature.setText("Temperature now = " + temp + " °C");
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            default:
                break;
        }


        return view;
    }
}

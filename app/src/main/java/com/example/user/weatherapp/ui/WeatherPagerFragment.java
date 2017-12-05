package com.example.user.weatherapp.ui;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.weatherapp.R;
import com.example.user.weatherapp.pojo.pojo_robot.OpenWeatherMapJSON;
import com.example.user.weatherapp.utils.Const;
import com.example.user.weatherapp.utils.DBHelper;
import com.google.gson.Gson;

import org.joda.time.DateTime;

import java.math.BigDecimal;

import static com.example.user.weatherapp.utils.Const.DB_NAME;
import static com.example.user.weatherapp.utils.Const.DB_VERSION;
import static com.example.user.weatherapp.utils.Const.PAGER_POSITION;
import static com.example.user.weatherapp.utils.Const.PNG;
import static com.example.user.weatherapp.utils.Const.RESPONSE;

/**
 * Created by User on 30.11.2017
 */

public class WeatherPagerFragment extends Fragment {

    private static final String TAG = WeatherPagerFragment.class.getSimpleName();
    private static final String CITY_NAME = "CITY_NAME";
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private int pagerPosition;
    private String response, cityName;
    private ImageView img;
    private OpenWeatherMapJSON weatherMapJSON;
    private TextView temperature, temperatureMax, temperatureMin, pressure, humidity, description, wind;

    public static WeatherPagerFragment newInstance(String responce, int pagerPosition, String cityName) {
        Log.d(TAG, "call newInstance:");
        Bundle args = new Bundle();
        args.putString(RESPONSE, responce);
        args.putInt(PAGER_POSITION, pagerPosition);
        args.putString(CITY_NAME, cityName);
        WeatherPagerFragment fragment = new WeatherPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initUI(View view){
        img = view.findViewById(R.id.img_full_fragment);
        temperature = view.findViewById(R.id.temperature_full_fragment);
        temperatureMin = view.findViewById(R.id.min_temperature_full_fragment);
        temperatureMax = view.findViewById(R.id.max_temperature_full_fragment);
        pressure = view.findViewById(R.id.pressure_full_fragment);
        humidity = view.findViewById(R.id.humidity_full_fragment);
        description = view.findViewById(R.id.description_full_fragment);
        wind = view.findViewById(R.id.wind_full_fragment);
        dbHelper = new DBHelper(getContext(), DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        pagerPosition = getArguments().getInt(PAGER_POSITION);
        response = getArguments().getString(RESPONSE);
        cityName = getArguments().getString(CITY_NAME);
        Log.d(TAG, "response = " + response);
        weatherMapJSON = new Gson().fromJson(response, OpenWeatherMapJSON.class);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pager_fragment, container, false);
        initUI(view);
        int plusDays = 0;
        switch (pagerPosition){
            case 0:
                fillPagerFragment(plusDays);
                break;
            case 1:
                plusDays = 1;
                fillPagerFragment(plusDays);
                Log.d(TAG, "date8 = " + weatherMapJSON.getList().get(8).getDtTxt());
                break;
            case 2:
                plusDays = 2;
                Log.d(TAG, "date16 = " + weatherMapJSON.getList().get(16).getDtTxt());
                fillPagerFragment(plusDays);
                break;
            case 3:
                plusDays = 3;
                Log.d(TAG, "date24 = " + weatherMapJSON.getList().get(24).getDtTxt());
                fillPagerFragment(plusDays);
                break;
            case 4:
                plusDays = 4;
                Log.d(TAG, "date32 = " + weatherMapJSON.getList().get(32).getDtTxt());
                fillPagerFragment(plusDays);
                break;
            default:
                break;
        }
        setHasOptionsMenu(true);
        return view;
    }

    private void fillPagerFragment(int plusDays){
        String fullTmp = "";
        String jodaDay = new DateTime().plusDays(plusDays).toLocalDate().toString();
        for (int i = 0, n = weatherMapJSON.getList().size(); i < n; i ++){
            String loopToday = weatherMapJSON.getList().get(i).getDtTxt().split(" ")[0];
            if (loopToday.equals(jodaDay)){
                double temp = new BigDecimal(weatherMapJSON.getList().get(i).getMain().getTemp() - 273.15).setScale(2, BigDecimal.ROUND_UP).doubleValue();
                fullTmp = fullTmp + "\n" + weatherMapJSON.getList().get(i).getDtTxt() + "        " + temp + getString(R.string.celciy);
            }
        }
        temperature.setText(getString(R.string.temperature_today) + fullTmp);
        plusDays = plusDays * 8;
        Glide.with(img.getContext()).load(Const.ICON_URL + weatherMapJSON.getList().get(plusDays).getWeather().get(0).getIcon() + PNG).into(img);

        double tempMin = new BigDecimal(weatherMapJSON.getList().get(plusDays).getMain().getTempMin() - 273.15).setScale(2, BigDecimal.ROUND_UP).doubleValue();
        temperatureMin.setText(getString(R.string.min_temperature) + tempMin + getString(R.string.celciy));
        double tempMax = new BigDecimal(weatherMapJSON.getList().get(plusDays).getMain().getTempMax() - 273.15).setScale(2, BigDecimal.ROUND_UP).doubleValue();
        temperatureMax.setText(getString(R.string.max_temperature) + tempMax + getString(R.string.celciy));
        pressure.setText(getString(R.string.pressure) + weatherMapJSON.getList().get(plusDays).getMain().getPressure() + getString(R.string.mbar));
        humidity.setText(getString(R.string.humidty) + weatherMapJSON.getList().get(plusDays).getMain().getHumidity() + "%");
        description.setText(getString(R.string.description) + weatherMapJSON.getList().get(plusDays).getWeather().get(0).getDescription());
        if (weatherMapJSON.getList().get(plusDays).getWind() != null){
            wind.setText(getString(R.string.wind_city_speed)  + weatherMapJSON.getList().get(plusDays).getWind().getSpeed() + getString(R.string.km_h));
        }else {
            wind.setText(R.string.wind_city_null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: ");
        switch (item.getItemId()){
            case android.R.id.home:
                getFragmentManager().popBackStack();
                Log.d(TAG, "size = " + getFragmentManager().getBackStackEntryCount());
                return true;
            case R.id.add_to_history:
                dbHelper.saveHistory(response, pagerPosition, cityName);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        MenuItem addItem = menu.findItem(R.id.add_to_history);
        addItem.setVisible(true);
        MenuItem history = menu.findItem(R.id.my_history);
        history.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }
}

package com.example.user.weatherapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.weatherapp.R;
import com.example.user.weatherapp.pojo.city_pojo.ExampleCity;
import com.example.user.weatherapp.pojo.coords_pojo.Example;
import com.example.user.weatherapp.pojo.coords_pojo.List;
import com.example.user.weatherapp.pojo.pojo_robot.ListItem;
import com.example.user.weatherapp.pojo.pojo_robot.OpenWeatherMapJSON;
import com.example.user.weatherapp.retrofit.WeatherAPI;
import com.example.user.weatherapp.utils.Const;
import com.google.gson.Gson;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.user.weatherapp.utils.Const.CHECK_PARAM;
import static com.example.user.weatherapp.utils.Const.JSON;
import static com.example.user.weatherapp.utils.Const.POSITION;

public class WeatherDescriptionFragment extends Fragment {


    private String json;
    private int position;
    private int checkParam;
    private static final String TAG = WeatherDescriptionFragment.class.getSimpleName();
    private Toolbar toolbar;
    private ImageView img;
    private TextView temperature, temperatureMax, temperatureMin, pressure, humidity, description, wind;
    private String responce;
    private java.util.List<ListItem> weekList = new ArrayList<>();
    private OpenWeatherMapJSON weatherMapJSON = new OpenWeatherMapJSON();
    private CityListFragment.Listener listener;

    public static WeatherDescriptionFragment newInstance(String param1, int param2, int checkParam) {
        WeatherDescriptionFragment fragment = new WeatherDescriptionFragment();
        Bundle args = new Bundle();
        args.putString(JSON, param1);
        args.putInt(POSITION, param2);
        args.putInt(CHECK_PARAM, checkParam);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            json = getArguments().getString(JSON);
            position = getArguments().getInt(POSITION);
            checkParam = getArguments().getInt(CHECK_PARAM);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_description, container, false);
        ViewPager viewPager = view.findViewById(R.id.pager);
        callWeatherWeekAPI(viewPager);
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        return view;
    }
    private void callWeatherWeekAPI(ViewPager viewPager){
        listener.openProgressDialog();
        Integer idCity = 0;
        if (checkParam == Const.MANY_ELEMENTS){
            Example example = new Gson().fromJson(json, Example.class);
            java.util.List<List> exampleList = example.getList();
            idCity = exampleList.get(position).getId();
        }else if (checkParam == Const.ONE_ELEMENT) {
            ExampleCity exampleCity = new Gson().fromJson(json, ExampleCity.class);
            idCity = exampleCity.getId();
        }

        WeatherAPI.getClient()
                .create(WeatherAPI.FiveDaysWeather.class)
                .getFiveDaysWeather(idCity, Const.KEY)
                .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(fiveDays -> {
                weekList = fiveDays.getList();
                weatherMapJSON.setList(weekList);
                Log.d(TAG, "in rx list size = " + weekList.size());
                Log.d(TAG, "callWeatherWeekAPI: " + fiveDays.getList().size());
                listener.closeProgressDialog();
                responce = new Gson().toJson(weatherMapJSON, OpenWeatherMapJSON.class);
                Log.d(TAG, "responce = " + responce);
                viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(), responce));
                toolbar.setTitle(fiveDays.getCity().getName() + "");

            });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "call onAttach()");
        if (context instanceof CityListFragment.Listener){
            listener = (CityListFragment.Listener) context;
        }else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_weather_description, container, false);
//        initUI(view);
//
//        if (checkParam == 100){
//            Log.d(TAG, "json" + json);
//            Example example = new Gson().fromJson(json, Example.class);
//            java.util.List<List> exampleList = example.getList();
//            toolbar.setTitle(exampleList.get(position).getName() + "");
//            Glide.with(img.getContext()).load(ICON_URL
//                    + exampleList.get(position).getWeather().get(0).getIcon()
//                    + PNG).into(img);
//            double temp = new BigDecimal(exampleList.get(position).getMain().getTemp() - 273.15).setScale(2, BigDecimal.ROUND_UP).doubleValue();
//            temperature.setText("Temperature now = " + temp + " °C");
//            double tempMin = new BigDecimal(exampleList.get(position).getMain().getTempMin() - 273.15).setScale(2, BigDecimal.ROUND_UP).doubleValue();
//            temperatureMin.setText("min temperature = " + tempMin + " °C");
//            double tempMax = new BigDecimal(exampleList.get(position).getMain().getTempMax() - 273.15).setScale(2, BigDecimal.ROUND_UP).doubleValue();
//            temperatureMax.setText("max temperature = " + tempMax + " °C");
//            pressure.setText("pressure = " + exampleList.get(position).getMain().getPressure() + "mbar");
//            humidity.setText("humidity = " + exampleList.get(position).getMain().getHumidity() + "%");
//            description.setText("description: " + exampleList.get(position).getWeather().get(0).getDescription());
//            if (exampleList.get(position).getWind() != null){
//                wind.setText("WindCity speed = "  + exampleList.get(position).getWind().getSpeed() + "km/h");
//            }else {
//                wind.setText("WindCity speed = 0.0km/h");
//            }
//        }else if (checkParam == 200){
//            ExampleCity exampleCity = new Gson().fromJson(json, ExampleCity.class);
//            toolbar.setTitle(exampleCity.getName() + "");
//            Glide.with(img.getContext())
//                    .load(ICON_URL
//                            + exampleCity.getWeather().get(0).getIcon()
//                            + PNG)
//                    .into(img);
//            double temp = new BigDecimal(exampleCity.getMain().getTemp() - 273.15).setScale(2, BigDecimal.ROUND_UP).doubleValue();
//            temperature.setText("Temperature now = " + temp + " °C");
//            double tempMin = new BigDecimal(exampleCity.getMain().getTempMin() - 273.15).setScale(2, BigDecimal.ROUND_UP).doubleValue();
//            temperatureMin.setText("min temperature = " + tempMin + " °C");
//            double tempMax = new BigDecimal(exampleCity.getMain().getTempMax() - 273.15).setScale(2, BigDecimal.ROUND_UP).doubleValue();
//            temperatureMax.setText("max temperature = " + tempMax + " °C");
//            pressure.setText("pressure = " + exampleCity.getMain().getPressure() + "mbar");
//            humidity.setText("humidity = " + exampleCity.getMain().getHumidity() + "%");
//            description.setText("description: " + exampleCity.getWeather().get(0).getDescription());
//            if (exampleCity.getWind() != null){
//                wind.setText("WindCity speed = "  + exampleCity.getWind().getSpeed() + "km/h");
//            }else {
//                wind.setText("WindCity speed = 0.0km/h");
//            }
//        }
//
//        setHasOptionsMenu(true);
//        return view;
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: ");
        switch (item.getItemId()){
            case android.R.id.home:
                getFragmentManager().popBackStack();
                Log.d(TAG, "size = " + getFragmentManager().getBackStackEntryCount());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


//    private void initUI(View view){
//        toolbar = view.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        img = view.findViewById(R.id.img_full_fragment);
//        temperature = view.findViewById(R.id.temperature_full_fragment);
//        temperatureMin = view.findViewById(R.id.min_temperature_full_fragment);
//        temperatureMax = view.findViewById(R.id.max_temperature_full_fragment);
//        pressure = view.findViewById(R.id.pressure_full_fragment);
//        humidity = view.findViewById(R.id.humidity_full_fragment);
//        description = view.findViewById(R.id.description_full_fragment);
//        wind = view.findViewById(R.id.wind_full_fragment);
//    }



    class MyPagerAdapter extends FragmentStatePagerAdapter {

        private String str;
        public MyPagerAdapter(FragmentManager fm, String response) {
            super(fm);
            str = response;
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "in adapter response = " + str);
            return WeatherPagerFragment.newInstance(str, position);
        }

        @Override
        public int getCount() {
            return Const.COUNT_DAYS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Today";
                case 1:
                    return "Tomorrow";
                case 2:
                    return "Today + 2 days";
                case 3:
                    return "Today + 3 days";
                case 4:
                    return "Today + 4 days";
                default:
                    return "Error";
            }
        }
    }

}

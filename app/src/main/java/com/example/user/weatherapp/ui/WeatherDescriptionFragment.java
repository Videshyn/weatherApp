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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.weatherapp.R;
import com.example.user.weatherapp.pojo.coords_pojo.MainCityModel;
import com.example.user.weatherapp.pojo.coords_pojo.MainModelList;
import com.example.user.weatherapp.pojo.coords_pojo.WrapperMainCityModel;
import com.example.user.weatherapp.pojo.pojo_robot.ListItem;
import com.example.user.weatherapp.pojo.pojo_robot.OpenWeatherMapJSON;
import com.example.user.weatherapp.retrofit.WeatherAPI;
import com.example.user.weatherapp.utils.Const;
import com.example.user.weatherapp.utils.DBHelper;
import com.google.gson.Gson;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.user.weatherapp.utils.Const.CURRENT_POSITION;
import static com.example.user.weatherapp.utils.Const.DB_CHECK;
import static com.example.user.weatherapp.utils.Const.DB_NAME;
import static com.example.user.weatherapp.utils.Const.DB_VERSION;
import static com.example.user.weatherapp.utils.Const.JSON;
import static com.example.user.weatherapp.utils.Const.POSITION;

public class WeatherDescriptionFragment extends Fragment {


    private String json;
    private int position, currentPosition;
    private static final String TAG = WeatherDescriptionFragment.class.getSimpleName();
    private Toolbar toolbar;
    private ImageView img;
    private TextView temperature, temperatureMax, temperatureMin, pressure, humidity, description, wind;
    private String responce;
    private java.util.List<ListItem> weekList = new ArrayList<>();
    private OpenWeatherMapJSON weatherMapJSON = new OpenWeatherMapJSON();
    private CityListFragment.Listener listener;
    private Listener dbSaveListener;


    public static WeatherDescriptionFragment newInstance(String json, int position, int currentPosition) {
        WeatherDescriptionFragment fragment = new WeatherDescriptionFragment();
        Bundle args = new Bundle();
        args.putString(JSON, json);
        args.putInt(POSITION, position);
        args.putInt(CURRENT_POSITION, currentPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            json = getArguments().getString(JSON);
            position = getArguments().getInt(POSITION);
            currentPosition = getArguments().getInt(CURRENT_POSITION);
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
        if (position == DB_CHECK){
            viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(), json, null));
            viewPager.setCurrentItem(currentPosition);
            listener.closeProgressDialog();
        }else {
            MainCityModel citiesWeatherModel = new Gson().fromJson(json, MainCityModel.class);
            java.util.List<MainModelList> weatherList = citiesWeatherModel.getList();
            if (citiesWeatherModel.getCount() == null){
                idCity = citiesWeatherModel.getId();
            }else {
                idCity = weatherList.get(position).getId();
            }
            WeatherAPI.getClient()
                    .create(WeatherAPI.FiveDaysWeather.class)
                    .getFiveDaysWeather(idCity, Const.KEY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(fiveDays -> {
                        weekList = fiveDays.getList();
                        weatherMapJSON.setList(weekList);
                        String cityName = fiveDays.getCity().getName();
                        listener.closeProgressDialog();
                        responce = new Gson().toJson(weatherMapJSON, OpenWeatherMapJSON.class);
                        viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(), responce, cityName));
                        toolbar.setTitle(fiveDays.getCity().getName() + "");
                    });
        }
    }

    public interface Listener{
        void callDbHelperSave();
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
                setHasOptionsMenu(true);
               break;
            case R.id.add_to_history:

        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        if (position == DB_CHECK){
            menu.clear();
        }else {
            MenuItem addItem = menu.findItem(R.id.add_to_history);
            addItem.setVisible(false);
        }
//        MenuItem history = menu.findItem(R.id.my_history);
//        history.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {

        private String json, city;
        public MyPagerAdapter(FragmentManager fm, String response, String cityName) {
            super(fm);
            json = response;
            city = cityName;
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "in adapter response = " + json);
            return WeatherPagerFragment.newInstance(json, position, city);
        }

        @Override
        public int getCount() {
            if (city == null){
                return new Gson()
                        .fromJson(json, WrapperMainCityModel.class)
                        .getModelList()
                        .size();
            }else
                return Const.COUNT_DAYS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (city == null){
                return new Gson()
                        .fromJson(json, WrapperMainCityModel.class)
                        .getModelList()
                        .get(position)
                        .getName();
            }else {
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

}

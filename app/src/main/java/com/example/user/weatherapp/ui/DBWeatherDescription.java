package com.example.user.weatherapp.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.example.user.weatherapp.pojo.HistoryModel;
import com.google.gson.Gson;

import static com.example.user.weatherapp.utils.Const.JSON;

/**
 * Created by User on 06.12.2017
 */

public class DBWeatherDescription extends Fragment {

    private String json;
    private HistoryModel historyModel;
    private ImageView img;
    private TextView description, date, wind, temp, pressure, humidity;

    public static DBWeatherDescription newInstance(String json) {
        DBWeatherDescription fragment = new DBWeatherDescription();
        Bundle args = new Bundle();
        args.putString(JSON, json);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        json = getArguments().getString(JSON);
    }

    private void initUi(View view){
        img = view.findViewById(R.id.img_full_fragment);
        description = view.findViewById(R.id.description_full_fragment);
        //date
        date = view.findViewById(R.id.min_temperature_full_fragment);
        //wind
        wind = view.findViewById(R.id.max_temperature_full_fragment);
        temp = view.findViewById(R.id.temperature_full_fragment);
        pressure = view.findViewById(R.id.pressure_full_fragment);
        humidity = view.findViewById(R.id.humidity_full_fragment);

        historyModel = new Gson().fromJson(json, HistoryModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pager_fragment, container, false);

        Toolbar toolbar = view.findViewById(R.id.pager_fragment_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initUi(view);

        Glide.with(getContext()).load(historyModel.getIconUrlModel()).into(img);
        description.setText(historyModel.getDescriptionModel());
        date.setText(historyModel.getDataModel());
        wind.setText(historyModel.getWindModel());
        temp.setText(historyModel.getTmpModel());
        pressure.setText(historyModel.getPressureModel());
        humidity.setText(historyModel.getHumadityModel());

        setHasOptionsMenu(true);
        return view;
    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu, menu);
//        menu.clear();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getFragmentManager().popBackStack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

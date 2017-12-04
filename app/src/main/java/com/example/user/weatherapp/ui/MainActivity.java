package com.example.user.weatherapp.ui;

import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.user.weatherapp.R;
import com.example.user.weatherapp.utils.Utils;
import com.google.android.gms.location.LocationServices;

import net.danlew.android.joda.JodaTimeAndroid;

public class MainActivity extends AppCompatActivity implements CityListFragment.Listener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private CityListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JodaTimeAndroid.init(this);

//        WeatherAPI.WeekResponce api = WeatherAPI.getClient().create(WeatherAPI.WeekResponce.class);
//        Call<WeekWeather> call = api.getWeek(706483, Const.KEY);
//
//        call.enqueue(new Callback<WeekWeather>() {
//            @Override
//            public void onResponse(Call<WeekWeather> call, Response<WeekWeather> response) {
//                Log.d(TAG, "" + response.body());
//            }
//
//            @Override
//            public void onFailure(Call<WeekWeather> call, Throwable t) {
//                Log.d(TAG, "" + t.getMessage());
//            }
//        });

        if (Utils.checkNetworkConnection(this)){
            startWeatherFragment();
        }else {
            showErrorDialog();
        }
    }

    private void showProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.title_progress_dialog);
        progressDialog.show();
    }

    private void startWeatherFragment(){
        showProgressDialog();

        LocationServices
                .getFusedLocationProviderClient(this)
                .getLastLocation()
                .addOnSuccessListener(this, location -> {
                if (location != null){
                    fragment = CityListFragment.newInstance(location.getLatitude(), location.getLongitude());
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
        });
    }

    private void showErrorDialog(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_network_error)
                .setMessage(R.string.error_network)
                .setPositiveButton(R.string.retry,(dialog, id) -> {
            if (Utils.checkNetworkConnection(this)){
                startWeatherFragment();
                dialog.dismiss();
            }else {
                showErrorDialog();
            }
        }).show();
    }



    @Override
    public void refreshCoords() {
        LocationServices
                .getFusedLocationProviderClient(this)
                .getLastLocation()
                .addOnSuccessListener(this, location -> fragment.onLocationUpdate(location));
    }


    @Override
    public void onBackPressed() {
        if (fragment != null){
            if (fragment.lastCity != null){
                if (getSupportFragmentManager().getBackStackEntryCount() == 0){
                    if (fragment.getElementsCount() > 1)super.onBackPressed();
                    fragment.clickBackCallAPI();
                }else if(getSupportFragmentManager().getBackStackEntryCount() != 0){
                    if (fragment.getElementsCount() > 1)super.onBackPressed();
                    else {
                        getSupportFragmentManager().popBackStack();
                        fragment.clickBackCallCityAPI();
                    }
                }else super.onBackPressed();
            }else super.onBackPressed();
        }else super.onBackPressed();
    }


    @Override
    public void openProgressDialog() {
        showProgressDialog();
    }

    @Override
    public void closeProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

}

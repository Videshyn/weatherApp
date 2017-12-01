package com.example.user.weatherapp.ui;

import android.app.ProgressDialog;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.user.weatherapp.R;
import com.example.user.weatherapp.pojo.week_pojo.WeekWeather;
import com.example.user.weatherapp.retrofit.WeatherAPI;
import com.example.user.weatherapp.utils.Const;
import com.example.user.weatherapp.utils.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements WeatherFragment.WeatherFragmentListener, WeatherDescriptionFragment.WeatherDescriptionFragmentListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private double lat = -100;
    private double lng = -100;
    private ProgressDialog progressDialog;
    private WeatherFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    if (lat >= 0.0 && lng >= 0.0){
                        fragment = WeatherFragment.newInstance(lat, lng);
                        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.container, fragment);
                        ft.commit();
                    }
                }
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
    public void closeProgressDialog() {
        Log.d(TAG, "closeProgressDialog: ");
        progressDialog.dismiss();
    }

    @Override
    public ArrayList<Double> refreshCoords() {
        ArrayList<Double> coords = new ArrayList<>();
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
            coords.add(location.getLatitude());
            coords.add(location.getLongitude());
        });
        return coords;
    }

    @Override
    public void openProgressDialog() {
        showProgressDialog();
    }

    @Override
    public void onBackPressed() {
        if (fragment != null){
            if (getSupportFragmentManager().getBackStackEntryCount() == 0 && !"null".equals(WeatherFragment.lastCity)){
                fragment.clickBackCallAPI();
            }else if (!"null".equals(WeatherFragment.lastCity) && getSupportFragmentManager().getBackStackEntryCount() != 0){
                getSupportFragmentManager().popBackStack();
                fragment.clickBackCallCityAPI();
            }else {
                super.onBackPressed();
            }
        }else
            super.onBackPressed();

    }

    @Override
    public void showProgressDialogInDescription() {
        showProgressDialog();
    }

    @Override
    public void closeProgressDialogInDescription() {
        progressDialog.dismiss();
    }
}

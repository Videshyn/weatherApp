package com.example.user.weatherapp.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.weatherapp.R;
import com.example.user.weatherapp.utils.Utils;
import com.google.android.gms.location.LocationServices;

import net.danlew.android.joda.JodaTimeAndroid;

public class MainActivity extends AppCompatActivity implements CityListFragment.Listener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_REFRESH_COORDS = 200;
    private ProgressDialog progressDialog;
    private CityListFragment fragment;
    private static final int REQUEST_CODE_START_FRAGMENT = 400;
    private EditText latET, lonET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JodaTimeAndroid.init(this);
        Log.d(TAG, "onCreate: start");
        latET = (EditText) findViewById(R.id.latET);
        lonET = (EditText) findViewById(R.id.lonET);
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
        if (Utils.checkNetworkConnection(this)) {
            Log.d(TAG, "onCreate: in if oncreate");
            startWeatherFragment();
        } else {
            showErrorDialog();
        }
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        Log.d(TAG, "showProgressDialog: ");
        progressDialog.setTitle(R.string.title_progress_dialog);
        progressDialog.show();
    }

    private void getLocationFragment(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "before location search");
            LocationServices
                    .getFusedLocationProviderClient(this)
                    .getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        Log.d(TAG, " location = " + (location == null));
                        if (location != null) {
                            Log.d(TAG, "lat = " + location.getLatitude() + " lon = " + location.getLongitude());
                            fragment = CityListFragment.newInstance(location.getLatitude(), location.getLongitude());
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.container, fragment)
                                    .commit();
                        }else {
                            setLocation();
                        }
                    });
        }else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_START_FRAGMENT);
        }
    }

    private void setLocation(){
        final View view = View.inflate(getBaseContext(), R.layout.dialog_location, null);
        final EditText latET = view.findViewById(R.id.latET);
        final EditText lonET = view.findViewById(R.id.lonET);
        new AlertDialog.Builder(this)
                .setTitle(R.string.location_erro)
                .setMessage(R.string.enter_coords)
                .setView(view)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    try {
                        double mLat = Double.parseDouble(latET.getText().toString());
                        double mLon = Double.parseDouble(lonET.getText().toString());
                        fragment = CityListFragment.newInstance(mLat, mLon);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();

                    }catch (NumberFormatException ex){
                        Toast.makeText(getBaseContext(), "NumberFormatException ", Toast.LENGTH_LONG).show();
                        setLocation();
                    }
                })
                .show();

    }

    private void startWeatherFragment() {
        showProgressDialog();
        Log.d(TAG, "startWeatherFragment: ");
        getLocationFragment();
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_network_error)
                .setMessage(R.string.error_network)
                .setPositiveButton(R.string.retry, (dialog, id) -> {
                    if (Utils.checkNetworkConnection(this)) {
                        startWeatherFragment();
                        dialog.dismiss();
                    } else {
                        showErrorDialog();
                    }
                }).show();
    }


    @Override
    public void refreshCoords() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            LocationServices
                    .getFusedLocationProviderClient(this)
                    .getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null)
                            fragment.onLocationUpdate(location);
                        else {
                            setLocation();
                        }
                    });
        }else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_REFRESH_COORDS);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: ");
        switch (requestCode){
            case REQUEST_CODE_REFRESH_COORDS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    refreshCoords();

                }else {
                    Toast.makeText(this, "Need permission", Toast.LENGTH_LONG).show();

                }
                break;
            case REQUEST_CODE_START_FRAGMENT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                   getLocationFragment();

                }else {
                    Toast.makeText(this, "Need permission", Toast.LENGTH_LONG).show();

                }
                break;
        }
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
                }else {
                    fragment.setHasOptionsMenu(true);
                    super.onBackPressed();
                }
            }else{
                fragment.setHasOptionsMenu(true);
                super.onBackPressed();
            }
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

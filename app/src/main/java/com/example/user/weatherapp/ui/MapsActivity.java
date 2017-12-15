package com.example.user.weatherapp.ui;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.user.weatherapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    public static String KEY = "9f6dc9e441d2f589ff7e472d7d352a61";
    private GoogleMap mMap;
    private double lat, lon;
    TileOverlay tileOverlay;
    String[] data = {"clouds_new", "precipitation_new", "pressure_new", "wind_new", "temp_new"};
    String choose = "temp_new";


    private TileProvider getTileProvider(){
        TileProvider tileProvider = new UrlTileProvider(256, 256) {

            @Override
            public URL getTileUrl(int x, int y, int zoom) {

                String s = String.format(Locale.US, "http://tile.openweathermap.org/map/%s/%d/%d/%d.png?appid=%s", choose, zoom, x, y, KEY);
                URL url = null;

                try {
                    url = new URL(s);
                    return url;
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
            }




        };
        return tileProvider;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Intent intent = getIntent();
        lon = intent.getDoubleExtra("lon", 0);
        lat = intent.getDoubleExtra("lat", 0);
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        LatLng sydney = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(sydney).title("lat = " + lat + " lng = " + lon));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 8));
//        LatLng indy = new LatLng(50, 36);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(indy, 8));






    }

    private void getTileOverlay(TileProvider tileProvider){
        tileOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider).fadeIn(true));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                choose = "clouds_new";
                getTileOverlay(getTileProvider());
                break;
            case 1:
                choose = "precipitation_new";
                getTileOverlay(getTileProvider());
                break;
            case 2:
                choose = "pressure_new";
                getTileOverlay(getTileProvider());
                break;
            case 3:
                choose = "wind_new";
                getTileOverlay(getTileProvider());
                break;
            case 4:
                choose = "temp_new";
                getTileOverlay(getTileProvider());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

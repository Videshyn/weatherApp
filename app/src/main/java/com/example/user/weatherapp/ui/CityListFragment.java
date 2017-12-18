package com.example.user.weatherapp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.user.weatherapp.R;
import com.example.user.weatherapp.retrofit.WeatherAPI;
import com.example.user.weatherapp.pojo.coords_pojo.MainCityModel;
import com.google.gson.Gson;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.user.weatherapp.utils.Const.DEFAULT_CNT;
import static com.example.user.weatherapp.utils.Const.KEY;
import static com.example.user.weatherapp.utils.Const.LAT;
import static com.example.user.weatherapp.utils.Const.LNG;

/**
 * Created by User on 16.10.2017
 */

public class CityListFragment extends Fragment implements WeatherAdapter.Listener, SwipeRefreshLayout.OnRefreshListener,
        SearchView.OnQueryTextListener{
    private static final String TAG = CityListFragment.class.getSimpleName();
    private double lat;
    private double lng;
    private RecyclerView recyclerView;
    private WeatherAdapter adapter;
    private Listener listener;
    private View view;
    private MainCityModel citiesModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView searchView;
    public String lastCity;
    private Toolbar toolbar;

    public static CityListFragment newInstance(double lat, double lng) {
        Log.d(TAG, "call newInstance:");
        Bundle args = new Bundle();
        args.putDouble(LAT, lat);
        args.putDouble(LNG, lng);
        CityListFragment fragment = new CityListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public int getElementsCount(){
        if (adapter != null){
            return adapter.getItemCount();
        }else return -1;
    }

    private void initCoors(){
        lat = getArguments().getDouble(LAT);
        lng = getArguments().getDouble(LNG);
    }

    public void callAPI(double latitude, double longitude){
        WeatherAPI.getClient().create(WeatherAPI.WeatherInterface.class)
                .getAll(latitude, longitude, DEFAULT_CNT, KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(example -> {
                    Log.d(TAG, "adapter = " + (adapter == null));
                    if (adapter != null){
                        adapter.updateList(example);
                        Log.d(TAG, "update ");

                    }else {
                        adapter = new WeatherAdapter(example, this);
                    }
                    recyclerView.setAdapter(adapter);
                    if (listener != null){
                        listener.closeProgressDialog();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }, error -> {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "Bad request", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(true);
                });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCoors();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.recycler, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        toolbar = view.findViewById(R.id.toolbar_recycler);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) view.findViewById(R.id.rec);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        callAPI(lat, lng);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void clickElement(MainCityModel exampleCity, int position) {
        String json = new Gson().toJson(exampleCity, MainCityModel.class);
        WeatherDescriptionFragment fragment = WeatherDescriptionFragment.newInstance(json, position, 0);
        getFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void onLocationUpdate(Location location){
        lat = location.getLatitude();
        lng = location.getLongitude();
        callAPI(lat, lng);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        listener.refreshCoords();
        Log.d(TAG, "swipe = " + (swipeRefreshLayout == null));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "call onAttach()");
        if (context instanceof Listener){
            listener = (Listener) context;
        }else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "call onDetach: ");
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_item_menu);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.setGroupVisible(R.id.group_add, false);
        menu.setGroupVisible(R.id.maps_group, true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: ");
        switch (item.getItemId()){
            case R.id.my_history:
                try{
                    Log.d(TAG, "onOptionsItemSelected: ");
                    ItemFragment itemFragment = ItemFragment.newInstance();
                    getFragmentManager().beginTransaction()
                            .add(R.id.container, itemFragment)
                            .addToBackStack(null)
                            .commit();
                }catch (Exception ex){
                    Log.d(TAG, "ex = " + ex.getMessage().toString());
                }
                return true;
            case R.id.my_map:
                Log.d(TAG, "my map");
                Intent mapIntent = new Intent(getActivity(), MapsActivity.class);
                mapIntent.putExtra("lat", lat);
                mapIntent.putExtra("lon", lng);
                startActivity(mapIntent);
                return true;
        }
        return false;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        lastCity = query;
        callCityAPI(query);
        return false;
    }

    private void callCityAPI(String query){
        WeatherAPI.getClient().create(WeatherAPI.ApiInterface.class)
                .getCityWeather(query, KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(example -> {
                    if (listener != null){
                        listener.closeProgressDialog();
                    }
                    if (adapter != null){
                        adapter.updateList(example);
                    }else {
                        adapter = new WeatherAdapter(example, this);
                    }
                    recyclerView.setAdapter(adapter);
                    setHomeButtonEnabled(true);
                }, exception ->
                    Toast.makeText(getActivity(), "Not found. Correct your response.", Toast.LENGTH_LONG).show()
                );
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void clickBackCallAPI(){
        if (searchView != null){
            searchView.clearFocus();
            searchView.onActionViewCollapsed();
            searchView.setQuery("", false);
            Log.d(TAG, "clickBackCallAPI: " + (searchView == null));
        }
        setHomeButtonEnabled(false);
        listener.openProgressDialog();
        callAPI(lat, lng);
    }

    public void clickBackCallCityAPI(){
        setHomeButtonEnabled(false);
        listener.openProgressDialog();
        callCityAPI(lastCity);
    }

    private void setHomeButtonEnabled(boolean flag){
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(flag);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(flag);
    }

    @Override
    public void onResume() {
        super.onResume();
        setHasOptionsMenu(true);
    }

    public interface Listener {
        void closeProgressDialog();
        void refreshCoords();
        void openProgressDialog();
    }
}

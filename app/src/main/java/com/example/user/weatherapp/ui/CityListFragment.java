package com.example.user.weatherapp.ui;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.user.weatherapp.R;
import com.example.user.weatherapp.pojo.city_pojo.ExampleCity;
import com.example.user.weatherapp.retrofit.WeatherAPI;
import com.example.user.weatherapp.pojo.coords_pojo.Example;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.user.weatherapp.utils.Const.DEFAULT_CNT;
import static com.example.user.weatherapp.utils.Const.KEY;
import static com.example.user.weatherapp.utils.Const.LAT;
import static com.example.user.weatherapp.utils.Const.LNG;
import static com.example.user.weatherapp.utils.Const.MANY_ELEMENTS;
import static com.example.user.weatherapp.utils.Const.ONE_ELEMENT;

/**
 * Created by User on 16.10.2017
 */

public class CityListFragment extends Fragment implements WeatherAdapter.Listener, SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener{
    private static final String TAG = CityListFragment.class.getSimpleName();
    private double lat;
    private double lng;
    private RecyclerView recyclerView;
    private WeatherAdapter adapter;
    private Listener listener;
    private View view;
    private Example citiesModel;
    private List<com.example.user.weatherapp.pojo.coords_pojo.List> citiesModelList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView searchView;
    private boolean toolBarFlag = false;
    public String lastCity;

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

    private void callAPI(double latitude, double longitude){
        Log.d(TAG, "callAPI: citiesModelList size = " + citiesModelList.size());
        WeatherAPI.getClient().create(WeatherAPI.WeatherInterface.class)
                .getAll(latitude, longitude, DEFAULT_CNT, KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(example -> {
                    Log.d(TAG, "callAPI: subscribe");
                    this.citiesModel = example;
//                    loadNextPack(0, 50);
                    citiesModelList = example.getList();
                    if (adapter != null){

                    }
                    adapter = new WeatherAdapter(citiesModelList, this);
                    recyclerView.setAdapter(adapter);
                    if (listener != null){
                        listener.closeProgressDialog();
                    }
                    if (swipeRefreshLayout.isRefreshing()){
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, error -> {
                    Log.d(TAG, "callAPI: on error");
                    Toast.makeText(getContext(), "Correct your request!", Toast.LENGTH_LONG).show();
                    listener.refreshCoords();
                });
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "call onCreate()");
        initCoors();
        callAPI(lat, lng);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "call onCreateView()");
        view =  inflater.inflate(R.layout.recycler, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);

        Toolbar toolbar = view.findViewById(R.id.toolbar_recycler);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) view.findViewById(R.id.rec);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ViewGroup parentViewGroup = (ViewGroup) view.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }
    }

    @Override
    public void clickElement(List<com.example.user.weatherapp.pojo.coords_pojo.List> lists, int position) {
        Log.d(TAG, "call clickElement: ");
        Example ex = new Example();
        ex.setList(lists);
        String json = new Gson().toJson(ex, Example.class);
        WeatherDescriptionFragment fragment = WeatherDescriptionFragment.newInstance(json, position, MANY_ELEMENTS);
        getFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
        setHasOptionsMenu(false);
    }

    @Override
    public void clickElement(ExampleCity exampleCity) {

        String json = new Gson().toJson(exampleCity, ExampleCity.class);
        WeatherDescriptionFragment fragment = WeatherDescriptionFragment.newInstance(json, 0, ONE_ELEMENT);
        getFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
        setHasOptionsMenu(false);
    }

//    @Override
//    public void loadNextPack(int down_limit, int up_limit) {
//        Log.d(TAG, "call loadNextPack: ");
//        for (int i = down_limit; i < up_limit; i ++){
//            citiesModelList.add(i, citiesModel.getList().get(i));
//        }
//        if (down_limit != 0)
//            adapter.notifyDataSetChanged();
//    }

    public void onLocationUpdate(Location location){
        lat = location.getLatitude();
        lng = location.getLongitude();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        listener.refreshCoords();
        callAPI(lat, lng);

        Log.d(TAG, "after setRefreshing false");
    }

    public interface Listener {
        void closeProgressDialog();
        void refreshCoords();
        void openProgressDialog();
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
        MenuItem addItem = menu.findItem(R.id.add_to_history);
        addItem.setVisible(true);
        MenuItem history = menu.findItem(R.id.my_history);
        history.setVisible(true);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        if (toolBarFlag){
            menu.clear();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                callAPI(lat, lng);
                return true;
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
        }
        return super.onOptionsItemSelected(item);
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
                            Log.d(TAG, "onQueryTextSubmit: " + example.getName());
                            adapter = new WeatherAdapter(example, this);
                            recyclerView.setAdapter(adapter);
                            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
                            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        }, exception ->
                                Toast.makeText(getActivity(), "Not found. Correct your response.", Toast.LENGTH_LONG).show()
                );
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void clickBackCallAPI(){
        toolBarFlag = true;
        if (searchView != null){
            searchView.clearFocus();
            searchView.onActionViewCollapsed();
            searchView.setQuery("", false);
            Log.d(TAG, "clickBackCallAPI: " + (searchView == null));
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        Log.d(TAG, "in clickback list size = " + citiesModelList.size());
        listener.openProgressDialog();
        callAPI(lat, lng);
    }
    public void clickBackCallCityAPI(){
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        listener.openProgressDialog();
        callCityAPI(lastCity);
    }

}

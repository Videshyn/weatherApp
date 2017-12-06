package com.example.user.weatherapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.weatherapp.R;
import com.example.user.weatherapp.pojo.coords_pojo.MainCityModel;
import com.example.user.weatherapp.pojo.coords_pojo.WrapperMainCityModel;
import com.google.gson.Gson;

import java.util.List;

import static com.example.user.weatherapp.utils.Const.DB_CHECK;

public class ItemFragment extends Fragment implements DBAdapter.Listener{

    private static final String TAG = ItemFragment.class.getSimpleName();

    public static ItemFragment newInstance() {
        return new ItemFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_item_list, null, false);
        RecyclerView recyclerView = view.findViewById(R.id.fragment_list_history);
        DBAdapter adapter = new DBAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        Toolbar toolbar = view.findViewById(R.id.list_item_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void clickElement(List<MainCityModel> historyModel, int currentPosition) {
        WrapperMainCityModel cityModel = new WrapperMainCityModel();
        cityModel.setModelList(historyModel);
        WeatherDescriptionFragment fragment = WeatherDescriptionFragment.newInstance(new Gson().toJson(cityModel, WrapperMainCityModel.class), DB_CHECK, currentPosition);
        getFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
        setHasOptionsMenu(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        menu.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (getFragmentManager().getBackStackEntryCount() == 0){
                    getActivity().onBackPressed();
                }else {
                    getFragmentManager().popBackStack();
                }
                break;
        }
        return true;
    }
}

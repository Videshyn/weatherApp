package com.example.user.weatherapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
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
    private DBAdapter adapter;
    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback = new ActionModeCallback();

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
        adapter = new DBAdapter(getContext(), this);
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
        WeatherDescriptionFragment fragment = WeatherDescriptionFragment.newInstance(new Gson()
                .toJson(cityModel, WrapperMainCityModel.class), DB_CHECK, currentPosition);
        getFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onItemClicked(int position) {
        if (actionMode != null){
            toggleSelection(position);
        }
    }

    private void toggleSelection(int position){
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();
        if (count == 0){
            actionMode.finish();
        }else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    @Override
    public boolean onItemLongClicked(int position) {
        if (actionMode == null){
            actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
        return false;
    }

    @Override
    public boolean statusActionMode() {
        return actionMode == null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.setGroupVisible(R.id.group_add, false);
        menu.setGroupVisible(R.id.group_history, false);
        menu.setGroupVisible(R.id.group_search, false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Log.d(TAG, "itemFragment stack = " + getFragmentManager().getBackStackEntryCount());
                if (getFragmentManager().getBackStackEntryCount() == 0){
                    getActivity().onBackPressed();
                }else {
                    getFragmentManager().popBackStack();
                }
                return true;
        }
        return false;
    }

    private class ActionModeCallback implements ActionMode.Callback{

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.action_mode_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()){
                    case R.id.delete_am:
                            adapter.deleteElements(actionMode);
                            adapter.clearSelection();
                            actionMode = null;
                            adapter.notifyDataSetChanged();
                        return true;
//                    case R.id.clear_selection_am:
//                        adapter.clearSelection();
//                        return true;
                }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelection();
            actionMode = null;
        }
    }
}

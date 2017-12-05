package com.example.user.weatherapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.weatherapp.R;

public class ItemFragment extends Fragment {

    private static final String TAG = ItemFragment.class.getSimpleName();

    public static ItemFragment newInstance() {
        return new ItemFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            RecyclerView view = (RecyclerView) inflater.inflate(R.layout.fragment_item_list, null, false);
            Log.d(TAG, "view == null = " + (view == null));

            MyItemRecyclerViewAdapter adapter = new MyItemRecyclerViewAdapter(getContext());
            view.setAdapter(adapter);
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            view.setLayoutManager(manager);




        return view;
    }





}

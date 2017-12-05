package com.example.user.weatherapp.ui;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.weatherapp.R;
import com.example.user.weatherapp.pojo.HistoryModel;
import com.example.user.weatherapp.utils.DBHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.weatherapp.utils.Const.DB_NAME;
import static com.example.user.weatherapp.utils.Const.DB_VERSION;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private List<HistoryModel> list = new ArrayList<>();
    private DBHelper dbHelper;

    public MyItemRecyclerViewAdapter(Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
        list = dbHelper.readHistory();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CardView mView = holder.cardView;

        final ImageView img = mView.findViewById(R.id.img_card);
        final TextView cityName = mView.findViewById(R.id.city_name_card);
        final TextView temp = mView.findViewById(R.id.temperature_card);
        final TextView data = mView.findViewById(R.id.date_card);

        Glide.with(img.getContext()).load(list.get(position).getIconUrlModel()).into(img);
        cityName.setText(list.get(position).getCityNameModel() + "");
        temp.setText(list.get(position).getTmpModel() + "°C");
        data.setText(list.get(position).getDataModel());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;

        public ViewHolder(CardView view) {
            super(view);
            cardView = view;
        }

    }
}

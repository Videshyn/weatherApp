package com.example.user.weatherapp.ui;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.weatherapp.R;
import com.example.user.weatherapp.pojo.coords_pojo.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.example.user.weatherapp.utils.Const.ICON_URL;
import static com.example.user.weatherapp.utils.Const.PNG;

/**
 * Created by User on 16.10.2017
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private static final String TAG = WeatherAdapter.class.getSimpleName();

    private Listener weatherAdapterListener;
    private Example exampleCity;
    private List<com.example.user.weatherapp.pojo.coords_pojo.List> citiesList = new ArrayList<>();

    public WeatherAdapter(Example exampleCity, Listener weatherAdapterListener) {
        this.exampleCity = exampleCity;
        this.weatherAdapterListener = weatherAdapterListener;
        citiesList = exampleCity.getList();
    }

    public void updateList(Example exampleCity){
        this.exampleCity = exampleCity;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new WeatherAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        final TextView cityName = (TextView) cardView.findViewById(R.id.city_name_card);
        final TextView temperature = (TextView) cardView.findViewById(R.id.temperature_card);
        final ImageView img = (ImageView) cardView.findViewById(R.id.img_card);

        if (exampleCity.getCount() == null){
            Log.d(TAG, "getCount == null");
            cityName.setText(exampleCity.getName());
            double tmpCity = new BigDecimal(exampleCity.getMain().getTemp() - 273.15).setScale(1, BigDecimal.ROUND_UP).doubleValue();
            temperature.setText(tmpCity + "");
            Glide.with(img.getContext()).load(ICON_URL + exampleCity.getWeather().get(0).getIcon() + PNG).into(img);
            cardView.setOnClickListener(v -> weatherAdapterListener.clickElement(exampleCity, 0));
        }else if (exampleCity.getCount() > 0){
            Log.d(TAG, "getCount != null");
            cityName.setText(citiesList.get(position).getName());
            double temp = new BigDecimal(citiesList.get(position).getMain().getTemp() - 273.15).setScale(1, BigDecimal.ROUND_UP).doubleValue();
            temperature.setText(temp + "°C");
            Glide.with(img.getContext()).load(ICON_URL + citiesList.get(position).getWeather().get(0).getIcon() + PNG).into(img);
            cardView.setOnClickListener(v -> weatherAdapterListener.clickElement(exampleCity, position));
        }
    }

    @Override
    public int getItemCount() {
        if (exampleCity.getCount() == null){
            Log.d(TAG, "getItemCount: " + exampleCity.getId());
            return 1;
        }else
            return citiesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        public ViewHolder(CardView itemView) {
            super(itemView);
            cardView = itemView;
        }
    }

    public interface Listener {
//        void clickElement(List<com.example.user.weatherapp.pojo.coords_pojo.List> lists, int position);
        void clickElement(Example exampleCity, int position);
//        void loadNextPack(int down_limit, int up_limit);
    }
}

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
import com.example.user.weatherapp.pojo.city_pojo.ExampleCity;

import java.math.BigDecimal;
import java.util.List;

import static com.example.user.weatherapp.utils.Const.ICON_URL;
import static com.example.user.weatherapp.utils.Const.PNG;

/**
 * Created by User on 16.10.2017
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private static final String TAG = WeatherAdapter.class.getSimpleName();
    public List<com.example.user.weatherapp.pojo.coords_pojo.List> lists;
    WeatherAdapterListener weatherAdapterListener;
    ExampleCity exampleCity;

    public WeatherAdapter(List<com.example.user.weatherapp.pojo.coords_pojo.List> lists, WeatherAdapterListener weatherAdapterListener) {
        this.lists = lists;
        this.weatherAdapterListener = weatherAdapterListener;
    }

    public WeatherAdapter(ExampleCity exampleCity, WeatherAdapterListener weatherAdapterListener) {
        this.exampleCity = exampleCity;
        this.weatherAdapterListener = weatherAdapterListener;
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

        if (exampleCity != null){
            cityName.setText(exampleCity.getName());
            double tmpCity = new BigDecimal(exampleCity.getMain().getTemp() - 273.15).setScale(1, BigDecimal.ROUND_UP).doubleValue();
            temperature.setText(tmpCity + "");
            Glide.with(img.getContext()).load(ICON_URL + exampleCity.getWeather().get(0).getIcon() + PNG).into(img);
            cardView.setOnClickListener(v -> weatherAdapterListener.clickElement(exampleCity));
        }else {
            cityName.setText(lists.get(position).getName());
            double temp = new BigDecimal(lists.get(position).getMain().getTemp() - 273.15).setScale(1, BigDecimal.ROUND_UP).doubleValue();
            temperature.setText(temp + "°C");
            Glide.with(img.getContext()).load(ICON_URL + lists.get(position).getWeather().get(0).getIcon() + PNG).into(img);
            cardView.setOnClickListener(v -> weatherAdapterListener.clickElement(lists, position));

            Log.d(TAG, "position = " + position + " cnt = " + lists.size());
//            if ((position + 1) >= getItemCount() && lists.size() < 50){
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                weatherAdapterListener.loadNextPack(position, (lists.size() + 9));
//
//            }
            Log.d(TAG, "list size = " + lists.size());
        }
    }

    @Override
    public int getItemCount() {
        if (exampleCity != null){
            return 1;
        }else
            return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        public ViewHolder(CardView itemView) {
            super(itemView);
            cardView = itemView;
        }
    }

    public interface WeatherAdapterListener{
        void clickElement(List<com.example.user.weatherapp.pojo.coords_pojo.List> lists, int position);
        void clickElement(ExampleCity exampleCity);
//        void loadNextPack(int down_limit, int up_limit);
    }
}

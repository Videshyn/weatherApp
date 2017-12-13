package com.example.user.weatherapp.ui;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.weatherapp.R;
import com.example.user.weatherapp.pojo.coords_pojo.MainCityModel;
import com.example.user.weatherapp.pojo.coords_pojo.MainModelList;

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
    private MainCityModel exampleCity;
    private List<MainModelList> citiesList = new ArrayList<>();

    public WeatherAdapter(MainCityModel exampleCity, Listener weatherAdapterListener) {
        this.exampleCity = exampleCity;
        this.weatherAdapterListener = weatherAdapterListener;
        citiesList = exampleCity.getList();
    }

    public void updateList(MainCityModel exampleCity){
        this.exampleCity = exampleCity;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new WeatherAdapter.ViewHolder(cardView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView cityName, temperature;
        ImageView img;

        public ViewHolder(CardView itemView) {
            super(itemView);
            cityName = (TextView) itemView.findViewById(R.id.city_name_card);
            temperature = (TextView) itemView.findViewById(R.id.temperature_card);
            img = (ImageView) itemView.findViewById(R.id.img_card);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (exampleCity.getCount() == null){
                weatherAdapterListener.clickElement(exampleCity, 0);
            }else if (exampleCity.getCount() > 0){
                weatherAdapterListener.clickElement(exampleCity, getAdapterPosition());
            }
        }
    }

    private void bindNullCount(ViewHolder holder){
        Glide.with(holder.img.getContext()).load(ICON_URL + exampleCity.getWeather().get(0).getIcon() + PNG).into(holder.img);
        holder.cityName.setText(exampleCity.getName());
        holder.temperature.setText("" + new BigDecimal(exampleCity.getMain().getTemp() - 273.15).setScale(1, BigDecimal.ROUND_UP));
    }

    private void bildNotNullCount(ViewHolder holder, final int position){
        Glide.with(holder.img.getContext()).load(ICON_URL + citiesList.get(position).getWeather().get(0).getIcon() + PNG).into(holder.img);
        holder.cityName.setText(citiesList.get(position).getName());
        holder.temperature.setText("" + new BigDecimal(citiesList.get(position).getMain().getTemp() - 273.15).setScale(1, BigDecimal.ROUND_UP));

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (exampleCity.getCount() == null){
            bindNullCount(holder);
        }else if (exampleCity.getCount() > 0){
            bildNotNullCount(holder, position);
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
    public interface Listener {
        void clickElement(MainCityModel exampleCity, int position);

    }

}

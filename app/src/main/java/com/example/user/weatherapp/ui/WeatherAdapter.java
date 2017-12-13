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
    private CLickListener cLickListener;
    private MainCityModel exampleCity;
    private List<MainModelList> citiesList = new ArrayList<>();
    private ArrayList<Integer> countNullPositions = new ArrayList<>();
    private ArrayList<Integer> countNotNullPosition = new ArrayList<>();


    public ArrayList<Integer> getCountNullPositions() {
        return countNullPositions;
    }

    public void setCountNullPositions(ArrayList<Integer> countNullPositions) {
        this.countNullPositions = countNullPositions;
    }

    public ArrayList<Integer> getCountNotNullPosition() {
        return countNotNullPosition;
    }

    public void setCountNotNullPosition(ArrayList<Integer> countNotNullPosition) {
        this.countNotNullPosition = countNotNullPosition;
    }

    public WeatherAdapter(MainCityModel exampleCity, Listener weatherAdapterListener, CLickListener cLickListener) {
        this.exampleCity = exampleCity;
        this.weatherAdapterListener = weatherAdapterListener;
        this.cLickListener = cLickListener;
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

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        final TextView cityName = (TextView) cardView.findViewById(R.id.city_name_card);
        final TextView temperature = (TextView) cardView.findViewById(R.id.temperature_card);
        final ImageView img = (ImageView) cardView.findViewById(R.id.img_card);

        if (exampleCity.getCount() == null){
            Glide.with(img.getContext()).load(ICON_URL + exampleCity.getWeather().get(0).getIcon() + PNG).into(img);
            cardView.setBackgroundColor(img.getContext().getResources().getColor(R.color.white));
            for(Integer element : countNullPositions){
                if (element == holder.getAdapterPosition()){
                    img.setImageResource(R.drawable.star);
                    cardView.setBackgroundColor(img.getContext().getResources().getColor(R.color.light_blue));
                    break;
                }
                Glide.with(img.getContext()).load(ICON_URL + exampleCity.getWeather().get(0).getIcon() + PNG).into(img);
                cardView.setBackgroundColor(img.getContext().getResources().getColor(R.color.white));
            }
            cityName.setText(exampleCity.getName());
            double tmpCity = new BigDecimal(exampleCity.getMain().getTemp() - 273.15).setScale(1, BigDecimal.ROUND_UP).doubleValue();
            temperature.setText(tmpCity + "");
            cardView.setOnClickListener(v -> weatherAdapterListener.clickElement(exampleCity, 0));
        }else if (exampleCity.getCount() > 0){
            Glide.with(img.getContext()).load(ICON_URL + citiesList.get(position).getWeather().get(0).getIcon() + PNG).into(img);
            cardView.setBackgroundColor(img.getContext().getResources().getColor(R.color.white));
            if (countNotNullPosition.size() != 0){
                for (Integer element : countNotNullPosition){
                    if (element == holder.getAdapterPosition()){
                        img.setImageResource(R.drawable.star);
                        cardView.setBackgroundColor(img.getContext().getResources().getColor(R.color.light_blue));
                        break;
                    }
                    cardView.setBackgroundColor(img.getContext().getResources().getColor(R.color.white));
                    Glide.with(img.getContext()).load(ICON_URL + citiesList.get(position).getWeather().get(0).getIcon() + PNG).into(img);
                }
            }

            cityName.setText(citiesList.get(position).getName());
            double temp = new BigDecimal(citiesList.get(position).getMain().getTemp() - 273.15).setScale(1, BigDecimal.ROUND_UP).doubleValue();
            temperature.setText(temp + "°C");
            cardView.setOnClickListener(v -> weatherAdapterListener.clickElement(exampleCity, position));
        }

        cardView.setOnLongClickListener(v -> {
            if (exampleCity.getCount() == null){
                countNullPositions.add(holder.getAdapterPosition());
                weatherAdapterListener.createActionMode(countNullPositions);
            }else {
                countNotNullPosition.add(holder.getAdapterPosition());
                weatherAdapterListener.createActionMode(countNotNullPosition);
            }
            img.setImageResource(R.drawable.star);
            cardView.setBackgroundColor(img.getContext().getResources().getColor(R.color.light_blue));

            return true;
        });

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
        boolean createActionMode(ArrayList<Integer> list);
    }

    public interface CLickListener{
        void omItemClicked(int position);
        boolean onItemLongClicked(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        public ViewHolder(CardView itemView) {
            super(itemView);
            cardView = itemView;
        }

    }

}

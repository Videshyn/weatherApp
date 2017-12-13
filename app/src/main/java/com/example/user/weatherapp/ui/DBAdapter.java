package com.example.user.weatherapp.ui;

import android.content.Context;
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
import com.example.user.weatherapp.utils.DBHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.weatherapp.utils.Const.DB_NAME;
import static com.example.user.weatherapp.utils.Const.DB_VERSION;

public class DBAdapter extends RecyclerView.Adapter<DBAdapter.ViewHolder> {

    private static final String TAG = DBAdapter.class.getSimpleName();
    private List<MainCityModel> list = new ArrayList<>();
    private DBHelper dbHelper;
    private Listener dbAdapterListener;

    public DBAdapter(Context context, Listener dbAdapterListener) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
        this.dbAdapterListener = dbAdapterListener;
        list = dbHelper.readHistory();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.cityName.setText(list.get(position).getName() + "");
        holder.temp.setText(list.get(position).getTmpDb() + "°C");
        holder.data.setText(list.get(position).getDateDb());
        Glide.with(holder.img.getContext()).load(list.get(position).getIconURLDB()).into(holder.img);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView cityName, temp, data;
        ImageView img;

        public ViewHolder(CardView view) {
            super(view);
            cityName = (TextView) view.findViewById(R.id.city_name_card);
            temp = (TextView) view.findViewById(R.id.temperature_card);
            data = (TextView) view.findViewById(R.id.date_card);
            img = (ImageView) view.findViewById(R.id.img_card);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (dbAdapterListener != null){
                dbAdapterListener.clickElement(list, getAdapterPosition());
            }
        }
    }

    public interface Listener{
        void clickElement(List<MainCityModel> lists, int currentPosition);
    }
}

package com.example.user.weatherapp.ui;

import android.content.Context;
import android.support.v7.view.ActionMode;
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
import com.example.user.weatherapp.ui.base.SelectableAdapter;
import com.example.user.weatherapp.utils.DBHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.weatherapp.utils.Const.DB_NAME;
import static com.example.user.weatherapp.utils.Const.DB_VERSION;

public class DBAdapter extends SelectableAdapter<DBAdapter.ViewHolder>{

    private static final String TAG = DBAdapter.class.getSimpleName();
    private List<MainCityModel> dbEntitiesList = new ArrayList<>();
    private ArrayList<Integer> idList = new ArrayList<>();
    private DBHelper dbHelper;
    private Listener dbAdapterListener;
    private Context context;

    public DBAdapter(Context context, Listener dbAdapterListener) {
        this.context = context;
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
        this.dbAdapterListener = dbAdapterListener;
        dbEntitiesList = dbHelper.readHistory();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.cityName.setText(dbEntitiesList.get(position).getName() + "");
        holder.temperature.setText(dbEntitiesList.get(position).getTmpDb() + "°C");
        holder.data.setText(dbEntitiesList.get(position).getDateDb());
        idList.add(dbEntitiesList.get(position).getIdDB());
        Glide.with(holder.img.getContext()).load(dbEntitiesList.get(position).getIconURLDB()).into(holder.img);
        if (isSelected(holder.getPosition())){
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.light_blue));
        }else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return dbEntitiesList.size();
    }

    public void deleteElements(ActionMode actionMode){
        List<Integer> positionsList = new ArrayList<>(selectedItem.size());
        for (int i = 0, n = selectedItem.size(); i < n; i ++){
            positionsList.add(selectedItem.keyAt(i));
        }
        for (int i = 0, n = dbEntitiesList.size(); i < n; i ++){
            idList.add(dbEntitiesList.get(i).getIdDB());
        }
        ArrayList<MainCityModel> removePattern = new ArrayList<>();
        for (int i = 0, n = positionsList.size(); i < n; i ++){
            removePattern.add(dbEntitiesList.get(positionsList.get(i)));
        }
        Log.d(TAG, "size models = " + removePattern.size());
        dbHelper.deleteHistory(positionsList, idList);
        if (actionMode != null){
            actionMode.finish();
            dbEntitiesList.removeAll(removePattern);
            notifyDataSetChanged();
        }
    }

    public interface Listener{
        void clickElement(List<MainCityModel> lists, int currentPosition);
        void onItemClicked(int position);
        boolean onItemLongClicked(int position);
        boolean statusActionMode();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        TextView cityName, temperature, data;
        ImageView img;

        public ViewHolder(CardView view) {
            super(view);
            cityName = (TextView) view.findViewById(R.id.city_name_card);
            temperature = (TextView) view.findViewById(R.id.temperature_card);
            data = (TextView) view.findViewById(R.id.date_card);
            img = (ImageView) view.findViewById(R.id.img_card);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (dbAdapterListener != null){
                if (dbAdapterListener.statusActionMode()){
                    dbAdapterListener.clickElement(dbEntitiesList, getPosition());
                }else {
                    dbAdapterListener.onItemClicked(getPosition());
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (dbAdapterListener != null){
                return dbAdapterListener.onItemLongClicked(getPosition());
            }
            return false;
        }
    }

}

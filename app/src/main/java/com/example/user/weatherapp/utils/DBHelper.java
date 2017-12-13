package com.example.user.weatherapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.user.weatherapp.pojo.coords_pojo.MainCityModel;
import com.example.user.weatherapp.pojo.pojo_robot.OpenWeatherMapJSON;
import com.example.user.weatherapp.pojo.pojo_robot.WeatherItemList;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.example.user.weatherapp.utils.Const.ICON_URL;
import static com.example.user.weatherapp.utils.Const.PNG;

/**
 * Created by User on 04.12.2017
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getSimpleName();
    private final String CREATE_TABLE = "create table WEATHER (id integer primary key autoincrement, " +
            "city_name text, data text, icon_url text, tmp text, wind_speed text, pressure text, humidity text, description text);";
    private final String DROP_TABLE = "DROP TABLE IF EXISTS WEATHER";
    private static final String TABLE_NAME = "WEATHER";
    private Context context;
    private DBListener dbListener;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DBListener dbListener) {
        super(context, name, factory, version);
        this.context = context;
        this.dbListener = dbListener;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public void saveHistory(String response, int pagerPosition, String cityName){

        SQLiteDatabase database = getWritableDatabase();
        OpenWeatherMapJSON weatherMapJSON = new Gson().fromJson(response, OpenWeatherMapJSON.class);

        ContentValues cv = new ContentValues();


        if (weatherMapJSON != null){
            WeatherItemList weatherItemList = weatherMapJSON.getList().get(pagerPosition * 8);
            Log.d(TAG, "saveHistory: " + (weatherMapJSON.getCity() == null));
            cv.put("city_name", cityName);
            cv.put("data", weatherItemList.getDtTxt().split(" ")[0]);
            cv.put("icon_url", ICON_URL
                    + weatherItemList.getWeather().get(0).getIcon()
                    + PNG);
            cv.put("tmp", new BigDecimal(weatherItemList.getMain().getTemp() - 273.15)
                    .setScale(2, BigDecimal.ROUND_UP).doubleValue() + "");
            if (weatherItemList.getWind() != null){
                cv.put("wind_speed", weatherItemList.getWind().getSpeed());
            }else
                cv.put("wind_speed", "null");
            cv.put("pressure", weatherItemList.getMain().getPressure());
            cv.put("humidity", weatherItemList.getMain().getHumidity());
            cv.put("description", weatherItemList.getWeather().get(0).getDescription());

            Long rowId = database.insert(TABLE_NAME, null, cv);
            Log.d(TAG, "rowId = " + rowId);
        }else {
            Log.d(TAG, "weatherMapJson == null");
        }
        Toast.makeText(context, "Added!", Toast.LENGTH_LONG).show();
        close();
        database.close();
    }

    public void deleteHistory(List<Integer> positionsList, List<Integer> idList){
        SQLiteDatabase database = getWritableDatabase();
        for (int i = 0, n = positionsList.size(); i < n; i ++){
            int idRow = database.delete(TABLE_NAME, "id = " + idList.get(positionsList.get(i)), null);
            Log.d(TAG, "del = " + idRow);
            dbListener.updateRecyclerView(idList.get(positionsList.get(i)));
        }

        database.close();

    }

    public List<MainCityModel> readHistory(){
        List<MainCityModel> modelList = new ArrayList<>();
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.query("WEATHER", null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            int idColumnIndex = cursor.getColumnIndex("id");
            int cityNameColumnIndex = cursor.getColumnIndex("city_name");
            int dataColumnIndex = cursor.getColumnIndex("data");
            int iconUrlColumnIndex = cursor.getColumnIndex("icon_url");
            int tmpColumnNumber = cursor.getColumnIndex("tmp");
            int windColumnIndex = cursor.getColumnIndex("wind_speed");
            int pressureColumnIndex = cursor.getColumnIndex("pressure");
            int humidityColumnIndex = cursor.getColumnIndex("humidity");
            int descriptionColumnIndex = cursor.getColumnIndex("description");
            do {
                modelList.add(new MainCityModel(
                        cursor.getString(tmpColumnNumber),
                        cursor.getString(pressureColumnIndex),
                        cursor.getString(humidityColumnIndex),
                        cursor.getString(windColumnIndex),
                        cursor.getInt(idColumnIndex),
                        cursor.getString(dataColumnIndex),
                        cursor.getString(descriptionColumnIndex),
                        cursor.getString(cityNameColumnIndex),
                        cursor.getString(iconUrlColumnIndex)
                ));
            }while (cursor.moveToNext());
        }else {
            Toast.makeText(context, "MainModelList is empty", Toast.LENGTH_LONG).show();
        }
        return modelList;
    }

    public interface DBListener{
        void updateRecyclerView(int position);
    }

}

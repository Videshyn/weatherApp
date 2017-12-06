
package com.example.user.weatherapp.pojo.coords_pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainCityModel {

    public MainCityModel(String tmpDb, String pressureDb, String humidityDb, String windModel, int idDB, String dateDb, String descriptionDb, String name, String iconURLDB) {
        this.tmpDb = tmpDb;
        this.pressureDb = pressureDb;
        this.humidityDb = humidityDb;
        this.windModel = windModel;
        this.idDB = idDB;
        this.dateDb = dateDb;
        this.descriptionDb = descriptionDb;
        this.name = name;
        this.iconURLDB = iconURLDB;
    }

    @SerializedName("iconURLDB")
    @Expose
    private String iconURLDB;

    @SerializedName("tmpDb")
    @Expose
    private String tmpDb;

    @SerializedName("pressureDb")
    @Expose
    private String pressureDb;

    @SerializedName("humidityDb")
    @Expose
    private String humidityDb;

    @SerializedName("windModel")
    @Expose
    private String windModel;

    @SerializedName("idDB")
    @Expose
    private int idDB;

    @SerializedName("dateStr")
    @Expose
    private String dateDb;

    @SerializedName("description")
    @Expose
    private String descriptionDb;

    @SerializedName("coord")
    @Expose
    private Coord coord;

    @SerializedName("weather")
    @Expose
    private java.util.List<Weather> weather = null;
    @SerializedName("base")
    @Expose
    private String base;

    @SerializedName("main")
    @Expose
    private Main main;
    @SerializedName("visibility")
    @Expose
    private Double visibility;
    @SerializedName("wind")
    @Expose
    private Wind wind;
    @SerializedName("clouds")
    @Expose
    private Clouds clouds;
    @SerializedName("dt")
    @Expose
    private Double dt;
    @SerializedName("sys")
    @Expose
    private Sys sys;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("cod")
    @Expose
    private String cod;
    @SerializedName("count")
    @Expose
    private Double count;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("list")
    @Expose
    private java.util.List<MainModelList> list = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public java.util.List<MainModelList> getList() {
        return list;
    }

    public void setList(java.util.List<MainModelList> list) {
        this.list = list;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public java.util.List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(java.util.List<Weather> weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Double getVisibility() {
        return visibility;
    }

    public void setVisibility(Double visibility) {
        this.visibility = visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Double getDt() {
        return dt;
    }

    public void setDt(Double dt) {
        this.dt = dt;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWindModel() {
        return windModel;
    }

    public void setWindModel(String windModel) {
        this.windModel = windModel;
    }

    public int getIdDB() {
        return idDB;
    }

    public void setIdDB(int idDB) {
        this.idDB = idDB;
    }


    public String getTmpDb() {
        return tmpDb;
    }

    public void setTmpDb(String tmpDb) {
        this.tmpDb = tmpDb;
    }

    public String getPressureDb() {
        return pressureDb;
    }

    public void setPressureDb(String pressureDb) {
        this.pressureDb = pressureDb;
    }

    public String getHumidityDb() {
        return humidityDb;
    }

    public void setHumidityDb(String humidityDb) {
        this.humidityDb = humidityDb;
    }

    public String getDateDb() {
        return dateDb;
    }

    public void setDateDb(String dateDb) {
        this.dateDb = dateDb;
    }

    public String getDescriptionDb() {
        return descriptionDb;
    }

    public void setDescriptionDb(String descriptionDb) {
        this.descriptionDb = descriptionDb;
    }

    public String getIconURLDB() {
        return iconURLDB;
    }

    public void setIconURLDB(String iconURLDB) {
        this.iconURLDB = iconURLDB;
    }
}

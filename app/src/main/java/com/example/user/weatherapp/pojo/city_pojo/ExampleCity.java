
package com.example.user.weatherapp.pojo.city_pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExampleCity {

    @SerializedName("coord")
    @Expose
    private CoordCity coord;
    @SerializedName("weather")
    @Expose
    private List<WeatherCity> weather = null;
    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("main")
    @Expose
    private MainCity main;
    @SerializedName("visibility")
    @Expose
    private Double visibility;
    @SerializedName("wind")
    @Expose
    private WindCity wind;
    @SerializedName("clouds")
    @Expose
    private CloudsCity clouds;
    @SerializedName("dt")
    @Expose
    private Double dt;
    @SerializedName("sys")
    @Expose
    private SysCity sys;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cod")
    @Expose
    private Integer cod;

    public CoordCity getCoord() {
        return coord;
    }

    public void setCoord(CoordCity coord) {
        this.coord = coord;
    }

    public List<WeatherCity> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherCity> weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public MainCity getMain() {
        return main;
    }

    public void setMain(MainCity main) {
        this.main = main;
    }

    public Double getVisibility() {
        return visibility;
    }

    public void setVisibility(Double visibility) {
        this.visibility = visibility;
    }

    public WindCity getWind() {
        return wind;
    }

    public void setWind(WindCity wind) {
        this.wind = wind;
    }

    public CloudsCity getClouds() {
        return clouds;
    }

    public void setClouds(CloudsCity clouds) {
        this.clouds = clouds;
    }

    public Double getDt() {
        return dt;
    }

    public void setDt(Double dt) {
        this.dt = dt;
    }

    public SysCity getSys() {
        return sys;
    }

    public void setSys(SysCity sys) {
        this.sys = sys;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

}

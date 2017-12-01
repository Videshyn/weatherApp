
package com.example.user.weatherapp.pojo.city_pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WindCity {

    @SerializedName("speed")
    @Expose
    private Double speed;
    @SerializedName("deg")
    @Expose
    private Integer deg;

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getDeg() {
        return deg;
    }

    public void setDeg(Integer deg) {
        this.deg = deg;
    }

}

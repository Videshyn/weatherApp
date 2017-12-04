
package com.example.user.weatherapp.pojo.city_pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CloudsCity {

    @SerializedName("all")
    @Expose
    private Double all;

    public Double getAll() {
        return all;
    }

    public void setAll(Double all) {
        this.all = all;
    }

}

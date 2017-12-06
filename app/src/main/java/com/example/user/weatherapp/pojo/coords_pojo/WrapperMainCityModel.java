package com.example.user.weatherapp.pojo.coords_pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User on 06.12.2017
 */

public class WrapperMainCityModel {

    @SerializedName("modelList")
    @Expose
    private List<MainCityModel> modelList;

    public List<MainCityModel> getModelList() {
        return modelList;
    }

    public void setModelList(List<MainCityModel> modelList) {
        this.modelList = modelList;
    }
}


package com.example.user.weatherapp.pojo.coords_pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("cod")
    @Expose
    private String cod;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("coordsPojoList")
    @Expose
    private java.util.List<CoordsPojoList> coordsPojoList = null;

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public java.util.List<CoordsPojoList> getCoordsPojoList() {
        return coordsPojoList;
    }

    public void setCoordsPojoList(java.util.List<CoordsPojoList> coordsPojoList) {
        this.coordsPojoList = coordsPojoList;
    }

}

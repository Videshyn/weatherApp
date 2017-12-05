package com.example.user.weatherapp.pojo;

/**
 * Created by User on 05.12.2017
 */

public class HistoryModel {

    private int idModel;
    private String cityNameModel;
    private String dataModel;
    private String iconUrlModel;
    private String tmpModel;
    private String windModel;
    private String pressureModel;
    private String humadityModel;
    private String descriptionModel;

    public HistoryModel(int idModel, String cityName, String dataModel, String iconUrlModel, String tmpModel,
                        String windModel, String pressureModel, String humadityModel, String descriptionModel) {
        this.idModel = idModel;
        this.cityNameModel = cityName;
        this.dataModel = dataModel;
        this.iconUrlModel = iconUrlModel;
        this.tmpModel = tmpModel;
        this.windModel = windModel;
        this.pressureModel = pressureModel;
        this.humadityModel = humadityModel;
        this.descriptionModel = descriptionModel;
    }

    public String getCityNameModel() {
        return cityNameModel;
    }

    public void setCityNameModel(String cityNameModel) {
        this.cityNameModel = cityNameModel;
    }

    public int getIdModel() {
        return idModel;
    }

    public void setIdModel(int idModel) {
        this.idModel = idModel;
    }

    public String getDataModel() {
        return dataModel;
    }

    public void setDataModel(String dataModel) {
        this.dataModel = dataModel;
    }

    public String getIconUrlModel() {
        return iconUrlModel;
    }

    public void setIconUrlModel(String iconUrlModel) {
        this.iconUrlModel = iconUrlModel;
    }

    public String getTmpModel() {
        return tmpModel;
    }

    public void setTmpModel(String tmpModel) {
        this.tmpModel = tmpModel;
    }

    public String getWindModel() {
        return windModel;
    }

    public void setWindModel(String windModel) {
        this.windModel = windModel;
    }

    public String getPressureModel() {
        return pressureModel;
    }

    public void setPressureModel(String pressureModel) {
        this.pressureModel = pressureModel;
    }

    public String getHumadityModel() {
        return humadityModel;
    }

    public void setHumadityModel(String humadityModel) {
        this.humadityModel = humadityModel;
    }

    public String getDescriptionModel() {
        return descriptionModel;
    }

    public void setDescriptionModel(String descriptionModel) {
        this.descriptionModel = descriptionModel;
    }
}

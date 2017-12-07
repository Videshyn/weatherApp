package com.example.user.weatherapp.pojo.pojo_robot;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class OpenWeatherMapJSON {

	@SerializedName("city")
	private City city;

	@SerializedName("cnt")
	private double cnt;

	@SerializedName("cod")
	private String cod;

	@SerializedName("message")
	private double message;

	@SerializedName("list")
	private List<WeatherItemList> list;

	public void setCity(City city){
		this.city = city;
	}

	public City getCity(){
		return city;
	}

	public void setCnt(double cnt){
		this.cnt = cnt;
	}

	public double getCnt(){
		return cnt;
	}

	public void setCod(String cod){
		this.cod = cod;
	}

	public String getCod(){
		return cod;
	}

	public void setMessage(double message){
		this.message = message;
	}

	public double getMessage(){
		return message;
	}

	public void setList(List<WeatherItemList> list){
		this.list = list;
	}

	public List<WeatherItemList> getList(){
		return list;
	}

	@Override
 	public String toString(){
		return 
			"OpenWeatherMapJSON{" +
			"city = '" + city + '\'' + 
			",cnt = '" + cnt + '\'' + 
			",cod = '" + cod + '\'' + 
			",message = '" + message + '\'' + 
			",list = '" + list + '\'' + 
			"}";
		}
}
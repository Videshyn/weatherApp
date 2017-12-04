package com.example.user.weatherapp.pojo.pojo_robot;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Wind{

	@SerializedName("deg")
	private Double deg;

	@SerializedName("speed")
	private Double speed;

	public void setDeg(Double deg){
		this.deg = deg;
	}

	public Double getDeg(){
		return deg;
	}

	public void setSpeed(Double speed){
		this.speed = speed;
	}

	public Double getSpeed(){
		return speed;
	}

	@Override
 	public String toString(){
		return 
			"Wind{" + 
			"deg = '" + deg + '\'' + 
			",speed = '" + speed + '\'' + 
			"}";
		}
}
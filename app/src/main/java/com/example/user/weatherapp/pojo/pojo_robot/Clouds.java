package com.example.user.weatherapp.pojo.pojo_robot;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Clouds{

	@SerializedName("all")
	private double all;

	public void setAll(double all){
		this.all = all;
	}

	public double getAll(){
		return all;
	}

	@Override
 	public String toString(){
		return 
			"Clouds{" + 
			"all = '" + all + '\'' + 
			"}";
		}
}
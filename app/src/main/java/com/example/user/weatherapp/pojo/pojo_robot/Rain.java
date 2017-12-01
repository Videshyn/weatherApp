package com.example.user.weatherapp.pojo.pojo_robot;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Rain{

	@SerializedName("3h")
	private double jsonMember3h;

	public void setJsonMember3h(double jsonMember3h){
		this.jsonMember3h = jsonMember3h;
	}

	public double getJsonMember3h(){
		return jsonMember3h;
	}

	@Override
 	public String toString(){
		return 
			"Rain{" + 
			"3h = '" + jsonMember3h + '\'' + 
			"}";
		}
}
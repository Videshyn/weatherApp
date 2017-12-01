package com.example.user.weatherapp.pojo.pojo_robot;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Sys{

	@SerializedName("pod")
	private String pod;

	public void setPod(String pod){
		this.pod = pod;
	}

	public String getPod(){
		return pod;
	}

	@Override
 	public String toString(){
		return 
			"Sys{" + 
			"pod = '" + pod + '\'' + 
			"}";
		}
}
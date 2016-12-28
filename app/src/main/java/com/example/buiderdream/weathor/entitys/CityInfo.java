package com.example.buiderdream.weathor.entitys;

import java.io.Serializable;

public class CityInfo implements Serializable{
	
	private int id;
	private String province;
	private String city;
	private String district;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
 
	public CityInfo(String province, String city, String district) {
		super();
		this.province = province;
		this.city = city;
		this.district = district;
	}
	public CityInfo() {
		super();
	}
	
	
	

}

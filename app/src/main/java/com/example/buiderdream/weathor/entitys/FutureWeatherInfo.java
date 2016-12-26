package com.example.buiderdream.weathor.entitys;

public class FutureWeatherInfo {
	private String temperature;/*�¶�*/
	private String wind_direction;/*��ǰ����*/
	private String week;/*����*/
	private String weather;/*��������*/
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getWind_direction() {
		return wind_direction;
	}
	public void setWind_direction(String wind_direction) {
		this.wind_direction = wind_direction;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public FutureWeatherInfo(String temperature, String wind_direction,
			String week, String weather) {
		super();
		this.temperature = temperature;
		this.wind_direction = wind_direction;
		this.week = week;
		this.weather = weather;
	}
	public FutureWeatherInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}

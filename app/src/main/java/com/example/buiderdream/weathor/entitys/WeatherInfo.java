package com.example.buiderdream.weathor.entitys;

import java.util.List;

public class WeatherInfo {
	private String temp;/*��ǰ�¶�*/
	private String wind_direction;/*��ǰ����*/
	private String time;/*����ʱ��*/
	private String city;
	private String temperature;/*�����¶�*/
	private String weather;/*��������*/
	private String dressing_index;/*����ָ��*/
	private String dressing_advice;/*���½���*/
	private String uv_index;/*������ǿ��*/
	private String exercise_index;/*����ָ��*/
	private String drying_index;/*����ָ��*/
	private String wash_index;/*ϴ��ָ��*/
	private String travel_index;	/*����ָ��*/
	private List<FutureWeatherInfo> list;/*δ�����������*/
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getWind_direction() {
		return wind_direction;
	}
	public void setWind_direction(String wind_direction) {
		this.wind_direction = wind_direction;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getDressing_index() {
		return dressing_index;
	}
	public void setDressing_index(String dressing_index) {
		this.dressing_index = dressing_index;
	}
	public String getDressing_advice() {
		return dressing_advice;
	}
	public void setDressing_advice(String dressing_advice) {
		this.dressing_advice = dressing_advice;
	}
	public String getUv_index() {
		return uv_index;
	}
	public void setUv_index(String uv_index) {
		this.uv_index = uv_index;
	}
	public String getExercise_index() {
		return exercise_index;
	}
	public void setExercise_index(String exercise_index) {
		this.exercise_index = exercise_index;
	}
	public String getDrying_index() {
		return drying_index;
	}
	public void setDrying_index(String drying_index) {
		this.drying_index = drying_index;
	}
	public String getWash_index() {
		return wash_index;
	}
	public void setWash_index(String wash_index) {
		this.wash_index = wash_index;
	}
	public String getTravel_index() {
		return travel_index;
	}
	public void setTravel_index(String travel_index) {
		this.travel_index = travel_index;
	}
	public List<FutureWeatherInfo> getList() {
		return list;
	}
	public void setList(List<FutureWeatherInfo> list) {
		this.list = list;
	}
	public WeatherInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}

package com.example.buiderdream.weathor.entitys;

import java.io.Serializable;
/**
 * Created by Administrator on 2016/12/22.
 * @author 李秉龙
 */
public class City implements Serializable{
    private String cityName;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}

package com.example.buiderdream.weathor.constants;

/**
 * Created by Administrator on 2016/12/22.
 * 常量类
 *
 * @author 李秉龙
 */
public class ConstantUtils {
    //和风天气主机地址
    public static final String HEFENGWEATHER_URL = "https://api.heweather.com/x3/weather";
    //和风天气开发者key
    public static final String HEFENGWEATHER_KEY = "43dfe081adc34c7d82b1ad86570b967f";
    //聚合天气主机地址
    public static final String JUHEWEATHER_URL = "http://v.juhe.cn/weather/citys";
    //和风天气开发者key
    public static final String JUHEWEATHER_KEY = "924305ff7b02119b1eb50fee155a4a67";

    //聚合经纬度地址
    public static final String JUHELATLON_URL = "http://apis.juhe.cn/geo/";
    //聚合经纬度key
    public static final String JUHELATLON_KEY = "f48c3e892f92a885b0e3c360132c3432";

    //保存用户关注的城市
    public static final String USER_COLLECT_CITY = "userColloctCitySP";
    //保存当前城市的天气信息
    public static final String LOCATION_CITY_WEATHER = "locationCityWeatherSP";
    //获取和风天气的数据
    public static final int WEATHERPAGEFRRAGMENT_GET_DATA = 0x00001000;

    //聚合定位得到的数据
    public static final int JUHELATLON_GET_DATA = 0x00001001;
    // 闪屏页获取数据
    public static final int SPLASH_GET_DATA = 0x00001002;
    // 刷新数据
    public static final int REFRESH_DATA = 0x00001002;

}

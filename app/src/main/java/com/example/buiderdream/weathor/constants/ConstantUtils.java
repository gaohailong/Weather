package com.example.buiderdream.weathor.constants;

/**
 * Created by Administrator on 2016/12/22.
 * 常量类
 * @author 李秉龙
 */
public class ConstantUtils {
    //和风天气主机地址
    public static final String HEFENGWEATHER_URL = "https://api.heweather.com/x3/weather" ;
   //和风天气开发者key
    public static final String HEFENGWEATHER_KEY = "43dfe081adc34c7d82b1ad86570b967f";
    //聚合天气主机地址
    public static final String JUHEWEATHER_URL = "http://v.juhe.cn/weather/citys" ;
    //和风天气开发者key
    public static final String JUHEWEATHER_KEY = "924305ff7b02119b1eb50fee155a4a67";

    //保存用户关注的城市
    public static final String USER_COLLECT_CITY = "userColloctCitySP";
    //获取和风天气的数据
    public static final int WEATHERPAGEFRRAGMENT_GET_DATA = 0x00001000;
}

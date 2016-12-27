package com.example.buiderdream.weathor.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.buiderdream.weathor.R;
import com.example.buiderdream.weathor.activitys.AllCityListActivity;
import com.example.buiderdream.weathor.activitys.CityListMgrActivity;
import com.example.buiderdream.weathor.constants.ConstantUtils;
import com.example.buiderdream.weathor.entitys.City;
import com.example.buiderdream.weathor.entitys.HeWeather;
import com.example.buiderdream.weathor.https.OkHttpClientManager;
import com.example.buiderdream.weathor.utils.DateUtils;
import com.example.buiderdream.weathor.utils.SharePreferencesUtil;
import com.google.gson.Gson;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by Administrator on 2016/12/22.
 * 每个城市的page
 *
 * @author 李秉龙
 */
public class WeatherPageFragment extends Fragment {
    private Context context;
    private List<City> cityList;
    private HeWeather weather;
    private View weatherPageFragment;
    private WeatherPageFragmentHandler handler;

    private ImageView img_addCity;  //添加城市
    private TextView tv_cityName;  //城市名
    private TextView tv_actualTemperature;  //实时温度
    private TextView tv_weather;  //天气情况
    private TextView tv_temperature;  //最高温度和最低温度
    private ImageView img_weather;   //今天天气图标
    private TextView tv_week;   //今天的星期

    private ImageView img_notification; //空气质量提示图片
    private TextView tv_airGrade; //空气质量等级
    private TextView tv_airHint; //空气质量提示
    private TextView tv_upDataTime;  //数据发布时

    private TextView tv_oneWeek;      //明天的星期
    private TextView tv_oneTempMax;   //明天的最高温度
    private TextView tv_oneTempMin;   //明天的最低温度
    private TextView tv_oneWeather;   //明天的天气
    private ImageView img_oneWeather;  //明天的天气图片

    private TextView tv_twoWeek;      //后天的星期
    private TextView tv_twoTempMax;   //后天的最高温度
    private TextView tv_twoTempMin;   //后天的最低温度
    private TextView tv_twoWeather;   //后天的天气
    private ImageView img_twoWeather;  //后天的天气图片

    private TextView tv_threeWeek;      //大后天的星期
    private TextView tv_threeTempMax;   //大后天的最高温度
    private TextView tv_threeTempMin;   //大后天的最低温度
    private TextView tv_threeWeather;   //大后天的天气
    private ImageView img_threeWeather;  //大后天的天气图片

    private TextView tv_fourWeek;      //大大后天的星期
    private TextView tv_fourTempMax;   //大大后天的最高温度
    private TextView tv_fourTempMin;   //大大后天的最低温度
    private TextView tv_fourWeather;   //大大后天的天气
    private ImageView img_fourWeather;  //大大后天的天气图片


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        initUserCollectCity();
        handler = new WeatherPageFragmentHandler(WeatherPageFragment.this);
        if (container == null) {
            return null;
        } else {
            weatherPageFragment = inflater.inflate(R.layout.fragment_weatherpage, container, false);
            initView();
            doRequestData(cityList.get(FragmentPagerItem.getPosition(getArguments())).getCityName());
            return weatherPageFragment;
        }
    }

    private void initView() {
        img_addCity = (ImageView) weatherPageFragment.findViewById(R.id.img_addCity);
        tv_cityName = (TextView) weatherPageFragment.findViewById(R.id.tv_cityName);
        tv_actualTemperature = (TextView) weatherPageFragment.findViewById(R.id.tv_actualTemperature);
        tv_weather = (TextView) weatherPageFragment.findViewById(R.id.tv_weather);
        tv_temperature = (TextView) weatherPageFragment.findViewById(R.id.tv_temperature);
        img_weather = (ImageView) weatherPageFragment.findViewById(R.id.img_weather);

        img_addCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CityListMgrActivity.class);
                startActivity(intent);

            }
        });
        tv_week = (TextView) weatherPageFragment.findViewById(R.id.tv_week);
        img_notification = (ImageView) weatherPageFragment.findViewById(R.id.img_notification);
        tv_airGrade = (TextView) weatherPageFragment.findViewById(R.id.tv_airGrade);
        tv_airHint = (TextView) weatherPageFragment.findViewById(R.id.tv_airHint);
        tv_upDataTime = (TextView) weatherPageFragment.findViewById(R.id.tv_upDataTime);

        tv_oneWeek = (TextView) weatherPageFragment.findViewById(R.id.tv_oneWeek);
        tv_oneTempMax = (TextView) weatherPageFragment.findViewById(R.id.tv_oneTempMax);
        tv_oneTempMin = (TextView) weatherPageFragment.findViewById(R.id.tv_oneTempMin);
        tv_oneWeather = (TextView) weatherPageFragment.findViewById(R.id.tv_oneWeather);
        img_oneWeather = (ImageView) weatherPageFragment.findViewById(R.id.img_oneWeather);

        tv_twoWeek = (TextView) weatherPageFragment.findViewById(R.id.tv_twoWeek);
        tv_twoTempMax = (TextView) weatherPageFragment.findViewById(R.id.tv_twoTempMax);
        tv_twoTempMin = (TextView) weatherPageFragment.findViewById(R.id.tv_twoTempMin);
        tv_twoWeather = (TextView) weatherPageFragment.findViewById(R.id.tv_twoWeather);
        img_twoWeather = (ImageView) weatherPageFragment.findViewById(R.id.img_twoWeather);

        tv_threeWeek = (TextView) weatherPageFragment.findViewById(R.id.tv_threeWeek);
        tv_threeTempMax = (TextView) weatherPageFragment.findViewById(R.id.tv_threeTempMax);
        tv_threeTempMin = (TextView) weatherPageFragment.findViewById(R.id.tv_threeTempMin);
        tv_threeWeather = (TextView) weatherPageFragment.findViewById(R.id.tv_threeWeather);
        img_threeWeather = (ImageView) weatherPageFragment.findViewById(R.id.img_threeWeather);

        tv_fourWeek = (TextView) weatherPageFragment.findViewById(R.id.tv_fourWeek);
        tv_fourTempMax = (TextView) weatherPageFragment.findViewById(R.id.tv_fourTempMax);
        tv_fourTempMin = (TextView) weatherPageFragment.findViewById(R.id.tv_fourTempMin);
        tv_fourWeather = (TextView) weatherPageFragment.findViewById(R.id.tv_fourWeather);
        img_fourWeather = (ImageView) weatherPageFragment.findViewById(R.id.img_fourWeather);

    }

    private void doRequestData(String cityName) {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("city", cityName);
        map.put("key", ConstantUtils.HEFENGWEATHER_KEY);
        String url = OkHttpClientManager.attachHttpGetParams(ConstantUtils.HEFENGWEATHER_URL, map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                // 将{}中视为是json对象
                JSONObject jsonObject = new JSONObject(result);
                // 获取键"weatherinfo"中对应的值
                JSONArray jsonArray1 = jsonObject
                        .getJSONArray("HeWeather data service 3.0");
                JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                // 使用jar包解析数据
                Gson gson = new Gson();
                weather = gson.fromJson(
                        jsonObject2.toString(), HeWeather.class);
                handler.sendEmptyMessage(ConstantUtils.WEATHERPAGEFRRAGMENT_GET_DATA);
            }
        });
    }


    /**
     * 更新界面
     */
    private void upDataView() {
        tv_cityName.setText( weather.getBasic().getCity());
        tv_actualTemperature.setText(weather.getNow().getFl()+"°");
        tv_weather.setText(weather.getNow().getCond().getTxt());
        tv_temperature.setText(weather.getDaily_forecast().get(0).getTmp().getMin()+"~"+weather.getDaily_forecast().get(0).getTmp().getMax());
        img_notification.setImageDrawable(getAirHintImg(weather.getAqi().getCity().getAqi()));
        tv_airGrade.setText(weather.getSuggestion().getAir().getBrf());
        tv_airHint.setText(weather.getSuggestion().getAir().getTxt());
        tv_week.setText(DateUtils.getWeek(weather.getDaily_forecast().get(0).getDate()));
        tv_upDataTime.setText(weather.getBasic().getUpdate().getLoc().substring(10,16)+"发布");

        tv_oneWeek.setText(DateUtils.getWeek(weather.getDaily_forecast().get(1).getDate()));
        tv_oneTempMax.setText(weather.getDaily_forecast().get(1).getTmp().getMax());
        tv_oneTempMin.setText(weather.getDaily_forecast().get(1).getTmp().getMin());
        tv_oneWeather.setText(weather.getDaily_forecast().get(1).getCond().getTxt_d());

        tv_twoWeek.setText(DateUtils.getWeek(weather.getDaily_forecast().get(2).getDate()));
        tv_twoTempMax.setText(weather.getDaily_forecast().get(2).getTmp().getMax());
        tv_twoTempMin.setText(weather.getDaily_forecast().get(2).getTmp().getMin());
        tv_twoWeather.setText(weather.getDaily_forecast().get(2).getCond().getTxt_d());

        tv_threeWeek.setText(DateUtils.getWeek(weather.getDaily_forecast().get(3).getDate()));
        tv_threeTempMax.setText(weather.getDaily_forecast().get(3).getTmp().getMax());
        tv_threeTempMin.setText(weather.getDaily_forecast().get(3).getTmp().getMin());
        tv_threeWeather.setText(weather.getDaily_forecast().get(3).getCond().getTxt_d());

        tv_fourWeek.setText(DateUtils.getWeek(weather.getDaily_forecast().get(4).getDate()));
        tv_fourTempMax.setText(weather.getDaily_forecast().get(4).getTmp().getMax());
        tv_fourTempMin.setText(weather.getDaily_forecast().get(4).getTmp().getMin());
        tv_fourWeather.setText(weather.getDaily_forecast().get(4).getCond().getTxt_d());
    }

    /**
     * 从SharePreferences中读取用户收藏的城市，如果没有初始化为北京
     */
    public void initUserCollectCity() {
        cityList = (List<City>) SharePreferencesUtil.readObject(context, ConstantUtils.USER_COLLECT_CITY);
        if (cityList == null || cityList.size() == 0) {
            cityList = new ArrayList<>();
            City city = new City();
            city.setCityName("北京");
            cityList.add(city);
        }
    }

    /**
     *设置空气质量的指示图片
     * @param brf
     * @return
     */
    public Drawable getAirHintImg(String brf) {
        int index = Integer.valueOf(brf);
        Drawable airHintImg = ContextCompat.getDrawable(context,R.mipmap.biz_plugin_weather_0_50);
       if (index<=50){
           airHintImg = ContextCompat.getDrawable(context,R.mipmap.biz_plugin_weather_0_50);
       }else if (50<index&&index<=100){
           airHintImg = ContextCompat.getDrawable(context,R.mipmap.biz_plugin_weather_51_100);
       }else if (100<index&&index<=150){
           airHintImg = ContextCompat.getDrawable(context,R.mipmap.biz_plugin_weather_101_150);
       }else if (150<index&&index<=200){
           airHintImg = ContextCompat.getDrawable(context,R.mipmap.biz_plugin_weather_151_200);
       }else if (200<index&&index<=300){
           airHintImg = ContextCompat.getDrawable(context,R.mipmap.biz_plugin_weather_201_300);
       }
        return airHintImg;
    }


    class WeatherPageFragmentHandler extends Handler {

        WeakReference<WeatherPageFragment> weakReference;

        public WeatherPageFragmentHandler(WeatherPageFragment fragment) {
            weakReference = new WeakReference<WeatherPageFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtils.WEATHERPAGEFRRAGMENT_GET_DATA:
                    upDataView();
                    break;
            }
        }
    }
}

package com.example.buiderdream.weathor.fragment;

import android.content.Context;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ActionMenuView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.buiderdream.weathor.R;
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

    private ImageView img_detailWeather;  //天气图片
    private TextView tv_detailWeather;   //详细的今日预报
    private TextView tv_detailTemp;   //详细的体感温度
    private TextView tv_detailHumidity;   //详细的空气湿度
    private TextView tv_detailWind;   //详细的风力风向

    private TextView detail_tv_pm25;  //pm25
    private TextView detail_tv_pm10;  //pm10
    private TextView detail_tv_so2;  //so2
    private TextView detail_tv_NO2;  //no2
    private TextView detail_tv_O3;  //o3
    private TextView detail_tv_CO;  //co

    private RelativeLayout rl_content;     //天气数据
    private RelativeLayout rl_air;          //空气质量
    private RelativeLayout rl_center;       //中心标题
    private RelativeLayout rl_bottomView;   //底部标题
    private LinearLayout gv_fourDay;         //未来四天数据


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

        img_detailWeather = (ImageView) weatherPageFragment.findViewById(R.id.img_detailWeather);
        tv_detailWeather = (TextView) weatherPageFragment.findViewById(R.id.tv_detailWeather);
        tv_detailTemp = (TextView) weatherPageFragment.findViewById(R.id.tv_detailTemp);
        tv_detailHumidity = (TextView) weatherPageFragment.findViewById(R.id.tv_detailHumidity);
        tv_detailWind = (TextView) weatherPageFragment.findViewById(R.id.tv_detailWind);

        rl_content = (RelativeLayout) this.weatherPageFragment.findViewById(R.id.rl_content);
        rl_air = (RelativeLayout) this.weatherPageFragment.findViewById(R.id.rl_air);
        rl_center = (RelativeLayout) this.weatherPageFragment.findViewById(R.id.rl_center);
        rl_bottomView = (RelativeLayout) this.weatherPageFragment.findViewById(R.id.rl_bottomView);
        gv_fourDay = (LinearLayout) this.weatherPageFragment.findViewById(R.id.gv_fourDay);

        detail_tv_pm25 = (TextView) weatherPageFragment.findViewById(R.id.detail_tv_pm25);
        detail_tv_pm10 = (TextView) weatherPageFragment.findViewById(R.id.detail_tv_pm10);
        detail_tv_so2 = (TextView) weatherPageFragment.findViewById(R.id.detail_tv_so2);
        detail_tv_NO2 = (TextView) weatherPageFragment.findViewById(R.id.detail_tv_NO2);
        detail_tv_O3 = (TextView) weatherPageFragment.findViewById(R.id.detail_tv_O3);
        detail_tv_CO = (TextView) weatherPageFragment.findViewById(R.id.detail_tv_CO);


        ViewGroup.LayoutParams params = rl_content.getLayoutParams();
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        params.width = width;
        //屏幕适配
        params.height = height - img_addCity.getLayoutParams().height * 2 - rl_air.getLayoutParams().height
                - rl_bottomView.getLayoutParams().height * 2 - rl_center.getLayoutParams().height - gv_fourDay.getLayoutParams().height;
        rl_content.setLayoutParams(params);

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
        img_weather.setImageResource(setWeatherImg(weather.getDaily_forecast().get(0).getCond().getCode_d()));
        tv_cityName.setText(weather.getBasic().getCity());
        tv_actualTemperature.setText(weather.getNow().getTmp() + "°");
        tv_weather.setText(weather.getNow().getCond().getTxt());
        tv_temperature.setText(weather.getDaily_forecast().get(0).getTmp().getMin() + "~" + weather.getDaily_forecast().get(0).getTmp().getMax());
        img_notification.setImageDrawable(getAirHintImg(weather.getAqi().getCity().getAqi()));
        tv_airGrade.setText(weather.getSuggestion().getAir().getBrf());
        tv_airHint.setText(weather.getSuggestion().getAir().getTxt());
        tv_week.setText(DateUtils.getWeek(weather.getDaily_forecast().get(0).getDate()));
        tv_upDataTime.setText(weather.getBasic().getUpdate().getLoc().substring(10, 16) + "发布");

        tv_oneWeek.setText(DateUtils.getWeek(weather.getDaily_forecast().get(1).getDate()));
        tv_oneTempMax.setText(weather.getDaily_forecast().get(1).getTmp().getMax() + "°");
        tv_oneTempMin.setText(weather.getDaily_forecast().get(1).getTmp().getMin() + "°");
        tv_oneWeather.setText(weather.getDaily_forecast().get(1).getCond().getTxt_d());
        img_oneWeather.setImageResource(setWeatherImg(weather.getDaily_forecast().get(1).getCond().getCode_d()));

        tv_twoWeek.setText(DateUtils.getWeek(weather.getDaily_forecast().get(2).getDate()));
        tv_twoTempMax.setText(weather.getDaily_forecast().get(2).getTmp().getMax() + "°");
        tv_twoTempMin.setText(weather.getDaily_forecast().get(2).getTmp().getMin() + "°");
        tv_twoWeather.setText(weather.getDaily_forecast().get(2).getCond().getTxt_d());
        img_twoWeather.setImageResource(setWeatherImg(weather.getDaily_forecast().get(2).getCond().getCode_d()));

        tv_threeWeek.setText(DateUtils.getWeek(weather.getDaily_forecast().get(3).getDate()));
        tv_threeTempMax.setText(weather.getDaily_forecast().get(3).getTmp().getMax() + "°");
        tv_threeTempMin.setText(weather.getDaily_forecast().get(3).getTmp().getMin() + "°");
        tv_threeWeather.setText(weather.getDaily_forecast().get(3).getCond().getTxt_d());
        img_threeWeather.setImageResource(setWeatherImg(weather.getDaily_forecast().get(3).getCond().getCode_d()));

        tv_fourWeek.setText(DateUtils.getWeek(weather.getDaily_forecast().get(4).getDate()));
        tv_fourTempMax.setText(weather.getDaily_forecast().get(4).getTmp().getMax() + "°");
        tv_fourTempMin.setText(weather.getDaily_forecast().get(4).getTmp().getMin() + "°");
        tv_fourWeather.setText(weather.getDaily_forecast().get(4).getCond().getTxt_d());
        img_fourWeather.setImageResource(setWeatherImg(weather.getDaily_forecast().get(4).getCond().getCode_d()));

        img_detailWeather.setImageResource(setWeatherImg(weather.getDaily_forecast().get(0).getCond().getCode_d()));
        tv_detailWeather.setText(weather.getNow().getCond().getTxt());
        tv_detailTemp.setText(weather.getNow().getFl() + "°");
        tv_detailHumidity.setText(weather.getNow().getHum());
        tv_detailWind.setText(weather.getNow().getWind().getDir() + weather.getNow().getWind().getSc());

        if (weather.getAqi()==null||weather.getAqi().equals("")){
            detail_tv_pm25.setText("暂无数据");
            detail_tv_pm10.setText("暂无数据");
            detail_tv_so2.setText("暂无数据");
            detail_tv_NO2.setText("暂无数据");
            detail_tv_O3.setText("暂无数据");
            detail_tv_CO.setText("暂无数据");
        }else {
            detail_tv_pm25.setText(weather.getAqi().getCity().getPm25());
            detail_tv_pm10.setText(weather.getAqi().getCity().getPm10());
            detail_tv_so2.setText(weather.getAqi().getCity().getSo2());
            detail_tv_NO2.setText(weather.getAqi().getCity().getNo2());
            detail_tv_O3.setText(weather.getAqi().getCity().getO3());
            detail_tv_CO.setText(weather.getAqi().getCity().getCo());
        }

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
     * 设置空气质量的指示图片
     *
     * @param brf
     * @return
     */
    private Drawable getAirHintImg(String brf) {
        int index = Integer.valueOf(brf);
        Drawable airHintImg = ContextCompat.getDrawable(context, R.mipmap.biz_plugin_weather_0_50);
        if (index <= 50) {
            airHintImg = ContextCompat.getDrawable(context, R.mipmap.biz_plugin_weather_0_50);
        } else if (50 < index && index <= 100) {
            airHintImg = ContextCompat.getDrawable(context, R.mipmap.biz_plugin_weather_51_100);
        } else if (100 < index && index <= 150) {
            airHintImg = ContextCompat.getDrawable(context, R.mipmap.biz_plugin_weather_101_150);
        } else if (150 < index && index <= 200) {
            airHintImg = ContextCompat.getDrawable(context, R.mipmap.biz_plugin_weather_151_200);
        } else if (200 < index && index <= 300) {
            airHintImg = ContextCompat.getDrawable(context, R.mipmap.biz_plugin_weather_201_300);
        }
        return airHintImg;
    }

    /**
     * 设置天气图片
     *
     * @param id
     * @return
     */
    private static int setWeatherImg(String id) {
        int src = 0;
        int windid = Integer.valueOf(id);
        switch (windid) {
            case 100:
                src = R.drawable.w100;
                break;
            case 101:
                src = R.drawable.w101;
                break;
            case 103:
                src = R.drawable.w103;
                break;
            case 104:
                src = R.drawable.w104;
                break;
            case 200:
                src = R.drawable.w200;
                break;
            case 201:
                src = R.drawable.w201;
                break;
            case 202:
                src = R.drawable.w202;
                break;
            case 203:
                src = R.drawable.w203;
                break;
            case 204:
                src = R.drawable.w204;
                break;
            case 205:
                src = R.drawable.w205;
                break;
            case 206:
                src = R.drawable.w206;
                break;
            case 207:
                src = R.drawable.w207;
                break;
            case 208:
                src = R.drawable.w208;
                break;
            case 209:
                src = R.drawable.w209;
                break;
            case 210:
                src = R.drawable.w210;
                break;
            case 211:
                src = R.drawable.w211;
                break;
            case 212:
                src = R.drawable.w212;
                break;
            case 213:
                src = R.drawable.w213;
                break;
            case 300:
                src = R.drawable.w300;
                break;
            case 301:
                src = R.drawable.w301;
                break;
            case 302:
                src = R.drawable.w302;
                break;
            case 303:
                src = R.drawable.w303;
                break;

            case 304:
                src = R.drawable.w304;
                break;
            case 305:
                src = R.drawable.w305;
                break;
            case 306:
                src = R.drawable.w306;
                break;
            case 307:
                src = R.drawable.w307;
                break;
            case 308:
                src = R.drawable.w308;
                break;

            case 309:
                src = R.drawable.w309;
                break;
            case 310:
                src = R.drawable.w310;
                break;
            case 311:
                src = R.drawable.w311;
                break;
            case 312:
                src = R.drawable.w312;
                break;
            case 313:
                src = R.drawable.w313;
                break;
            case 400:
                src = R.drawable.w400;
                break;
            case 401:
                src = R.drawable.w401;
                break;
            case 402:
                src = R.drawable.w402;
                break;
            case 403:
                src = R.drawable.w403;
                break;
            case 404:
                src = R.drawable.w404;
                break;
            case 405:
                src = R.drawable.w405;
                break;
            case 406:
                src = R.drawable.w406;
                break;
            case 407:
                src = R.drawable.w407;
                break;
            case 500:
                src = R.drawable.w500;
                break;
            case 501:
                src = R.drawable.w501;
                break;
            case 502:
                src = R.drawable.w502;
                break;
            case 503:
                src = R.drawable.w503;
                break;
            case 504:
                src = R.drawable.w504;
                break;
            case 507:
                src = R.drawable.w507;
                break;
            case 508:
                src = R.drawable.w508;
                break;
            case 900:
                src = R.drawable.w900;
                break;
            case 901:
                src = R.drawable.w901;
                break;
            case 999:
                src = R.drawable.w999;
                break;
            default:
                break;
        }
        return src;
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

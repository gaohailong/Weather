package com.example.buiderdream.weathor.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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

    private ImageView img_addCity;
    private TextView tv_cityName;  //城市名
    private TextView tv_actualTemperature;  //实时温度
    private TextView tv_weather;  //天气情况
    private TextView tv_temperature;  //最高温度和最低温度

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
        img_addCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CityListMgrActivity.class);
                startActivity(intent);

            }
        });
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

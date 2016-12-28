package com.example.buiderdream.weathor.activitys;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.widget.Toast;

import com.example.buiderdream.weathor.R;
import com.example.buiderdream.weathor.base.BaseActivity;
import com.example.buiderdream.weathor.constants.ConstantUtils;
import com.example.buiderdream.weathor.entitys.City;
import com.example.buiderdream.weathor.entitys.HeWeather;
import com.example.buiderdream.weathor.https.OkHttpClientManager;
import com.example.buiderdream.weathor.service.NotifyService;
import com.example.buiderdream.weathor.utils.NetWorkUtils;
import com.example.buiderdream.weathor.utils.SharePreferencesUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class SplashActivity extends BaseActivity {
    private List<City> cityList;
    private Context context;
    /**
     * 当前位置
     */
    private LocationManager manager;
    private String provider;
    private  String lat,lon;//经纬度
    private String district;//城市名称
    private SplashHandler handler;
    private HeWeather weather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        Intent intent = new Intent(SplashActivity.this, NotifyService.class);
//        startService(intent);
        context = this;
        handler = new SplashHandler();
       if (NetWorkUtils.GetNetype(context)==-1){
           Toast.makeText(context,"当前无网络连接！",Toast.LENGTH_SHORT).show();
           nextActivity();
           return;
       }
        getLoctionCity();


    }
    /**
     * 得到当前位置的经纬度
     */
    public void getLoctionCity() {
        //得到当前城市的经纬度
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providerList = manager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(SplashActivity.this, "没有", Toast.LENGTH_SHORT).show();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = manager.getLastKnownLocation(provider);
        if (location != null)
        {
            //纬度
            Toast.makeText(SplashActivity.this,location.getLatitude()+"",Toast.LENGTH_SHORT).show();
            lat = location.getLatitude()+"";
            //经度
            Toast.makeText(SplashActivity.this,location.getLongitude()+"",Toast.LENGTH_SHORT).show();
            lon = location.getLongitude()+"";
        }
        manager.requestLocationUpdates(provider,5000,1,locationListener);
        Get_CityName(lat,lon);
        Toast.makeText(SplashActivity.this,district,Toast.LENGTH_SHORT).show();
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    //经纬度转为城市名
    public void Get_CityName(String Lat,String lon){
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String,String> map = new HashMap<>();
        map.put("lng",lon);
        map.put("lat",Lat);
        map.put("key", ConstantUtils.JUHELATLON_KEY);
        map.put("type",1+"");
        String url = OkHttpClientManager.attachHttpGetParams(ConstantUtils.JUHELATLON_URL,map);
        manager.getAsync(url,new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context,"失败了",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                JSONObject object = new JSONObject(result);
                JSONObject object1 = object.getJSONObject("result");
                JSONObject object2 =object1.getJSONObject("ext");
                district = object2.getString("district") ;
                district = district.substring(0,district.length()-1);
                handler.sendEmptyMessage(ConstantUtils.JUHELATLON_GET_DATA);
            }
        });
    }
    /**
     * 得到当前城市的数据
     */
    private void doGetJuHeLocation() {

        City city = new City();
        city.setCityName(district);
        cityList = (ArrayList<City>) SharePreferencesUtil.readObject(context,ConstantUtils.USER_COLLECT_CITY);
        if (cityList==null||cityList.size()==0){
            cityList = new ArrayList<>();
            cityList.add(city);
        }else {
            if (!cityList.get(0).getCityName().equals(district)){
                int cityIndex = cityList.indexOf(city);
                if (cityIndex>0) {
                    cityList.remove(city);
                }
                cityList.set(0,city);
            }
        }
        SharePreferencesUtil.saveObject(context, ConstantUtils.USER_COLLECT_CITY,cityList);
        doRequestData(district);
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
                handler.sendEmptyMessage(ConstantUtils.SPLASH_GET_DATA);
            }
        });
    }



    class  SplashHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ConstantUtils.JUHELATLON_GET_DATA:
                    doGetJuHeLocation();
                    break;
                case ConstantUtils.SPLASH_GET_DATA:
                    //保存当前城市数据
                    SharePreferencesUtil.saveObject(context,ConstantUtils.LOCATION_CITY_WEATHER,weather);
                    nextActivity();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 跳转到下一视图
     */
    private void nextActivity() {
        boolean flag = getSharedPreferences(ConstantUtils.GUIDE_SP,MODE_PRIVATE).getBoolean("firstUse",true);
        if (!flag){
            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(SplashActivity.this,GuideActivity.class);
            startActivity(intent);
        }
        finish();
    }
}

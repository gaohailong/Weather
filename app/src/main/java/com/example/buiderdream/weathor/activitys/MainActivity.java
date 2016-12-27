package com.example.buiderdream.weathor.activitys;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Switch;
import android.widget.Toast;

import com.ant.liao.GifView;
import com.example.buiderdream.weathor.R;
import com.example.buiderdream.weathor.base.BaseActivity;
import com.example.buiderdream.weathor.constants.ConstantUtils;
import com.example.buiderdream.weathor.entitys.City;
import com.example.buiderdream.weathor.fragment.WeatherPageFragment;
import com.example.buiderdream.weathor.https.OkHttpClientManager;
import com.example.buiderdream.weathor.utils.SharePreferencesUtil;
import com.google.gson.Gson;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import okhttp3.Request;

/**
 * Created by Administrator on 2016/12/22.
 * @author 李秉龙
 */
public class MainActivity extends FragmentActivity {

    private ViewPager vp_cityWeather;
    private FragmentPagerItems.Creator creater;//对节点的动态添加
    private ArrayList<Fragment> fragmentList;
    private List<City> cityList;
    private Context context;
    private GifView git_background;

    /**
     * 当前位置
     */
    private LocationManager manager;
    private String provider;
    private  String lat,lon;//经纬度
    private String district;//城市名称
    private  ManiActivityHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        saveCity();
        initView();


        //得到当前城市的经纬度
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providerList = manager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(MainActivity.this, "没有", Toast.LENGTH_SHORT).show();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = manager.getLastKnownLocation(provider);
        if (location != null)

        {
            //纬度
            Toast.makeText(MainActivity.this,location.getLatitude()+"",Toast.LENGTH_SHORT).show();
            lat = location.getLatitude()+"";
            //经度
            Toast.makeText(MainActivity.this,location.getLongitude()+"",Toast.LENGTH_SHORT).show();
            lon = location.getLongitude()+"";
        }
        manager.requestLocationUpdates(provider,5000,1,locationListener);

        Get_CityName(lat,lon);
        Toast.makeText(MainActivity.this,district,Toast.LENGTH_SHORT).show();
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
        map.put("key",ConstantUtils.JUHELATLON_KEY);
        map.put("type",1+"");
        String url = OkHttpClientManager.attachHttpGetParams(ConstantUtils.JUHELATLON_URL,map);
        manager.getAsync(url,new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {

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

    //测试添加静态用户收藏城市后期删除
    private void saveCity() {
        cityList = new ArrayList<>();
        City city1 = new City();
        city1.setCityName("太原");
        City city2 = new City();
        city2.setCityName("上海");
        City city3 = new City();
        city3.setCityName("北京");
        cityList.add(city1);
        cityList.add(city2);
        cityList.add(city3);
        SharePreferencesUtil.saveObject(context,ConstantUtils.USER_COLLECT_CITY,cityList);

    }

    private void initView() {
        vp_cityWeather = (ViewPager) this.findViewById(R.id.vp_cityWeather);
        git_background = (GifView) this.findViewById(R.id.gif_background);
        // 设置Gif图片源
        git_background.setGifImage(R.drawable.gif_sunny);
        // 设置显示的大小，拉伸或者压缩
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        git_background.setShowDimension(width, height);
        // 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
        git_background.setGifImageType(GifView.GifImageType.COVER);
        initFragmentList();
    }

    private void initFragmentList() {
        creater = FragmentPagerItems.with(context);
        initUserCollectCity();
        for (int i = 0; i < cityList.size(); i++) {
            creater.add(cityList.get(i).getCityName(), WeatherPageFragment.class);
        }
        FragmentPagerItemAdapter fragmentadapter = new FragmentPagerItemAdapter(getSupportFragmentManager()
                , creater.create());
        vp_cityWeather.setAdapter(fragmentadapter);

    }

    /**
     *  从SharePreferences中读取用户收藏的城市，如果没有初始化为北京
     */
       public void initUserCollectCity() {
        cityList = (List<City>) SharePreferencesUtil.readObject(context, ConstantUtils.USER_COLLECT_CITY);
        if (cityList==null||cityList.size()==0){
            cityList = new ArrayList<>();
            City city = new City();
            city.setCityName("北京");
            cityList.add(city);
        }
    }

    class  ManiActivityHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ConstantUtils.JUHELATLON_GET_DATA:
                    Toast.makeText(context,district+"jjjjj",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}

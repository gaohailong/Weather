package com.example.buiderdream.weathor.activitys;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.example.buiderdream.weathor.R;
import com.example.buiderdream.weathor.adapter.WeatherFragmentPagerAdapter;
import com.example.buiderdream.weathor.entitys.City;
import com.example.buiderdream.weathor.fragment.WeatherPageFragment;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private ViewPager vp_cityWeather ;
    private FragmentPagerItems.Creator creater;//对节点的动态添加
    private WeatherFragmentPagerAdapter adapter;
    private ArrayList<Fragment> fragmentList;
    private List<City> cityList;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initView();
    }

    private void initView() {
        vp_cityWeather = (ViewPager) this.findViewById(R.id.vp_cityWeather);

        initFragmentList();


    }

    private void initFragmentList() {
        creater = FragmentPagerItems.with(context);
        cityList = new ArrayList<>();
        for (int i = 0; i<3;i++){
            City city = new City();
            city.setCityName("aaaa"+i);
            cityList.add(city);
        }
        //        将节点添加到标题中
        for (int i = 0; i < cityList.size(); i++) {
            creater.add(cityList.get(i).getCityName(), WeatherPageFragment.class);
        }
        FragmentPagerItemAdapter fragmentadapter = new FragmentPagerItemAdapter(getSupportFragmentManager()
                , creater.create());
        vp_cityWeather.setAdapter(fragmentadapter);

    }


}

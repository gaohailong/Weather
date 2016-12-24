package com.example.buiderdream.weathor.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.ant.liao.GifView;
import com.example.buiderdream.weathor.R;
import com.example.buiderdream.weathor.entitys.City;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 * 每个城市的page
 * @author 李秉龙
 */
public class WeatherPageFragment extends Fragment {
    private Context context;
    private List<City> cityList;
    private City city;
    private TextView tv_text;


    private View weatherPageFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        cityList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            City city = new City();
            city.setCityName("aaaa" + i);
            cityList.add(city);
        }
        // TODO Auto-generated method stub
        if (container == null) {
            return null;
        } else {
            weatherPageFragment = inflater.inflate(R.layout.fragment_weatherpage, container, false);
            return weatherPageFragment;
        }
    }
}

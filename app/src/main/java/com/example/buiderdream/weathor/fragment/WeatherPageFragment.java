package com.example.buiderdream.weathor.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.buiderdream.weathor.R;
import com.example.buiderdream.weathor.entitys.City;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/24.
 */

public class WeatherPageFragment extends Fragment {
    private Context mContext;
    private List<City> cityList;
    private City city;
    private TextView tv_text;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cityList = new ArrayList<>();
        for (int i = 0; i<3;i++){
            City city = new City();
            city.setCityName("aaaa"+i);
            cityList.add(city);
        }
        // TODO Auto-generated method stub
        if (container == null) {
            return null;
        } else {
            View view = inflater.inflate(R.layout.fragment_weatherpage, container, false);
            tv_text = (TextView) view.findViewById(R.id.tv_text);
            tv_text.setText(cityList.get( FragmentPagerItem.getPosition(getArguments())).getCityName());
            return view;
        }
    }
}

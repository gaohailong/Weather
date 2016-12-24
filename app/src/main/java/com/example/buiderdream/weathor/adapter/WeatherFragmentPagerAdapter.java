package com.example.buiderdream.weathor.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/24.
 */

public class WeatherFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "WeatherFragmentPagerAdapter";
    private ArrayList<Fragment> mList;
    private Context mContext;
    public WeatherFragmentPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.mContext = context;
        mList = new ArrayList<Fragment>();
    }

    public void setLists(ArrayList<Fragment> lists) {
        this.mList = lists;
    }

    public void UpdateList(ArrayList<Fragment> arrayList) {
        this.mList.clear();
        this.mList.addAll(arrayList);

        notifyDataSetChanged();
    }
    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }
    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return WeatherFragmentPagerAdapter.POSITION_NONE;
    }
}

package com.example.buiderdream.weathor.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.ant.liao.GifView;
import com.example.buiderdream.weathor.R;
import com.example.buiderdream.weathor.constants.ConstantUtils;
import com.example.buiderdream.weathor.entitys.City;
import com.example.buiderdream.weathor.fragment.WeatherPageFragment;
import com.example.buiderdream.weathor.service.NotifyService;
import com.example.buiderdream.weathor.utils.SharePreferencesUtil;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;

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
    private GifView gif_background;

    private static long currentTime = 0;
    private static long laseTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, NotifyService.class);
        startService(intent);
        context = this;
        initView();



    }


    private void initView() {
        vp_cityWeather = (ViewPager) this.findViewById(R.id.vp_cityWeather);
        gif_background = (GifView) this.findViewById(R.id.gif_background);
        // 设置Gif图片源
        gif_background.setGifImage(R.drawable.gif_default);
        // 设置显示的大小，拉伸或者压缩
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        gif_background.setShowDimension(width, height);
        // 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
        gif_background.setGifImageType(GifView.GifImageType.COVER);

    }

    @Override
    protected void onResume() {
        super.onResume();
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
            SharePreferencesUtil.saveObject(context,ConstantUtils.USER_COLLECT_CITY,cityList);
        }
    }
}

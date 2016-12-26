package com.example.buiderdream.weathor.activitys;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.WindowManager;

import com.ant.liao.GifView;
import com.example.buiderdream.weathor.R;
import com.example.buiderdream.weathor.constants.ConstantUtils;
import com.example.buiderdream.weathor.entitys.City;
import com.example.buiderdream.weathor.fragment.WeatherPageFragment;
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

    private ViewPager vp_cityWeather ;
    private FragmentPagerItems.Creator creater;//对节点的动态添加
    private ArrayList<Fragment> fragmentList;
    private List<City> cityList;
    private Context context;
    private GifView git_background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        saveCity();
        initView();
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
}

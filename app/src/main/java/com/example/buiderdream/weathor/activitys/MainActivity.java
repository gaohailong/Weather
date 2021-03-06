package com.example.buiderdream.weathor.activitys;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.KeyEvent;
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
 *
 * @author 李秉龙
 */
public class MainActivity extends FragmentActivity implements WeatherPageFragment.OnButtonClick {


    private ViewPager vp_cityWeather;
    private FragmentPagerItems.Creator creater;//对节点的动态添加
    private ArrayList<Fragment> fragmentList;
    private List<City> cityList;
    private Context context;
    private GifView gif_background;
    private static int currItem = 0;


    AlarmManager manager;
    PendingIntent pendingIntent;

    private static long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, NotifyService.class);
        pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 1000 * 60, pendingIntent);

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
        vp_cityWeather.setCurrentItem(currItem);
        currItem=0;
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
            SharePreferencesUtil.saveObject(context, ConstantUtils.USER_COLLECT_CITY, cityList);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(context, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onButtonClick() {
        Intent intent = new Intent(MainActivity.this, CityListMgrActivity.class);
        startActivityForResult(intent, 1000);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000&&resultCode==1001){
            int result = data.getIntExtra("result",0);
            currItem = result;
        }
    }
}

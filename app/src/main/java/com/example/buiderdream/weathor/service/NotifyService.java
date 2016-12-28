package com.example.buiderdream.weathor.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.buiderdream.weathor.R;
import com.example.buiderdream.weathor.activitys.MainActivity;
import com.example.buiderdream.weathor.activitys.SplashActivity;
import com.example.buiderdream.weathor.constants.ConstantUtils;
import com.example.buiderdream.weathor.entitys.City;
import com.example.buiderdream.weathor.entitys.HeWeather;
import com.example.buiderdream.weathor.https.OkHttpClientManager;
import com.example.buiderdream.weathor.utils.SharePreferencesUtil;
import com.example.buiderdream.weathor.utils.UpdataWeatherUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by Admin on 2016/12/28.
 */


public class NotifyService extends Service{

    private  NotiHandler handler;
    private HeWeather weather;

    Notification notification;
    RemoteViews remoteViews;
    NotificationManager nm;

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        List<City> list = (ArrayList<City>) SharePreferencesUtil.readObject(getApplication(),ConstantUtils.USER_COLLECT_CITY);
        doRequestData(list.get(0).getCityName());
        handler = new  NotiHandler();
//        setNotification();
        return super.onStartCommand(intent, flags, startId);
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
                handler.sendEmptyMessage(ConstantUtils.SPLASH_NOTIFY);

            }
        });
    }

    class  NotiHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ConstantUtils.SPLASH_NOTIFY:
                    SharePreferencesUtil.saveObject(getApplication(), ConstantUtils.LOCATION_CITY_WEATHER, weather);
                    setNotification();
                    break;
            }

        }
    }
    private void setNotification() {
        HeWeather weather = (HeWeather) SharePreferencesUtil.readObject(this, ConstantUtils.LOCATION_CITY_WEATHER);
        //  当前城市
        notification = new Notification();
        notification.icon = R.mipmap.ic_weather_48px;
        notification.tickerText = "新通知";
        notification.defaults = Notification.DEFAULT_ALL;
        remoteViews = new RemoteViews(this.getPackageName(), R.layout.item_notify);
        if (weather!= null) {
            String curCity = weather.getBasic().getCity();
            //当前城市温度
            String cutTemp = weather.getNow().getTmp();
            UpdataWeatherUtils.setWeatherImg(weather.getNow().getCond().getCode());
            remoteViews.setTextViewText(R.id.noti_textView2, curCity);
            remoteViews.setTextViewText(R.id.noti_textView3, cutTemp+"摄氏度");
        }
        remoteViews.setImageViewResource(R.id.noti_imageView,UpdataWeatherUtils.setWeatherImg(weather.getNow().getCond().getCode()));
        notification.contentView = remoteViews;

        notification.when =System.currentTimeMillis();


        Intent intents = new Intent(this, SplashActivity.class);

        PendingIntent  pendingIntent = PendingIntent.getActivity(this
                ,0,intents,0);
        remoteViews.setOnClickPendingIntent(R.id.item_noti,pendingIntent);
        nm = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        nm.notify(1,notification);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

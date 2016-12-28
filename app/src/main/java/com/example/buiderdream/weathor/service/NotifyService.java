package com.example.buiderdream.weathor.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.example.buiderdream.weathor.R;
import com.example.buiderdream.weathor.activitys.AllCityListActivity;
import com.example.buiderdream.weathor.activitys.CityListMgrActivity;
import com.example.buiderdream.weathor.activitys.MainActivity;
import com.example.buiderdream.weathor.constants.ConstantUtils;
import com.example.buiderdream.weathor.entitys.HeWeather;
import com.example.buiderdream.weathor.utils.SharePreferencesUtil;
import com.example.buiderdream.weathor.utils.UpdataWeatherUtils;

/**
 * Created by Admin on 2016/12/28.
 */

public class NotifyService extends Service{
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
//        HeWeather weather = (HeWeather) SharePreferencesUtil.readObject(this, ConstantUtils.LOCATION_CITY_WEATHER);
//      当前城市
//       weather.getBasic().getCity()
        //当前城市温度
//         weather.getNow().getTmp();
//        UpdataWeatherUtils.setWeatherImg(weather.getNow().getCond().getCode());

        notification = new Notification();
        remoteViews = new RemoteViews(this.getPackageName(), R.layout.item_notify);
        remoteViews.setTextViewText(R.id.noti_textView2, "111");
        remoteViews.setTextViewText(R.id.noti_textView3,"1111111111");
        remoteViews.setImageViewResource(R.id.noti_imageView,R.drawable.w100);
        notification.contentView = remoteViews;
        notification.icon = R.drawable.btn_homeasup_default;
        notification.tickerText = "新通知";
        notification.defaults = Notification.DEFAULT_ALL;
        notification.when =System.currentTimeMillis();


        Intent intents = new Intent(this, MainActivity.class);

        PendingIntent  pendingIntent = PendingIntent.getActivity(this
                ,0,intents,0);
        remoteViews.setOnClickPendingIntent(R.id.item_noti,pendingIntent);


        nm = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        nm.notify(1,notification);

        return super.onStartCommand(intent, flags, startId);


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

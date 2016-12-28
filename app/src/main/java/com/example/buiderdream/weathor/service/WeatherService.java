package com.example.buiderdream.weathor.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.buiderdream.weathor.R;
import com.example.buiderdream.weathor.activitys.MainActivity;
import com.example.buiderdream.weathor.activitys.SplashActivity;
import com.example.buiderdream.weathor.appwidget.WeatherWidget;
import com.example.buiderdream.weathor.constants.ConstantUtils;
import com.example.buiderdream.weathor.entitys.HeWeather;
import com.example.buiderdream.weathor.utils.SharePreferencesUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class WeatherService extends Service {
    Timer timer,timer2;
    SimpleDateFormat sdf;

    public WeatherService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateTime();
            }
        }, 0, 60000);
        timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                updateView();
            }
        }, 0, 6000);
    }

    private void updateTime() {
        sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(new Date());
        RemoteViews rv = new RemoteViews(getPackageName(),R.layout.weather_widget);
        rv.setTextViewText(R.id.widget_time,time);
        AppWidgetManager awm = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName componentName = new ComponentName(getApplicationContext(),WeatherWidget.class);
        awm.updateAppWidget(componentName,rv);
    }

    private void updateView() {
        HeWeather weather = (HeWeather) SharePreferencesUtil.readObject(getApplicationContext(), ConstantUtils.LOCATION_CITY_WEATHER);
        RemoteViews rv = new RemoteViews(getPackageName(),R.layout.weather_widget);

        if (weather!=null) {
            String temp = weather.getNow().getTmp();
            String city = weather.getBasic().getCity();
            String wind = weather.getNow().getWind().getDir();
            String cond = weather.getNow().getCond().getTxt();
            String suggest = weather.getSuggestion().getDrsg().getTxt();
            String aqi = weather.getAqi().getCity().getQlty();
            int code = Integer.parseInt(weather.getNow().getCond().getCode());
            rv.setTextViewText(R.id.widget_city,city);
            rv.setTextViewText(R.id.widget_wind,wind);
            rv.setTextViewText(R.id.widget_temp,temp+"°C");
            rv.setTextViewText(R.id.widget_cond,cond);
            rv.setTextViewText(R.id.widget_suggestion,suggest);
            rv.setTextViewText(R.id.widget_aqi,"空气质量:"+aqi);
            //Log.i("-----",aqi);
            switch (code/100){
                case 1:
                    rv.setImageViewResource(R.id.widget_img,R.drawable.widget_sunny);
                    break;
                case 2:
                    rv.setImageViewResource(R.id.widget_img,R.drawable.widget_cloudy);
                    break;
                case 3:
                    rv.setImageViewResource(R.id.widget_img,R.drawable.widget_rain);
                    break;
                case 4:
                    rv.setImageViewResource(R.id.widget_img,R.drawable.widget_snow);
                    break;
                default:
                    rv.setImageViewResource(R.id.widget_img,R.drawable.widget_fog);
                    break;
            }
        }

        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,0);
        rv.setOnClickPendingIntent(R.id.widget_rela,pendingIntent);

        AppWidgetManager awm = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName componentName = new ComponentName(getApplicationContext(),WeatherWidget.class);
        awm.updateAppWidget(componentName,rv);
    }

    @Override
    public void onDestroy(  ) {
        super.onDestroy();
        timer = null;
        timer2 = null;
    }
}

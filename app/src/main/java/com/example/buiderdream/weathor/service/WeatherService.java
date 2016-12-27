package com.example.buiderdream.weathor.service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.example.buiderdream.weathor.R;
import com.example.buiderdream.weathor.appwidget.WeatherWidget;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class WeatherService extends Service {
    Timer timer;
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
                updateView();
            }
        }, 0, 1000);
    }

    private void updateView() {
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        RemoteViews rv = new RemoteViews(getPackageName(),R.layout.weather_widget);
        rv.setTextViewText(R.id.textView,time);
        AppWidgetManager awm = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName componentName = new ComponentName(getApplicationContext(),WeatherWidget.class);
        awm.updateAppWidget(componentName,rv);
    }
}

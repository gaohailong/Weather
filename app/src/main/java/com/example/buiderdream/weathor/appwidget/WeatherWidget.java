package com.example.buiderdream.weathor.appwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.example.buiderdream.weathor.service.WeatherService;

/**
 * Created by 文捷 on 2016/12/27.
 */

public class WeatherWidget extends AppWidgetProvider {
    //刷新Widget
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        //remoteViews和AppWidgetManager

    }


    //从屏幕移除
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        context.startService(new Intent(context,WeatherService.class));
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        context.stopService(new Intent(context,WeatherService.class));

    }

}

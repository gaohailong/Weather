package com.example.buiderdream.weathor.base;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.buiderdream.weathor.R;
/**
 * Created by Administrator on 2016/12/22.
 * @author 李秉龙
 */
public class BaseActivity extends Activity {
    //获取手机屏幕分辨率的类
    private static DisplayMetrics dm;
    private static Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        dm = new DisplayMetrics();
        activity = BaseActivity.this;
        getWindowManager().getDefaultDisplay().getMetrics(dm);
    }
   public static Activity currentActivity(){
       return activity;
   }
}

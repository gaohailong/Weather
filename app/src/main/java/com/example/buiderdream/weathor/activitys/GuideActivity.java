package com.example.buiderdream.weathor.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.buiderdream.weathor.R;
import com.example.buiderdream.weathor.base.BaseActivity;
import com.example.buiderdream.weathor.constants.ConstantUtils;

import java.util.ArrayList;
/**
 * Created by Administrator on 2016/12/22.
 * @author 王特
 */
public class GuideActivity extends BaseActivity {
    private ViewPager vp;
    private ArrayList<View> list;
    private GuideAdapter adapter;
    private TextView tv_immediatelyIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        adapter=new GuideAdapter();
        vp.setAdapter(adapter);
    }


    private void initView() {
        // TODO Auto-generated method stub
        vp=(ViewPager) findViewById(R.id.lead_vp);
        list=new ArrayList<View>();
        list.add(getLayoutInflater().inflate(R.layout.vp_guide1, null));
        list.add(getLayoutInflater().inflate(R.layout.vp_guide3, null));
        View vp_guide4 = getLayoutInflater().inflate(R.layout.vp_guide4, null);
        list.add(vp_guide4);
        tv_immediatelyIn = (TextView) vp_guide4.findViewById(R.id.tv_immediatelyIn);
        tv_immediatelyIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences(ConstantUtils.GUIDE_SP,MODE_PRIVATE).edit();
                editor.putBoolean("firstUse", false);
                editor.commit();
                Intent intent = new Intent(GuideActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0==arg1;
        }
        //移除
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            View view=list.get(position);
            container.removeView(view);
        }
        //添加
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            View v=list.get(position);
            container.addView(v);
            return v;
        }
            }
}

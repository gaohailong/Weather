package com.example.buiderdream.weathor.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ant.liao.GifView;
import com.example.buiderdream.weathor.R;
import com.example.buiderdream.weathor.base.BaseActivity;
import com.example.buiderdream.weathor.constants.ConstantUtils;
import com.example.buiderdream.weathor.entitys.City;
import com.example.buiderdream.weathor.utils.CommonAdapter;
import com.example.buiderdream.weathor.utils.SharePreferencesUtil;
import com.example.buiderdream.weathor.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;
/**
 * 用户添加的城市列表
 * @author 文捷
 */
public class CityListMgrActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    private ListView listView;
    private List<City> cityList;
    private CommonAdapter<City> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list_mgr);
        initView();
        //initCityNameListData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initCityNameListData();
    }

    /**
     * 从SharePreferences中读取用户收藏的城市，如果没有,默认添加北京
     */
    private void initCityNameListData() {
        cityList = (List<City>) SharePreferencesUtil.readObject(CityListMgrActivity.this, ConstantUtils.USER_COLLECT_CITY);
        if (cityList == null || cityList.size() == 0) {
            cityList = new ArrayList<>();
            City city = new City();
            city.setCityName("北京");
            cityList.add(city);
        }
        //给listView加载数据的adapter
        adapter = new CommonAdapter<City>(CityListMgrActivity.this,cityList,R.layout.item_city_list) {
            @Override
            public void convert(ViewHolder helper, final City item) {
                helper.setText(R.id.tv_cityName,item.getCityName());
                TextView tv = helper.getView(R.id.tv_delete);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cityList.remove(item);
                         adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.cityList_mgr_list);
    }

    public void addCity_click(View view){
        Intent intent = new Intent(CityListMgrActivity.this, AllCityListActivity.class);
        startActivity(intent);
    }
    public void back_click(View view) {
      stopActivity();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent();
        intent.putExtra("result", i);
        //设置回传的意图p
        setResult(1001, intent);
        stopActivity();

    }

    private void stopActivity() {
        if (cityList!=null){
            SharePreferencesUtil.saveObject(CityListMgrActivity.this, ConstantUtils.USER_COLLECT_CITY,cityList);
        }
        finish();
    }

}

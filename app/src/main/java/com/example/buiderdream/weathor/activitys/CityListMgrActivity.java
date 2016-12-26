package com.example.buiderdream.weathor.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.buiderdream.weathor.R;
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
public class CityListMgrActivity extends AppCompatActivity {
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
            public void convert(ViewHolder helper, City item) {
                helper.setText(R.id.cityName,item.getCityName());
            }
        };
        listView.setAdapter(adapter);
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.cityList_mgr_list);
    }

    public void addCity_click(View view){
        Intent intent = new Intent(CityListMgrActivity.this, AllCityListActivity.class);
        startActivity(intent);
    }
    public void back_click(View view) {
        finish();
    }
}

package com.example.buiderdream.weathor.activitys;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.liao.GifView;
import com.example.buiderdream.weathor.R;
import com.example.buiderdream.weathor.base.BaseActivity;
import com.example.buiderdream.weathor.constants.ConstantUtils;
import com.example.buiderdream.weathor.entitys.City;
import com.example.buiderdream.weathor.entitys.CityInfo;
import com.example.buiderdream.weathor.https.OkHttpClientManager;
import com.example.buiderdream.weathor.utils.CityDBHelper;
import com.example.buiderdream.weathor.utils.CommonAdapter;
import com.example.buiderdream.weathor.utils.SharePreferencesUtil;
import com.example.buiderdream.weathor.utils.ViewHolder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 网络数据所支持的全部城市列表
 *
 * @author 文捷
 */
public class AllCityListActivity extends BaseActivity {
    private EditText searchEdit;
    List<City> collecCity = new ArrayList<City>();
    private ProgressBar pro_all_city;
    CityDBHelper cityDBHelper = new CityDBHelper(AllCityListActivity.this);
    List<CityInfo>  cityInfos = new ArrayList<CityInfo>();
    private ListView listView;
    private List<String> cityNameList = new ArrayList<String>();
    private CommonAdapter<String> cityNameAdapter;
    /**
     * 异步任务结束后通知进行数据库添加操作，只在第一次执行添加
     */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what ==1){
                if (cityDBHelper.findCitys().size()==0){
                    WriteToDB((ArrayList<CityInfo>) msg.obj);
                }

            }
        }
    };

    private void WriteToDB(final ArrayList<CityInfo> cityInfo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < cityInfo.size(); i++) {
                    cityDBHelper.AddCitys(cityInfo.get(i));
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        notifyListAdapter();
                        pro_all_city.setVisibility(View.GONE);
                    }
                });
            }
        }).start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_city_list);
        initView();
        initListView();
    }

    /**
     * 加载list数据，数据库查询操作
     */
    private void initListView() {

        if (cityDBHelper.findCitys().size()==0) {
            doRequestData();
        }else{

            notifyListAdapter();
        }
    }

    private void notifyListAdapter() {
        cityInfos = cityDBHelper.findCitys();
        for (int i = 0; i < cityInfos.size(); i++) {
            cityNameList.add(cityInfos.get(i).getDistrict()+"-"+cityInfos.get(i).getCity());
        }
        cityNameAdapter = new CommonAdapter<String>(this,cityNameList,R.layout.item_city_list) {
            @Override
            public void convert(ViewHolder helper, String item) {
                TextView tv = helper.getView(R.id.tv_cityName);
                tv.setText(item);
            }
        };
        listView.setAdapter(cityNameAdapter);
        cityNameAdapter.notifyDataSetChanged();


    }

    /**
     *
     * 异步获取网络数据，数据解析并封装到List中
     */
    private void doRequestData() {
        pro_all_city.setVisibility(View.VISIBLE);

        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put("key", ConstantUtils.JUHEWEATHER_KEY);
        String url = OkHttpClientManager.attachHttpGetParams(ConstantUtils.JUHEWEATHER_URL, map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                // 将{}中视为是json对象
                JSONObject jsonObject = new JSONObject(result);
                // 获取键"result"中对应的值
                JSONArray resultArray = jsonObject
                        .getJSONArray("result");
                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject object = resultArray.getJSONObject(i);
                    // 解析数据
                    CityInfo cityInfo = new CityInfo();
                    cityInfo.setProvince(object.getString("province"));
                    cityInfo.setCity(object.getString("city"));
                    cityInfo.setDistrict(object.getString("district"));
                    cityInfos.add(cityInfo);
                }

                Message message = new Message();
                message.what = 1;
                message.obj = cityInfos;
                handler.sendMessage(message);
            }
        });
    }

    private void initView() {
        pro_all_city = (ProgressBar) findViewById(R.id.pro_all_city);

        listView = (ListView) findViewById(R.id.city_list);
        searchEdit = (EditText) findViewById(R.id.searchEdit);
        //listView设置监听事件，点击item后将对象存入sharedPreference
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String string = cityNameList.get(i);
                string = string.substring(0,string.lastIndexOf("-"));
                Toast.makeText(AllCityListActivity.this,string,Toast.LENGTH_SHORT).show();
                saveCitys(string);
            }
        });
    }

    /**
     * 保存用户点击的item城市到SharePreferences，已有的城市不再添加
     * @param string
     *
     */
    public void saveCitys(String string){
        collecCity = (ArrayList<City>) SharePreferencesUtil.readObject(AllCityListActivity.this, ConstantUtils.USER_COLLECT_CITY);
        City city = new City();
        city.setCityName(string);
        for(int i = 0;i<collecCity.size();i++){
            if(string.equals(collecCity.get(i).getCityName())){
                finish();
                return;
            }
        }
        collecCity.add(city);
        SharePreferencesUtil.saveObject(AllCityListActivity.this,ConstantUtils.USER_COLLECT_CITY,collecCity);
        finish();
    }

    public void back_click(View view) {
        finish();
    }

    /**
     * 搜索按钮触发列表的相应改变
     * @param v
     */
    public void search_click(View v) {
        // 搜索
        String searchCity = searchEdit.getText().toString();
        cityInfos = cityDBHelper.findCitys();

        cityNameList = new ArrayList<String>();
        for (int i = 0; i < cityInfos.size(); i++) {
            if (searchCity.equals(cityInfos.get(i).getCity())||searchCity.equals(cityInfos.get(i).getDistrict())
                    ||cityInfos.get(i).getDistrict().contains(searchCity)||cityInfos.get(i).getCity().contains(searchCity)) {
                cityNameList.add(cityInfos.get(i).getDistrict()+"-"+cityInfos.get(i).getCity());
            }
        }
        cityNameAdapter = new CommonAdapter<String>(getApplicationContext(),cityNameList,R.layout.item_city_list) {
            @Override
            public void convert(ViewHolder helper, String item) {
                TextView tv = helper.getView(R.id.tv_cityName);
                tv.setText(item);
            }
        };
        listView.setAdapter(cityNameAdapter);
        cityNameAdapter.notifyDataSetChanged();
    }
}

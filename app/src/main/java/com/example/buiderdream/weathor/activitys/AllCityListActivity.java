package com.example.buiderdream.weathor.activitys;

import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.buiderdream.weathor.R;
import com.example.buiderdream.weathor.constants.ConstantUtils;
import com.example.buiderdream.weathor.entitys.City;
import com.example.buiderdream.weathor.entitys.CityInfo;
import com.example.buiderdream.weathor.https.OkHttpClientManager;
import com.example.buiderdream.weathor.utils.CityDBHelper;
import com.example.buiderdream.weathor.utils.SharePreferencesUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class AllCityListActivity extends AppCompatActivity {
    File dbFile = new File("data" + File.separator + "data" + File.separator
            + "com.example.myweather" + File.separator + "databases"
            + File.separator + "citys.db");

    private ListView listView;
    private EditText searchEdit;
    private List<String> cityNameList = new ArrayList<String>();
    private ArrayAdapter<String> cityNameAdapter;
    List<City> collecCity = new ArrayList<City>();

    CityDBHelper cityDB = new CityDBHelper(AllCityListActivity.this);
    List<CityInfo> cityInfos = new ArrayList<CityInfo>();
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what ==1){
                if (cityDB.findCitys().size()==0){
                    for (int i = 0; i < cityInfos.size(); i++) {
                        cityDB.AddCitys(cityInfos.get(i));
                    }
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_city_list);
        initView();
        initListView();
    }
    private void initListView() {

        if (!dbFile.exists()) {
            doRequestData();

        }
        cityInfos = cityDB.findCitys();
        for (int i = 0; i < cityInfos.size(); i++) {
            cityNameList.add(cityInfos.get(i).getDistrict()+"-"+cityInfos.get(i).getCity());
        }
        cityNameAdapter = new ArrayAdapter<String>(AllCityListActivity.this, android.R.layout.simple_list_item_1, cityNameList);
        listView.setAdapter(cityNameAdapter);
        cityNameAdapter.notifyDataSetChanged();


    }
    private void doRequestData() {
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
    public void saveCitys(String string){
        collecCity = (List<City>) SharePreferencesUtil.readObject(AllCityListActivity.this, ConstantUtils.USER_COLLECT_CITY);
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

    public void search_click(View v) {
        // 搜索
        Toast.makeText(AllCityListActivity.this, "------------", Toast.LENGTH_SHORT).show();
        String searchCity = searchEdit.getText().toString();
        //if (searchCity!=""||searchCity.length()!=0||!searchCity.isEmpty()) {
        cityInfos = cityDB.findCitys();
        Toast.makeText(AllCityListActivity.this, searchCity, Toast.LENGTH_SHORT).show();

        cityNameList = new ArrayList<String>();
        for (int i = 0; i < cityInfos.size(); i++) {
            if (searchCity.equals(cityInfos.get(i).getCity())||searchCity.equals(cityInfos.get(i).getDistrict())
                    ||cityInfos.get(i).getDistrict().contains(searchCity)||cityInfos.get(i).getCity().contains(searchCity)) {
                cityNameList.add(cityInfos.get(i).getDistrict()+"-"+cityInfos.get(i).getCity());
            }
        }
        cityNameAdapter = new ArrayAdapter<String>(AllCityListActivity.this, android.R.layout.simple_list_item_1, cityNameList);
        listView.setAdapter(cityNameAdapter);
        cityNameAdapter.notifyDataSetChanged();
        //}


    }
}

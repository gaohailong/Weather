package com.example.buiderdream.weathor.activitys;

import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.buiderdream.weathor.R;
import com.example.buiderdream.weathor.entitys.CityInfo;
import com.example.buiderdream.weathor.https.DoNetDataUtils;
import com.example.buiderdream.weathor.utils.CityDBHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AllCityListActivity extends AppCompatActivity {
    public static String CITY_LIST = "http://v.juhe.cn/weather/citys?key=924305ff7b02119b1eb50fee155a4a67";
    File dbFile = new File("data" + File.separator + "data" + File.separator
            + "com.example.myweather" + File.separator + "databases"
            + File.separator + "citys.db");

    private ListView listView;
    private EditText searchEdit;
    private SQLiteDatabase db;
    private List<String> cityNameList = new ArrayList<String>();
    private ArrayAdapter<String> cityNameAdapter;

    CityDBHelper cityDB = new CityDBHelper(AllCityListActivity.this);
    List<CityInfo> cityInfos = new ArrayList<CityInfo>();

    Handler handler=new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(msg.what==1){
                cityInfos = (List<CityInfo>) msg.obj;
                Log.i("--11111-", cityInfos.toString());
                for (int i = 0; i < cityInfos.size(); i++) {
                    cityDB.AddCitys(cityInfos.get(i));
                }
                cityInfos = cityDB.findCitys();
                for (int i = 0; i < cityInfos.size(); i++) {
                    cityNameList.add(cityInfos.get(i).getDistrict()+"-"+cityInfos.get(i).getCity());
                }
                cityNameAdapter.notifyDataSetChanged();

            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_city_list);
    }
    private void initListView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!dbFile.exists()) {
                    DoNetDataUtils.getNetData(handler, CITY_LIST);
                }
                cityInfos = cityDB.findCitys();
                for (int i = 0; i < cityInfos.size(); i++) {
                    cityNameList.add(cityInfos.get(i).getDistrict()+"-"+cityInfos.get(i).getCity());

                }
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        cityNameAdapter = new ArrayAdapter<String>(AllCityListActivity.this, android.R.layout.simple_list_item_1, cityNameList);
                        listView.setAdapter(cityNameAdapter);
                        cityNameAdapter.notifyDataSetChanged();

                    }
                });
            }

        }).start();
    }


    private void initView() {
        listView = (ListView) findViewById(R.id.city_list);
        searchEdit = (EditText) findViewById(R.id.searchEdit);

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

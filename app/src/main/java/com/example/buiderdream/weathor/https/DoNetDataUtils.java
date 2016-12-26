package com.example.buiderdream.weathor.https;

import android.os.Handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import org.apache.http.HttpClientConnection;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//
//
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.util.Log;
//import com.example.buiderdream.weathor.entitys.CityInfo;

public class DoNetDataUtils {
	private static String result = new String();

	public static void getNetData(final Handler handler, final String url){
		//Looper.prepare();

		/*
		new Thread(new Runnable() {
			
			@Override
			public void run() {

				HttpClient client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				try {
					HttpResponse response = client.execute(httpGet);
					if (response.getStatusLine().getStatusCode()==200) {
						HttpEntity entity = response.getEntity();
						result = EntityUtils.toString(entity);
						
						List<CityInfo> cityList = new ArrayList<CityInfo>(); 
						JSONArray resultArray;
						try {
							JSONObject resultoObject = new JSONObject(result);
							resultArray = resultoObject.getJSONArray("result");
							for (int i = 0; i < resultArray.length(); i++) {
								JSONObject object = resultArray.getJSONObject(i);
								CityInfo cityInfo = new CityInfo();
								cityInfo.setProvince(object.getString("province"));
								cityInfo.setCity(object.getString("city"));
								cityInfo.setDistrict(object.getString("district"));
								cityList.add(cityInfo);
							}
							Message msg=new Message();
							msg.what=1;
							msg.obj=cityList;
							handler.sendMessage(msg);
							Log.i("---", cityList.toString());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		*/
	}

}

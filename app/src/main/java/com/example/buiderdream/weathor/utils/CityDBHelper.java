package com.example.buiderdream.weathor.utils;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.buiderdream.weathor.entitys.CityInfo;

public class CityDBHelper extends SQLiteOpenHelper {

	SQLiteDatabase db;
	private List<CityInfo> list;
	
	public CityDBHelper(Context context) {
		super(context, "citys.db", null, 1);		 
	}
	
		 
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "create table citys(id integer primary key autoincrement,province varchar(10),city varchar(10),district varchar(10))";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if (oldVersion<newVersion) {
			db.execSQL("drop table citys if exits");
			db.execSQL( "create table citys(id integer primary key autoincrement,province varchar(10),city varchar(10),district varchar(10))");
		}
		
	}
	//���
	public void AddCitys(CityInfo cityInfo){
		db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("province", cityInfo.getProvince());
		values.put("city", cityInfo.getCity());
		values.put("district", cityInfo.getDistrict());
		db.insert("citys", null, values);				
	}
	//��ѯ
	public List<CityInfo> findCitys()
	{
		db = getReadableDatabase();
		list = new ArrayList<CityInfo>();
		Cursor cursor = db.query("citys", null, null, null, null, null, null);
		while (cursor.moveToNext()) {
			 
			String province = cursor.getString(1);
			String city = cursor.getString(2);
			String district = cursor.getString(3);
			list.add(new CityInfo(province, city, district));
		}
		return list;
	}

}

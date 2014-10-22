package com.royal.tudeuda.db;

import com.royal.tudeuda.manejador.ManejadorDeuda;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	public static String DB_NAME = "tudeuda.sqlite";
	public static int DB_SCHEMA_VERSION = 1;
	
	public DBHelper(Context context){
		super(context, DB_NAME, null, DB_SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(ManejadorDeuda.CREATE_TABLE_DEUDA);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}

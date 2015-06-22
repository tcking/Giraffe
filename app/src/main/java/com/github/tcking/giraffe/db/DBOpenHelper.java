package com.github.tcking.giraffe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.tcking.giraffe.model.DaoMaster;

public class DBOpenHelper extends SQLiteOpenHelper {
	

	public DBOpenHelper(Context context, String name, CursorFactory factory,int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("greenDAO", "Creating tables for schema version " + DaoMaster.SCHEMA_VERSION);
		DaoMaster.createAllTables(db, false);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersoin, int newVersion) {
		new DBUpgradeManager(db).upgrade(oldVersoin,newVersion);
	}

}

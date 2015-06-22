/**
 * 
 */
package com.github.tcking.giraffe.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * 数据库升级管理器
 * <pre>
 * 流程：
 * 1.编写实现类，实现DBUpgrader接口
 * 2.在registerUpgraders中注册所有的Upgrader
 * </pre>
 * @author tangchao
 *
 */
public class DBUpgradeManager {
	private SQLiteDatabase db;
	/**
	 * all upgrader instances,sorted by version desc
	 */
	private final NavigableMap<Integer,DBUpgrader> upgraders=new TreeMap<Integer,DBUpgrader>();
	
	public DBUpgradeManager(SQLiteDatabase db) {
		this.db=db;
        registerUpgraders();
    }

    /**
     * important:数据库的升级程序需要在这注册 (don't care about the order)
     */
    private void registerUpgraders() {
//        registerUpgrader(new DBUpgrader2());
    }


    /*
     * 注册db升级器
     * @param upgrader
     */
	private void registerUpgrader(DBUpgrader upgrader){
		upgraders.put(upgrader.targetVersion(), upgrader);
	}
	
	/**
	 * 将数据库升级到targetVersion
	 * @param oldVersoin
	 * @param targetVersion
	 */
	public void upgrade(int oldVersoin, int targetVersion) {
		DBUpgradeHelper.upgradeSchame(db);//升级表结构
		SortedMap<Integer, DBUpgrader> subMap = upgraders.subMap(oldVersoin, false, targetVersion, true);
		Iterator<Entry<Integer, DBUpgrader>> iterator = subMap.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<Integer, DBUpgrader> next = iterator.next();
			next.getValue().upgrade(db);
		}
	}

}

/**
 * 
 */
package com.github.tcking.giraffe.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @author tangchao
 *
 */
public interface DBUpgrader {
	
	/**
	 * 升级完成后的目标数据库版本
	 * @return
	 */
	public Integer targetVersion();
	
	/**
	 * 升级
	 * @param db
	 */
	public  void upgrade(SQLiteDatabase db);
}

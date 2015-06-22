package com.github.tcking.giraffe.db;

import android.database.sqlite.SQLiteDatabase;

import com.github.tcking.giraffe.core.CoreApp;
import com.github.tcking.giraffe.model.DaoMaster;
import com.github.tcking.giraffe.model.DaoSession;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理dao
 * Created by tc(mytcking@gmail.com) on 15/6/22.
 */
public class DBManager {
    private static Map<String, DBManager> dbs = new HashMap<String, DBManager>();
    private DaoSession daoSession;

    public DaoSession getDaoSession() {
        return daoSession;
    }

    private DBManager(String dbName) {
        DBOpenHelper dbOpenHelper = new DBOpenHelper(CoreApp.getInstance(), dbName + ".db", null, DaoMaster.SCHEMA_VERSION);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        daoSession = new DaoMaster(db).newSession();
        dbs.put(dbName, this);
    }

    /**
     * 获取全局数据库对象
     * @return
     */
    public static DBManager getGlobalInstance() {
        return  getInstance("common");
    }

    /**
     * 获取一个DBmanger对象
     *
     * @param dbName 一般为用户的id(一个用户一个数据库)
     * @return
     */
    public static DBManager getInstance(String dbName) {
        synchronized (dbs) {
            DBManager dbManager = dbs.get(dbName);
            if (dbManager == null) {
                dbManager = new DBManager(dbName);
            }
            return dbManager;
        }
    }


}

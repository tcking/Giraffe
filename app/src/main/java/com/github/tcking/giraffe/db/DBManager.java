package com.github.tcking.giraffe.db;

import android.database.sqlite.SQLiteDatabase;

import com.github.tcking.giraffe.core.CoreApp;
import com.github.tcking.giraffe.manager.AppSecurityManager;
import com.github.tcking.giraffe.model.DaoMaster;
import com.github.tcking.giraffe.model.DaoSession;
import com.github.tcking.giraffe.model.KVTable;

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


    /**
     * 获取当前用户的数据库实例
     * @return
     */
    public static DBManager getInstance() {
        String userId = AppSecurityManager.currentUserId();
        if (userId ==null) {
            throw new RuntimeException("is user login?");
        }
        return getInstance(userId);
    }


    /**
     * 根据KVTable的key或者value
     *
     * @param key
     * @return
     */
    public String getV(String key) {
        KVTable kv = daoSession.getKVTableDao().load(key);
        if (kv != null) {
            return kv.getValue();
        }
        return null;
    }

    /**
     * 删除键值队
     *
     * @param key
     */
    public void removeV(String key) {
        KVTable kv = daoSession.getKVTableDao().load(key);
        if (kv != null) {
            daoSession.getKVTableDao().delete(kv);
        }
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public boolean updateKV(String key, String value) {
        boolean existed = false;
        KVTable kv = daoSession.getKVTableDao().load(key);
        if (kv == null) {
            kv = new KVTable(key, value);
            existed = true;
        } else {
            kv.setValue(value);
        }
        daoSession.getKVTableDao().insertOrReplace(kv);
        return existed;
    }

    public void updateKVTable(String key, String value) {
        daoSession.getKVTableDao().insertOrReplace(new KVTable(key, value));
    }

}

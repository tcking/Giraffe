package com.github.tcking.giraffe.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.csi.jf.mobile.core.Log;
import com.csi.jf.mobile.model.DaoMaster;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoException;
import de.greenrobot.dao.Property;

public class DBUpgradeHelper {
	
	/**
	 * 升级数据库结构
	 * @param db
	 */
	public static void upgradeSchame(SQLiteDatabase db) {
    	Iterator<Class<? extends AbstractDao<?, ?>>> it = DaoMaster.DAOS.iterator();
    	while(it.hasNext()){
    		Class<? extends AbstractDao<?, ?>> next = it.next();
    		alterTable(db,next);
    	}
	}
    

	/**
     * 仅解决添加字段
     * @param db
     * @param table
     * @return
     */
    private static boolean alterTable(SQLiteDatabase db, Class<? extends AbstractDao<?, ?>> daoClass) {
    	boolean altered = false;
		Cursor cursor = null;
		try {
			String tableName = ((String) daoClass.getField("TABLENAME").get(null));
			String sql = "pragma table_info (" + tableName + ")";
			cursor = db.rawQuery(sql, null);
			if (cursor != null && cursor.getCount()>0) {
				// 查找表名
				// 查找列名和对应的类型 如ChatMsg在数据中姓名的列名是nmae, 类型是string
				Property[] properties = reflectProperties(daoClass);
				String[] newColumns = new String[properties.length];
				String[] newTypes = new String[properties.length];//table.getPkColumns();
				for (int j = 0; j < newTypes.length; j++) {
					StringBuffer typeSb = new StringBuffer();
					typeSb.append(" ");
					typeSb.append(properties[j].type.getSimpleName());
					newTypes[j] = typeSb.toString();

					newColumns[j] = properties[j].columnName;
				}


				StringBuffer sbAdd = new StringBuffer(" ADD ");
				String[] oldColumns = new String[cursor.getCount()];
				int i = 0;
				while (cursor.moveToNext()) {
					oldColumns[i] = cursor.getString(cursor.getColumnIndex("name"));
					i++;
				}
				for (i = 0; i < newColumns.length; i++) {
					String newCol = newColumns[i];
					sbAdd.setLength(5);
					boolean has = false;
					for (int j = 0; j < oldColumns.length; j++) {
						String oldCol = oldColumns[j];
						if (oldCol.equals(newCol)) {
							has = true;
							break;
						}
					}
					// 比较新旧列，旧列中不包含的添加到表结构中来
					if (!has) {
						sbAdd.append(newCol).append(newTypes[i]);
						db.execSQL("ALTER TABLE " + tableName
										+ sbAdd.toString()
						);
						altered = true;
					}
				}
			}
		} catch (Exception e1) {
            Log.e("DBUpgradeHelper.alterTable error", e1);
		}finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		
		return altered;
	}
    
    
    private static Property[] reflectProperties(Class<? extends AbstractDao<?, ?>> daoClass) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException
    {
      Class propertiesClass = Class.forName(daoClass.getName() + "$Properties");
      Field[] fields = propertiesClass.getDeclaredFields();
      ArrayList<Property> propertyList = new ArrayList<Property>();
      for (Field field : fields)
      {
        if ((field.getModifiers() & 0x9) == 9) {
          Object fieldValue = field.get(null);
          if ((fieldValue instanceof Property)) {
            propertyList.add((Property)fieldValue);
          }
        }
      }

      Property[] properties = new Property[propertyList.size()];
      for (Property property : propertyList) {
        if (properties[property.ordinal] != null) {
          throw new DaoException("Duplicate property ordinals");
        }
        properties[property.ordinal] = property;
      }
      return properties;
    }
}
package com.github.tcking.giraffe.dao;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * GreenDAO的生成器
 * 1.如有改变Entity结构需要将version+1
 * 2.srcDir为APP工程的src目录
 * 3.如果对于的Entity没有改变，可以调用setSkipGeneration(true)，让其不生成Entity的java文件
 * @author tangchao
 *
 */
public class DAOGenerator {
	private static int version=1; //如有改变Entity结构需要将version+1
	private static String srcDir="../app/src/main/java";

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Schema schema = new Schema(version, "com.github.tcking.giraffe.model");
		schema.enableKeepSectionsByDefault();//enable Keep sections
		schema.enableActiveEntitiesByDefault();
		
		Entity kvTable = schema.addEntity("KVTable");
        kvTable.addStringProperty("key").primaryKey();
        kvTable.addStringProperty("value");
        kvTable.implementsSerializable();
        kvTable.setSkipGeneration(false);
		
        DaoGenerator daoGenerator = new DaoGenerator();
		try {
			daoGenerator.generateAll(schema, srcDir);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
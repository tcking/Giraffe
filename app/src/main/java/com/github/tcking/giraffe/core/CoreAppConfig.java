/**
 * 
 */
package com.github.tcking.giraffe.core;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.github.tcking.example.App;
import com.github.tcking.giraffe.event.AppInitEvent;
import com.github.tcking.giraffe.event.ConfigLoadEvent;
import com.github.tcking.giraffe.helper.PlaceHolderProperties;
import com.github.tcking.giraffe.manager.BaseManager;
import com.google.code.microlog4android.config.PropertyConfigurator;

import java.io.File;
import java.io.InputStream;

import de.greenrobot.event.EventBus;

/**
 * app top level config
 * @author tangchao
 *
 */
public abstract class CoreAppConfig extends BaseManager{

	/**
	 * 默认加载配置文件的名称:assets/config.properties
	 */
	public static final String configureFileName= "config.properties";

	protected static String mode;//当前app所在的环境[dev,test,rc,production]
	protected static boolean strictMode;//是否开启严格模式
	protected static boolean isSqlDebug;//是否打开sql的debug（打印出sql语句）
    protected static String appHome="/giraffe";
    protected static String appVersion;
    protected static int appVersionCode;

    /**
	 * 是否为严格模式，用于在开发阶段查找问题
	 * @return
	 */
	public static boolean isStrictMode() {
		return strictMode;
	}

	/**
	 * 是否为生产环境
	 * @return
	 */
	public static boolean isProductionMode() {
		return "production".equals(mode);
	}



	/**
	 * 从配置文件读取配置并应用
	 * @param app
	 */
	public static void configure(Context context) {
		Resources resources = context.getResources();
		AssetManager assetManager = resources.getAssets();
		try {
			InputStream inputStream = assetManager.open(configureFileName);
			PlaceHolderProperties properties = new PlaceHolderProperties();
			properties.load(inputStream);
			mode=properties.getProperty("mode", "dev");
            appHome = properties.getProperty("app_home", appHome);
            strictMode="true".equals(properties.getProperty(mode + ".strictMode", "false"));
			isSqlDebug="true".equals(properties.getProperty(mode+".isSqlDebug", "false"));
            PropertyConfigurator.getConfigurator(context).configure(properties);//配置日志

            PackageManager pm = App.getInstance().getPackageManager();
            PackageInfo pi;
            try {
                pi = pm.getPackageInfo(App.getInstance().getPackageName(), 0);
                appVersion = pi.versionName;
                appVersionCode=pi.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("Config.configure error", e);
            }

            EventBus.getDefault().postSticky(new ConfigLoadEvent(properties));
		} catch (Exception e) {
            android.util.Log.e("CoreAppConfig", "configure error", e);
        }

		if(isProductionMode()){//生产环境下强制关闭debug
			strictMode=false;
			isSqlDebug=false;
		}
//		QueryBuilder.LOG_SQL=isSqlDebug;
//		QueryBuilder.LOG_VALUES = isSqlDebug;
	}



    @Override
    public void onEvent(AppInitEvent event) {
        super.onEvent(event);
        EventBus.getDefault().removeStickyEvent(ConfigLoadEvent.class);
    }
}

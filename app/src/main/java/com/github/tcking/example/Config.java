package com.github.tcking.example;

import android.os.Environment;
import com.github.tcking.giraffe.core.CoreAppConfig;

import java.util.Properties;

/**
 * Created by tc(mytcking@gmail.com) on 15/8/2.
 */
public class Config extends CoreAppConfig {

    private static String imageCache=appHome+"/cache/images";

    public static void configure(Properties properties) {
        imageCache=properties.getProperty("image_cache",imageCache);
    }

    public static String getImageCache() {
        return imageCache;
    }

    public static String getPicCacheDir(){
        return getAppRootDir()+"/piccache";
    }

    /**
     * 获取应文件夹的根目录(不同的环境区分开来)
     * @return
     */
    public static String getAppRootDir() {
        String dir=appHome;
        if (!isProductionMode()) {
            dir=dir+"-"+mode;
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath()+dir;
    }


}

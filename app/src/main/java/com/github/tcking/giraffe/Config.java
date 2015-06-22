package com.github.tcking.giraffe;

import java.util.Properties;

/**
 * Created by tc(mytcking@gmail.com) on 15/6/21.
 */
public class Config {
    private static String appHome ="/giraffe";//app work dir

    /**
     * image cache dir
     * @return
     */
    public static String getImageCache() {
        return imageCache;
    }

    private static String imageCache=appHome+"/cache/images";

    public static void configure(Properties properties) {
        appHome = properties.getProperty("app_home", appHome);
        imageCache=properties.getProperty("image_cache",imageCache);
    }
}

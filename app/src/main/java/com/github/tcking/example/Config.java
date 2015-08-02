package com.github.tcking.example;

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
}

package com.github.tcking.example.manager;

import com.androidquery.callback.AbstractAjaxCallback;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.BitmapAjaxCallback;
import com.androidquery.callback.ProxyHandle;
import com.androidquery.util.AQUtility;
import com.github.tcking.example.Config;
import com.github.tcking.giraffe.core.CoreAppConfig;
import com.github.tcking.giraffe.event.ConfigLoadEvent;
import com.github.tcking.giraffe.helper.FileHelper;
import java.io.File;
import java.net.Proxy;
import java.util.Map;
import org.apache.http.HttpRequest;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * 管理配置
 * 1.加载配置
 * 2.配置方面的初始化，例如创建需要的文件夹等
 * Created by tc(mytcking@gmail.com) on 15/6/21.
 */
public class AppConfigManager extends CoreAppConfig {

    public void onEvent(ConfigLoadEvent event){
        Config.configure(event.getProperties());
        FileHelper.createDir(Config.getImageCache());
        configAQeury();
    }

    private void configAQeury() {
        AjaxCallback.setNetworkLimit(8);
        //set the max number of icons (image width <= 50) to be cached in memory, default is 20
        BitmapAjaxCallback.setIconCacheLimit(20);
        //set the max number of images (image width > 50) to be cached in memory, default is 20
        BitmapAjaxCallback.setCacheLimit(40);
        //set the max size of an image to be cached in memory, default is 1600 pixels (ie. 400x400)
        BitmapAjaxCallback.setPixelLimit(400 * 400);
        //set the max size of the memory cache, default is 1M pixels (4MB)
        BitmapAjaxCallback.setMaxPixelLimit(2000000);

        File cacheDir = new File(Config.getPicCacheDir());
        AbstractAjaxCallback.setProxyHandle(new ProxyHandle() {
            @Override
            public void applyProxy(AbstractAjaxCallback<?, ?> cb, HttpRequest request, DefaultHttpClient client) {
                Map<String, String> securityData = AppSecurityManager.getSecurityData();
                if (securityData!=null) {//抓取图片的时候也增加header
                    cb.headers(securityData);
                }
            }

            @Override
            public Proxy makeProxy(AbstractAjaxCallback<?, ?> cb) {
                return null;
            }
        });
        AQUtility.setCacheDir(cacheDir);
    }

}

package com.github.tcking.giraffe.manager;

import com.github.tcking.giraffe.Config;
import com.github.tcking.giraffe.event.ConfigLoadEvent;

import java.io.File;

import de.greenrobot.event.EventBus;

/**
 * 管理配置
 * 1.加载配置
 * 2.配置方面的初始化，例如创建需要的文件夹等
 * Created by tc(mytcking@gmail.com) on 15/6/21.
 */
public class ConfigManager extends BaseManager {

    public void onEvent(ConfigLoadEvent event){
        Config.configure(event.getProperties());
        EventBus.getDefault().removeStickyEvent(ConfigLoadEvent.class);
        createDir(Config.getImageCache());
    }

    private void createDir(String dir) {
        File d = new File(dir);
        if (!d.exists()) {
            d.mkdirs();
        }
    }
}

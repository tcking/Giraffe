package com.github.tcking.example.manager;

import com.github.tcking.example.Config;
import com.github.tcking.giraffe.core.CoreAppConfig;
import com.github.tcking.giraffe.event.ConfigLoadEvent;
import com.github.tcking.giraffe.helper.FileHelper;


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
    }
}

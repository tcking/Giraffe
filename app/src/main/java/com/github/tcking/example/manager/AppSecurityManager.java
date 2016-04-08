package com.github.tcking.example.manager;

import com.github.tcking.giraffe.manager.CoreSecurityManager;
import java.util.Map;

/**
 * Created by tc(mytcking@gmail.com) on 15/8/5.
 */
public class AppSecurityManager extends CoreSecurityManager {

    /**
     * 网络请求中需要追加到header中的数据
     * @return
     */
    public static Map<String, String> getSecurityData() {
        return null;
    }
}

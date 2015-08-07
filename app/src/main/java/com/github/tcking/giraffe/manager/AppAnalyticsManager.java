package com.github.tcking.giraffe.manager;

import android.content.Context;

/**
 * Created by tc(mytcking@gmail.com) on 15/8/5.
 */
public class AppAnalyticsManager extends BaseManager {
    private static AppAnalyticsManager instance;


    public AppAnalyticsManager() {
        instance=this;
    }


    public void onPageStart(String clazz) {

    }

    public void onPageEnd(String clazz) {

    }
    public void onResume(Context context) {
    }
    public void onPause(Context context) {

    }

    public static AppAnalyticsManager getInstance() {
        if (instance == null) {
            instance=new AppAnalyticsManager();
        }
        return instance;
    }
}

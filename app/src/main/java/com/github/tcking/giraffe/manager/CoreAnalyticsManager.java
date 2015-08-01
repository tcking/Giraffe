package com.github.tcking.giraffe.manager;

import android.content.Context;

/**
 * Created by tc(mytcking@gmail.com) on 15/7/10.
 */
public class CoreAnalyticsManager extends BaseManager {
    public CoreAnalyticsManager() {
        instance=this;
    }

    private static CoreAnalyticsManager instance;


    public static CoreAnalyticsManager getInstance() {
        return instance;
    }

    public void onPageStart(String clazz) {

    }

    public void onPageEnd(String clazz) {

    }
    public void onResume(Context context) {
    }
    public void onPause(Context context) {

    }
}

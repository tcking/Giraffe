package com.github.tcking.giraffe.event;

import android.content.Context;

/**
 * app完成初始化时发布的事件
 * Created by tc(mytcking@gmail.com) on 15/6/16.
 */
public class AppInitEvent extends BaseEvent{
    private Context context;

    public Context getContext() {
        return context;
    }

    public AppInitEvent(Context context) {
        this.context = context;
    }
}

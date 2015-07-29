/**
 * 
 */
package com.github.tcking.giraffe.manager;

import android.content.Context;

import com.github.tcking.giraffe.event.AppInitEvent;

import de.greenrobot.event.EventBus;

/**
 * 所有业务处理都应该继承此类：
 * 1.并且必须有一个空参的构造函数
 * 2.如果需要注册到事件总线，在preferences.xml中注册manager，注册的次序就是事件发布的次序
 * @author tangchao
 *
 */
public abstract class BaseManager implements Manager{
	private boolean registerToEventBus =true;


    /**
	 * 是否注册到事件总线
	 * @return
	 */
	protected final boolean isRegisterToEventBus() {
		return registerToEventBus;
	}

	/**
	 * true表示注册到事件总线
	 * @param registerToEventBus
	 */
	protected final void setRegisterToEventBus(boolean registerToEventBus) {
		this.registerToEventBus = registerToEventBus;
	}

	/**
	 * 在应用启动的时候注册到事件总线
	 */
	public void onAppStart(Context context){
		if (registerToEventBus) {
			EventBus.getDefault().register(this);
		}
	}
	
	/**
	 * for register to eventbus,
	 *  do noting
	 * @param event
	 */
	public void onEvent(AppInitEvent event){
		//do nothing
	}
	
}

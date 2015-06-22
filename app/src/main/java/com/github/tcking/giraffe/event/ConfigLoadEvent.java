/**
 * 
 */
package com.github.tcking.giraffe.event;

import java.util.Properties;

/**
 * 配置文件加载完成后驱动的事件
 * @author tc
 *
 */
public class ConfigLoadEvent extends BaseEvent {

	private Properties properties;

	public ConfigLoadEvent(Properties properties) {
		this.properties=properties;
	}

	public Properties getProperties() {
		return properties;
	}
	
}

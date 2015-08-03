/**
 * 
 */
package com.github.tcking.giraffe.helper;


import android.net.Uri;
import android.text.TextUtils;

import com.github.tcking.giraffe.core.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 和web相关的工具类
 * @author tangchao
 */
public class WebHelper {
	/**true表示为url
	 * @param input
	 * @return
	 */
	public static boolean isURL(String input){
		if (TextUtils.isEmpty(input)) {
			return false;
		}
		input = input.toLowerCase();
		return input.startsWith("http://") || input.startsWith("https://");
	}

    /**
     * 获取url中的参数
     * @param url
     * @param key
     * @return
     */
	public static String getParameter(String url, String key) {
		return getParameter(url,key,null);
	}
	
	public static String getParameter(String url, String key,String defaultValue) {
		if (TextUtils.isEmpty(url)) {
			return defaultValue;
		}
		Uri uri = Uri.parse(url);
		final String queryParameter = uri.getQueryParameter(key);
		return queryParameter!=null?queryParameter:defaultValue;
	}

	public static String URLEncode(String account) {
		if (TextUtils.isEmpty(account)) {
			return account;
		}
		try {
			return URLEncoder.encode(account, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.e("WebUtil.URLEncode error", e);
			return URLEncoder.encode(account);
		}
	}

    /**
     * 在URL后面增加参数
     * @param url
     * @param parameters
     * @return
     */
    public static String appendParameter(String url, Map<String,String> parameters) {
		if (TextUtils.isEmpty(url) || parameters==null || parameters.size()==0) {
			return url;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(url);
		if (!url.contains("?")) {
			sb.append("?");
		}
		Iterator<Map.Entry<String, String>> it = parameters.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> next = it.next();
            String value = next.getValue();
            String key = next.getKey();
            if (!TextUtils.isEmpty(value) && !TextUtils.isEmpty(key)) {
                sb.append("&").append(key).append("=").append(value);
            }
		}
		return sb.toString().replaceAll("&&","&").replaceAll("\\?&","\\?");
	}

	/**
	 * 获取参数
	 * @param url
	 * @param component
	 * @return
	 */
	public static List<String> getParameters(String url, String component) {
		Uri uri = Uri.parse(url);
		return uri.getQueryParameters(component);
	}

    public static String safe(String nickname) {
        return nickname==null?"":nickname;
    }
}

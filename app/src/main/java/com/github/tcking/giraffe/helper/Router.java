package com.github.tcking.giraffe.helper;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.github.tcking.example.activity.BaseActivity;
import com.github.tcking.example.activity.MainActivity;
import com.github.tcking.giraffe.core.CoreApp;
import com.github.tcking.giraffe.core.CoreBaseActivity;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * 路由处理app的url，(Router是页面之间的解耦，所有页面的跳转尽量通过Router来做)，例如：
 * 打开主界面: giraffe://main
 * 打开登录界面: giraffe://login
 * 关闭当前页并打开主界面: giraffe://../main  (类似于文件访问一样，..表示返回上级目录，也就是关闭当前页)
 * </pre>
 * Created by tc(mytcking@gmail.com) on 15/8/13.
 */
public class Router {
    /**
     * app所属url前缀
     */
    public static final String APP_SCHEMA = "giraffe://";
    public static Pattern p = Pattern.compile(APP_SCHEMA+"(\\w+)\\??(\\w*)");

    /**
     * 判断指定的url是否为component所属的
     *
     * @param url
     * @param componentURL
     * @return
     */
    public static boolean isComponentURL(String url, String componentURL) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        String component = APP_SCHEMA + componentURL;
        final int t = url.indexOf("?");
        if (t < 0) {
            return component.equals(url);
        } else {
            return component.equals(url.subSequence(0, t));
        }
    }

    /**
     * 打开页面（component）
     * @param url
     * @param parameters 传递给目标页面的参数 key,value,key,value的形式
     * @return
     */
    public boolean go(String url,Object... parameters) {
        if (WebHelper.isURL(url)) {
            //open WebView
            return true;
        }
        boolean handled=false;
        if (url.startsWith(APP_SCHEMA)) {
            String path = url.substring(APP_SCHEMA.length());
            String[] paths = path.split("/");
            for (String s : paths) {
                handled=goNative(APP_SCHEMA+s,parameters) || handled;
            }
        }
        return handled;
    }

    private boolean goNative(String url,Object... parameters) {
        boolean handled=true;
        String componentName=parseComponentName(url);
        if ("..".equals(componentName)) {
            CoreBaseActivity.finishTop();
        } else if("main".equals(componentName)){
            startActivity(createIntent(MainActivity.class,url,parameters));
        }else {
            handled=false;
        }
        return handled;
    }

    private Intent createIntent(Class<? extends CoreBaseActivity> clazz, String url, Object... parameters) {
        Intent intent = new Intent(CoreApp.getInstance(), clazz);
        intent.putExtra("url", url);
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i = i + 2) {
                if (i < parameters.length) {
                    Object value = parameters[i + 1];
                    if (value instanceof String) {
                        intent.putExtra((String) parameters[i], i + 1 < parameters.length ? (String) value : "");
                    } else if (value instanceof Boolean) {
                        intent.putExtra((String) parameters[i], i + 1 < parameters.length ? (Boolean) value : false);
                    } else if (value instanceof Serializable) {
                        intent.putExtra((String) parameters[i], (Serializable) value);
                    } else {
                        throw new RuntimeException("unsupported parameter type:"+value.getClass().getName());
                    }
                }
            }
        }
        return intent;
    }

    private static void startActivity(Intent intent) {
        Activity topActivity = BaseActivity.getTopActivity();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (topActivity != null) {
            topActivity.startActivity(intent);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            CoreApp.getInstance().startActivity(intent);
        }
    }

    /**
     * 解析出component的名称
     * @param url
     * @return
     */
    public static String parseComponentName(String url) {
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}

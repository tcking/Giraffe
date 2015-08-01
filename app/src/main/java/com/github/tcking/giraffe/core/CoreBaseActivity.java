package com.github.tcking.giraffe.core;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.github.tcking.giraffe.helper.Toaster;
import com.github.tcking.giraffe.manager.CoreAnalyticsManager;
import com.github.tcking.giraffe.manager.AppSecurityManager;

import java.util.Stack;

/**
 * Created by tc(mytcking@gmail.com) on 15/7/10.
 */
public class CoreBaseActivity extends AppCompatActivity {
    private static boolean _isVisible = false;
    private boolean isFinished;
    private boolean analyticsEnabled=false;
    /**
     * 此activity是否需要权限控制
     */
    private boolean accessControl=true;
    private String analyticsPageName;

    /**
     * 是否有activity正在显示在屏幕上
     * @return
     */
    public static boolean isVisible() {
        return _isVisible;
    }

    protected static Stack<CoreBaseActivity> activitys=new Stack<CoreBaseActivity>();

    @Override
    protected void onResume(){
        super.onResume();
        _isVisible=true;
        if (analyticsEnabled) {
            CoreAnalyticsManager.getInstance().onPageStart(getAnalyticsPageName());
        }
        CoreAnalyticsManager.getInstance().onResume(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        _isVisible=false;
        if (analyticsEnabled) {
            CoreAnalyticsManager.getInstance().onPageEnd(getAnalyticsPageName());
        }
        CoreAnalyticsManager.getInstance().onPause(this);
    }

    public static Stack<CoreBaseActivity> getActivitys() {
        return activitys;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (accessControl && !AppSecurityManager.isCertificated()) {
            onAccessDenied(this.getClass());
            finish();
            return;
        }
        if (CoreAppConfig.isStrictMode()) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
        activitys.push(this);
    }

    /**
     * 没有权限访问时的回调
     * @param aClass
     */
    protected void onAccessDenied(Class<? extends CoreBaseActivity> aClass) {
        Log.d("CoreBaseActivity.onAccessDenied {}", aClass);
        Toaster.show("onAccessDenied:"+aClass.getName());
    }

    @Override
    protected void onDestroy() {
        isFinished=true;
        activitys.remove(this);
        super.onDestroy();
    }

    public boolean isFinished() {
        return isFinished;
    }

    public static void finishAll(){
        while(!activitys.empty()){
            CoreBaseActivity a=activitys.pop();
            if (a.accessControl) {
                a.overridePendingTransition(0,0);//不使用动画
                a.finish();
            }
        }
    }

    /**
     * 获取堆栈最上面一个activity
     * @return
     */
    public static Activity getTopActivity() {
        return activitys.empty()?null:activitys.peek();
    }

    /**
     * true表示此activity需要用户登录才能访问
     * @return the accessControl
     */
    protected boolean isAccessControl() {
        return accessControl;
    }

    /**
     * 默认进行权限控制，如果不需要登录的用户也能访问，则需要在调用super.onCreate之前调用：setAccessControl(false)
     * @param accessControl the accessControl to set
     */
    protected void setAccessControl(boolean accessControl) {
        this.accessControl = accessControl;
    }

    /**
     *
     * @return the analyticsEnabled
     */
    public boolean isAnalyticsEnabled() {
        return analyticsEnabled;
    }

    /**
     * activity默认不进行页面统计（因为页面统计由其fragment负责），如果activity没有fragment，则需要调用setAnalyticsEnabled(true)
     * @param analyticsEnabled the analyticsEnabled to set
     */
    public void setAnalyticsEnabled(boolean analyticsEnabled) {
        this.analyticsEnabled = analyticsEnabled;
    }

    /**
     * @param analyticsPageName the analyticsPageName to set
     */
    protected void setAnalyticsPageName(String analyticsPageName) {
        this.analyticsPageName = analyticsPageName;
    }

    /**
     * @return the analyticsPageName
     */
    protected String getAnalyticsPageName() {
        return TextUtils.isEmpty(analyticsPageName)?getClass().getSimpleName():analyticsPageName;
    }
}

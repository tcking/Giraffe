package com.github.tcking.giraffe.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.github.tcking.giraffe.core.CoreApp;
import com.github.tcking.giraffe.event.DeviceNetworkChangeEvent;

import de.greenrobot.event.EventBus;

/**
 * 获取设备相关的信息，例如网络状态，类型，IMEI，屏幕像素，分辨率，px和dp的相互转换等
 * Created by tc(mytcking@gmail.com) on 15/6/22.
 */
public class DeviceManager extends BroadcastReceiver implements Manager {

    private static DeviceManager instance;

    public static final int NETWORK_TYPE_2G = 2;
    private static final int NETWORK_TYPE_3G = 3;
    private static final int NETWORK_TYPE_4G = 4;
    private static final int NETWORK_TYPE_UNKNOWN = 0;
    private float density;

    public DeviceManager() {
        if (instance != null) {
            throw new RuntimeException("using getInstance");
        }
        instance = this;
    }

    public static DeviceManager getInstance() {
        return instance;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            EventBus.getDefault().post(new DeviceNetworkChangeEvent());
        }
    }

    @Override
    public void onAppStart() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        CoreApp.getInstance().registerReceiver(this, filter);

        density=getDisplayMetrics().density;
    }


    /**
     * 获取手机的IMEI
     *
     * @return
     */
    public String getIMEI() {
        TelephonyManager tm = ((TelephonyManager) CoreApp.getInstance().getSystemService(CoreApp.TELEPHONY_SERVICE));
        return tm.getDeviceId() == null ? "-1" : tm.getDeviceId();
    }


    /**
     * wifi是否连接
     * @return
     */
    public boolean hasWifi() {
        ConnectivityManager cm = getConnectivityManager();
        NetworkInfo network = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return network != null && NetworkInfo.State.CONNECTED == network.getState();
    }

    private static ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) CoreApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 移动网络是否连接
     * @return
     */
    public boolean hasMobileNetwork() {
        ConnectivityManager cm = getConnectivityManager();
        NetworkInfo network = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return network != null && NetworkInfo.State.CONNECTED == network.getState();
    }

    /**
     * 是否有网络连接
     * @return
     */
    public boolean hasNetwork() {
        return hasWifi() || hasMobileNetwork();
    }

    /**
     * 获取移动网络的类型
     * @return
     */
    public static int getMobileNetworkType() {
        ConnectivityManager cm = getConnectivityManager();
        NetworkInfo network = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        switch (network.getSubtype()) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NETWORK_TYPE_2G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return NETWORK_TYPE_3G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return NETWORK_TYPE_4G;
            default:
                return NETWORK_TYPE_UNKNOWN;
        }
    }

    public float getDensity(){
        if (density<=0) {
            density=getDisplayMetrics().density;
        }
        return density;
    }

    public DisplayMetrics getDisplayMetrics(){
        return CoreApp.getInstance().getResources().getDisplayMetrics();
    }

    /**
     * 将dp换算为px
     * @param dp
     * @return
     */
    public int dp2px(float dp) {
        return (int) (dp * getDensity() + 0.5f);
    }

    /**
     * 将px换算为dp
     * @param px
     * @return
     */
    public int px2dp(int px) {
        return (int) (px / getDensity() + 0.5f);
    }

    /**
     * 获取屏幕的高度（px）
     * @return
     */
    public int getScreenHeightInPx(){
        return getDisplayMetrics().heightPixels;
    }

    /**
     *获取屏幕的宽度（px）
     * @return
     */
    public int getScreenWidthInPx(){
        return getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕的宽度（dp）
     * @return
     */
    public int getScreenWidthInDp(){
        return px2dp(getScreenWidthInPx());
    }

    /**
     * 获取屏幕的高度（dp）
     * @return
     */
    public int getScreenHeightInDp(){
        return px2dp(getScreenHeightInPx());
    }
}

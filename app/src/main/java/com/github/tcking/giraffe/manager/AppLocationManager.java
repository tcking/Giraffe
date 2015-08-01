package com.github.tcking.giraffe.manager;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import com.github.tcking.giraffe.core.CoreApp;
import com.github.tcking.giraffe.core.Log;
import com.github.tcking.giraffe.event.LocationEvent;
import com.github.tcking.giraffe.event.LocationTimeoutEvent;

import de.greenrobot.event.EventBus;

/**
 * <pre>
 * 获取定位（目前只提供单次定位，获取到位置信息后停止定位）
 * 1.要获取定位时，调用tryLocation
 * 2.获取到定位消息时发送Event：LocationEvent
 * 3.获取定位超时时时发送Event：LocationTimeoutEvent
 * </pre>
 * Created by tc(mytcking@gmail.com) on 15/7/31.
 */
public class AppLocationManager extends BaseManager {
    public static final int MIN_TIME = 0;
    public static final int MIN_DISTANCE = 0;
    public static final int TIME_OUT = 15000;//定位的超时时间
    private static AppLocationManager instance;
    private boolean isLocating =false;//是否正在定位

    private Location lastLocation;

    public Location getLastLocation() {
        return lastLocation;
    }

    public AppLocationManager() {
        if (instance == null) {
            instance = this;
        } else {
            throw new RuntimeException("using getInstance");
        }
    }

    private void setLastLocation(Location lastLocation) {
        Log.d("AppLocationManager.setLastLocation :{}", lastLocation);
        if (lastLocation!=null) {
            this.lastLocation = lastLocation;
            EventBus.getDefault().post(new LocationEvent(lastLocation));
            removeUpdates();
        }
    }

    private void removeUpdates() {
        LocationManager locationManager = (LocationManager) CoreApp.getInstance().getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(locationListener);
        isLocating =false;
    }

    private LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("AppLocationManager.onLocationChanged {}", location);
            setLastLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("AppLocationManager.onStatusChanged provider:{},status:{}", provider, status);
            if (isLocating) {
                removeUpdates();
                tryLocation();
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("AppLocationManager.onProviderEnabled {}", provider);
            if (isLocating) {
                removeUpdates();
                tryLocation();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("AppLocationManager.onProviderDisabled {}", provider);
            if (isLocating) {
                removeUpdates();
                tryLocation();
            }
        }
    };


    @Override
    public void onAppStart(Context context) {
        super.onAppStart(context);
    }


    public void tryLocation(boolean GPSFirst,int timeout){
        CoreApp app = CoreApp.getInstance();
        LocationManager locationManager = (LocationManager) app.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSProviderEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkProviderEnabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.d("AppLocationManager.tryLocation isGPSProviderEnabled:{},isNetworkProviderEnabled:{},isLocating:{}",
                isGPSProviderEnabled, isNetworkProviderEnabled, isLocating);

//        boolean corseLocationPermession = DeviceManager.checkPermission("android.permission.ACCESS_COARSE_LOCATION");
//        boolean GPSLocationPermession = DeviceManager.checkPermission("android.permission.ACCESS_FINE_LOCATION");
//
//        Log.d("AppLocationManager.tryLocation corseLocationPermession:{},GPSLocationPermession:{}",corseLocationPermession,GPSLocationPermession);
//        if (!corseLocationPermession && !corseLocationPermession) {
////            AlertDialog.Builder builder = new AlertDialog.Builder(CoreBaseActivity.getTopActivity(), AlertDialog.THEME_HOLO_LIGHT);
//            Toast.makeText(CoreApp.getInstance(),"没有定位权限，请为应用授权后再试",Toast.LENGTH_LONG).show();
//            return;
//        }


        if (!isGPSProviderEnabled && !isNetworkProviderEnabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            app.startActivity(intent);
            return;
        }

        if (!isLocating) {
            isLocating =true;
            lastLocation = null;
            String provider=LocationManager.NETWORK_PROVIDER;//默认使用网络定位，GPS慢，而且在室内基本没戏
            if (isGPSProviderEnabled && GPSFirst) {
                provider = LocationManager.GPS_PROVIDER;
            }else if (!isNetworkProviderEnabled) {
                provider = LocationManager.GPS_PROVIDER;
            }
            locationManager.requestLocationUpdates(provider, MIN_TIME, MIN_DISTANCE, locationListener);
            CoreApp.getInstance().runOnUiTread(new Runnable() {
                @Override
                public void run() {
                    if (lastLocation == null) {
                        EventBus.getDefault().post(new LocationTimeoutEvent());
                        removeUpdates();
                    }
                }
            }, timeout>0?timeout:TIME_OUT);
        }
    }

    public void tryLocation(){
        tryLocation(false,0);
    }

    public static AppLocationManager getInstance() {
        return instance;
    }
}

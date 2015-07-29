package com.github.tcking.giraffe.core;

import android.app.Application;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;

import com.github.tcking.giraffe.event.AppInitEvent;
import com.github.tcking.giraffe.event.LowMemoryEvent;
import com.github.tcking.giraffe.manager.Manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.greenrobot.event.EventBus;

/**
 * 1.实例化业务层（manager），并注册到事件总线
 * 2.加载配置
 * 3.开始事件驱动流程
 * Created by tc(mytcking@gmail.com) on 15/6/16.
 */
public abstract class CoreApp extends Application {
    private static CoreApp instance;
    List<Manager> registeredManagers=new ArrayList<Manager>();
    private Handler handler;
    private static ExecutorService threadPool;
    private static ExecutorService singleExecutor;
    public CoreApp() {
        instance = this;
        handler = new Handler();
        threadPool = Executors.newCachedThreadPool();
        singleExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadAsyncTask();
        CoreAppConfig.configure(this);//加载配置
        if (CoreAppConfig.isStrictMode()) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
        registerManager(getRegisterManager());
        EventBus.getDefault().post(new AppInitEvent(this));
    }

    /**
     * 获取注册了的manager
     * @return
     */
    public abstract int getRegisterManager();

    /**
     * 获取Application实例
     * @return
     */
    public static CoreApp getInstance() {
        if (instance == null)
            throw new IllegalStateException();
        return instance;
    }

    /**
     * 注册manager，启动的时候注册到事件总线
     */
    private void registerManager(int managers) {
        TypedArray managerClasses = getResources().obtainTypedArray(managers);
        for (int index = 0; index < managerClasses.length(); index++){
            try {
                Manager manager=(Manager) Class.forName(managerClasses.getString(index)).newInstance();
                Log.d("CoreApp.registerManager {}",manager.getClass().getName());
                registeredManagers.add(manager);
                manager.onAppStart(this);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        managerClasses.recycle();
    }

    /**
     * 这里使用一个空的AsyncTask，确保第一个AsyncTask在UI线程中执行，避免出现：NoClassDefFoundError android/os/AsyncTask
     *
     */
    private void loadAsyncTask() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                return null;
            }
        }.execute();
    }

    /**
     * 获取线程池
     * @return
     */
    public static ExecutorService getThreadPool() {
        if(threadPool.isTerminated() || threadPool.isShutdown()){
            threadPool=Executors.newCachedThreadPool();
        }
        return threadPool;
    }

    /**
     * 在UI线程中执行
     * @param runnable
     */
    public void runOnUiTread(Runnable runnable) {
        handler.post(runnable);
    }

    /**
     * 在UI线程中执行
     * @param runnable
     * @param delayMillis
     */
    public void runOnUiTread(Runnable runnable,long delayMillis) {
        handler.postDelayed(runnable,delayMillis);
    }

    public static ExecutorService getSingleExecutor() {
        if(singleExecutor.isTerminated() || singleExecutor.isShutdown()){
            singleExecutor= Executors.newSingleThreadExecutor();
        }
        return singleExecutor;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d("CoreApp.onLowMemory ");
        EventBus.getDefault().post(new LowMemoryEvent());
    }
}

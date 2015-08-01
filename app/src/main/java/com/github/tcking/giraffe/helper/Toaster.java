package com.github.tcking.giraffe.helper;

import android.widget.Toast;

import com.welezu.mobile.App;

/**
 * Created by tc(mytcking@gmail.com) on 15/7/27.
 */
public class Toaster {

    public static void showLong(String message) {
        show(message,Toast.LENGTH_LONG);
    }

    public static void show(String message) {
        show(message,Toast.LENGTH_SHORT);
    }

    private static void show(final String message, final int length) {
        App.getInstance().runOnUiTread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(App.getInstance(), message, length).show();
            }
        });
    }

    public static void show(int stringId){
        show(App.getInstance().getString(stringId));
    }
    public static void showLong(int stringId){
        showLong(App.getInstance().getString(stringId));
    }
}

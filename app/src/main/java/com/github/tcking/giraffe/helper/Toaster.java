package com.github.tcking.giraffe.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tcking.example.App;
import com.github.tcking.example.R;
import com.github.tcking.giraffe.core.CoreApp;


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
        CoreApp.getInstance().runOnUiTread(new Runnable() {
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

    public static void ok(final String message){
        xShow(R.drawable.ic_done_white_24dp, message);
    }

    public static void error(final String message) {
        xShow(R.drawable.ic_highlight_off_white_24dp, message);
    }
    public static void info(final String message) {
        xShow(R.drawable.ic_info_outline_white_24dp, message);
    }

    public static void xShow(final int icon, final String message) {
        CoreApp.getInstance().runOnUiTread(new Runnable() {
            @Override
            public void run() {
                CoreApp context = CoreApp.getInstance();
                Toast result = new Toast(context);
                LayoutInflater inflate = (LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflate.inflate(R.layout.app_toast, null);
                v.findViewById(R.id.img).setBackgroundResource(icon);
                v.setBackgroundResource(R.drawable.app_toast_ok_bg);
                TextView tv = (TextView) v.findViewById(R.id.button);
                tv.setText(message);
                result.setView(v);
                result.setDuration(Toast.LENGTH_SHORT);
                result.show();
            }
        });
    }
}

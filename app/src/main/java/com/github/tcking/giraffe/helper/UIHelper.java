package com.github.tcking.giraffe.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.github.tcking.giraffe.core.CoreApp;
import com.github.tcking.giraffe.core.CoreBaseActivity;


/**
 * Created by tc(mytcking@gmail.com) on 15/7/23.
 */
public class UIHelper {
    public static Drawable getTintedDrawable(int drawableResId,int colorResId) {
        Resources res = CoreApp.getInstance().getResources();
        Drawable drawable = res.getDrawable(drawableResId);
        int color = res.getColor(colorResId);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    public static Drawable getTintedDrawableWithBounds(int drawableResId,int colorResId) {
        Resources res = CoreApp.getInstance().getResources();
        Drawable drawable = res.getDrawable(drawableResId);
        int color = res.getColor(colorResId);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        /// 调用TextView的setCompoundDrawables这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    public static void hideInputMethod() {
        hideInputMethod(CoreBaseActivity.getTopActivity());
    }

    public static void hideInputMethod(Activity activity) {
        if (activity == null) {
            return;
        }
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus != null) {
            hideInputMethod(currentFocus);
        }
    }

    public static void hideInputMethod(final View currentFocus) {
        CoreApp app = CoreApp.getInstance();
        InputMethodManager imm = (InputMethodManager) app.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
    }

    /**
     * 显示提示对话框
     *
     * @param title
     * @param msg
     * @param buttonText
     */
    public static void alert(String title, String msg, String buttonText, final Runnable action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CoreBaseActivity.getTopActivity(), AlertDialog.THEME_HOLO_LIGHT);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(msg)) {
            builder.setMessage(msg);
        }
        builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (action != null) {
                    action.run();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 显示提示对话框
     *
     * @param title
     * @param msg
     * @param buttonText
     */
    public static void confirm(String title, String msg, String yesButtonText, final Runnable yesAction,String noButtonText,final Runnable noAction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CoreBaseActivity.getTopActivity(),AlertDialog.THEME_HOLO_LIGHT);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(msg)) {
            builder.setMessage(msg);
        }
        builder.setPositiveButton(TextUtils.isEmpty(yesButtonText)?"确定":yesButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (yesAction != null) {
                    yesAction.run();
                }
            }
        });
        builder.setNegativeButton(TextUtils.isEmpty(noButtonText) ? "取消" : noButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (noAction != null) {
                    noAction.run();
                }
            }
        });
        AlertDialog dialog = builder.create();
//        dialog.show();
    }

    /**
     * 显示/隐藏系统状态栏
     * @param show
     */
    public static void toggleShowStatusBar(boolean show) {
        Activity topActivity = CoreBaseActivity.getTopActivity();
        if (topActivity != null) {
            topActivity.getWindow().getDecorView().setSystemUiVisibility(show ? View.VISIBLE : View.INVISIBLE);
        }

    }

    /**
     * 使状态栏透明(状态栏不占空间,覆盖在最上)
     * 注意:fullscreen mode doesn't resize
     *  http://stackoverflow.com/questions/7417123/android-how-to-adjust-layout-in-full-screen-mode-when-softkeyboard-is-visible
     */
    public static void transparentStatusBar(boolean transparent) {
        Activity topActivity = CoreBaseActivity.getTopActivity();
        if (topActivity != null) {
            Window window = topActivity.getWindow();
            if (transparent) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }

    public static void showInputMethod(final View view) {
        CoreApp.getInstance().runOnUiTread(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) CoreApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        }, 100);
    }
}

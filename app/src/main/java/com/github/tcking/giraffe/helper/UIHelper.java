package com.github.tcking.giraffe.helper;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import com.welezu.mobile.App;

/**
 * Created by tc(mytcking@gmail.com) on 15/7/23.
 */
public class UIHelper {
    public static Drawable getTintedDrawable(int drawableResId,int colorResId) {
        Resources res = App.getInstance().getResources();
        Drawable drawable = res.getDrawable(drawableResId);
        int color = res.getColor(colorResId);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    public static Drawable getTintedDrawableWithBounds(int drawableResId,int colorResId) {
        Resources res = App.getInstance().getResources();
        Drawable drawable = res.getDrawable(drawableResId);
        int color = res.getColor(colorResId);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        /// 调用TextView的setCompoundDrawables这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }
}

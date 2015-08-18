package com.github.tcking.giraffe.helper;

import android.support.v4.view.ViewPager;
import android.text.Editable;

/**
 * <pre>
 * 一些Listeners的适配，do nothing，简化代码，例如想要增加一个TextWatcher，而只对其中的一个afterTextChanged感兴趣，
 * new Listeners.TextWatcher() {
 *      @Override public void afterTextChanged(Editable s) {
 *          //do something
 *      }
 * };
 * </pre>
 * Created by tc(mytcking@gmail.com) on 15/8/17.
 */
public class Listeners {
    public static class TextWatcher implements android.text.TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public static class OnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}

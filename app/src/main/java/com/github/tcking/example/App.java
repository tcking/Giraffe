package com.github.tcking.example;

import com.github.tcking.giraffe.core.CoreApp;

/**
 * Created by tc(mytcking@gmail.com) on 15/8/1.
 */
public class App extends CoreApp {
    @Override
    public int getRegisterManager() {
        return R.array.managers;
    }
}

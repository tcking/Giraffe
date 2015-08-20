package com.github.tcking.example.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.github.tcking.example.R;
import com.github.tcking.giraffe.helper.Router;

/**
 * Created by tc(mytcking@gmail.com) on 15/8/5.
 */
public class AppDrawerActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_drawer);
        setupViews();
    }

    private void setupViews() {
        NavigationView navigationView= (NavigationView) findViewById(R.id.app_nav);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    ((DrawerLayout) findViewById(R.id.app_drawer)).closeDrawers();
                    int itemId = menuItem.getItemId();
                    if (itemId ==R.id.menu_navbar) {
                        Router.goComponent("tabBar");
                    }else if (itemId == R.id.menu_widget) {
                        Router.goComponent("main");
                    }else if (itemId == R.id.menu_photo) {
                        Router.goComponent("getPhoto");
                    }
                    return false;
                }
            });
        }
    }

    @Override
    protected int getNavigationIcon() {
        return R.drawable.ic_menu_white_36dp;
    }

    @Override
    protected boolean onNavigationIconPressed() {
        ((DrawerLayout) findViewById(R.id.app_drawer)).openDrawer(GravityCompat.START);
        return true;
    }
}

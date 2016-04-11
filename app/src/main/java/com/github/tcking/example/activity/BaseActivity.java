package com.github.tcking.example.activity;

import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.androidquery.AQuery;
import com.github.tcking.example.R;
import com.github.tcking.giraffe.core.CoreBaseActivity;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

/**
 * Created by tc(mytcking@gmail.com) on 15/8/5.
 */
public class BaseActivity extends CoreBaseActivity{
    protected Toolbar toolbar;
    protected AQuery $;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set for Android-Iconics
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        setAccessControl(false);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        $ = new AQuery(this);
        setupToolbar();
    }

    protected void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            if (getNavigationIcon()!=0) {
                toolbar.setNavigationIcon(getNavigationIcon());
            }
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            resetTitle(getTitle());
        }
    }

    /**
     * 设置导航图标
     * @return
     */
    protected int getNavigationIcon() {
        return 0;
    }

    private void resetTitle(CharSequence title) {
        ActionBar actionBar = getSupportActionBar();
        if ($.id(R.id.toolbar_title_center).getView() != null) {
            $.id(R.id.toolbar_title_center).text(title);
            toolbar.setTitle("");
            actionBar.setTitle("");
        } else {
            toolbar.setTitle(title);
        }
    }

    /**
     * 判断MainActivity是在堆栈中
     * @return
     */
    public static boolean isMainExists() {
        synchronized (activitys) {
            for (CoreBaseActivity a : activitys) {
                if (a instanceof AppDrawerActivity) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            if (onNavigationIconPressed()) {
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 当toolbar左上角的home icon或back icon点击时的处理，默认为finish
     * @return
     */
    protected boolean onNavigationIconPressed() {
        finish();
        return true;
    }

    public void setDisplayHomeAsUpEnabled(boolean enabled){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(enabled);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        resetTitle(title);
    }
}

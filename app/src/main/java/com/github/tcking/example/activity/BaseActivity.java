package com.github.tcking.example.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.androidquery.AQuery;
import com.github.tcking.example.R;
import com.github.tcking.giraffe.core.CoreBaseActivity;

/**
 * Created by tc(mytcking@gmail.com) on 15/8/5.
 */
public class BaseActivity extends CoreBaseActivity{
    protected Toolbar toolbar;
    private AQuery $;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            if ((BaseActivity.getTopActivity() instanceof MainActivity)) {
//                toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
            } else {
//                toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_18dp);
            }
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            resetTitle(getTitle());
        }
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
                if (a instanceof MainActivity) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home && !(this instanceof MainActivity)) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

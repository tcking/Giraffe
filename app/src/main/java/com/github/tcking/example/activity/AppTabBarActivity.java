package com.github.tcking.example.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.tcking.example.R;
import com.github.tcking.giraffe.helper.Listeners;
import com.github.tcking.giraffe.helper.Toaster;

/**
 * tab bar布局
 * Created by tc(mytcking@gmail.com) on 15/8/14.
 */
public class AppTabBarActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_tabbar);

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.app_RadioGroup);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.app_ViewPager);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    if (group.getChildAt(i).getId()==checkedId) {
                        viewPager.setCurrentItem(i);
                        return;
                    }
                }
            }
        });

        viewPager.addOnPageChangeListener(new Listeners.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position>=radioGroup.getChildCount()) {
                    Toaster.show("tab not match the page");
                    return;
                }
                CompoundButton radio = (CompoundButton) radioGroup.getChildAt(position);
                radio.setChecked(true);
            }
        });

        viewPager.setAdapter(new TabBarPagerAdapter(getSupportFragmentManager()));


    }

    class TabBarPagerAdapter extends FragmentPagerAdapter{

        public TabBarPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            TabBarFragment fragment = new TabBarFragment();
            Bundle args=new Bundle();
            args.putString("testString",""+position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    class TabBarFragment extends Fragment{
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.app_tabbar_fragment_x, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            ((TextView) view.findViewById(R.id.app_TextView)).setText(getArguments().getString("testString"));
        }
    }
}

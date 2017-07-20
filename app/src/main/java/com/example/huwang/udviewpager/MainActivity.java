package com.example.huwang.udviewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.example.huwang.udviewpager.fragment.ViewPagerFragment;
import com.example.huwang.udviewpager.view.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private ViewPagerIndicator mViewPagerIndicator;

    private List<String> mTitles = Arrays.asList("短信", "通讯录", "发现", "我的信息", "invisible1", "invisible2", "invisible3");
    private List<ViewPagerFragment> mContents = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_test);
        initViews();
        initDatas();

        // 可以设置标签数目和标签索要显示的内容，并动态的添加子控件
        mViewPagerIndicator.setVisibleTabCount(4);
        mViewPagerIndicator.setTabItemTitles(mTitles);

        mViewPager.setAdapter(mAdapter);
        mViewPagerIndicator.setViewPager(mViewPager, 0);
    }

    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mViewPagerIndicator = (ViewPagerIndicator) findViewById(R.id.id_indicator);
    }

    private void initDatas() {
        for (String title : mTitles) {
            ViewPagerFragment fragment = ViewPagerFragment.newInstance(title);
            mContents.add(fragment);
        }
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mContents.get(position);
            }

            @Override
            public int getCount() {
                return mContents.size();
            }
        };
    }
}

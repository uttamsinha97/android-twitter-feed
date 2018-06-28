package com.android_twitter_show_timelines_demo.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android_twitter_show_timelines_demo.R;
import com.android_twitter_show_timelines_demo.adapter.TabViewPagerAdapter;
import com.android_twitter_show_timelines_demo.tabs.CollectionTimelineFragment;
import com.android_twitter_show_timelines_demo.tabs.SearchTimelineFragment;
import com.android_twitter_show_timelines_demo.tabs.UserTimelineFragment;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    /**
     * init views including finding id of views and setting tab with fragments
     */
    private void initViews() {
        viewPager = findViewById(R.id.twitter_view_pager);
        setupViewPager();

        tabLayout = findViewById(R.id.twitter_tab);
        tabLayout.setupWithViewPager(viewPager);//setting tab over viewpager
    }

    /**
     * set fragment to View pager
     */
    private void setupViewPager() {

        //get tab array from string.xml
        String[] tabArray = getResources().getStringArray(R.array.tab_items);

        TabViewPagerAdapter adapter = new TabViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(UserTimelineFragment.newInstance(), tabArray[0]);
        adapter.addFrag(SearchTimelineFragment.newInstance(), tabArray[1]);
        adapter.addFrag(CollectionTimelineFragment.newInstance(), tabArray[2]);
        viewPager.setAdapter(adapter);
    }
}

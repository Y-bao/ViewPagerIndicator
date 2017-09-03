package com.ybao.viewpagerindicator.tabview;

import android.support.v4.view.ViewPager;

/**
 * Created by Y-bao on 2017/8/17 0017.
 */

public abstract class HViewPagerTabAdapter extends ViewPagerTabAdapter {

    ViewPager viewPager;

    public void setViewPager(ViewPager viewPager) {
        if (this.viewPager != null) {
            this.viewPager.setOnPageChangeListener(null);
        }
        this.viewPager = viewPager;
        this.viewPager.setOnPageChangeListener(onPageChangeListener);
        setAdapter(viewPager.getAdapter());
    }

    @Override
    protected void setCurrentItem(int position, boolean smoothScroll) {
        viewPager.setCurrentItem(position, smoothScroll);
    }

    @Override
    protected int getCurrentItem() {
        return viewPager.getCurrentItem();
    }
}

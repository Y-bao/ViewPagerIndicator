package com.ybao.viewpagerindicator.tabview;

import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.ybao.viewpagerindicator.tabview.indicator.TabIndicatorAdapter;

/**
 * Created by Y-bao on 2017/8/17 0017.
 */

public abstract class ViewPagerTabAdapter<K> extends TabIndicatorAdapter<K> {

    PagerAdapter adapter;

    private ViewPager.OnPageChangeListener inPutListener;

    protected void setAdapter(PagerAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("adapter can't isnull");
        }
        this.adapter = adapter;
        adapter.registerDataSetObserver(dataSetObserver);
        dataSetObserver.onChanged();
    }

    @Override
    protected void onItemClick(View v, int position) {
        if (Math.abs(position - getCurrentItem()) > 1) {
            t.smoothScroll(position);
            setCurrentItem(position, false);
        } else {
            setCurrentItem(position, true);
        }
    }

    @Override
    protected CharSequence getName(int position) {
        return adapter.getPageTitle(position);
    }

    protected abstract void setCurrentItem(int position, boolean smoothScroll);

    protected abstract int getCurrentItem();

    @Override
    public int getCount() {
        return adapter.getCount();
    }


    @Override
    protected void onTransform(View item, float offset) {
    }


    @Override
    protected void onAfterMeasure() {
        t.select(getCurrentItem());
    }

    @Override
    protected void onDrawIndicator(Canvas canvas, float translationD, float tempSize) {
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener pageChangeListener) {
        this.inPutListener = pageChangeListener;
    }

    DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }
    };

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        int oldpos;

        @Override
        public void onPageSelected(int position) {
            oldpos = position;
            if (inPutListener != null) {
                inPutListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (positionOffset == 0) {
                if (position == oldpos) {
                    t.scrollToPositionOffset(position + 1, position, 1);
                } else if (oldpos < position) {
                    t.scrollToPositionOffset(position - 1, position, 1);
                } else if (oldpos > position) {
                    t.scrollToPositionOffset(position + 1, position, 1);
                }
            } else {
                if (position == oldpos) {
                    t.scrollToPositionOffset(position, position + 1, positionOffset);
                } else if (oldpos < position) {
                    t.scrollToPositionOffset(position - 1, position, positionOffset);
                } else if (oldpos > position) {
                    t.scrollToPositionOffset(position + 1, position, 1 - positionOffset);
                }
            }

            Log.e("onPageScrolled", "" + oldpos + " : " + position);
            if (inPutListener != null) {
                inPutListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (inPutListener != null) {
                inPutListener.onPageScrollStateChanged(state);
            }
        }
    };
}

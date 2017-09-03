package com.ybao.viewpagerindicator.tabview.scroll;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ybao on 2017/8/17.
 */

public interface ScrollTabBoxImpt {

    void setShowItemCount(int showItemCount);

    void setFilling(boolean filling);

    void addItem(View child);

    void addItem(View child, int index);

    void addItem(View child, ViewGroup.LayoutParams params);

    void addItem(View child, int index, ViewGroup.LayoutParams params);

    void addItem(View child, int width, int height);

    int getItemCount();

    View getItemAt(int index);

    void removeAllItems();

    ViewGroup getBox();
}

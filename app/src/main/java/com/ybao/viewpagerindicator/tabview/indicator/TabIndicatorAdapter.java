package com.ybao.viewpagerindicator.tabview.indicator;

import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;

import com.ybao.viewpagerindicator.tabview.scroll.TabAdapter;

/**
 * Created by Y-bao on 2017/8/17 0017.
 */

public abstract class TabIndicatorAdapter<K> extends TabAdapter<IndicatorManager.HelperImpt, K> {

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = getView(position, parent, getName(position));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v, position);
            }
        });
        return view;
    }

    protected abstract View getView(int position, ViewGroup parent, CharSequence name);

    protected abstract CharSequence getName(int position);

    protected void onItemClick(View v, int position) {

    }

    void transform(View item, float offset) {
        onTransform(item, offset);
    }

    protected void onTransform(View item, float offset) {
    }

    void afterMeasure() {
        onAfterMeasure();
    }

    protected void onAfterMeasure() {
        getScrollTabBox().select(0);
    }

    void drawIndicator(Canvas canvas, float translationD, float tempSize) {
        onDrawIndicator(canvas, translationD, tempSize);
    }

    protected void onDrawIndicator(Canvas canvas, float translationD, float tempSize) {
    }

    protected void onScrollToPositionOffset(View nowView, View newView, float offset, float translationD, float tempSize) {

    }
}

package com.ybao.viewpagerindicator.tabview.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.ybao.viewpagerindicator.tabview.scroll.HScrollTabBox;


/**
 * 等分容器
 * Created by Ybao on 16/8/30.
 */
public class HTabIndicator extends HScrollTabBox implements IndicatorManager.HelperImpt {
    IndicatorManager indicatorManager;

    public HTabIndicator(Context context) {
        this(context, null);
    }

    public HTabIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        indicatorManager = new IndicatorManager(targetImpt);
    }

    @Override
    public void setAdapter(TabIndicatorAdapter adapter) {
        indicatorManager.setAdapter(adapter);
        super.setAdapter(adapter);
    }

    @Override
    protected void onDataChanged() {
        reSet();
        super.onDataChanged();
        indicatorManager.onDataChanged();
    }

    @Override
    public void setLockType(int lockType) {
        indicatorManager.setLockType(lockType);
    }

    @Override
    public void setShowOffSetItem(int showOffSetItem) {
        indicatorManager.setShowOffSetItem(showOffSetItem);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        indicatorManager.onMeasure();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        indicatorManager.dispatchDraw(canvas);
    }

    @Override
    public void scrollToPositionOffset(int fromPosition, int toPosition, float offset) {
        indicatorManager.scrollToPositionOffset(fromPosition, toPosition, offset);
    }

    @Override
    public void select(int toPosition) {
        indicatorManager.select(toPosition);
    }

    @Override
    public void smoothScroll(final int toPosition) {
        indicatorManager.smoothScroll(toPosition);
    }

    @Override
    public void stopScroll() {
        indicatorManager.stopScroll();
    }

    @Override
    public void reSet() {
        indicatorManager.reSet();
    }

    IndicatorManager.TargetImpt targetImpt = new IndicatorManager.TargetImpt() {

        @Override
        public int getItemStart(View item) {
            int[] itemLoc = new int[2];
            item.getLocationOnScreen(itemLoc);
            int[] boxLoc = new int[2];
            getBox().getLocationOnScreen(boxLoc);
            return itemLoc[0] - boxLoc[0];
        }

        @Override
        public void scrollTo(float d) {
            HTabIndicator.this.scrollTo((int) d, 0);
        }

        @Override
        public int getMeasuredSize(View view) {
            return view.getMeasuredWidth();
        }

        @Override
        public int getOffset() {
            return HTabIndicator.this.getScrollX();
        }

        @Override
        public View getTargetView() {
            return HTabIndicator.this;
        }

        @Override
        public int getItemCount() {
            return HTabIndicator.this.getItemCount();
        }

        @Override
        public View getItemAt(int index) {
            return HTabIndicator.this.getItemAt(index);
        }
    };
}

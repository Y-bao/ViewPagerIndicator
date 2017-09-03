package com.ybao.viewpagerindicator.tabview.scroll;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;


/**
 * 等分容器
 * Created by Ybao on 16/8/30.
 */
public class HScrollTabBox extends HorizontalScrollView implements ScrollTabBoxImpt {
    private LinearLayout boxItem;
    private boolean filling = false;
    private int showItemCount = 4;

    private TabAdapter adapter;

    private DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            onDataChanged();
        }
    };

    public HScrollTabBox(Context context) {
        this(context, null);
    }

    public HScrollTabBox(Context context, AttributeSet attrs) {
        super(context, attrs);

        boxItem = new LinearLayout(getContext());
        boxItem.setOrientation(LinearLayout.HORIZONTAL);
        setHorizontalScrollBarEnabled(false);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        super.addView(boxItem, layoutParams);
    }

    protected void onDataChanged() {
        removeAllItems();
        if (adapter != null) {
            int n = adapter.getCount();
            for (int i = 0; n > i; i++) {
                View view = adapter.getView(i, null, getBox());
                addItem(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }
    }

    @Override
    public void setShowItemCount(int showItemCount) {
        this.showItemCount = showItemCount;
        requestLayout();
    }

    @Override
    public void setFilling(boolean filling) {
        this.filling = filling;
        requestLayout();
    }

    public void setAdapter(TabAdapter adapter) {
        if (this.adapter != null) {
            this.adapter.unregisterDataSetObserver(dataSetObserver);
        }
        this.adapter = adapter;
        this.adapter.setScrollTabBox(this);
        this.adapter.registerDataSetObserver(dataSetObserver);
        dataSetObserver.onChanged();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (showItemCount > 0) {
            int n = getItemCount();
            int itemHeigth = getMeasuredHeight();
            int itemWidth = 0;
            if (n > showItemCount) {
                itemWidth = (int) (getMeasuredWidth() / (showItemCount + 0.5));
            } else {
                if (filling) {
                    itemWidth = getMeasuredWidth() / n;
                } else {
                    itemWidth = getMeasuredWidth() / showItemCount;
                }
            }
            boxItem.measure(MeasureSpec.makeMeasureSpec(itemWidth * n, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(itemHeigth, MeasureSpec.EXACTLY));
            for (int i = 0; i < n; i++) {
                View item = getItemAt(i);
                item.measure(MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(itemHeigth, MeasureSpec.EXACTLY));
            }
        }
    }

    @Override
    public void addItem(View child) {
        boxItem.addView(child);
    }

    @Override
    public void addItem(View child, int index) {
        boxItem.addView(child, index);
    }

    @Override
    public void addItem(View child, ViewGroup.LayoutParams params) {
        boxItem.addView(child, params);
    }

    @Override
    public void addItem(View child, int index, ViewGroup.LayoutParams params) {
        boxItem.addView(child, index, params);
    }

    @Override
    public void addItem(View child, int width, int height) {
        boxItem.addView(child, width, height);
    }

    @Override
    public int getItemCount() {
        return boxItem.getChildCount();
    }

    @Override
    public View getItemAt(int index) {
        return boxItem.getChildAt(index);
    }

    @Override
    public void removeAllItems() {
        boxItem.removeAllViews();
    }

    @Override
    public ViewGroup getBox() {
        return boxItem;
    }
}

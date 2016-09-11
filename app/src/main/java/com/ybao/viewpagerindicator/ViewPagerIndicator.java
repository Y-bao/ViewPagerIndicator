package com.ybao.viewpagerindicator;


import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;

/**
 * ViewPager指示器
 * Created by Ybao on 16/8/30.
 */
public class ViewPagerIndicator extends HorizontalScrollView implements ViewPager.OnPageChangeListener {

    /**
     * 手指滑动时的偏移量
     */
    private float mTranslationX;

    /**
     * 与之绑定的ViewPager
     */
    public ViewPager mViewPager;

    LinearLayout boxItem;

    private int showItemCount = 4;

    int itemWidth = 0;

    int oldpos = 0;
    int pos = 0;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        boxItem = new LinearLayout(getContext());
        boxItem.setOrientation(LinearLayout.HORIZONTAL);
        setHorizontalScrollBarEnabled(false);
        HorizontalScrollView.LayoutParams layoutParams = new HorizontalScrollView.LayoutParams(HorizontalScrollView.LayoutParams.WRAP_CONTENT, HorizontalScrollView.LayoutParams.MATCH_PARENT);
        addView(boxItem, layoutParams);
    }

    public void setShowItemCount(int showItemCount) {
        this.showItemCount = showItemCount;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int n = boxItem.getChildCount();
        int itemHeigth = getMeasuredHeight();
        if (n > showItemCount) {
            itemWidth = (int) (getMeasuredWidth() / (showItemCount + 0.5));
        } else {
            itemWidth = getMeasuredWidth() / showItemCount;
        }
        boxItem.measure(MeasureSpec.makeMeasureSpec(itemWidth * n, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(itemHeigth, MeasureSpec.EXACTLY));
        for (int i = 0; i < n; i++) {
            View item = boxItem.getChildAt(i);
            item.measure(MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(itemHeigth, MeasureSpec.EXACTLY));
        }
    }

    /**
     * 绘制指示器
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (indexDrawImpt != null) {
            indexDrawImpt.onDraw(this, canvas, mTranslationX);
        }
        super.dispatchDraw(canvas);
    }

    /**
     * 初始化三角形的宽度
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (indexDrawImpt != null) {
            indexDrawImpt.onInit(w, itemWidth);
        }
    }

    /**
     * 对外的ViewPager的回调接口
     *
     * @author zhy
     */
    public interface PageChangeListener {
        void onPageScrolled(int position, float positionOffset,
                            int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }

    // 对外的ViewPager的回调接口
    private PageChangeListener onPageChangeListener;

    // 对外的ViewPager的回调接口的设置
    public void setOnPageChangeListener(PageChangeListener pageChangeListener) {
        this.onPageChangeListener = pageChangeListener;
    }

    DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            boxItem.removeAllViews();
            if (adapter != null) {
                int n = adapter.getCount();
                for (int i = 0; n > i; i++) {
                    View view = null;
                    if (itemCreateAndTransgormImpt != null) {
                        view = itemCreateAndTransgormImpt.createItem(boxItem, i, adapter.getPageTitle(i));
                    }
                    if (view == null) {
                        TextView textView = new TextView(getContext());
                        ViewHelper.setAlpha(textView, 0.5f);
                        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        textView.setGravity(Gravity.CENTER);
                        textView.setTextColor(0xff000000);
                        textView.setText(adapter.getPageTitle(i));
                        view = textView;
                    }
                    boxItem.addView(view);
                    // 设置item的click事件
                    setItemClickEvent();
                }
                View view = boxItem.getChildAt(mViewPager.getCurrentItem());
                if (view != null && itemCreateAndTransgormImpt != null) {
                    itemCreateAndTransgormImpt.select(view, true);
                }
            }
        }
    };

    PagerAdapter adapter;

    // 设置关联的ViewPager
    public void setViewPager(ViewPager mViewPager) {
        this.mViewPager = mViewPager;
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(this);
    }

    public void setAdapter(PagerAdapter adapter) {
        if (this.adapter != null) {
            try {
                this.adapter.unregisterDataSetObserver(dataSetObserver);
            } catch (Exception e) {

            }
        }
        this.adapter = adapter;
        adapter.registerDataSetObserver(dataSetObserver);
        if (mViewPager != null) {
            mViewPager.setAdapter(adapter);
        }
        dataSetObserver.onChanged();
    }

    @Override
    public void onPageSelected(int position) {
        oldpos = pos;
        pos = position;

        // 回调
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        // 滚动
        scroll(position, positionOffset);

        // 回调
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE && itemCreateAndTransgormImpt != null) {
            int start = Math.min(pos, oldpos);
            int end = Math.max(pos, oldpos);
            for (int i = start; i < end + 1; i++) {
                View view = boxItem.getChildAt(i);
                itemCreateAndTransgormImpt.select(view, false);
            }
            View view = boxItem.getChildAt(pos);
            itemCreateAndTransgormImpt.select(view, true);
        }
        // 回调
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrollStateChanged(state);
        }

    }


    /**
     * 设置点击事件
     */
    public void setItemClickEvent() {
        int cCount = boxItem.getChildCount();
        for (int i = 0; i < cCount; i++) {
            final int j = i;
            View view = boxItem.getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }


    /**
     * 指示器跟随手指滚动，以及容器滚动
     *
     * @param position
     * @param offset
     */

    public void scroll(int position, float offset) {
        mTranslationX = itemWidth * (position + offset);
        float lock = itemWidth * (position + offset) + itemWidth / 2;
        if (lockType > 0) {
            scrollTo((int) (lock - lockType / 2), 0);
        } else if (lockType == LOCK_MID) {
            scrollTo((int) (lock - getMeasuredWidth() / 2), 0);
        } else if (lockType == LOCK_END) {
            scrollTo((int) (lock - (getMeasuredWidth() - itemWidth / 2)), 0);
        } else {
            scrollTo((int) (lock - itemWidth / 2), 0);
        }
        invalidate();
        transform(position, offset);
    }

    float oldOffset = 0;

    public void transform(int position, float offset) {
        View nowItem = boxItem.getChildAt(position);
        View newItem = null;
        if (offset == 0) {
            if (oldOffset != 0) {
                newItem = boxItem.getChildAt(position + (oldOffset > 0 ? +1 : -1));
            }
        } else {
            newItem = boxItem.getChildAt(position + (offset > 0 ? +1 : -1));
        }
        boolean isClip = false;
        if (itemCreateAndTransgormImpt != null) {
            isClip = itemCreateAndTransgormImpt.transform(offset, nowItem, newItem);
        }
        if (!isClip) {
            float absoffset = Math.abs(offset);
            if (nowItem != null) {
                ViewHelper.setAlpha(nowItem, 1 - 0.5f * absoffset);
            }
            if (newItem != null) {
                ViewHelper.setAlpha(newItem, 0.5f + 0.5f * absoffset);
            }
        }

        oldOffset = offset;
    }

    public void setLockType(int lockType) {
        this.lockType = lockType;
    }

    private int lockType = LOCK_MID;
    public static int LOCK_START = 0;
    public static int LOCK_END = -1;
    public static int LOCK_MID = -2;

    ItemCreateAndTransgormImpt itemCreateAndTransgormImpt;

    public void setItemCreateAndTransgormImpt(ItemCreateAndTransgormImpt itemCreateAndTransgormImpt) {
        this.itemCreateAndTransgormImpt = itemCreateAndTransgormImpt;
    }

    public interface ItemCreateAndTransgormImpt {
        View createItem(ViewGroup parent, int position, CharSequence text);

        boolean transform(float offset, View nowItem, View newItem);

        void select(View view, boolean select);
    }

    IndexDrawImpt indexDrawImpt;

    public void setIndexDrawImpt(IndexDrawImpt indexDrawImpt) {
        this.indexDrawImpt = indexDrawImpt;
    }

    public interface IndexDrawImpt {
        void onInit(int w, int itemWidth);

        void onDraw(View v, Canvas canvas, float mTranslationX);
    }
}

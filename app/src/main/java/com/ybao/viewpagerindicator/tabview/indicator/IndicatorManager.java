package com.ybao.viewpagerindicator.tabview.indicator;

import android.graphics.Canvas;
import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.ybao.viewpagerindicator.tabview.scroll.ScrollTabBoxImpt;

/**
 * Created by ybao on 2017/9/3.
 */

public class IndicatorManager {
    private TargetImpt targetImpt;
    public static int LOCK_NONE = 0;
    public static int LOCK_START = -1;
    public static int LOCK_END = -2;
    public static int LOCK_MID = -3;
    public static int LOCK_BETWEEN = -4;
    boolean isInit = false;

    int tempPos = -1;
    int pos = -1;
    int cacheOldPos = -1;

    TabIndicatorAdapter adapter;

    float mTranslationD = 0;
    float mTempSize = 0;

    int showOffSetItem = 0;

    private int lockType = LOCK_MID;

    public IndicatorManager(TargetImpt targetImpt) {
        this.targetImpt = targetImpt;
    }

    public void setAdapter(TabIndicatorAdapter adapter) {
        this.adapter = adapter;
    }

    public void setLockType(int lockType) {
        this.lockType = lockType;
    }

    public void setShowOffSetItem(int showOffSetItem) {
        this.showOffSetItem = showOffSetItem;
    }

    public void onDataChanged() {
        isInit = true;
        select(0);
    }

    public void onMeasure() {
        if (!isInit) {
            return;
        }
        isInit = false;
        if (tempPos >= 0) {
            select(tempPos);
            tempPos = -1;
        } else if (adapter != null && targetImpt.getItemCount() > 0) {
            adapter.afterMeasure();
        }
    }

    public void dispatchDraw(Canvas canvas) {
        if (adapter != null && targetImpt.getItemCount() > 0) {
            adapter.drawIndicator(canvas, mTranslationD, (int) mTempSize);
        }
    }

    Animator animator;

    public void smoothScroll(final int toPosition) {
        if (isInit) {
            tempPos = toPosition;
            return;
        }
        if (toPosition < 0 || toPosition > targetImpt.getItemCount() - 1) {
            return;
        }
        stopScroll();

        View fromView = targetImpt.getItemAt(pos);
        View toView = targetImpt.getItemAt(toPosition);
        int targetViewSize = targetImpt.getMeasuredSize(targetImpt.getTargetView());


        float _fromViewSize = 0;
        float _toViewSize = 0;
        float _fromOffset = 0;
        float _toOffset = 0;
        float _fromTranslationD = 0;
        float _toTranslationD = 0;
        if (fromView != null) {
            if (adapter != null) {
                adapter.transform(fromView, 1);
            }
            _fromViewSize = mTempSize;
            _fromTranslationD = mTranslationD;
            _fromOffset = targetImpt.getOffset();
        }
        if (toView != null) {
            if (adapter != null) {
                adapter.transform(toView, 0);
            }
            _toViewSize = targetImpt.getMeasuredSize(toView);
            _toTranslationD = targetImpt.getItemStart(toView) + _toViewSize / 2;
        }

        int dc = 1;
        if (lockType == LOCK_START) {
            dc = -1;
        } else if (lockType == LOCK_END) {
            dc = 1;
        } else {
            dc = toPosition > pos ? 1 : -1;
        }
        View toNextView = null;
        float _toNextViewSize = 0;
        float _toNextTranslationD = 0;
        if (showOffSetItem > 0 && (toNextView = targetImpt.getItemAt(toPosition + dc * showOffSetItem)) != null) {
            _toNextViewSize = targetImpt.getMeasuredSize(toNextView);
            _toNextTranslationD = targetImpt.getItemStart(toNextView) + _toNextViewSize / 2;
        } else {
            _toNextViewSize = _toViewSize;
            _toNextTranslationD = _toTranslationD;
        }

        if (lockType > 0) {
            _toOffset = _toTranslationD - lockType;
        } else if (lockType == LOCK_MID) {
            _toOffset = _toTranslationD - targetViewSize / 2;
        } else if (lockType == LOCK_START) {
            _toOffset = _toNextTranslationD - _toNextViewSize / 2;
        } else if (lockType == LOCK_END) {
            _toOffset = _toNextTranslationD - targetViewSize + _toNextViewSize / 2;
        } else if (lockType == LOCK_BETWEEN) {
            int start = (int) (_toNextTranslationD + _toNextViewSize / 2 - targetViewSize);
            int end = (int) (_toNextTranslationD - _toNextViewSize / 2);
            if (start > targetImpt.getOffset()) {
                _toOffset = start;
            } else if (end < targetImpt.getOffset()) {
                _toOffset = end;
            } else {
                _toOffset = _fromOffset;
            }
        } else {
            _toOffset = _fromOffset;
        }
        final float fromTranslationD = _fromTranslationD;
        final float fromViewSize = _fromViewSize;
        final float fromOffset = _fromOffset;

        final float toTranslationD = _toTranslationD;
        final float toViewSize = _toViewSize;
        final float toOffset = _toOffset;

        this.pos = toPosition;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f = Float.parseFloat(animation.getAnimatedValue().toString());

                float nowOffset = (toOffset - fromOffset) * f + fromOffset;
                targetImpt.scrollTo(nowOffset);

                float nowTranslationD = (toTranslationD - fromTranslationD) * f + fromTranslationD;
                mTranslationD = nowTranslationD;
                float nowViewSize = (toViewSize - fromViewSize) * f + fromViewSize;
                mTempSize = nowViewSize;
                targetImpt.getTargetView().invalidate();
            }
        });
        animator = valueAnimator;
        animator.start();
    }

    public void stopScroll() {
        if (animator != null) {
            if (animator.isRunning()) {
                animator.cancel();
            }
            animator = null;
        }
    }

    public void reSet() {
        stopScroll();
        pos = -1;
        mTranslationD = 0;
        mTempSize = 0;
    }

    public void select(int toPosition) {
        stopScroll();
        if (isInit) {
            tempPos = toPosition;
            return;
        }
        scrollToPositionOffset(pos, toPosition, 1);
    }

    public void scrollToPositionOffset(int fromPosition, int toPosition, float offset) {
        if (isInit) {
            return;
        }
        if (animator != null && animator.isRunning()) {
            return;
        }
        View cacheOldView = targetImpt.getItemAt(cacheOldPos);
        if (cacheOldView != null) {
            if (adapter != null) {
                adapter.transform(cacheOldView, 1);
            }
        }
        cacheOldPos = fromPosition;
        this.pos = toPosition;
        View nowView = targetImpt.getItemAt(fromPosition);
        View newView = targetImpt.getItemAt(toPosition);
        int targetViewSize = targetImpt.getMeasuredSize(targetImpt.getTargetView());

        int nowSize = 0;
        int nowStart = 0;
        if (nowView != null) {
            nowSize = targetImpt.getMeasuredSize(nowView);
            nowStart = targetImpt.getItemStart(nowView) + nowSize / 2;
            if (adapter != null) {
                adapter.transform(nowView, offset);
            }
        }
        int newSize = 0;
        int newStart = 0;
        if (newView != null) {
            newSize = targetImpt.getMeasuredSize(newView);
            newStart = targetImpt.getItemStart(newView) + newSize / 2;
            if (adapter != null) {
                adapter.transform(newView, 1 - offset);
            }
        }

        mTempSize = nowSize * (1 - offset) + newSize * offset;
        mTranslationD = nowStart + (newStart - nowStart) * offset;

        int dc = 1;
        if (lockType == LOCK_START) {
            dc = -1;
        } else if (lockType == LOCK_END) {
            dc = 1;
        } else {
            dc = toPosition > fromPosition ? 1 : -1;
        }

        View toNextView = null;
        float _toNextViewSize = 0;
        float _toNextTranslationD = 0;
        if (showOffSetItem > 0 && (toNextView = targetImpt.getItemAt(toPosition + dc * showOffSetItem)) != null) {
            int newNextSize = targetImpt.getMeasuredSize(toNextView);
            int newNextStart = targetImpt.getItemStart(toNextView) + newNextSize / 2;
            _toNextViewSize = nowSize * (1 - offset) + newNextSize * offset;
            _toNextTranslationD = nowStart + (newNextStart - nowStart) * offset;
        } else {
            _toNextViewSize = mTempSize;
            _toNextTranslationD = mTranslationD;
        }

        if (lockType > 0) {
            targetImpt.scrollTo(mTranslationD - mTempSize / 2 - lockType);
        } else if (lockType == LOCK_MID) {
            targetImpt.scrollTo(mTranslationD - targetViewSize / 2);
        } else if (lockType == LOCK_START) {
            int start = (int) (_toNextTranslationD - _toNextViewSize / 2);
            targetImpt.scrollTo(start);
        } else if (lockType == LOCK_END) {
            int end = (int) (_toNextTranslationD + _toNextViewSize / 2 - targetViewSize);
            targetImpt.scrollTo(end);
        } else if (lockType == LOCK_BETWEEN) {
            int start = (int) (_toNextTranslationD + _toNextViewSize / 2 - targetViewSize);
            int end = (int) (_toNextTranslationD - _toNextViewSize / 2);
            if (start > targetImpt.getOffset()) {
                targetImpt.scrollTo(start);
            } else if (end < targetImpt.getOffset()) {
                targetImpt.scrollTo(end);
            }
        }
        targetImpt.getTargetView().invalidate();
        if (adapter != null && targetImpt.getItemCount() > 0) {
            adapter.onScrollToPositionOffset(nowView, newView, offset, mTranslationD, mTempSize);
        }
    }

    public interface TargetImpt {

        int getItemStart(View item);

        void scrollTo(float d);

        int getMeasuredSize(View view);

        int getOffset();

        View getTargetView();

        int getItemCount();

        View getItemAt(int index);
    }

    public interface HelperImpt extends ScrollTabBoxImpt {
        void setAdapter(TabIndicatorAdapter adapter);

        void setLockType(int lockType);

        void setShowOffSetItem(int showOffSetItem);

        void scrollToPositionOffset(int fromPosition, int toPosition, float offset);

        void select(int toPosition);

        void smoothScroll(int toPosition);

        void stopScroll();

        void reSet();
    }
}

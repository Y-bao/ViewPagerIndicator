package com.ybao.viewpagerindicator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ybao.viewpagerindicator.tabview.HViewPagerTabAdapter;
import com.ybao.viewpagerindicator.tabview.indicator.HTabIndicator;
import com.ybao.viewpagerindicator.tabview.indicator.IndicatorManager;

public class MainActivity extends FragmentActivity {
    HTabIndicator viewPagerIndicator;
    ViewPager viewPager;
    MyPagerAdapter myPagerAdapter;
    private Paint mPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        setContentView(R.layout.activity_main);
        viewPagerIndicator = (HTabIndicator) findViewById(R.id.vpid);
        viewPagerIndicator.setLockType(IndicatorManager.LOCK_START);
        viewPagerIndicator.setShowOffSetItem(1);
        viewPager = (ViewPager) findViewById(R.id.vp);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        hViewPagerTabAdapter.setViewPager(viewPager);
        viewPagerIndicator.setAdapter(hViewPagerTabAdapter);
    }

    HViewPagerTabAdapter hViewPagerTabAdapter = new HViewPagerTabAdapter() {
        private Path mPath = new Path();

        @Override
        protected View getView(int position, ViewGroup parent, CharSequence name) {
            TextView textView = new TextView(parent.getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setText(name);
            return textView;
        }

        @Override
        protected void onDrawIndicator(Canvas canvas, float translationD, float tempSize) {
            mPath.reset();
            mPath.moveTo(0, 0);
            mPath.lineTo(tempSize, 0);
            mPath.lineTo(tempSize, -10);
            mPath.lineTo(0, -10);
            mPath.close();

            // 初始时的偏移量
            float x = translationD - tempSize / 2;

            canvas.save();
            // 画笔平移到正确的位置
            canvas.translate(x, viewPagerIndicator.getMeasuredHeight() + 1);
            canvas.drawPath(mPath, mPaint);
            canvas.restore();
        }

        @Override
        protected void onTransform(View item, float offset) {
            super.onTransform(item, offset);
        }
    };
}

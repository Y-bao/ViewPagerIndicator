package com.ybao.viewpagerindicator;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
    ViewPagerIndicator viewPagerIndicator;
    ViewPager viewPager;
    MyPagerAdapter myPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPagerIndicator = (ViewPagerIndicator) findViewById(R.id.vpid);
        viewPager = (ViewPager) findViewById(R.id.vp);

        viewPagerIndicator.setItemCreateAndTransgormImpt(new ViewPagerIndicator.ItemCreateAndTransgormImpt() {
            @Override
            public View createItem(ViewGroup parent, int position, CharSequence text) {
                TextView textView = new TextView(parent.getContext());
                textView.setText(text);
                return textView;
            }

            @Override
            public boolean transform(float offset, View nowItem, View newItem) {
                return false;
            }

            @Override
            public void select(View view, boolean select) {
                if (view != null) {
                    ((TextView) view).setTextColor(select ? Color.GREEN : Color.BLACK);
                }
            }
        });
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPagerIndicator.setViewPager(viewPager);
        viewPagerIndicator.setAdapter(myPagerAdapter);
    }
}

package com.ybao.viewpagerindicator.tabview.scroll;

import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Y-bao on 2017/8/17 0017.
 */

public abstract class TabAdapter<T extends ScrollTabBoxImpt, K> extends BaseAdapter {
    protected T t;

    List<K> datas;

    public void setScrollTabBox(T t) {
        this.t = t;
    }

    public T getScrollTabBox() {
        return t;
    }

    public void setDatas(List<K> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public K getItem(int position) {
        if (datas == null) {
            return null;
        }
        return datas.get(position);
    }

    @Override
    public int getCount() {
        if (datas == null) {
            return 0;
        }
        return datas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}

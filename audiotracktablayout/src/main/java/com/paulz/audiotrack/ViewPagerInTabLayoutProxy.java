package com.paulz.audiotrack;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by Paul Z on 2018/10/12.
 * Description:
 */

public class ViewPagerInTabLayoutProxy implements IPagerInTabLayoutProxy{
    ViewPager pager;
    public ViewPagerInTabLayoutProxy(ViewPager viewPager){
        pager=viewPager;
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        pager.setCurrentItem(item,smoothScroll);
    }

    @Override
    public int getCurrentItem() {
        return pager.getCurrentItem();
    }

    @Override
    public void removeOnPageChangeListener(Object listener) {
        pager.removeOnPageChangeListener((ViewPager.OnPageChangeListener) listener);
    }

    @Override
    public void addOnPageChangeListener(Object listener) {
        pager.addOnPageChangeListener((ViewPager.OnPageChangeListener) listener);
    }

    @Override
    public PagerAdapter getAdapter() {
        return pager.getAdapter();
    }
}

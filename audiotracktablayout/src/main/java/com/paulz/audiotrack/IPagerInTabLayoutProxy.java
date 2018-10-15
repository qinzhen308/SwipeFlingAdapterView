package com.paulz.audiotrack;

import android.support.v4.view.PagerAdapter;

/**
 * Created by Paul Z on 2018/10/12.
 * Description:
 */

public interface IPagerInTabLayoutProxy {

    void setCurrentItem(int item, boolean smoothScroll);
    int getCurrentItem();
    void removeOnPageChangeListener(Object listener);
    void addOnPageChangeListener(Object listener);

    PagerAdapter getAdapter();

}

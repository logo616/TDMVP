package com.tdyh.android.base;

/**
 * Created by Administrator on 2017/10/18 0018.
 */

public interface PresenterFactory<T extends Presenter> {
    T create();
}

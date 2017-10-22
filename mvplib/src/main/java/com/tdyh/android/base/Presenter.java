package com.tdyh.android.base;

/**
 * Created by Administrator on 2017/10/17 0017.
 */

public interface Presenter<V extends BaseView> {


    void attachView(V view);

    void detachView();

}
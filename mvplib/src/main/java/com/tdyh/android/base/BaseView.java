package com.tdyh.android.base;

import android.content.Context;

/**
 * Created by Administrator on 2017/10/17 0017.
 */

public interface BaseView {

/*
    //这个可以在Activity中包裹Fragment的时候应用，这时候继承MVPBaseActivity
    //Activity中初始化Presenter的实例 ，然后通过view调用该方法将Presenter塞给Fragment
    void setPresenter(P com.tdyh.android.tdmvp.presenter);
*/

    Context getViewContext();

    void showLoading(String msg);

    void hideLoading();

    void showError(String message);

}

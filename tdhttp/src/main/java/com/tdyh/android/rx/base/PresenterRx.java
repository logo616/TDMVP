package com.tdyh.android.rx.base;

import io.reactivex.disposables.Disposable;

/**
 * Created by gzh on 2017/10/20 0020.
 */

public interface PresenterRx {

    //将网络请求的每一个disposable添加进入CompositeDisposable，再退出时候一并注销
    void addDisposable(Disposable subscription);

    //注销所有请求
    void unDisposable();



}

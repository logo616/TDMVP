package com.tdyh.android.base;

import android.util.Log;

import android.support.annotation.Nullable;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/10/17 0017.
 */

public  class BasePresenter<V extends BaseView>  implements Presenter<V> {

    private Reference<V> mViewRef;//View 接口类型的弱引用

    @Override
    public void attachView(V view) {
        if (mViewRef!=null && isViewAttached() && getView()==view){
            Log.d("BasePresenter","attachView again");
        }else {
            mViewRef = new WeakReference<V>(view);
        }
    }

    @Nullable
    protected V getView() {
        return mViewRef!=null?mViewRef.get():null;
    }

    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    @Override
    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    /**
     * 每次调用业务请求的时候都要先调用方法检查是否与View建立连接，没有则抛出异常
     */
    public void checkViewAttached() {
        if (!isViewAttached()) {
            throw new MvpViewNotAttachedException();
        }
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("请求数据前请先调用 attachView(MvpView) 方法与View建立连接");
        }
    }



}
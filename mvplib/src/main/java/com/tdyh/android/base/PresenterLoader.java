package com.tdyh.android.base;

import android.content.Context;
import android.content.Loader;

/**
 *
 * 存放Presenter的缓存
 * http://www.jianshu.com/p/d5828ea38b3c
 * Created by Administrator on 2017/10/18 0018.
 */

public class PresenterLoader<T extends Presenter> extends Loader<T> {

    private final PresenterFactory<T> factory;

    private T presenter;

    public PresenterLoader(Context context, PresenterFactory factory) {
        super(context);
        this.factory = factory;
    }

    @Override
    protected void onStartLoading() {
        if (presenter != null) {
            deliverResult(presenter);
            return;
        }
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        presenter = factory.create();
        deliverResult(presenter);
    }

    @Override
    protected void onReset() {
        presenter.detachView();
        presenter = null;
    }
}


package com.tdyh.android.rx.base;

import com.tdyh.android.base.BasePresenter;
import com.tdyh.android.base.BaseView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by gzh on 2017/10/20 0020.
 */

public class BaseRxPresenter<V extends BaseView> extends BasePresenter<V > implements PresenterRx {


    //以下下为配合RxJava2+retrofit2使用的

    //将所有正在处理的Subscription都添加到CompositeSubscription中。统一退出的时候注销观察
    private CompositeDisposable mCompositeDisposable;

    /**
     * 将Disposable添加
     *
     * @param subscription
     */
    @Override
    public void addDisposable(Disposable subscription) {
        //csb 如果解绑了的话添加 sb 需要新的实例否则绑定时无效的
        if (mCompositeDisposable == null || mCompositeDisposable.isDisposed()) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    /**
     * 在界面退出等需要解绑观察者的情况下调用此方法统一解绑，防止Rx造成的内存泄漏
     */
    @Override
    public void unDisposable() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    @Override
    public void detachView(){
        super.detachView();
//        unDisposable();
    }


}

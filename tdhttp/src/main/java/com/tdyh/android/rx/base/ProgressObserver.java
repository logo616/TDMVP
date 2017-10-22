package com.tdyh.android.rx.base;

import android.content.Context;
import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by gzh on 2017/10/20 0020.
 */

public class ProgressObserver<T> implements Observer<T>, ProgressCancelListener {
    private static final String TAG = "ProgressObserver";
    private ObserverOnNextListener listener;
    private ProgressDialogHandler mProgressDialogHandler;
//    private Context context;
    private Disposable d;

    public ProgressObserver(Context context, ObserverOnNextListener listener) {
        this(context,null,listener);
    }

    public ProgressObserver(Context context, String loadingMsg,ObserverOnNextListener listener) {
        this.listener = listener;
//        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context,loadingMsg, this, true);
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG)
                    .sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    /**
     * 订阅时，自动弹窗进度框
     * @param d
     */
    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        showProgressDialog();
    }

    @Override
    public void onNext(T t) {
        listener.onNext(t);
    }

    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        Log.e(TAG, "onError: ", e);
    }

    @Override
    public void onComplete() {
        dismissProgressDialog();
        Log.d(TAG, "onComplete: ");
    }

    @Override
    public void onCancelProgress() {
        //如果处于订阅状态，则取消订阅
        if (!d.isDisposed()) {
            d.dispose();
        }
    }
}
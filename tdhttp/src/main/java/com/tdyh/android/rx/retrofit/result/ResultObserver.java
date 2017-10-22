package com.tdyh.android.rx.retrofit.result;



import com.tdyh.android.rx.Constants;


import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by dugang on 2017/4/12. 网络请求结果处理
 */
@SuppressWarnings("unused")
public abstract class ResultObserver<T> implements Observer<Result<T>> {

    @Override public void onSubscribe(Disposable d) {

    }

    @Override public void onNext(Result<T> result) {
        if (result != null) {
            if (result.getCode() == Constants.NET_CODE_SUCCESS) {
                handlerResult(result.getResult());
            } else {
                handlerError(result.getCode(), result.getMsg());
            }
        } else {
            handlerError(Constants.NET_CODE_ERROR, Constants.EMPTY_RESPONSE_EXCEPTION);
        }
    }

    @Override public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            handlerError(Constants.NET_CODE_SOCKET_TIMEOUT, Constants.SOCKET_TIMEOUT_EXCEPTION);
        } else if (e instanceof ConnectException) {
            handlerError(Constants.NET_CODE_CONNECT, Constants.CONNECT_EXCEPTION);
        } else if (e instanceof UnknownHostException) {
            handlerError(Constants.NET_CODE_UNKNOWN_HOST, Constants.UNKNOWN_HOST_EXCEPTION);
        } else {
            handlerError(Constants.NET_CODE_ERROR, e.getMessage());
        }
    }

    @Override public void onComplete() {

    }

    /**
     * 返回正常数据
     */
    public abstract void handlerResult(T t);

    /**
     * 返回业务错误
     */
    public void handlerError(int code, String msg) {
        //可以统一做处理
    }

}

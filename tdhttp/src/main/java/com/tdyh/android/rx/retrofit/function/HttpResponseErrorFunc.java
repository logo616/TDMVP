package com.tdyh.android.rx.retrofit.function;

import com.tdyh.android.rx.retrofit.eception.ExceptionHandler;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * http请求错误处理
 * Created by gaozh on 2019/4/17.<p>
 */
public class HttpResponseErrorFunc<T> implements Function<Throwable, Observable<T>> {

    @Override
    public Observable<T> apply(Throwable throwable) throws Exception {

        return Observable.error(ExceptionHandler.handleException(throwable));
    }
}

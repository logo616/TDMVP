package com.tdyh.android.rx.retrofit.result;


import com.tdyh.android.rx.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dugang on 2017/4/18.ResultCallBack
 */

public abstract class ResultCallBack<T> implements Callback<Result<T>> {
    @Override public void onResponse(Call<Result<T>> call, Response<Result<T>> response) {
        if (response != null && response.body()!=null) {
            if (response.body().getCode() == Constants.NET_CODE_SUCCESS) {
                handlerResult(true, null, response.body().getResult());
            } else {
                handlerResult(false, new Throwable(response.body().getMsg()), null);
            }
        } else {
            handlerResult(false, new Throwable(Constants.EMPTY_RESPONSE_EXCEPTION), null);
        }
    }

    @Override public void onFailure(Call<Result<T>> call, Throwable t) {
        handlerResult(false, t, null);
    }

    public abstract void handlerResult(boolean success, Throwable throwable, T t);
}

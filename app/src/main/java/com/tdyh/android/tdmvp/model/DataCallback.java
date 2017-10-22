package com.tdyh.android.tdmvp.model;

/**
 * Created by Administrator on 2017/10/18 0018.
 */

public interface DataCallback {
    void onSuccess();
    void onFail(String errorCode,String errorMsg);
}

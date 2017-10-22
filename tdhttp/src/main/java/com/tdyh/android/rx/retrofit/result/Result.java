package com.tdyh.android.rx.retrofit.result;

import java.io.Serializable;

/**
 * Created by dugang on 2017/3/15. 网络请求结果
 */
@SuppressWarnings("unused")
public class Result<T> implements Serializable {
    private int code;
    private String msg;
    private T result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}

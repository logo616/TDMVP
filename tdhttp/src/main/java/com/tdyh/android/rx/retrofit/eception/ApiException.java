package com.tdyh.android.rx.retrofit.eception;

/**
 *  Api异常信息
 * Created by gaozh on 2019/8/30.<p>
 */
public class ApiException extends Exception {

    private String code;
    private String msg;
    private Throwable throwable;
    public ApiException(String code,String msg,Throwable throwable){
        this.code=code;
        this.msg=msg;
        this.throwable= throwable;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}

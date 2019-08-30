package com.tdyh.android.rx.retrofit.eception;

/**
 * Created by gaozh on 2019/8/30.<p>
 */
public class ServerException extends RuntimeException {

    private String code;
    private String msg;

    public ServerException(String code,String msg){
        this.code=code;
        this.msg=msg;
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
}

package com.tdyh.android.rx.retrofit.eception;

import android.net.ParseException;
import android.util.Log;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;

import retrofit2.HttpException;

/**
 * Created by gaozh on 2019/8/30.<p>
 */
public class ExceptionHandler {
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int REQUEST_TIMEOUT = 408;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;

    /**
     * 未知错误
     */
    public static final String UNKNOWN = "1000";
    /**
     * ;
     * 解析错误;
     */
    public static final String PARSE_ERROR = "1001";
    /**
     * ;
     * 网络错误;
     */
    public static final String NETWORD_ERROR = "1002";
    /**
     * ;
     * 协议出错;
     */
    public static final String HTTP_ERROR = "1003";

    /**
     * ;
     * 证书出错;
     */
    public static final String SSL_ERROR = "1005";


    public static ApiException handleException(Throwable e) {
        Log.e("ExceptionHandler", e.toString());
        ApiException ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ApiException(httpException.code() + "", httpException.message(), e);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                    ex.setMsg("网络错误");
                    break;
                default:
                    ex.setMsg("网络错误");
                    break;
            }
            return ex;
        } else if (e instanceof ServerException) {
            ServerException serverException = (ServerException) e;
            ex = new ApiException(serverException.getCode(), serverException.getMsg(), e);
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException
        ) {
            ex = new ApiException(PARSE_ERROR, "解析错误", e);
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ApiException(NETWORD_ERROR, "连接失败", e);
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ApiException(SSL_ERROR, "证书验证失败", e);
            return ex;
        } else {
            ex = new ApiException(UNKNOWN, "未知错误", e);
            return ex;
        }
    }
}

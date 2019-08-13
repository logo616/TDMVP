package com.tdyh.android.okhttp.multidownload;

import android.util.Log;

import com.tdyh.android.okhttp.https.HttpsUtils;
import com.tdyh.android.okhttp.log.LoggerInterceptor;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author
 * @date 2017/8/20
 */
public class OkHttpStack implements HttpStack {

    private static final int DEFAULT_TIMEOUT = 3*60;
    private InputStream is;
    private ResponseBody body;
    private long contentLength=-1;
    private static OkHttpClient mHttpClient;

    public OkHttpStack() {
        if (mHttpClient==null) {
            synchronized (OkHttpStack.class) {
                if (mHttpClient==null) {
                    HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
                    mHttpClient = new OkHttpClient().newBuilder()
                            .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .addInterceptor(new LoggerInterceptor("OkHttpStack",true))
                            .build();
                }
            }
        }

    }

    @Override
    public long getContentLength(String downloadUrl) throws Exception {
        Request request = new Request.Builder()
                .get()
                .url(downloadUrl)
                .build();

        Call call = mHttpClient.newCall(request);
        Response response = call.execute();
        if (response.isSuccessful()) {
                ResponseBody body = response.body();
                contentLength = body.contentLength();
                body.close();
        }
        return contentLength;
    }

    @Override
    public InputStream download(String downloadUrl, long startIndex, long endIndex) throws Exception {
        Request request = new Request.Builder().header("RANGE", "bytes=" + startIndex + "-" + endIndex)
                .url(downloadUrl)
                .build();
        Log.e("","下载线程："+startIndex +  " endIndex="+endIndex);
            Call call = mHttpClient.newCall(request);

            Response response = call.execute();
            if (response.isSuccessful()) {
                body = response.body();
                is = body.byteStream();
            }else {
                throw  new RuntimeException("错误码："+response.code());
            }
        return is;
    }

    @Override
    public void close() {
        if (is != null) {
            try {
                body.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

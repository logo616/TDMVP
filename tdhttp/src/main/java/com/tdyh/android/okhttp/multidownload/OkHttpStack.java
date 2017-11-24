package com.tdyh.android.okhttp.multidownload;

import com.tdyh.android.okhttp.OkHttpUtils;
import com.tdyh.android.okhttp.https.HttpsUtils;
import com.tdyh.android.okhttp.log.LoggerInterceptor;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 */
public class OkHttpStack implements HttpStack {

    private static final int DEFAULT_TIMEOUT = 3 * 60;
    private static OkHttpClient mHttpClient;

    public OkHttpStack() {
        getHttpClient();
    }


    private void getHttpClient() {
        if (mHttpClient == null) {
            synchronized (OkHttpStack.class) {
                if (mHttpClient == null) {
                    HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);//部分请求出现超时
                    mHttpClient = OkHttpUtils.getInstance().getOkHttpClient().newBuilder()
                            .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .addInterceptor(new LoggerInterceptor("OkHttpStack", true))
                            .build();
                }
            }
        }
    }

    @Override
    public long getContentLength(String downloadUrl) throws Exception {
        long contentLength = -1;
        Request request = new Request.Builder()
                .get()
                .url(downloadUrl)
                .build();

        Call call = mHttpClient.newCall(request);
        Response response = call.execute();
        if (response.isSuccessful()) {
            ResponseBody body = response.body();
            if (body != null) {
                contentLength = body.contentLength();
                body.close();
            }
        }
        return contentLength;
    }

    @Override
    public InputStream download(String downloadUrl, long startIndex, long endIndex) throws Exception {
        InputStream is = null;
        Request request = new Request.Builder().header("RANGE", "bytes=" + startIndex + "-" + endIndex)
                .url(downloadUrl)
                .build();

        Call call = mHttpClient.newCall(request);

        Response response = call.execute();
        if (response.isSuccessful()) {
            if (response.body() != null) {
                is = response.body().byteStream();
            }
        } else {
            throw new RuntimeException("错误码：" + response.code());
        }
        return is;
    }
}

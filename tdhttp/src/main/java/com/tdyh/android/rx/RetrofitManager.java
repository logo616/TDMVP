package com.tdyh.android.rx;


import android.os.Build;
import android.support.annotation.NonNull;

import com.tdyh.android.rx.retrofit.interceptor.HeaderInterceptor;
import com.tdyh.android.rx.retrofit.interceptor.ParamsInterceptor;
import com.tdyh.android.rx.utils.SystemUtil;
import com.tdyh.android.rx.utils.Utils;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dugang on 2017/3/15.Retrofit管理类
 */

public class RetrofitManager {

    private static volatile Retrofit INSTENCE;

    /**
     * 配置网络请求的url
     */
    private static String getBaseUrl() {
        return "https://api.douban.com/v2/movie/";
    }

    /**
     * 配置网络请求缓存
     */
    private static Cache getCache() {
        return new Cache(Utils.getContext().getCacheDir(), 1024 * 1024 * 50);
    }

    /**
     * 配置网络请求头
     */
    private static HashMap<String, String> getRequestHeader() {
        HashMap<String, String> header = new HashMap<>();
        header.put("app_version", SystemUtil.getVersionName());
        header.put("app_build", "" + SystemUtil.getVersionCode());
        header.put("device_name", Build.MODEL);
        header.put("device_platform", "Android");

/*        if (ShareData.getInstance().getAccount() != null)
            header.put("token", ShareData.getInstance().getAccount().getToken());*/

        return header;
    }

    /**
     * 配置网络请求体
     */
    public static HashMap<String, String> getRequestParams() {
        return null;
    }

    /**
     * 获取Retrofit
     */
    public static Retrofit getInstance() {
        if (INSTENCE==null) {
            synchronized (Retrofit.class) {
                if (INSTENCE==null) {
                    OkHttpClient okHttpClient = getOkHttpClient();
                    Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
                    retrofitBuilder.baseUrl(getBaseUrl());
                    retrofitBuilder.client(okHttpClient);
                    retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
                    retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
                    INSTENCE = retrofitBuilder.build();
                }
            }
        }
        return INSTENCE;
    }


    public static Retrofit getInstance(String baseUrl) {

             OkHttpClient okHttpClient = getOkHttpClient();

            Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
            retrofitBuilder.baseUrl(baseUrl);
            retrofitBuilder.client(okHttpClient);
            retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
            retrofitBuilder.addConverterFactory(GsonConverterFactory.create());

            return  retrofitBuilder.build();
    }

    @NonNull
    private static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        //debug模式添加log信息拦截
        if (Utils.isDebug()) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpBuilder.addInterceptor(interceptor);
        }
        okHttpBuilder.addNetworkInterceptor(new HeaderInterceptor(getRequestHeader()));
        okHttpBuilder.addNetworkInterceptor(new ParamsInterceptor(getRequestParams()));
        okHttpBuilder.cache(getCache());

        //设置连接超时
        okHttpBuilder.connectTimeout(10, TimeUnit.SECONDS);
        //设置写超时
        okHttpBuilder.writeTimeout(10, TimeUnit.SECONDS);
        //设置读超时
        okHttpBuilder.readTimeout(10, TimeUnit.SECONDS);
        return okHttpBuilder.build();
    }
}

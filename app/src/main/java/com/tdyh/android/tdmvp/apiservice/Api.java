package com.tdyh.android.tdmvp.apiservice;

import com.tdyh.android.rx.RetrofitManager;

/**
 * Created by gzh on 2017/10/21 0021.
 */

public class Api {

    public static MovieService apiService;
    //单例
    public static MovieService getApiService() {
        if (apiService == null) {
            synchronized (Api.class) {
                if (apiService == null) {
                    new Api();
                }
            }
        }
        return apiService;
    }

    private Api() {
        apiService =  RetrofitManager.getInstance().create(MovieService.class);
    }
}

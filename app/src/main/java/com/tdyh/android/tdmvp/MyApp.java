package com.tdyh.android.tdmvp;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.tdyh.android.rx.utils.Utils;

/**
 * Created by Administrator on 2017/10/18 0018.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        Utils.init(this,true);
    }
}

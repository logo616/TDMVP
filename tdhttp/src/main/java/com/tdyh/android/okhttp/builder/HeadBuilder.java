package com.tdyh.android.okhttp.builder;

import com.tdyh.android.okhttp.OkHttpUtils;
import com.tdyh.android.okhttp.request.OtherRequest;
import com.tdyh.android.okhttp.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}

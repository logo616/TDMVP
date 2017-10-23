package com.tdyh.android.okhttp.multidownload;

import java.io.InputStream;


public interface HttpStack {

    InputStream download(String downloadUrl, long startIndex, long endIndex) throws Exception;

    long getContentLength(String downloadUrl) throws Exception;

}



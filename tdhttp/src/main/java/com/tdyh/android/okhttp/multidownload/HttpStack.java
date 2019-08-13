package com.tdyh.android.okhttp.multidownload;

import java.io.InputStream;

/**
 * @author
 * @date 2017/8/20
 */
public interface HttpStack {

    InputStream download(String downloadUrl, long startIndex, long endIndex) throws Exception;

    void close();

    long getContentLength(String downloadUrl) throws Exception;

}

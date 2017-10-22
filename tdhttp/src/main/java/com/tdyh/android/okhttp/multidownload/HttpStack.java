package com.tdyh.android.okhttp.multidownload;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author djcken
 * @date 2017/8/20
 */
public interface HttpStack {

    InputStream download(String downloadUrl, long startIndex, long endIndex) throws Exception;

    void close();

    long getContentLength(String downloadUrl) throws Exception;

}

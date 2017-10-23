package com.tdyh.android.okhttp.multidownload;

/**
 * 下载监听
 */
public interface DownloadListner {
    void onFinished();

    void onProgress(float progress);

    void onPause();

    void onCancel();

    void onFail(String msg);
}

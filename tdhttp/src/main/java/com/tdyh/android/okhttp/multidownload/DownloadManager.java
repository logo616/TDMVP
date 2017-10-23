package com.tdyh.android.okhttp.multidownload;

import android.text.TextUtils;
import android.util.Log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 下载管理器，断点续传
 *
 */
public class DownloadManager {

    private String DEFAULT_FILE_DIR;//默认下载目录
    private Map<String, DownloadTask> mDownloadTasks;//文件下载任务索引，String为url,用来唯一区别并操作下载的文件
    private static DownloadManager mInstance;
    private static final String TAG = "DownloadManager";
    /**
     * 下载文件
     */
    public void download(String... urls) {
        //单任务开启下载或多任务开启下载
        for (int i = 0, length = urls.length; i < length; i++) {
            String url = urls[i];
            if (mDownloadTasks.containsKey(url)) {
                mDownloadTasks.get(url).start();
            }
        }
    }


    // 获取下载文件的名称
    private String getFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * 暂停
     */
    public void pause(String... urls) {
        //单任务暂停或多任务暂停下载
        for (int i = 0, length = urls.length; i < length; i++) {
            String url = urls[i];
            if (mDownloadTasks.containsKey(url)) {
                mDownloadTasks.get(url).pause();
            }
        }
    }

    /**
     * 取消下载
     */
    public void cancel(String... urls) {
        //单任务取消或多任务取消下载
        for (int i = 0, length = urls.length; i < length; i++) {
            String url = urls[i];
            if (mDownloadTasks.containsKey(url)) {
                mDownloadTasks.get(url).cancel();
            }
        }
    }

    /**
     * 添加下载任务
     */
    public void add(String url, DownloadListner l) {
        add(url, null, null, l);
    }

    /**
     * 添加下载任务
     */
    public void add(String url, String fileDir, DownloadListner l) {
        add(url, fileDir, null, l);
    }

    /**
     * 添加下载任务
     */
    public void add(String url, String fileDir, String fileName, DownloadListner l) {
        if (TextUtils.isEmpty(fileDir)) {//没有指定下载目录,使用默认目录
            fileDir = getDefaultDirectory();
        }
        if (TextUtils.isEmpty(fileName)) {
            fileName = getFileName(url);
        }
        if (!mDownloadTasks.containsKey(url)) {
            //防止重复添加
            mDownloadTasks.put(url, new DownloadTask(new FilePoint(url, fileDir, fileName), l));
        }
    }

    /**
     * 默认下载目录
     * @return
     */
    private String getDefaultDirectory() {
        if (TextUtils.isEmpty(DEFAULT_FILE_DIR)) {
//            DEFAULT_FILE_DIR = SDCardUtil.getProjectVideoDir();
            throw new RuntimeException("DownloadManager must setDefaultDirectory(defaultDir) ");
        }
        return DEFAULT_FILE_DIR;
    }

    public void setDefaultDirectory(String defaultDir){
        if (TextUtils.isEmpty(this.DEFAULT_FILE_DIR)) {
            this.DEFAULT_FILE_DIR = defaultDir;
        }else {
            Log.e(TAG,"DEFAULT_FILE_DIR has exit");
        }
    }

    public static DownloadManager getInstance() {//管理器初始化
        if (mInstance == null) {
            synchronized (DownloadManager.class) {
                if (mInstance == null) {
                    mInstance = new DownloadManager();
                }
            }
        }
        return mInstance;
    }

    private DownloadManager() {
        mDownloadTasks = new ConcurrentHashMap<>();
    }

    /**
     * 取消下载
     */
    public boolean isDownloading(String... urls) {
        //这里传一个url就是判断一个下载任务
        //多个url数组适合下载管理器判断是否作操作全部下载或全部取消下载
        boolean result = false;
        for (int i = 0, length = urls.length; i < length; i++) {
            String url = urls[i];
            if (mDownloadTasks.containsKey(url)) {
                result = mDownloadTasks.get(url).isDownloading();
            }
        }
        return result;
    }


}

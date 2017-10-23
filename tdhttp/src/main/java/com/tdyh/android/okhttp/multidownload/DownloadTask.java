package com.tdyh.android.okhttp.multidownload;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 *
 */
public class DownloadTask implements Runnable {

    private static final String TAG = "DownloadTask";

    private final int THREAD_COUNT = 3;//线程数
    private FilePoint mPoint;
    private long mFileLength;


    private boolean isDownloading = false;
    private int childCanleCount;//子线程取消数量
    private int childPauseCount;//子线程暂停数量
    private int childFinshCount;


    private long[] mProgress;
    private File[] mCacheFiles;
    private File mTmpFile;//临时占位文件
    private boolean pause;//是否暂停
    private boolean cancel;//是否取消下载
    private boolean fail;


    private final int MSG_PROGRESS = 1;//进度
    private final int MSG_FINISH = 2;//完成下载
    private final int MSG_PAUSE = 3;//暂停
    private final int MSG_CANCEL = 4;//暂停
    private final int MSG_FAIL = 5;//失败
    private DownloadListner mListner;//下载回调监听

    private OkHttpStack mOkHttpLenStack;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == mListner) {
                return;
            }
            switch (msg.what) {
                case MSG_PROGRESS://进度
                    long progress = 0;
                    for (long mProgres : mProgress) {
                        progress += mProgres;
                    }
                    mListner.onProgress(progress * 1.0f / mFileLength);
                    break;
                case MSG_PAUSE://暂停
                    childPauseCount++;
                    if (childPauseCount % THREAD_COUNT != 0) return;
                    resetStutus();
                    mListner.onPause();
                    break;
                case MSG_FINISH://完成
                    childFinshCount++;
                    if (childFinshCount % THREAD_COUNT != 0) return;

                    mTmpFile.renameTo(new File(mPoint.getFileDir(), mPoint.getFileName()));//下载完毕后，重命名目标文件名
                    resetStutus();
                    mListner.onFinished();
                    break;
                case MSG_CANCEL://取消
                    childCanleCount++;
                    if (childCanleCount % THREAD_COUNT != 0) return;
                    resetStutus();
                    mProgress = new long[THREAD_COUNT];
                    mListner.onCancel();
                    break;
                case MSG_FAIL://失败

            /*    childFailCount++;
                if (childFailCount % THREAD_COUNT != 0) return;*/
                    resetStutus();
                    String failMsg = "";
                    if (msg.obj != null) {
                        failMsg = (String) msg.obj;
                    }
                    mListner.onFail(failMsg);
                    break;
            }
        }
    };

    /**
     * 任务管理器初始化数据
     *
     * @param point
     * @param l
     */
    DownloadTask(FilePoint point, DownloadListner l) {
        this.mPoint = point;
        this.mListner = l;
        this.mProgress = new long[THREAD_COUNT];
        this.mCacheFiles = new File[THREAD_COUNT];
        mOkHttpLenStack = new OkHttpStack();
    }


    synchronized void start() {

        Log.e(TAG, "start: " + isDownloading + "\t" + mPoint.getUrl());
        if (isDownloading) {
            return;
        }
        isDownloading = true;
        fail = false;

        startDownload();
    }

    private void startDownload() {
        ThreadPoolManager.getInstance().execute(this);
    }

    @Override
    public void run() {

        Log.e(TAG, "长度下载线程：" + Thread.currentThread().getName());
        try {
            mFileLength = mOkHttpLenStack.getContentLength(mPoint.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
            resetStutus();
            Log.e(TAG, "获取下载文件长度失败");
//            childFailCount = THREAD_COUNT - 1;
            sendFailMsg("获取下载文件长度失败:" + e.toString());
            return;
        }
        try {
            // 在本地创建一个与资源同样大小的文件来占位
            mTmpFile = new File(mPoint.getFileDir(), mPoint.getFileName() + ".tmp");
            if (!mTmpFile.getParentFile().exists()) {
                mTmpFile.getParentFile().mkdirs();
            }
            RandomAccessFile tmpAccessFile = new RandomAccessFile(mTmpFile, "rw");
            tmpAccessFile.setLength(mFileLength);


             /*将下载任务分配给每个线程*/
            long blockSize = mFileLength / THREAD_COUNT;// 计算每个线程理论上下载的数量.
                    /*为每个线程配置并分配任务*/
            for (int threadId = 0; threadId < THREAD_COUNT; threadId++) {
                long startIndex = threadId * blockSize; // 线程开始下载的位置
                long endIndex = (threadId + 1) * blockSize - 1; // 线程结束下载的位置
                if (threadId == (THREAD_COUNT - 1)) { // 如果是最后一个线程,将剩下的文件全部交给这个线程完成
                    endIndex = mFileLength - 1;
                }
                download(startIndex, endIndex, threadId);// 开启线程下载
            }
        } catch (Exception e) {
//                        childFailCount = THREAD_COUNT - 1;
            Log.e(TAG, "创建文件失败:" + e.toString());
            sendFailMsg("创建文件失败:" + e.toString());
        }
    }

    private void sendFailMsg(String failMsg) {
        mHandler.sendEmptyMessage(MSG_PAUSE);

        fail = true;
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_FAIL;
        msg.obj = failMsg;
        mHandler.sendMessage(msg);
        Log.d(TAG, "下载文件失败：" + failMsg);
    }

    private void download(final long startIndex, final long endIndex, final int threadId) throws IOException {

        long newStartIndex = startIndex;
        // 分段请求网络连接,分段将文件保存到本地.
        // 加载下载位置缓存文件
        final File cacheFile = new File(mPoint.getFileDir(), "thread" + threadId + "_" + mPoint.getFileName() + ".cache");
        mCacheFiles[threadId] = cacheFile;
        final RandomAccessFile cacheAccessFile = new RandomAccessFile(cacheFile, "rwd");
        if (cacheFile.exists()) {// 如果文件存在
            String startIndexStr = cacheAccessFile.readLine();
            Log.d(TAG, "startIndexStr==" + startIndexStr);
            if (!TextUtils.isEmpty(startIndexStr)) {
                try {
                    newStartIndex = Integer.parseInt(startIndexStr);//重新设置下载起点
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        final long finalStartIndex = newStartIndex;
        Log.d(TAG, String.format("开始下载 threadId=%d ,startIndex=%d ,finalStartIndex=%d ,endIndex=%d", threadId, startIndex, finalStartIndex, endIndex));
        ThreadPoolManager.getInstance().execute(new DownloadThreadTask(threadId, startIndex, endIndex, finalStartIndex, cacheFile, cacheAccessFile));

    }

    private class DownloadThreadTask implements Runnable {

        private long finalStartIndex;
        private RandomAccessFile cacheAccessFile;
        private File cacheFile;
        private int threadId;
        private long startIndex;
        private long endIndex;

        private long mCurrentDownStartIndex;//实质开始下载的初始化位置
        private OkHttpStack mOkHttpStack;

        DownloadThreadTask(int threadId, long startIndex, long endIndex, long currentDownStartIndex, File cacheFile, RandomAccessFile cacheAccessFile) {
            this.threadId = threadId;
            this.startIndex = startIndex;
            this.cacheFile = cacheFile;
            this.cacheAccessFile = cacheAccessFile;
            this.finalStartIndex = currentDownStartIndex;
            this.mCurrentDownStartIndex = currentDownStartIndex;
            this.endIndex = endIndex;
            mOkHttpStack = new OkHttpStack();
        }

        private void saveToCacheAccessFile(long currentDownStartIndex) {
            try {
                Log.d(TAG, "保存当前下载开始位置" + currentDownStartIndex);
                //将当前下载到的位置保存到文件中
                cacheAccessFile.seek(0);
                cacheAccessFile.write((currentDownStartIndex + "").getBytes("UTF-8"));

            } catch (Exception ex) {
                Log.e(TAG, "保存下载长度记录文件失败");
            }
        }

        @Override
        public void run() {

            try {
                Log.e(TAG, threadId + "下载线程：" + Thread.currentThread().getName());
                InputStream  is = mOkHttpStack.download(mPoint.getUrl(), finalStartIndex, endIndex);

                if (is == null) {
                    resetStutus();
                    Log.e(TAG, "下载失败，inputStream==null");
                    sendFailMsg("下载失败");
                    return;
                }

                RandomAccessFile tmpAccessFile = new RandomAccessFile(mTmpFile, "rw");// 获取前面已创建的文件.
                tmpAccessFile.seek(finalStartIndex);// 文件写入的开始位置.
                  /*  将网络流中的文件写入本地*/
                byte[] buffer = new byte[1024 << 2];
                int length;
                int total = 0;// 记录本次下载文件的大小

                while ((length = is.read(buffer)) > 0) {
                    if (cancel) {
                        //关闭资源
                        saveToCacheAccessFile(mCurrentDownStartIndex);
                        close(cacheAccessFile, is);

                        cleanFile(cacheFile);
                        mHandler.sendEmptyMessage(MSG_CANCEL);
                        return;
                    }
                    if (pause) {
                        //关闭资源
                        saveToCacheAccessFile(mCurrentDownStartIndex);
                        close(cacheAccessFile, is);

                        //发送暂停消息
                        mHandler.sendEmptyMessage(MSG_PAUSE);
                        return;
                    }
                    if (fail) {
                        saveToCacheAccessFile(mCurrentDownStartIndex);
                        return;
                    }

                    tmpAccessFile.write(buffer, 0, length);

                    total += length;
                    mCurrentDownStartIndex = finalStartIndex + total;
                    //发送进度消息
                    mProgress[threadId] = mCurrentDownStartIndex - startIndex;
                    mHandler.sendEmptyMessage(MSG_PROGRESS);
                }
                //关闭资源
                close(cacheAccessFile, is);

                // 删除临时文件
                cleanFile(cacheFile);
                //发送完成消息
                mHandler.sendEmptyMessage(MSG_FINISH);
                Log.e(TAG, "当前线程下载完毕:" + threadId);
            } catch (Exception ex) {
                saveToCacheAccessFile(mCurrentDownStartIndex);
                Log.e(TAG, threadId + " 下载过程中 失败:" + ex.toString());
                sendFailMsg("下载过程中 :" + ex.toString());
            }
        }
    }


    /**
     * 关闭资源
     *
     * @param closeables
     */
    private void close(Closeable... closeables) {
        int length = closeables.length;
        try {
            for (int i = 0; i < length; i++) {
                Closeable closeable = closeables[i];
                if (null != closeable)
                    closeables[i].close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            for (int i = 0; i < length; i++) {
                closeables[i] = null;
            }
        }
    }

    /**
     * 删除临时文件
     */
    private void cleanFile(File... files) {
        if (files!=null) {
            for (File file : files) {
                if (null != file)
                    file.delete();
            }
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        pause = true;
    }

    /**
     * 取消
     */
    public void cancel() {
        cancel = true;
        cleanFile(mTmpFile);
        if (!isDownloading) {
            if (null != mListner) {
                cleanFile(mCacheFiles);
                resetStutus();
                mListner.onCancel();
            }
        }
    }

    /**
     * 重置下载状态
     */
    private void resetStutus() {
        pause = false;
        cancel = false;
        isDownloading = false;
    }

    public boolean isDownloading() {
        return isDownloading;
    }
}

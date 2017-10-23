package com.tdyh.android.okhttp.multidownload;

import android.os.Process;
import android.support.annotation.NonNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池管理
 */
public class ThreadPoolManager {

    private static ThreadPoolManager mInstance = new ThreadPoolManager();

    private ThreadPoolExecutor mExecutor;

    private int mCorePoolSize;
    private int mMaximumPoolSize;
    private long mKeepAliveTime = 2;
    private TimeUnit mUnit = TimeUnit.HOURS;
    private BlockingQueue<Runnable> mWorkQueue = new LinkedBlockingQueue<Runnable>();
    private RejectedExecutionHandler mHandler = new ThreadPoolExecutor.AbortPolicy();

    public static ThreadPoolManager getInstance() {
        return mInstance;
    }

    private ThreadPoolManager() {
        //calculate corePoolSize, which is the same to AsyncTask.
        mCorePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
        mMaximumPoolSize = mCorePoolSize;
        //we custom the threadpool.
        mExecutor = new ThreadPoolExecutor(
                mCorePoolSize, //is 3 in avd.
                mMaximumPoolSize, //which is unuseless
                mKeepAliveTime,
                mUnit,
                mWorkQueue,
                new DownloadThreadFactory(),
                mHandler
        );

    }


    public void setCorePoolSize(int size) {
        this.mCorePoolSize = size;
        mExecutor.setCorePoolSize(mCorePoolSize);
    }

    /**
     * 往线程池中添加任务
     *
     * @param runnable
     */
    public void execute(Runnable runnable) {
        if (runnable != null) {
            mExecutor.execute(runnable);
        }
    }

    /**
     * 从线程池中移除任务
     *
     * @param runnable
     */
    public void remove(Runnable runnable) {
        if (runnable != null) {
            mExecutor.remove(runnable);
        }
    }

    private class DownloadThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNum = new AtomicInteger(0);

        @Override
        public Thread newThread(@NonNull final Runnable r) {
            return new Thread("DownloadThreadFactory" + threadNum.incrementAndGet()) {
                @Override
                public void run() {
                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    r.run();
                }
            };
        }
    }
}

package com.tdyh.android.okhttp.multidownload;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理
 * @author djcken
 * @date 2017/8/20
 */
public class ThreadPoolManager {

    private static ThreadPoolManager mInstance = new ThreadPoolManager();
    private final ExecutorService mExecutors;
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
        mCorePoolSize=128;
        mMaximumPoolSize = mCorePoolSize;
        //we custom the threadpool.
        mExecutor = new ThreadPoolExecutor(
                mCorePoolSize, //is 3 in avd.
                mMaximumPoolSize, //which is unuseless
                mKeepAliveTime,
                mUnit,
                mWorkQueue,
                Executors.defaultThreadFactory(),
                mHandler
        );
        mExecutors=  Executors.newCachedThreadPool();

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
//            mExecutor.execute(runnable);
            mExecutors.execute(runnable);
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

}

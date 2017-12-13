package com.tdyh.android.common.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * log
 */
public class LogUtil {

    public static final boolean DEBUG = BuildConfig.DEBUG;
    public static final String TAG = "TDMVP";
    public static final String LogFileName =  "log.txt";
    public static final String ErrorFileName = "error.txt";
    public static final String DIR=Environment.getExternalStorageDirectory()+File.pathSeparator+"TDLog";

    public static void i(String msg) {
        if (DEBUG)
            Log.i(TAG, msg);
    }

    public static void e(String msg) {
        if (DEBUG)
            Log.e(TAG, msg);
    }

    public static void e(String tag, String msg) {
        if (DEBUG)
            Log.e(tag, msg);
    }

    public static void d(String msg) {
        if (DEBUG)
            Log.d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        if (DEBUG)
            Log.d(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void log(String logText) {

        if (!DEBUG)
            return;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                File f = new File(Environment.getExternalStorageDirectory(), LogFileName);
                if (!f.exists()) {
                    f.createNewFile();
                }

                FileOutputStream fos = new FileOutputStream(f, true);
                StringBuilder sb = new StringBuilder();
                SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss");
                Date time = new Date();
                sb.append("***************").append("DATE：").append(ymd.format(time)).append(" TIME：").append(hms.format(time)).append("***************").append("\n").append(logText).append("\n\n");
                fos.write(sb.toString().getBytes());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeFile(String filePath, String content) {

        if (content == null)
            content = "";

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(content.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //记录异常
    public static void writeUncaughtException(Throwable e) {
        e.printStackTrace();
        LogUtil.e(e.toString());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("======出错 start======\n");
        stringBuilder.append(getException(e));
        stringBuilder.append("======出错 end======\n");

        SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
        String date= ymd.format(new Date());

        String filePath= DIR+File.pathSeparator+date+ErrorFileName;
        writeFile(filePath, stringBuilder.toString());
    }


    private static String getException(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        return sb.toString();
    }



}

package com.tdyh.android.common.utils;

import android.content.Context;
import android.hardware.Camera;
import android.support.v4.content.ContextCompat;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

/**
 * @author gzh
 * @date 2017/12/16 0016
 */

public class PermissionUtils {
    /**
     * 摄像头权限
     * @param context
     * @return
     */
    public static boolean isCameraGranted(Context context) {
        boolean permission= ContextCompat.checkSelfPermission(context, "android.permission.CAMERA") == PERMISSION_GRANTED;
        if (permission) {
            permission = isCameraCanUse();
        }
        return permission;
    }

    /**
     * 测试当前摄像头能否被使用
     * @return
     */
    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            e.printStackTrace();
            canUse = false;
        }
        if (canUse) {
            mCamera.release();
            mCamera = null;
        }
        return canUse;
    }
}

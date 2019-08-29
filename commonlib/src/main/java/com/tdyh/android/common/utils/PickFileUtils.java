package com.tdyh.android.common.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * 选择文件
 * Created by gaozh on 2019/8/29.<p>
 */
public class PickFileUtils {

    public interface OnPickFileCalllback {
        void onPickPickFile(Uri fileUri);
    }

    private SparseArray< OnPickFileCalllback> mOnPickCalllbackMap =new SparseArray<>();

    private int mChoosePicCode=-1;
    private int mChooseFileCode=-2;
    private Uri takeCameraImageUri;
    private WeakReference<Activity> mActivityWeakReference;

    public PickFileUtils(Activity context){
        mActivityWeakReference=new WeakReference<>(context);
    }

    public void pickPicture( int requestCode, OnPickFileCalllback calllback) {
        this.mChoosePicCode = requestCode;
        mOnPickCalllbackMap.put(requestCode,calllback);
        Activity activity= mActivityWeakReference.get();
        if (activity!=null) {
            File imageStorageDir = activity.getExternalFilesDir("images");//内部存储Android目录下data/包名/

//        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "images");//外部存储
            // Create the storage directory if it does not exist
            if (imageStorageDir!=null && !imageStorageDir.exists()) {
                imageStorageDir.mkdirs();
            }
            File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
            takeCameraImageUri = Uri.fromFile(file);

            Intent chooserIntent = IntentUtils.getChooseImageFile(activity, takeCameraImageUri);
            activity.startActivityForResult(chooserIntent, requestCode);
        }
    }

    public void pickFile(int requestCode, OnPickFileCalllback calllback){
        mChooseFileCode=requestCode;
        mOnPickCalllbackMap.put(requestCode,calllback);
        Activity activity= mActivityWeakReference.get();
        if (activity!=null){
            Intent chooserIntent = IntentUtils.getChooseFiles();
            activity.startActivityForResult(chooserIntent, requestCode);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.mChoosePicCode || requestCode ==this.mChooseFileCode) {
            Activity activity= mActivityWeakReference.get();
            if (activity!=null && !activity.isFinishing()) {
                Uri picUri = getUriFromData(activity, resultCode, data);
                OnPickFileCalllback mOnPickPicutrueCalllback = mOnPickCalllbackMap.get(requestCode);
                if (mOnPickPicutrueCalllback != null) {
                    mOnPickCalllbackMap.delete(requestCode);
                    mOnPickPicutrueCalllback.onPickPickFile(picUri);
                }
            }
        }/*else if (requestCode ==this.mChooseFileCode){//选择文件

        }*/
    }

    @Nullable
    private Uri getUriFromData(Context context, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri result = (data == null) ? null : data.getData();
            if (result != null) {
                String path = getFilePath(context, result);
                result =path!=null ? Uri.fromFile(new File(path)) : null;
            } else if (takeCameraImageUri != null) {
                //有可能拍照时取消了
                String path = takeCameraImageUri.getPath();
                if (!StringUtils.isBlank(path)) {
                    File file = new File(path);
                    if (file.exists()) {
                        result = Uri.fromFile(file);
                        takeCameraImageUri = null;
                    }
                }
            }
            return result;
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getFilePath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}

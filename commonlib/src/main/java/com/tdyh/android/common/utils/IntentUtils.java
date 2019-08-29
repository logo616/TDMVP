package com.tdyh.android.common.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaozh on 2019/8/29.<p>
 */
public class IntentUtils {

    /**
     * 选择文件
     */
   public static Intent getChooseFiles(){
       Intent i = new Intent(Intent.ACTION_GET_CONTENT);
       i.addCategory(Intent.CATEGORY_OPENABLE);
       i.setType("*/*");//选择的文件类型

//       Intent chooser= Intent.createChooser(i,"选择文件");//等同下面两行
       Intent chooser = new Intent(Intent.ACTION_CHOOSER);
       chooser.putExtra(Intent.EXTRA_TITLE, "选择文件");
       chooser.putExtra(Intent.EXTRA_INTENT, i);

        return  chooser;
    }

    /**
     * 选择图片（相机、相册）
     *
     */
    public static Intent  getChooseImageFile(Context context,Uri cameraOutputImageUri){

        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//相机
        final PackageManager packageManager = context.getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent i = new Intent(captureIntent);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            i.setPackage(packageName);
            /*不设置EXTRA_OUTPUT这样获取图片，Bitmap bitmap= intent.getParcelableExtra("data");
               设置EXTRA_OUTPUT，则直接得到拍照图片的imageUri*/
            i.putExtra(MediaStore.EXTRA_OUTPUT, cameraOutputImageUri);

            cameraIntents.add(i);
        }

        Intent picIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        picIntent.setType("image*/*");
        cameraIntents.add(picIntent);

        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        Intent chooserIntent = Intent.createChooser(i,"选择图片");
        //多个应用
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));

        return  chooserIntent;
    }


}

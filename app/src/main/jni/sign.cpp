//
// Created by Administrator on 2017/11/29 0029.
//
#include <jni.h>
#include <string.h>
#include <stdio.h>
#include "com_tdyh_android_tdmvp_utils_JNIUtils.h" //导入.h头文件

/**
 *这个key是和服务器之间通信的秘钥
 */
const char* AUTH_KEY = "td123456";

/**
 * 发布的app 签名,只有和本签名一致的app 才会返回 AUTH_KEY
 * 这个RELEASE_SIGN的值是上一步用java代码获取的值
 */
const char* RELEASE_SIGN = "308201dd30820146020101300d06092a864886f70d010105050030373116301406035504030c0d416e64726f69642044656275673110300e060355040a0c07416e64726f6964310b3009060355040613025553301e170d3137303832383032303735305a170d3437303832313032303735305a30373116301406035504030c0d416e64726f69642044656275673110300e060355040a0c07416e64726f6964310b300906035504061302555330819f300d06092a864886f70d010101050003818d0030818902818100d806e2f8e7b51a13466b43657f05eac90e166270c132724694593945a8299e9358da1fe137c0054912bc81a7c0ac638e44c059d78be8bfa31763f2c185dbf91cfe94259f3fda4811e74581628059ecbdf34611de3646c76c244fb64d0ce1d961ee81b33135e836c9a80cb3e6771549a3030a8eb77e650de47d28fb5e6789fea30203010001300d06092a864886f70d010105050003818100116488d08e8dd67b8d9f8b7f3d7fdaef27adb86c5ce7ffae269ce62f5852f749671c5552897b502b3b6fd2061fc837eb3744aaf2a2f00411bc37b199b3defed0a909c99f3605974773b6efd46dd8d905708f44435fe19ebcd24d66907b8672ac9d031de988ed306c3b5ee776ed03f00806b22459c6e1c1930964e12c1a4f2181";

/**
 * 发布的app 签名 的HashCode
 */
const int RELEASE_SIGN_HASHCODE = -332752192;


JNIEXPORT jstring JNICALL Java_com_tdyh_android_tdmvp_utils_JNIUtils_getPublicKey
  (JNIEnv *env, jclass jclazz, jobject contextObject){

    jclass native_class = env->GetObjectClass(contextObject);
    jmethodID pm_id = env->GetMethodID(native_class, "getPackageManager", "()Landroid/content/pm/PackageManager;");
    jobject pm_obj = env->CallObjectMethod(contextObject, pm_id);
    jclass pm_clazz = env->GetObjectClass(pm_obj);
    // 得到 getPackageInfo 方法的 ID
    jmethodID package_info_id = env->GetMethodID(pm_clazz, "getPackageInfo","(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    //jclass native_classs = env->GetObjectClass(contextObject);

    jmethodID mId = env->GetMethodID(native_class, "getPackageName", "()Ljava/lang/String;");
    jstring pkg_str = static_cast<jstring>(env->CallObjectMethod(contextObject, mId));
    // 获得应用包的信息
    jobject pi_obj = env->CallObjectMethod(pm_obj, package_info_id, pkg_str, 64);
    // 获得 PackageInfo 类
    jclass pi_clazz = env->GetObjectClass(pi_obj);
    // 获得签名数组属性的 ID
    jfieldID signatures_fieldId = env->GetFieldID(pi_clazz, "signatures", "[Landroid/content/pm/Signature;");
    jobject signatures_obj = env->GetObjectField(pi_obj, signatures_fieldId);
    jobjectArray signaturesArray = (jobjectArray)signatures_obj;
    jsize size = env->GetArrayLength(signaturesArray);
    jobject signature_obj = env->GetObjectArrayElement(signaturesArray, 0);
    jclass signature_clazz = env->GetObjectClass(signature_obj);

    //第一种方式--检查签名字符串的方式
    jmethodID string_id = env->GetMethodID(signature_clazz, "toCharsString", "()Ljava/lang/String;");
    jstring str = static_cast<jstring>(env->CallObjectMethod(signature_obj, string_id));
    char *c_msg = (char*)env->GetStringUTFChars(str,0);

    if(strcmp(c_msg,RELEASE_SIGN)==0)//签名一致  返回合法的 api key，否则返回错误
    {
        return (env)->NewStringUTF(AUTH_KEY);
    }else
    {
        return (env)->NewStringUTF("error");
    }

    //第二种方式--检查签名的hashCode的方式
    /*
    jmethodID int_hashcode = env->GetMethodID(signature_clazz, "hashCode", "()I");
    jint hashCode = env->CallIntMethod(signature_obj, int_hashcode);
    if(hashCode == RELEASE_SIGN_HASHCODE)
    {
        return (env)->NewStringUTF(AUTH_KEY);
    }else{
        return (env)->NewStringUTF("错误");
    }
     */

  }

/*
 public static String getSignature(Context context)
    {
        try {
            // 通过包管理器获得指定包名包含签名的包信息
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            // 通过返回的包信息获得签名数组
            Signature[] signatures = packageInfo.signatures;
            // 循环遍历签名数组拼接应用签名
            return signatures[0].toCharsString();
            //得到应用签名
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    */

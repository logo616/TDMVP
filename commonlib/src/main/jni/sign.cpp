//
// Created by Administrator on 2017/11/29 0029.
//
#include <jni.h>
#include <string.h>
#include <stdio.h>
#include "com_tdyh_android_common_utils_SignUtils.h" //导入.h头文件

/**
 *这个key是和服务器之间通信的秘钥
 */
const char* AUTH_KEY = "td123456";

/**
 * 发布的app 签名,只有和本签名一致的app 才会返回 AUTH_KEY
 * 这个RELEASE_SIGN的值是上一步用java代码获取的值
 */
const char* RELEASE_SIGN = "308201e53082014ea003020102020456c41e62300d06092a864886f70d01010505003037310b30090603550406130255533110300e060355040a1307416e64726f6964311630140603550403130d416e64726f6964204465627567301e170d3136303231373037313635305a170d3436303230393037313635305a3037310b30090603550406130255533110300e060355040a1307416e64726f6964311630140603550403130d416e64726f696420446562756730819f300d06092a864886f70d010101050003818d003081890281810081a71c824e409953ec64772cd6ba3d9075d96186f9fdc641f7d41f0491362120218d34b9771015df93acdfc71afeb0ac048e0cd80a07b6ec4a98199b982520a80797e7cbabcabc0d41d7bd2fb3b2b74bb4786440ea28bab0629dc31f79458bbbf06c22455bf81e2d86e23135301f73fe18531b16b97559fbf8bfbe75d508fcdf0203010001300d06092a864886f70d0101050500038181000a0fcbc9b07f46edec72db7e1418feffd1f457c4038b1e83522c3487812a276a67cee0de44a343748546fc398d184afc31fe6c82c69dd77c029fe45c3662efa80275d660b26481cac7a45487af5265288d7253db401b8a85de96c7db63f2139b1a27799f37ec8640bc0b32be4678eec60acf84c2afb4d77891c9687e13a25d65";

/**
 * 发布的app 签名 的HashCode
 */
const int RELEASE_SIGN_HASHCODE = -332752192;


JNIEXPORT jstring JNICALL Java_com_tdyh_android_common_utils_SignUtils_getPublicKey
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

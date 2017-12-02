package com.tdyh.android.common.utils.security;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;


/**
 * @author gzh
 * @date 2017/11/24 0024
 * 对称加密  AES_GCM android 实现
 */

public class AesGcmAlg {
    private static final String AndroidKeyStore = "AndroidKeyStore";
    private static final String AES_MODE_GCM = KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_GCM + "/"
            + KeyProperties.ENCRYPTION_PADDING_NONE;
    private static final String KEY_ALIAS = "AESGCMKEY";
    private static final String LOG_GCM_TAG = "AesGcmAlg";

    public static byte[] gcm_iv = null;

    public static byte[] Encyption(String plaintext,SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_MODE_GCM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encodedBytes = cipher.doFinal(plaintext.getBytes());
        //保存解密需要的IV变量
        gcm_iv = cipher.getIV();
        return encodedBytes;
    }

    public static byte[] Decyption(String encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_MODE_GCM);
        KeyStore keyStore = KeyStore.getInstance(AndroidKeyStore);
        keyStore.load(null);
        SecretKey secretKey = (SecretKey) keyStore.getKey(KEY_ALIAS, null);
        cipher.init(Cipher.DECRYPT_MODE, secretKey,new GCMParameterSpec(128, gcm_iv));

        byte[] decodedBytes = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT));
        return decodedBytes;
    }
    public static SecretKey createKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, AndroidKeyStore);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyGenerator.init(
                    new KeyGenParameterSpec.Builder(KEY_ALIAS,
                            KeyProperties.PURPOSE_ENCRYPT |

                                    KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .build());
        }
        return keyGenerator.generateKey();
    }


    public static void testCiperAesGcm() throws Exception {
        String plaintext = "123456789012345" +
                "123456789012345" +
                "123456789012345" +
                "123456789012345";
        System.out.println(LOG_GCM_TAG + "加密明文为：" + plaintext);
        System.out.println(LOG_GCM_TAG + "加密模式为：GCM");
        SecretKey secretKey = AesGcmAlg.createKey();

        System.out.println(LOG_GCM_TAG + "加密密钥为：" + secretKey);
        byte[] encodedBytes = AesGcmAlg.Encyption(plaintext, secretKey);
        String encryptedBase64Encoded = Base64.encodeToString(encodedBytes, Base64.DEFAULT);
        System.out.println(LOG_GCM_TAG + "加密结果为：" + encryptedBase64Encoded);

        byte[] decodedBytes = AesGcmAlg.Decyption(encryptedBase64Encoded);
        System.out.println(LOG_GCM_TAG + "初始向量为：" + AesGcmAlg.gcm_iv);
        System.out.println(LOG_GCM_TAG + "解密结果为：" + new String(decodedBytes, "UTF-8"));
    }
}

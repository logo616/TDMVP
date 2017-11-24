package com.tdyh.android.common.uitls.security;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * @author gzh
 * @date 2017/11/24 0024
 * 对称加密  AES_CBC android 实现
 */

public class AesCbcAlg {
    private static final String AndroidKeyStore = "AndroidKeyStore";
    private static final String AES_MODE_CBC = KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7;
    private static final String KEY_ALIAS = "AESCBCKEY";//这个别名
    public static byte[] cbc_iv = null;
    private static String LOG_CBC_TAG="AesCbcAlg";

    public static byte[] Encyption(String plaintext,SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_MODE_CBC);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encodedBytes = cipher.doFinal(plaintext.getBytes());
        //保存解密需要的IV变量
        cbc_iv = cipher.getIV();
        return encodedBytes;
    }

    public static byte[] Decyption(String encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_MODE_CBC);
        KeyStore keyStore = KeyStore.getInstance(AndroidKeyStore);
        keyStore.load(null);
        SecretKey secretKey = (SecretKey) keyStore.getKey(KEY_ALIAS, null);
        cipher.init(Cipher.DECRYPT_MODE, secretKey,new IvParameterSpec(cbc_iv));
        byte[] decodedBytes = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT));
        return decodedBytes;
    }

    //上面这案例中是将IV保存到文件中，实际使用时，建议将IV保存到Data中，用于解密的时候使用，
    public static SecretKey createKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, AndroidKeyStore);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyGenerator.init(
                    new KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                            .build());
        }
        //这里还有几个用户认证的程序需要搞定setUserAuthenticationRequired，setUserAuthenticationValidityDurationSeconds(int seconds)
        return keyGenerator.generateKey();
    }


    public static void testCiperAesCbc() throws Exception {
        String plaintext = "123456789012345" +
                "123456789012345" +
                "123456789012345" +
                "123456789012345";
        System.out.println(LOG_CBC_TAG + "加密明文为：" + plaintext);
        System.out.println(LOG_CBC_TAG + "加密模式为：CBC" );
        SecretKey secretKey = AesCbcAlg.createKey();

        System.out.println(LOG_CBC_TAG + "加密密钥为：" + secretKey);
        byte[] encodedBytes = AesCbcAlg.Encyption(plaintext, secretKey);
        String encryptedBase64Encoded = Base64.encodeToString(encodedBytes, Base64.DEFAULT);
        System.out.println(LOG_CBC_TAG + "加密结果为：" + encryptedBase64Encoded);

        byte[] decodedBytes = AesCbcAlg.Decyption(encryptedBase64Encoded);
        System.out.println(LOG_CBC_TAG + "初始向量为：" + AesCbcAlg.cbc_iv);
        System.out.println(LOG_CBC_TAG + "解密结果为：" + new String(decodedBytes, "UTF-8"));
    }


}

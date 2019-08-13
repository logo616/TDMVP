package com.tdyh.android.common.utils.security;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.Signature;

/**
 * @author gzh
 * @date 2017/11/24 0024
 *  数字签名
 */

public class RsaSignCiper {
    private static final String AndroidKeyStore = "AndroidKeyStore";
    private static final String RSA_MODE_SIGN = "SHA256withRSA/PSS";
    private static final String KEY_ALIAS = "RSASIGNCIPERKEY";//别名

    private static final String LOG_RSA_OAEP_LOG = "RsaSignCiper";

    public static byte[] SignRsa(String plaintext) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(AndroidKeyStore);
        keyStore.load(null);
        KeyStore.Entry entry = keyStore.getEntry(KEY_ALIAS, null);
        if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
            Log.w("RSASIGN", "Not an instance of a PrivateKeyEntry");
            return null;
        }
        Signature signature = Signature.getInstance(RSA_MODE_SIGN);
        signature.initSign(((KeyStore.PrivateKeyEntry) entry).getPrivateKey());
        signature.update(plaintext.getBytes());
        byte[] signatureBytes = signature.sign();
        return signatureBytes;
    }

    public static Boolean VerifyRsa(String plaintext,byte[] signatureBytes) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        KeyStore.Entry entry = keyStore.getEntry(KEY_ALIAS, null);
        if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
            Log.w("RSASIGN", "Not an instance of a PrivateKeyEntry");
            return null;
        }
        Signature signature = Signature.getInstance(RSA_MODE_SIGN);
        signature.initVerify(((KeyStore.PrivateKeyEntry) entry).getCertificate());
        signature.update(plaintext.getBytes());
        boolean valid = signature.verify(signatureBytes);
        return valid;
    }
    public static KeyPair createKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_RSA, AndroidKeyStore);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyPairGenerator.initialize(
                    new KeyGenParameterSpec.Builder(
                            KEY_ALIAS,
                            KeyProperties.PURPOSE_SIGN | KeyProperties.PURPOSE_VERIFY)
                            .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                            .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PSS)

                            .setKeySize(2048)
                            .build());
        }
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }
    public static  void testCiperRsaOaep() throws Exception {
        String plaintext = "123456789012345" +
                "123456789012345" +
                "123456789012345" +
                "123456789012345";
        System.out.println(LOG_RSA_OAEP_LOG + "加密明文为：" + plaintext);
        System.out.println(LOG_RSA_OAEP_LOG + "加密模式为：RSAOAEP" );
        KeyPair secretKey = RsaOaepCiper.createKey();

        System.out.println(LOG_RSA_OAEP_LOG + "RSA公钥为：" + secretKey.getPublic());
        System.out.println(LOG_RSA_OAEP_LOG + "RSA私钥为：" + secretKey.getPrivate());
        byte[] encodedBytes = RsaOaepCiper.Encyption(plaintext);
        String encryptedBase64Encoded = Base64.encodeToString(encodedBytes, Base64.DEFAULT);
        System.out.println(LOG_RSA_OAEP_LOG + "加密结果为：" + encryptedBase64Encoded);

        byte[] decodedBytes = RsaOaepCiper.Decyption(encryptedBase64Encoded);
        System.out.println(LOG_RSA_OAEP_LOG + "解密结果为：" + new String(decodedBytes, "UTF-8"));
    }

}

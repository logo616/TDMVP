package com.tdyh.android.common.uitls.security;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

/**
 * @author gzh
 * @date 2017/11/24 0024
 * 2 非对称加密：本地随机的密钥
方案：直接使用androidKeyStore生成，密钥对，不需要考虑密钥保存方案。
 */

public class RsaOaepCiper {
    private static final String AndroidKeyStore = "AndroidKeyStore";
    private static final String RSA_MODE_OAEP = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    private static final String KEY_ALIAS = "RSACIPERKEY";

    private static final String LOG_RSA_OAEP_LOG ="RsaOaepCiper" ;

    public static byte[] Encyption(String plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_MODE_OAEP);
        KeyStore keyStore = KeyStore.getInstance(AndroidKeyStore);
        keyStore.load(null);
        PublicKey publicKey = keyStore.getCertificate(KEY_ALIAS).getPublicKey();
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encodedBytes = cipher.doFinal(plaintext.getBytes());
        return encodedBytes;
    }

    public static byte[] Decyption(String encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_MODE_OAEP);
        KeyStore keyStore = KeyStore.getInstance(AndroidKeyStore);
        keyStore.load(null);
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(KEY_ALIAS, null);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decodedBytes = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT));
        return decodedBytes;
    }
    public static KeyPair createKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, AndroidKeyStore);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyPairGenerator.initialize(
                    new KeyGenParameterSpec.Builder(
                            KEY_ALIAS,
                            KeyProperties.PURPOSE_DECRYPT)
                            .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)

                            .setKeySize(2048)

                         //默认为2048，保险期间最好加上，否则容易扯皮
                            .build());
        }
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    public static void testCiperRsaOaep() throws Exception {
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

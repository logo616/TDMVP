package com.tdyh.android.common.utils;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;

/**
 * /**
 *
 * @author gzh
 * @date 2017/11/23 0023
 * 使用ksyStore加密工具类,不支持大数据
 *
 */

public class KeystoreEncryUtils {

    private static KeystoreEncryUtils encryUtilsInstance;
    private KeyStore keyStore;
    private static Context mContext;

    public static KeystoreEncryUtils getInstance(Context context) {
        synchronized (KeystoreEncryUtils.class) {
            if (null == encryUtilsInstance) {
                encryUtilsInstance = new KeystoreEncryUtils();
                mContext = context.getApplicationContext();
            }
        }
        return encryUtilsInstance;
    }

    private KeystoreEncryUtils() {
//        initKeyStore();
    }

    private void initKeyStore(String alias) {
        try {
            if (keyStore==null) {
                keyStore = KeyStore.getInstance("AndroidKeyStore");
                keyStore.load(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        createNewKeys(alias);
    }

    private void createNewKeys(String alias) {
        if (!TextUtils.isEmpty((alias))) {
            try {
                // Create new key if needed
                if (!keyStore.containsAlias(alias)) {
                    Calendar start = Calendar.getInstance();
                    Calendar end = Calendar.getInstance();
                    end.add(Calendar.YEAR, 1);

                    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

                        KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(mContext)
                                    .setAlias(alias)
                                    .setSubject(new X500Principal("CN=Sample Name, O=Android Authority"))
                                    .setSerialNumber(BigInteger.ONE)
                                    .setStartDate(start.getTime())
                                    .setEndDate(end.getTime())
                                    .build();

                        generator.initialize(spec);
                    }

                    KeyPair keyPair = generator.generateKeyPair();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    /**
     * 加密方法
     *
     * @param needEncryptWord 　需要加密的字符串,长度在256以下
     * @param alias           　加密秘钥
     * @return
     */
    public String encryptString(String needEncryptWord, String alias) {
        if (!TextUtils.isEmpty(alias) && !TextUtils.isEmpty(needEncryptWord)) {

            initKeyStore(alias);

            String encryptStr = "";
            byte[] vals = null;
            try {
                KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
//            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();
                if (needEncryptWord.isEmpty()) {
//                Toast.makeText(this, "Enter text in the 'Initial Text' widget", Toast.LENGTH_LONG).show();
                    return encryptStr;
                }

//            Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
                Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//            inCipher.init(Cipher.ENCRYPT_MODE, publicKey);
                inCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                CipherOutputStream cipherOutputStream = new CipherOutputStream(
                        outputStream, inCipher);
                cipherOutputStream.write(needEncryptWord.getBytes("UTF-8"));
                cipherOutputStream.close();

                vals = outputStream.toByteArray();

                return  Base64.encodeToString(vals, Base64.DEFAULT);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    public String decryptString(String needDecryptWord, String alias) {
        if (!TextUtils.isEmpty(alias) && !TextUtils.isEmpty(needDecryptWord)) {
            initKeyStore(alias);

            String decryptStr = "";
            try {
                KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
//            RSAPrivateKey privateKey = (RSAPrivateKey) privateKeyEntry.getPrivateKey();

//            Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
                Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//            output.init(Cipher.DECRYPT_MODE, privateKey);
                output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
                CipherInputStream cipherInputStream = new CipherInputStream(
                        new ByteArrayInputStream(Base64.decode(needDecryptWord, Base64.DEFAULT)), output);
                ArrayList<Byte> values = new ArrayList<>();
                int nextByte;
                while ((nextByte = cipherInputStream.read()) != -1) {
                    values.add((byte) nextByte);
                }

                byte[] bytes = new byte[values.size()];
                for (int i = 0; i < bytes.length; i++) {
                    bytes[i] = values.get(i).byteValue();
                }

                decryptStr = new String(bytes, 0, bytes.length, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return decryptStr;
        }
        return "";
    }
}
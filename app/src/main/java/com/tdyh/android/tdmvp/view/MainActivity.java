package com.tdyh.android.tdmvp.view;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tdyh.android.base.BaseActivity;
import com.tdyh.android.common.utils.AndroidKeyStoreRSAUtils;
import com.tdyh.android.common.utils.KeystoreEncryUtils;
import com.tdyh.android.tdmvp.R;
import com.tdyh.android.tdmvp.contract.MainContract;
import com.tdyh.android.tdmvp.presenter.MainPresenter;
import com.tdyh.android.common.utils.SignUtils;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.RSAPublicKey;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainPresenter, MainContract.View> implements MainContract.View {

    @BindView(R.id.button)
    Button button;
    @BindView(R.id.btn_keystore_encry)
    Button btnKeystoreEncry;
    @BindView(R.id.btn_keystore_public_encry)
    Button btnKeystorePublicEncry;
    @BindView(R.id.btn_keystore_privatekey_decry)
    Button btnKeystorePrivatekeyDecry;



    @Override
    public void initialize() {

        Log.d("TDMVP","sign:"+ SignUtils.getSignature(this));

        String publicKey= SignUtils.getPublicKey(this);
        Toast.makeText(this,publicKey,Toast.LENGTH_SHORT).show();
    }



    private void buttonClick() {

        mPresenter.login("gao", "1234556");
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainPresenter onCreatePresenter() {
        return new MainPresenter();
    }

    @Override
    public void loginCallback(boolean isSuccess) {
        Log.d("", "loginCallback:  " + isSuccess);
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getViewContext() {
        return this;
    }


    @OnClick({R.id.button, R.id.btn_keystore_encry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button:
                buttonClick();
                break;
            case R.id.btn_keystore_encry:
                keystoreEncry();
                break;
        }
    }

    private void keystoreEncry() {
        String text = "在有些技术文献和资料里常用Rijndael代之AES算法。AES是一个对称的、块加密算法，什么意思？“对称”的意思是：（1）甲方选择某一种加密规则，对信息进行加密；（2）乙方使用同一种规则，对信息进行解密。这种加密模式有一个最大弱点：甲方必须把加密规则告诉乙方，否则无法解密。“块”的意思是：如果待加密数据太长，则需要按固定长度分割后，对每段明文数据依次单独加密。AES算法数据分组的长度和秘钥长度相互...1、FACTORY—追MM少不了请吃饭了，麦当劳的鸡翅和肯德基的鸡翅都是MM爱吃的东西，虽然口味有所不同，但不管你带MM去麦当劳或肯德基，只管向服务员说“来四个鸡翅”就行了。麦当劳和肯德基就是生产鸡翅的Factory工厂模式：客户类和工厂类分开。消费者任何时候需要某种产品，只需向工厂请求即可。消费者无须修改就可以接纳新产品。缺点是当产品修改时，工厂类也要做相应的修改。如：如何创建及如何向客户端提供。";
        String alias = "123";
        KeystoreEncryUtils utils = KeystoreEncryUtils.getInstance(this);
        String encryText = utils.encryptString(text, alias);

        System.out.println(" encry:" + encryText);

        String decodeText = utils.decryptString(encryText, alias);
        System.out.println("decodetext:" + decodeText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_keystore_public_encry)
    public void onBtnKeystorePublicEncryClicked() {

        String text = "在有些技术文献和资料里常用Rijndael代之AES算法。AES是一个对称的、块加密算法，什么意思？“对称”的意思是：（1）甲方选择某一种加密规则，对信息进行加密；（2）乙方使用同一种规则，对信息进行解密。这种加密模式有一个最大弱点：甲方必须把加密规则告诉乙方，否则无法解密。“块”的意思是：如果待加密数据太长，则需要按固定长度分割后，对每段明文数据依次单独加密。AES算法数据分组的长度和秘钥长度相互...1、FACTORY—追MM少不了请吃饭了，麦当劳的鸡翅和肯德基的鸡翅都是MM爱吃的东西，虽然口味有所不同，但不管你带MM去麦当劳或肯德基，只管向服务员说“来四个鸡翅”就行了。麦当劳和肯德基就是生产鸡翅的Factory工厂模式：客户类和工厂类分开。消费者任何时候需要某种产品，只需向工厂请求即可。消费者无须修改就可以接纳新产品。缺点是当产品修改时，工厂类也要做相应的修改。如：如何创建及如何向客户端提供。";
        try {
            createKey();
            System.out.println("原文："+text);
            byte[] encryptBytes = AndroidKeyStoreRSAUtils.encryptByPublicKeyForSpilt(text.getBytes(),
                    publicKey.getEncoded());
//                    byte[] encryptBytes = AndroidKeyStoreRSAUtils.encryptByPublicKey(encryptionString.getBytes(),
//                            publicKey.getEncoded());
            String encryStr = Base64.encodeToString(encryptBytes,Base64.DEFAULT);
            System.out.println(encryStr);
            decodeString=encryStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_keystore_privatekey_decry)
    public void onBtnKeystorePrivatekeyDecryClicked() {

        String decodeString = this.decodeString;
        if (TextUtils.isEmpty(decodeString)) {
            Toast.makeText(this, "请先加密", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            byte[] decryptBytes = AndroidKeyStoreRSAUtils.decryptByPrivateKeyForSpilt(
                    Base64.decode(decodeString,Base64.DEFAULT));
//                    byte[] decryptBytes = AndroidKeyStoreRSAUtils.decryptByPrivateKey(
//                            Base64Decoder.decodeToBytes(decodeString));

            System.out.println(new String(decryptBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    RSAPublicKey publicKey;
    String decodeString;

    private void createKey(){
        if (AndroidKeyStoreRSAUtils.isHaveKeyStore()) {//是否有秘钥
            publicKey = (RSAPublicKey) AndroidKeyStoreRSAUtils.getLocalPublicKey();
            if (publicKey != null) {
                Toast.makeText(this, "已经生成过密钥对", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        try {//在项目中放在application或启动页中
            KeyPair keyPair = AndroidKeyStoreRSAUtils.generateRSAKeyPair(this);
            // 公钥
            publicKey = (RSAPublicKey) keyPair.getPublic();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}

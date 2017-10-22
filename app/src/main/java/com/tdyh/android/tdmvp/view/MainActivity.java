package com.tdyh.android.tdmvp.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tdyh.android.base.BaseActivity;

import com.tdyh.android.tdmvp.R;
import com.tdyh.android.tdmvp.contract.MainContract;
import com.tdyh.android.tdmvp.presenter.MainPresenter;

public class MainActivity extends BaseActivity<MainPresenter,MainContract.View> implements MainContract.View{

    private Button btn;

    @Override
    public void initialize() {
        btn= (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonClick();

            }
        });
    }

    private void buttonClick(){

        mPresenter.login("gao","1234556");
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
        Log.d("","loginCallback:  "+isSuccess);
        Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getViewContext() {
        return this;
    }
}

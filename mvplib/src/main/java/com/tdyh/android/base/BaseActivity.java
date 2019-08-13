package com.tdyh.android.base;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2017/10/17 0017.
 */

public abstract class BaseActivity<P extends BasePresenter<V>, V extends BaseView> extends AppCompatActivity implements BaseView ,LoaderManager.LoaderCallbacks<P> {

    public final static int BASE_ACTIVITY_LOADER_ID = 100;

    protected ProgressDialog progressDialog;

    protected P mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //防止fragment.getActivity()为空
        removeFragmentsState(savedInstanceState);

        super.onCreate(savedInstanceState);
        setContentView(getContentLayoutId());
        progressDialog = new ProgressDialog(this);
        getLoaderManager().initLoader(BASE_ACTIVITY_LOADER_ID, null, this);

//        mPresenter = onCreatePresenter();
        initialize();
    }

    public abstract void initialize();
    public abstract int getContentLayoutId();

    /**
     * 创建prensenter
     * @return <T extends BasePresenter> 必须是BasePresenter的子类
     */
    protected abstract P onCreatePresenter();

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        removeFragmentsState(outState);
    }

    private void removeFragmentsState(Bundle outState){
        //不保存fragment状态，防止fragment.getActivity()为null
        if(outState!=null){
            String FRAGMENTS_TAG =  "android:support:fragments";
            outState.remove(FRAGMENTS_TAG);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        progressDialog=null;
        super.onDestroy();
    }


    @Override
    public void showLoading(String msg) {
        if (progressDialog!=null) {
            progressDialog.setMessage(msg);
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }

    @Override
    public void hideLoading() {

        if (progressDialog!=null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public Loader<P> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<P>(this, new PresenterFactory() {
            @Override
            public Presenter create() {
                return onCreatePresenter();
            }
        });
    }

    @Override
    public void onLoadFinished(Loader<P> loader, P data) {
        mPresenter = data;
    }

    @Override
    public void onLoaderReset(Loader<P> loader) {
        mPresenter = null;
    }
}


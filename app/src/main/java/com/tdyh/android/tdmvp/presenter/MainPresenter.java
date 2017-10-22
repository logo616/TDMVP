package com.tdyh.android.tdmvp.presenter;

import android.util.Log;
import android.widget.Toast;

import com.tdyh.android.okhttp.multidownload.DownloadListner;
import com.tdyh.android.okhttp.multidownload.DownloadManager;
import com.tdyh.android.rx.base.BaseRxPresenter;
import com.tdyh.android.rx.base.ObserverOnNextListener;
import com.tdyh.android.rx.base.ProgressObserver;
import com.tdyh.android.tdmvp.bean.MovieEntity;
import com.tdyh.android.tdmvp.contract.MainContract;
import com.tdyh.android.tdmvp.model.LogModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Administrator on 2017/10/18 0018.
 */

public class MainPresenter extends BaseRxPresenter<MainContract.View> implements MainContract.Presenter {

    LogModel logModel;
    public MainPresenter(){
        logModel=new LogModel();
    }

    @Override
    public void login(String userName, String pwd) {

    /*    logModel.getMovie().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressObserver<MovieEntity>(getView().getViewContext(),"登录中...", new ObserverOnNextListener<MovieEntity>() {
                    @Override
                    public void onNext(MovieEntity o) {
                        Log.d("","成功");
                    }
                }));*/

             String filePath=   getView().getViewContext().getExternalFilesDir("images").getPath();


            getView().showLoading("下载中");
//            String url="http://n9.cmsfile.pg0.cn/group4/M00/24/A2/CgpBUlkc-kyAVhnYAABzBwMSXXU758.jpg";
            String url="https://s3.amazonaws.com/psiphon/web/60l3-nnss-6gsn/psiphon3.exe";
                DownloadManager.getInstance().add(url, filePath, new DownloadListner() {

                    @Override
                    public void onFinished() {
                        Log.e("","下载完成onFinished");
                        Toast.makeText(getView().getViewContext(),"下载成功",Toast.LENGTH_SHORT).show();
                        getView().hideLoading();
                    }

                    @Override
                    public void onProgress(float progress) {
                        Log.d("下载中",progress+"");
                    }

                    @Override
                    public void onPause() {

                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onFail(String msg) {
                        getView().hideLoading();
                        Log.e("","下载完成onFail  ："+msg);
                        Toast.makeText(getView().getViewContext(),"下载失败："+msg,Toast.LENGTH_SHORT).show();
                    }
                });
              DownloadManager.getInstance().download(url);


   /*  if (isViewAttached()) {
            getView().showLoading("登录中");
        }*/
       /* UseCaseHandler handler=UseCaseHandler.getInstance();
        LoginCase.RequestValues requestValues=new LoginCase.RequestValues(userName,pwd);
        LoginCase loginCase=new LoginCase(logModel);
        handler.execute(loginCase ,requestValues,new  UseCase.UseCaseCallback<LoginCase.ResponseValue>() {

            @Override
            public void onSuccess(LoginCase.ResponseValue response) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().loginCallback(true);
                }
            }

            @Override
            public void onError(String errorCode, Object errorMsg) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().loginCallback(false);
                }
            }
        });*/
    }



}

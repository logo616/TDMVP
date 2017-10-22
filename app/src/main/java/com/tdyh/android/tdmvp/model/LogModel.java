package com.tdyh.android.tdmvp.model;


import android.util.Log;

import com.tdyh.android.tdmvp.apiservice.Api;
import com.tdyh.android.tdmvp.apiservice.MovieService;
import com.tdyh.android.tdmvp.bean.MovieEntity;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/10/18 0018.
 */

public class LogModel implements IUserModel {


    private static final String TAG = "LogModel";

    @Override
    public void login(String userName, String pwd, final DataCallback callback) {

        String baseUrl = "https://api.douban.com/v2/movie/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        MovieService movieService = retrofit.create(MovieService.class);

       movieService.getTopMovie(0, 10)
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Observer<MovieEntity>() {
                   @Override
                   public void onSubscribe(@NonNull Disposable d) {
                       Log.d(TAG,"onSubscribe");
                   }

                   @Override
                   public void onNext(@NonNull MovieEntity movieEntity) {
                       Log.d(TAG,"onNext");
                        callback.onSuccess();

                   }

                   @Override
                   public void onError(@NonNull Throwable e) {
                       Log.d(TAG,"失败"+e.toString());
                        callback.onFail("",e.getMessage());
                   }

                   @Override
                   public void onComplete() {
                       Log.d(TAG,"onComplete");
                   }
               });
//        callback.onSuccess();

    }

    //进行网络请求
   /* private void getMovie(){
        String baseUrl = "https://api.douban.com/v2/movie/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        MovieService movieService = retrofit.create(MovieService.class);
        Call<MovieEntity> call = movieService.getTopMovie(0, 10);
        call.enqueue(new Callback<MovieEntity>() {
            @Override
            public void onResponse(Call<MovieEntity> call, Response<MovieEntity> response) {

            }

            @Override
            public void onFailure(Call<MovieEntity> call, Throwable t) {

            }
        });
    }*/

    public Observable<MovieEntity> getMovie(){
        return   Api.getApiService().getTopMovie(0,10);
    }
}

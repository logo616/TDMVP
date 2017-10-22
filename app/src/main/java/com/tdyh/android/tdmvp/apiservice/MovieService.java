package com.tdyh.android.tdmvp.apiservice;

import com.tdyh.android.tdmvp.bean.MovieEntity;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public interface MovieService {

    @GET("top250")
//    Call<MovieEntity> getTopMovie(@Query("start") int start, @Query("count") int count);
    Observable<MovieEntity> getTopMovie(@Query("start") int start, @Query("count") int count);
}

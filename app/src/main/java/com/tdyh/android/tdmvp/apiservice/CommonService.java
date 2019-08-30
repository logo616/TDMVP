package com.tdyh.android.tdmvp.apiservice;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by gaozh on 2019/8/29.<p>
 */
public interface CommonService {

   @POST
   Observable<String> sendPost( @Url String url, @Body RequestBody body);

}

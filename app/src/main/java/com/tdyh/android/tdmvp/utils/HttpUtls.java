package com.tdyh.android.tdmvp.utils;

import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.tdyh.android.common.utils.GsonUtils;
import com.tdyh.android.rx.RetrofitManager;
import com.tdyh.android.rx.retrofit.function.HttpResponseErrorFunc;
import com.tdyh.android.rx.retrofit.result.Result;
import com.tdyh.android.tdmvp.apiservice.CommonService;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;

/**
 * Created by gaozh on 2019/8/29.<p>
 */
public class HttpUtls {

    public static  <T,R> Observable<R> sendPost(T params, final Class responseClass){

        CommonService movieService=   RetrofitManager.getInstance().create(CommonService.class);

        String json= GsonUtils.toJson(params);
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("jsonData",json);
        FormBody formBody = builder.build();
        String url="http://api.map.baidu.com/telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ";

     return    movieService.sendPost(url,formBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<String, Result<R>>() {
                    @Override
                    public Result<R> apply(String s) throws Exception {
                        Log.e("Gao","==========="+s);
                        s="{code:1,msg:'sussess',result:{userName:'gao',sex:'2'}}";
//                        Type type = new TypeToken<Result<R>>() {}.getType();
//                        如何判断R是list还是bean
                        Result<R> result;
                        try {
                            result = (Result<R>) fromJsonObject(s, responseClass);
                        } catch (JsonSyntaxException e) {//解析异常，说明是array数组
                            result = (Result<R>) fromJsonArray(s, responseClass);
                        }
                        return result;
                    }
                }).map(new Function<Result<R>, R>() {
                 @Override
                 public R apply(Result<R> rResult) throws Exception {
                     R result=rResult.getResult();
                     Log.e("Gao","result==========="+result);

                     return result;
                 }
             }).onErrorResumeNext(new HttpResponseErrorFunc<R>());
    }

    public static <T> Result<T> fromJsonObject(String reader, Class<T> clazz) {
        Type type = new ParameterizedTypeImpl(Result.class, new Class[]{clazz});
        return GsonUtils.getGson().fromJson(reader, type);
    }

    public static <T> Result<List<T>> fromJsonArray(String reader, Class<T> clazz) {
        // 生成List<T> 中的 List<T>
        Type listType = new ParameterizedTypeImpl(List.class, new Class[]{clazz});
        // 根据List<T>生成完整的Result<List<T>>
        Type type = new ParameterizedTypeImpl(Result.class, new Type[]{listType});
        return GsonUtils.getGson().fromJson(reader, type);
    }

   /* fun <T> commonPost(params: CommonRequestParams, clazz: ):  {
        val url="http://baike.baidu.com/api/openapi/BaikeLemmaCardApi?scope=103&format=json&appid=379020&bk_key=1&bk_length=600"
        return RxRestCreator.getRxService().commonPost(url,params.toRequestBody())
                .map(object : Function<ResponseResult<String>, String> {
            override fun apply(t: ResponseResult<String>): String {
                return t.data
            }
        }).map(object : Function<String, T> {
            override fun apply(t: String): T? {
            return GsonUtils.fromJson(t, clazz)
                }
        }).onErrorResumeNext(HttpResponseErrorFunc<T>())
    }*/

}

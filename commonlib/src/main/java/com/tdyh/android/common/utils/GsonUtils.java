package com.tdyh.android.common.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by gaozh on 2019/8/29.<p>
 */
public class GsonUtils {

    private static Gson mGson=new Gson();

    public static Gson getGson(){
        return mGson;
    }

    public static <T> T fromJson(String json, Class<T> clz){
        try {
            T result=  mGson.fromJson(json, clz);
            return result;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    public static <T> T fromJson(String json, Type type){
        try {
            T result=  mGson.fromJson(json, type);
            return result;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }



    public static  String toJson( Object clz){
        try {
            return  mGson.toJson(clz);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}

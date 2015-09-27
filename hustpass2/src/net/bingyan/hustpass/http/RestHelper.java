package net.bingyan.hustpass.http;

import net.bingyan.hustpass.API;

import retrofit.RestAdapter;

/**
 * Created by ant on 14-8-5.
 */
public class RestHelper {

    public static <T> T getService(Class<T> t){
        return  getService(API.APIHOST,t);
    }
    public static <T> T getService(String HOST,Class<T> t){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(HOST)
                .build();
        return restAdapter.create(t);
    }
}

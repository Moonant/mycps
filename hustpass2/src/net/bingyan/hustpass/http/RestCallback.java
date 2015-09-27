package net.bingyan.hustpass.http;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ant on 14-8-26.
 */
public abstract class RestCallback<T> implements Callback<T> {

    public abstract void finalexe();

    @Override
    public void success(T t, Response response) {
        finalexe();
    }

    @Override
    public void failure(RetrofitError error) {
        finalexe();
    }
}

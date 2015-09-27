package net.bingyan.hustpass.db;

import android.app.Application;

import com.google.gson.Gson;

import net.bingyan.hustpass.Cache;
import net.bingyan.hustpass.CacheDao;
import net.bingyan.hustpass.MyApplication;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by ant on 14-8-5.
 */
public class CacheDaoHelper {
    Application app;
    private  static CacheDaoHelper instance;

    final public static long TWO_HOUR = 1000*60*60*2;
    final public static long CACHE_TIME_OUT = 1000*60*60*2;

    public static CacheDaoHelper  getInstance(){
        if(instance!=null){
            return instance;
        }
        instance = new CacheDaoHelper(MyApplication.getInstance());
        return instance;
    }

    public CacheDaoHelper(Application app) {
        this.app = app;
    }

    private CacheDao getCacheDao(){
        return ((MyApplication) app).getDaoSession().getCacheDao();
    }


    public <T> T getCache(Class<T> t) {
        Cache cache = getCacheDao().queryBuilder()
                .where(CacheDao.Properties.Name.eq(t.getName()))
                .unique();
        if (cache!=null) {
            return new Gson().fromJson(cache.getJson(), t);
        }
        return null;
    }
    public <T> CacheInfo<T> getCacheInfo(Class<T> t) {
        Cache cache = getCacheDao().queryBuilder()
                .where(CacheDao.Properties.Name.eq(t.getName()))
                .unique();
        if (cache!=null) {
            CacheInfo<T> cacheInfo = new CacheInfo<T>();
            cacheInfo.setCache(new Gson().fromJson(cache.getJson(), t));
            cacheInfo.setTime(new Date().getTime() - cache.getDate().getTime());
            return cacheInfo;
        }
        return null;
    }

    public <T> T getCache(Class<T> t, String tag) {
        Cache cache = getCacheDao().queryBuilder()
                .where(CacheDao.Properties.Name.eq(t.getName()+tag))
                .unique();
        if (cache!=null) {
            return new Gson().fromJson(cache.getJson(), t);
        }
        return null;
    }


    public <T> CacheInfo<T> getCacheInfo(Class<T> t, String tag) {
        Cache cache = getCacheDao().queryBuilder()
                .where(CacheDao.Properties.Name.eq(t.getName()+tag))
                .unique();
        if (cache!=null) {
            CacheInfo<T> cacheInfo = new CacheInfo<T>();
            cacheInfo.setCache(new Gson().fromJson(cache.getJson(), t));
            cacheInfo.setTime(new Date().getTime() - cache.getDate().getTime());
            return cacheInfo;
        }
        return null;
    }

    public class CacheInfo<T>{
        private T cache;
        private long time;

        public void setCache(T cache) {
            this.cache = cache;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public long getTime() {
            return time;
        }

        public T getCache() {
            return cache;
        }
    }

    public <T> void putCache(T t, String tag) {
        Cache newsListCache = new Cache();
        newsListCache.setName(t.getClass().getName()+tag);
        newsListCache.setJson(new Gson().toJson(t));
        newsListCache.setDate(new Date());
        getCacheDao().insertOrReplace(newsListCache);
    }


    public <T> T getCache(Class<T> t,long time) {
        Cache cache = getCacheDao().queryBuilder()
                .where(CacheDao.Properties.Name.eq(t.getName()))
                .unique();
        //getTime() milliseconds 毫秒
        if (cache!=null&&(new Date().getTime()-cache.getDate().getTime()<time)) {
            return new Gson().fromJson(cache.getJson(), t);
        }
        return null;
    }


    public <T> void putCache(T t) {
        Cache newsListCache = new Cache();
        newsListCache.setName(t.getClass().getName());
        newsListCache.setJson(new Gson().toJson(t));
        newsListCache.setDate(new Date());
        getCacheDao().insertOrReplace(newsListCache);
    }



}

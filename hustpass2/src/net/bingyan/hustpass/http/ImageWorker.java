package net.bingyan.hustpass.http;

import android.widget.ImageView;

public interface ImageWorker {
    //设置加载时图片
    public ImageWorker setHoldImageResId(int resId);

    //设置加载失败图片
    public ImageWorker setErrorImageResId(int resId);

    //设置自定义大小
    public ImageWorker resize(int width, int height);

    /**
     * 执行图片网络请求
     *
     * @param url : "http://" "content://" "file://" "/sdcard/...."
     * */

    public void displayImageView(ImageView imageView, String url);

    public void displayImageView(ImageView imageView, String url, ImageWorkerCallBack imageWorkerCallBack);

    public interface ImageWorkerCallBack {
        public ImageWorker callback();
    }
}

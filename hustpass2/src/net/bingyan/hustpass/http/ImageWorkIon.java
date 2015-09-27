package net.bingyan.hustpass.http;

import android.content.Context;
import android.widget.ImageView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.IonImageViewRequestBuilder;
import com.koushikdutta.ion.future.ImageViewFuture;

import net.bingyan.hustpass.MyApplication;
import net.bingyan.hustpass.R;

public class ImageWorkIon implements ImageWorker {

    Context context;

    boolean resize;
    int resizeWidth;
    int resizeHeight;

    int holdImageResId;
    int errorImageResId;

    ImageWorkIon instace;

    public ImageWorkIon(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        init();
    }

    public  ImageWorkIon getInstace(){

        if(instace!=null){
            return instace;
        }
        instace = new ImageWorkIon(MyApplication.getAppContext());
        return instace;
    }


    public void init() {
        resize = false;
        holdImageResId = R.drawable.loding_image;
        errorImageResId = R.drawable.loding_image;
    }

    @Override
    public ImageWorker setHoldImageResId(int resId) {
        // TODO Auto-generated method stub
        holdImageResId = resId;
        return this;
    }

    @Override
    public ImageWorker setErrorImageResId(int resId) {
        // TODO Auto-generated method stub
        errorImageResId = resId;
        return this;
    }

    @Override
    public ImageWorker resize(int width, int height) {
        // TODO Auto-generated method stub
        resize = true;
        resizeHeight = height;
        resizeWidth = width;
        return this;
    }

    @Override
    public void displayImageView(ImageView imageView, String url) {
        // TODO Auto-generated method stub
        displayImageView(imageView, url, null);
    }

    @Override
    public void displayImageView(ImageView imageView, String url,
                                 final ImageWorkerCallBack callBack) {
        // TODO Auto-generated method stub
        IonImageViewRequestBuilder builder = (IonImageViewRequestBuilder) Ion
                .with(context).load(url).withBitmap()
                .placeholder(holdImageResId).error(errorImageResId);
        if (resize)
            builder.resize(resizeWidth, resizeHeight);

        ImageViewFuture imageViewFuture = builder.intoImageView(imageView);
        if (callBack != null)
            imageViewFuture.setCallback(new FutureCallback<ImageView>() {
                @Override
                public void onCompleted(Exception arg0, ImageView arg1) {
                    // TODO Auto-generated method stub
                    callBack.callback();
                }
            });

        init();
    }
}
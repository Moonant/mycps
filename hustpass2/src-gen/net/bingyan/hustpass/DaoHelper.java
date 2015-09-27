package net.bingyan.hustpass;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ant on 14-8-4.
 */
public class DaoHelper {
    public static List<Slide> initSlideDao(Context context, SlideDao slideDao){
        String[] imgurls = new String[] {
                "http://slide.hustonline.net/image/1.jpg",
                "http://slide.hustonline.net/image/2.jpg",
                "http://slide.hustonline.net/image/3.png",
                "http://slide.hustonline.net/image/4.png" };
        String[] urls = new String[] {
                "http://ik.hustonline.net/wap", "http://ershou.hustonline.net",
                "http://rank.hustonline.net", "http://weibo.com/bingyanhust" };

        List<Slide> slides = new ArrayList<Slide> ();
        for (int i=0;i<imgurls.length;i++){
            Slide slide = new Slide();
            slide.setId((long)i);
            slide.setImageurl(imgurls[i]);
            slide.setSiteurl(urls[i]);
            slides.add(slide);
            slideDao.insert(slide);
        }
        return slides;
    }
}

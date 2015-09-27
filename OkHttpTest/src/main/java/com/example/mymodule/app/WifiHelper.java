package com.example.mymodule.app;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ant on 14-8-10.
 */
public class WifiHelper {
    OkHttpClient client ;


    public WifiHelper(){
        client = new OkHttpClient();
        client.setConnectTimeout(5 * 1000, TimeUnit.MILLISECONDS);
        client.setReadTimeout(5 * 1000, TimeUnit.MILLISECONDS);
    }

    public void start(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String msg = login();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();

    }

    public String login() throws IOException{
        String url = getWifiLoginurl();

        MediaType JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
        String json = "username=U201213465&pwd=000999&validcode=no_check&is_check=true";
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept-Language","zh-TW")
                .header("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17")
                .build();

        Response response = client.newCall(request).execute();

        String  s="function showAuthErrorMessage([\\s\\S]*?)showError\\('(.*?)'\\)";
        Pattern pattern = Pattern.compile(s);
        Matcher matcher = pattern.matcher(response.body().string());

        String msg = "";
        if (matcher.find()){
            msg = matcher.group(2);
            System.out.println(matcher.group(2));
        }

        return msg;
    }

    public String getWifiLoginurl() throws IOException {
        String url = "http://www2.hustonline.net/user/testemail";

        // get login html url from baidu
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        String html = response.body().string();





        System.out.println(html);


        Pattern pattern = Pattern.compile("location.href=['\"](.*?)['\"]");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            url = (matcher.group(1));
            System.out.println(url);
        }else {
            //不是校园网，或者已登录
            return null;
        }

        //get login url
        request = new Request.Builder()
                .url(url)
                .header("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17")
                .build();

        response = client.newCall(request).execute();
        url = response.request().urlString();
        System.out.println(url);

        //get login form url
        String s1 = "./HKchinaMobileLogin.html?";
        String s2 = "userV2.do?method=login&param=true&";
        url = url.replace(s1,s2);
        return url;
    }


}

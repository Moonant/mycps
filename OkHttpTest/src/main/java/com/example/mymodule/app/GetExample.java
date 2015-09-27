package com.example.mymodule.app;


import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetExample {
    OkHttpClient client = new OkHttpClient();

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17")
                .build();


        Response response = client.newCall(request).execute();

        url = response.header("Location",null);
        System.out.println(response.headers().toString());
        System.out.println(""+response.code());

        System.out.println(response.request().urlString());
        while (url!=null) {
            System.out.println(url);
            request = new Request.Builder()
                    .url(url)
                    .build();

            response = client.newCall(request).execute();
            url = response.header("Location",null);
        }
        return response.body().string();
    }

    String getWifiLoginurl() throws IOException{
        String url = "http://www.baidu.com";

        // get login html url from baidu
        Request request = new Request.Builder()
                .url(url)
//                .header("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17")
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

//            System.out.println(response.headers().toString());
//            System.out.println(""+response.code());
//            System.out.println(response.request().urlString());
        return url;
    }

    String login(String url) throws IOException{
        MediaType JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
        String json = "username=U201213465&pwd=000999&validcode=no_check&is_check=true";
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
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
    String logout(String url) throws IOException{
        String s1 = "method=login";
        String s2 = "method=logout";
        url = url.replace(s1,s2);
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17")
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("hello world,Let's start");
        GetExample example = new GetExample();
//        String response = example.run("https://raw.github.com/square/okhttp/master/README.md");
//        String response = example.run("http://www.baidu.com");
//        String response = example.run("http://192.168.50.3:8080/eportal/index.jsp?wlanuserip=9bcb81f3c9f531e44b5387f0d3c31d21&wlanacname=5fcbc245a7ffdfa4&ssid=&nasip=9623c2a4e3d61fd465d8afdd9ebf87dd&mac=cbd8ba371f708bbb9a52927a4523c0be&t=wireless-v2&url=cd1458b3c353c25d70b79300d4544cafb24d9f0fe057bafe949681b9031381487bb8a06643e1bacf");
        String response = example.getWifiLoginurl();
        System.out.println(response);
        if(response==null) return;
        String msg = example.login(response);
    }
}

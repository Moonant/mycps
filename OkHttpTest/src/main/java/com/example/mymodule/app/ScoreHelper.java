package com.example.mymodule.app;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by harry on 15-3-3.
 */
public class ScoreHelper {
    OkHttpClient client;
    String key1="",key2="";

    public ScoreHelper(){
        client = new OkHttpClient();
        client.setConnectTimeout(10 * 1000, TimeUnit.MILLISECONDS);
        client.setReadTimeout(10 * 1000, TimeUnit.MILLISECONDS);

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(cookieManager);
    }

    public void start(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String name = "U201213465";
                    String psw = "TW9vbmFudDg1Mg==";

                    login(name,psw);
                    getkey();
                    getScore(name,"20141");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }
    public void login(String name, String psw) throws IOException {
        String loginurl = "http://bksjw.hust.edu.cn/hublogin.action";
        MediaType JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
        String json = "username="+name+"&password="+psw+"&submit=立即登录&usertype=xs";
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(loginurl)
                .post(body)
                .header("Referer", "http://bksjw.hust.edu.cn/index.jsp")
                .build();

        Response response = client.newCall(request).execute();

//        System.out.println(response.body().string());
    }

    public void getkey() throws Exception{
        String url = "http://bksjw.hust.edu.cn/aam/score/QueryScoreByStudent_readyToQuery.action?cdbh=225";
        Request request = new Request.Builder()
                .url(url)
                .header("Referer", "http://bksjw.hust.edu.cn/frames/body_left.jsp")
                .build();
        Response response = client.newCall(request).execute();


        String body = response.body().string();


        String  s="key1\"\\ value=\"(.*)\"";
        Pattern pattern = Pattern.compile(s);
        Matcher matcher = pattern.matcher(body);

        if (matcher.find()){
            key1 = matcher.group(1);
        }

        s="key2\"\\ value=\"(.*)\"";
        pattern = Pattern.compile(s);
        matcher = pattern.matcher(body);

        if (matcher.find()){
            key2 = matcher.group(1);
        }

        System.out.println(""+key1+" "+key2);
    }

    public void getScore(String name,String xqselect) throws Exception{
        String url = "http://bksjw.hust.edu.cn/aam/score/QueryScoreByStudent_queryScore.action";

        MediaType JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
        String json = "stuSfid="+name+"&key1="+key1+"&key2="+key2+"&type=cj&xqselect="+xqselect;
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17")
                .header("Referer", "http://bksjw.hust.edu.cn/aam/score/QueryScoreByStudent_readyToQuery.action?cdbh=225")
                .build();

        System.out.println(client.getCookieHandler().toString());

        Response response = client.newCall(request).execute();
        String bodyS = response.body().string();

        String s="<tr[^>]*>\\s*<td[^>]*>(.*)</td>\\s*<td[^>]*>(.*)</td>\\s*<td[^>]*>(.*)</td>\\s*<td[^>]*>(.*)</td>";
        Pattern pattern = Pattern.compile(s);
        Matcher matcher = pattern.matcher(bodyS);

//        while (matcher.find()){
//            System.out.println(matcher.group(1)+" "+matcher.group(2)+" "+matcher.group(3)) ;
//        }


        s="院系：(.*)班级：(.*)姓名：(.*)学号：(.*)</td>";
        pattern = Pattern.compile(s);
        matcher = pattern.matcher(bodyS);

        if (matcher.find()){
            System.out.println(matcher.group(1).replaceAll("&nbsp;","")+" "+matcher.group(2)+" "+matcher.group(3)+" "+matcher.group(4)) ;
        }

        s="获得学分</td>\\s*<td[^>]*>(.*)</td>";
        pattern = Pattern.compile(s);
        matcher = pattern.matcher(bodyS);

        if (matcher.find()){
            System.out.println(matcher.group(1));
        }
    }


    public static void main(String[] args) throws IOException {
        ScoreHelper example = new ScoreHelper();
        example.start();
    }
}

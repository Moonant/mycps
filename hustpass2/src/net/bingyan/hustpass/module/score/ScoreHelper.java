package net.bingyan.hustpass.module.score;

import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogRecord;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;

/**
 * Created by harry on 15-3-4.
 */
public class ScoreHelper {
    OkHttpClient client;
    String key1 = "", key2 = "";

    public ScoreHelper() {
        client = new OkHttpClient();
        client.setConnectTimeout(10 * 1000, TimeUnit.MILLISECONDS);
        client.setReadTimeout(10 * 1000, TimeUnit.MILLISECONDS);

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(cookieManager);
    }


    public void checkPsw(final String name, final String psw, final Callback<HubCheckBean> cb) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final HubCheckBean hubCheckBean = new HubCheckBean();
                    if (login(name, psw)) {
                        hubCheckBean.setCode(0);
                    } else {
                        hubCheckBean.setCode(1);
                        hubCheckBean.setMsg("用户名或密码错误。");
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            cb.success(hubCheckBean, null);
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            cb.success(null, null);
                        }
                    });

                }
            }
        }).start();
    }

    public boolean login(String name, String psw) throws IOException {
        psw = Base64.encodeToString(psw.getBytes(), 0);
        String loginurl = "http://bksjw.hust.edu.cn/hublogin.action";
        MediaType JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
        String json = "username=" + name + "&password=" + psw + "&submit=立即登录&usertype=xs";
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(loginurl)
                .post(body)
                .header("Referer", "http://bksjw.hust.edu.cn/index.jsp")
                .build();

        Response response = client.newCall(request).execute();

        String bodys = response.body().string();


        String s = "frames/body_left.jsp";
        Pattern pattern = Pattern.compile(s);
        Matcher matcher = pattern.matcher(bodys);

        if (matcher.find()) {
            return true;
        }
        return false;
    }

    public void getkey() throws Exception {
        String url = "http://bksjw.hust.edu.cn/aam/score/QueryScoreByStudent_readyToQuery.action?cdbh=225";
        Request request = new Request.Builder()
                .url(url)
                .header("Referer", "http://bksjw.hust.edu.cn/frames/body_left.jsp")
                .build();
        Response response = client.newCall(request).execute();


        String body = response.body().string();


        String s = "key1\"\\ value=\"(.*)\"";
        Pattern pattern = Pattern.compile(s);
        Matcher matcher = pattern.matcher(body);

        if (matcher.find()) {
            key1 = matcher.group(1);
        }

        s = "key2\"\\ value=\"(.*)\"";
        pattern = Pattern.compile(s);
        matcher = pattern.matcher(body);

        if (matcher.find()) {
            key2 = matcher.group(1);
        }

        System.out.println("" + key1 + " " + key2);
    }

    public void getScore(final String name,final String psw,final String xqselect, final Callback<ScoreBeanV2> cb) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    login(name, psw);
                    getkey();

                    String url = "http://bksjw.hust.edu.cn/aam/score/QueryScoreByStudent_queryScore.action";

                    MediaType JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
                    String json = "stuSfid=" + name + "&key1=" + key1 + "&key2=" + key2 + "&type=cj&xqselect=" + xqselect;
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17")
                            .header("Referer", "http://bksjw.hust.edu.cn/aam/score/QueryScoreByStudent_readyToQuery.action?cdbh=225")
                            .build();

                    Response response = client.newCall(request).execute();
                    String bodyS = response.body().string();



                    final ScoreBeanV2 scoreBean = new ScoreBeanV2();
                    ScoreBeanV2.Data data = scoreBean.new Data();
                    ScoreBeanV2.Data.Information information = data.new Information();
                    data.setInformation(information);
                    data.setTime("time");
                    data.setWeightedscore("weightedscore");
                    List<ScoreBeanV2.Data.Score> scores = new ArrayList<ScoreBeanV2.Data.Score>();
                    data.setScore(scores);
                    scoreBean.setCode(0);
                    scoreBean.setData(data);

                    String s = "<tr[^>]*>\\s*<td[^>]*>(.*)</td>\\s*<td[^>]*>(.*)</td>\\s*<td[^>]*>(.*)</td>\\s*<td[^>]*>(.*)</td>";
                    Pattern pattern = Pattern.compile(s);
                    Matcher matcher = pattern.matcher(bodyS);
                    while (matcher.find()) {
                        ScoreBeanV2.Data.Score score = data.new Score();
                        score.setName(matcher.group(1));
                        score.setScore(matcher.group(2));
                        score.setCredit(matcher.group(3));
                        scores.add(score);
                    }

                    s="院系：(.*)班级：(.*)姓名：(.*)学号：(.*)</td>";
                    pattern = Pattern.compile(s);
                    matcher = pattern.matcher(bodyS);
                    if (matcher.find()){
                        information.setCollege(matcher.group(1).replaceAll("&nbsp;",""));
                        information.setClass_(matcher.group(2).replaceAll("&nbsp;",""));
                        information.setName(matcher.group(3).replaceAll("&nbsp;",""));
                        information.setId(matcher.group(4).replaceAll("&nbsp;",""));
                    }

                    s="获得学分</td>\\s*<td[^>]*>(.*)</td>";
                    pattern = Pattern.compile(s);
                    matcher = pattern.matcher(bodyS);
                    if (matcher.find()){
                        data.setWeightedscore(matcher.group(1));
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            cb.success(scoreBean, null);
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            cb.success(null, null);
                        }
                    });

                }
            }
        }).start();
    }

    Handler handler = new Handler();

}
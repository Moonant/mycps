package net.bingyan.hustpass;


import net.bingyan.hustpass.module.account.AccountBean;
import net.bingyan.hustpass.module.classroom.ClassBeanV2;
import net.bingyan.hustpass.module.classroom.ClassBeanV3;
import net.bingyan.hustpass.module.compter.ComputerBean;
import net.bingyan.hustpass.module.elec.ElecBean;
import net.bingyan.hustpass.module.food.FoodShopBean;
import net.bingyan.hustpass.module.food.FoodShopListBean;
import net.bingyan.hustpass.module.ann.AnnIndexBean;
import net.bingyan.hustpass.module.ann.AnnListBean;
import net.bingyan.hustpass.module.lecture.LectureContentBean;
import net.bingyan.hustpass.module.lecture.LectureListBean;
import net.bingyan.hustpass.module.news.NewsBean;
import net.bingyan.hustpass.module.news.NewsContentBean;
import net.bingyan.hustpass.module.news.NewsSlideBean;
import net.bingyan.hustpass.module.home.SlidesBean;
import net.bingyan.hustpass.module.recruit.RecruitActivityBean;
import net.bingyan.hustpass.module.recruit.RecruitLotteryBean;
import net.bingyan.hustpass.module.score.HubCheckBean;
import net.bingyan.hustpass.module.score.ScoreBeanV2;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public class API {
	public static final String APIHOST = "http://api.hustonline.net/";

	
	public static class News {
//		public static final int[] sort = { 1138, 12, 289 };
		public static final String[] title = { "学校要闻", "综合新闻", "菁菁校园" };
		/** 新闻内容，id=？ */
		public static final String NEWS_CONTENT = API.APIHOST
				+ "hustnewsapi/single_article.aspx?id=%d&token=hustnewsapi";
	}



	public static class Library {
		public static final String SEARCH_URL = APIHOST + "library/?op=search&type=%s&word=%s&page=%d";
		public static final String DETAIL_URL = APIHOST + "library/?op=book&type=json&id=%s";
	}

    /***********2.1*****************/
    public interface AppService {
        public static String slideHOST = "http://slide.hustonline.net";
        @GET("/list.php?n=4")
        void getSlides(Callback<SlidesBean> cb);
    }

    public interface AnnService{
        @GET("/announcement/index/")
        void getIndex(Callback<AnnIndexBean> cb);

        @GET("/announcement/index/{department}/{title}/{offset}")
        void getList(@Path("department") String department,@Path("title") String title,@Path("offset") int offset,Callback<AnnListBean> cb);

        public static final String ANNOUNCEMENT_CONTENT_URL = "http://api.hustonline.net/announcement/content/%d";
    }

    public interface ClassRoomServiceV2{
        public  static  String HOST="http://202.114.18.13:9093/";
        // 7,1,5,8
        @GET("/check_classroom?class=class1+class2+class3+class4+class5&is_divide=1")
        void getClassRoom(@Query("building") String buildinglist ,Callback<ClassBeanV2> cb);
    }

    public interface ClassRoomServiceV3{
        public  static  String HOST="http://api.hustonline.net";
        // 7,1,5,8
        @GET("/classroom/get_class_info?token=classroombingyan&dateIndex=0")
        void getClassRoom(@Query("building") String buildinglist ,Callback<ClassBeanV3> cb);
    }

    public interface ComputerService{
        public  static  String HOST="http://xb.hustonline.net";
        @GET("/jifang/usage")
        void getComputer(Callback<ComputerBean> cb);
    }

    public interface ElecService{
        @GET("/electricity/")
        void getElec(@Query("quyu") String quyu,@Query("loudong") int loudong, @Query("fangjian") String fangjian,Callback<ElecBean> cb);
    }

    public  interface FoodService{
        public  static  String HOST="http://waimai.hustonline.net";
        @GET("/api")
        void getShopList(Callback<FoodShopListBean> cb);
        @GET("/api/shop/{id}")
        void getShop(@Path("id") int id,Callback<FoodShopBean> cb);
    }

    public interface NewsService {
        public static final int[] sort = { 1138, 12, 289 };
        public static final String[] title = { "学校要闻", "综合新闻", "菁菁校园" };

        /** 新闻列表，参数site=1，sort=？（三类），pagesize=10，pagenum=？ */
        @GET("/hustnewsapi/article_list.aspx?site=1&pagesize=10&token=hustnewsapi")
        void getNewsList(@Query("sort") int sort,@Query("pagenum") int pagenum,Callback<NewsBean> cb);

        /** 新闻内容，id=？ */
        @GET("/hustnewsapi/single_article.aspx?id=%d&token=hustnewsapi")
        void getNewsContent(@Query("id") int id, Callback<NewsContentBean> cb );

        @GET("/hustnewsapi/slide_article.php?goal=1&token=hustnewsapi")
        void getNewsSlide(@Query("sort") int sort, Callback<NewsSlideBean> cb);
    }

    public interface LectureService{
        // 10位 php时间戳
        @GET("/lecture/lecture_list/")
        public void getList(@Query("after") long id, Callback<LectureListBean> cb);

        @GET("lecture/lecture_info/")
        public void getContent(@Query("id") int id, Callback<LectureContentBean> cb);
    }

    public interface HubService{
        public final static String CheckPswHOST = "http://202.114.18.13:9093";
        public final static String ScoreHOST = "http://xb.hustonline.net";

        @GET("/check_hub")
        public void checkPsw(@Query("usr") String usr, @Query("psw") String psw, Callback<String> cb);

        @GET("/rapi/{term}/{uid}/{sha}")
        public void getScore(@Path("term") String term,@Path("uid") String uid,@Path("sha") String sha,Callback<ScoreBeanV2> cb);

    }

    public interface HubServiceV2{
        public static final String CheckPswHOST = "http://api2.hustonline.net";
        public final static String ScoreHOST = "http://score.hustonline.net";
        @GET("/v2/hublogin/check_hub")
        public void checkPsw(@Query("username") String usr, @Query("password") String psw, Callback<HubCheckBean> cb);

        @GET("/getScore")
        public void getScore(@Query("xqh") String term,@Query("sfid") String uid,Callback<ScoreBeanV2> cb);
    }

    public interface HustOnlineService{
        public static final String HOST = "http://www2.hustonline.net";

        // ( True(被注册过) / False )
        @GET("/user/testemail")
        public void checkEmail(@Query("email") String email, Callback<String> cb);

        // ( 0 / 1(被注册过) )
        @GET("/user/testusername")
        public void checkUsername(@Query("username") String username, Callback<String> cb);

        // ( True / False )
        @FormUrlEncoded
        @POST("/user/testuser")
        public void Checkuser(@Field("email") String email, @Field("passwd") String passwd, Callback<String> cb);

        // hustonline_id / False
        @GET("/user/get_id")
        public void getHustonlineId(@Query("email") String email, Callback<String> cb);

        // hustonline_name / False
        @GET("/user/get_username")
        public void getUserName(@Query("email") String email, Callback<String> cb);

        // True / False
        @FormUrlEncoded
        @POST("/user/wap_adduser")
        public void adduser(@Field("username") String username,@Field("email") String email, @Field("passwd") String passwd, Callback<String> cb);
    }

    public interface HustpassService{
        public static final String HOST = "http://account.hustpass.hustonline.net";

        @GET("/")
        public void get(Callback<String> cb);


        // type: hub，wifi，electric
        //{"hub_id": "asjdhsajd", "id": 1, "hustonline_id": 1355973198, "hub_passwd": "12362136"}
        @FormUrlEncoded
        @POST("/hub/get_info")
        public void getHubInfo(@Field("hustonline_id") Long hustonline_id
                , @Field("hustonline_passwd") String hustonline_passwd
                ,Callback<AccountBean.HubAccountBean> cb);

        // 返回值：1-4
        @FormUrlEncoded
        @POST("/hub/update_info")
        public void updateHubInfo(@Field("hub_id") String hub_id
                , @Field("hub_passwd") String hub_passwd
                , @Field("hustonline_id") Long hustonline_id
                , @Field("hustonline_passwd") String hustonline_passwd
                ,Callback<Integer> cb);




        //{"wifi_id": "asjdhsajd", "id": 1, "hustonline_id": 1355973198, "wifi_passwd": "12362136"}
        @FormUrlEncoded
        @POST("/wifi/get_info")
        public void getwifiInfo(@Field("hustonline_id") Long hustonline_id
                , @Field("hustonline_passwd") String hustonline_passwd
                ,Callback<AccountBean.WifiAccountBean> cb);

        // 返回值：1-4
        @FormUrlEncoded
        @POST("/wifi/update_info")
        public void updateWifiInfo(@Field("wifi_id") String wifi_id
                , @Field("wifi_passwd") String wifi_passwd
                , @Field("hustonline_id") Long hustonline_id
                , @Field("hustonline_passwd") String hustonline_passwd
                ,Callback<Integer> cb);


        //{"area_id": "韵苑", "id": 1, "hustonline_id": 1355973198, "building_id": 25, "room_id": 509}
        @FormUrlEncoded
        @POST("/elec/get_info")
        public void getelecInfo(@Field("hustonline_id") Long hustonline_id
                , @Field("hustonline_passwd") String hustonline_passwd
                ,Callback<AccountBean.ElecAccountBean> cb);

        // 返回值：1-4
        @FormUrlEncoded
        @POST("/elec/update_info")
        public void updateelecInfo(@Field("area_id") String area_id
                , @Field("building_id") int building_id
                , @Field("room_id") String room_id
                , @Field("hustonline_id") Long hustonline_id
                , @Field("hustonline_passwd") String hustonline_passwd
                ,Callback<Integer> cb);
    }

    //招新API
    public interface RecruitService {

        //获取抽奖号
        @GET("/v2/random/lottery")
        public void lottery(@Query("id") String id, Callback<RecruitLotteryBean> cb);

        @GET("/v2/GetActivity/get_activity/{time}")
        public void getState(@Path("time") String time, Callback<RecruitActivityBean> cb);
    }
}

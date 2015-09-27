package net.bingyan.hustpass.module.lecture;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.API;
import net.bingyan.hustpass.http.RestHelper;
import net.bingyan.hustpass.ui.base.BaseActivity;
import net.bingyan.hustpass.util.Util;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LectureContentActivity extends BaseActivity {
	TextView mTitle;
	TextView mTime;
	TextView mLocation;
	TextView mSpeaker;
	TextView mContent;
	int mId;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
        setContentView(R.layout.activity_lecture_content);
		mId = getIntent().getIntExtra("id", 0);
        init();
	}

    private void getByHttp(Callback<LectureContentBean> cb){
        RestHelper.getService(API.LectureService.class).getContent(mId,cb);
    }

    private void init(){
        getByHttp(new Callback<LectureContentBean>() {
            @Override
            public void success(LectureContentBean lectureContentBean, Response response) {
                initData(lectureContentBean);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

	private void initData( LectureContentBean lectureContentBean) {
		mTitle = (TextView) findViewById(R.id.lecture_title);
		mTime = (TextView) findViewById(R.id.lecture_time);
		mLocation = (TextView) findViewById(R.id.lecture_location);
		mSpeaker = (TextView) findViewById(R.id.lecture_speeker);
		mContent = (TextView) findViewById(R.id.lecture_content);

        mTitle.setText(lectureContentBean.getTitle());
        mTime.setText(Util.formatDate(
                Long.parseLong(lectureContentBean.getBeginTime()) * 1000, "EEEE aa h:mm"));
        mLocation.setText(lectureContentBean.getLocation());
        mSpeaker.setText(lectureContentBean.getSpeaker());

        if (TextUtils.isEmpty(lectureContentBean.getDescription())) {
            mContent.setText(lectureContentBean.getCategory());
        } else {
            mContent.setText(Html.fromHtml(lectureContentBean
                    .getDescription()));
        }
	}
}

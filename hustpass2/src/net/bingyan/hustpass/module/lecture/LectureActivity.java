package net.bingyan.hustpass.module.lecture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.API;
import net.bingyan.hustpass.http.RestHelper;
import net.bingyan.hustpass.module.ann.AnnListBean;
import net.bingyan.hustpass.module.lecture.LectureListBean.Result;
import net.bingyan.hustpass.ui.base.BaseActivity;
import net.bingyan.hustpass.util.Util;
import net.bingyan.hustpass.widget.AnnTagView;
import net.bingyan.hustpass.widget.XListView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LectureActivity extends BaseActivity implements OnItemClickListener {

    List<List<HashMap<String, Object>>> mItemList = new ArrayList<List<HashMap<String, Object>>>();
	BaseAdapter mListAdapter = new XListAdapter(mItemList);
    XListView mListView;

	String mCurrentDayTag = "";

    static final int TAG_SINGLE = AnnTagView.MODE_SINGLE;
    static final int TAG_FIRST = AnnTagView.MODE_TOP;
    static final int TAG_MIDDLE = AnnTagView.MODE_MID;
    static final int TAG_LAST = AnnTagView.MODE_BOTTOM;


	private String[] monthArray;

	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		monthArray = getResources().getStringArray(R.array.month_names);

        setContentView(R.layout.activity_announcement_listview);
        initView();
	}

	public void initView() {
		mListView = (XListView) findViewById(R.id.list_view);

		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(false);

        mListView.setAdapter(mListAdapter);
		mListView.setOnItemClickListener(this);
		init();
	}

    public void getByHttp(Callback<LectureListBean> cb){
        RestHelper.getService(API.LectureService.class).getList(System.currentTimeMillis() / 1000,cb);
    }

    private void init(){
        showProgressBar();
        getByHttp(new Callback<LectureListBean>() {
            @Override
            public void success(LectureListBean lectureListBean, Response response) {
                initData(lectureListBean);
                finalexe();
            }

            @Override
            public void failure(RetrofitError error) {
                Util.toast("网络出错"+error.getMessage());
                finalexe();
            }

            private void finalexe(){
                stopProgressBar();
            }
        });
    }

    private void initData(LectureListBean lectureListBean){
        View emptyView = findViewById(R.id.listview_empty);
        mListView.setEmptyView(emptyView);
        count = 0;
        oldSize = 0;
        addItemToList(lectureListBean.getResult());
        mListAdapter.notifyDataSetChanged();
    }

    int count = 0;
    int oldSize = 0;
    public void addItemToList(List<Result> datas) {
        List<List<HashMap<String, Object>>> list = mItemList;
        for (Result data : datas) {
            String date = Util.formatDate(Long.parseLong(data.getBeginTime()) * 1000, "yyyy-MM-dd");

            count++;
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("title", data.getTitle());
            map.put("dayTag", date);
            map.put("id", data.getId());

            if(count==1){
                mCurrentDayTag = date; // 2013-11-22
                List<HashMap<String, Object>> newSection = new ArrayList<HashMap<String, Object>>();
                newSection.add(map); // this
                list.add(newSection);
                if(datas.size() == 1){
                    map.put("tagBg",TAG_SINGLE);
                }
                continue;
            }

            List<HashMap<String, Object>> lastSection = list.get(list.size()-1);
            int tag;

            if (!mCurrentDayTag.equals(date)) { // add new section

                if(lastSection.size() == 1){ // last section 只有一个，或者这是最后一个
                    tag = TAG_SINGLE;
                }else {
                    tag = TAG_LAST;
                }

                if(count == datas.size()+oldSize){
                    tag = TAG_SINGLE;
                    map.put("tagBg",TAG_SINGLE);
                }

                lastSection.get(lastSection.size()-1).put("tagBg",tag); //last

                mCurrentDayTag = date; // 2013-11-22
                List<HashMap<String, Object>> newSection = new ArrayList<HashMap<String, Object>>();
                newSection.add(map); // this
                list.add(newSection);

            } else {

                if(lastSection.size()==1){
                    tag = TAG_FIRST;
                }else {
                    tag = TAG_MIDDLE;
                }

                if(count == datas.size()+oldSize){
                    tag = TAG_FIRST;
                    map.put("tagBg",TAG_LAST);
                }

                lastSection.get(lastSection.size()-1).put("tagBg",tag); //last
                lastSection.add(map); // this
            }
        }
        oldSize = count;
    }

    class XListAdapter extends BaseAdapter {
        List<List<HashMap<String, Object>>> list ;

        public XListAdapter(List<List<HashMap<String, Object>>> list) {
            // TODO Auto-generated constructor stub
            this.list = list;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            int count = 0;
            for (List<HashMap<String, Object>> section : list){
                count+=section.size();
            }
            return count;
        }

        @Override
        public Object getItem(int position) { //1
            // TODO Auto-generated method stub
            int count = 0;
            int i = 0;
            for (List<HashMap<String, Object>> section : list){
                if(count+section.size() > position){
                    section.get(position-count).put("sectionId",i);
                    return section.get(position-count);
                }
                count+=section.size();
                i++;
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            HashMap<String, Object> map = (HashMap<String, Object>) getItem(position);

            if(convertView == null){
                convertView = View.inflate(LectureActivity.this, R.layout.activity_ann_list_item, null);
                ViewHolder viewHolder = new ViewHolder();
                convertView.setTag(viewHolder);
                viewHolder.annTagView = (AnnTagView) convertView.findViewById(R.id.tag_view);
                viewHolder.dayTv = (TextView) convertView.findViewById(R.id.announcement_tag_day);
                viewHolder.monthTv = (TextView) convertView.findViewById(R.id.announcement_tag_month);
                viewHolder.titleTv = (TextView) convertView.findViewById(R.id.title);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.id = (String)map.get("id");


            int tag = (Integer) map.get("tagBg");
            holder.titleTv.setText((String) map.get("title"));

            mLog.v(""+(Integer)map.get("sectionId"));
            if(((Integer)map.get("sectionId"))%2 ==0) {
                mLog.v("blue:"+position);
                holder.annTagView.setMode(tag, R.color.app_blue);
            }else {
                mLog.v("yellow:"+position);
                holder.annTagView.setMode(tag, R.color.app_yellow);
            }

            if (tag == TAG_FIRST || tag == TAG_SINGLE) {
                String[] date = ((String) map.get("dayTag")).split("-");
                holder.dayTv.setVisibility(View.VISIBLE);
                holder.monthTv.setVisibility(View.VISIBLE);
                holder.dayTv.setText(date[2]);
                holder.monthTv.setText(" "+monthArray[Integer.parseInt(date[1]) - 1] + ".");
            }else {
                holder.dayTv.setVisibility(View.INVISIBLE);
                holder.monthTv.setVisibility(View.INVISIBLE);
            }

            return convertView;
        }

        class ViewHolder{
            AnnTagView annTagView;
            TextView dayTv;
            TextView monthTv;
            TextView titleTv;
            String id;
        }
    }


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
//		String vId = (String) view.getTag();
		Intent intent = new Intent();
		intent.putExtra("id",((XListAdapter.ViewHolder) view.getTag()).id);
		intent.setClass(LectureActivity.this, LectureContentActivity.class);
		startActivity(intent);
	}
}

package net.bingyan.hustpass.module.ann;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.gson.Gson;

import net.bingyan.hustpass.API;
import net.bingyan.hustpass.R;
import net.bingyan.hustpass.db.CacheDaoHelper;
import net.bingyan.hustpass.http.RestHelper;
import net.bingyan.hustpass.ui.base.WebViewActivity;
import net.bingyan.hustpass.util.AppLog;
import net.bingyan.hustpass.util.Util;
import net.bingyan.hustpass.widget.AnnTagView;
import net.bingyan.hustpass.widget.XListView;
import net.bingyan.hustpass.widget.XListView.IXListViewListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AnnFragment extends SherlockFragment implements
		IXListViewListener, OnItemClickListener {
    AppLog mLog = new AppLog(getClass());

	public List<List<HashMap<String, Object>>> mItemList = new ArrayList<List<HashMap<String, Object>>>();
	protected XListAdapter mListAdapter = new XListAdapter(mItemList);
    protected XListView mListView;

	String mCurrentDayTag = "";

    static final int TAG_SINGLE = AnnTagView.MODE_SINGLE;
    static final int TAG_FIRST = AnnTagView.MODE_TOP;
    static final int TAG_MIDDLE = AnnTagView.MODE_MID;
    static final int TAG_LAST = AnnTagView.MODE_BOTTOM;

	public String[] monthArray;

    Bundle bundle;

    boolean isLoading = false;

    LayoutInflater inflater;

	public static AnnFragment newInstance(Bundle bundle) {
		AnnFragment nf = new AnnFragment();
		nf.setArguments(bundle);
		return nf;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		bundle = getArguments();
		monthArray = getResources().getStringArray(R.array.month_names);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        this.inflater = inflater;
		View view = inflater.inflate(R.layout.activity_announcement_listview, container, false);

		mListView = (XListView) view.findViewById(R.id.list_view);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(false);

		mListView.setXListViewListener(this);
		mListView.setOnItemClickListener(this);
		mListView.setAdapter(mListAdapter);

        init();
		return view;
	}

    public AnnListBean getCache(){
        return CacheDaoHelper.getInstance().getCache(AnnListBean.class,bundle.getString("department")+bundle.getString("title"));
    }

    public void putCache(AnnListBean annListBean){
        CacheDaoHelper.getInstance().putCache(annListBean, bundle.getString("department")+bundle.getString("title"));
    }

    public void getByHttp(int offset, Callback<AnnListBean> cb){
        RestHelper.getService(API.AnnService.class).getList(bundle.getString("department"),bundle.getString("title"),offset,cb);
    }

    private void init(){
        final AnnListBean annListBeanCache = getCache();

        if(annListBeanCache!=null){
            initData(annListBeanCache);
        }

        getByHttp(0,new Callback<AnnListBean>() {
            @Override
            public void success(AnnListBean annListBean, Response response) {

                if(!annListBean.equals(annListBeanCache)){
                    initData(annListBean);
                    putCache(annListBean);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Util.toast(R.string.net_error_toast);
            }
        });
    }


    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub

        if(isLoading){
            return;
        }

        isLoading = true;
        getByHttp(mListAdapter.getCount(),new Callback<AnnListBean>() {
            @Override
            public void success(AnnListBean annListBean, Response response) {
                addData(annListBean);
                isLoading = false;
            }

            @Override
            public void failure(RetrofitError error) {
                isLoading = false;
            }
        });
    }

    private void initData(AnnListBean annListBean){
        mItemList.clear();
        count = 0;
        oldSize = 0;
        addData(annListBean);
    }

    private void addData(AnnListBean annListBean){
        mLog.v(new Gson().toJson(annListBean));
        addItemToList(annListBean.getData());
        mListAdapter.notifyDataSetChanged();
    }
    int count = 0;
    int oldSize = 0;
    public void addItemToList(List<AnnListBean.Datum> datas) {
        List<List<HashMap<String, Object>>> list = mItemList;
        for (AnnListBean.Datum data : datas) {
            count++;
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("title", data.getTitle());
            map.put("dayTag", data.getDate());
            map.put("id", data.getId());

            if(count==1){
                mCurrentDayTag = data.getDate(); // 2013-11-22
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

            if (!mCurrentDayTag.equals(data.getDate())) { // add new section

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

                mCurrentDayTag = data.getDate(); // 2013-11-22
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
                convertView = inflater.inflate(R.layout.activity_ann_list_item, null,false);
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
        String vId = ((XListAdapter.ViewHolder) view.getTag()).id;
		String url = String.format(API.AnnService.ANNOUNCEMENT_CONTENT_URL,
				Integer.parseInt(vId));
		Bundle bundle = new Bundle();
		bundle.putString("url", url);
		bundle.putString("title", "院系通知");
		Intent intent = new Intent();
		intent.putExtras(bundle);
		intent.setClass(this.getActivity(), WebViewActivity.class);
		startActivity(intent);
	}

}

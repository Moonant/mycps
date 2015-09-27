package net.bingyan.hustpass.module.news;

import java.util.ArrayList;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.db.CacheDaoHelper;
import net.bingyan.hustpass.http.RestHelper;
import net.bingyan.hustpass.http.ImageWorkIon;
import net.bingyan.hustpass.http.ImageWorker;
import net.bingyan.hustpass.API;
import net.bingyan.hustpass.module.news.NewsBean.Article;
import net.bingyan.hustpass.util.AppLog;
import net.bingyan.hustpass.widget.XListView;
import net.bingyan.hustpass.widget.XListView.IXListViewListener;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NewsFragment extends SherlockFragment implements
		IXListViewListener, OnItemClickListener {

    AppLog mLog = new AppLog(getClass());
    ImageWorker imageWorker ;

    public int sort;
    private Boolean isLoading = false;

	public ArrayList<Article> mItemList = new ArrayList<Article>();
	XListView mListView;
	XListAdapter mListAdapter = new XListAdapter(mItemList);

    View mTopView;
    LayoutInflater inflater;

	public static NewsFragment newInstance(Bundle bundle) {
		NewsFragment nf = new NewsFragment();
		nf.setArguments(bundle);
		return nf;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		sort = bundle.getInt("sort");

        imageWorker = new ImageWorkIon(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        this.inflater = inflater;
		View view = inflater.inflate(R.layout.activity_news_listview, container, false);

		mListView = (XListView) view.findViewById(R.id.list_view);

        mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(false);
		mListView.setXListViewListener(this);

        mListView.setAdapter(mListAdapter);
		mListView.setOnItemClickListener(this);

        init();
		return view;
	}

    private NewsBean getListCache(){
        return CacheDaoHelper.getInstance().getCache(NewsBean.class, String.valueOf(sort));
    }

    private void putListCache(NewsBean newsBean){
        CacheDaoHelper.getInstance().putCache(newsBean, String.valueOf(sort));
    }

    private  void getListByHttp(int pagenum, Callback<NewsBean> cb){
        RestHelper.getService(API.NewsService.class).getNewsList(sort,pagenum,cb);
    }

    private NewsSlideBean getSlideCache(){
        return CacheDaoHelper.getInstance().getCache(NewsSlideBean.class, String.valueOf(sort));
    }

    private void putSlideCache(NewsSlideBean newsSlideBean){
        CacheDaoHelper.getInstance().putCache(newsSlideBean, String.valueOf(sort));
    }

    private  void getSlideByHttp(Callback<NewsSlideBean> cb){
        RestHelper.getService(API.NewsService.class).getNewsSlide(sort, cb);
    }

    private void init(){

        NewsBean newsBean = getListCache();

        if(newsBean!=null){
            initData(newsBean);
        }

        getListByHttp(1, new Callback<NewsBean>() {
            @Override
            public void success(NewsBean newsBean, Response response) {
                mLog.v(response.getUrl());
                initData(newsBean);
                putListCache(newsBean);
            }

            @Override
            public void failure(RetrofitError error) {
                mLog.v(error.getUrl());
            }
        });

         NewsSlideBean newsSlideBean = getSlideCache();

        if(newsSlideBean!=null){
            initSlideData(newsSlideBean);
        }

        getSlideByHttp(new Callback<NewsSlideBean>() {
            @Override
            public void success(NewsSlideBean newsSlideBean, Response response) {
                mLog.v(response.getUrl());
                initSlideData(newsSlideBean);
                putSlideCache(newsSlideBean);
            }

            @Override
            public void failure(RetrofitError error) {
                mLog.v(error.getUrl());
            }
        });

    }

    private void initData(NewsBean newsBean){
        mItemList.clear();
        addData(newsBean);
    }

    private void addData(NewsBean newsBean){
        mItemList.addAll(newsBean.getArticlelist());
        mListAdapter.notifyDataSetChanged();
    }

    private void initSlideData(NewsSlideBean newsSlideBean){
        NewsSlideBean.Articlelist data = newsSlideBean.getArticlelist().get(0);

        if(mTopView == null){
            mTopView = inflater.inflate(R.layout.activity_news_top,null,false);
            mListView.addHeaderView(mTopView);
        }

        if(data.getId().equals(mTopView.getTag())){
            return ;
        }

        ImageView imageView = (ImageView) mTopView.findViewById(R.id.news_top_img);
        imageWorker.displayImageView(imageView, data.getTitlepic());
        ((TextView) mTopView.findViewById(R.id.news_list_title)).setText(data.getTitle());
        ((TextView) mTopView.findViewById(R.id.news_list_brief)).setText(data.getTitle());
        mTopView.setTag(data.getId());
    }

	/**
	 * listview的接口
	 * */
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
        mLog.v("load more");

        if(isLoading){
            return;
        }

        isLoading = true;
        getListByHttp(mListAdapter.getCount()/10+1, new Callback<NewsBean>() {
            @Override
            public void success(NewsBean newsBean, Response response) {
                addData(newsBean);
                isLoading = false;
                mListView.stopLoadMore();
            }

            @Override
            public void failure(RetrofitError error) {
                isLoading = false;
                mListView.stopLoadMore();
            }
        });
	}

    class XListAdapter extends BaseAdapter {
        ArrayList<Article> itemList;

        public XListAdapter(ArrayList<Article> itemList) {
            // TODO Auto-generated constructor stub
            this.itemList = itemList;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Article map = itemList.get(position);

            if (convertView == null) {
                ViewHolder viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.activity_news_list_item, null,false);
                viewHolder.tvTitle = (TextView) convertView
                        .findViewById(R.id.news_list_title);
                viewHolder.tvBreif = (TextView) convertView
                        .findViewById(R.id.news_list_brief);
                viewHolder.imageView = (ImageView) convertView
                        .findViewById(R.id.news_list_img);
                convertView.setTag(R.id.tag_first, viewHolder);
            }

            ViewHolder viewHolder = (ViewHolder) convertView.getTag(R.id.tag_first);

            viewHolder.tvTitle.setText(map.getTitle());
            viewHolder.tvBreif.setText(map.getBrief());
            if (TextUtils.isEmpty(map.getTitlepic())) {
                viewHolder.imageView.setVisibility(View.GONE);
            } else {
                viewHolder.imageView.setVisibility(View.VISIBLE);
                imageWorker.displayImageView(viewHolder.imageView, map.getTitlepic());
            }

            convertView.setTag(map.getId());
            return convertView;
        }
    }

    private class ViewHolder {
        TextView tvTitle;
        TextView tvBreif;
        ImageView imageView;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        String vId = (String) view.getTag();

        if (TextUtils.isEmpty(vId)) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("url", String.format(API.News.NEWS_CONTENT, Integer.parseInt(vId)));
        bundle.putString("title", "新闻");
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(this.getActivity(), NewsWebViewActivity.class);
        startActivity(intent);
    }
}
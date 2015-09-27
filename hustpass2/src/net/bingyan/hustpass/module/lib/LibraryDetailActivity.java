package net.bingyan.hustpass.module.lib;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import net.bingyan.hustpass.R;
import net.bingyan.hustpass.API;
import net.bingyan.hustpass.http.HttpUtil;
import net.bingyan.hustpass.http.ImageWorkIon;
import net.bingyan.hustpass.http.ImageWorker;
import net.bingyan.hustpass.ui.base.BaseActivity;

public class LibraryDetailActivity extends BaseActivity {
	String TAG = "LibraryDetailActivity";
	String id;
	ListView mListView;
	LibraryDetailAdapter mAdapter;
	LibraryDetailTask libraryDetailTask;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		id = getIntent().getStringExtra("id");
		libraryDetailTask = (LibraryDetailTask) new LibraryDetailTask()
				.execute();
	}
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TAG); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}

	public void initView() {
		mListView = (ListView) findViewById(R.id.library_detail_listview);
	}

	private String getName(String t){
		if (t != null) {
			String pinyin ="";
			String[] t2 = t.split(" / ");  // name auther ...				
			String strPattern = "[a-zA-z\\ \\=\\:0-9\\/\\,]*$";  
		    Pattern p = Pattern.compile(strPattern);  
		    Matcher mather = p.matcher(t2[0]);
		    if(mather.find()){
		    	pinyin = mather.group();
		    	t2[0]=t2[0].replace(pinyin, "");
		    }
		   return t2[0];
		}else {
			return "暂无数据";
		}
	}
	
	public void setView(BookDetailBean bookDetailBean) {
		setContentView(R.layout.activity_library_detail);
		initView();
		((TextView) findViewById(R.id.library_detail_bookname))
				.setText(getName(bookDetailBean.getTitle()));
		((TextView) findViewById(R.id.library_detail_bookpingying)).setText("");
		((TextView) findViewById(R.id.library_detail_editor))
				.setText(bookDetailBean.getEditor()[0]);
		((TextView) findViewById(R.id.library_detail_publisher))
				.setText(bookDetailBean.getPublisher());
		((TextView) findViewById(R.id.library_detail_isbn))
				.setText(bookDetailBean.getIsbn()[0]);

//		ImageCacheLoader imageCacheLoader = new ImageCacheLoader(this);
//		imageCacheLoader.displayImage(
//				(ImageView) findViewById(R.id.library_detail_bookimg),
//				bookDetailBean.getCoverimg());
        ImageWorker imageWorker = new ImageWorkIon(this);
        imageWorker.displayImageView(
                (ImageView) findViewById(R.id.library_detail_bookimg),
                bookDetailBean.getCoverimg());


		if (bookDetailBean.getDetail() != null) {
			if (mAdapter == null) {
				mAdapter = new LibraryDetailAdapter(bookDetailBean.getDetail());
				mListView.setAdapter(mAdapter);
			} else {
				mAdapter.notifyDataSetChanged();
			}
		} else {
			TextView textView = new TextView(this);
			textView.setText("暂无数据");
			mListView.setEmptyView(textView);
		}
	}

	View mEmptyView;

	private void setEmptyView(boolean isRefreshing) {
		setContentView(R.layout.listview_emptyview2);
		if (mEmptyView == null) {
			mEmptyView = findViewById(R.id.empty_view);
		}
		if (isRefreshing) {
			mEmptyView.findViewById(R.id.progressBar1).setVisibility(
					View.VISIBLE);
			mEmptyView.findViewById(R.id.empty_view_text)
					.setVisibility(View.GONE);
			mEmptyView.findViewById(R.id.empty_view_img).setVisibility(
					View.GONE);
		} else {
			((TextView) mEmptyView.findViewById(R.id.empty_view_text))
					.setText("network error,please check your network");
			mEmptyView.findViewById(R.id.empty_view_text)
					.setVisibility(View.VISIBLE);
			mEmptyView.findViewById(R.id.empty_view_img).setVisibility(
					View.VISIBLE);
			mEmptyView.findViewById(R.id.progressBar1).setVisibility(View.GONE);
		}
	}

	class LibraryDetailAdapter extends BaseAdapter {
		List<BookDetailBean.BookShelf> bookShelfs;
		int count = 0;

		public LibraryDetailAdapter(List<BookDetailBean.BookShelf> bookShelfs) {
			// TODO Auto-generated constructor stub
			this.bookShelfs = bookShelfs;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return bookShelfs.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return bookShelfs.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			if (convertView == null) {
				convertView = View.inflate(LibraryDetailActivity.this,
						R.layout.activity_library_detail_list_item, null);
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.text1 = (TextView) convertView
						.findViewById(R.id.textview1);
				viewHolder.text2 = (TextView) convertView
						.findViewById(R.id.textview2);
				viewHolder.text3 = (TextView) convertView
						.findViewById(R.id.textview3);
				convertView.setTag(viewHolder);
			}
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.text1.setText(bookShelfs.get(position).getBook_site());
			viewHolder.text2.setText(bookShelfs.get(position).getBook_num());
			viewHolder.text3.setText(bookShelfs.get(position).getBook_status());
			return convertView;
		}
	}

	static class ViewHolder {
		TextView text1;
		TextView text2;
		TextView text3;
	}

	class LibraryDetailTask extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			setEmptyView(true);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String result;
			result = HttpUtil.getString(String.format(API.Library.DETAIL_URL,
					id));
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			Gson gson = new Gson();
			try {
				BookDetailBean bookDetailBean = gson.fromJson(result,
						BookDetailBean.class);
				setView(bookDetailBean);
			} catch (Exception e) {
				// TODO: handle exception
				setEmptyView(false);
				e.printStackTrace();
			}
		}

	}
}

package net.bingyan.hustpass.module.lib;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.API;
import net.bingyan.hustpass.http.HttpUtil;
import net.bingyan.hustpass.ui.base.BaseActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

public class LibraryListActivity extends BaseActivity {
	String TAG = "LibraryListActivity";
	ListView mListView;
	View mEmptyView;
	LibraryListAdapter mAdapter;
	LibraryListTask mBookListTask;

	String key;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		key = getIntent().getStringExtra("key");
		setContentView(R.layout.listview);

		mListView = (ListView) findViewById(R.id.listview);
		mBookListTask = (LibraryListTask) new LibraryListTask().execute();
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(LibraryListActivity.this,
						LibraryDetailActivity.class);
				intent.putExtra("id", mAdapter.books.get(position).getId());
				intent.putExtra("name", ((TextView)view.findViewById(R.id.title)).getText().toString());
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		super.onClick(arg0);
		if (arg0.getId() == R.id.empty_view_btn_retry) {
			mBookListTask = (LibraryListTask) new LibraryListTask().execute();
		}
	}

	private void setEmptyView(boolean isRefreshing,String s) {
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
					.setText(s);
			mEmptyView.findViewById(R.id.empty_view_text)
					.setVisibility(View.VISIBLE);
			mEmptyView.findViewById(R.id.empty_view_img).setVisibility(
					View.VISIBLE);
			mEmptyView.findViewById(R.id.progressBar1).setVisibility(View.GONE);
		}
		mListView.setEmptyView(mEmptyView);
	}
	private void setEmptyView(boolean isRefreshing) {
		setEmptyView(isRefreshing,getString(R.string.net_error));
	}

	private void setListView(List<BookListBean.Book> books) {
		if(books==null||books.size()==0){
			setEmptyView(false,"图书馆暂无此类书籍");
			return;
		}
		if (mAdapter == null) {
			mAdapter = new LibraryListAdapter(books);
			mListView.setAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}

	class LibraryListAdapter extends BaseAdapter {
		List<BookListBean.Book> books;

		public LibraryListAdapter(List<BookListBean.Book> books) {
			// TODO Auto-generated constructor stub
			this.books = books;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return books.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return books.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			String t = books.get(position).getTitle();
			String pinyin="";
			String name="暂无数据";
			String auther="暂无数据";
			if (t != null) {
				String[] t2 = t.split(" / ");  // name auther ...				
				String strPattern = "[a-zA-z\\ \\=\\:0-9\\/\\,]*$";  
			    Pattern p = Pattern.compile(strPattern);  
			    Matcher mather = p.matcher(t2[0]);
			    if(mather.find()){
			    	pinyin = mather.group();
			    	t2[0]=t2[0].replace(pinyin, "");
			    }
			    name = t2[0];
			    auther = t2[t2.length-1];
			}
			if (convertView == null) {
				convertView = View.inflate(LibraryListActivity.this,
						R.layout.activity_foodshop_list_item, null);
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.title = (TextView) convertView
						.findViewById(R.id.title);
				viewHolder.pinyin = (TextView) convertView
						.findViewById(R.id.foodshop_address);
				viewHolder.editor = (TextView) convertView
						.findViewById(R.id.foodshop_time);
				convertView.setTag(viewHolder);
			}
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			
			viewHolder.title.setText(name);
			viewHolder.pinyin.setText(pinyin);
			viewHolder.editor.setText(auther);
			
			if (position % 2 == 0) {
				viewHolder.title.setTextColor(getResources().getColor(
						R.color.foodshop_item_color_yellow));
				viewHolder.editor
						.setBackgroundResource(R.drawable.foodshop_item_bar_yellow);
			} else {
				viewHolder.title.setTextColor(getResources().getColor(
						R.color.foodshop_item_color_blue));
				viewHolder.editor
						.setBackgroundResource(R.drawable.foodshop_item_bar_blue);
			}
			return convertView;
		}
	}

	static class ViewHolder {
		TextView title;
		TextView pinyin;
		TextView editor;
	}

	class LibraryListTask extends AsyncTask<Void, Void, String> {

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
			Log.i(TAG, key);
			String[] keys = key.split(" ");				
			result = HttpUtil.getString(String.format(API.Library.SEARCH_URL,
					"keyword", Uri.encode(keys[0]), 0));
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			Gson gson = new Gson();
			try {
				BookListBean bookListBean = gson.fromJson(result,
						BookListBean.class);
				setListView(bookListBean.getResult());
			} catch (Exception e) {
				// TODO: handle exception
				setEmptyView(false);
				e.printStackTrace();
			}
		}

	}
}

package net.bingyan.hustpass.module.compter;

import java.util.ArrayList;
import java.util.List;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.db.CacheDaoHelper;
import net.bingyan.hustpass.http.RestHelper;
import net.bingyan.hustpass.API;
import net.bingyan.hustpass.ui.base.BaseActivity;
import net.bingyan.hustpass.util.Util;
import net.bingyan.hustpass.widget.CircularProgressBar;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ComputerRoomActivity extends BaseActivity {
	String TAG = "ComputerRoomActivity";
	ListView mListView;
	ComputerAdapter mAdapter;
//	ComputerTask computerTask;
	int mSlectedId = R.id.computer_title_dong;
	List<List<Object>> slectedRoomList = new ArrayList<List<Object>>();
	List<List<Object>> roomList = new ArrayList<List<Object>>();

	TextView mTabDong, mTabZhu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer_room);
        init();
    }
    private ComputerBean getComputerCache() {
        // 12 小时
        return new CacheDaoHelper(getApplication()).getCache(ComputerBean.class);
    }

    private void putComputerCache(ComputerBean computerBean) {
        new CacheDaoHelper(getApplication()).putCache(computerBean);
    }

    private void getComputerHttp(Callback<ComputerBean> cb) {
        RestHelper.getService(API.ComputerService.HOST,API.ComputerService.class).getComputer(cb);
    }

    private void init(){
        ComputerBean computerBean = getComputerCache();
        if(computerBean!=null){
            initContent(computerBean);
        }

        showProgressBar();
        getComputerHttp(new Callback<ComputerBean>() {
            @Override
            public void success(ComputerBean computerBean, Response response) {
                stopProgressBar();
                initContent(computerBean);
                putComputerCache(computerBean);
            }

            @Override
            public void failure(RetrofitError error) {
                Util.toast("网络出错"+error.getMessage());
                stopProgressBar();
            }
        });
    }

    private void initContent(ComputerBean computerBean){
        mListView = (ListView) findViewById(R.id.computer_listView);
        mTabDong = (TextView) findViewById(R.id.computer_title_dong);
        mTabDong.setOnClickListener(this);
        mTabZhu = (TextView) findViewById(R.id.computer_title_zhu);
        mTabZhu.setOnClickListener(this);

        setListView(computerBean.getResult());
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == mSlectedId)
			return;
		if (arg0.getId() == R.id.computer_title_dong
				|| arg0.getId() == R.id.computer_title_zhu) {
			mSlectedId = arg0.getId();
			setSlectedRoomList();
			return;
		}
		super.onClick(arg0);
	}

	private void setSlectedRoomList() {
		slectedRoomList.clear();
		if (mSlectedId == R.id.computer_title_dong) {
			/*
			 * slectedRoomList.add(roomList.get(0));
			 * slectedRoomList.add(roomList.get(1));
			 */
			for (List<Object> room : roomList) {
				if (Float.parseFloat(room.get(0).toString()) == 1) {
					slectedRoomList.add(room);
				}
			}

			mTabDong.setBackgroundColor(getResources().getColor(R.color.white));
			mTabZhu.setBackgroundResource(R.drawable.classroom_top_btn_bg_r_l);
			mTabZhu.setTextColor(getResources().getColor(R.color.white));
			mTabDong.setTextColor(getResources().getColor(
					R.color.computer_tab_text));
		} else {
			/*
			 * slectedRoomList.add(roomList.get(2));
			 * slectedRoomList.add(roomList.get(3));
			 */
			for (List<Object> room : roomList) {
				if (Float.parseFloat(room.get(0).toString()) == -1) {
					slectedRoomList.add(room);
				}
			}
			mTabZhu.setBackgroundColor(getResources().getColor(R.color.white));
			mTabDong.setBackgroundResource(R.drawable.classroom_top_btn_bg_r_r);
			mTabDong.setTextColor(getResources().getColor(R.color.white));
			mTabZhu.setTextColor(getResources().getColor(
					R.color.computer_tab_text));
		}
		if (mAdapter == null) {
			mAdapter = new ComputerAdapter(slectedRoomList);
			mListView.setAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}

	private void setListView(List<List<Object>> rooms) {
		if(rooms==null||rooms.size()==0){
//			setEmptyView(false,getString(R.string.api_error));
            mListView.setEmptyView(findViewById(R.id.listview_empty));
			return;
		}
		try {
			Float.valueOf(rooms.get(0).get(2).toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
//			setEmptyView(false,getString(R.string.api_error));
            mListView.setEmptyView(findViewById(R.id.listview_empty));
			return;
		}
		
		roomList = rooms;
		setSlectedRoomList();
	}

//	View mEmptyView;
//
//	private void setEmptyView(boolean isRefreshing,String info) {
//		if (mEmptyView == null) {
//			mEmptyView = findViewById(R.id.empty_view);
//		}
//		if (isRefreshing) {
//			mEmptyView.findViewById(R.id.progressBar1).setVisibility(
//					View.VISIBLE);
//			mEmptyView.findViewById(R.id.empty_view_text)
//					.setVisibility(View.GONE);
//			mEmptyView.findViewById(R.id.empty_view_img).setVisibility(
//					View.GONE);
//		} else {
//			((TextView) mEmptyView.findViewById(R.id.empty_view_text))
//					.setText(info);
//			mEmptyView.findViewById(R.id.empty_view_text)
//					.setVisibility(View.VISIBLE);
//			mEmptyView.findViewById(R.id.empty_view_img).setVisibility(
//					View.VISIBLE);
//			mEmptyView.findViewById(R.id.progressBar1).setVisibility(View.GONE);
//		}
//		mListView.setEmptyView(mEmptyView);
//	}
//	private void setEmptyView(boolean isRefreshing) {
//		if(isRefreshing==false){
//			if(isAdded())
//			setEmptyView(isRefreshing,getString(R.string.net_error));
//		}
//		else {
//			if(isAdded())
//				setEmptyView(isRefreshing,getString(R.string.net_error));
//			else
//				setEmptyView(isRefreshing,"");
//		}
//	}

	class ComputerAdapter extends BaseAdapter {
		List<List<Object>> rooms;

		public ComputerAdapter(List<List<Object>> rooms) {
			// TODO Auto-generated constructor stub
			this.rooms = rooms;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return rooms.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return rooms.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			List<Object> room = rooms.get(position);
			convertView = View.inflate(ComputerRoomActivity.this,
					R.layout.activity_computer_list_item, null);
			CircularProgressBar cBar = (CircularProgressBar) convertView
					.findViewById(R.id.computer_circular);
			String name = room.get(1).toString();
			if (mSlectedId == R.id.computer_title_dong) {
				cBar.setName(name.substring(3, name.length()));
			} else {
				cBar.setName(name.substring(3, name.length()) + "室");
			}

			try {
				//java.lang.NumberFormatException: Invalid float: "可能已关闭"
				cBar.setData(Float.valueOf(room.get(2).toString()),
						Float.valueOf(room.get(3).toString()));
				cBar.commit();
				((TextView) convertView.findViewById(R.id.computer_used))
				.setText("占用 "
						+ (((Double) room.get(2)).intValue() - ((Double) room
								.get(3)).intValue()) + "台");
				((TextView) convertView.findViewById(R.id.computer_left))
				.setText("剩余 " + ((Double) room.get(3)).intValue() + "台");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return convertView;
		}

	}

//	class ComputerTask extends AsyncTask<Void, Void, String> {
//
//		@Override
//		protected void onPreExecute() {
//			// TODO Auto-generated method stub
//			setEmptyView(true);
//			super.onPreExecute();
//		}
//
//		@Override
//		protected String doInBackground(Void... params) {
//			// TODO Auto-generated method stub
//			String result;
//			result = HttpUtil.getString(API.ComputerRoom.ROOMUSAGE_URL);
//			return result;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			// TODO Auto-generated method stub
//			Gson gson = new Gson();
//			try {
//				ComputerBean computerBean = gson.fromJson(result,
//						ComputerBean.class);
//				if (computerBean.getStatus().equals("fail")) {
//					setEmptyView(false,getString(R.string.api_error));
//				} else {
//					setListView(computerBean.getResult());
//				}
//			} catch (Exception e) {
//				// TODO: handle exception
//				setEmptyView(false);
//				e.printStackTrace();
//			}
//		}
//
//	}
}

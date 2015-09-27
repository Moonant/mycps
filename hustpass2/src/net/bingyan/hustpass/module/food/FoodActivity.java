package net.bingyan.hustpass.module.food;

import java.util.List;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.db.CacheDaoHelper;
import net.bingyan.hustpass.http.RestHelper;
import net.bingyan.hustpass.API;
import net.bingyan.hustpass.ui.base.BaseActivity;
import net.bingyan.hustpass.util.AppLog;
import net.bingyan.hustpass.util.Util;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FoodActivity extends BaseActivity {
	AppLog mLog = new AppLog(getClass());
	ListView mListView;
	FoodShopListAdapter mAdapter;

    private FoodShopListBean getCache() {
        return new CacheDaoHelper(getApplication()).getCache(FoodShopListBean.class);
    }

    private void putCache(FoodShopListBean foodShopListBean) {
        new CacheDaoHelper(getApplication()).putCache(foodShopListBean);
    }

    private void getHttp(Callback<FoodShopListBean> cb) {
        RestHelper.getService(API.FoodService.HOST,API.FoodService.class).getShopList(cb);
    }

	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
        setContentView(R.layout.listview);
        init();
	}

    private void init(){
        FoodShopListBean foodShopListBean = getCache();

        if(foodShopListBean!=null){
            initContent(foodShopListBean);
        }else{
            showProgressBar();
            getHttp(new Callback<FoodShopListBean>() {
                @Override
                public void success(FoodShopListBean foodShopListBean, Response response) {
                    stopProgressBar();
                    mLog.v("success"+response.getUrl());
                    initContent(foodShopListBean);
                    putCache(foodShopListBean);
                }

                @Override
                public void failure(RetrofitError error) {
                    stopProgressBar();
                    mLog.v(error.getUrl());
                    Util.toast(R.string.net_error_toast+error.getMessage());
                }
            });
        }

    }

    private void initContent(FoodShopListBean foodShopListBean){
        mListView = (ListView) findViewById(R.id.listview);
        mAdapter = new FoodShopListAdapter(foodShopListBean.getData());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mAdapter);
    }

	class FoodShopListAdapter extends BaseAdapter implements OnItemClickListener{
		List<FoodShopListBean.FoodShop> foodShops;

		public FoodShopListAdapter(List<FoodShopListBean.FoodShop> foodShops) {
			// TODO Auto-generated constructor stub
			this.foodShops = foodShops;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return foodShops.size();
		}

		@Override
		public FoodShopListBean.FoodShop getItem(int position) {
			// TODO Auto-generated method stub
			return foodShops.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(FoodActivity.this, FoodShopDeatailActivity.class);
            intent.putExtra("id", getItem(position).getShop_id());
            startActivity(intent);
        }

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = View.inflate(FoodActivity.this,
						R.layout.activity_foodshop_list_item, null);
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.title = (TextView) convertView
						.findViewById(R.id.title);
				viewHolder.address = (TextView) convertView
						.findViewById(R.id.foodshop_address);
				viewHolder.time = (TextView) convertView
						.findViewById(R.id.foodshop_time);
				convertView.setTag(viewHolder);
			}
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.title.setText(foodShops.get(position).getShop_name());
			viewHolder.address.setText(foodShops.get(position)
					.getShop_address());
			viewHolder.time.setText("营业时间   "
					+ foodShops.get(position).getShop_time());
			if (position % 2 == 1) {
				viewHolder.title.setTextColor(getResources().getColor(
						R.color.foodshop_item_color_yellow));
				viewHolder.time
						.setBackgroundResource(R.drawable.foodshop_item_bar_yellow);
			} else {
				viewHolder.title.setTextColor(getResources().getColor(
						R.color.foodshop_item_color_blue));
				viewHolder.time
						.setBackgroundResource(R.drawable.foodshop_item_bar_blue);
			}
			return convertView;
		}
	}

	static class ViewHolder {
		TextView title;
		TextView address;
		TextView time;
	}
}

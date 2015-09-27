package net.bingyan.hustpass.module.food;

import java.util.List;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.API;
import net.bingyan.hustpass.db.CacheDaoHelper;
import net.bingyan.hustpass.http.RestHelper;
import net.bingyan.hustpass.ui.base.BaseActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FoodShopDeatailActivity extends BaseActivity {
	int id = 0;
	String shopTel;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		id = getIntent().getIntExtra("id", 1);
		setContentView(R.layout.activity_foodshop_detail);

		init();
	}

    private FoodShopBean getCache(){
        return  CacheDaoHelper.getInstance().getCache(FoodShopBean.class,String.valueOf(id));
    }

    private void putCache(FoodShopBean foodShopBean){
        CacheDaoHelper.getInstance().putCache(foodShopBean, String.valueOf(id));
    }

    private void getByHttp(Callback<FoodShopBean> cb){
        RestHelper.getService(API.FoodService.HOST,API.FoodService.class).getShop(id,cb);
    }

	public void init() {
		FoodShopBean foodShopBean = getCache();

        if(foodShopBean!=null){
            initData(foodShopBean);
        }else {
            getByHttp(new Callback<FoodShopBean>() {
                @Override
                public void success(FoodShopBean foodShopBean, Response response) {
                    initData(foodShopBean);
                    putCache(foodShopBean);
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }

	}

    private void initData(FoodShopBean foodShopBean){
        shopTel = foodShopBean.getShop_tel();
        setView(foodShopBean);
    }



	private void setView(final FoodShopBean foodShopBean) {
		((TextView) findViewById(R.id.foodshop_name)).setText(foodShopBean.getShop_name());
		((TextView) findViewById(R.id.foodshop_address)).setText(foodShopBean.getShop_address());
		((TextView) findViewById(R.id.foodshop_phone_num)).setText("电话： "+ foodShopBean.getShop_tel());

		findViewById(R.id.foodshop_call_btn).setOnClickListener(this);

        ListView mListView = (ListView) findViewById(R.id.foodshop_detail_listview);
        FoodDetailListAdapter mAdapter = new FoodDetailListAdapter(foodShopBean.getData());
		mListView.setAdapter(mAdapter);
	}

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        super.onClick(arg0);
        if (arg0.getId() == R.id.foodshop_call_btn) {
            Uri uri = Uri.parse("tel:" + shopTel); // 拨打电话号码的URI格式
            Intent it = new Intent(); // 实例化Intent
            it.setAction(Intent.ACTION_CALL); // 指定Action
            it.setData(uri); // 设置数据
            startActivity(it);// 启动Acitivity
        }
    }

	class FoodDetailListAdapter extends BaseAdapter {
		List<FoodShopBean.Food> foods;

		public FoodDetailListAdapter(List<FoodShopBean.Food> foods) {
			// TODO Auto-generated constructor stub
			this.foods = foods;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return foods.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return foods.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			if (convertView == null) {
				convertView = View.inflate(FoodShopDeatailActivity.this,
						R.layout.activity_foodshop_detail_list_item, null);
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.title = (TextView) convertView
						.findViewById(R.id.title);
				viewHolder.price = (TextView) convertView
						.findViewById(R.id.food_price);
				convertView.setTag(viewHolder);
			}
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.title.setText(foods.get(position).getFood_name());
			viewHolder.price.setText("" + foods.get(position).getFood_price());
			return convertView;
		}
	}

	static class ViewHolder {
		TextView title;
		TextView price;
	}


}

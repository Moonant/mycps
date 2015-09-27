package net.bingyan.hustpass.module.food;

import java.util.List;

public class FoodShopBean {
	String shop_address;
	String shop_tel;
	int success;
	String shop_name;
	String shop_time;
	List<Food> data;

	public String getShop_address() {
		return shop_address;
	}

	public void setShop_address(String shop_address) {
		this.shop_address = shop_address;
	}

	public String getShop_tel() {
		return shop_tel;
	}

	public void setShop_tel(String shop_tel) {
		this.shop_tel = shop_tel;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getShop_time() {
		return shop_time;
	}

	public void setShop_time(String shop_time) {
		this.shop_time = shop_time;
	}

	public List<Food> getData() {
		return data;
	}

	public void setData(List<Food> data) {
		this.data = data;
	}

	public class Food {
		float food_price;
		int food_id;
		String food_name;
		public float getFood_price() {
			return food_price;
		}
		public void setFood_price(float food_price) {
			this.food_price = food_price;
		}
		public int getFood_id() {
			return food_id;
		}
		public void setFood_id(int food_id) {
			this.food_id = food_id;
		}
		public String getFood_name() {
			return food_name;
		}
		public void setFood_name(String food_name) {
			this.food_name = food_name;
		}
	}
}

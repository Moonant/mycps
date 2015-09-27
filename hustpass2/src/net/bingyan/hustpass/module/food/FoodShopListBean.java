package net.bingyan.hustpass.module.food;

import java.util.List;

public class FoodShopListBean {
	List<FoodShop> data;
	int success;

	public List<FoodShop> getData() {
		return data;
	}

	public void setData(List<FoodShop> data) {
		this.data = data;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public class FoodShop {
		String shop_address;

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

		public int getShop_id() {
			return shop_id;
		}

		public void setShop_id(int shop_id) {
			this.shop_id = shop_id;
		}

		public String getShop_time() {
			return shop_time;
		}

		public void setShop_time(String shop_time) {
			this.shop_time = shop_time;
		}

		public String getShop_name() {
			return shop_name;
		}

		public void setShop_name(String shop_name) {
			this.shop_name = shop_name;
		}

		String shop_tel;
		int shop_id;
		String shop_time;
		String shop_name;
	}
}

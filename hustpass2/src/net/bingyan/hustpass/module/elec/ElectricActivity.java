package net.bingyan.hustpass.module.elec;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.helper.AccountManager;
import net.bingyan.hustpass.ui.base.BaseActivity;
import net.bingyan.hustpass.util.Util;

public class ElectricActivity extends BaseActivity {

	TextView slectArea;
	EditText building;
	EditText room;
	View search;
	int[] areaId = { R.string.area_1, R.string.area_2, R.string.area_3,
			R.string.area_4 };

	PopupWindow popupWindow;
    AccountManager.ElecAccountManager elecAccountManager;

	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
        setContentView(R.layout.activity_elec);
        init();
	}

    @Override
    protected void onResume() {
        super.onResume();
//        Util.toast(R.string.account_alert_cannot_save_msg);
    }

    private void  init(){
        elecAccountManager = AccountManager.getElecManger();

        initView();
    }

	private void initView() {
		slectArea = (TextView) findViewById(R.id.elec_slec_xiaoqu);
		building = (EditText) findViewById(R.id.elec_text_building);
		room = (EditText) findViewById(R.id.elec_text_room);
		search = findViewById(R.id.elec_btn_search);

//        if(elecAccountManager.isLogin()){
//            slectArea.setText(elecAccountManager.getArea());
//            building.setText(elecAccountManager.getBuilding());
//            room.setText(elecAccountManager.getRoom());
//        }

		slectArea.setOnClickListener(this);
		search.setOnClickListener(this);

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.activity_elec_popwindow, null,
				false);
		view.findViewById(R.id.elec_area_1).setOnClickListener(this);
		view.findViewById(R.id.elec_area_2).setOnClickListener(this);
		view.findViewById(R.id.elec_area_3).setOnClickListener(this);
		view.findViewById(R.id.elec_area_4).setOnClickListener(this);

		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT){
            @Override
            public void showAsDropDown(View anchor) {
                super.showAsDropDown(anchor);
                slectArea.setBackgroundResource(R.drawable.elec_area_text_bg);
            }

            @Override
            public void dismiss() {
                super.dismiss();
                slectArea.setBackgroundResource(R.drawable.app_edit_shape_grey);
            }
        };
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.color.transparent));
		popupWindow.setFocusable(true);
	}

	private void startSearch() {
		String area = slectArea.getText().toString().trim();
        String buildingNum = building.getText().toString().trim();
		String roomNum = room.getText().toString().trim();
		if (area.equals("校区") || TextUtils.isEmpty(buildingNum) || TextUtils.isEmpty(roomNum)) {
            Util.toast("信息不完整");
			return;
		}
		Bundle bundle = new Bundle();
		bundle.putString("area", area);
		bundle.putInt("building", Integer.valueOf(buildingNum));
		bundle.putString("room", roomNum);

        elecAccountManager.updateAccount(area,Integer.valueOf(buildingNum),roomNum);

        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(this,ElecDetailActivity.class);
        startActivity(intent);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(arg0.getId()!=R.id.elec_slec_xiaoqu)
			slectArea.setSelected(false);
		switch (arg0.getId()) {
		case R.id.elec_slec_xiaoqu:
			popupWindow.showAsDropDown(slectArea);
			arg0.setSelected(true);
			break;
		case R.id.elec_btn_search:
			startSearch();
			break;
		case R.id.elec_area_1:
			slectArea.setText(getString(areaId[0]));
			popupWindow.dismiss();
			break;
		case R.id.elec_area_2:
			slectArea.setText(getString(areaId[1]));
			popupWindow.dismiss();
			break;
		case R.id.elec_area_3:
			slectArea.setText(getString(areaId[2]));
			popupWindow.dismiss();
			break;
		case R.id.elec_area_4:
			slectArea.setText(getString(areaId[3]));
			popupWindow.dismiss();
			break;
		default:
			break;
		}
		super.onClick(arg0);
	}

}

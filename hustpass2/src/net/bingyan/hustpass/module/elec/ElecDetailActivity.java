package net.bingyan.hustpass.module.elec;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import net.bingyan.hustpass.API;
import net.bingyan.hustpass.R;
import net.bingyan.hustpass.db.CacheDaoHelper;
import net.bingyan.hustpass.http.RestCallback;
import net.bingyan.hustpass.http.RestHelper;
import net.bingyan.hustpass.provider.ElecWidgetProvider;
import net.bingyan.hustpass.ui.base.BaseActivity;
import net.bingyan.hustpass.util.Pref;
import net.bingyan.hustpass.util.Util;
import net.bingyan.hustpass.widget.LineGraphView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ElecDetailActivity extends BaseActivity  implements LineGraphView.OnLineGraphBarChangeListener{
//    AppLog mLog = new AppLog(getClass());

    TextView elecGraphData;

    String area;
    int building;
    String room;


	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

        Bundle bundle = getIntent().getExtras();
        area = bundle.getString("area");
        building =  bundle.getInt("building");
        room = bundle.getString("room");

        setContentView(R.layout.activity_elec_detail);
        init();
	}

    private CacheDaoHelper.CacheInfo<ElecBean> getCache() {
        return new CacheDaoHelper(getApplication()).getCacheInfo(ElecBean.class, area + building + room);
    }

    private void putCache(ElecBean elecBean) {
        new CacheDaoHelper(getApplication()).putCache(elecBean,area+building+room);
    }

    private void getHttp(Callback<ElecBean> cb) {
        RestHelper.getService(API.ElecService.class).getElec(area, building, room,cb);
    }

    private void init(){
        CacheDaoHelper.CacheInfo<ElecBean> elecBean = getCache();
        if(elecBean!=null){
            initContent(elecBean.getCache());
        }

        if(elecBean!=null&&elecBean.getTime()<CacheDaoHelper.CACHE_TIME_OUT){
            return;
        }

        showProgressBar(); // show
        getHttp(new RestCallback<ElecBean>() {
            @Override
            public void finalexe() {
                stopProgressBar(); // stop
            }

            @Override
            public void success(ElecBean elecBean, Response response) {
                finalexe();

                if (elecBean.getState().equals("success")) {
                    mLog.v(response.getUrl());
                    initContent(elecBean);
                    putCache(elecBean);
                }else {
                    Util.toast("无法找到宿舍，请检查输入信息");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLog.v(error.getUrl());
                Util.toast("网络出错"+error.getMessage());

                finalexe();
            }
        });
    }

    private void initContent(ElecBean elecBean){

        if (elecBean.getState().equals("success")) {

            // 剩余电量 更新
			((TextView) findViewById(R.id.elec_detail_remain)).setText(elecBean.getRemain());
			float remain = Float.parseFloat(elecBean.getRemain());
			Pref.getPref().edit().putFloat(Pref.ELEC_REMAIN, remain).commit();

            // 提示语
            TextView infoTv = (TextView) findViewById(R.id.elec_detail_remain_info);
			if (remain > Pref.getPref().getInt(Pref.INT_ELEC_ALARM, 10)) {
                infoTv.setText(getString(R.string.elec_enough));
			} else {
                infoTv.setText(getString(R.string.elec_not_enough));
			}

            //
			setGraphPoints(elecBean.getHistory());

            // 广播 小组建
			Intent widgetIntent = new Intent(ElecWidgetProvider.UPDATE_ACTION);
			sendBroadcast(widgetIntent);
		} else {
            Util.toast("无法找到宿舍，请检查输入信息");
		}

    }


    public void onBarChanged(float x, float y){
        if (elecGraphData == null) {
            elecGraphData = (TextView) findViewById(R.id.elec_bar_data);
        }
        String date = Util.formatDateNoGMT((int)x * 1000L , "yyyy-MM-dd");
        elecGraphData.setText(date + "   " + String.format("%.1f",y) + "kW‧h");
    }

    public void setGraphPoints(String[][] datas) {
        LineGraphView.Line line = new LineGraphView.Line();
        float x, y;
        float startY = 0, endY = 0;

        for (int i = datas.length - 1; i >= 0; i--) {
            x = Util.dateParse(datas[i][1], "yyyy-MM-dd hh:mm:ss").getTime() / 1000;
            mLog.v(""+x);
            mLog.v(""+(int)x);
            y = Float.parseFloat(datas[i][0]);

            if (i == datas.length - 1){
                startY = y;
            }

            if (i == 0){
                endY = y;
            }

            line.addPoint(new LineGraphView.LinePoint(x, y));
        }

        LineGraphView lineGraphView = (LineGraphView)findViewById(R.id.elec_graph);
        lineGraphView.setLine(line);
        lineGraphView.setOnLineGraphBarChangeListener(this);

        ((TextView) findViewById(R.id.elec_avg_day)).setText("日均："
                + String.format("%.1f", ((-endY + startY) / datas.length))
                + "kW‧h");
        ((TextView) findViewById(R.id.elec_avg_month)).setText("月均："
                + String.format("%.1f", ((-endY + startY) / datas.length * 30))
                + "kW‧h");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        MenuItem changeBtn = menu.add(1, 0, 0, "切换帐号");
        changeBtn.setIcon(R.drawable.score_change_account_icon)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return  super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getGroupId() == 1 && item.getItemId() == 0) {
            startActivity(new Intent(this,ElectricActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


}

package net.bingyan.hustpass.module.score;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import net.bingyan.hustpass.API;
import net.bingyan.hustpass.R;
import net.bingyan.hustpass.db.CacheDaoHelper;
import net.bingyan.hustpass.http.RestHelper;
import net.bingyan.hustpass.ui.base.BaseFragment;
import net.bingyan.hustpass.util.Util;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ScoreFragment extends BaseFragment {

    ScoreAdapter mAdapter;

    View mView;
    View footerView;

    Bundle bundle;

    public static ScoreFragment newInstance(Bundle bundle) {
        ScoreFragment nf = new ScoreFragment();
        nf.setArguments(bundle);
        return nf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        bundle = getArguments();
    }
    LayoutInflater inflater;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        this.inflater = inflater;
        mView = getLayoutInflater(null).inflate(R.layout.activity_score_fragment, container, false);
        init();
        return mView;
    }

    private CacheDaoHelper.CacheInfo<ScoreBeanV2> getCache() {
        return CacheDaoHelper.getInstance().getCacheInfo(ScoreBeanV2.class, bundle.getString("term") + bundle.getString("uid"));
    }

    private void putCache(ScoreBeanV2 scoreBean) {
        CacheDaoHelper.getInstance().putCache(scoreBean, bundle.getString("term") + bundle.getString("uid"));
    }

    private void getByHttp(Callback<ScoreBeanV2> cb) {
//        RestHelper.getService(API.HubService.ScoreHOST, API.HubService.class)
//        RestHelper.getService(API.HubServiceV2.ScoreHOST, API.HubServiceV2.class)
//                .getScore(bundle.getString("term")
//                        , bundle.getString("uid")
//                        , cb);

        ScoreHelper scoreHelper = new ScoreHelper();
        scoreHelper.getScore(bundle.getString("uid"),bundle.getString("psw"),bundle.getString("term"),cb);

    }


    private void init() {
        initView();

        final CacheDaoHelper.CacheInfo<ScoreBeanV2> scoreBeanCache = getCache();
        if (scoreBeanCache!= null) {
            initData(scoreBeanCache.getCache());
        }

        if(scoreBeanCache!=null&&scoreBeanCache.getTime()<CacheDaoHelper.CACHE_TIME_OUT){
            return;
        }

        mLog.v("fragment start http"+bundle.getString("term"));
        showProgressBar();

        getByHttp(new Callback<ScoreBeanV2>() {
            @Override
            public void success(ScoreBeanV2 scoreBean, Response response) {
                if (scoreBeanCache==null||!scoreBean.equals(scoreBeanCache.getCache())) {
                    initData(scoreBean);
                    putCache(scoreBean);
                }
                finalexe();
            }

            @Override
            public void failure(RetrofitError error) {
                Util.toast("网络出错"+error.getMessage());
                finalexe();
            }

            private void finalexe(){
                mLog.v("fragment ok http"+bundle.getString("term"));
                stopProgressBar();
            }
        });
    }

    public void initView() {

    }

    public void initData(ScoreBeanV2 scoreBean) {
        if(scoreBean==null||scoreBean.getData()==null){
            return;
        }

        if (footerView == null) {
            ListView mListView = (ListView) mView.findViewById(R.id.score_listview);
            footerView = inflater.inflate( R.layout.activity_score_footerview, null,false);
            mListView.addFooterView(footerView);
            mAdapter = new ScoreAdapter(scoreBean.getData().getScore());
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.updataData(scoreBean.getData().getScore());
        }
        ScoreBeanV2.Data.Information infor = scoreBean.getData().getInformation();
        ((TextView) mView.findViewById(R.id.score_user_name)).setText(infor.getName());
        ((TextView) mView.findViewById(R.id.score_department)).setText(infor.getClass_());
        ((TextView) mView.findViewById(R.id.score_uid)).setText(infor.getId());
//        ((TextView) footerView.findViewById(R.id.score_xuefen)).setText("学分：" + scoreBean.getData().get.getCredits());
        ((TextView) footerView.findViewById(R.id.score_xuefen)).setText("");
        ((TextView) footerView.findViewById(R.id.score_jiaquan)).setText("加权/学分：" + scoreBean.getData().getWeightedscore());
    }

    class ScoreAdapter extends BaseAdapter {
        List<ScoreBeanV2.Data.Score> classInfos;

        public ScoreAdapter(List<ScoreBeanV2.Data.Score> classInfos) {
            this.classInfos = classInfos;
        }

        public void updataData(List<ScoreBeanV2.Data.Score> classInfos) {
            this.classInfos = classInfos;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return classInfos.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return classInfos.get(position);
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
                convertView = View.inflate(getActivity(),
                        R.layout.activity_score_list_item, null);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.textView1 = (TextView) convertView
                        .findViewById(R.id.textview1);
                viewHolder.textView2 = (TextView) convertView
                        .findViewById(R.id.textview2);
                viewHolder.textView3 = (TextView) convertView
                        .findViewById(R.id.textview3);
                convertView.setTag(viewHolder);
            }
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.textView1.setText(classInfos.get(position).getName());
            viewHolder.textView2.setText(classInfos.get(position).getCredit());
            viewHolder.textView3.setText(classInfos.get(position).getScore());
            if (position == classInfos.size() - 1) {
                convertView.findViewById(R.id.score_list_divider)
                        .setVisibility(View.GONE);
            }
            if (isfailed(classInfos.get(position).getScore())) {
                viewHolder.textView3.setTextColor(Color.parseColor("#FF4B69"));
                convertView.setBackgroundColor(Color.parseColor("#FFE0E2"));
            } else {
                viewHolder.textView3.setTextColor(Color.parseColor("#838383"));
                convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            return convertView;
        }

        private boolean isfailed(String score) {
            try {
                if (Float.valueOf(score) >= 60) {
                    return false;
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            return true;
        }

    }

    private class ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;
    }
}

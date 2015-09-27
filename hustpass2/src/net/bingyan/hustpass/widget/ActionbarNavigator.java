package net.bingyan.hustpass.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.util.Util;

import java.util.List;

/**
 * Created by ant on 14-8-5.
 */
public class ActionbarNavigator extends TextView implements AdapterView.OnItemClickListener,View.OnClickListener {

    PopupWindow popWindow;

    ListView listView;
    OnItemSelectedListener onItemSelectedListener;
    List<String> data;

    public ActionbarNavigator(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ActionbarNavigator(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public ActionbarNavigator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public void setData(List<String> data, int initPosition,String title) {
        View vPopWindow = View.inflate(getContext(), R.layout.activity_announcement_popwindow, null);
        this.data = data;
        initPopWindow(vPopWindow,title);
        initListView(vPopWindow);
        setText(data.get(initPosition));
        setOnClickListener(this);
    }

    private void initPopWindow(View vPopWindow, String title) {
        View main = findViewById(android.R.id.content);
        ((TextView)vPopWindow.findViewById(R.id.title)).setText(title);
        popWindow = new PopupWindow(vPopWindow, Util.getSreenWidth(getContext()),
                Util.getSreenHeight(getContext())-150, true);
        popWindow.setBackgroundDrawable(getResources().getDrawable(R.color.transparent_half));
        popWindow.setFocusable(true);
    }

    private void initListView(View vPopWindow) {
        listView = (ListView) vPopWindow.findViewById(R.id.announcement_navigater_listview);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(
                getContext(), R.layout.activity_announcement_popwindow_item,
                data);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(popWindow.isShowing()){
            popWindow.dismiss();
        }else {
            popWindow.showAsDropDown(this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        popWindow.dismiss();
        setText(data.get(i));
        if(onItemSelectedListener!=null){
            onItemSelectedListener.onSelected(i,view);
        }
    }

    public  interface OnItemSelectedListener {
        public void onSelected(int i, View v);
    }

    private void init() {
        final View vPopWindow = View.inflate(getContext(), R.layout.activity_announcement_popwindow, null);
        listView = (ListView) vPopWindow.findViewById(R.id.announcement_navigater_listview);

        View main = findViewById(R.id.abs__content);
        popWindow = new PopupWindow(vPopWindow, main.getWidth(),
                main.getHeight(), true);
        popWindow.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
        popWindow.setFocusable(true);


//        listView.setAdapter(listAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                // TODO Auto-generated method stub
//                popWindow.dismiss();
//                if (position == itemPositionNow)
//                    return;
//                Pref.getPref().edit().putInt(Pref.ANNOUNCEMENT_INDEX, position)
//                        .commit();
//                mNavigator.setText(items[position]);
//                itemPositionNow = position;
//                mTitle = new String[announcementInfoBean.getData()
//                        .get(itemPositionNow).getCategory().size()];
//                announcementInfoBean.getData().get(itemPositionNow)
//                        .getCategory().toArray(mTitle);
//                mDepartment = announcementInfoBean.getData()
//                        .get(itemPositionNow).getSimp_name();
//			/*	if (mXFragmentPagerAdapter != null) {
//					mXFragmentPagerAdapter.notifyDataSetChanged();
//					mPageIndicator.setViewPager(mViewPager);
//				} else {*/
//                mXFragmentPagerAdapter = new AnnFragmentPagerAdapter(
//                        getChildFragmentManager(), sm, mTitle);
//                mViewPager.setAdapter(mXFragmentPagerAdapter);
//                mPageIndicator.setViewPager(mViewPager);
////				}
//            }
//        });
    }
}

package net.bingyan.hustpass.module.classroom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.db.CacheDaoHelper;
import net.bingyan.hustpass.http.RestHelper;
import net.bingyan.hustpass.API;
import net.bingyan.hustpass.util.Pref;
import net.bingyan.hustpass.widget.pinnedHeaderListview.PinnedHeaderListView;
import net.bingyan.hustpass.widget.pinnedHeaderListview.SectionedBaseAdapter;
import net.bingyan.hustpass.ui.base.BaseActivity;
import net.bingyan.hustpass.util.HustUtils;
import net.bingyan.hustpass.util.Util;
import net.bingyan.hustpass.widget.ActionbarNavigator;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ClassroomActivity extends BaseActivity {
//    String TAG = "ClassroomActivity";
    TabHelper mTabHelper;

    String[] mBuildings = {"东九教学楼", "东十二教学楼", "西五教学楼", "西十二教学楼"};
    String[] mBuildingsId = {"DONG9","DONG12","XI5","XI12"};
//    String[] mBuildings = {"东九教学楼", "西十二教学楼"};
//    String[] mBuildingsId = {"9", "12"};

    int mNavigatorIdNow = 0;

    PopupWindow popWindowSeach;
    View searchDeatilView;
    EditText searchText;
    TextView searchTitle;
    TextView searchDetail;

    Boolean[] mSlectedId;

    List<ArrayList<String>> items = new ArrayList<ArrayList<String>>();
    List<String> sectionsArrayList = new ArrayList<String>();
    int[] sectionsArrayListNum = new int[30];
    List<ArrayList<String[]>> PinnedItems = new ArrayList<ArrayList<String[]>>();

    PinnedHeaderListView mListView;
    SectionedBaseAdapter mClassRoomListAdapter;
    ClassBeanV3.Data classRoomData;

    @Override
    public void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_classroom);
        mNavigatorIdNow = Pref.getPref().getInt(Pref.CLASSROOM_BUILDING_ID, 0);
        if(mNavigatorIdNow>1){
            mNavigatorIdNow = 0;
        }
        mTabHelper = new TabHelper();
        mListView = (PinnedHeaderListView) findViewById(R.id.classroom_listview);
        mListView.setEmptyView(findViewById(R.id.listview_empty_tv));
        init();
    }

    private CacheDaoHelper.CacheInfo<ClassBeanV3> getClassRoomCache() {
        // 20 小时
        return new CacheDaoHelper(getApplication()).getCacheInfo(ClassBeanV3.class, mBuildingsId[mNavigatorIdNow]);
    }

    private void putClassRoomCache(ClassBeanV3 classRoomBean) {
        new CacheDaoHelper(getApplication()).putCache(classRoomBean, mBuildingsId[mNavigatorIdNow]);
    }

    private void getClassRoomHttp(Callback<ClassBeanV3> cb) {
        RestHelper.getService(API.ClassRoomServiceV3.HOST,API.ClassRoomServiceV3.class).getClassRoom(mBuildingsId[mNavigatorIdNow], cb);
    }

    private void init() {
        CacheDaoHelper.CacheInfo<ClassBeanV3> classRoomBean = getClassRoomCache();
        if (classRoomBean != null) {
            initContent(classRoomBean.getCache().getData());
        }

        if (classRoomBean != null && classRoomBean.getTime() < CacheDaoHelper.CACHE_TIME_OUT) {
            return;
        }

        showProgressBar();
        ((TextView)findViewById(R.id.listview_empty_tv)).setText("加载中...");
        getClassRoomHttp(new Callback<ClassBeanV3>() {
            @Override
            public void success(ClassBeanV3 classBean, Response response) {
                stopProgressBar();

                if (classBean.getStatus()==null||!classBean.getStatus().equals("success")) {
                    Util.toast("暂无教室数据，请下次再来");
                    ((TextView)findViewById(R.id.listview_empty_tv)).setText("暂无教室数据");
                    initNavigater();
                    return;
                }

                initContent(classBean.getData());
                putClassRoomCache(classBean);
            }

            @Override
            public void failure(RetrofitError error) {
                Util.toast("网络出错" + error.getMessage());
                ((TextView)findViewById(R.id.listview_empty_tv)).setText("网络出错");
                stopProgressBar();
            }
        });

    }

    private void initContent(ClassBeanV3.Data classRoomBean) {
        this.classRoomData = classRoomBean;
        initNavigater();
        initListView(classRoomBean);
    }

    private void initNavigater() {
        final ActionbarNavigator mNavigator = (ActionbarNavigator) findViewById(R.id.actionbar_navigiter);
        mNavigator.setVisibility(View.VISIBLE);
        List<String> items = new ArrayList<String>();
        for (String item : mBuildings) {
            items.add(item);
        }
        mNavigator.setData(items, mNavigatorIdNow, "教学楼选择");
        mNavigator.setOnItemSelectedListener(new ActionbarNavigator.OnItemSelectedListener() {
            @Override
            public void onSelected(int i, View v) {
                Pref.getPref().edit().putInt(Pref.CLASSROOM_BUILDING_ID, i).commit();
                mNavigatorIdNow = i;
                init();
            }
        });
    }

    private void initListView(ClassBeanV3.Data classRoomData) {
        setContentView(classRoomData);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuItem searchBtn;
//        searchBtn = menu.add(0, 0, 0, "search");
//        searchBtn.setIcon(R.drawable.classroom_actionbar_search_btn)
//                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getGroupId() == 0) {
//            switch (item.getItemId()) {
//                case 0:
//                    if (popWindowSeach == null) {
//                        initSeachBox();
//                    }
//                    if (popWindowSeach.isShowing()) {
//                        popWindowSeach.dismiss();
//                    } else {
//                        popWindowSeach
//                                .showAsDropDown(findViewById(R.id.actionbar_navigiter));
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }

//    private void initSeachBox() {
//        LayoutInflater inflater = (LayoutInflater) this
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View seachBoxView = inflater.inflate(
//                R.layout.activity_classroom_seachbox, null, false);
//        searchDeatilView = seachBoxView
//                .findViewById(R.id.classroom_search_detail_view);
//
//        searchText = (EditText) seachBoxView
//                .findViewById(R.id.classroom_seach_text);
//        searchTitle = (TextView) seachBoxView.findViewById(R.id.title);
//        searchDetail = (TextView) seachBoxView
//                .findViewById(R.id.classroom_seach_detail);
//
//        popWindowSeach = new PopupWindow(seachBoxView,
//                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
//        popWindowSeach.setBackgroundDrawable(getResources().getDrawable(
//                R.color.transparent_half));
//        popWindowSeach.setFocusable(true);
//        seachBoxView.findViewById(R.id.classroom_seach_btn).setOnClickListener(
//                this);
//        seachBoxView.findViewById(R.id.pop_class_search_main)
//                .setOnClickListener(this);
//    }

//    private void searchRoomDetail() {
//        String roomName = searchText.getText().toString().toUpperCase().trim();
//        String title = mBuildings[mNavigatorIdNow] + roomName;
//        String detail = "";
//        if (!Util.isEmpty(roomName) && classRoomData != null) {
//            for (ClassBean.ClassRoomBean.Room room : classRoomData.getRoomList()) {
//                if (room.getRoomName().equals(roomName)) {
//                    String[] list = new String[6];
//                    for (int i = 0; i < 6; i++) {
//                        for (ClassBean.ClassRoomBean.Room.Event roomEvent : room
//                                .getEventList()) {
//                            if (roomEvent.getStartTime() <= i + 1
//                                    && roomEvent.getEndTime() >= i + 1) {
//                                list[i] = roomEvent.getEventOrganizer();
//                            }
//                        }
//                    }
//
//                    for (int i = 0; i < 6; i++) {
//                        if (list[i] == null)
//                            list[i] = "自习";
//                        detail = detail + ((i + 1) * 2 - 1) + "-" + (i + 1) * 2
//                                + "    " + list[i] + "\n";
//                    }
//                    break;
//                }
//            }
//        }
//        if (Util.isEmpty(detail)) {
//            detail = "请输入正确完整的教室名\n";
//        }
//        searchDeatilView.setVisibility(View.VISIBLE);
//        searchTitle.setText(title);
//        searchDetail.setText(detail);
//    }

    public void onTabSelectedChanged(Boolean[] slectedId) {
        mLog.v("tab changed");
        setContentView(classRoomData);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        super.onClick(arg0);
//        if (arg0.getId() == R.id.classroom_seach_btn) {
//            searchRoomDetail();
//            return;
//        }
        if (arg0.getId() == R.id.pop_class_search_main) {
            if (popWindowSeach.isShowing()) {
                popWindowSeach.dismiss();
            }
            return;
        }
        if (mTabHelper != null)
            mTabHelper.onClick(arg0);
    }

//    private void setContentView(ClassBean.ClassRoomBean classRoomBean) {
//        if (classRoomBean == null)
//            return;
//        items.clear();
//        sectionsArrayList.clear();
//        PinnedItems.clear();
//        mSlectedId = mTabHelper.slectedId;
//
//        List<String> freeRooms = new ArrayList<String>();
//        List<Integer> filter = new ArrayList<Integer>();
//        for (int i = 1; i < mSlectedId.length; i++) {
//            if (mSlectedId[i]) {
//                filter.add(i);
//            }
//        }
//        if (!filter.isEmpty()) {
//            OUTERMOST:
//            for (ClassBean.ClassRoomBean.Room room : classRoomBean
//                    .getRoomList()) {
//                for (ClassBean.ClassRoomBean.Room.Event roomEvent : room.getEventList()) {
//                    for (Integer integer : filter) {
//                        if (integer <= roomEvent.getEndTime()
//                                && integer >= roomEvent.getStartTime()) {
//                            continue OUTERMOST;
//                        }
//                    }
//                }
//                freeRooms.add(room.getRoomName());
//            }
//        }
//        if (freeRooms.isEmpty()) {
//            if (mClassRoomListAdapter == null) {
//                mClassRoomListAdapter = new ClassRoomListAdapter(PinnedItems,
//                        sectionsArrayList);
//                mListView.setAdapter(mClassRoomListAdapter);
//            } else {
//                ((TextView)findViewById(R.id.listview_empty_tv)).setText("请选择自习时间(蓝色选中)");
//                mClassRoomListAdapter.notifyDataSetChanged();
//            }
//            return;
//        }
//        String[] floorNameStrings = {"楼", "座"};
//        for (String roomName : freeRooms) {
//            String section = roomName.charAt(0)
//                    + (Character.isDigit(roomName.charAt(0)) ? floorNameStrings[0]
//                    : floorNameStrings[1]);
//            int index = sectionsArrayList.indexOf(section);
//            if (index == -1) {
//                int i = 0;
//                for (String tag : sectionsArrayList){
//                    if(tag.compareTo(section)>0){
//                         break;
//                    }else {
//                        i++;
//                    }
//                }
//
//                sectionsArrayList.add(i,section);
//                sectionsArrayListNum[sectionsArrayList.indexOf(section)] = 0;
//                items.add(new ArrayList<String>());
//            }
//            items.get(sectionsArrayList.indexOf(section)).add(roomName);
//
//        }
//
//        String[] itemRoomNames = {"", "", "", ""};
//        for (int i = 0; i < sectionsArrayList.size(); i++) {
//            PinnedItems.add(new ArrayList<String[]>());
//            int count = 0;
//            ArrayList<String> sectionItem = items.get(i);
//            for (int j = 0; j < sectionItem.size(); j++) {
//                itemRoomNames[count] = sectionItem.get(j);
//                count++;
//                if (count == 4) {
//                    count = 0;
//                    String[] rooms = itemRoomNames.clone();
//                    PinnedItems.get(i).add(rooms);
//                }
//                if ((j == sectionItem.size() - 1 && count != 0)) {
//                    for (int j2 = count; j2 < 4; j2++) {
//                        itemRoomNames[j2] = "";
//                    }
//                    count = 0;
//                    String[] rooms = itemRoomNames.clone();
//                    PinnedItems.get(i).add(rooms);
//                }
//            }
//        }
//
//        if (mClassRoomListAdapter == null) {
//            mClassRoomListAdapter = new ClassRoomListAdapter(PinnedItems,
//                    sectionsArrayList);
//            mListView.setAdapter(mClassRoomListAdapter);
//        } else {
//            mClassRoomListAdapter.notifyDataSetChanged();
//        }
//    }

    private void setContentView(ClassBeanV3.Data classRoomBean) {
        if (classRoomBean == null)
            return;
        items.clear();
        sectionsArrayList.clear();
        PinnedItems.clear();
        mSlectedId = mTabHelper.slectedId;

        List<String> freeRooms = new ArrayList<String>();
        Set<Integer> filter = new HashSet<Integer>();
        for (int i = 1; i < mSlectedId.length; i++) {
            if (mSlectedId[i]) {
                if(i==6){
                    filter.add(5);
                }else {
                    filter.add(i);
                }
            }
        }
        if (!filter.isEmpty()) {
            for (Integer integer : filter) {
                List<String> data = classRoomBean.getfreeRoom(integer);
                if(freeRooms.isEmpty()){
                    freeRooms.addAll(data);
                }else {
                    Iterator<String> iter = freeRooms.iterator();
                    while(iter.hasNext()){
                        String s = iter.next();
                        if(!data.contains(s)){
                            iter.remove();
                        }
                    }
                }
            }
        }
        if (freeRooms.isEmpty()) {
            if (mClassRoomListAdapter == null) {
                mClassRoomListAdapter = new ClassRoomListAdapter(PinnedItems,
                        sectionsArrayList);
                mListView.setAdapter(mClassRoomListAdapter);
            } else {
                ((TextView)findViewById(R.id.listview_empty_tv)).setText("请选择自习时间(蓝色选中)");
                mClassRoomListAdapter.notifyDataSetChanged();
            }
            return;
        }
        String[] floorNameStrings = {"楼", "座"};
        for (String roomName : freeRooms) {
            if(roomName.length()==0) continue;
            String section = roomName.charAt(0)
                    + (Character.isDigit(roomName.charAt(0)) ? floorNameStrings[0]
                    : floorNameStrings[1]);
            int index = sectionsArrayList.indexOf(section);
            if (index == -1) {
                int i = 0;
                for (String tag : sectionsArrayList){
                    if(tag.compareTo(section)>0){
                        break;
                    }else {
                        i++;
                    }
                }

                sectionsArrayList.add(i,section);
                sectionsArrayListNum[sectionsArrayList.indexOf(section)] = 0;
                items.add(new ArrayList<String>());
            }
            items.get(sectionsArrayList.indexOf(section)).add(roomName);

        }

        String[] itemRoomNames = {"", "", "", ""};
        for (int i = 0; i < sectionsArrayList.size(); i++) {
            PinnedItems.add(new ArrayList<String[]>());
            int count = 0;
            ArrayList<String> sectionItem = items.get(i);
            for (int j = 0; j < sectionItem.size(); j++) {
                itemRoomNames[count] = sectionItem.get(j);
                count++;
                if (count == 4) {
                    count = 0;
                    String[] rooms = itemRoomNames.clone();
                    PinnedItems.get(i).add(rooms);
                }
                if ((j == sectionItem.size() - 1 && count != 0)) {
                    for (int j2 = count; j2 < 4; j2++) {
                        itemRoomNames[j2] = "";
                    }
                    count = 0;
                    String[] rooms = itemRoomNames.clone();
                    PinnedItems.get(i).add(rooms);
                }
            }
        }

        if (mClassRoomListAdapter == null) {
            mClassRoomListAdapter = new ClassRoomListAdapter(PinnedItems,
                    sectionsArrayList);
            mListView.setAdapter(mClassRoomListAdapter);
        } else {
            mClassRoomListAdapter.notifyDataSetChanged();
        }
    }

    class ClassRoomListAdapter extends SectionedBaseAdapter {
        List<ArrayList<String[]>> pinnedItems;
        List<String> sections;

        public ClassRoomListAdapter(List<ArrayList<String[]>> PinnedItems,
                                    List<String> sections) {
            // TODO Auto-generated constructor stub
            this.pinnedItems = PinnedItems;
            this.sections = sections;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public Object getItem(int section, int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int section, int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int getSectionCount() {
            // TODO Auto-generated method stub
            return sections.size();
        }

        @Override
        public int getCountForSection(int section) {
            // TODO Auto-generated method stub
            return pinnedItems.get(section).size();
        }

        @Override
        public View getItemView(int section, int position, View convertView,
                                ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                convertView = View.inflate(ClassroomActivity.this,
                        R.layout.activity_classroom_listview_item, null);
                PinnedViewHolder vh = new PinnedViewHolder();
                vh.tv[0] = (TextView) convertView
                        .findViewById(R.id.classroom_text_1);
                vh.tv[1] = (TextView) convertView
                        .findViewById(R.id.classroom_text_2);
                vh.tv[2] = (TextView) convertView
                        .findViewById(R.id.classroom_text_3);
                vh.tv[3] = (TextView) convertView
                        .findViewById(R.id.classroom_text_4);
                convertView.setTag(vh);
            }
            PinnedViewHolder vh = (PinnedViewHolder) convertView.getTag();
            for (int i = 0; i < 4; i++) {
                vh.tv[i].setText(pinnedItems.get(section).get(position)[i]);
            }
            return convertView;
        }

        @Override
        public View getSectionHeaderView(int section, View convertView,
                                         ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                convertView = View.inflate(ClassroomActivity.this,
                        R.layout.activity_classroom_item_section, null);
            }
            TextView tv = ((TextView) convertView
                    .findViewById(R.id.classroom_listview_section));
            tv.setText(sections.get(section));
            if (section % 2 == 1) {
                tv.setBackgroundColor(getResources().getColor(
                        R.color.classroom_item_section_color_2));
            } else {
                tv.setBackgroundColor(getResources().getColor(
                        R.color.classroom_item_section_color_1));
            }
            return convertView;
        }

    }

    class PinnedViewHolder {
        TextView[] tv = new TextView[4];
    }

    public class TabHelper {
        int[] vId = {R.id.classroom_top_btn_all, R.id.classroom_top_btn_1,
                R.id.classroom_top_btn_2, R.id.classroom_top_btn_3,
                R.id.classroom_top_btn_4, R.id.classroom_top_btn_5,
                R.id.classroom_top_btn_6};
        View[] btns = new View[7];
        int[] imgSelectId = {R.drawable.class_select_1,
                R.drawable.class_select_2, R.drawable.class_select_3,
                R.drawable.class_select_4, R.drawable.class_select_5,
                R.drawable.class_select_6};
        ImageView[] imgViews = new ImageView[7];
        TextView[] textViews = new TextView[7];
        Drawable[] imgSelects = new Drawable[6];
        Drawable[] imgUnSelects = new Drawable[6];
        int colorSelected;
        int colorUnselected;

        int[] imgUnselectId = {R.drawable.class_unselect_1,
                R.drawable.class_unselect_2, R.drawable.class_unselect_3,
                R.drawable.class_unselect_4, R.drawable.class_unselect_5,
                R.drawable.class_unselect_6};
        Boolean[] slectedId = {false, false, false, false, false, false, false};

        public TabHelper() {
            // TODO Auto-generated constructor stub
            int now = (int) HustUtils.getCrouseNum();
            if (now < 0) {
                now = -1 * now + 1;
                if (now == 13)
                    now = 12;
            }
            now = (now - 1) / 2;
            slectedId[now + 1] = true;

            for (int i = 0; i < vId.length; i++) {
                btns[i] = findViewById(vId[i]);
                btns[i].setOnClickListener(ClassroomActivity.this);
                imgViews[i] = (ImageView) btns[i]
                        .findViewById(R.id.classroom_top_btn_icon);
                textViews[i] = (TextView) btns[i]
                        .findViewById(R.id.classroom_top_btn_text);
            }
            for (int i = 0; i < imgSelectId.length; i++) {
                imgSelects[i] = getResources().getDrawable(imgSelectId[i]);
                imgUnSelects[i] = getResources().getDrawable(imgUnselectId[i]);
            }
            colorSelected = getResources().getColor(
                    R.color.classroom_top_text_selected);
            colorUnselected = getResources().getColor(R.color.white);
            toggleView();
        }

        private void onClick(View v) {
            for (int i = 0; i < vId.length; i++) {
                if (vId[i] == v.getId()) {
                    if (i == 0) {
                        for (int j = 0; j < slectedId.length; j++) {
                            slectedId[j] = true;
                        }
                    } else {
                        slectedId[i] = !slectedId[i];
                        if (slectedId[0] && !slectedId[i]) {
                            slectedId[0] = false;
                        }
                    }
                    toggleView();
                    onTabSelectedChanged(slectedId);
                }
            }
        }

        private void toggleView() {
            for (int j = 0; j < slectedId.length; j++) {
                if (!slectedId[j]) {
                    if (j == 0) {
                        btns[j].setBackgroundResource(R.drawable.classroom_top_btn_bg_r_n);
                    } else {
                        btns[j].setBackgroundResource(R.drawable.classroom_top_btn_bg_mid);
                        imgViews[j].setImageDrawable(imgSelects[j - 1]);
                        textViews[j].setTextColor(colorSelected);
                    }

                } else {
                    textViews[j].setTextColor(colorUnselected);
                    if (j == 0) {
                        // imgViews[0].setImageDrawable(getResources().getDrawable(R.drawable.classroom_top_all));
                    } else {
                        imgViews[j].setImageDrawable(imgUnSelects[j - 1]);
                    }
                    if (j == 0) {
                        if (slectedId[j + 1]) {
                            btns[j].setBackgroundResource(R.drawable.classroom_top_btn_bg_r_r);
                        } else {
                            btns[j].setBackgroundResource(R.drawable.classroom_top_btn_bg_r_n);
                        }
                    } else if (j == 6) {
                        if (slectedId[j - 1]) {
                            btns[j].setBackgroundResource(R.drawable.classroom_top_btn_bg_r_l);
                        } else {
                            btns[j].setBackgroundResource(R.drawable.classroom_top_btn_bg_r_n);
                        }

                    } else if (slectedId[j - 1] && slectedId[j + 1]) {
                        btns[j].setBackgroundResource(R.drawable.classroom_top_btn_bg_r_lr);
                    } else if (slectedId[j - 1] && !slectedId[j + 1]) {
                        btns[j].setBackgroundResource(R.drawable.classroom_top_btn_bg_r_l);
                    } else if (!slectedId[j - 1] && slectedId[j + 1]) {
                        btns[j].setBackgroundResource(R.drawable.classroom_top_btn_bg_r_r);
                    } else {
                        btns[j].setBackgroundResource(R.drawable.classroom_top_btn_bg_r_n);
                    }
                }
            }

        }
    }



}

package net.bingyan.hustpass.adapter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 老版本使用，弱引用，内部类pageinfo，来实现，不用继承<br>
 * 但是activity 不容易得到fragment 对它操作<br>
 * 所以老版本，把获取actionbar响应放在fragment中，setHasOptionsMenu(true);<br>
 * 我再activity中得到fragment的引用，也就是为了refresh按钮<br>
 * <p/>
 * 最终我决定 定义接口<br>
 * 其实可以给BaseActivity定义接口，让fragment去实现，关键在于希望把处理放在哪里
 */
abstract public class XFragmentPagerAdapter extends FragmentStatePagerAdapter {

    public List<String> mTitles = new ArrayList<String>();

    public XFragmentPagerAdapter(FragmentManager fm, String[] titles) {
        // TODO Auto-generated constructor stub
        super(fm);

        for (String s : titles) {
            mTitles.add(s);
        }

    }

    public XFragmentPagerAdapter(FragmentManager fm, List<String> titles) {
        // TODO Auto-generated constructor stub
        super(fm);
        mTitles = titles;
    }

    public void setTitle(List<String> titles){
        mTitles = titles;
    }

    abstract public Fragment getItem(int arg0);

    abstract public Bundle getBundle(int i);

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // TODO Auto-generated method stub
        return mTitles.get(position);
    }

}

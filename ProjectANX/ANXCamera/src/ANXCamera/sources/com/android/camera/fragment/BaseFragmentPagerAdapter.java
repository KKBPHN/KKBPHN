package com.android.camera.fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import java.util.List;

public class BaseFragmentPagerAdapter extends FragmentPagerAdapter {
    private List mFragmentList;

    public BaseFragmentPagerAdapter(FragmentManager fragmentManager, List list) {
        super(fragmentManager);
        this.mFragmentList = list;
    }

    public int getCount() {
        List list = this.mFragmentList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public List getFragmentList() {
        return this.mFragmentList;
    }

    public Fragment getItem(int i) {
        return (Fragment) this.mFragmentList.get(i);
    }

    public long getItemId(int i) {
        Fragment item = getItem(i);
        return item != null ? (long) (item.hashCode() | i) : super.getItemId(i);
    }

    public void recycleFragmentList(FragmentManager fragmentManager) {
        if (this.mFragmentList != null) {
            FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
            for (Fragment remove : this.mFragmentList) {
                beginTransaction.remove(remove);
            }
            beginTransaction.commitAllowingStateLoss();
        }
    }
}

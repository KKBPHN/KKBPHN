package com.miui.internal.app;

import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import miui.view.PagerAdapter;
import miui.view.ViewPager;

class DynamicFragmentPagerAdapter extends PagerAdapter {
    private Context mContext;
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;
    private ArrayList mFragmentInfos = new ArrayList();
    private FragmentManager mFragmentManager;
    private boolean mInitialized;
    private WeakReference mViewPagerRef;

    class FragmentInfo {
        Bundle args;
        Class clazz;
        Fragment fragment = null;
        boolean hasActionMenu;
        Tab tab;
        String tag;

        FragmentInfo(String str, Class cls, Bundle bundle, Tab tab2, boolean z) {
            this.tag = str;
            this.clazz = cls;
            this.args = bundle;
            this.tab = tab2;
            this.hasActionMenu = z;
        }
    }

    public DynamicFragmentPagerAdapter(Context context, FragmentManager fragmentManager, ViewPager viewPager) {
        this.mContext = context;
        this.mFragmentManager = fragmentManager;
        this.mViewPagerRef = new WeakReference(viewPager);
        viewPager.setAdapter(this);
    }

    private void removeAllFragmentFromManager() {
        FragmentTransaction beginTransaction = this.mFragmentManager.beginTransaction();
        int size = this.mFragmentInfos.size();
        for (int i = 0; i < size; i++) {
            beginTransaction.remove(getFragment(i, false));
        }
        beginTransaction.commitAllowingStateLoss();
        this.mFragmentManager.executePendingTransactions();
    }

    private void removeFragmentFromManager(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = fragment.getFragmentManager();
            if (fragmentManager != null) {
                FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
                beginTransaction.remove(fragment);
                beginTransaction.commitAllowingStateLoss();
                fragmentManager.executePendingTransactions();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public int addFragment(String str, int i, Class cls, Bundle bundle, Tab tab, boolean z) {
        int i2;
        ArrayList arrayList;
        FragmentInfo fragmentInfo = new FragmentInfo(str, cls, bundle, tab, z);
        if (isRTL()) {
            if (i >= this.mFragmentInfos.size()) {
                arrayList = this.mFragmentInfos;
                i2 = 0;
            } else {
                arrayList = this.mFragmentInfos;
                i2 = toIndexForRTL(i) + 1;
            }
            arrayList.add(i2, fragmentInfo);
        } else {
            this.mFragmentInfos.add(i, fragmentInfo);
        }
        notifyDataSetChanged();
        return i;
    }

    /* access modifiers changed from: 0000 */
    public int addFragment(String str, Class cls, Bundle bundle, Tab tab, boolean z) {
        if (isRTL()) {
            ArrayList arrayList = this.mFragmentInfos;
            FragmentInfo fragmentInfo = new FragmentInfo(str, cls, bundle, tab, z);
            arrayList.add(0, fragmentInfo);
        } else {
            ArrayList arrayList2 = this.mFragmentInfos;
            FragmentInfo fragmentInfo2 = new FragmentInfo(str, cls, bundle, tab, z);
            arrayList2.add(fragmentInfo2);
        }
        notifyDataSetChanged();
        return this.mFragmentInfos.size() - 1;
    }

    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        this.mCurTransaction.detach((Fragment) obj);
    }

    /* access modifiers changed from: 0000 */
    public int findPositionByTag(String str) {
        int size = this.mFragmentInfos.size();
        for (int i = 0; i < size; i++) {
            if (((FragmentInfo) this.mFragmentInfos.get(i)).tag.equals(str)) {
                return toIndexForRTL(i);
            }
        }
        return -1;
    }

    public void finishUpdate(ViewGroup viewGroup) {
        FragmentTransaction fragmentTransaction = this.mCurTransaction;
        if (fragmentTransaction != null) {
            fragmentTransaction.commitAllowingStateLoss();
            this.mCurTransaction = null;
            this.mFragmentManager.executePendingTransactions();
        }
        if (!this.mInitialized && isRTL()) {
            this.mInitialized = true;
            ViewPager viewPager = (ViewPager) this.mViewPagerRef.get();
            if (viewPager != null) {
                viewPager.setCurrentItem(toIndexForRTL(viewPager.getCurrentItem()));
            }
        }
    }

    public int getCount() {
        return this.mFragmentInfos.size();
    }

    /* access modifiers changed from: 0000 */
    public Fragment getFragment(int i, boolean z) {
        return getFragment(i, z, true);
    }

    /* access modifiers changed from: 0000 */
    public Fragment getFragment(int i, boolean z, boolean z2) {
        if (this.mFragmentInfos.isEmpty()) {
            return null;
        }
        ArrayList arrayList = this.mFragmentInfos;
        if (z2) {
            i = toIndexForRTL(i);
        }
        FragmentInfo fragmentInfo = (FragmentInfo) arrayList.get(i);
        if (fragmentInfo.fragment == null) {
            fragmentInfo.fragment = this.mFragmentManager.findFragmentByTag(fragmentInfo.tag);
            if (fragmentInfo.fragment == null && z) {
                fragmentInfo.fragment = Fragment.instantiate(this.mContext, fragmentInfo.clazz.getName(), fragmentInfo.args);
                fragmentInfo.clazz = null;
                fragmentInfo.args = null;
            }
        }
        return fragmentInfo.fragment;
    }

    public int getItemPosition(Object obj) {
        int size = this.mFragmentInfos.size();
        for (int i = 0; i < size; i++) {
            if (obj == ((FragmentInfo) this.mFragmentInfos.get(i)).fragment) {
                return i;
            }
        }
        return -2;
    }

    /* access modifiers changed from: 0000 */
    public Tab getTabAt(int i) {
        return ((FragmentInfo) this.mFragmentInfos.get(i)).tab;
    }

    public boolean hasActionMenu(int i) {
        if (i < 0 || i >= this.mFragmentInfos.size()) {
            return false;
        }
        return ((FragmentInfo) this.mFragmentInfos.get(i)).hasActionMenu;
    }

    public Object instantiateItem(ViewGroup viewGroup, int i) {
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        Fragment fragment = getFragment(i, true, false);
        if (fragment.getFragmentManager() != null) {
            this.mCurTransaction.attach(fragment);
        } else {
            this.mCurTransaction.add(viewGroup.getId(), fragment, ((FragmentInfo) this.mFragmentInfos.get(i)).tag);
        }
        if (fragment != this.mCurrentPrimaryItem) {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }
        return fragment;
    }

    /* access modifiers changed from: 0000 */
    public boolean isRTL() {
        return this.mContext.getResources().getConfiguration().getLayoutDirection() == 1;
    }

    public boolean isViewFromObject(View view, Object obj) {
        return ((Fragment) obj).getView() == view;
    }

    /* access modifiers changed from: 0000 */
    public void removeAllFragment() {
        removeAllFragmentFromManager();
        this.mFragmentInfos.clear();
        this.mCurrentPrimaryItem = null;
        notifyDataSetChanged();
    }

    /* access modifiers changed from: 0000 */
    public int removeFragment(Tab tab) {
        int size = this.mFragmentInfos.size();
        for (int i = 0; i < size; i++) {
            FragmentInfo fragmentInfo = (FragmentInfo) this.mFragmentInfos.get(i);
            if (fragmentInfo.tab == tab) {
                removeFragmentFromManager(fragmentInfo.fragment);
                this.mFragmentInfos.remove(i);
                if (this.mCurrentPrimaryItem == fragmentInfo.fragment) {
                    this.mCurrentPrimaryItem = null;
                }
                notifyDataSetChanged();
                return toIndexForRTL(i);
            }
        }
        return -1;
    }

    /* access modifiers changed from: 0000 */
    public int removeFragment(Fragment fragment) {
        int size = this.mFragmentInfos.size();
        for (int i = 0; i < size; i++) {
            if (getFragment(i, false) == fragment) {
                removeFragmentFromManager(fragment);
                this.mFragmentInfos.remove(i);
                if (this.mCurrentPrimaryItem == fragment) {
                    this.mCurrentPrimaryItem = null;
                }
                notifyDataSetChanged();
                return toIndexForRTL(i);
            }
        }
        return -1;
    }

    /* access modifiers changed from: 0000 */
    public void removeFragmentAt(int i) {
        removeFragmentFromManager(getFragment(i, false));
        this.mFragmentInfos.remove(toIndexForRTL(i));
        notifyDataSetChanged();
    }

    /* access modifiers changed from: 0000 */
    public void setFragmentActionMenuAt(int i, boolean z) {
        FragmentInfo fragmentInfo = (FragmentInfo) this.mFragmentInfos.get(toIndexForRTL(i));
        if (fragmentInfo.hasActionMenu != z) {
            fragmentInfo.hasActionMenu = z;
            notifyDataSetChanged();
        }
    }

    public void setPrimaryItem(ViewGroup viewGroup, int i, Object obj) {
        Fragment fragment = (Fragment) obj;
        Fragment fragment2 = this.mCurrentPrimaryItem;
        if (fragment != fragment2) {
            if (fragment2 != null) {
                fragment2.setMenuVisibility(false);
                this.mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (!isRTL() || this.mInitialized) {
                if (fragment != null) {
                    fragment.setMenuVisibility(true);
                    fragment.setUserVisibleHint(true);
                }
                this.mCurrentPrimaryItem = fragment;
            }
        }
    }

    public void startUpdate(ViewGroup viewGroup) {
    }

    /* access modifiers changed from: 0000 */
    public int toIndexForRTL(int i) {
        if (!isRTL()) {
            return i;
        }
        int size = this.mFragmentInfos.size() - 1;
        if (size > i) {
            return size - i;
        }
        return 0;
    }
}

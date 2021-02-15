package com.miui.internal.app;

import android.app.Fragment;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;
import miui.app.ActionBar.FragmentViewPagerChangeListener;
import miui.util.ViewUtils;
import miui.view.ViewPager;

class ViewPagerScrollEffect implements FragmentViewPagerChangeListener {
    int mBaseItem = -1;
    boolean mBaseItemUpdated = true;
    int mIncomingPosition = -1;
    ViewGroup mListView = null;
    DynamicFragmentPagerAdapter mPagerAdapter;
    int mScrollBasePosition = -1;
    ViewPager mViewPager;
    ArrayList sList = new ArrayList();
    Rect sRect = new Rect();

    public ViewPagerScrollEffect(ViewPager viewPager, DynamicFragmentPagerAdapter dynamicFragmentPagerAdapter) {
        this.mViewPager = viewPager;
        this.mPagerAdapter = dynamicFragmentPagerAdapter;
    }

    /* access modifiers changed from: 0000 */
    public void clearTranslation(ViewGroup viewGroup) {
        fillList(viewGroup, this.sList);
        if (!this.sList.isEmpty()) {
            Iterator it = this.sList.iterator();
            while (it.hasNext()) {
                ((View) it.next()).setTranslationX(0.0f);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void clearTranslation(ArrayList arrayList, ViewGroup viewGroup) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            View view = (View) it.next();
            if (viewGroup.indexOfChild(view) == -1 && view.getTranslationX() != 0.0f) {
                view.setTranslationX(0.0f);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public int computOffset(int i, int i2, int i3, float f) {
        float f2 = ((float) (i < i3 ? (i * i2) / i3 : i2)) + ((0.1f - ((f * f) / 0.9f)) * ((float) i2));
        if (f2 > 0.0f) {
            return (int) f2;
        }
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public void fillList(ViewGroup viewGroup, ArrayList arrayList) {
        clearTranslation(arrayList, viewGroup);
        arrayList.clear();
        ViewUtils.getContentRect(viewGroup, this.sRect);
        if (!this.sRect.isEmpty()) {
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = viewGroup.getChildAt(i);
                if (childAt.getVisibility() != 8 || childAt.getHeight() > 0) {
                    arrayList.add(childAt);
                }
            }
        }
    }

    public void onPageScrollStateChanged(int i) {
        if (i == 0) {
            this.mBaseItem = this.mViewPager.getCurrentItem();
            this.mBaseItemUpdated = true;
            ViewGroup viewGroup = this.mListView;
            if (viewGroup != null) {
                clearTranslation(viewGroup);
            }
        }
    }

    public void onPageScrolled(int i, float f, boolean z, boolean z2) {
        int i2 = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
        if (i2 == 0) {
            this.mBaseItem = i;
            this.mBaseItemUpdated = true;
            ViewGroup viewGroup = this.mListView;
            if (viewGroup != null) {
                clearTranslation(viewGroup);
            }
        }
        if (this.mScrollBasePosition != i) {
            int i3 = this.mBaseItem;
            if (i3 < i) {
                this.mBaseItem = i;
            } else {
                int i4 = i + 1;
                if (i3 > i4) {
                    this.mBaseItem = i4;
                }
            }
            this.mScrollBasePosition = i;
            this.mBaseItemUpdated = true;
            ViewGroup viewGroup2 = this.mListView;
            if (viewGroup2 != null) {
                clearTranslation(viewGroup2);
            }
        }
        if (i2 > 0) {
            if (this.mBaseItemUpdated) {
                this.mBaseItemUpdated = false;
                if (this.mBaseItem != i || i >= this.mPagerAdapter.getCount() - 1) {
                    this.mIncomingPosition = i;
                } else {
                    this.mIncomingPosition = i + 1;
                }
                Fragment fragment = this.mPagerAdapter.getFragment(this.mIncomingPosition, false);
                this.mListView = null;
                if (!(fragment == null || fragment.getView() == null)) {
                    View findViewById = fragment.getView().findViewById(16908298);
                    if (findViewById instanceof ViewGroup) {
                        this.mListView = (ViewGroup) findViewById;
                    }
                }
            }
            if (this.mIncomingPosition == i) {
                f = 1.0f - f;
            }
            float f2 = f;
            ViewGroup viewGroup3 = this.mListView;
            if (viewGroup3 != null) {
                translateView(viewGroup3, viewGroup3.getWidth(), this.mListView.getHeight(), f2, this.mIncomingPosition != i);
            }
        }
    }

    public void onPageSelected(int i) {
    }

    /* access modifiers changed from: 0000 */
    public void translateView(ViewGroup viewGroup, int i, int i2, float f, boolean z) {
        fillList(viewGroup, this.sList);
        if (!this.sList.isEmpty()) {
            int i3 = 0;
            int top = ((View) this.sList.get(0)).getTop();
            int i4 = Integer.MAX_VALUE;
            Iterator it = this.sList.iterator();
            while (it.hasNext()) {
                View view = (View) it.next();
                if (i4 != view.getTop()) {
                    int top2 = view.getTop();
                    int computOffset = computOffset(top2 - top, i, i2, f);
                    if (!z) {
                        computOffset = -computOffset;
                    }
                    int i5 = computOffset;
                    i4 = top2;
                    i3 = i5;
                }
                view.setTranslationX((float) i3);
            }
        }
    }
}

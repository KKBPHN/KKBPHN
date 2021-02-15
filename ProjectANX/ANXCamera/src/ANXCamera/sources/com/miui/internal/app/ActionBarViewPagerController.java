package com.miui.internal.app;

import android.animation.ObjectAnimator;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import com.miui.internal.app.ActionBarImpl.TabImpl;
import com.miui.internal.util.DeviceHelper;
import com.miui.internal.widget.ActionBarContainer;
import com.miui.internal.widget.ActionBarOverlayLayout;
import java.util.ArrayList;
import java.util.Iterator;
import miui.R;
import miui.app.ActionBar.FragmentViewPagerChangeListener;
import miui.view.ViewPager;
import miui.view.ViewPager.LayoutParams;
import miui.view.ViewPager.OnPageChangeListener;
import miui.view.springback.SpringBackLayout;

public class ActionBarViewPagerController {
    /* access modifiers changed from: private */
    public ActionBarImpl mActionBar;
    private ObjectAnimator mActionMenuChangeAnimator;
    private ActionMenuChangeAnimatorObject mActionMenuChangeAnimatorObject;
    /* access modifiers changed from: private */
    public ArrayList mListeners;
    /* access modifiers changed from: private */
    public DynamicFragmentPagerAdapter mPagerAdapter;
    private TabListener mTabListener = new TabListener() {
        public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {
        }

        public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
            int count = ActionBarViewPagerController.this.mPagerAdapter.getCount();
            for (int i = 0; i < count; i++) {
                if (ActionBarViewPagerController.this.mPagerAdapter.getTabAt(i) == tab) {
                    ActionBarViewPagerController.this.mViewPager.setCurrentItem(i, true);
                    return;
                }
            }
        }

        public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {
        }
    };
    /* access modifiers changed from: private */
    public ViewPager mViewPager;
    private View mViewPagerDecor;

    class ActionMenuChangeAnimatorObject {
        private int mPosition;
        private boolean mShowActionMenu;

        ActionMenuChangeAnimatorObject() {
        }

        /* access modifiers changed from: 0000 */
        public void reset(int i, boolean z) {
            this.mPosition = i;
            this.mShowActionMenu = z;
        }

        public void setValue(float f) {
            if (ActionBarViewPagerController.this.mListeners != null) {
                Iterator it = ActionBarViewPagerController.this.mListeners.iterator();
                while (it.hasNext()) {
                    FragmentViewPagerChangeListener fragmentViewPagerChangeListener = (FragmentViewPagerChangeListener) it.next();
                    if (fragmentViewPagerChangeListener instanceof ActionBarContainer) {
                        int i = this.mPosition;
                        float f2 = 1.0f - f;
                        boolean z = this.mShowActionMenu;
                        fragmentViewPagerChangeListener.onPageScrolled(i, f2, z, !z);
                    }
                }
            }
        }
    }

    class ScrollStatus {
        private static final float THRESHOLD = 1.0E-4f;
        int mFromPos;
        private float mOffsetAtScroll;
        private int mPosAtScroll;
        boolean mScrollBegin;
        boolean mScrollEnd;
        int mToPos;

        private ScrollStatus() {
            this.mPosAtScroll = -1;
        }

        private void onScrollBegin(int i, float f) {
            boolean z = false;
            this.mScrollBegin = false;
            if (f > this.mOffsetAtScroll) {
                z = true;
            }
            this.mFromPos = z ? i : i + 1;
            if (z) {
                i++;
            }
            this.mToPos = i;
        }

        private void onScrollEnd() {
            this.mFromPos = this.mToPos;
            this.mPosAtScroll = -1;
            this.mOffsetAtScroll = 0.0f;
            this.mScrollEnd = true;
        }

        private void onScrollPositionChange(int i, float f) {
            this.mPosAtScroll = i;
            this.mOffsetAtScroll = f;
            this.mScrollBegin = true;
            this.mScrollEnd = false;
        }

        /* access modifiers changed from: 0000 */
        public void update(int i, float f) {
            if (f < THRESHOLD) {
                onScrollEnd();
            } else if (this.mPosAtScroll != i) {
                onScrollPositionChange(i, f);
            } else if (this.mScrollBegin) {
                onScrollBegin(i, f);
            }
        }
    }

    ActionBarViewPagerController(ActionBarImpl actionBarImpl, FragmentManager fragmentManager, boolean z) {
        this.mActionBar = actionBarImpl;
        ActionBarOverlayLayout actionBarOverlayLayout = this.mActionBar.getActionBarOverlayLayout();
        Context context = actionBarOverlayLayout.getContext();
        View findViewById = actionBarOverlayLayout.findViewById(R.id.view_pager);
        if (findViewById instanceof ViewPager) {
            this.mViewPager = (ViewPager) findViewById;
        } else {
            this.mViewPager = new ViewPager(context);
            this.mViewPager.setId(R.id.view_pager);
            SpringBackLayout springBackLayout = new SpringBackLayout(context);
            springBackLayout.setScrollOrientation(5);
            springBackLayout.addView(this.mViewPager, new LayoutParams());
            springBackLayout.setTarget(this.mViewPager);
            ((ViewGroup) actionBarOverlayLayout.findViewById(16908290)).addView(springBackLayout, new ViewGroup.LayoutParams(-1, -1));
        }
        this.mPagerAdapter = new DynamicFragmentPagerAdapter(context, fragmentManager, this.mViewPager);
        this.mViewPager.setInternalPageChangeListener(new OnPageChangeListener() {
            ScrollStatus mStatus = new ScrollStatus();

            public void onPageScrollStateChanged(int i) {
                if (ActionBarViewPagerController.this.mListeners != null) {
                    Iterator it = ActionBarViewPagerController.this.mListeners.iterator();
                    while (it.hasNext()) {
                        ((FragmentViewPagerChangeListener) it.next()).onPageScrollStateChanged(i);
                    }
                }
            }

            public void onPageScrolled(int i, float f, int i2) {
                this.mStatus.update(i, f);
                if (!this.mStatus.mScrollBegin && ActionBarViewPagerController.this.mListeners != null) {
                    boolean hasActionMenu = ActionBarViewPagerController.this.mPagerAdapter.hasActionMenu(this.mStatus.mFromPos);
                    boolean hasActionMenu2 = ActionBarViewPagerController.this.mPagerAdapter.hasActionMenu(this.mStatus.mToPos);
                    if (ActionBarViewPagerController.this.mPagerAdapter.isRTL()) {
                        i = ActionBarViewPagerController.this.mPagerAdapter.toIndexForRTL(i);
                        if (!this.mStatus.mScrollEnd) {
                            i--;
                            f = 1.0f - f;
                        }
                    }
                    Iterator it = ActionBarViewPagerController.this.mListeners.iterator();
                    while (it.hasNext()) {
                        ((FragmentViewPagerChangeListener) it.next()).onPageScrolled(i, f, hasActionMenu, hasActionMenu2);
                    }
                }
            }

            public void onPageSelected(int i) {
                int indexForRTL = ActionBarViewPagerController.this.mPagerAdapter.toIndexForRTL(i);
                ActionBarViewPagerController.this.mActionBar.setSelectedNavigationItem(indexForRTL);
                ActionBarViewPagerController.this.mPagerAdapter.setPrimaryItem(ActionBarViewPagerController.this.mViewPager, i, ActionBarViewPagerController.this.mPagerAdapter.getFragment(i, false, false));
                if (ActionBarViewPagerController.this.mListeners != null) {
                    Iterator it = ActionBarViewPagerController.this.mListeners.iterator();
                    while (it.hasNext()) {
                        ((FragmentViewPagerChangeListener) it.next()).onPageSelected(indexForRTL);
                    }
                }
            }
        });
        if (z && DeviceHelper.FEATURE_WHOLE_ANIM) {
            addOnFragmentViewPagerChangeListener(new ViewPagerScrollEffect(this.mViewPager, this.mPagerAdapter));
        }
    }

    /* access modifiers changed from: 0000 */
    public int addFragmentTab(String str, Tab tab, int i, Class cls, Bundle bundle, boolean z) {
        ((TabImpl) tab).setInternalTabListener(this.mTabListener);
        this.mActionBar.internalAddTab(tab, i);
        return this.mPagerAdapter.addFragment(str, i, cls, bundle, tab, z);
    }

    /* access modifiers changed from: 0000 */
    public int addFragmentTab(String str, Tab tab, Class cls, Bundle bundle, boolean z) {
        ((TabImpl) tab).setInternalTabListener(this.mTabListener);
        this.mActionBar.internalAddTab(tab);
        return this.mPagerAdapter.addFragment(str, cls, bundle, tab, z);
    }

    /* access modifiers changed from: 0000 */
    public void addOnFragmentViewPagerChangeListener(FragmentViewPagerChangeListener fragmentViewPagerChangeListener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList();
        }
        this.mListeners.add(fragmentViewPagerChangeListener);
    }

    /* access modifiers changed from: 0000 */
    public Fragment getFragmentAt(int i) {
        return this.mPagerAdapter.getFragment(i, true);
    }

    /* access modifiers changed from: 0000 */
    public int getFragmentTabCount() {
        return this.mPagerAdapter.getCount();
    }

    /* access modifiers changed from: 0000 */
    public int getViewPagerOffscreenPageLimit() {
        return this.mViewPager.getOffscreenPageLimit();
    }

    /* access modifiers changed from: 0000 */
    public void removeAllFragmentTab() {
        this.mActionBar.internalRemoveAllTabs();
        this.mPagerAdapter.removeAllFragment();
    }

    /* access modifiers changed from: 0000 */
    public void removeFragment(Fragment fragment) {
        int removeFragment = this.mPagerAdapter.removeFragment(fragment);
        if (removeFragment >= 0) {
            this.mActionBar.internalRemoveTabAt(removeFragment);
        }
    }

    /* access modifiers changed from: 0000 */
    public void removeFragmentAt(int i) {
        this.mPagerAdapter.removeFragmentAt(i);
        this.mActionBar.internalRemoveTabAt(i);
    }

    /* access modifiers changed from: 0000 */
    public void removeFragmentTab(Tab tab) {
        this.mActionBar.internalRemoveTab(tab);
        this.mPagerAdapter.removeFragment(tab);
    }

    /* access modifiers changed from: 0000 */
    public void removeFragmentTab(String str) {
        int findPositionByTag = this.mPagerAdapter.findPositionByTag(str);
        if (findPositionByTag >= 0) {
            removeFragmentAt(findPositionByTag);
        }
    }

    /* access modifiers changed from: 0000 */
    public void removeOnFragmentViewPagerChangeListener(FragmentViewPagerChangeListener fragmentViewPagerChangeListener) {
        ArrayList arrayList = this.mListeners;
        if (arrayList != null) {
            arrayList.remove(fragmentViewPagerChangeListener);
        }
    }

    /* access modifiers changed from: 0000 */
    public void setFragmentActionMenuAt(int i, boolean z) {
        this.mPagerAdapter.setFragmentActionMenuAt(i, z);
        if (i == this.mViewPager.getCurrentItem()) {
            if (this.mActionMenuChangeAnimatorObject == null) {
                this.mActionMenuChangeAnimatorObject = new ActionMenuChangeAnimatorObject();
                this.mActionMenuChangeAnimator = ObjectAnimator.ofFloat(this.mActionMenuChangeAnimatorObject, "Value", new float[]{0.0f, 1.0f});
                this.mActionMenuChangeAnimator.setDuration(DeviceHelper.FEATURE_WHOLE_ANIM ? (long) this.mViewPager.getContext().getResources().getInteger(17694720) : 0);
            }
            this.mActionMenuChangeAnimatorObject.reset(i, z);
            this.mActionMenuChangeAnimator.start();
        }
    }

    /* access modifiers changed from: 0000 */
    public void setViewPagerDecor(View view) {
        View view2 = this.mViewPagerDecor;
        if (view2 != null) {
            this.mViewPager.removeView(view2);
        }
        if (view != null) {
            this.mViewPagerDecor = view;
            LayoutParams layoutParams = new LayoutParams();
            layoutParams.isDecor = true;
            this.mViewPager.addView(this.mViewPagerDecor, -1, layoutParams);
        }
    }

    /* access modifiers changed from: 0000 */
    public void setViewPagerOffscreenPageLimit(int i) {
        this.mViewPager.setOffscreenPageLimit(i);
    }
}

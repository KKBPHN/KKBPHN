package miui.app;

import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

public abstract class ActionBar extends android.app.ActionBar {
    public static final boolean DEFAULT_SHOW_BLUR_BACKGROUND = false;
    public static final int DISPLAY_SHOW_ACTIONBAR_BLUR = 32768;
    public static final int DISPLAY_SHOW_SPLIT_ACTIONBAR_BLUR = 16384;
    public static final int STATE_COLLAPSE = 0;
    public static final int STATE_EXPAND = 1;

    public interface FragmentViewPagerChangeListener {
        void onPageScrollStateChanged(int i);

        void onPageScrolled(int i, float f, boolean z, boolean z2);

        void onPageSelected(int i);
    }

    public interface OnScrollListener {
        boolean onContentScrolled();

        void onFling(float f, int i);

        void onScroll(int i, float f);

        void onStartScroll();

        void onStopScroll();
    }

    public abstract int addFragmentTab(String str, Tab tab, int i, Class cls, Bundle bundle, boolean z);

    public abstract int addFragmentTab(String str, Tab tab, Class cls, Bundle bundle, boolean z);

    public abstract void addOnFragmentViewPagerChangeListener(FragmentViewPagerChangeListener fragmentViewPagerChangeListener);

    public abstract ActionBarTransitionListener getActionBarTransitionListener();

    public abstract View getEndView();

    public abstract int getExpandState();

    public abstract Fragment getFragmentAt(int i);

    public abstract int getFragmentTabCount();

    public abstract View getStartView();

    public abstract int getViewPagerOffscreenPageLimit();

    public abstract boolean isFragmentViewPagerMode();

    public abstract boolean isResizable();

    public abstract void removeAllFragmentTab();

    public abstract void removeFragmentTab(Tab tab);

    public abstract void removeFragmentTab(Fragment fragment);

    public abstract void removeFragmentTab(String str);

    public abstract void removeFragmentTabAt(int i);

    public abstract void removeOnFragmentViewPagerChangeListener(FragmentViewPagerChangeListener fragmentViewPagerChangeListener);

    public abstract void setActionBarTransitionListener(ActionBarTransitionListener actionBarTransitionListener);

    public abstract void setEndView(View view);

    public abstract void setExpandState(int i);

    public abstract void setExpandState(int i, boolean z);

    public abstract void setExpandTabTextAppearance(int i, int i2);

    public abstract void setFragmentActionMenuAt(int i, boolean z);

    public abstract void setFragmentViewPagerMode(Context context, FragmentManager fragmentManager);

    public abstract void setFragmentViewPagerMode(Context context, FragmentManager fragmentManager, boolean z);

    public abstract void setProgress(int i);

    public abstract void setProgressBarIndeterminate(boolean z);

    public abstract void setProgressBarIndeterminateVisibility(boolean z);

    public abstract void setProgressBarVisibility(boolean z);

    public abstract void setResizable(boolean z);

    public abstract void setSecondaryTabTextAppearance(int i, int i2);

    public abstract void setStartView(View view);

    public abstract void setTabBadgeVisibility(int i, boolean z);

    public abstract void setTabIconWithPosition(int i, int i2, int i3, int i4, int i5, int i6);

    public abstract void setTabIconWithPosition(int i, int i2, Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4);

    public abstract void setTabTextAppearance(int i, int i2);

    public abstract void setTabsMode(boolean z);

    public abstract void setViewPagerDecor(View view);

    public abstract void setViewPagerOffscreenPageLimit(int i);

    public abstract void showActionBarShadow(boolean z);

    public abstract void showSplitActionBar(boolean z, boolean z2);
}

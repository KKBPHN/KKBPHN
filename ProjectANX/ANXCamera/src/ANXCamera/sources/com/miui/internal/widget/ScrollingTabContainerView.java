package com.miui.internal.widget;

import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import com.miui.internal.R;
import com.miui.internal.util.DeviceHelper;
import com.miui.internal.view.ActionBarPolicy;
import miui.app.ActionBar.FragmentViewPagerChangeListener;
import miui.util.AttributeResolver;
import miui.util.ViewUtils;

public class ScrollingTabContainerView extends HorizontalScrollView implements OnItemClickListener, FragmentViewPagerChangeListener {
    public static final int MODE_COLLAPSE = 0;
    public static final int MODE_MOVABLE = 1;
    public static final int MODE_SECONDARY = 2;
    private boolean mAllowCollapse;
    private int mContentHeight;
    private final LayoutInflater mInflater;
    private int mLastSelectedPosition;
    int mMaxTabWidth;
    private Paint mPaint;
    private int mSelectedTabIndex;
    int mStackedTabMaxWidth;
    protected int mStyleMode;
    private TabClickListener mTabClickListener;
    /* access modifiers changed from: private */
    public TabViewResData mTabData;
    private Bitmap mTabIndicatorBitmap;
    private float mTabIndicatorPosition;
    /* access modifiers changed from: private */
    public LinearLayout mTabLayout;
    Runnable mTabSelector;
    private int[] mTabSizeStages;
    private Spinner mTabSpinner;
    private boolean mTranslucentIndicator;

    class TabAdapter extends BaseAdapter {
        private TabAdapter() {
        }

        public int getCount() {
            return ScrollingTabContainerView.this.mTabLayout.getChildCount();
        }

        public Object getItem(int i) {
            return ((TabView) ScrollingTabContainerView.this.mTabLayout.getChildAt(i)).getTab();
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                return ScrollingTabContainerView.this.createTabView((Tab) getItem(i), true);
            }
            ((TabView) view).bindTab((Tab) getItem(i));
            return view;
        }
    }

    class TabClickListener implements OnClickListener {
        private TabClickListener() {
        }

        public void onClick(View view) {
            ((TabView) view).getTab().select();
            int childCount = ScrollingTabContainerView.this.mTabLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = ScrollingTabContainerView.this.mTabLayout.getChildAt(i);
                childAt.setSelected(childAt == view);
            }
        }
    }

    public class TabView extends LinearLayout {
        /* access modifiers changed from: private */
        public ImageView mBadgeView;
        private View mCustomView;
        private ImageView mIconView;
        private ScrollingTabContainerView mParent;
        private Tab mTab;
        /* access modifiers changed from: private */
        public TextView mTextView;

        public TabView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        /* access modifiers changed from: 0000 */
        public void attach(ScrollingTabContainerView scrollingTabContainerView, Tab tab, boolean z) {
            this.mParent = scrollingTabContainerView;
            this.mTab = tab;
            if (z) {
                setGravity(8388627);
            }
            update();
        }

        public void bindTab(Tab tab) {
            this.mTab = tab;
            update();
        }

        public Tab getTab() {
            return this.mTab;
        }

        public TextView getTextView() {
            return this.mTextView;
        }

        public void update() {
            Tab tab = this.mTab;
            View customView = tab.getCustomView();
            if (customView == null) {
                View view = this.mCustomView;
                if (view != null) {
                    removeView(view);
                    this.mCustomView = null;
                }
                Context context = getContext();
                Drawable icon = tab.getIcon();
                CharSequence text = tab.getText();
                if (icon != null) {
                    if (this.mIconView == null) {
                        ImageView imageView = new ImageView(context);
                        LayoutParams layoutParams = new LayoutParams(-2, -2);
                        layoutParams.gravity = 16;
                        imageView.setLayoutParams(layoutParams);
                        addView(imageView, 0);
                        this.mIconView = imageView;
                    }
                    this.mIconView.setImageDrawable(icon);
                    this.mIconView.setVisibility(0);
                } else {
                    ImageView imageView2 = this.mIconView;
                    if (imageView2 != null) {
                        imageView2.setVisibility(8);
                        this.mIconView.setImageDrawable(null);
                    }
                }
                if (text != null) {
                    if (this.mTextView == null) {
                        ScrollingTabTextView scrollingTabTextView = new ScrollingTabTextView(context, null, this.mParent.mTabData.tabTextStyle);
                        scrollingTabTextView.setEllipsize(TruncateAt.END);
                        LayoutParams layoutParams2 = new LayoutParams(-2, -2);
                        layoutParams2.gravity = 16;
                        scrollingTabTextView.setLayoutParams(layoutParams2);
                        addView(scrollingTabTextView);
                        this.mTextView = scrollingTabTextView;
                    }
                    this.mTextView.setText(text);
                    this.mTextView.setVisibility(0);
                    if (this.mBadgeView == null) {
                        ImageView imageView3 = new ImageView(context);
                        imageView3.setImageDrawable(AttributeResolver.resolveDrawable(context, R.attr.actionBarTabBadgeIcon));
                        imageView3.setVisibility(8);
                        LayoutParams layoutParams3 = new LayoutParams((int) (((float) context.getResources().getDimensionPixelOffset(R.dimen.action_tab_badge_width)) * this.mParent.mTabData.iconZoom), (int) (((float) context.getResources().getDimensionPixelOffset(R.dimen.action_tab_badge_height)) * this.mParent.mTabData.iconZoom));
                        layoutParams3.gravity = 48;
                        Drawable background = getBackground();
                        if (background != null) {
                            int intrinsicHeight = background.getIntrinsicHeight();
                            int lineHeight = this.mTextView.getLineHeight();
                            if (intrinsicHeight > lineHeight) {
                                layoutParams3.topMargin = (intrinsicHeight - lineHeight) / 2;
                            }
                        }
                        imageView3.setLayoutParams(layoutParams3);
                        addView(imageView3);
                        this.mBadgeView = imageView3;
                    }
                } else {
                    TextView textView = this.mTextView;
                    if (textView != null) {
                        textView.setVisibility(8);
                        this.mTextView.setText(null);
                    }
                }
                ImageView imageView4 = this.mIconView;
                if (imageView4 != null) {
                    imageView4.setContentDescription(tab.getContentDescription());
                }
            } else if (this.mParent.mStyleMode == 0) {
                ViewParent parent = customView.getParent();
                if (parent != this) {
                    if (parent != null) {
                        ((ViewGroup) parent).removeView(customView);
                    }
                    addView(customView);
                }
                this.mCustomView = customView;
                TextView textView2 = this.mTextView;
                if (textView2 != null) {
                    textView2.setVisibility(8);
                }
                ImageView imageView5 = this.mIconView;
                if (imageView5 != null) {
                    imageView5.setVisibility(8);
                    this.mIconView.setImageDrawable(null);
                }
            }
        }
    }

    class TabViewResData {
        int barLayoutRes;
        float iconZoom;
        int marginHorizontal;
        int tabLayoutRes;
        int tabTextStyle;

        private TabViewResData() {
        }
    }

    public ScrollingTabContainerView(Context context) {
        this(context, 0);
    }

    public ScrollingTabContainerView(Context context, int i) {
        super(context);
        this.mPaint = new Paint();
        this.mLastSelectedPosition = -1;
        this.mStyleMode = 0;
        this.mTabData = null;
        this.mInflater = LayoutInflater.from(context);
        this.mStyleMode = i;
        ActionBarPolicy actionBarPolicy = ActionBarPolicy.get(context);
        this.mTabData = initTabViewData(context, actionBarPolicy, i);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(null, miui.R.styleable.ActionBar, 16843470, 0);
        Drawable drawable = obtainStyledAttributes.getDrawable(miui.R.styleable.ActionBar_tabIndicator);
        this.mTranslucentIndicator = obtainStyledAttributes.getBoolean(miui.R.styleable.ActionBar_translucentTabIndicator, true);
        this.mTabIndicatorBitmap = getTabIndicatorBitmap(drawable);
        obtainStyledAttributes.recycle();
        if (this.mTranslucentIndicator) {
            this.mPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        }
        setHorizontalScrollBarEnabled(false);
        this.mStackedTabMaxWidth = actionBarPolicy.getStackedTabMaxWidth();
        this.mTabLayout = (LinearLayout) this.mInflater.inflate(this.mTabData.barLayoutRes, this, false);
        addView(this.mTabLayout, new FrameLayout.LayoutParams(-2, -1));
        this.mTabSizeStages = new int[2];
        this.mTabSizeStages[0] = context.getResources().getDimensionPixelSize(R.dimen.action_bar_tab_expand_text_size_1);
        this.mTabSizeStages[1] = context.getResources().getDimensionPixelSize(R.dimen.action_bar_tab_expand_text_size_2);
    }

    private Spinner createSpinner() {
        Spinner spinner = new Spinner(getContext(), null, 16843479);
        spinner.setLayoutParams(new LayoutParams(-2, -1));
        return spinner;
    }

    /* access modifiers changed from: private */
    public TabView createTabView(Tab tab, boolean z) {
        TabView tabView = (TabView) this.mInflater.inflate(this.mTabData.tabLayoutRes, this.mTabLayout, false);
        tabView.attach(this, tab, z);
        if (z) {
            tabView.setBackground(null);
            tabView.setLayoutParams(new LayoutParams(-1, this.mContentHeight));
        } else {
            tabView.setFocusable(true);
            if (this.mTabClickListener == null) {
                this.mTabClickListener = new TabClickListener();
            }
            tabView.setOnClickListener(this.mTabClickListener);
        }
        if (this.mTabLayout.getChildCount() != 0) {
            ((LayoutParams) tabView.getLayoutParams()).setMarginStart(this.mTabData.marginHorizontal);
        }
        return tabView;
    }

    private Bitmap getTabIndicatorBitmap(Drawable drawable) {
        int i;
        Config config;
        int i2;
        if (drawable == null) {
            return null;
        }
        if (this.mTranslucentIndicator) {
            i = drawable.getIntrinsicWidth();
            i2 = drawable.getIntrinsicHeight();
            config = Config.ALPHA_8;
        } else {
            i = drawable.getIntrinsicWidth();
            i2 = drawable.getIntrinsicHeight();
            config = Config.ARGB_8888;
        }
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, config);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return createBitmap;
    }

    private TabViewResData initTabViewData(Context context, ActionBarPolicy actionBarPolicy, int i) {
        int i2;
        Resources resources;
        TabViewResData tabViewResData = new TabViewResData();
        if (i != 0) {
            if (i == 1) {
                tabViewResData.barLayoutRes = R.layout.action_bar_tabbar_expand;
                tabViewResData.tabLayoutRes = R.layout.action_bar_tab_expand;
                tabViewResData.tabTextStyle = miui.R.attr.actionBarTabTextExpandStyle;
                tabViewResData.iconZoom = 1.0f;
                resources = context.getResources();
                i2 = R.dimen.action_bar_tab_expand_margin;
            } else if (i == 2) {
                tabViewResData.barLayoutRes = R.layout.action_bar_tabbar_secondary;
                tabViewResData.tabLayoutRes = R.layout.action_bar_tab_secondary;
                tabViewResData.tabTextStyle = miui.R.attr.actionBarTabTextSecondaryStyle;
                tabViewResData.iconZoom = 0.78f;
                resources = context.getResources();
                i2 = R.dimen.action_bar_tab_secondary_margin;
            } else {
                throw new RuntimeException("Unsupport mode in ScrollingTabContainerView");
            }
            tabViewResData.marginHorizontal = resources.getDimensionPixelOffset(i2);
            setContentHeight(-2);
        } else {
            tabViewResData.barLayoutRes = R.layout.action_bar_tabbar;
            tabViewResData.tabLayoutRes = R.layout.action_bar_tab;
            tabViewResData.tabTextStyle = 16843509;
            tabViewResData.iconZoom = 0.74f;
            tabViewResData.marginHorizontal = 0;
            setContentHeight(actionBarPolicy.getTabContainerHeight());
        }
        return tabViewResData;
    }

    private boolean isCollapsed() {
        Spinner spinner = this.mTabSpinner;
        return spinner != null && spinner.getParent() == this;
    }

    private void measureTabViewSizeStage2(int[] iArr, int i, int i2) {
        if (this.mStyleMode == 1) {
            for (int i3 = 0; i3 < iArr.length && this.mTabLayout.getMeasuredWidth() > getMeasuredWidth(); i3++) {
                int i4 = iArr[i3];
                for (int i5 = 0; i5 < this.mTabLayout.getChildCount(); i5++) {
                    TextView textView = ((TabView) this.mTabLayout.getChildAt(i5)).getTextView();
                    if (textView != null) {
                        textView.setTextSize(0, (float) i4);
                    }
                }
                this.mTabLayout.measure(i, i2);
            }
        }
    }

    private void performCollapse() {
        if (!isCollapsed()) {
            if (this.mTabSpinner == null) {
                this.mTabSpinner = createSpinner();
            }
            removeView(this.mTabLayout);
            addView(this.mTabSpinner, new ViewGroup.LayoutParams(-2, -1));
            if (this.mTabSpinner.getAdapter() == null) {
                this.mTabSpinner.setAdapter(new TabAdapter());
            }
            Runnable runnable = this.mTabSelector;
            if (runnable != null) {
                removeCallbacks(runnable);
                this.mTabSelector = null;
            }
            this.mTabSpinner.setSelection(this.mSelectedTabIndex);
        }
    }

    private boolean performExpand() {
        if (!isCollapsed()) {
            return false;
        }
        removeView(this.mTabSpinner);
        addView(this.mTabLayout, new ViewGroup.LayoutParams(-2, -1));
        setTabSelected(this.mTabSpinner.getSelectedItemPosition());
        return false;
    }

    public void addTab(Tab tab, int i, boolean z) {
        TabView createTabView = createTabView(tab, false);
        this.mTabLayout.addView(createTabView, i);
        Spinner spinner = this.mTabSpinner;
        if (spinner != null) {
            ((TabAdapter) spinner.getAdapter()).notifyDataSetChanged();
        }
        if (z) {
            createTabView.setSelected(true);
            this.mLastSelectedPosition = this.mTabLayout.getChildCount() - 1;
        }
        if (this.mAllowCollapse) {
            requestLayout();
        }
    }

    public void addTab(Tab tab, boolean z) {
        TabView createTabView = createTabView(tab, false);
        this.mTabLayout.addView(createTabView);
        Spinner spinner = this.mTabSpinner;
        if (spinner != null) {
            ((TabAdapter) spinner.getAdapter()).notifyDataSetChanged();
        }
        if (z) {
            createTabView.setSelected(true);
            this.mLastSelectedPosition = this.mTabLayout.getChildCount() - 1;
        }
        if (this.mAllowCollapse) {
            requestLayout();
        }
    }

    public void animateToTab(int i) {
        final View childAt = this.mTabLayout.getChildAt(i);
        Runnable runnable = this.mTabSelector;
        if (runnable != null) {
            removeCallbacks(runnable);
        }
        this.mTabSelector = new Runnable() {
            public void run() {
                int left = childAt.getLeft() - ((ScrollingTabContainerView.this.getWidth() - childAt.getWidth()) / 2);
                if (DeviceHelper.FEATURE_WHOLE_ANIM) {
                    ScrollingTabContainerView.this.smoothScrollTo(left, 0);
                } else {
                    ScrollingTabContainerView.this.scrollTo(left, 0);
                }
                ScrollingTabContainerView.this.mTabSelector = null;
            }
        };
        post(this.mTabSelector);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        Bitmap bitmap = this.mTabIndicatorBitmap;
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, this.mTabIndicatorPosition, (float) (getHeight() - this.mTabIndicatorBitmap.getHeight()), this.mPaint);
        }
    }

    public float getTabIndicatorPosition() {
        return this.mTabIndicatorPosition;
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Runnable runnable = this.mTabSelector;
        if (runnable != null) {
            post(runnable);
        }
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        ActionBarPolicy actionBarPolicy = ActionBarPolicy.get(getContext());
        setContentHeight(actionBarPolicy.getTabContainerHeight());
        this.mStackedTabMaxWidth = actionBarPolicy.getStackedTabMaxWidth();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Runnable runnable = this.mTabSelector;
        if (runnable != null) {
            removeCallbacks(runnable);
        }
    }

    public void onItemClick(AdapterView adapterView, View view, int i, long j) {
        ((TabView) view).getTab().select();
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mTabLayout.getChildAt(this.mSelectedTabIndex) != null) {
            setTabIndicatorPosition(this.mSelectedTabIndex);
        }
    }

    public void onMeasure(int i, int i2) {
        int i3;
        float f;
        float f2;
        int mode = MeasureSpec.getMode(i);
        boolean z = true;
        boolean z2 = mode == 1073741824;
        setFillViewport(z2);
        int childCount = this.mTabLayout.getChildCount();
        if (childCount <= 1 || !(mode == 1073741824 || mode == Integer.MIN_VALUE)) {
            i3 = -1;
        } else {
            if (childCount > 2) {
                f2 = (float) MeasureSpec.getSize(i);
                f = 0.4f;
            } else {
                f2 = (float) MeasureSpec.getSize(i);
                f = 0.6f;
            }
            this.mMaxTabWidth = (int) (f2 * f);
            i3 = Math.min(this.mMaxTabWidth, this.mStackedTabMaxWidth);
        }
        this.mMaxTabWidth = i3;
        int i4 = this.mContentHeight;
        if (i4 != -2) {
            i2 = MeasureSpec.makeMeasureSpec(i4, 1073741824);
        }
        if (z2 || !this.mAllowCollapse) {
            z = false;
        }
        if (z) {
            this.mTabLayout.measure(0, i2);
            if (this.mTabLayout.getMeasuredWidth() > MeasureSpec.getSize(i)) {
                performCollapse();
                int measuredWidth = getMeasuredWidth();
                super.onMeasure(i, i2);
                int measuredWidth2 = getMeasuredWidth();
                if (z2 && measuredWidth != measuredWidth2) {
                    setTabSelected(this.mSelectedTabIndex);
                }
                measureTabViewSizeStage2(this.mTabSizeStages, 0, i2);
            }
        }
        performExpand();
        int measuredWidth3 = getMeasuredWidth();
        super.onMeasure(i, i2);
        int measuredWidth22 = getMeasuredWidth();
        setTabSelected(this.mSelectedTabIndex);
        measureTabViewSizeStage2(this.mTabSizeStages, 0, i2);
    }

    public void onPageScrollStateChanged(int i) {
    }

    public void onPageScrolled(int i, float f, boolean z, boolean z2) {
        setTabIndicatorPosition(i, f);
    }

    public void onPageSelected(int i) {
        TabView tabView = (TabView) this.mTabLayout.getChildAt(i);
        if (tabView != null) {
            tabView.sendAccessibilityEvent(4);
        }
        setTabIndicatorPosition(i);
        int i2 = this.mLastSelectedPosition;
        if (i2 != -1) {
            boolean z = true;
            if (Math.abs(i2 - i) == 1) {
                ScrollingTabTextView scrollingTabTextView = (ScrollingTabTextView) ((TabView) this.mTabLayout.getChildAt(this.mLastSelectedPosition)).getTextView();
                ScrollingTabTextView scrollingTabTextView2 = (ScrollingTabTextView) tabView.getTextView();
                if (!(scrollingTabTextView == null || scrollingTabTextView2 == null)) {
                    if (ViewUtils.isLayoutRtl(this)) {
                    }
                    z = false;
                    scrollingTabTextView.startScrollAnimation(z);
                    scrollingTabTextView2.startScrollAnimation(z);
                }
            }
        }
        this.mLastSelectedPosition = i;
    }

    public void removeAllTabs() {
        this.mTabLayout.removeAllViews();
        Spinner spinner = this.mTabSpinner;
        if (spinner != null) {
            ((TabAdapter) spinner.getAdapter()).notifyDataSetChanged();
        }
        if (this.mAllowCollapse) {
            requestLayout();
        }
    }

    public void removeTabAt(int i) {
        this.mTabLayout.removeViewAt(i);
        Spinner spinner = this.mTabSpinner;
        if (spinner != null) {
            ((TabAdapter) spinner.getAdapter()).notifyDataSetChanged();
        }
        if (this.mAllowCollapse) {
            requestLayout();
        }
    }

    public void setAllowCollapse(boolean z) {
        this.mAllowCollapse = z;
    }

    public void setBadgeVisibility(int i, boolean z) {
        if (i <= this.mTabLayout.getChildCount() - 1) {
            ImageView access$300 = ((TabView) this.mTabLayout.getChildAt(i)).mBadgeView;
            if (access$300 != null) {
                access$300.setVisibility(z ? 0 : 8);
            }
        }
    }

    public void setContentHeight(int i) {
        this.mContentHeight = i;
        requestLayout();
    }

    public void setEmbeded(boolean z) {
        setHorizontalFadingEdgeEnabled(true);
    }

    public void setTabIconWithPosition(int i, int i2, Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (i <= this.mTabLayout.getChildCount() - 1) {
            TextView access$400 = ((TabView) this.mTabLayout.getChildAt(i)).mTextView;
            if (access$400 != null) {
                access$400.setCompoundDrawablePadding(i2);
                if (ViewUtils.isLayoutRtl(this)) {
                    access$400.setCompoundDrawablesWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
                } else {
                    access$400.setCompoundDrawablesWithIntrinsicBounds(drawable3, drawable2, drawable, drawable4);
                }
            }
        }
    }

    public void setTabIndicatorPosition(int i) {
        setTabIndicatorPosition(i, 0.0f);
    }

    public void setTabIndicatorPosition(int i, float f) {
        if (this.mTabIndicatorBitmap != null) {
            View childAt = this.mTabLayout.getChildAt(i);
            View childAt2 = this.mTabLayout.getChildAt(i + 1);
            this.mTabIndicatorPosition = ((float) (childAt.getLeft() + ((childAt.getWidth() - this.mTabIndicatorBitmap.getWidth()) / 2))) + ((childAt2 == null ? (float) childAt.getWidth() : ((float) (childAt.getWidth() + childAt2.getWidth())) / 2.0f) * f);
            invalidate();
        }
    }

    public void setTabSelected(int i) {
        this.mSelectedTabIndex = i;
        int childCount = this.mTabLayout.getChildCount();
        int i2 = 0;
        while (i2 < childCount) {
            View childAt = this.mTabLayout.getChildAt(i2);
            boolean z = i2 == i;
            childAt.setSelected(z);
            if (z) {
                animateToTab(i);
            }
            i2++;
        }
    }

    public void setTextAppearance(int i, int i2) {
        if (i >= 0 && i <= this.mTabLayout.getChildCount() - 1) {
            TextView access$400 = ((TabView) this.mTabLayout.getChildAt(i)).mTextView;
            if (access$400 != null) {
                access$400.setTextAppearance(access$400.getContext(), i2);
            }
        }
    }

    public void updateTab(int i) {
        ((TabView) this.mTabLayout.getChildAt(i)).update();
        Spinner spinner = this.mTabSpinner;
        if (spinner != null) {
            ((TabAdapter) spinner.getAdapter()).notifyDataSetChanged();
        }
        if (this.mAllowCollapse) {
            requestLayout();
        }
    }
}

package miui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.miui.internal.R;
import java.util.ArrayList;
import java.util.List;
import miui.animation.Folme;
import miui.animation.base.AnimConfig;
import miui.animation.controller.AnimState;
import miui.animation.property.ViewProperty;
import miui.util.AttributeResolver;
import miui.util.HapticFeedbackUtil;
import miui.view.MiuiHapticFeedbackConstants;

public class FilterSortView extends RelativeLayout {
    /* access modifiers changed from: private */
    public TabView mBackgroundTabView;
    private LinearLayout mContentLayout;
    private int mFilteredId;
    private OnFilteredListener mOnFilteredListener;
    private List mTabViewChildIds;

    public class TabView extends LinearLayout {
        /* access modifiers changed from: private */
        public ImageView mArrow;
        /* access modifiers changed from: private */
        public boolean mDescending;
        /* access modifiers changed from: private */
        public boolean mFiltered;
        private int mIndicatorVisibility;
        private OnFilteredListener mOnFilteredListener;
        /* access modifiers changed from: private */
        public TextView mTextView;
        private float mWeight;

        interface OnFilteredListener {
            void onFilteredChangedListener(TabView tabView, boolean z);
        }

        public TabView(Context context) {
            this(context, null);
        }

        public TabView(Context context, AttributeSet attributeSet) {
            this(context, attributeSet, 0);
        }

        public TabView(Context context, AttributeSet attributeSet, int i) {
            super(context, attributeSet, i);
            LayoutInflater.from(context).inflate(R.layout.filter_sort_tab_view, this, true);
            this.mTextView = (TextView) findViewById(16908308);
            this.mArrow = (ImageView) findViewById(R.id.arrow);
            if (attributeSet != null) {
                TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.FilterSortView, i, 0);
                String string = obtainStyledAttributes.getString(R.styleable.FilterSortView_android_text);
                boolean z = obtainStyledAttributes.getBoolean(R.styleable.FilterSortView_descending, true);
                this.mIndicatorVisibility = obtainStyledAttributes.getInt(R.styleable.FilterSortView_indicatorVisibility, 0);
                this.mWeight = obtainStyledAttributes.getFloat(R.styleable.FilterSortView_android_layout_weight, 0.0f);
                obtainStyledAttributes.recycle();
                initView(string, z);
            }
            this.mArrow.setVisibility(this.mIndicatorVisibility);
            if (getId() == -1) {
                setId(LinearLayout.generateViewId());
            }
        }

        /* access modifiers changed from: private */
        public void initView(CharSequence charSequence, boolean z) {
            setGravity(17);
            if (getBackground() == null) {
                setBackground(parseBackground());
            }
            this.mTextView.setText(charSequence);
            setDescending(z);
        }

        private Drawable parseBackground() {
            return getResources().getDrawable(R.drawable.filter_sort_tab_view_bg_normal);
        }

        /* access modifiers changed from: private */
        public void setDescending(boolean z) {
            this.mDescending = z;
            this.mArrow.setRotationX(z ? 0.0f : 180.0f);
        }

        /* access modifiers changed from: private */
        public void setFiltered(boolean z) {
            ViewGroup viewGroup = (ViewGroup) getParent();
            if (z && viewGroup != null) {
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    TabView tabView = (TabView) viewGroup.getChildAt(i);
                    if (tabView != this && tabView.mFiltered) {
                        tabView.setFiltered(false);
                    }
                }
            }
            this.mFiltered = z;
            this.mTextView.setSelected(z);
            this.mArrow.setSelected(z);
            setSelected(z);
            OnFilteredListener onFilteredListener = this.mOnFilteredListener;
            if (onFilteredListener != null) {
                onFilteredListener.onFilteredChangedListener(this, z);
            }
        }

        /* access modifiers changed from: private */
        public void setOnFilteredListener(OnFilteredListener onFilteredListener) {
            this.mOnFilteredListener = onFilteredListener;
        }

        public float getWeight() {
            return this.mWeight;
        }

        public boolean isDescending() {
            return this.mDescending;
        }

        public void setIndicatorVisibility(int i) {
            this.mArrow.setVisibility(i);
        }

        public void setOnClickListener(final OnClickListener onClickListener) {
            super.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (TabView.this.mFiltered) {
                        TabView tabView = TabView.this;
                        tabView.setDescending(true ^ tabView.mDescending);
                    } else {
                        TabView.this.setFiltered(true);
                    }
                    onClickListener.onClick(view);
                    if (HapticFeedbackUtil.isSupportLinearMotorVibrate(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_MESH_NORMAL)) {
                        view.performHapticFeedback(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_MESH_NORMAL);
                    }
                }
            });
        }

        public void setWeight(float f) {
            this.mWeight = f;
        }
    }

    public FilterSortView(Context context) {
        this(context, null);
    }

    public FilterSortView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public FilterSortView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mTabViewChildIds = new ArrayList();
        this.mFilteredId = -1;
        this.mOnFilteredListener = new OnFilteredListener() {
            public void onFilteredChangedListener(TabView tabView, boolean z) {
                if (z && FilterSortView.this.mBackgroundTabView.getVisibility() == 0) {
                    Folme.useAt(FilterSortView.this.mBackgroundTabView).state().to(new AnimState("target").add(ViewProperty.X, tabView.getX() + ((float) FilterSortView.this.getPaddingLeft()), new long[0]).add(ViewProperty.Y, tabView.getY() + ((float) FilterSortView.this.getPaddingTop()), new long[0]).add(ViewProperty.WIDTH, tabView.getWidth(), new long[0]).add(ViewProperty.HEIGHT, tabView.getHeight(), new long[0]), new AnimConfig[0]);
                }
            }
        };
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.filter_sort_view_padding);
        setPadding(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
        setBackground(AttributeResolver.resolveDrawable(getContext(), R.attr.filterSortViewBackground));
        this.mContentLayout = new LinearLayout(context);
        this.mContentLayout.setOrientation(0);
        LayoutParams layoutParams = new LayoutParams(-1, -2);
        layoutParams.addRule(13);
        addView(this.mContentLayout, layoutParams);
        LayoutParams layoutParams2 = new LayoutParams(-1, -2);
        layoutParams2.addRule(15);
        this.mBackgroundTabView = new TabView(context);
        this.mBackgroundTabView.mArrow.setVisibility(8);
        this.mBackgroundTabView.mTextView.setVisibility(8);
        this.mBackgroundTabView.setBackground(parseChildBackground());
        this.mBackgroundTabView.setVisibility(4);
        addView(this.mBackgroundTabView, 0, layoutParams2);
    }

    private Drawable parseChildBackground() {
        Drawable resolveDrawable = AttributeResolver.resolveDrawable(getContext(), R.attr.filterSortTabViewBackground);
        return resolveDrawable == null ? getResources().getDrawable(R.drawable.filter_sort_tab_view_bg_light) : resolveDrawable;
    }

    private void updateFiltered(TabView tabView) {
        if (this.mBackgroundTabView.getVisibility() != 0) {
            this.mBackgroundTabView.setVisibility(0);
        }
        LayoutParams layoutParams = (LayoutParams) this.mBackgroundTabView.getLayoutParams();
        layoutParams.width = tabView.getWidth();
        layoutParams.height = tabView.getHeight();
        layoutParams.addRule(15);
        this.mBackgroundTabView.setLayoutParams(layoutParams);
        this.mBackgroundTabView.setX(tabView.getX() + ((float) getPaddingLeft()));
        this.mBackgroundTabView.setY(tabView.getY() + ((float) getPaddingTop()));
    }

    public TabView addTab(CharSequence charSequence) {
        return addTab(charSequence, true);
    }

    public TabView addTab(CharSequence charSequence, boolean z) {
        TabView tabView = new TabView(getContext());
        tabView.setOnFilteredListener(this.mOnFilteredListener);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.weight = 1.0f;
        layoutParams.gravity = 16;
        this.mContentLayout.addView(tabView, layoutParams);
        this.mTabViewChildIds.add(Integer.valueOf(tabView.getId()));
        tabView.initView(charSequence, z);
        return tabView;
    }

    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        if (!(view instanceof TabView) || view.getId() == this.mBackgroundTabView.getId()) {
            super.addView(view, i, layoutParams);
            return;
        }
        TabView tabView = (TabView) view;
        tabView.setOnFilteredListener(this.mOnFilteredListener);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams2.weight = tabView.getWeight();
        layoutParams2.gravity = 16;
        this.mContentLayout.addView(tabView, layoutParams2);
        this.mTabViewChildIds.add(Integer.valueOf(tabView.getId()));
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        int i5 = this.mFilteredId;
        if (i5 != -1 && z) {
            TabView tabView = (TabView) findViewById(i5);
            if (tabView != null) {
                updateFiltered(tabView);
            }
        }
    }

    public void setFilteredTab(TabView tabView) {
        this.mFilteredId = tabView.getId();
        tabView.setFiltered(true);
    }

    public void setTabIncatorVisibility(int i) {
        for (int i2 = 0; i2 < this.mContentLayout.getChildCount(); i2++) {
            ((TabView) this.mContentLayout.getChildAt(i2)).setIndicatorVisibility(i);
        }
    }
}

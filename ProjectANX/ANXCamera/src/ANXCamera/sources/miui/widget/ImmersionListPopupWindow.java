package miui.widget;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import com.miui.internal.R;
import com.miui.internal.util.DeviceHelper;
import com.miui.internal.variable.Android_Widget_PopupWindow_class;
import com.miui.internal.variable.Android_Widget_PopupWindow_class.Factory;
import com.miui.internal.widget.ImmersionListPopupWindowHelper;
import com.miui.internal.widget.RoundFrameLayout;
import miui.util.AttributeResolver;
import miui.util.HapticFeedbackUtil;
import miui.util.ViewUtils;
import miui.view.MiuiHapticFeedbackConstants;
import miui.view.inputmethod.InputMethodHelper;

public class ImmersionListPopupWindow extends PopupWindow {
    private static final float DIM = 0.3f;
    private static final float OFFSET_X = 12.0f;
    private static final float OFFSET_Y = 8.0f;
    private static final String TAG = "ImmersionListPopupWindow";
    /* access modifiers changed from: private */
    public ListAdapter mAdapter;
    protected final Rect mBackgroundPadding;
    protected View mContentView;
    private int mContentWidth;
    private Context mContext;
    protected int mElevation;
    /* access modifiers changed from: private */
    public boolean mHasContentWidth;
    private boolean mIsRtl;
    /* access modifiers changed from: private */
    public ListView mListView;
    private int mMaxAllowedWidth;
    private int mMinAllowedWidth;
    private DataSetObserver mObserver = new DataSetObserver() {
        public void onChanged() {
            ImmersionListPopupWindow.this.mHasContentWidth = false;
            if (ImmersionListPopupWindow.this.isShowing()) {
                ImmersionListPopupWindow immersionListPopupWindow = ImmersionListPopupWindow.this;
                immersionListPopupWindow.update(immersionListPopupWindow.computePopupContentWidth(), ImmersionListPopupWindow.this.getHeight());
            }
        }
    };
    private final int mOffsetX;
    private final int mOffsetY;
    /* access modifiers changed from: private */
    public OnDismissListener mOnDismissListener;
    /* access modifiers changed from: private */
    public OnItemClickListener mOnItemClickListener;
    protected FrameLayout mRootView;
    private WindowManager mWindowManager;

    public ImmersionListPopupWindow(Context context) {
        super(context);
        this.mContext = context;
        Resources resources = context.getResources();
        this.mMaxAllowedWidth = resources.getDimensionPixelSize(R.dimen.list_menu_dialog_maximum_width);
        this.mMinAllowedWidth = resources.getDimensionPixelSize(R.dimen.list_menu_dialog_minimum_width);
        float f = resources.getDisplayMetrics().density;
        this.mOffsetX = 12;
        this.mOffsetY = (int) (f * OFFSET_Y);
        this.mBackgroundPadding = new Rect();
        setFocusable(true);
        this.mRootView = new RoundFrameLayout(context);
        this.mRootView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ImmersionListPopupWindow.this.dismiss();
            }
        });
        prepareContentView(context);
        Android_Widget_PopupWindow_class android_Widget_PopupWindow_class = Factory.getInstance().get();
        android_Widget_PopupWindow_class.setLayoutInScreenEnabled(this, true);
        android_Widget_PopupWindow_class.setLayoutInsetDecor(this, true);
        setAnimationStyle(DeviceHelper.FEATURE_WHOLE_ANIM ? R.style.Animation_PopupWindow_ImmersionMenu : 0);
        this.mElevation = ImmersionListPopupWindowHelper.getPopupElevation(this.mContext);
        super.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                if (ImmersionListPopupWindow.this.mOnDismissListener != null) {
                    ImmersionListPopupWindow.this.mOnDismissListener.onDismiss();
                }
            }
        });
    }

    private WindowManager getWindowManager() {
        if (this.mWindowManager == null) {
            this.mWindowManager = (WindowManager) this.mContext.getSystemService("window");
        }
        return this.mWindowManager;
    }

    private static int measureIndividualMenuWidth(ListAdapter listAdapter, ViewGroup viewGroup, Context context, int i) {
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
        int makeMeasureSpec2 = MeasureSpec.makeMeasureSpec(0, 0);
        int count = listAdapter.getCount();
        ViewGroup viewGroup2 = viewGroup;
        int i2 = 0;
        int i3 = 0;
        View view = null;
        for (int i4 = 0; i4 < count; i4++) {
            int itemViewType = listAdapter.getItemViewType(i4);
            if (itemViewType != i3) {
                view = null;
                i3 = itemViewType;
            }
            if (viewGroup2 == null) {
                viewGroup2 = new FrameLayout(context);
            }
            view = listAdapter.getView(i4, view, viewGroup2);
            view.measure(makeMeasureSpec, makeMeasureSpec2);
            int measuredWidth = view.getMeasuredWidth();
            if (measuredWidth >= i) {
                return i;
            }
            if (measuredWidth > i2) {
                i2 = measuredWidth;
            }
        }
        return i2;
    }

    private void showWithAnchor(View view) {
        this.mIsRtl = ViewUtils.isLayoutRtl(view);
        int i = -view.getHeight();
        Rect rect = this.mBackgroundPadding;
        showAsDropDown(view, this.mIsRtl ? 0 - (rect.left - this.mOffsetX) : (rect.right - this.mOffsetX) + 0, (i - rect.top) + this.mOffsetY, 8388661);
        if (HapticFeedbackUtil.isSupportLinearMotorVibrate(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_POPUP_NORMAL)) {
            view.performHapticFeedback(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_POPUP_NORMAL);
        }
    }

    /* access modifiers changed from: protected */
    public int computePopupContentWidth() {
        if (!this.mHasContentWidth) {
            this.mContentWidth = measureIndividualMenuWidth(this.mAdapter, null, this.mContext, this.mMaxAllowedWidth);
            this.mHasContentWidth = true;
        }
        int max = Math.max(this.mContentWidth, this.mMinAllowedWidth);
        Rect rect = this.mBackgroundPadding;
        return max + rect.left + rect.right;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public LayoutAnimationController createDefaultFadeInAnimation() {
        return null;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public LayoutAnimationController createDefaultFadeOutAnimation() {
        return null;
    }

    public void dismiss(boolean z) {
        dismiss();
    }

    public void fastShow(View view, ViewGroup viewGroup) {
        setWidth(computePopupContentWidth());
        showWithAnchor(view);
        this.mContentView.forceLayout();
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public Animator getBackgroundAnimator(LayoutAnimationController layoutAnimationController, boolean z) {
        return null;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public Drawable getBlurBackground(Context context, View view) {
        return null;
    }

    public View getContentView() {
        return this.mContentView;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public void installHeaderView(View view, ViewGroup viewGroup) {
    }

    /* access modifiers changed from: protected */
    public void prepareContentView(Context context) {
        Drawable resolveDrawable = AttributeResolver.resolveDrawable(this.mContext, miui.R.attr.immersionWindowBackground);
        if (resolveDrawable != null) {
            resolveDrawable.getPadding(this.mBackgroundPadding);
            this.mRootView.setBackground(resolveDrawable);
        }
        setBackgroundDrawable(new ColorDrawable(0));
        setPopupWindowContentView(this.mRootView);
    }

    public boolean prepareShow(View view, ViewGroup viewGroup) {
        String str;
        String str2 = TAG;
        if (view == null) {
            str = "show: anchor is null";
        } else {
            if (this.mContentView == null) {
                this.mContentView = new ListView(this.mContext);
                this.mContentView.setId(16908298);
            }
            if (!(this.mRootView.getChildCount() == 1 && this.mRootView.getChildAt(0) == this.mContentView)) {
                this.mRootView.removeAllViews();
                this.mRootView.addView(this.mContentView);
                LayoutParams layoutParams = (LayoutParams) this.mContentView.getLayoutParams();
                layoutParams.width = -1;
                layoutParams.height = -2;
                layoutParams.gravity = 16;
            }
            this.mRootView.setElevation((float) this.mElevation);
            setElevation((float) this.mElevation);
            ImmersionListPopupWindowHelper.setPopupShadowAlpha(this.mRootView);
            this.mListView = (ListView) this.mContentView.findViewById(16908298);
            ListView listView = this.mListView;
            if (listView == null) {
                str = "list not found";
            } else {
                listView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView adapterView, View view, int i, long j) {
                        int headerViewsCount = i - ImmersionListPopupWindow.this.mListView.getHeaderViewsCount();
                        if (ImmersionListPopupWindow.this.mOnItemClickListener != null && headerViewsCount >= 0 && headerViewsCount < ImmersionListPopupWindow.this.mAdapter.getCount()) {
                            ImmersionListPopupWindow.this.mOnItemClickListener.onItemClick(adapterView, view, headerViewsCount, j);
                        }
                    }
                });
                this.mListView.setAdapter(this.mAdapter);
                setWidth(computePopupContentWidth());
                InputMethodHelper.getInstance().getManager().hideSoftInputFromWindow(view.getWindowToken(), 0);
                return true;
            }
        }
        Log.e(str2, str);
        return false;
    }

    public void setAdapter(ListAdapter listAdapter) {
        ListAdapter listAdapter2 = this.mAdapter;
        if (listAdapter2 != null) {
            listAdapter2.unregisterDataSetObserver(this.mObserver);
        }
        this.mAdapter = listAdapter;
        ListAdapter listAdapter3 = this.mAdapter;
        if (listAdapter3 != null) {
            listAdapter3.registerDataSetObserver(this.mObserver);
        }
    }

    public void setContentView(View view) {
        this.mContentView = view;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    /* access modifiers changed from: protected */
    public void setPopupWindowContentView(View view) {
        super.setContentView(view);
    }

    public void show(View view, ViewGroup viewGroup) {
        if (prepareShow(view, viewGroup)) {
            showWithAnchor(view);
            ImmersionListPopupWindowHelper.changeWindowBackground(this.mRootView.getRootView(), DIM);
        }
    }
}

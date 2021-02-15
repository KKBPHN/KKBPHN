package miui.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import com.miui.internal.R;
import com.miui.internal.util.DeviceHelper;
import com.miui.internal.variable.Android_Widget_PopupWindow_class;
import com.miui.internal.variable.Android_Widget_PopupWindow_class.Factory;
import com.miui.internal.widget.DropDownPopupWindowHelper;
import miui.util.AttributeResolver;
import miui.util.HapticFeedbackUtil;
import miui.util.ViewUtils;
import miui.view.MiuiHapticFeedbackConstants;

public class DropDownPopupWindow {
    private static final float DIM = 0.3f;
    private static int OFFSET_H = 40;
    private static final String TAG = "DropDownPopupWindow";
    private View mAnchorView;
    /* access modifiers changed from: private */
    public ValueAnimator mAnimator;
    private AnimatorListener mAnimatorListener = new AnimatorListener() {
        private void tryDismiss() {
            if (DropDownPopupWindow.this.mDismissPending) {
                DropDownPopupWindow.this.realDismiss();
            }
        }

        public void onAnimationCancel(Animator animator) {
            tryDismiss();
        }

        public void onAnimationEnd(Animator animator) {
            tryDismiss();
        }

        public void onAnimationRepeat(Animator animator) {
        }

        public void onAnimationStart(Animator animator) {
            if (DropDownPopupWindow.this.mDismissPending && DropDownPopupWindow.this.mDropDownController != null) {
                DropDownPopupWindow.this.mDropDownController.onDismiss();
            }
        }
    };
    /* access modifiers changed from: private */
    public ContainerView mContainer;
    /* access modifiers changed from: private */
    public ContainerController mContainerController;
    /* access modifiers changed from: private */
    public ContentController mContentController;
    /* access modifiers changed from: private */
    public View mContentView;
    private Context mContext;
    private int mDismissDuration = 300;
    /* access modifiers changed from: private */
    public boolean mDismissPending;
    /* access modifiers changed from: private */
    public Controller mDropDownController;
    private int mElevation;
    private int mMaxWith;
    private int mMinWith;
    private PopupWindow mPopupWindow;
    /* access modifiers changed from: private */
    public View mRealContainerView;
    private int mShowDuration = 300;
    private AnimatorUpdateListener mValueUpdateListener = new AnimatorUpdateListener() {
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) DropDownPopupWindow.this.mAnimator.getAnimatedValue()).floatValue();
            if (DropDownPopupWindow.this.mContainerController != null) {
                DropDownPopupWindow.this.mContainerController.onAniamtionUpdate(DropDownPopupWindow.this.mContainer, floatValue);
            }
            if (DropDownPopupWindow.this.mContentController != null) {
                DropDownPopupWindow.this.mContentController.onAniamtionUpdate(DropDownPopupWindow.this.mContentView, floatValue);
            }
        }
    };

    public interface ContainerController extends Controller {
        boolean onAddContent(View view, View view2);
    }

    class ContainerView extends FrameLayout {
        public ContainerView(Context context, AttributeSet attributeSet, int i) {
            super(context, attributeSet, i);
        }

        public boolean onKeyPreIme(int i, KeyEvent keyEvent) {
            if (i != 4 || keyEvent.getAction() != 1) {
                return false;
            }
            DropDownPopupWindow.this.dismiss();
            return true;
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (!super.onTouchEvent(motionEvent) && motionEvent.getAction() == 1) {
                DropDownPopupWindow.this.dismiss();
            }
            return true;
        }
    }

    public interface ContentController extends Controller {
        View getContentView();
    }

    public interface Controller {
        void onAniamtionUpdate(View view, float f);

        void onDismiss();

        void onShow();
    }

    public class DefaultContainerController implements ContainerController {
        public boolean onAddContent(View view, View view2) {
            return false;
        }

        public void onAniamtionUpdate(View view, float f) {
            Drawable background = view == null ? null : view.getBackground();
            if (background != null) {
                background.setAlpha((int) (f * 255.0f));
            }
        }

        public void onDismiss() {
        }

        public void onShow() {
        }
    }

    public class ListController extends ViewContentController {
        private ListView mListView;

        public ListController(Context context) {
            this(context, R.layout.drop_down_popup_list);
        }

        public ListController(Context context, int i) {
            super(context, i);
        }

        public ListController(DropDownPopupWindow dropDownPopupWindow) {
            super(dropDownPopupWindow, R.layout.drop_down_popup_list);
        }

        public ListController(DropDownPopupWindow dropDownPopupWindow, int i) {
            super(dropDownPopupWindow, i);
        }

        public ListView getListView() {
            initContent();
            return this.mListView;
        }

        /* access modifiers changed from: protected */
        public void onContentInit(View view) {
            this.mListView = (ListView) view.findViewById(16908298);
        }
    }

    public class ViewContentController implements ContentController {
        private View mContent;
        private Context mContext;
        private int mLayoutId;

        public ViewContentController(Context context, int i) {
            this.mContext = context;
            this.mLayoutId = i;
        }

        public ViewContentController(DropDownPopupWindow dropDownPopupWindow, int i) {
            this(dropDownPopupWindow.getContext(), i);
            dropDownPopupWindow.setContentController(this);
        }

        public View getContentView() {
            initContent();
            return this.mContent;
        }

        /* access modifiers changed from: protected */
        public void initContent() {
            if (this.mContent == null) {
                this.mContent = LayoutInflater.from(this.mContext).inflate(this.mLayoutId, null);
                onContentInit(this.mContent);
            }
        }

        public void onAniamtionUpdate(View view, float f) {
            if (view != null) {
                view.setTranslationY(((float) (-view.getHeight())) * (1.0f - f));
            }
        }

        /* access modifiers changed from: protected */
        public void onContentInit(View view) {
        }

        public void onDismiss() {
        }

        public void onShow() {
        }
    }

    public DropDownPopupWindow(Context context, AttributeSet attributeSet, int i) {
        this.mContext = context;
        this.mPopupWindow = new PopupWindow(context, attributeSet, 0, i);
        this.mContainer = new ContainerView(context, attributeSet, i);
        this.mPopupWindow.setAnimationStyle(DropDownPopupWindowHelper.getAnimationStyle());
        initPopupWindow();
    }

    private void initPopupWindow() {
        int i;
        int i2;
        Resources resources;
        this.mElevation = DropDownPopupWindowHelper.getPopupElevation(this.mContext);
        this.mMinWith = DropDownPopupWindowHelper.getPopupMinWidth(this.mContext);
        this.mMaxWith = this.mContext.getResources().getDisplayMetrics().widthPixels - (OFFSET_H * 2);
        this.mPopupWindow.setWidth(DropDownPopupWindowHelper.getWidth());
        this.mPopupWindow.setHeight(DropDownPopupWindowHelper.getWidth());
        Android_Widget_PopupWindow_class android_Widget_PopupWindow_class = Factory.getInstance().get();
        android_Widget_PopupWindow_class.setLayoutInScreenEnabled(this.mPopupWindow, true);
        android_Widget_PopupWindow_class.setLayoutInsetDecor(this.mPopupWindow, true);
        this.mPopupWindow.setSoftInputMode(3);
        this.mPopupWindow.setOutsideTouchable(false);
        this.mPopupWindow.setFocusable(true);
        this.mContainer.setFocusableInTouchMode(true);
        try {
            i = AttributeResolver.resolveColor(this.mContext, R.attr.arrowPopupWindowDimColor);
        } catch (Exception unused) {
            i = -1;
        }
        if (i == -1) {
            if (ViewUtils.isNightMode(this.mContext)) {
                resources = this.mContext.getResources();
                i2 = R.color.arrow_popup_window_dim_color_dark;
            } else {
                resources = this.mContext.getResources();
                i2 = R.color.arrow_popup_window_dim_color_light;
            }
            i = resources.getColor(i2);
        }
        this.mContainer.setBackgroundColor(i);
        this.mPopupWindow.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                if (!DropDownPopupWindow.this.mDismissPending) {
                    DropDownPopupWindow.this.realDismiss();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void realDismiss() {
        PopupWindow popupWindow = this.mPopupWindow;
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        ContainerController containerController = this.mContainerController;
        if (containerController != null) {
            containerController.onDismiss();
        }
        ContentController contentController = this.mContentController;
        if (contentController != null) {
            contentController.onDismiss();
        }
        Controller controller = this.mDropDownController;
        if (controller != null) {
            controller.onDismiss();
        }
        this.mDismissPending = false;
    }

    private void startAnimation(float f, float f2, int i) {
        ValueAnimator valueAnimator = this.mAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        if (this.mContainerController != null || this.mContentController != null) {
            ValueAnimator valueAnimator2 = this.mAnimator;
            if (valueAnimator2 == null) {
                this.mAnimator = ValueAnimator.ofFloat(new float[]{f, f2});
            } else {
                valueAnimator2.setFloatValues(new float[]{f, f2});
            }
            this.mAnimator.setDuration(DeviceHelper.FEATURE_WHOLE_ANIM ? (long) i : 0);
            this.mAnimator.addUpdateListener(this.mValueUpdateListener);
            this.mAnimator.addListener(this.mAnimatorListener);
            this.mAnimator.start();
        }
    }

    public void dismiss() {
        this.mDismissPending = true;
        realDismiss();
    }

    public Context getContext() {
        return this.mContext;
    }

    public void setAnchor(View view) {
        this.mAnchorView = view;
    }

    public void setContainerController(ContainerController containerController) {
        this.mContainerController = containerController;
    }

    public void setContentController(ContentController contentController) {
        this.mContentController = contentController;
    }

    public void setDropDownController(Controller controller) {
        this.mDropDownController = controller;
    }

    public void show() {
        View view;
        int i;
        if (this.mPopupWindow.isShowing()) {
            View view2 = this.mAnchorView;
            if (view2 != null) {
                this.mPopupWindow.update(view2, 0, 0);
            } else {
                this.mPopupWindow.update(0, 0);
            }
        } else {
            int width = DropDownPopupWindowHelper.getWidth();
            ContentController contentController = this.mContentController;
            if (contentController != null) {
                this.mContentView = contentController.getContentView();
                View view3 = this.mContentView;
                if (view3 != null) {
                    width = DropDownPopupWindowHelper.setupContentView(this.mContainer, view3, this.mElevation, this.mMinWith, this.mContainerController);
                }
            }
            int i2 = this.mMaxWith;
            if (width > i2) {
                width = i2;
            }
            this.mRealContainerView = this.mContentView;
            this.mPopupWindow.setTouchInterceptor(new OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    int x = (int) motionEvent.getX();
                    int y = (int) motionEvent.getY();
                    if (motionEvent.getAction() != 0 || (x >= 0 && x < DropDownPopupWindow.this.mRealContainerView.getWidth() && y >= 0 && y < DropDownPopupWindow.this.mRealContainerView.getHeight())) {
                        return false;
                    }
                    DropDownPopupWindow.this.dismiss();
                    return true;
                }
            });
            ContainerController containerController = this.mContainerController;
            if (containerController != null) {
                containerController.onShow();
            }
            this.mPopupWindow.setWidth(width);
            this.mPopupWindow.setHeight(DropDownPopupWindowHelper.getHeight());
            PopupWindow popupWindow = this.mPopupWindow;
            View view4 = this.mRealContainerView;
            popupWindow.setElevation(view4 == null ? 0.0f : view4.getElevation());
            this.mPopupWindow.setContentView(this.mRealContainerView);
            View view5 = this.mAnchorView;
            if (view5 == null || !view5.isAttachedToWindow()) {
                this.mPopupWindow.showAtLocation(this.mRealContainerView, 8388659, 0, 0);
                view = this.mRealContainerView;
            } else {
                this.mPopupWindow.setHeight(DropDownPopupWindowHelper.getRealHeight(this.mContext, this.mAnchorView));
                int[] iArr = new int[2];
                this.mAnchorView.getLocationInWindow(iArr);
                if (width == this.mMaxWith) {
                    i = OFFSET_H;
                    if (ViewUtils.isLayoutRtl(this.mRealContainerView)) {
                        i += this.mMaxWith;
                    }
                } else {
                    i = DropDownPopupWindowHelper.getShowX(iArr, this.mAnchorView.getWidth() - this.mPopupWindow.getWidth());
                }
                this.mPopupWindow.showAtLocation(this.mRealContainerView, 0, i, DropDownPopupWindowHelper.getShowY(iArr, this.mAnchorView.getHeight()));
                view = this.mAnchorView;
            }
            if (view != null && HapticFeedbackUtil.isSupportLinearMotorVibrate(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_POPUP_NORMAL)) {
                view.performHapticFeedback(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_POPUP_NORMAL);
            }
        }
        View view6 = this.mRealContainerView;
        if (view6 != null) {
            DropDownPopupWindowHelper.changeWindowBackground(view6.getRootView(), DIM);
        }
    }
}

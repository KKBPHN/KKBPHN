package miui.animation.controller;

import android.app.UiModeManager;
import android.graphics.Color;
import android.util.ArrayMap;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;
import miui.R;
import miui.animation.IAnimTarget;
import miui.animation.ITouchStyle;
import miui.animation.ITouchStyle.TouchType;
import miui.animation.ViewTarget;
import miui.animation.base.AnimConfig;
import miui.animation.listener.TransitionListener;
import miui.animation.listener.UpdateInfo;
import miui.animation.property.FloatProperty;
import miui.animation.property.ViewProperty;
import miui.animation.utils.CommonUtils;
import miui.animation.utils.EaseManager;
import miui.animation.utils.LogUtils;

public class FolmeTouch extends FolmeBase implements ITouchStyle {
    private static final float DEFAULT_SCALE = 0.9f;
    private static final int SCALE_DIS = 10;
    private static WeakHashMap sTouchRecord = new WeakHashMap();
    private AnimConfig mAlphaConfig;
    private AnimConfig mDownConfig = new AnimConfig();
    private int mDownWeight;
    private FolmeFont mFontStyle;
    private boolean mIsDown;
    private WeakReference mListView;
    private int[] mLocation = new int[2];
    /* access modifiers changed from: private */
    public float mScaleDist;
    private Map mScaleSetMap = new ArrayMap();
    private boolean mSetTint;
    private WeakReference mTouchView;
    private AnimConfig mUpConfig = new AnimConfig();
    private int mUpWeight;

    class InnerListViewTouchListener implements OnTouchListener {
        private AnimConfig[] mConfigs;
        private WeakReference mFolmeTouchRef;

        InnerListViewTouchListener(FolmeTouch folmeTouch, AnimConfig... animConfigArr) {
            this.mFolmeTouchRef = new WeakReference(folmeTouch);
            this.mConfigs = animConfigArr;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            WeakReference weakReference = this.mFolmeTouchRef;
            FolmeTouch folmeTouch = weakReference == null ? null : (FolmeTouch) weakReference.get();
            if (folmeTouch != null) {
                AnimConfig[] animConfigArr = this.mConfigs;
                if (motionEvent == null) {
                    folmeTouch.onEventUp(animConfigArr);
                } else {
                    folmeTouch.handleMotionEvent(view, motionEvent, animConfigArr);
                }
            }
            return false;
        }
    }

    class InnerViewTouchListener implements OnTouchListener {
        private WeakHashMap mTouchMap;

        private InnerViewTouchListener() {
            this.mTouchMap = new WeakHashMap();
        }

        /* access modifiers changed from: 0000 */
        public void addTouch(FolmeTouch folmeTouch, AnimConfig... animConfigArr) {
            this.mTouchMap.put(folmeTouch, animConfigArr);
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            for (Entry entry : this.mTouchMap.entrySet()) {
                ((FolmeTouch) entry.getKey()).handleMotionEvent(view, motionEvent, (AnimConfig[]) entry.getValue());
            }
            return false;
        }

        /* access modifiers changed from: 0000 */
        public boolean removeTouch(FolmeTouch folmeTouch) {
            this.mTouchMap.remove(folmeTouch);
            return this.mTouchMap.isEmpty();
        }
    }

    class ListViewInfo {
        View itemView;
        AbsListView listView;

        private ListViewInfo() {
        }
    }

    public FolmeTouch(IAnimTarget... iAnimTargetArr) {
        super(iAnimTargetArr);
        initScaleDist(iAnimTargetArr.length > 0 ? iAnimTargetArr[0] : null);
        FloatProperty property = getProperty(2);
        FloatProperty property2 = getProperty(3);
        this.mState.getState(TouchType.UP).add(property, 1.0f, new long[0]).add(property2, 1.0f, new long[0]);
        this.mState.getState(TouchType.DOWN).add(property, 0.9f, new long[0]).add(property2, 0.9f, new long[0]);
        setTintColor();
        this.mDownConfig.ease = EaseManager.getStyle(-2, 0.99f, 0.15f);
        this.mDownConfig.addListeners(new TransitionListener() {
            public void onBegin(Object obj, UpdateInfo updateInfo) {
                TouchType touchType = TouchType.DOWN;
                if (obj == touchType && !FolmeTouch.this.isScaleSet(touchType)) {
                    FloatProperty floatProperty = updateInfo.property;
                    if (floatProperty == ViewProperty.SCALE_X || floatProperty == ViewProperty.SCALE_Y) {
                        IAnimTarget target = FolmeTouch.this.mState.getTarget();
                        float max = Math.max(target.getValue(6), target.getValue(5));
                        float max2 = Math.max((max - FolmeTouch.this.mScaleDist) / max, 0.9f);
                        updateInfo.anim.setValues(max2);
                    }
                }
            }
        });
        this.mUpConfig.ease = EaseManager.getStyle(-2, 0.99f, 0.3f);
        this.mAlphaConfig = new AnimConfig(getProperty(4)).setEase(-2, 0.9f, 0.2f);
        setTintMode(1);
    }

    /* access modifiers changed from: private */
    public boolean bindListView(View view, boolean z, AnimConfig... animConfigArr) {
        if (this.mState.getTarget() != null) {
            ListViewInfo listViewInfo = getListViewInfo(view);
            if (!(listViewInfo == null || listViewInfo.listView == null)) {
                if (LogUtils.isLogEnabled()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("handleListViewTouch for ");
                    sb.append(view);
                    LogUtils.debug(sb.toString(), new Object[0]);
                }
                handleListViewTouch(listViewInfo.listView, view, z, animConfigArr);
                return true;
            }
        }
        return false;
    }

    private AnimConfig[] getDownConfig(AnimConfig... animConfigArr) {
        return (AnimConfig[]) CommonUtils.mergeArray(animConfigArr, this.mDownConfig);
    }

    private ListViewInfo getListViewInfo(View view) {
        AbsListView absListView = null;
        ListViewInfo listViewInfo = new ListViewInfo();
        ViewParent parent = view.getParent();
        while (true) {
            if (parent == null) {
                break;
            } else if (parent instanceof AbsListView) {
                absListView = (AbsListView) parent;
                break;
            } else {
                if (parent instanceof View) {
                    view = (View) parent;
                }
                parent = parent.getParent();
            }
        }
        if (absListView != null) {
            this.mListView = new WeakReference(listViewInfo.listView);
            listViewInfo.listView = absListView;
            listViewInfo.itemView = view;
        }
        return listViewInfo;
    }

    public static ListViewTouchListener getListViewTouchListener(AbsListView absListView) {
        return (ListViewTouchListener) absListView.getTag(CommonUtils.KEY_FOLME_LISTVIEW_TOUCH_LISTENER);
    }

    private TouchType getType(TouchType... touchTypeArr) {
        return touchTypeArr.length > 0 ? touchTypeArr[0] : TouchType.DOWN;
    }

    private AnimConfig[] getUpConfig(AnimConfig... animConfigArr) {
        return (AnimConfig[]) CommonUtils.mergeArray(animConfigArr, this.mUpConfig, this.mAlphaConfig);
    }

    private void handleListViewTouch(AbsListView absListView, View view, boolean z, AnimConfig... animConfigArr) {
        ListViewTouchListener listViewTouchListener = getListViewTouchListener(absListView);
        if (listViewTouchListener == null) {
            listViewTouchListener = new ListViewTouchListener(absListView);
            absListView.setTag(CommonUtils.KEY_FOLME_LISTVIEW_TOUCH_LISTENER, listViewTouchListener);
        }
        if (z) {
            absListView.setOnTouchListener(listViewTouchListener);
        }
        listViewTouchListener.putListener(view, new InnerListViewTouchListener(this, animConfigArr));
    }

    /* access modifiers changed from: private */
    public void handleMotionEvent(View view, MotionEvent motionEvent, AnimConfig... animConfigArr) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            onEventDown(animConfigArr);
        } else if (actionMasked != 2) {
            onEventUp(animConfigArr);
        } else {
            onEventMove(motionEvent, view, animConfigArr);
        }
    }

    private void handleViewTouch(View view, AnimConfig... animConfigArr) {
        InnerViewTouchListener innerViewTouchListener = (InnerViewTouchListener) sTouchRecord.get(view);
        if (innerViewTouchListener == null) {
            innerViewTouchListener = new InnerViewTouchListener();
            sTouchRecord.put(view, innerViewTouchListener);
        }
        view.setOnTouchListener(innerViewTouchListener);
        innerViewTouchListener.addTouch(this, animConfigArr);
    }

    private void initScaleDist(IAnimTarget iAnimTarget) {
        View targetObject = iAnimTarget instanceof ViewTarget ? ((ViewTarget) iAnimTarget).getTargetObject() : null;
        if (targetObject != null) {
            this.mScaleDist = TypedValue.applyDimension(1, 10.0f, targetObject.getResources().getDisplayMetrics());
        }
    }

    static boolean isOnTouchView(View view, int[] iArr, MotionEvent motionEvent) {
        if (view == null) {
            return true;
        }
        view.getLocationOnScreen(iArr);
        int rawX = (int) motionEvent.getRawX();
        int rawY = (int) motionEvent.getRawY();
        return rawX >= iArr[0] && rawX <= iArr[0] + view.getWidth() && rawY >= iArr[1] && rawY <= iArr[1] + view.getHeight();
    }

    /* access modifiers changed from: private */
    public boolean isScaleSet(TouchType touchType) {
        return Boolean.TRUE.equals(this.mScaleSetMap.get(touchType));
    }

    private void onEventDown(AnimConfig... animConfigArr) {
        if (LogUtils.isLogEnabled()) {
            LogUtils.debug("onEventDown, touchDown", new Object[0]);
        }
        this.mIsDown = true;
        touchDown(animConfigArr);
    }

    private void onEventMove(MotionEvent motionEvent, View view, AnimConfig... animConfigArr) {
        if (this.mIsDown && !isOnTouchView(view, this.mLocation, motionEvent)) {
            touchUp(animConfigArr);
            resetTouchStatus();
        }
    }

    /* access modifiers changed from: private */
    public void onEventUp(AnimConfig... animConfigArr) {
        if (this.mIsDown) {
            if (LogUtils.isLogEnabled()) {
                LogUtils.debug("onEventUp, touchUp", new Object[0]);
            }
            touchUp(animConfigArr);
            resetTouchStatus();
        }
    }

    private void resetTouchStatus() {
        this.mIsDown = false;
    }

    private View resetView(WeakReference weakReference) {
        View view = (View) weakReference.get();
        if (view != null) {
            view.setOnTouchListener(null);
        }
        return view;
    }

    /* access modifiers changed from: private */
    public void resetViewTouch(View view, boolean z) {
        view.setClickable(z);
        view.setOnTouchListener(null);
    }

    private void setTintColor() {
        if (!this.mSetTint) {
            int argb = Color.argb(20, 0, 0, 0);
            Object targetObject = this.mState.getTarget().getTargetObject();
            if (targetObject instanceof View) {
                View view = (View) targetObject;
                int i = R.color.folme_color_touch_tint;
                UiModeManager uiModeManager = (UiModeManager) view.getContext().getSystemService("uimode");
                if (uiModeManager != null && uiModeManager.getNightMode() == 2) {
                    i = R.color.folme_color_touch_tint_dark;
                }
                argb = view.getResources().getColor(i);
            }
            this.mState.getState(TouchType.DOWN).add(getProperty(7), argb, new long[0]);
        }
    }

    private boolean setTouchView(View view) {
        WeakReference weakReference = this.mTouchView;
        if ((weakReference != null ? (View) weakReference.get() : null) == view) {
            return false;
        }
        this.mTouchView = new WeakReference(view);
        return true;
    }

    public void bindViewOfListItem(final View view, final AnimConfig... animConfigArr) {
        if (setTouchView(view)) {
            CommonUtils.runOnPreDraw(view, new Runnable() {
                public void run() {
                    FolmeTouch.this.bindListView(view, false, animConfigArr);
                }
            });
        }
    }

    public void cancel() {
        super.cancel();
        FolmeFont folmeFont = this.mFontStyle;
        if (folmeFont != null) {
            folmeFont.cancel();
        }
    }

    public void clean() {
        super.clean();
        FolmeFont folmeFont = this.mFontStyle;
        if (folmeFont != null) {
            folmeFont.clean();
        }
        this.mScaleSetMap.clear();
        WeakReference weakReference = this.mTouchView;
        if (weakReference != null) {
            resetView(weakReference);
            this.mTouchView = null;
        }
        WeakReference weakReference2 = this.mListView;
        if (weakReference2 != null) {
            View resetView = resetView(weakReference2);
            if (resetView != null) {
                resetView.setTag(CommonUtils.KEY_FOLME_LISTVIEW_TOUCH_LISTENER, null);
            }
            this.mListView = null;
        }
        resetTouchStatus();
    }

    public void handleTouchOf(View view, boolean z, AnimConfig... animConfigArr) {
        handleViewTouch(view, animConfigArr);
        if (setTouchView(view)) {
            if (LogUtils.isLogEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append("handleViewTouch for ");
                sb.append(view);
                LogUtils.debug(sb.toString(), new Object[0]);
            }
            final boolean isClickable = view.isClickable();
            view.setClickable(true);
            final boolean z2 = z;
            final View view2 = view;
            final AnimConfig[] animConfigArr2 = animConfigArr;
            AnonymousClass3 r2 = new Runnable() {
                public void run() {
                    if (!z2 && FolmeTouch.this.bindListView(view2, true, animConfigArr2)) {
                        FolmeTouch.this.resetViewTouch(view2, isClickable);
                    }
                }
            };
            CommonUtils.runOnPreDraw(view, r2);
        }
    }

    public void handleTouchOf(View view, AnimConfig... animConfigArr) {
        handleTouchOf(view, false, animConfigArr);
    }

    public void ignoreTouchOf(View view) {
        InnerViewTouchListener innerViewTouchListener = (InnerViewTouchListener) sTouchRecord.get(view);
        if (innerViewTouchListener != null && innerViewTouchListener.removeTouch(this)) {
            sTouchRecord.remove(view);
        }
    }

    public void onMotionEvent(MotionEvent motionEvent) {
        handleMotionEvent(null, motionEvent, new AnimConfig[0]);
    }

    public void onMotionEventEx(View view, MotionEvent motionEvent, AnimConfig... animConfigArr) {
        handleMotionEvent(view, motionEvent, animConfigArr);
    }

    public ITouchStyle setAlpha(float f, TouchType... touchTypeArr) {
        this.mState.getState(getType(touchTypeArr)).add(getProperty(4), f, new long[0]);
        return this;
    }

    public ITouchStyle setBackgroundColor(float f, float f2, float f3, float f4) {
        return setBackgroundColor(Color.argb((int) (f * 255.0f), (int) (f2 * 255.0f), (int) (f3 * 255.0f), (int) (f4 * 255.0f)));
    }

    public ITouchStyle setBackgroundColor(int i) {
        this.mState.getState(TouchType.DOWN).add(getProperty(8), i, new long[0]);
        return this;
    }

    public void setFontStyle(FolmeFont folmeFont) {
        this.mFontStyle = folmeFont;
    }

    public ITouchStyle setScale(float f, TouchType... touchTypeArr) {
        TouchType type = getType(touchTypeArr);
        this.mScaleSetMap.put(type, Boolean.valueOf(true));
        this.mState.getState(type).add(getProperty(2), f, new long[0]).add(getProperty(3), f, new long[0]);
        return this;
    }

    public ITouchStyle setTint(float f, float f2, float f3, float f4) {
        return setTint(Color.argb((int) (f * 255.0f), (int) (f2 * 255.0f), (int) (f3 * 255.0f), (int) (f4 * 255.0f)));
    }

    public ITouchStyle setTint(int i) {
        this.mSetTint = true;
        this.mState.getState(TouchType.DOWN).add(getProperty(7), i, new long[0]);
        return this;
    }

    public ITouchStyle setTintMode(int i) {
        this.mDownConfig.setTintMode(i);
        this.mUpConfig.setTintMode(i);
        return this;
    }

    public void setTouchDown() {
        setTintColor();
        this.mState.setTo((Object) TouchType.DOWN);
    }

    public void setTouchUp() {
        this.mState.setTo((Object) TouchType.UP);
    }

    public void touchDown(AnimConfig... animConfigArr) {
        setTintColor();
        alignState(TouchType.UP, TouchType.DOWN);
        AnimConfig[] downConfig = getDownConfig(animConfigArr);
        FolmeFont folmeFont = this.mFontStyle;
        if (folmeFont != null) {
            folmeFont.to(this.mDownWeight, downConfig);
        }
        IFolmeStateStyle iFolmeStateStyle = this.mState;
        iFolmeStateStyle.to(iFolmeStateStyle.getState(TouchType.DOWN), downConfig);
    }

    public void touchUp(AnimConfig... animConfigArr) {
        alignState(TouchType.DOWN, TouchType.UP);
        AnimConfig[] upConfig = getUpConfig(animConfigArr);
        FolmeFont folmeFont = this.mFontStyle;
        if (folmeFont != null) {
            folmeFont.to(this.mUpWeight, upConfig);
        }
        IFolmeStateStyle iFolmeStateStyle = this.mState;
        iFolmeStateStyle.to(iFolmeStateStyle.getState(TouchType.UP), upConfig);
    }

    public ITouchStyle useVarFont(TextView textView, int i, int i2, int i3) {
        FolmeFont folmeFont = this.mFontStyle;
        if (folmeFont != null) {
            this.mUpWeight = i2;
            this.mDownWeight = i3;
            folmeFont.useAt(textView, i, i2);
        }
        return this;
    }
}

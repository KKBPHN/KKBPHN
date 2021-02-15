package miuix.animation.controller;

import android.app.UiModeManager;
import android.graphics.Color;
import android.util.ArrayMap;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;
import miuix.animation.IAnimTarget;
import miuix.animation.ITouchStyle;
import miuix.animation.ITouchStyle.TouchType;
import miuix.animation.ViewTarget;
import miuix.animation.base.AnimConfig;
import miuix.animation.listener.TransitionListener;
import miuix.animation.listener.UpdateInfo;
import miuix.animation.property.FloatProperty;
import miuix.animation.property.ViewProperty;
import miuix.animation.utils.CommonUtils;
import miuix.animation.utils.EaseManager;
import miuix.animation.utils.LogUtils;
import miuix.folme.R;

public class FolmeTouch extends FolmeBase implements ITouchStyle {
    private static final float DEFAULT_SCALE = 0.9f;
    private static final int SCALE_DIS = 10;
    private static WeakHashMap sTouchRecord = new WeakHashMap();
    private AnimConfig mAlphaConfig;
    private boolean mClickInvoked;
    private OnClickListener mClickListener;
    private AnimConfig mDownConfig = new AnimConfig();
    private int mDownWeight;
    private float mDownX;
    private float mDownY;
    private FolmeFont mFontStyle;
    private boolean mIsDown;
    private WeakReference mListView;
    private int[] mLocation = new int[2];
    /* access modifiers changed from: private */
    public boolean mLongClickInvoked;
    /* access modifiers changed from: private */
    public OnLongClickListener mLongClickListener;
    private LongClickTask mLongClickTask;
    /* access modifiers changed from: private */
    public float mScaleDist;
    private Map mScaleSetMap = new ArrayMap();
    private boolean mSetTint;
    private int mTouchIndex;
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

    final class LongClickTask implements Runnable {
        private WeakReference mTouchRef;

        private LongClickTask() {
        }

        public void run() {
            FolmeTouch folmeTouch = (FolmeTouch) this.mTouchRef.get();
            if (folmeTouch != null) {
                IAnimTarget target = folmeTouch.mState.getTarget();
                if (target instanceof ViewTarget) {
                    View view = (View) target.getTargetObject();
                    if (view != null && folmeTouch.mLongClickListener != null) {
                        view.performLongClick();
                        folmeTouch.invokeLongClick(view);
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void start(FolmeTouch folmeTouch) {
            IAnimTarget target = folmeTouch.mState.getTarget();
            if (target instanceof ViewTarget) {
                View targetObject = ((ViewTarget) target).getTargetObject();
                if (targetObject != null) {
                    this.mTouchRef = new WeakReference(folmeTouch);
                    targetObject.postDelayed(this, (long) ViewConfiguration.getLongPressTimeout());
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void stop(FolmeTouch folmeTouch) {
            IAnimTarget target = folmeTouch.mState.getTarget();
            if (target instanceof ViewTarget) {
                View targetObject = ((ViewTarget) target).getTargetObject();
                if (targetObject != null) {
                    targetObject.removeCallbacks(this);
                }
            }
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

    private void doHandleTouchOf(View view, OnClickListener onClickListener, OnLongClickListener onLongClickListener, boolean z, AnimConfig... animConfigArr) {
        setClickAndLongClickListener(onClickListener, onLongClickListener);
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
            AnonymousClass3 r0 = new Runnable() {
                public void run() {
                    if (!z2 && FolmeTouch.this.bindListView(view2, true, animConfigArr2)) {
                        FolmeTouch.this.resetViewTouch(view2, isClickable);
                    }
                }
            };
            CommonUtils.runOnPreDraw(view, r0);
        }
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
        return (ListViewTouchListener) absListView.getTag(R.id.miuix_animation_tag_touch_listener);
    }

    private TouchType getType(TouchType... touchTypeArr) {
        return touchTypeArr.length > 0 ? touchTypeArr[0] : TouchType.DOWN;
    }

    private AnimConfig[] getUpConfig(AnimConfig... animConfigArr) {
        return (AnimConfig[]) CommonUtils.mergeArray(animConfigArr, this.mUpConfig, this.mAlphaConfig);
    }

    private void handleClick(View view, MotionEvent motionEvent) {
        if (this.mIsDown && this.mClickListener != null && this.mTouchIndex == motionEvent.getActionIndex()) {
            IAnimTarget target = this.mState.getTarget();
            if ((target instanceof ViewTarget) && isInTouchSlop(view, motionEvent)) {
                View targetObject = ((ViewTarget) target).getTargetObject();
                targetObject.performClick();
                invokeClick(targetObject);
            }
        }
    }

    private void handleListViewTouch(AbsListView absListView, View view, boolean z, AnimConfig... animConfigArr) {
        ListViewTouchListener listViewTouchListener = getListViewTouchListener(absListView);
        if (listViewTouchListener == null) {
            listViewTouchListener = new ListViewTouchListener(absListView);
            absListView.setTag(R.id.miuix_animation_tag_touch_listener, listViewTouchListener);
        }
        if (z) {
            absListView.setOnTouchListener(listViewTouchListener);
        }
        listViewTouchListener.putListener(view, new InnerListViewTouchListener(this, animConfigArr));
    }

    /* access modifiers changed from: private */
    public void handleMotionEvent(View view, MotionEvent motionEvent, AnimConfig... animConfigArr) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked == 1) {
                handleClick(view, motionEvent);
            } else if (actionMasked == 2) {
                onEventMove(motionEvent, view, animConfigArr);
                return;
            }
            onEventUp(animConfigArr);
            return;
        }
        recordDownEvent(motionEvent);
        onEventDown(animConfigArr);
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

    /* access modifiers changed from: private */
    public void invokeClick(View view) {
        if (!this.mClickInvoked && !this.mLongClickInvoked) {
            this.mClickInvoked = true;
            this.mClickListener.onClick(view);
        }
    }

    /* access modifiers changed from: private */
    public void invokeLongClick(View view) {
        if (!this.mLongClickInvoked) {
            this.mLongClickInvoked = true;
            this.mLongClickListener.onLongClick(view);
        }
    }

    private boolean isInTouchSlop(View view, MotionEvent motionEvent) {
        return CommonUtils.getDistance(this.mDownX, this.mDownY, motionEvent.getRawX(), motionEvent.getRawY()) < ((double) CommonUtils.getTouchSlop(view));
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
        if (!this.mIsDown) {
            return;
        }
        if (!isOnTouchView(view, this.mLocation, motionEvent)) {
            touchUp(animConfigArr);
            resetTouchStatus();
        } else if (this.mLongClickTask != null && !isInTouchSlop(view, motionEvent)) {
            this.mLongClickTask.stop(this);
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

    private void recordDownEvent(MotionEvent motionEvent) {
        if (this.mClickListener != null || this.mLongClickListener != null) {
            this.mTouchIndex = motionEvent.getActionIndex();
            this.mDownX = motionEvent.getRawX();
            this.mDownY = motionEvent.getRawY();
            this.mClickInvoked = false;
            this.mLongClickInvoked = false;
            startLongClickTask();
        }
    }

    private void resetTouchStatus() {
        LongClickTask longClickTask = this.mLongClickTask;
        if (longClickTask != null) {
            longClickTask.stop(this);
        }
        this.mIsDown = false;
        this.mTouchIndex = 0;
        this.mDownX = 0.0f;
        this.mDownY = 0.0f;
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

    private void setClickAndLongClickListener(OnClickListener onClickListener, OnLongClickListener onLongClickListener) {
        IAnimTarget target = this.mState.getTarget();
        View targetObject = target instanceof ViewTarget ? ((ViewTarget) target).getTargetObject() : null;
        if (targetObject != null) {
            if (this.mClickListener != null && onClickListener == null) {
                targetObject.setOnClickListener(null);
            } else if (onClickListener != null) {
                targetObject.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        FolmeTouch.this.invokeClick(view);
                    }
                });
            }
            this.mClickListener = onClickListener;
            if (this.mLongClickListener != null && onLongClickListener == null) {
                targetObject.setOnLongClickListener(null);
            } else if (onLongClickListener != null) {
                targetObject.setOnLongClickListener(new OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        if (FolmeTouch.this.mLongClickInvoked) {
                            return false;
                        }
                        FolmeTouch.this.invokeLongClick(view);
                        return true;
                    }
                });
            }
            this.mLongClickListener = onLongClickListener;
        }
    }

    private void setTintColor() {
        if (!this.mSetTint) {
            int argb = Color.argb(20, 0, 0, 0);
            Object targetObject = this.mState.getTarget().getTargetObject();
            if (targetObject instanceof View) {
                View view = (View) targetObject;
                int i = R.color.miuix_folme_color_touch_tint;
                UiModeManager uiModeManager = (UiModeManager) view.getContext().getSystemService("uimode");
                if (uiModeManager != null && uiModeManager.getNightMode() == 2) {
                    i = R.color.miuix_folme_color_touch_tint_dark;
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

    private void startLongClickTask() {
        if (this.mLongClickListener != null) {
            if (this.mLongClickTask == null) {
                this.mLongClickTask = new LongClickTask();
            }
            this.mLongClickTask.start(this);
        }
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
                resetView.setTag(R.id.miuix_animation_tag_touch_listener, null);
            }
            this.mListView = null;
        }
        resetTouchStatus();
    }

    public void handleTouchOf(View view, OnClickListener onClickListener, OnLongClickListener onLongClickListener, AnimConfig... animConfigArr) {
        doHandleTouchOf(view, onClickListener, onLongClickListener, false, animConfigArr);
    }

    public void handleTouchOf(View view, OnClickListener onClickListener, AnimConfig... animConfigArr) {
        doHandleTouchOf(view, onClickListener, null, false, animConfigArr);
    }

    public void handleTouchOf(View view, boolean z, AnimConfig... animConfigArr) {
        doHandleTouchOf(view, null, null, z, animConfigArr);
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

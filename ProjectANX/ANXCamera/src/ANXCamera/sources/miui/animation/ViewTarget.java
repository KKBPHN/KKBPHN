package miui.animation;

import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.lang.ref.WeakReference;
import miui.animation.property.FloatProperty;
import miui.animation.property.ViewProperty;
import miui.animation.property.ViewPropertyExt;
import miui.animation.utils.CommonUtils;

public class ViewTarget extends IAnimTarget {
    public static final ITargetCreator sCreator = new ITargetCreator() {
        public IAnimTarget createTarget(View view) {
            return new ViewTarget(view);
        }
    };
    private boolean mViewInThread;
    private WeakReference mViewRef;

    private ViewTarget(View view) {
        this.mViewRef = new WeakReference(view);
        this.mViewInThread = Looper.myLooper() != Looper.getMainLooper();
    }

    /* access modifiers changed from: private */
    public void initLayout(View view, Runnable runnable) {
        ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup) {
            view.setTag(CommonUtils.KEY_FOLME_INIT_LAYOUT, Boolean.valueOf(true));
            ViewGroup viewGroup = (ViewGroup) parent;
            view.measure(viewGroup.getMeasuredWidthAndState(), viewGroup.getMeasuredHeightAndState());
            runnable.run();
            view.setTag(CommonUtils.KEY_FOLME_INIT_LAYOUT, null);
        }
    }

    public boolean allowAnimRun() {
        View targetObject = getTargetObject();
        return targetObject != null && !Folme.isInDraggingState(targetObject);
    }

    public void executeOnInitialized(final Runnable runnable) {
        final View view = (View) this.mViewRef.get();
        if (view == null) {
            return;
        }
        if (view.getVisibility() == 8 && !view.isLaidOut() && (view.getWidth() == 0 || view.getHeight() == 0)) {
            post(new Runnable() {
                public void run() {
                    ViewTarget.this.initLayout(view, runnable);
                }
            });
        } else {
            post(runnable);
        }
    }

    public void getLocationOnScreen(int[] iArr) {
        View view = (View) this.mViewRef.get();
        if (view == null) {
            iArr[1] = Integer.MAX_VALUE;
            iArr[0] = Integer.MAX_VALUE;
            return;
        }
        view.getLocationOnScreen(iArr);
    }

    public FloatProperty getProperty(int i) {
        switch (i) {
            case 0:
                return ViewProperty.X;
            case 1:
                return ViewProperty.Y;
            case 2:
                return ViewProperty.SCALE_X;
            case 3:
                return ViewProperty.SCALE_Y;
            case 4:
                return ViewProperty.ALPHA;
            case 5:
                return ViewProperty.HEIGHT;
            case 6:
                return ViewProperty.WIDTH;
            case 7:
                return ViewPropertyExt.FOREGROUND;
            case 8:
                return ViewPropertyExt.BACKGROUND;
            case 9:
                return ViewProperty.ROTATION;
            case 10:
                return ViewProperty.ROTATION_X;
            case 11:
                return ViewProperty.ROTATION_Y;
            case 12:
                return ViewProperty.SCROLL_X;
            case 13:
                return ViewProperty.SCROLL_Y;
            case 14:
                return ViewProperty.AUTO_ALPHA;
            case 15:
                return ViewProperty.TRANSLATION_X;
            case 16:
                return ViewProperty.TRANSLATION_Y;
            case 17:
                return ViewProperty.Z;
            case 18:
                return ViewProperty.TRANSLATION_Z;
            default:
                return null;
        }
    }

    public View getTargetObject() {
        return (View) this.mViewRef.get();
    }

    public int getType(FloatProperty floatProperty) {
        if (floatProperty.equals(ViewProperty.X)) {
            return 0;
        }
        if (floatProperty.equals(ViewProperty.Y)) {
            return 1;
        }
        if (floatProperty.equals(ViewProperty.TRANSLATION_X)) {
            return 15;
        }
        if (floatProperty.equals(ViewProperty.TRANSLATION_Y)) {
            return 16;
        }
        if (floatProperty.equals(ViewProperty.SCALE_X)) {
            return 2;
        }
        if (floatProperty.equals(ViewProperty.SCALE_Y)) {
            return 3;
        }
        if (floatProperty.equals(ViewProperty.ALPHA)) {
            return 4;
        }
        if (floatProperty.equals(ViewProperty.HEIGHT)) {
            return 5;
        }
        if (floatProperty.equals(ViewProperty.WIDTH)) {
            return 6;
        }
        if (floatProperty.equals(ViewPropertyExt.FOREGROUND)) {
            return 7;
        }
        if (floatProperty.equals(ViewPropertyExt.BACKGROUND)) {
            return 8;
        }
        if (floatProperty.equals(ViewProperty.AUTO_ALPHA)) {
            return 14;
        }
        if (floatProperty.equals(ViewProperty.ROTATION)) {
            return 9;
        }
        if (floatProperty.equals(ViewProperty.ROTATION_X)) {
            return 10;
        }
        if (floatProperty.equals(ViewProperty.ROTATION_Y)) {
            return 11;
        }
        if (floatProperty.equals(ViewProperty.SCROLL_X)) {
            return 12;
        }
        if (floatProperty.equals(ViewProperty.SCROLL_Y)) {
            return 13;
        }
        if (floatProperty.equals(ViewProperty.TRANSLATION_Z)) {
            return 18;
        }
        return floatProperty.equals(ViewProperty.Z) ? 17 : -1;
    }

    public boolean isValid() {
        return ((View) this.mViewRef.get()) != null;
    }

    public void onFrameEnd(boolean z) {
        View view = (View) this.mViewRef.get();
        if (z && view != null) {
            view.setTag(CommonUtils.KEY_FOLME_SET_HEIGHT, null);
            view.setTag(CommonUtils.KEY_FOLME_SET_WIDTH, null);
        }
    }

    public void post(Runnable runnable) {
        View targetObject = getTargetObject();
        if (targetObject != null) {
            if (!this.mViewInThread || !targetObject.isAttachedToWindow()) {
                try {
                    runnable.run();
                } catch (Exception e) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("ViewTarget.post failed, ");
                    sb.append(getTargetObject());
                    Log.w(CommonUtils.TAG, sb.toString(), e);
                }
            } else {
                targetObject.post(runnable);
            }
        }
    }

    public boolean shouldUseIntValue(FloatProperty floatProperty) {
        if (floatProperty == ViewProperty.WIDTH || floatProperty == ViewProperty.HEIGHT || floatProperty == ViewProperty.SCROLL_X || floatProperty == ViewProperty.SCROLL_Y) {
            return true;
        }
        return super.shouldUseIntValue(floatProperty);
    }
}

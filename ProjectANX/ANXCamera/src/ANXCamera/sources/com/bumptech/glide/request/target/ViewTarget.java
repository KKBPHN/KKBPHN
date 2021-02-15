package com.bumptech.glide.request.target;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import com.bumptech.glide.R;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.util.Preconditions;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Deprecated
public abstract class ViewTarget extends BaseTarget {
    private static final String TAG = "ViewTarget";
    private static boolean isTagUsedAtLeastOnce;
    private static int tagId = R.id.glide_custom_view_target_tag;
    @Nullable
    private OnAttachStateChangeListener attachStateListener;
    private boolean isAttachStateListenerAdded;
    private boolean isClearedByUs;
    private final SizeDeterminer sizeDeterminer;
    protected final View view;

    @VisibleForTesting
    final class SizeDeterminer {
        private static final int PENDING_SIZE = 0;
        @VisibleForTesting
        @Nullable
        static Integer maxDisplayLength;
        private final List cbs = new ArrayList();
        @Nullable
        private SizeDeterminerLayoutListener layoutListener;
        private final View view;
        boolean waitForLayout;

        final class SizeDeterminerLayoutListener implements OnPreDrawListener {
            private final WeakReference sizeDeterminerRef;

            SizeDeterminerLayoutListener(@NonNull SizeDeterminer sizeDeterminer) {
                this.sizeDeterminerRef = new WeakReference(sizeDeterminer);
            }

            public boolean onPreDraw() {
                String str = ViewTarget.TAG;
                if (Log.isLoggable(str, 2)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("OnGlobalLayoutListener called attachStateListener=");
                    sb.append(this);
                    Log.v(str, sb.toString());
                }
                SizeDeterminer sizeDeterminer = (SizeDeterminer) this.sizeDeterminerRef.get();
                if (sizeDeterminer != null) {
                    sizeDeterminer.checkCurrentDimens();
                }
                return true;
            }
        }

        SizeDeterminer(@NonNull View view2) {
            this.view = view2;
        }

        private static int getMaxDisplayLength(@NonNull Context context) {
            if (maxDisplayLength == null) {
                WindowManager windowManager = (WindowManager) context.getSystemService("window");
                Preconditions.checkNotNull(windowManager);
                Display defaultDisplay = windowManager.getDefaultDisplay();
                Point point = new Point();
                defaultDisplay.getSize(point);
                maxDisplayLength = Integer.valueOf(Math.max(point.x, point.y));
            }
            return maxDisplayLength.intValue();
        }

        private int getTargetDimen(int i, int i2, int i3) {
            int i4 = i2 - i3;
            if (i4 > 0) {
                return i4;
            }
            if (this.waitForLayout && this.view.isLayoutRequested()) {
                return 0;
            }
            int i5 = i - i3;
            if (i5 > 0) {
                return i5;
            }
            if (this.view.isLayoutRequested() || i2 != -2) {
                return 0;
            }
            String str = ViewTarget.TAG;
            if (Log.isLoggable(str, 4)) {
                Log.i(str, "Glide treats LayoutParams.WRAP_CONTENT as a request for an image the size of this device's screen dimensions. If you want to load the original image and are ok with the corresponding memory cost and OOMs (depending on the input size), use override(Target.SIZE_ORIGINAL). Otherwise, use LayoutParams.MATCH_PARENT, set layout_width and layout_height to fixed dimension, or use .override() with fixed dimensions.");
            }
            return getMaxDisplayLength(this.view.getContext());
        }

        private int getTargetHeight() {
            int paddingTop = this.view.getPaddingTop() + this.view.getPaddingBottom();
            LayoutParams layoutParams = this.view.getLayoutParams();
            return getTargetDimen(this.view.getHeight(), layoutParams != null ? layoutParams.height : 0, paddingTop);
        }

        private int getTargetWidth() {
            int paddingLeft = this.view.getPaddingLeft() + this.view.getPaddingRight();
            LayoutParams layoutParams = this.view.getLayoutParams();
            return getTargetDimen(this.view.getWidth(), layoutParams != null ? layoutParams.width : 0, paddingLeft);
        }

        private boolean isDimensionValid(int i) {
            return i > 0 || i == Integer.MIN_VALUE;
        }

        private boolean isViewStateAndSizeValid(int i, int i2) {
            return isDimensionValid(i) && isDimensionValid(i2);
        }

        private void notifyCbs(int i, int i2) {
            Iterator it = new ArrayList(this.cbs).iterator();
            while (it.hasNext()) {
                ((SizeReadyCallback) it.next()).onSizeReady(i, i2);
            }
        }

        /* access modifiers changed from: 0000 */
        public void checkCurrentDimens() {
            if (!this.cbs.isEmpty()) {
                int targetWidth = getTargetWidth();
                int targetHeight = getTargetHeight();
                if (isViewStateAndSizeValid(targetWidth, targetHeight)) {
                    notifyCbs(targetWidth, targetHeight);
                    clearCallbacksAndListener();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void clearCallbacksAndListener() {
            ViewTreeObserver viewTreeObserver = this.view.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeOnPreDrawListener(this.layoutListener);
            }
            this.layoutListener = null;
            this.cbs.clear();
        }

        /* access modifiers changed from: 0000 */
        public void getSize(@NonNull SizeReadyCallback sizeReadyCallback) {
            int targetWidth = getTargetWidth();
            int targetHeight = getTargetHeight();
            if (isViewStateAndSizeValid(targetWidth, targetHeight)) {
                sizeReadyCallback.onSizeReady(targetWidth, targetHeight);
                return;
            }
            if (!this.cbs.contains(sizeReadyCallback)) {
                this.cbs.add(sizeReadyCallback);
            }
            if (this.layoutListener == null) {
                ViewTreeObserver viewTreeObserver = this.view.getViewTreeObserver();
                this.layoutListener = new SizeDeterminerLayoutListener(this);
                viewTreeObserver.addOnPreDrawListener(this.layoutListener);
            }
        }

        /* access modifiers changed from: 0000 */
        public void removeCallback(@NonNull SizeReadyCallback sizeReadyCallback) {
            this.cbs.remove(sizeReadyCallback);
        }
    }

    public ViewTarget(@NonNull View view2) {
        Preconditions.checkNotNull(view2);
        this.view = view2;
        this.sizeDeterminer = new SizeDeterminer(view2);
    }

    @Deprecated
    public ViewTarget(@NonNull View view2, boolean z) {
        this(view2);
        if (z) {
            waitForLayout();
        }
    }

    @Nullable
    private Object getTag() {
        return this.view.getTag(tagId);
    }

    private void maybeAddAttachStateListener() {
        OnAttachStateChangeListener onAttachStateChangeListener = this.attachStateListener;
        if (onAttachStateChangeListener != null && !this.isAttachStateListenerAdded) {
            this.view.addOnAttachStateChangeListener(onAttachStateChangeListener);
            this.isAttachStateListenerAdded = true;
        }
    }

    private void maybeRemoveAttachStateListener() {
        OnAttachStateChangeListener onAttachStateChangeListener = this.attachStateListener;
        if (onAttachStateChangeListener != null && this.isAttachStateListenerAdded) {
            this.view.removeOnAttachStateChangeListener(onAttachStateChangeListener);
            this.isAttachStateListenerAdded = false;
        }
    }

    private void setTag(@Nullable Object obj) {
        isTagUsedAtLeastOnce = true;
        this.view.setTag(tagId, obj);
    }

    @Deprecated
    public static void setTagId(int i) {
        if (!isTagUsedAtLeastOnce) {
            tagId = i;
            return;
        }
        throw new IllegalArgumentException("You cannot set the tag id more than once or change the tag id after the first request has been made");
    }

    @NonNull
    public final ViewTarget clearOnDetach() {
        if (this.attachStateListener != null) {
            return this;
        }
        this.attachStateListener = new OnAttachStateChangeListener() {
            public void onViewAttachedToWindow(View view) {
                ViewTarget.this.resumeMyRequest();
            }

            public void onViewDetachedFromWindow(View view) {
                ViewTarget.this.pauseMyRequest();
            }
        };
        maybeAddAttachStateListener();
        return this;
    }

    @Nullable
    public Request getRequest() {
        Object tag = getTag();
        if (tag == null) {
            return null;
        }
        if (tag instanceof Request) {
            return (Request) tag;
        }
        throw new IllegalArgumentException("You must not call setTag() on a view Glide is targeting");
    }

    @CallSuper
    public void getSize(@NonNull SizeReadyCallback sizeReadyCallback) {
        this.sizeDeterminer.getSize(sizeReadyCallback);
    }

    @NonNull
    public View getView() {
        return this.view;
    }

    @CallSuper
    public void onLoadCleared(@Nullable Drawable drawable) {
        super.onLoadCleared(drawable);
        this.sizeDeterminer.clearCallbacksAndListener();
        if (!this.isClearedByUs) {
            maybeRemoveAttachStateListener();
        }
    }

    @CallSuper
    public void onLoadStarted(@Nullable Drawable drawable) {
        super.onLoadStarted(drawable);
        maybeAddAttachStateListener();
    }

    /* access modifiers changed from: 0000 */
    public void pauseMyRequest() {
        Request request = getRequest();
        if (request != null) {
            this.isClearedByUs = true;
            request.clear();
            this.isClearedByUs = false;
        }
    }

    @CallSuper
    public void removeCallback(@NonNull SizeReadyCallback sizeReadyCallback) {
        this.sizeDeterminer.removeCallback(sizeReadyCallback);
    }

    /* access modifiers changed from: 0000 */
    public void resumeMyRequest() {
        Request request = getRequest();
        if (request != null && request.isCleared()) {
            request.begin();
        }
    }

    public void setRequest(@Nullable Request request) {
        setTag(request);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Target for: ");
        sb.append(this.view);
        return sb.toString();
    }

    @NonNull
    public final ViewTarget waitForLayout() {
        this.sizeDeterminer.waitForLayout = true;
        return this;
    }
}

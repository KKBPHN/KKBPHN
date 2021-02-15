package com.miui.internal.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import com.miui.internal.variable.Android_View_Accessibility_AccessibilityEvent_class.Factory;
import java.util.ArrayList;
import java.util.List;

public abstract class ExploreByTouchHelper extends AccessibilityDelegate {
    private static final int CONTENT_CHANGE_TYPE_SUBTREE = 1;
    private static final int CONTENT_CHANGE_TYPE_UNDEFINED = 0;
    private static final String DEFAULT_CLASS_NAME = View.class.getName();
    public static final int HOST_ID = -1;
    public static final int INVALID_ID = Integer.MIN_VALUE;
    private static final Rect INVALID_PARENT_BOUNDS = new Rect(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    private final Context mContext;
    private int mFocusedVirtualViewId = Integer.MIN_VALUE;
    private int mHoveredVirtualViewId = Integer.MIN_VALUE;
    private final AccessibilityManager mManager;
    private ExploreByTouchNodeProvider mNodeProvider;
    private List mTempArray;
    private int[] mTempGlobalRect;
    private Rect mTempParentRect;
    private Rect mTempScreenRect;
    private Rect mTempVisibleRect;
    private final View mView;

    class ExploreByTouchNodeProvider extends AccessibilityNodeProvider {
        private ExploreByTouchNodeProvider() {
        }

        public AccessibilityNodeInfo createAccessibilityNodeInfo(int i) {
            return ExploreByTouchHelper.this.createNode(i);
        }

        public boolean performAction(int i, int i2, Bundle bundle) {
            return ExploreByTouchHelper.this.performAction(i, i2, bundle);
        }
    }

    public ExploreByTouchHelper(View view) {
        if (view != null) {
            this.mView = view;
            this.mContext = view.getContext();
            this.mManager = (AccessibilityManager) this.mContext.getSystemService("accessibility");
            return;
        }
        throw new IllegalArgumentException("View may not be null");
    }

    private boolean clearAccessibilityFocus(int i) {
        if (!isAccessibilityFocused(i)) {
            return false;
        }
        this.mFocusedVirtualViewId = Integer.MIN_VALUE;
        this.mView.invalidate();
        sendEventForVirtualView(i, 65536);
        return true;
    }

    private AccessibilityEvent createEvent(int i, int i2) {
        return i != -1 ? createEventForChild(i, i2) : createEventForHost(i2);
    }

    private AccessibilityEvent createEventForChild(int i, int i2) {
        AccessibilityEvent obtain = AccessibilityEvent.obtain(i2);
        obtain.setEnabled(true);
        obtain.setClassName(DEFAULT_CLASS_NAME);
        onPopulateEventForVirtualView(i, obtain);
        if (!obtain.getText().isEmpty() || obtain.getContentDescription() != null) {
            obtain.setPackageName(this.mView.getContext().getPackageName());
            obtain.setSource(this.mView, i);
            return obtain;
        }
        throw new RuntimeException("Callbacks must add text or a content description in populateEventForVirtualViewId()");
    }

    private AccessibilityEvent createEventForHost(int i) {
        AccessibilityEvent obtain = AccessibilityEvent.obtain(i);
        this.mView.onInitializeAccessibilityEvent(obtain);
        onPopulateEventForHost(obtain);
        return obtain;
    }

    /* access modifiers changed from: private */
    public AccessibilityNodeInfo createNode(int i) {
        return i != -1 ? createNodeForChild(i) : createNodeForHost();
    }

    private AccessibilityNodeInfo createNodeForChild(int i) {
        ensureTempRects();
        Rect rect = this.mTempParentRect;
        int[] iArr = this.mTempGlobalRect;
        Rect rect2 = this.mTempScreenRect;
        AccessibilityNodeInfo obtain = AccessibilityNodeInfo.obtain();
        obtain.setEnabled(true);
        obtain.setClassName(DEFAULT_CLASS_NAME);
        obtain.setBoundsInParent(INVALID_PARENT_BOUNDS);
        onPopulateNodeForVirtualView(i, obtain);
        if (obtain.getText() == null && obtain.getContentDescription() == null) {
            throw new RuntimeException("Callbacks must add text or a content description in populateNodeForVirtualViewId()");
        }
        obtain.getBoundsInParent(rect);
        if (!rect.equals(INVALID_PARENT_BOUNDS)) {
            int actions = obtain.getActions();
            if ((actions & 64) != 0) {
                throw new RuntimeException("Callbacks must not add ACTION_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
            } else if ((actions & 128) == 0) {
                obtain.setPackageName(this.mView.getContext().getPackageName());
                obtain.setSource(this.mView, i);
                obtain.setParent(this.mView);
                if (this.mFocusedVirtualViewId == i) {
                    obtain.setAccessibilityFocused(true);
                    obtain.addAction(128);
                } else {
                    obtain.setAccessibilityFocused(false);
                    obtain.addAction(64);
                }
                if (intersectVisibleToUser(rect)) {
                    obtain.setVisibleToUser(true);
                    obtain.setBoundsInParent(rect);
                }
                this.mView.getLocationOnScreen(iArr);
                int scrollX = iArr[0] - this.mView.getScrollX();
                int scrollY = iArr[1] - this.mView.getScrollY();
                rect2.set(rect);
                rect2.offset(scrollX, scrollY);
                obtain.setBoundsInScreen(rect2);
                return obtain;
            } else {
                throw new RuntimeException("Callbacks must not add ACTION_CLEAR_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
            }
        } else {
            throw new RuntimeException("Callbacks must set parent bounds in populateNodeForVirtualViewId()");
        }
    }

    private AccessibilityNodeInfo createNodeForHost() {
        AccessibilityNodeInfo obtain = AccessibilityNodeInfo.obtain(this.mView);
        this.mView.onInitializeAccessibilityNodeInfo(obtain);
        int childCount = obtain.getChildCount();
        onPopulateNodeForHost(obtain);
        List list = this.mTempArray;
        if (list == null) {
            this.mTempArray = new ArrayList();
        } else {
            list.clear();
        }
        List list2 = this.mTempArray;
        getVisibleVirtualViews(list2);
        if (childCount <= 0 || list2.size() <= 0) {
            int size = list2.size();
            for (int i = 0; i < size; i++) {
                obtain.addChild(this.mView, ((Integer) list2.get(i)).intValue());
            }
            return obtain;
        }
        throw new RuntimeException("Views cannot have both real and virtual children");
    }

    private void ensureTempRects() {
        this.mTempGlobalRect = new int[2];
        this.mTempParentRect = new Rect();
        this.mTempScreenRect = new Rect();
    }

    private boolean intersectVisibleToUser(Rect rect) {
        if (rect == null || rect.isEmpty() || this.mView.getWindowVisibility() != 0) {
            return false;
        }
        View view = this.mView;
        do {
            ViewParent parent = view.getParent();
            if (parent instanceof View) {
                view = (View) parent;
                if (view.getAlpha() <= 0.0f) {
                    break;
                }
            } else if (parent == null) {
                return false;
            } else {
                if (this.mTempVisibleRect == null) {
                    this.mTempVisibleRect = new Rect();
                }
                Rect rect2 = this.mTempVisibleRect;
                if (!this.mView.getLocalVisibleRect(rect2)) {
                    return false;
                }
                return rect.intersect(rect2);
            }
        } while (view.getVisibility() == 0);
        return false;
    }

    private boolean isAccessibilityFocused(int i) {
        return this.mFocusedVirtualViewId == i;
    }

    private boolean manageFocusForChild(int i, int i2) {
        if (i2 == 64) {
            return requestAccessibilityFocus(i);
        }
        if (i2 != 128) {
            return false;
        }
        return clearAccessibilityFocus(i);
    }

    /* access modifiers changed from: private */
    public boolean performAction(int i, int i2, Bundle bundle) {
        return i != -1 ? performActionForChild(i, i2, bundle) : performActionForHost(i2, bundle);
    }

    private boolean performActionForChild(int i, int i2, Bundle bundle) {
        return (i2 == 64 || i2 == 128) ? manageFocusForChild(i, i2) : onPerformActionForVirtualView(i, i2, bundle);
    }

    private boolean performActionForHost(int i, Bundle bundle) {
        return this.mView.performAccessibilityAction(i, bundle);
    }

    private boolean requestAccessibilityFocus(int i) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) this.mContext.getSystemService("accessibility");
        if (!this.mManager.isEnabled() || !accessibilityManager.isTouchExplorationEnabled() || isAccessibilityFocused(i)) {
            return false;
        }
        int i2 = this.mFocusedVirtualViewId;
        if (i2 != Integer.MIN_VALUE) {
            sendEventForVirtualView(i2, 65536);
        }
        this.mFocusedVirtualViewId = i;
        this.mView.invalidate();
        sendEventForVirtualView(i, 32768);
        return true;
    }

    private void updateHoveredVirtualView(int i) {
        int i2 = this.mHoveredVirtualViewId;
        if (i2 != i) {
            this.mHoveredVirtualViewId = i;
            sendEventForVirtualView(i, 128);
            sendEventForVirtualView(i2, 256);
        }
    }

    public boolean dispatchHoverEvent(MotionEvent motionEvent) {
        boolean z = false;
        if (this.mManager.isEnabled() && this.mManager.isTouchExplorationEnabled()) {
            int action = motionEvent.getAction();
            if (action == 7 || action == 9) {
                int virtualViewAt = getVirtualViewAt(motionEvent.getX(), motionEvent.getY());
                updateHoveredVirtualView(virtualViewAt);
                if (virtualViewAt != Integer.MIN_VALUE) {
                    z = true;
                }
            } else if (action != 10 || this.mFocusedVirtualViewId == Integer.MIN_VALUE) {
                return false;
            } else {
                updateHoveredVirtualView(Integer.MIN_VALUE);
                return true;
            }
        }
        return z;
    }

    public AccessibilityNodeProvider getAccessibilityNodeProvider(View view) {
        if (this.mNodeProvider == null) {
            this.mNodeProvider = new ExploreByTouchNodeProvider();
        }
        return this.mNodeProvider;
    }

    public int getFocusedVirtualView() {
        return this.mFocusedVirtualViewId;
    }

    public abstract int getVirtualViewAt(float f, float f2);

    public abstract void getVisibleVirtualViews(List list);

    public void invalidateRoot() {
        invalidateVirtualView(-1, 1);
    }

    public void invalidateVirtualView(int i) {
        invalidateVirtualView(i, 0);
    }

    public void invalidateVirtualView(int i, int i2) {
        if (i != Integer.MIN_VALUE && this.mManager.isEnabled()) {
            ViewParent parent = this.mView.getParent();
            if (parent != null) {
                AccessibilityEvent createEvent = createEvent(i, 2048);
                Factory.getInstance().get().setContentChangeTypes(createEvent, i2);
                parent.requestSendAccessibilityEvent(this.mView, createEvent);
            }
        }
    }

    public abstract boolean onPerformActionForVirtualView(int i, int i2, Bundle bundle);

    /* access modifiers changed from: protected */
    public void onPopulateEventForHost(AccessibilityEvent accessibilityEvent) {
    }

    public abstract void onPopulateEventForVirtualView(int i, AccessibilityEvent accessibilityEvent);

    /* access modifiers changed from: protected */
    public void onPopulateNodeForHost(AccessibilityNodeInfo accessibilityNodeInfo) {
    }

    public abstract void onPopulateNodeForVirtualView(int i, AccessibilityNodeInfo accessibilityNodeInfo);

    public boolean sendEventForVirtualView(int i, int i2) {
        if (i == Integer.MIN_VALUE || !this.mManager.isEnabled()) {
            return false;
        }
        ViewParent parent = this.mView.getParent();
        if (parent == null) {
            return false;
        }
        return parent.requestSendAccessibilityEvent(this.mView, createEvent(i, i2));
    }
}

package com.android.camera.module.impl.component;

import com.android.camera.ActivityBase;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BackStack;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import java.util.Iterator;
import java.util.Stack;

public class BackStackImpl implements BackStack {
    private static final String TAG = "BackStack";
    private ActivityBase mActivity;
    private Stack mStacks = new Stack();

    public BackStackImpl(ActivityBase activityBase) {
        this.mActivity = activityBase;
    }

    public static BackStackImpl create(ActivityBase activityBase) {
        return new BackStackImpl(activityBase);
    }

    private final boolean handleBackStack(int i) {
        if (this.mStacks.isEmpty()) {
            return false;
        }
        Iterator it = this.mStacks.iterator();
        while (it.hasNext()) {
            HandleBackTrace handleBackTrace = (HandleBackTrace) it.next();
            if (handleBackTrace.canProvide()) {
                if (handleBackTrace.onBackEvent(i)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("consume global backEvent ");
                    sb.append(i);
                    sb.append(" | ");
                    sb.append(handleBackTrace.getClass().getSimpleName());
                    Log.u(TAG, sb.toString());
                    return true;
                }
            }
        }
        return false;
    }

    private void refreshBackStack(Stack stack, int i) {
        if (stack != null && !stack.isEmpty()) {
            Object[] array = stack.toArray();
            int length = array.length;
            for (int i2 = 0; i2 < length; i2++) {
                Object obj = array[i2];
                if (obj != null && (obj instanceof HandleBackTrace)) {
                    HandleBackTrace handleBackTrace = (HandleBackTrace) obj;
                    if (handleBackTrace.canProvide()) {
                        handleBackTrace.onBackEvent(i);
                    }
                }
            }
        }
    }

    public void addInBackStack(HandleBackTrace handleBackTrace) {
        this.mStacks.add(handleBackTrace);
    }

    public boolean handleBackStackFromKeyBack() {
        return handleBackStack(1);
    }

    public void handleBackStackFromLongPressShutter() {
        refreshBackStack(this.mStacks, 8);
    }

    public void handleBackStackFromShutter() {
        refreshBackStack(this.mStacks, 3);
    }

    public boolean handleBackStackFromTapDown(int i, int i2) {
        if (!this.mActivity.getCameraScreenNail().getDisplayRect().contains(i, i2)) {
            return false;
        }
        return handleBackStack(2);
    }

    public void handleBackStackFromTimerBurstShutter() {
        refreshBackStack(this.mStacks, 9);
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(171, this);
    }

    public void removeBackStack(HandleBackTrace handleBackTrace) {
        this.mStacks.remove(handleBackTrace);
    }

    public void unRegisterProtocol() {
        this.mStacks.clear();
        this.mActivity = null;
        ModeCoordinatorImpl.getInstance().detachProtocol(171, this);
    }
}

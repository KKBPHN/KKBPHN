package miui.view.springback;

import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

public class SpringBackLayoutHelper {
    private static final int INVALID_POINTER = -1;
    int mActivePointerId = -1;
    float mInitialDownX;
    float mInitialDownY;
    int mScrollOrientation;
    private ViewGroup mTarget;
    int mTargetScrollOrientation;
    private int mTouchSlop;

    public SpringBackLayoutHelper(ViewGroup viewGroup, int i) {
        this.mTarget = viewGroup;
        this.mTargetScrollOrientation = i;
        this.mTouchSlop = ViewConfiguration.get(viewGroup.getContext()).getScaledTouchSlop();
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x000e, code lost:
        if (r0 != 3) goto L_0x0078;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void checkOrientation(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            int i = 1;
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    int i2 = this.mActivePointerId;
                    if (i2 != -1) {
                        int findPointerIndex = motionEvent.findPointerIndex(i2);
                        if (findPointerIndex >= 0) {
                            float y = motionEvent.getY(findPointerIndex);
                            float f = y - this.mInitialDownY;
                            float x = motionEvent.getX(findPointerIndex) - this.mInitialDownX;
                            if (Math.abs(x) > ((float) this.mTouchSlop) || Math.abs(f) > ((float) this.mTouchSlop)) {
                                if (Math.abs(x) <= Math.abs(f)) {
                                    i = 2;
                                }
                                this.mScrollOrientation = i;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                }
            }
            this.mScrollOrientation = 0;
            this.mTarget.requestDisallowInterceptTouchEvent(false);
        } else {
            this.mActivePointerId = motionEvent.getPointerId(0);
            int findPointerIndex2 = motionEvent.findPointerIndex(this.mActivePointerId);
            if (findPointerIndex2 >= 0) {
                this.mInitialDownY = motionEvent.getY(findPointerIndex2);
                this.mInitialDownX = motionEvent.getX(findPointerIndex2);
                this.mScrollOrientation = 0;
            }
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        checkOrientation(motionEvent);
        int i = this.mScrollOrientation;
        if (i == 0 || i == this.mTargetScrollOrientation) {
            this.mTarget.requestDisallowInterceptTouchEvent(false);
            return true;
        }
        this.mTarget.requestDisallowInterceptTouchEvent(true);
        return false;
    }
}

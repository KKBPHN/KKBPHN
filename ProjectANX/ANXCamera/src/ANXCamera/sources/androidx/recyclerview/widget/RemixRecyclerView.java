package androidx.recyclerview.widget;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView.SmoothScroller;
import miuix.animation.utils.VelocityMonitor;
import miuix.core.view.NestedCurrentFling;
import miuix.overscroller.widget.OverScroller;
import miuix.recyclerview.R;
import miuix.view.HapticCompat;
import miuix.view.HapticFeedbackConstants;

abstract class RemixRecyclerView extends RecyclerView {
    private static final int INVALID_POINTER = -1;
    private int mScrollPointerId;
    /* access modifiers changed from: private */
    public boolean mSpringEnabled;
    /* access modifiers changed from: private */
    public VelocityMonitor mVelocityMonitor;

    class ViewFlinger extends ViewFlinger {
        private boolean mEatRunOnAnimationRequest = false;
        private boolean mHasReachEdgeBeforeFling;
        Interpolator mInterpolator = RecyclerView.sQuinticInterpolator;
        private int mLastFlingX;
        private int mLastFlingY;
        OverScroller mOverScroller;
        private boolean mReSchedulePostAnimationCallback = false;

        ViewFlinger() {
            super();
            this.mOverScroller = new OverScroller(RemixRecyclerView.this.getContext(), RecyclerView.sQuinticInterpolator);
        }

        private int computeScrollDuration(int i, int i2, int i3, int i4) {
            int i5;
            int abs = Math.abs(i);
            int abs2 = Math.abs(i2);
            boolean z = abs > abs2;
            int sqrt = (int) Math.sqrt((double) ((i3 * i3) + (i4 * i4)));
            int sqrt2 = (int) Math.sqrt((double) ((i * i) + (i2 * i2)));
            RemixRecyclerView remixRecyclerView = RemixRecyclerView.this;
            int width = z ? remixRecyclerView.getWidth() : remixRecyclerView.getHeight();
            int i6 = width / 2;
            float f = (float) width;
            float f2 = (float) i6;
            float distanceInfluenceForSnapDuration = f2 + (distanceInfluenceForSnapDuration(Math.min(1.0f, (((float) sqrt2) * 1.0f) / f)) * f2);
            if (sqrt > 0) {
                i5 = Math.round(Math.abs(distanceInfluenceForSnapDuration / ((float) sqrt)) * 1000.0f) * 4;
            } else {
                if (!z) {
                    abs = abs2;
                }
                i5 = (int) (((((float) abs) / f) + 1.0f) * 300.0f);
            }
            return Math.min(i5, 2000);
        }

        private float distanceInfluenceForSnapDuration(float f) {
            return (float) Math.sin((double) ((f - 0.5f) * 0.47123894f));
        }

        private void internalPostOnAnimation() {
            RemixRecyclerView.this.removeCallbacks(this);
            ViewCompat.postOnAnimation(RemixRecyclerView.this, this);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0066, code lost:
            if (r11.this$0.canScrollVertically(r2) == false) goto L_0x006a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0078, code lost:
            if (r11.this$0.canScrollHorizontally(r2) == false) goto L_0x006a;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void fling(int i, int i2) {
            RemixRecyclerView.this.setScrollState(2);
            this.mLastFlingY = 0;
            this.mLastFlingX = 0;
            Interpolator interpolator = this.mInterpolator;
            Interpolator interpolator2 = RecyclerView.sQuinticInterpolator;
            if (interpolator != interpolator2) {
                this.mInterpolator = interpolator2;
                this.mOverScroller = new OverScroller(RemixRecyclerView.this.getContext(), RecyclerView.sQuinticInterpolator);
            }
            if (i != 0) {
                i = -((int) RemixRecyclerView.this.mVelocityMonitor.getVelocity(0));
            }
            int i3 = i;
            boolean z = true;
            if (i2 != 0) {
                i2 = -((int) RemixRecyclerView.this.mVelocityMonitor.getVelocity(1));
            }
            int i4 = i2;
            boolean canScrollHorizontally = RemixRecyclerView.this.mLayout.canScrollHorizontally();
            boolean canScrollVertically = RemixRecyclerView.this.mLayout.canScrollVertically();
            boolean z2 = canScrollHorizontally;
            if (canScrollVertically) {
                z2 |= true;
            }
            int i5 = -1;
            if (!z2) {
                if (z2) {
                    if (i3 > 0) {
                        i5 = 1;
                    }
                }
                this.mOverScroller.fling(0, 0, i3, i4, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
                postOnAnimation();
            } else if (i4 > 0) {
                i5 = 1;
            }
            z = false;
            this.mHasReachEdgeBeforeFling = z;
            this.mOverScroller.fling(0, 0, i3, i4, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            postOnAnimation();
        }

        /* access modifiers changed from: 0000 */
        public void postOnAnimation() {
            if (this.mEatRunOnAnimationRequest) {
                this.mReSchedulePostAnimationCallback = true;
            } else {
                internalPostOnAnimation();
            }
        }

        /* access modifiers changed from: 0000 */
        public void resetFlingPosition() {
            this.mLastFlingY = 0;
            this.mLastFlingX = 0;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:91:0x01ac, code lost:
            if (r0.this$0.canScrollVertically(r4) == false) goto L_0x01ae;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:99:0x01cf, code lost:
            if (r0.this$0.canScrollHorizontally(r4) == false) goto L_0x01ae;
         */
        /* JADX WARNING: Removed duplicated region for block: B:111:0x01f4  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            int i;
            int i2;
            boolean z;
            RemixRecyclerView remixRecyclerView = RemixRecyclerView.this;
            if (remixRecyclerView.mLayout == null) {
                stop();
                return;
            }
            this.mReSchedulePostAnimationCallback = false;
            this.mEatRunOnAnimationRequest = true;
            remixRecyclerView.consumePendingUpdateOperations();
            OverScroller overScroller = this.mOverScroller;
            if (overScroller.computeScrollOffset()) {
                int currX = overScroller.getCurrX();
                int currY = overScroller.getCurrY();
                int i3 = currX - this.mLastFlingX;
                int i4 = currY - this.mLastFlingY;
                this.mLastFlingX = currX;
                this.mLastFlingY = currY;
                RemixRecyclerView remixRecyclerView2 = RemixRecyclerView.this;
                int[] iArr = remixRecyclerView2.mReusableIntPair;
                iArr[0] = 0;
                iArr[1] = 0;
                ViewParent parent = remixRecyclerView2.getParent();
                View findViewById = RemixRecyclerView.this.getRootView().findViewById(16908290);
                while (parent != null && ((!(parent instanceof NestedCurrentFling) || !((NestedCurrentFling) parent).onNestedCurrentFling(this.mOverScroller.getCurrVelocityX(), this.mOverScroller.getCurrVelocityY())) && (!(parent instanceof ViewGroup) || parent != findViewById))) {
                    parent = parent.getParent();
                }
                RemixRecyclerView remixRecyclerView3 = RemixRecyclerView.this;
                if (remixRecyclerView3.dispatchNestedPreScroll(i3, i4, remixRecyclerView3.mReusableIntPair, null, 1)) {
                    int[] iArr2 = RemixRecyclerView.this.mReusableIntPair;
                    i3 -= iArr2[0];
                    i4 -= iArr2[1];
                }
                if (RemixRecyclerView.this.getOverScrollMode() != 2) {
                    RemixRecyclerView.this.considerReleasingGlowsOnScroll(i3, i4);
                }
                RemixRecyclerView remixRecyclerView4 = RemixRecyclerView.this;
                if (remixRecyclerView4.mAdapter != null) {
                    int[] iArr3 = remixRecyclerView4.mReusableIntPair;
                    iArr3[0] = 0;
                    iArr3[1] = 0;
                    remixRecyclerView4.scrollStep(i3, i4, iArr3);
                    RemixRecyclerView remixRecyclerView5 = RemixRecyclerView.this;
                    int[] iArr4 = remixRecyclerView5.mReusableIntPair;
                    i = iArr4[0];
                    i2 = iArr4[1];
                    i3 -= i;
                    i4 -= i2;
                    SmoothScroller smoothScroller = remixRecyclerView5.mLayout.mSmoothScroller;
                    if (smoothScroller != null && !smoothScroller.isPendingInitialRun() && smoothScroller.isRunning()) {
                        int itemCount = RemixRecyclerView.this.mState.getItemCount();
                        if (itemCount == 0) {
                            smoothScroller.stop();
                        } else {
                            if (smoothScroller.getTargetPosition() >= itemCount) {
                                smoothScroller.setTargetPosition(itemCount - 1);
                            }
                            smoothScroller.onAnimation(i, i2);
                        }
                    }
                } else {
                    i2 = 0;
                    i = 0;
                }
                if (!RemixRecyclerView.this.mItemDecorations.isEmpty()) {
                    RemixRecyclerView.this.invalidate();
                }
                RemixRecyclerView remixRecyclerView6 = RemixRecyclerView.this;
                int[] iArr5 = remixRecyclerView6.mReusableIntPair;
                iArr5[0] = 0;
                iArr5[1] = 0;
                remixRecyclerView6.dispatchNestedScroll(i, i2, i3, i4, null, 1, iArr5);
                int[] iArr6 = RemixRecyclerView.this.mReusableIntPair;
                int i5 = i3 - iArr6[0];
                int i6 = i4 - iArr6[1];
                if (!(i == 0 && i2 == 0)) {
                    RemixRecyclerView.this.dispatchOnScrolled(i, i2);
                }
                if (!RemixRecyclerView.this.awakenScrollBars()) {
                    RemixRecyclerView.this.invalidate();
                }
                boolean z2 = overScroller.isFinished() || (((overScroller.getCurrX() == overScroller.getFinalX()) || i5 != 0) && ((overScroller.getCurrY() == overScroller.getFinalY()) || i6 != 0));
                SmoothScroller smoothScroller2 = RemixRecyclerView.this.mLayout.mSmoothScroller;
                boolean z3 = smoothScroller2 != null && smoothScroller2.isPendingInitialRun();
                if (z3 || !z2) {
                    postOnAnimation();
                    RemixRecyclerView remixRecyclerView7 = RemixRecyclerView.this;
                    GapWorker gapWorker = remixRecyclerView7.mGapWorker;
                    if (gapWorker != null) {
                        gapWorker.postFromTraversal(remixRecyclerView7, i, i2);
                    }
                } else {
                    if (RemixRecyclerView.this.getOverScrollMode() != 2) {
                        int currVelocity = (int) overScroller.getCurrVelocity();
                        int i7 = i5 < 0 ? -currVelocity : i5 > 0 ? currVelocity : 0;
                        if (i6 < 0) {
                            currVelocity = -currVelocity;
                        } else if (i6 <= 0) {
                            currVelocity = 0;
                        }
                        RemixRecyclerView.this.absorbGlows(i7, currVelocity);
                    }
                    int i8 = -1;
                    if (!RemixRecyclerView.this.mLayout.canScrollVertically()) {
                        if (RemixRecyclerView.this.mLayout.canScrollHorizontally()) {
                            if (this.mOverScroller.getFinalX() > this.mOverScroller.getStartX()) {
                                i8 = 1;
                            }
                        }
                        z = false;
                        if (!RemixRecyclerView.this.mSpringEnabled && this.mOverScroller.getMode() == 1 && !this.mHasReachEdgeBeforeFling && z) {
                            HapticCompat.performHapticFeedback(RemixRecyclerView.this, HapticFeedbackConstants.MIUI_SCROLL_EDGE);
                        }
                        if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
                            RemixRecyclerView.this.mPrefetchRegistry.clearPrefetchPositions();
                        }
                    } else if (this.mOverScroller.getFinalY() > this.mOverScroller.getStartY()) {
                        i8 = 1;
                    }
                    z = true;
                    HapticCompat.performHapticFeedback(RemixRecyclerView.this, HapticFeedbackConstants.MIUI_SCROLL_EDGE);
                    if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
                    }
                }
            }
            SmoothScroller smoothScroller3 = RemixRecyclerView.this.mLayout.mSmoothScroller;
            if (smoothScroller3 != null && smoothScroller3.isPendingInitialRun()) {
                smoothScroller3.onAnimation(0, 0);
            }
            this.mEatRunOnAnimationRequest = false;
            if (this.mReSchedulePostAnimationCallback) {
                internalPostOnAnimation();
            } else {
                RemixRecyclerView.this.setScrollState(0);
                RemixRecyclerView.this.stopNestedScroll(1);
            }
        }

        public void smoothScrollBy(int i, int i2, int i3, @Nullable Interpolator interpolator) {
            if (i3 == Integer.MIN_VALUE) {
                i3 = computeScrollDuration(i, i2, 0, 0);
            }
            int i4 = i3;
            if (interpolator == null) {
                interpolator = RecyclerView.sQuinticInterpolator;
            }
            if (this.mInterpolator != interpolator) {
                this.mInterpolator = interpolator;
                this.mOverScroller = new OverScroller(RemixRecyclerView.this.getContext(), interpolator);
            }
            this.mLastFlingY = 0;
            this.mLastFlingX = 0;
            RemixRecyclerView.this.setScrollState(2);
            this.mOverScroller.startScroll(0, 0, i, i2, i4);
            if (VERSION.SDK_INT < 23) {
                this.mOverScroller.computeScrollOffset();
            }
            postOnAnimation();
        }

        public void stop() {
            RemixRecyclerView.this.removeCallbacks(this);
            this.mOverScroller.abortAnimation();
        }
    }

    public RemixRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public RemixRecyclerView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.recyclerViewStyle);
    }

    public RemixRecyclerView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mScrollPointerId = -1;
        this.mSpringEnabled = true;
    }

    private void trackVelocity(MotionEvent motionEvent) {
        int i;
        if (this.mVelocityMonitor == null) {
            this.mVelocityMonitor = new VelocityMonitor();
        }
        int actionMasked = motionEvent.getActionMasked();
        int actionIndex = motionEvent.getActionIndex();
        if (actionMasked != 0) {
            int i2 = 1;
            if (actionMasked == 1 || actionMasked == 2) {
                int findPointerIndex = motionEvent.findPointerIndex(this.mScrollPointerId);
                if (findPointerIndex < 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Error processing scroll; pointer index for id ");
                    sb.append(this.mScrollPointerId);
                    sb.append(" not found. Did any MotionEvents get skipped?");
                    Log.e("RecyclerView", sb.toString());
                    return;
                }
                updateVelocity(motionEvent, findPointerIndex);
            } else if (actionMasked != 5) {
                if (actionMasked == 6 && motionEvent.getPointerId(actionIndex) == this.mScrollPointerId) {
                    if (actionIndex != 0) {
                        i2 = 0;
                    }
                    i = motionEvent.getPointerId(i2);
                    this.mScrollPointerId = i;
                    updateVelocity(motionEvent, actionIndex);
                }
            }
        } else {
            this.mVelocityMonitor.clear();
        }
        i = motionEvent.getPointerId(actionIndex);
        this.mScrollPointerId = i;
        updateVelocity(motionEvent, actionIndex);
    }

    private void updateVelocity(MotionEvent motionEvent, int i) {
        int i2 = VERSION.SDK_INT;
        VelocityMonitor velocityMonitor = this.mVelocityMonitor;
        if (i2 >= 29) {
            velocityMonitor.update((double) motionEvent.getRawX(i), (double) motionEvent.getRawY(i));
            return;
        }
        velocityMonitor.update((double) motionEvent.getRawX(), (double) motionEvent.getRawY());
    }

    public boolean getSpringEnabled() {
        return this.mSpringEnabled;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        trackVelocity(motionEvent);
        return super.onInterceptTouchEvent(motionEvent);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        trackVelocity(motionEvent);
        return super.onTouchEvent(motionEvent);
    }

    public void setOverScrollMode(int i) {
        super.setOverScrollMode(i);
        if (i == 2) {
            this.mSpringEnabled = false;
        }
    }

    public void setSpringEnabled(boolean z) {
        this.mSpringEnabled = z;
    }
}

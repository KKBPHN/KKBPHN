package miui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.miui.internal.R;
import com.miui.internal.util.DeviceHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import miui.util.ArrayMap;
import miui.util.AttributeResolver;
import miui.util.ViewUtils;

public class DynamicListView extends ListView {
    private static final int EDGE_OFFSET = 50;
    private static final int FULL_ALPHA = 255;
    private static final int SMOOTH_SCROLL_AMOUNT_AT_EDGE = 5;
    private static final TypeEvaluator sBoundEvaluator = new TypeEvaluator() {
        private Rect mEvaluateRect = new Rect();

        public Rect evaluate(float f, Rect rect, Rect rect2) {
            this.mEvaluateRect.set(interpolate(rect.left, rect2.left, f), interpolate(rect.top, rect2.top, f), interpolate(rect.right, rect2.right, f), interpolate(rect.bottom, rect2.bottom, f));
            return this.mEvaluateRect;
        }

        public int interpolate(int i, int i2, float f) {
            return (int) (((float) i) + (f * ((float) (i2 - i))));
        }
    };
    private final int INVALID_ID = -1;
    private final int INVALID_POINTER_ID = -1;
    /* access modifiers changed from: private */
    public long mAboveItemId = -1;
    private int mActivePointerId = -1;
    /* access modifiers changed from: private */
    public long mBelowItemId = -1;
    /* access modifiers changed from: private */
    public boolean mCellIsMobile = false;
    private int mDownY = -1;
    /* access modifiers changed from: private */
    public long mDuration = 200;
    private int mEdgeOffset = 0;
    /* access modifiers changed from: private */
    public BitmapDrawable mHoverCell;
    private Rect mHoverCellCurrentBounds;
    private Rect mHoverCellOriginalBounds;
    private OnScrollListener mInternalScrollListener = new OnScrollListener() {
        private int mCurrentFirstVisibleItem;
        private int mCurrentScrollState;
        private int mCurrentVisibleItemCount;
        private int mPreviousFirstVisibleItem = -1;
        private int mPreviousVisibleItemCount = -1;

        private void isScrollCompleted() {
            if (this.mCurrentVisibleItemCount > 0 && this.mCurrentScrollState == 0) {
                if (DynamicListView.this.mCellIsMobile && DynamicListView.this.mIsMobileScrolling) {
                    DynamicListView.this.handleMobileCellScroll();
                } else if (DynamicListView.this.mIsWaitingForScrollFinish) {
                    DynamicListView.this.touchEventsEnded();
                }
            }
        }

        public void checkAndHandleFirstVisibleCellChange() {
            if (this.mCurrentFirstVisibleItem != this.mPreviousFirstVisibleItem && DynamicListView.this.mCellIsMobile && DynamicListView.this.mMobileItemId != -1) {
                DynamicListView dynamicListView = DynamicListView.this;
                dynamicListView.updateNeighborViewsForID(dynamicListView.mMobileItemId);
                DynamicListView.this.handleCellSwitch();
            }
        }

        public void checkAndHandleLastVisibleCellChange() {
            if (this.mCurrentFirstVisibleItem + this.mCurrentVisibleItemCount != this.mPreviousFirstVisibleItem + this.mPreviousVisibleItemCount && DynamicListView.this.mCellIsMobile && DynamicListView.this.mMobileItemId != -1) {
                DynamicListView dynamicListView = DynamicListView.this;
                dynamicListView.updateNeighborViewsForID(dynamicListView.mMobileItemId);
                DynamicListView.this.handleCellSwitch();
            }
        }

        public void onScroll(AbsListView absListView, int i, int i2, int i3) {
            if (DynamicListView.this.mOnScrollListener != null) {
                DynamicListView.this.mOnScrollListener.onScroll(absListView, i, i2, i3);
            }
            this.mCurrentFirstVisibleItem = i;
            this.mCurrentVisibleItemCount = i2;
            int i4 = this.mPreviousFirstVisibleItem;
            if (i4 == -1) {
                i4 = this.mCurrentFirstVisibleItem;
            }
            this.mPreviousFirstVisibleItem = i4;
            int i5 = this.mPreviousVisibleItemCount;
            if (i5 == -1) {
                i5 = this.mCurrentVisibleItemCount;
            }
            this.mPreviousVisibleItemCount = i5;
            checkAndHandleFirstVisibleCellChange();
            checkAndHandleLastVisibleCellChange();
            this.mPreviousFirstVisibleItem = this.mCurrentFirstVisibleItem;
            this.mPreviousVisibleItemCount = this.mCurrentVisibleItemCount;
        }

        public void onScrollStateChanged(AbsListView absListView, int i) {
            if (DynamicListView.this.mOnScrollListener != null) {
                DynamicListView.this.mOnScrollListener.onScrollStateChanged(absListView, i);
            }
            this.mCurrentScrollState = i;
            DynamicListView.this.mScrollState = i;
            isScrollCompleted();
        }
    };
    /* access modifiers changed from: private */
    public boolean mIsMobileScrolling = false;
    /* access modifiers changed from: private */
    public boolean mIsWaitingForScrollFinish = false;
    /* access modifiers changed from: private */
    public Map mItemIdTopMap = new ArrayMap();
    private int mLargeSmoothScrollAmount;
    private int mLastEventY = -1;
    private Rect mLastShadowBounds = new Rect();
    /* access modifiers changed from: private */
    public Bitmap mLastStateBitmap;
    /* access modifiers changed from: private */
    public long mMobileItemId = -1;
    private OnItemRemoveListener mOnItemRemoveListener;
    /* access modifiers changed from: private */
    public OnScrollListener mOnScrollListener;
    /* access modifiers changed from: private */
    public Paint mPaint = new Paint();
    /* access modifiers changed from: private */
    public RearrangeListener mRearrangeListener;
    /* access modifiers changed from: private */
    public ObjectAnimator mScaleAnimator;
    private float mScaleFactor = 0.0f;
    private Runnable mScrollListRunnable = new Runnable() {
        public void run() {
            if (DynamicListView.this.mIsMobileScrolling) {
                DynamicListView dynamicListView = DynamicListView.this;
                dynamicListView.smoothScrollBy(dynamicListView.mSmoothScrollAmountAtEdge, 10);
                DynamicListView.this.removeCallbacks(this);
                DynamicListView.this.postDelayed(this, 5);
            }
        }
    };
    /* access modifiers changed from: private */
    public int mScrollState = 0;
    private Rect mShadowBounds = new Rect();
    /* access modifiers changed from: private */
    public Drawable mShadowDrawable;
    private int mShadowHeight;
    private int mSmallSmoothScrollAmount;
    /* access modifiers changed from: private */
    public int mSmoothScrollAmountAtEdge = 0;
    private int mTotalOffset = 0;
    /* access modifiers changed from: private */
    public ObjectAnimator mTouchEndAnimator;
    private AnimatorListenerAdapter mTouchEndAnimatorListener = new AnimatorListenerAdapter() {
        private boolean mCanceled;

        public void onAnimationCancel(Animator animator) {
            super.onAnimationCancel(animator);
            DynamicListView.this.mTouchEndAnimator = null;
            this.mCanceled = true;
        }

        public void onAnimationEnd(Animator animator) {
            DynamicListView dynamicListView = DynamicListView.this;
            View viewForId = dynamicListView.getViewForId(dynamicListView.mMobileItemId);
            if (viewForId != null) {
                viewForId.setVisibility(0);
            }
            DynamicListView.this.mAboveItemId = -1;
            DynamicListView.this.mMobileItemId = -1;
            DynamicListView.this.mBelowItemId = -1;
            DynamicListView.this.mHoverCell = null;
            DynamicListView.this.setEnabled(true);
            DynamicListView.this.invalidate();
            DynamicListView.this.mTouchEndAnimator = null;
            if (!this.mCanceled && DynamicListView.this.mRearrangeListener != null) {
                DynamicListView.this.mRearrangeListener.onDragEnd();
            }
        }

        public void onAnimationStart(Animator animator) {
            DynamicListView.this.setEnabled(false);
            this.mCanceled = false;
        }
    };

    public interface OnItemRemoveListener {
        void onItemRemove(List list);
    }

    public interface RearrangeListener {
        void onDragEnd();

        void onDragStart();

        void onOrderChanged(int i, int i2);
    }

    public DynamicListView(Context context) {
        super(context);
        init(context);
    }

    public DynamicListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public DynamicListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    static /* synthetic */ int access$1812(DynamicListView dynamicListView, int i) {
        int i2 = dynamicListView.mTotalOffset + i;
        dynamicListView.mTotalOffset = i2;
        return i2;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<java.lang.Integer>, for r10v0, types: [java.util.List, java.util.List<java.lang.Integer>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void animateRemoval(List list, final List<Integer> list2) {
        final BaseAdapter baseAdapter = (BaseAdapter) getAdapter();
        ArrayList arrayList = new ArrayList(1);
        for (Integer intValue : list2) {
            View viewForPosition = getViewForPosition(intValue.intValue());
            if (viewForPosition != null) {
                arrayList.add(viewForPosition);
            }
        }
        int firstVisiblePosition = getFirstVisiblePosition();
        this.mItemIdTopMap.clear();
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            childAt.setTranslationX(0.0f);
            if (!arrayList.contains(childAt)) {
                this.mItemIdTopMap.put(Long.valueOf(baseAdapter.getItemId(firstVisiblePosition + i)), Integer.valueOf(childAt.getTop()));
            }
        }
        this.mOnItemRemoveListener.onItemRemove(list);
        baseAdapter.notifyDataSetChanged();
        getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
                DynamicListView.this.getViewTreeObserver().removeOnPreDrawListener(this);
                int firstVisiblePosition = DynamicListView.this.getFirstVisiblePosition();
                int childCount = DynamicListView.this.getChildCount();
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < childCount; i++) {
                    final View childAt = DynamicListView.this.getChildAt(i);
                    ObjectAnimator ofFloat = ObjectAnimator.ofFloat(childAt, View.TRANSLATION_Y, new float[]{0.0f});
                    ofFloat.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationCancel(Animator animator) {
                            super.onAnimationCancel(animator);
                            childAt.setLayerType(0, null);
                        }

                        public void onAnimationEnd(Animator animator) {
                            super.onAnimationEnd(animator);
                            childAt.setLayerType(0, null);
                        }

                        public void onAnimationStart(Animator animator) {
                            super.onAnimationStart(animator);
                            childAt.setLayerType(2, null);
                        }
                    });
                    arrayList.add(ofFloat);
                    Integer num = (Integer) DynamicListView.this.mItemIdTopMap.get(Long.valueOf(baseAdapter.getItemId(firstVisiblePosition + i)));
                    int top = childAt.getTop();
                    if (num == null) {
                        num = Integer.valueOf(((childAt.getHeight() + DynamicListView.this.getDividerHeight()) * list2.size()) + top);
                    } else if (num.intValue() == top) {
                    }
                    childAt.setTranslationY((float) (num.intValue() - top));
                }
                DynamicListView.this.mItemIdTopMap.clear();
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(arrayList);
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        DynamicListView.this.setEnabled(true);
                    }
                });
                animatorSet.setDuration(DynamicListView.this.mDuration);
                animatorSet.start();
                return true;
            }
        });
    }

    private BitmapDrawable getAndAddHoverView(View view) {
        int width = view.getWidth();
        int height = view.getHeight();
        int top = view.getTop();
        int left = view.getLeft();
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), getBitmapFromView(view));
        this.mHoverCellOriginalBounds = new Rect(left, top, width + left, height + top);
        this.mHoverCellCurrentBounds = new Rect(this.mHoverCellOriginalBounds);
        bitmapDrawable.setBounds(this.mHoverCellCurrentBounds);
        return bitmapDrawable;
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_8888);
        view.draw(new Canvas(createBitmap));
        return createBitmap;
    }

    private View getViewForPosition(int i) {
        int firstVisiblePosition = getFirstVisiblePosition();
        if (i < firstVisiblePosition || i > getLastVisiblePosition()) {
            return null;
        }
        return getChildAt(i - firstVisiblePosition);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<java.lang.Integer>, for r3v0, types: [java.util.List, java.util.List<java.lang.Integer>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private List getViewsForPosition(List<Integer> list) {
        ArrayList arrayList = new ArrayList(1);
        for (Integer intValue : list) {
            View viewForPosition = getViewForPosition(intValue.intValue());
            if (viewForPosition != null) {
                arrayList.add(viewForPosition);
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: private */
    public void handleCellSwitch() {
        final int i = this.mLastEventY - this.mDownY;
        int i2 = this.mHoverCellOriginalBounds.top + this.mTotalOffset + i;
        int height = this.mHoverCellCurrentBounds.height() / 2;
        View viewForId = getViewForId(this.mBelowItemId);
        View viewForId2 = getViewForId(this.mMobileItemId);
        View viewForId3 = getViewForId(this.mAboveItemId);
        boolean z = true;
        boolean z2 = viewForId != null && i2 + height > viewForId.getTop();
        if (viewForId3 == null || i2 - height >= viewForId3.getTop()) {
            z = false;
        }
        if (viewForId2 == null) {
            return;
        }
        if (z2 || z) {
            long j = z2 ? this.mBelowItemId : this.mAboveItemId;
            if (!z2) {
                viewForId = viewForId3;
            }
            int positionForView = getPositionForView(viewForId2);
            RearrangeListener rearrangeListener = this.mRearrangeListener;
            if (rearrangeListener != null) {
                rearrangeListener.onOrderChanged(positionForView, getPositionForView(viewForId));
            }
            ((BaseAdapter) getAdapter()).notifyDataSetChanged();
            this.mDownY = this.mLastEventY;
            final int top = viewForId.getTop();
            viewForId2.setVisibility(0);
            viewForId.setVisibility(4);
            updateNeighborViewsForID(this.mMobileItemId);
            ViewTreeObserver viewTreeObserver = getViewTreeObserver();
            final ViewTreeObserver viewTreeObserver2 = viewTreeObserver;
            final long j2 = j;
            AnonymousClass7 r2 = new OnPreDrawListener() {
                public boolean onPreDraw() {
                    viewTreeObserver2.removeOnPreDrawListener(this);
                    View viewForId = DynamicListView.this.getViewForId(j2);
                    if (viewForId != null) {
                        DynamicListView.access$1812(DynamicListView.this, i);
                        viewForId.setTranslationY((float) (top - viewForId.getTop()));
                        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(viewForId, View.TRANSLATION_Y, new float[]{0.0f});
                        ofFloat.setDuration(DynamicListView.this.mDuration);
                        ofFloat.start();
                    }
                    return true;
                }
            };
            viewTreeObserver.addOnPreDrawListener(r2);
        }
    }

    /* access modifiers changed from: private */
    public void handleMobileCellScroll() {
        this.mIsMobileScrolling = handleMobileCellScroll(this.mHoverCellCurrentBounds);
    }

    private void init(Context context) {
        super.setOnScrollListener(this.mInternalScrollListener);
        Resources resources = getResources();
        float f = resources.getDisplayMetrics().density;
        this.mSmallSmoothScrollAmount = (int) (5.0f * f);
        this.mLargeSmoothScrollAmount = (int) (((float) this.mSmallSmoothScrollAmount) * 1.5f);
        this.mEdgeOffset = (int) (f * 50.0f);
        this.mShadowDrawable = AttributeResolver.resolveDrawable(context, R.attr.dynamicListviewDraggingItemShadow);
        if (this.mShadowDrawable == null) {
            this.mShadowDrawable = resources.getDrawable(ViewUtils.isNightMode(context) ? R.drawable.dynamic_listview_dragging_item_shadow_dark : R.drawable.dynamic_listview_dragging_item_shadow_light);
        }
        this.mShadowHeight = this.mShadowDrawable.getIntrinsicHeight();
    }

    private void makeScalingAnimation() {
        Rect rect = new Rect(this.mHoverCellOriginalBounds);
        int height = (int) (((float) rect.height()) * this.mScaleFactor * 0.5f);
        rect.set(rect.left, rect.top - height, rect.right + (((int) (((float) rect.width()) * this.mScaleFactor * 0.5f)) * 2), rect.bottom + height);
        this.mHoverCellCurrentBounds.set(rect);
        this.mHoverCellOriginalBounds.set(rect);
        this.mScaleAnimator = ObjectAnimator.ofObject(this, "HoverCellBounds", sBoundEvaluator, new Object[]{rect});
        this.mScaleAnimator.setDuration(DeviceHelper.FEATURE_WHOLE_ANIM ? this.mDuration : 0);
        this.mScaleAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                DynamicListView.this.mShadowDrawable.setAlpha((int) (valueAnimator.getAnimatedFraction() * 255.0f));
            }
        });
        this.mScaleAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator animator) {
                super.onAnimationCancel(animator);
                DynamicListView.this.mScaleAnimator = null;
            }

            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                DynamicListView.this.mScaleAnimator = null;
            }
        });
        this.mScaleAnimator.start();
    }

    private void touchEventsCancelled() {
        View viewForId = getViewForId(this.mMobileItemId);
        if (this.mCellIsMobile) {
            this.mAboveItemId = -1;
            this.mMobileItemId = -1;
            this.mBelowItemId = -1;
            viewForId.setVisibility(0);
            this.mHoverCell = null;
            invalidate();
        }
        this.mCellIsMobile = false;
        this.mIsMobileScrolling = false;
        this.mActivePointerId = -1;
    }

    /* access modifiers changed from: private */
    public void touchEventsEnded() {
        View viewForId = getViewForId(this.mMobileItemId);
        if (this.mCellIsMobile || this.mIsWaitingForScrollFinish) {
            this.mCellIsMobile = false;
            this.mIsWaitingForScrollFinish = false;
            this.mIsMobileScrolling = false;
            this.mActivePointerId = -1;
            if (this.mScrollState != 0) {
                this.mIsWaitingForScrollFinish = true;
                return;
            }
            this.mHoverCellCurrentBounds.offsetTo(this.mHoverCellOriginalBounds.left, viewForId.getTop());
            this.mHoverCellCurrentBounds.set(this.mHoverCellOriginalBounds.left, viewForId.getTop(), this.mHoverCellOriginalBounds.left + viewForId.getWidth(), viewForId.getTop() + viewForId.getHeight());
            this.mTouchEndAnimator = ObjectAnimator.ofObject(this, "HoverCellBounds", sBoundEvaluator, new Object[]{this.mHoverCellCurrentBounds});
            this.mTouchEndAnimator.setDuration(DeviceHelper.FEATURE_WHOLE_ANIM ? this.mDuration : 0);
            this.mTouchEndAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    DynamicListView.this.mShadowDrawable.setAlpha((int) ((1.0f - valueAnimator.getAnimatedFraction()) * 255.0f));
                    DynamicListView.this.invalidate();
                }
            });
            this.mTouchEndAnimator.addListener(this.mTouchEndAnimatorListener);
            this.mTouchEndAnimator.start();
        } else {
            touchEventsCancelled();
        }
    }

    /* access modifiers changed from: private */
    public void updateNeighborViewsForID(long j) {
        int positionForId = getPositionForId(j);
        BaseAdapter baseAdapter = (BaseAdapter) getAdapter();
        this.mAboveItemId = baseAdapter.getItemId(positionForId - 1);
        this.mBelowItemId = baseAdapter.getItemId(positionForId + 1);
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mHoverCell != null) {
            this.mShadowDrawable.draw(canvas);
            this.mHoverCell.draw(canvas);
        }
        Bitmap bitmap = this.mLastStateBitmap;
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, this.mPaint);
        }
    }

    public Rect getHoverCellBounds() {
        return this.mHoverCell.getBounds();
    }

    public int getLastStateAlpha() {
        return this.mPaint.getAlpha();
    }

    public int getPositionForId(long j) {
        View viewForId = getViewForId(j);
        if (viewForId == null) {
            return -1;
        }
        return getPositionForView(viewForId);
    }

    public View getViewForId(long j) {
        int firstVisiblePosition = getFirstVisiblePosition();
        BaseAdapter baseAdapter = (BaseAdapter) getAdapter();
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (baseAdapter.getItemId(firstVisiblePosition + i) == j) {
                return childAt;
            }
        }
        return null;
    }

    public boolean handleMobileCellScroll(Rect rect) {
        int i;
        int computeVerticalScrollOffset = computeVerticalScrollOffset();
        int height = getHeight();
        int computeVerticalScrollExtent = computeVerticalScrollExtent();
        int computeVerticalScrollRange = computeVerticalScrollRange();
        int i2 = rect.top;
        int height2 = rect.height();
        int i3 = this.mEdgeOffset;
        if (i2 > i3 * 2 || computeVerticalScrollOffset <= 0) {
            int i4 = i2 + height2;
            int i5 = this.mEdgeOffset;
            if (i4 < height - (i5 * 2) || computeVerticalScrollOffset + computeVerticalScrollExtent >= computeVerticalScrollRange) {
                removeCallbacks(this.mScrollListRunnable);
                return false;
            }
            this.mSmoothScrollAmountAtEdge = this.mSmallSmoothScrollAmount;
            if (i4 >= height - i5) {
                i = this.mLargeSmoothScrollAmount;
            }
            postDelayed(this.mScrollListRunnable, 10);
            return true;
        }
        this.mSmoothScrollAmountAtEdge = -this.mSmallSmoothScrollAmount;
        if (i2 <= i3) {
            i = -this.mLargeSmoothScrollAmount;
        }
        postDelayed(this.mScrollListRunnable, 10);
        return true;
        this.mSmoothScrollAmountAtEdge = i;
        postDelayed(this.mScrollListRunnable, 10);
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0027, code lost:
        if (r4.getPointerId((r4.getAction() & 65280) >> 8) != r3.mActivePointerId) goto L_0x00a4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & 255;
        if (action != 0) {
            if (action != 1) {
                if (action == 2) {
                    int i = this.mActivePointerId;
                    if (i != -1) {
                        this.mLastEventY = (int) motionEvent.getY(motionEvent.findPointerIndex(i));
                        int i2 = this.mLastEventY - this.mDownY;
                        if (this.mCellIsMobile) {
                            ObjectAnimator objectAnimator = this.mScaleAnimator;
                            if (objectAnimator != null && objectAnimator.isRunning()) {
                                this.mScaleAnimator.end();
                            }
                            int i3 = this.mHoverCellOriginalBounds.top + i2 + this.mTotalOffset;
                            if (i3 < 0) {
                                i3 = 0;
                            } else if (i3 > getHeight() - this.mHoverCellCurrentBounds.height()) {
                                i3 = getHeight() - this.mHoverCellCurrentBounds.height();
                            }
                            this.mHoverCellCurrentBounds.offsetTo(this.mHoverCellOriginalBounds.left, i3);
                            setHoverCellBounds(this.mHoverCellCurrentBounds);
                            handleCellSwitch();
                            this.mIsMobileScrolling = false;
                            handleMobileCellScroll();
                            return false;
                        }
                    }
                } else if (action == 3) {
                    touchEventsCancelled();
                } else if (action == 6) {
                }
            }
            touchEventsEnded();
        } else {
            this.mDownY = (int) motionEvent.getY();
            this.mActivePointerId = motionEvent.getPointerId(0);
        }
        return super.onTouchEvent(motionEvent);
    }

    public void removeItems(List list) {
        removeItems(list, null);
    }

    public void removeItems(List list, List list2) {
        int i;
        char c;
        boolean z;
        List list3 = list;
        List list4 = list2;
        final ArrayList arrayList = new ArrayList(list3);
        final ArrayList arrayList2 = new ArrayList(list.size());
        ArrayList arrayList3 = new ArrayList();
        Iterator it = arrayList.iterator();
        while (true) {
            i = 1;
            c = 0;
            if (!it.hasNext()) {
                z = false;
                break;
            }
            Long l = (Long) it.next();
            int positionForId = getPositionForId(l.longValue());
            if (positionForId == -1) {
                z = true;
                break;
            }
            arrayList2.add(Integer.valueOf(positionForId));
            if (list4 != null && list4.contains(l)) {
                arrayList3.add(Integer.valueOf(positionForId));
            }
        }
        if (z) {
            this.mLastStateBitmap = getBitmapFromView(this);
            this.mOnItemRemoveListener.onItemRemove(list3);
            ((BaseAdapter) getAdapter()).notifyDataSetChanged();
            ObjectAnimator ofInt = ObjectAnimator.ofInt(this, "LastStateAlpha", new int[]{0});
            ofInt.setDuration(this.mDuration);
            ofInt.start();
            ofInt.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    DynamicListView.this.mLastStateBitmap = null;
                    DynamicListView.this.mPaint.setAlpha(255);
                }
            });
            return;
        }
        Collections.sort(arrayList2);
        List viewsForPosition = getViewsForPosition(arrayList2);
        List viewsForPosition2 = getViewsForPosition(arrayList3);
        if (!viewsForPosition.isEmpty()) {
            setEnabled(false);
            AnimatorSet animatorSet = new AnimatorSet();
            ArrayList arrayList4 = new ArrayList(viewsForPosition.size());
            ArrayList arrayList5 = new ArrayList(viewsForPosition.size());
            int size = viewsForPosition.size();
            int i2 = 0;
            while (i2 < size) {
                final View view = (View) viewsForPosition.get(i2);
                int width = viewsForPosition2.contains(view) ? -view.getWidth() : view.getWidth();
                Property property = View.TRANSLATION_X;
                float[] fArr = new float[i];
                fArr[c] = (float) width;
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, property, fArr);
                ofFloat.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationCancel(Animator animator) {
                        super.onAnimationCancel(animator);
                        view.setLayerType(0, null);
                    }

                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        view.setLayerType(0, null);
                    }

                    public void onAnimationStart(Animator animator) {
                        super.onAnimationStart(animator);
                        view.setLayerType(2, null);
                    }
                });
                arrayList4.add(ofFloat);
                ofFloat.setDuration(this.mDuration);
                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
                ofFloat2.setDuration((this.mDuration / 3) * ((long) i2));
                arrayList5.add(ofFloat2);
                i2++;
                i = 1;
                c = 0;
            }
            animatorSet.playTogether(arrayList5);
            for (int i3 = 0; i3 < size; i3++) {
                animatorSet.play((Animator) arrayList4.get(i3)).after((Animator) arrayList5.get(i3));
            }
            animatorSet.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    DynamicListView.this.animateRemoval(arrayList, arrayList2);
                }
            });
            animatorSet.start();
        }
    }

    public void setDuration(long j) {
        this.mDuration = j;
    }

    public void setHoverCellBounds(Rect rect) {
        this.mLastShadowBounds.set(this.mShadowBounds);
        this.mHoverCell.setBounds(rect);
        Rect rect2 = this.mShadowBounds;
        int i = rect.left;
        int i2 = rect.top;
        int i3 = this.mShadowHeight;
        rect2.set(i, i2 - (i3 / 2), rect.right, rect.bottom + (i3 / 2));
        this.mShadowDrawable.setBounds(this.mShadowBounds);
        this.mLastShadowBounds.union(this.mShadowBounds);
        invalidate(this.mLastShadowBounds);
    }

    public void setLastStateAlpha(int i) {
        this.mPaint.setAlpha(i);
        invalidate();
    }

    public void setOnItemRemoveListener(OnItemRemoveListener onItemRemoveListener) {
        this.mOnItemRemoveListener = onItemRemoveListener;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }

    public void setRearrangeListener(RearrangeListener rearrangeListener) {
        this.mRearrangeListener = rearrangeListener;
    }

    public void setScaleFactor(float f) {
        this.mScaleFactor = f;
    }

    public void startDragging(int i) {
        ObjectAnimator objectAnimator = this.mTouchEndAnimator;
        if (objectAnimator != null && objectAnimator.isRunning()) {
            this.mTouchEndAnimator.end();
            this.mTouchEndAnimatorListener.onAnimationEnd(this.mTouchEndAnimator);
        }
        this.mTotalOffset = 0;
        View viewForPosition = getViewForPosition(i);
        this.mMobileItemId = getAdapter().getItemId(i);
        this.mHoverCell = getAndAddHoverView(viewForPosition);
        makeScalingAnimation();
        viewForPosition.setVisibility(4);
        this.mCellIsMobile = true;
        updateNeighborViewsForID(this.mMobileItemId);
        RearrangeListener rearrangeListener = this.mRearrangeListener;
        if (rearrangeListener != null) {
            rearrangeListener.onDragStart();
        }
    }
}

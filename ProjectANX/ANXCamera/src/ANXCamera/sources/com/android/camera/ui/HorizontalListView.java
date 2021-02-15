package com.android.camera.ui;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListAdapter;
import android.widget.Scroller;
import com.android.camera.ActivityBase;
import com.android.camera.Display;
import com.android.camera.Util;
import com.android.camera.lib.compatibility.related.vibrator.ViberatorContext;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import java.util.LinkedList;
import java.util.Queue;

public class HorizontalListView extends AdapterView {
    private static final int MAX_VISIBLE_ITEM_COUNT = 6;
    private static final int MIN_DELTA_FOR_SCROLLING = 10;
    private static final String TAG = "HorizontalListView";
    protected ListAdapter mAdapter;
    /* access modifiers changed from: private */
    public boolean mBlockNotification;
    protected int mCurrentX;
    /* access modifiers changed from: private */
    public boolean mDataChanged = false;
    private DataSetObserver mDataObserver = new DataSetObserver() {
        public void onChanged() {
            synchronized (HorizontalListView.this) {
                HorizontalListView.this.mDataChanged = true;
            }
            HorizontalListView.this.invalidate();
            HorizontalListView.this.requestLayout();
        }

        public void onInvalidated() {
            HorizontalListView.this.reset();
            HorizontalListView.this.invalidate();
            HorizontalListView.this.requestLayout();
        }
    };
    private int mDisplayOffset = 0;
    private GestureDetector mGesture;
    private boolean mIsRecording;
    /* access modifiers changed from: private */
    public boolean mIsScrollingPerformed;
    private int mItemWidth = 180;
    private View mLastSelectImageListItem;
    /* access modifiers changed from: private */
    public int mLeftViewIndex = -1;
    /* access modifiers changed from: private */
    public MainContentProtocol mMainContentProtocol;
    private int mMaxVisibleItemCount = 6;
    private int mMaxX = Integer.MAX_VALUE;
    protected int mNextX;
    private OnGestureListener mOnGesture = new SimpleOnGestureListener() {
        private boolean isEventWithinView(MotionEvent motionEvent, View view) {
            Rect rect = new Rect();
            int[] iArr = new int[2];
            view.getLocationOnScreen(iArr);
            int i = iArr[0];
            int width = view.getWidth() + i;
            int i2 = iArr[1];
            rect.set(i, i2, width, view.getHeight() + i2);
            return rect.contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY());
        }

        public boolean onDown(MotionEvent motionEvent) {
            return HorizontalListView.this.onDown(motionEvent);
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return HorizontalListView.this.onFling(motionEvent, motionEvent2, f, f2);
        }

        public void onLongPress(MotionEvent motionEvent) {
            int childCount = HorizontalListView.this.getChildCount();
            int i = 0;
            while (i < childCount) {
                View childAt = HorizontalListView.this.getChildAt(i);
                if (!isEventWithinView(motionEvent, childAt)) {
                    i++;
                } else if (HorizontalListView.this.mOnItemLongClicked != null) {
                    int access$700 = HorizontalListView.this.toDataIndex(HorizontalListView.this.mLeftViewIndex + 1 + i);
                    OnItemLongClickListener access$900 = HorizontalListView.this.mOnItemLongClicked;
                    HorizontalListView horizontalListView = HorizontalListView.this;
                    access$900.onItemLongClick(horizontalListView, childAt, access$700, horizontalListView.mAdapter.getItemId(access$700));
                    return;
                } else {
                    return;
                }
            }
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if ((HorizontalListView.this.mMainContentProtocol != null && HorizontalListView.this.mMainContentProtocol.isSplitFocusExposureDown()) || !HorizontalListView.this.isTouchMoveEnable()) {
                return false;
            }
            synchronized (HorizontalListView.this) {
                HorizontalListView.this.mNextX += (int) f;
            }
            HorizontalListView.this.mIsScrollingPerformed = true;
            HorizontalListView.this.requestLayout();
            return true;
        }

        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            HorizontalListView.this.mBlockNotification = true;
            int i = 0;
            while (true) {
                if (i >= HorizontalListView.this.getChildCount()) {
                    break;
                }
                View childAt = HorizontalListView.this.getChildAt(i);
                if (isEventWithinView(motionEvent, childAt)) {
                    int access$700 = HorizontalListView.this.toDataIndex(HorizontalListView.this.mLeftViewIndex + 1 + i);
                    if (HorizontalListView.this.mOnItemSelected != null) {
                        OnItemSelectedListener access$800 = HorizontalListView.this.mOnItemSelected;
                        HorizontalListView horizontalListView = HorizontalListView.this;
                        access$800.onItemSelected(horizontalListView, childAt, access$700, horizontalListView.mAdapter.getItemId(access$700));
                    }
                } else {
                    i++;
                }
            }
            return true;
        }
    };
    private OnItemClickListener mOnItemClicked;
    /* access modifiers changed from: private */
    public OnItemLongClickListener mOnItemLongClicked;
    /* access modifiers changed from: private */
    public OnItemSelectedListener mOnItemSelected;
    private int mPaddingWidth;
    private int mPresetWidth = 0;
    private int mPreviousSelectViewIndex = 0;
    private Queue mRemovedViewQueue = new LinkedList();
    private int mRightViewIndex = 0;
    protected Scroller mScroller;
    private boolean mSelectCenter = true;
    private int mSelectViewIndex = 0;
    private boolean mTouchDown;
    private boolean mTouchMoveEnable = true;

    public interface OnSingleTapDownListener {
        void onSingleTapDown(AdapterView adapterView, View view, int i, long j);
    }

    public interface OnValueChangedListener {
        void onValueChanged(int i, boolean z);
    }

    public HorizontalListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    private void addAndMeasureChild(View view, int i) {
        LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(-1, -1);
        }
        addViewInLayout(view, i, layoutParams, true);
        view.measure(MeasureSpec.makeMeasureSpec(getWidth(), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(getHeight(), Integer.MIN_VALUE));
    }

    private void cacheChildItem(View view) {
        if (this.mRemovedViewQueue.size() < 10) {
            this.mRemovedViewQueue.offer(view);
        }
    }

    private void fillList(int i) {
        View childAt = getChildAt(getChildCount() - 1);
        int i2 = 0;
        fillListRight(childAt != null ? childAt.getRight() : 0, i);
        View childAt2 = getChildAt(0);
        if (childAt2 != null) {
            i2 = childAt2.getLeft();
        }
        fillListLeft(i2, i);
    }

    private void fillListLeft(int i, int i2) {
        while (i + i2 > 0) {
            int i3 = this.mLeftViewIndex;
            if (i3 >= 0 && i3 < this.mAdapter.getCount()) {
                View view = this.mAdapter.getView(toDataIndex(this.mLeftViewIndex), (View) this.mRemovedViewQueue.poll(), this);
                if (this.mSelectCenter || this.mLeftViewIndex != this.mSelectViewIndex) {
                    view.setActivated(false);
                } else {
                    this.mLastSelectImageListItem = view;
                    view.setActivated(true);
                }
                addAndMeasureChild(view, 0);
                i -= getChildWidth();
                this.mLeftViewIndex--;
                this.mDisplayOffset -= getChildWidth();
            } else {
                return;
            }
        }
    }

    private void fillListRight(int i, int i2) {
        while (i + i2 < getWidth() && this.mRightViewIndex < this.mAdapter.getCount()) {
            View view = this.mAdapter.getView(toDataIndex(this.mRightViewIndex), (View) this.mRemovedViewQueue.poll(), this);
            if (this.mSelectCenter || this.mRightViewIndex != this.mSelectViewIndex) {
                view.setActivated(false);
            } else {
                this.mLastSelectImageListItem = view;
                view.setActivated(true);
            }
            addAndMeasureChild(view, -1);
            i += getChildWidth();
            if (this.mRightViewIndex == this.mAdapter.getCount() - 1) {
                this.mMaxX = ((this.mPaddingWidth * 2) + (getChildWidth() * this.mAdapter.getCount())) - getWidth();
            }
            if (this.mMaxX < 0) {
                this.mMaxX = 0;
            }
            this.mRightViewIndex++;
        }
    }

    private int getChildWidth() {
        return this.mItemWidth;
    }

    private synchronized void initView() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService("window");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        this.mPresetWidth = displayMetrics.widthPixels;
        this.mLeftViewIndex = -1;
        this.mRightViewIndex = 0;
        this.mCurrentX = 0;
        this.mNextX = 0;
        this.mMaxX = Integer.MAX_VALUE;
        if (this.mSelectCenter) {
            this.mPaddingWidth = (this.mPresetWidth - this.mItemWidth) / 2;
            this.mDisplayOffset = this.mPaddingWidth;
        } else {
            this.mDisplayOffset = 0;
        }
        this.mScroller = new Scroller(getContext());
        this.mGesture = new GestureDetector(getContext(), this.mOnGesture);
        if (this.mLastSelectImageListItem != null) {
            this.mLastSelectImageListItem.setActivated(false);
            this.mLastSelectImageListItem = null;
        }
        ((ActivityBase) getContext()).loadCameraSound(6);
    }

    /* access modifiers changed from: private */
    public void justify(boolean z) {
        int i = this.mSelectViewIndex;
        int i2 = this.mLeftViewIndex;
        boolean z2 = true;
        if (i > i2 && i < this.mRightViewIndex && Math.abs((getChildAt((i - i2) - 1).getLeft() + (this.mItemWidth / 2)) - (this.mPresetWidth / 2)) <= 10) {
            z2 = false;
        }
        if (z2) {
            int i3 = this.mPaddingWidth;
            int i4 = this.mItemWidth;
            int i5 = (((this.mSelectViewIndex * i4) + i3) + (i4 / 2)) - (this.mPresetWidth / 2);
            this.mMaxX = ((i3 * 2) + (i4 * this.mAdapter.getCount())) - this.mPresetWidth;
            int i6 = this.mMaxX;
            if (i5 <= i6) {
                i6 = i5;
            }
            if (i6 != this.mCurrentX) {
                if (z || !isShown()) {
                    this.mNextX = i6;
                    requestLayout();
                } else {
                    scrollTo(i6);
                }
            }
        }
    }

    private void loadItems() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i);
        }
    }

    private void measureChildWidth() {
        if (this.mAdapter != null) {
            int windowWidth = Display.getWindowWidth() / this.mMaxVisibleItemCount;
            this.mItemWidth = windowWidth;
            int count = this.mAdapter.getCount();
            int i = windowWidth;
            for (int i2 = 0; i2 < count; i2++) {
                View view = this.mAdapter.getView(i2, null, this);
                view.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
                if (view.getMeasuredWidth() > i) {
                    i = view.getMeasuredWidth();
                }
            }
            this.mItemWidth = i;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0021, code lost:
        if (r12 != null) goto L_0x0030;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        if (r12 != null) goto L_0x0030;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void notifyItemSelect(View view, int i, long j) {
        View view2;
        if (view != null) {
            if (!this.mBlockNotification) {
                OnItemClickListener onItemClickListener = this.mOnItemClicked;
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(this, view, i, j);
                }
                OnItemSelectedListener onItemSelectedListener = this.mOnItemSelected;
                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(this, view, i, j);
                }
                view2 = this.mLastSelectImageListItem;
            } else {
                if (i == toDataIndex(this.mSelectViewIndex)) {
                    view2 = this.mLastSelectImageListItem;
                }
            }
            view2.setActivated(false);
            this.mLastSelectImageListItem = view;
            view.setActivated(true);
        }
    }

    private void positionItems(int i) {
        if (getChildCount() > 0) {
            this.mDisplayOffset += i;
            int i2 = this.mDisplayOffset;
            int childWidth = getChildWidth();
            int height = getHeight();
            int i3 = this.mPresetWidth / 2;
            int i4 = this.mLeftViewIndex + 1;
            int i5 = i2;
            int i6 = 0;
            while (i6 < getChildCount()) {
                View childAt = getChildAt(i6);
                boolean z = childAt.getLeft() < i3 && childAt.getRight() > i3;
                int i7 = i5 + childWidth;
                childAt.layout(i5, 0, i7, height);
                boolean z2 = i5 < i3 && i7 > i3;
                childAt.setActivated(z2);
                if (this.mSelectCenter && z2 && !z) {
                    int dataIndex = toDataIndex(i4);
                    notifyItemSelect(childAt, dataIndex, this.mAdapter.getItemId(dataIndex));
                }
                i4++;
                i6++;
                i5 = i7;
            }
        }
    }

    private void removeNonVisibleItems(int i) {
        View childAt = getChildAt(0);
        int i2 = 0;
        while (childAt != null && childAt.getRight() + i <= 0) {
            this.mDisplayOffset += getChildWidth();
            cacheChildItem(childAt);
            this.mLeftViewIndex++;
            i2++;
            childAt = getChildAt(i2);
        }
        if (i2 > 0) {
            removeViewsInLayout(0, i2 - 0);
        }
        int childCount = getChildCount() - 1;
        View childAt2 = getChildAt(getChildCount() - 1);
        int i3 = childCount;
        while (childAt2 != null && childAt2.getLeft() + i >= getWidth()) {
            cacheChildItem(childAt2);
            this.mRightViewIndex--;
            i3--;
            childAt2 = getChildAt(i3);
        }
        if (childCount > i3) {
            removeViewsInLayout(i3 + 1, childCount - i3);
        }
    }

    /* access modifiers changed from: private */
    public synchronized void reset() {
        initView();
        removeAllViewsInLayout();
        requestLayout();
    }

    /* access modifiers changed from: private */
    public int toDataIndex(int i) {
        return Util.isLayoutRTL(getContext()) ? (this.mAdapter.getCount() - 1) - i : i;
    }

    private int toViewIndex(int i) {
        return Util.isLayoutRTL(getContext()) ? (this.mAdapter.getCount() - 1) - i : i;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent) | this.mGesture.onTouchEvent(motionEvent);
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mTouchDown = true;
            this.mBlockNotification = false;
        } else if (action == 1 || action == 3) {
            if (this.mScroller.isFinished()) {
                this.mIsScrollingPerformed = false;
                justify(false);
            }
            this.mTouchDown = false;
        }
        return dispatchTouchEvent;
    }

    public ListAdapter getAdapter() {
        return this.mAdapter;
    }

    public View getSelectedView() {
        return null;
    }

    public boolean isScrolling() {
        return this.mIsScrollingPerformed;
    }

    public boolean isTouchMoveEnable() {
        return this.mTouchMoveEnable;
    }

    /* access modifiers changed from: protected */
    public boolean onDown(MotionEvent motionEvent) {
        this.mScroller.forceFinished(true);
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        synchronized (this) {
            this.mScroller.fling(this.mNextX, 0, (int) (-f), 0, 0, this.mMaxX, 0, 0);
        }
        requestLayout();
        return true;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00c4, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00c6, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void onLayout(boolean z, int i, int i2, int i3, int i4) {
        boolean z2;
        super.onLayout(z, i, i2, i3, i4);
        if (this.mAdapter != null) {
            if (this.mAdapter.getCount() != 0) {
                if (this.mDataChanged) {
                    int i5 = this.mCurrentX;
                    initView();
                    removeAllViewsInLayout();
                    this.mNextX = i5;
                    this.mDataChanged = false;
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (this.mScroller.computeScrollOffset()) {
                    this.mNextX = this.mScroller.getCurrX();
                }
                if (this.mNextX <= 0) {
                    this.mNextX = 0;
                    this.mScroller.forceFinished(true);
                }
                if (this.mNextX >= this.mMaxX) {
                    this.mNextX = this.mMaxX;
                    this.mScroller.forceFinished(true);
                }
                int i6 = this.mCurrentX - this.mNextX;
                this.mCurrentX = this.mNextX;
                removeNonVisibleItems(i6);
                fillList(i6);
                positionItems(i6);
                if (this.mScroller.isFinished()) {
                    if (!z2) {
                        loadItems();
                        if (this.mScroller.isFinished() && !this.mTouchDown) {
                            this.mIsScrollingPerformed = false;
                            if (this.mSelectCenter) {
                                post(new Runnable() {
                                    public void run() {
                                        HorizontalListView.this.justify(false);
                                    }
                                });
                            }
                            if (this.mSelectViewIndex != this.mPreviousSelectViewIndex) {
                                if (this.mSelectViewIndex > this.mLeftViewIndex && this.mSelectViewIndex <= this.mRightViewIndex) {
                                    int dataIndex = toDataIndex(this.mSelectViewIndex);
                                    notifyItemSelect(getChildAt((this.mSelectViewIndex - this.mLeftViewIndex) - 1), dataIndex, this.mAdapter.getItemId(dataIndex));
                                }
                                this.mPreviousSelectViewIndex = this.mSelectViewIndex;
                            }
                        }
                    }
                }
                post(new Runnable() {
                    public void run() {
                        HorizontalListView.this.requestLayout();
                    }
                });
            }
        }
    }

    public synchronized void scrollTo(int i) {
        this.mIsScrollingPerformed = true;
        this.mScroller.startScroll(this.mNextX, 0, i - this.mNextX, 0);
        requestLayout();
    }

    @Deprecated
    public void setAdapter(ListAdapter listAdapter) {
        ListAdapter listAdapter2 = this.mAdapter;
        if (listAdapter2 != null) {
            listAdapter2.unregisterDataSetObserver(this.mDataObserver);
            this.mRemovedViewQueue.clear();
        }
        this.mSelectViewIndex = 0;
        this.mPreviousSelectViewIndex = 0;
        this.mAdapter = listAdapter;
        measureChildWidth();
        this.mAdapter.registerDataSetObserver(this.mDataObserver);
        reset();
    }

    public void setAdapterWithInitPostion(ListAdapter listAdapter, int i) {
        setAdapter(listAdapter);
        this.mIsScrollingPerformed = true;
        setSelection(i);
        justify(true);
    }

    public void setIsRecording(boolean z) {
        this.mIsRecording = z;
    }

    public void setItemWidth(int i) {
        this.mItemWidth = i;
        if (this.mSelectCenter) {
            this.mPaddingWidth = (this.mPresetWidth - this.mItemWidth) / 2;
            this.mDisplayOffset = this.mPaddingWidth;
        }
    }

    public void setMainContentProtocol(MainContentProtocol mainContentProtocol) {
        this.mMainContentProtocol = mainContentProtocol;
    }

    public void setMaxVisibleItemCount(int i) {
        this.mMaxVisibleItemCount = i;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClicked = onItemLongClickListener;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.mOnItemSelected = onItemSelectedListener;
    }

    public void setPresetWidth(int i) {
        this.mPresetWidth = i;
    }

    public void setSelection(int i) {
        int viewIndex = toViewIndex(i);
        int i2 = this.mSelectViewIndex;
        if (i2 != viewIndex) {
            this.mPreviousSelectViewIndex = i2;
            this.mSelectViewIndex = viewIndex;
            if (isShown() && !this.mIsRecording) {
                ((ActivityBase) getContext()).playCameraSound(6);
                ViberatorContext.getInstance(getContext().getApplicationContext()).performSlideScaleNormal();
            }
            int i3 = this.mLeftViewIndex;
            if (viewIndex > i3 && viewIndex < this.mRightViewIndex) {
                View childAt = getChildAt((viewIndex - i3) - 1);
                int dataIndex = toDataIndex(viewIndex);
                notifyItemSelect(childAt, dataIndex, this.mAdapter.getItemId(dataIndex));
            }
            if (!this.mIsScrollingPerformed) {
                justify(false);
            }
        }
    }

    public void setTouchMoveEnable(boolean z) {
        this.mTouchMoveEnable = z;
    }
}

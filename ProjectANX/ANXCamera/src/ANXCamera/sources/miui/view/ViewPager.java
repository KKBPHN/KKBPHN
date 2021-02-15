package miui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import com.miui.internal.widget.ActionBarOverlayLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import miui.util.ActionBarUtils;

public class ViewPager extends ViewGroup {
    private static final int CLOSE_ENOUGH = 2;
    private static final Comparator COMPARATOR = new Comparator() {
        public int compare(ItemInfo itemInfo, ItemInfo itemInfo2) {
            return itemInfo.position - itemInfo2.position;
        }
    };
    static final float CURRENT_PAGE_DETERMIN_FACTOR = 0.05f;
    private static final boolean DEBUG = false;
    private static final int DEFAULT_GUTTER_SIZE = 16;
    private static final int DEFAULT_OFFSCREEN_PAGES = 3;
    private static final int INVALID_POINTER = -1;
    /* access modifiers changed from: private */
    public static final int[] LAYOUT_ATTRS = {16842931};
    private static final int MAX_SETTLE_DURATION = 800;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    private static final int PAGE_SETTLE_DURATION = 250;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "ViewPager";
    private static final boolean USE_CACHE = false;
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float f) {
            float f2 = f - 1.0f;
            return (f2 * f2 * f2 * f2 * f2) + 1.0f;
        }
    };
    private int mActivePointerId = -1;
    private PagerAdapter mAdapter;
    private OnAdapterChangeListener mAdapterChangeListener;
    private float mBottomMarginProgress;
    private int mBottomPageBounds;
    private boolean mCalledSuper;
    private int mChildHeightMeasureSpec;
    private int mChildWidthMeasureSpec;
    private int mCloseEnough;
    private int mCurItem;
    private int mDecorChildCount;
    private int mDefaultGutterSize;
    boolean mDragEnabled = true;
    private long mFakeDragBeginTime;
    private boolean mFakeDragging;
    private boolean mFirstLayout = true;
    private float mFirstOffset = -3.4028235E38f;
    private int mFlingDistance;
    private boolean mForceReplayout;
    private int mGutterSize;
    private boolean mIgnoreGutter;
    private boolean mInLayout;
    private float mInitialMotionX;
    private OnPageChangeListener mInternalPageChangeListener;
    private boolean mIsBeingDragged;
    private boolean mIsUnableToDrag;
    private final ArrayList mItems = new ArrayList();
    private float mLastMotionX;
    private float mLastMotionY;
    private float mLastOffset = Float.MAX_VALUE;
    float mLastPageOffset = 0.0f;
    private EdgeEffect mLeftEdge;
    private Drawable mMarginDrawable;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private boolean mNeedCalculatePageOffsets = false;
    private PagerObserver mObserver;
    private int mOffscreenPageLimit = 3;
    private OnPageChangeListener mOnPageChangeListener;
    private int mPageMargin;
    private boolean mPopulatePending;
    private Parcelable mRestoredAdapterState = null;
    private int mRestoredCurItem = -1;
    private EdgeEffect mRightEdge;
    private int mScrollState = 0;
    private SpringScroller mScroller;
    private boolean mScrollingCacheEnabled;
    private final ItemInfo mTempItem = new ItemInfo();
    private final Rect mTempRect = new Rect();
    private int mTopPageBounds;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;

    interface Decor {
    }

    class ItemInfo {
        boolean hasActionMenu;
        Object object;
        float offset;
        int position;
        boolean scrolling;
        float widthFactor;

        ItemInfo() {
        }
    }

    public class LayoutParams extends android.view.ViewGroup.LayoutParams {
        public int gravity;
        public boolean isDecor;
        public boolean needsMeasure;
        public float widthFactor = 0.0f;

        public LayoutParams() {
            super(-1, -1);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, ViewPager.LAYOUT_ATTRS);
            this.gravity = obtainStyledAttributes.getInteger(0, 48);
            obtainStyledAttributes.recycle();
        }
    }

    interface OnAdapterChangeListener {
        void onAdapterChanged(PagerAdapter pagerAdapter, PagerAdapter pagerAdapter2);
    }

    public interface OnPageChangeListener {
        void onPageScrollStateChanged(int i);

        void onPageScrolled(int i, float f, int i2);

        void onPageSelected(int i);
    }

    class PagerObserver extends DataSetObserver {
        private PagerObserver() {
        }

        public void onChanged() {
            ViewPager.this.dataSetChanged();
        }

        public void onInvalidated() {
            ViewPager.this.dataSetChanged();
        }
    }

    public class SavedState extends BaseSavedState {
        public static final Creator CREATOR = new Creator() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        Parcelable adapterState;
        int position;

        SavedState(Parcel parcel) {
            super(parcel);
            this.position = parcel.readInt();
            this.adapterState = parcel.readParcelable(null);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("FragmentPager.SavedState{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(" position=");
            sb.append(this.position);
            sb.append("}");
            return sb.toString();
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.position);
            parcel.writeParcelable(this.adapterState, i);
        }
    }

    public class SimpleOnPageChangeListener implements OnPageChangeListener {
        public void onPageScrollStateChanged(int i) {
        }

        public void onPageScrolled(int i, float f, int i2) {
        }

        public void onPageSelected(int i) {
        }
    }

    public ViewPager(Context context) {
        super(context);
        initViewPager();
    }

    public ViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initViewPager();
    }

    private void calculatePageOffsets(ItemInfo itemInfo, int i, ItemInfo itemInfo2) {
        int i2;
        int i3;
        ItemInfo itemInfo3;
        ItemInfo itemInfo4;
        int count = this.mAdapter.getCount();
        int width = getWidth();
        float f = width > 0 ? ((float) this.mPageMargin) / ((float) width) : 0.0f;
        if (itemInfo2 != null) {
            int i4 = itemInfo2.position;
            int i5 = itemInfo.position;
            if (i4 < i5) {
                float f2 = itemInfo2.offset + itemInfo2.widthFactor + f;
                int i6 = i4 + 1;
                int i7 = 0;
                while (i6 <= itemInfo.position && i7 < this.mItems.size()) {
                    while (true) {
                        itemInfo4 = (ItemInfo) this.mItems.get(i7);
                        if (i6 > itemInfo4.position && i7 < this.mItems.size() - 1) {
                            i7++;
                        }
                    }
                    while (i6 < itemInfo4.position) {
                        f2 += this.mAdapter.getPageWidth(i6) + f;
                        i6++;
                    }
                    itemInfo4.offset = f2;
                    f2 += itemInfo4.widthFactor + f;
                    i6++;
                }
            } else if (i4 > i5) {
                int size = this.mItems.size() - 1;
                float f3 = itemInfo2.offset;
                while (true) {
                    i4--;
                    if (i4 < itemInfo.position || size < 0) {
                        break;
                    }
                    while (true) {
                        itemInfo3 = (ItemInfo) this.mItems.get(size);
                        if (i4 < itemInfo3.position && size > 0) {
                            size--;
                        }
                    }
                    while (i4 > itemInfo3.position) {
                        f3 -= this.mAdapter.getPageWidth(i4) + f;
                        i4--;
                    }
                    f3 -= itemInfo3.widthFactor + f;
                    itemInfo3.offset = f3;
                }
            }
        }
        int size2 = this.mItems.size();
        float f4 = itemInfo.offset;
        int i8 = itemInfo.position;
        int i9 = i8 - 1;
        this.mFirstOffset = i8 == 0 ? f4 : -3.4028235E38f;
        int i10 = count - 1;
        this.mLastOffset = itemInfo.position == i10 ? (itemInfo.offset + itemInfo.widthFactor) - 1.0f : Float.MAX_VALUE;
        int i11 = i - 1;
        while (i11 >= 0) {
            ItemInfo itemInfo5 = (ItemInfo) this.mItems.get(i11);
            while (true) {
                i3 = itemInfo5.position;
                if (i9 <= i3) {
                    break;
                }
                f4 -= this.mAdapter.getPageWidth(i9) + f;
                i9--;
            }
            f4 -= itemInfo5.widthFactor + f;
            itemInfo5.offset = f4;
            if (i3 == 0) {
                this.mFirstOffset = f4;
            }
            i11--;
            i9--;
        }
        float f5 = itemInfo.offset + itemInfo.widthFactor + f;
        int i12 = itemInfo.position + 1;
        int i13 = i + 1;
        while (i13 < size2) {
            ItemInfo itemInfo6 = (ItemInfo) this.mItems.get(i13);
            while (true) {
                i2 = itemInfo6.position;
                if (i12 >= i2) {
                    break;
                }
                f5 += this.mAdapter.getPageWidth(i12) + f;
                i12++;
            }
            if (i2 == i10) {
                this.mLastOffset = (itemInfo6.widthFactor + f5) - 1.0f;
            }
            itemInfo6.offset = f5;
            f5 += itemInfo6.widthFactor + f;
            i13++;
            i12++;
        }
        this.mNeedCalculatePageOffsets = false;
    }

    private void completeScroll() {
        boolean z = this.mScrollState == 2;
        if (z) {
            setScrollingCacheEnabled(false);
            this.mScroller.abortAnimation();
            int scrollX = getScrollX();
            int scrollY = getScrollY();
            int currX = this.mScroller.getCurrX();
            int currY = this.mScroller.getCurrY();
            if (!(scrollX == currX && scrollY == currY)) {
                scrollTo(currX, currY);
            }
            setScrollState(0);
        }
        this.mPopulatePending = false;
        Iterator it = this.mItems.iterator();
        while (it.hasNext()) {
            ItemInfo itemInfo = (ItemInfo) it.next();
            if (itemInfo.scrolling) {
                itemInfo.scrolling = false;
                z = true;
            }
        }
        if (z) {
            populate();
        }
    }

    private int determineTargetPage(int i, float f, int i2, int i3) {
        if (Math.abs(i3) <= this.mFlingDistance || Math.abs(i2) <= this.mMinimumVelocity) {
            i = (int) (((float) i) + f + 0.5f);
        } else if (i2 <= 0) {
            i++;
        }
        if (this.mItems.size() <= 0) {
            return i;
        }
        ItemInfo itemInfo = (ItemInfo) this.mItems.get(0);
        ArrayList arrayList = this.mItems;
        return Math.max(itemInfo.position, Math.min(i, ((ItemInfo) arrayList.get(arrayList.size() - 1)).position));
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private Rect getChildRectInPagerCoordinates(Rect rect, View view) {
        if (rect == null) {
            rect = new Rect();
        }
        if (view == null) {
            rect.set(0, 0, 0, 0);
            return rect;
        }
        rect.left = view.getLeft();
        rect.right = view.getRight();
        rect.top = view.getTop();
        rect.bottom = view.getBottom();
        ViewParent parent = view.getParent();
        while ((parent instanceof ViewGroup) && parent != this) {
            ViewGroup viewGroup = (ViewGroup) parent;
            rect.left += viewGroup.getLeft();
            rect.right += viewGroup.getRight();
            rect.top += viewGroup.getTop();
            rect.bottom += viewGroup.getBottom();
            parent = viewGroup.getParent();
        }
        return rect;
    }

    private int getClientWidth() {
        return (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
    }

    private ItemInfo infoForCurrentScrollPosition() {
        int width = getWidth();
        float scrollX = width > 0 ? ((float) getScrollX()) / ((float) width) : 0.0f;
        float f = width > 0 ? ((float) this.mPageMargin) / ((float) width) : 0.0f;
        ItemInfo itemInfo = null;
        float f2 = 0.0f;
        float f3 = 0.0f;
        int i = 0;
        int i2 = -1;
        boolean z = true;
        while (i < this.mItems.size()) {
            ItemInfo itemInfo2 = (ItemInfo) this.mItems.get(i);
            if (!z) {
                int i3 = i2 + 1;
                if (itemInfo2.position != i3) {
                    itemInfo2 = this.mTempItem;
                    itemInfo2.offset = f2 + f3 + f;
                    itemInfo2.position = i3;
                    itemInfo2.widthFactor = this.mAdapter.getPageWidth(itemInfo2.position);
                    i--;
                }
            }
            f2 = itemInfo2.offset;
            float f4 = itemInfo2.widthFactor + f2 + f;
            if (!z && scrollX < f2) {
                return itemInfo;
            }
            if (scrollX < f4 || i == this.mItems.size() - 1) {
                return itemInfo2;
            }
            i2 = itemInfo2.position;
            f3 = itemInfo2.widthFactor;
            i++;
            z = false;
            itemInfo = itemInfo2;
        }
        return itemInfo;
    }

    private boolean isGutterDrag(float f, float f2) {
        return (f < ((float) this.mGutterSize) && f2 > 0.0f) || (f > ((float) (getWidth() - this.mGutterSize)) && f2 < 0.0f);
    }

    private void onSecondaryPointerUp(MotionEvent motionEvent) {
        int actionIndex = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(actionIndex) == this.mActivePointerId) {
            int i = actionIndex == 0 ? 1 : 0;
            this.mLastMotionX = motionEvent.getX(i);
            this.mActivePointerId = motionEvent.getPointerId(i);
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.clear();
            }
        }
    }

    private boolean pageScrolled(int i, boolean z) {
        boolean z2;
        int i2;
        String str = "onPageScrolled did not call superclass implementation";
        if (this.mItems.size() == 0) {
            this.mCalledSuper = false;
            onPageScrolled(0, 0.0f, 0);
            if (this.mCalledSuper) {
                return false;
            }
            throw new IllegalStateException(str);
        }
        ItemInfo infoForCurrentScrollPosition = infoForCurrentScrollPosition();
        int width = getWidth();
        int i3 = this.mPageMargin;
        int i4 = width + i3;
        float f = (float) width;
        float f2 = ((float) i3) / f;
        int i5 = infoForCurrentScrollPosition.position;
        float f3 = ((((float) i) / f) - infoForCurrentScrollPosition.offset) / (infoForCurrentScrollPosition.widthFactor + f2);
        int i6 = (int) (((float) i4) * f3);
        if (z) {
            if (this.mLastPageOffset >= f3 || f3 <= 0.55f) {
                z2 = this.mLastPageOffset > f3 && f3 < 0.45f;
                i2 = i5;
            } else {
                i2 = i5 + 1;
                z2 = true;
            }
            this.mLastPageOffset = f3;
            if (z2) {
                int i7 = i2 < 0 ? 0 : i2 >= this.mAdapter.getCount() ? this.mAdapter.getCount() - 1 : i2;
                if (i7 != this.mCurItem) {
                    populate(i7);
                    OnPageChangeListener onPageChangeListener = this.mOnPageChangeListener;
                    if (onPageChangeListener != null) {
                        onPageChangeListener.onPageSelected(i7);
                    }
                    OnPageChangeListener onPageChangeListener2 = this.mInternalPageChangeListener;
                    if (onPageChangeListener2 != null) {
                        onPageChangeListener2.onPageSelected(i7);
                    }
                }
            }
        }
        this.mCalledSuper = false;
        onPageScrolled(i5, f3, i6);
        if (this.mCalledSuper) {
            return true;
        }
        throw new IllegalStateException(str);
    }

    private boolean performDrag(float f) {
        boolean z;
        boolean z2;
        float f2 = this.mLastMotionX - f;
        this.mLastMotionX = f;
        float scrollX = ((float) getScrollX()) + f2;
        float width = (float) getWidth();
        float f3 = this.mFirstOffset * width;
        float f4 = this.mLastOffset * width;
        boolean z3 = false;
        ItemInfo itemInfo = (ItemInfo) this.mItems.get(0);
        ArrayList arrayList = this.mItems;
        ItemInfo itemInfo2 = (ItemInfo) arrayList.get(arrayList.size() - 1);
        if (itemInfo.position != 0) {
            f3 = itemInfo.offset * width;
            z = false;
        } else {
            z = true;
        }
        if (itemInfo2.position != this.mAdapter.getCount() - 1) {
            f4 = itemInfo2.offset * width;
            z2 = false;
        } else {
            z2 = true;
        }
        if (scrollX < f3) {
            if (z) {
                this.mLeftEdge.onPull(Math.abs(f3 - scrollX) / width);
                z3 = true;
            }
            scrollX = f3;
        } else if (scrollX > f4) {
            if (z2) {
                this.mRightEdge.onPull(Math.abs(scrollX - f4) / width);
                z3 = true;
            }
            scrollX = f4;
        }
        int i = (int) scrollX;
        this.mLastMotionX += scrollX - ((float) i);
        scrollTo(i, getScrollY());
        pageScrolled(i, true);
        return z3;
    }

    private void recomputeScrollPosition(int i, int i2, int i3, int i4) {
        int min;
        if (i2 <= 0 || this.mItems.isEmpty()) {
            ItemInfo infoForPosition = infoForPosition(this.mCurItem);
            min = (int) ((infoForPosition != null ? Math.min(infoForPosition.offset, this.mLastOffset) : 0.0f) * ((float) i));
            if (min != getScrollX()) {
                completeScroll();
            } else {
                return;
            }
        } else if (!this.mScroller.isFinished()) {
            this.mScroller.setFinalX(getCurrentItem() * getClientWidth());
            return;
        } else {
            min = (int) ((((float) getScrollX()) / ((float) (i2 + i4))) * ((float) (i + i3)));
        }
        scrollTo(min, getScrollY());
    }

    private void removeNonDecorViews() {
        int i = 0;
        while (i < getChildCount()) {
            if (!((LayoutParams) getChildAt(i).getLayoutParams()).isDecor) {
                removeViewAt(i);
                i--;
            }
            i++;
        }
    }

    private boolean resetTouch() {
        this.mActivePointerId = -1;
        endDrag();
        this.mLeftEdge.onRelease();
        this.mRightEdge.onRelease();
        return this.mRightEdge.isFinished() | this.mLeftEdge.isFinished();
    }

    private void setScrollState(int i) {
        if (this.mScrollState != i) {
            this.mScrollState = i;
            OnPageChangeListener onPageChangeListener = this.mOnPageChangeListener;
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageScrollStateChanged(i);
            }
            OnPageChangeListener onPageChangeListener2 = this.mInternalPageChangeListener;
            if (onPageChangeListener2 != null) {
                onPageChangeListener2.onPageScrollStateChanged(i);
            }
        }
    }

    private void setScrollingCacheEnabled(boolean z) {
        if (this.mScrollingCacheEnabled != z) {
            this.mScrollingCacheEnabled = z;
        }
    }

    public void addFocusables(ArrayList arrayList, int i, int i2) {
        int size = arrayList.size();
        int descendantFocusability = getDescendantFocusability();
        if (descendantFocusability != 393216) {
            for (int i3 = 0; i3 < getChildCount(); i3++) {
                View childAt = getChildAt(i3);
                if (childAt.getVisibility() == 0) {
                    ItemInfo infoForChild = infoForChild(childAt);
                    if (infoForChild != null && infoForChild.position == this.mCurItem) {
                        childAt.addFocusables(arrayList, i, i2);
                    }
                }
            }
        }
        if ((descendantFocusability == 262144 && (arrayList == null || size != arrayList.size())) || !isFocusable()) {
            return;
        }
        if (((i2 & 1) != 1 || !isInTouchMode() || isFocusableInTouchMode()) && arrayList != null) {
            arrayList.add(this);
        }
    }

    /* access modifiers changed from: 0000 */
    public ItemInfo addNewItem(int i, int i2) {
        ItemInfo itemInfo = new ItemInfo();
        itemInfo.position = i;
        itemInfo.object = this.mAdapter.instantiateItem((ViewGroup) this, i);
        itemInfo.widthFactor = this.mAdapter.getPageWidth(i);
        itemInfo.hasActionMenu = this.mAdapter.hasActionMenu(i);
        if (i2 < 0 || i2 >= this.mItems.size()) {
            this.mItems.add(itemInfo);
        } else {
            this.mItems.add(i2, itemInfo);
        }
        return itemInfo;
    }

    public void addTouchables(ArrayList arrayList) {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() == 0) {
                ItemInfo infoForChild = infoForChild(childAt);
                if (infoForChild != null && infoForChild.position == this.mCurItem) {
                    childAt.addTouchables(arrayList);
                }
            }
        }
    }

    public void addView(View view, int i, android.view.ViewGroup.LayoutParams layoutParams) {
        if (!checkLayoutParams(layoutParams)) {
            layoutParams = generateLayoutParams(layoutParams);
        }
        LayoutParams layoutParams2 = (LayoutParams) layoutParams;
        layoutParams2.isDecor |= view instanceof Decor;
        if (!this.mInLayout) {
            super.addView(view, i, layoutParams);
        } else if (!layoutParams2.isDecor) {
            layoutParams2.needsMeasure = true;
            addViewInLayout(view, i, layoutParams);
        } else {
            throw new IllegalStateException("Cannot add pager decor view during layout");
        }
    }

    public boolean arrowScroll(int i) {
        boolean pageRight;
        View findFocus = findFocus();
        if (findFocus == this) {
            findFocus = null;
        }
        boolean z = false;
        View findNextFocus = FocusFinder.getInstance().findNextFocus(this, findFocus, i);
        if (findNextFocus != null && findNextFocus != findFocus) {
            if (i == 17) {
                int i2 = getChildRectInPagerCoordinates(this.mTempRect, findNextFocus).left;
                int i3 = getChildRectInPagerCoordinates(this.mTempRect, findFocus).left;
                if (findFocus != null && i2 >= i3) {
                    pageRight = pageLeft();
                    z = pageRight;
                }
            } else if (i == 66) {
                int i4 = getChildRectInPagerCoordinates(this.mTempRect, findNextFocus).left;
                int i5 = getChildRectInPagerCoordinates(this.mTempRect, findFocus).left;
                if (findFocus != null && i4 <= i5) {
                    pageRight = pageRight();
                    z = pageRight;
                }
            }
            pageRight = findNextFocus.requestFocus();
            z = pageRight;
        } else if (i == 17 || i == 1) {
            z = pageLeft();
        } else if (i == 66 || i == 2) {
            z = pageRight();
        }
        if (z) {
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(i));
        }
        return z;
    }

    public boolean beginFakeDrag() {
        if (this.mIsBeingDragged) {
            return false;
        }
        this.mFakeDragging = true;
        setScrollState(1);
        this.mLastMotionX = 0.0f;
        this.mInitialMotionX = 0.0f;
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 0, 0.0f, 0.0f, 0);
        this.mVelocityTracker.addMovement(obtain);
        obtain.recycle();
        this.mFakeDragBeginTime = uptimeMillis;
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean canScroll(View view, boolean z, int i, int i2, int i3) {
        View view2 = view;
        boolean z2 = true;
        if (view2 instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view2;
            int scrollX = view.getScrollX();
            int scrollY = view.getScrollY();
            for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
                View childAt = viewGroup.getChildAt(childCount);
                int i4 = i2 + scrollX;
                if (i4 >= childAt.getLeft() && i4 < childAt.getRight()) {
                    int i5 = i3 + scrollY;
                    if (i5 >= childAt.getTop() && i5 < childAt.getBottom()) {
                        if (canScroll(childAt, true, i, i4 - childAt.getLeft(), i5 - childAt.getTop())) {
                            return true;
                        }
                    }
                }
            }
        }
        if (!z || !view.canScrollHorizontally(-i)) {
            z2 = false;
        }
        return z2;
    }

    public boolean canScrollHorizontally(int i) {
        boolean z = false;
        if (this.mAdapter == null) {
            return false;
        }
        int clientWidth = getClientWidth();
        int scrollX = getScrollX();
        if (i < 0) {
            if (scrollX > ((int) (((float) clientWidth) * this.mFirstOffset))) {
                z = true;
            }
            return z;
        }
        if (i > 0 && scrollX < ((int) (((float) clientWidth) * this.mLastOffset))) {
            z = true;
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return (layoutParams instanceof LayoutParams) && super.checkLayoutParams(layoutParams);
    }

    public void computeScroll() {
        if (this.mScroller.isFinished() || !this.mScroller.computeScrollOffset()) {
            completeScroll();
            return;
        }
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        int currX = this.mScroller.getCurrX();
        int currY = this.mScroller.getCurrY();
        if (!(scrollX == currX && scrollY == currY)) {
            scrollTo(currX, currY);
            if (!pageScrolled(currX, false)) {
                this.mScroller.abortAnimation();
                scrollTo(0, currY);
            }
        }
        postInvalidateOnAnimation();
    }

    /* access modifiers changed from: 0000 */
    public void dataSetChanged() {
        boolean z = false;
        boolean z2 = this.mItems.size() < (this.mOffscreenPageLimit * 2) + 1 && this.mItems.size() < this.mAdapter.getCount();
        boolean z3 = z2;
        int i = this.mCurItem;
        int i2 = 0;
        boolean z4 = false;
        boolean z5 = false;
        while (i2 < this.mItems.size()) {
            ItemInfo itemInfo = (ItemInfo) this.mItems.get(i2);
            int itemPosition = this.mAdapter.getItemPosition(itemInfo.object);
            if (itemPosition != -1) {
                if (itemPosition == -2) {
                    this.mItems.remove(i2);
                    i2--;
                    if (!z4) {
                        this.mAdapter.startUpdate((ViewGroup) this);
                        z4 = true;
                    }
                    this.mAdapter.destroyItem((ViewGroup) this, itemInfo.position, itemInfo.object);
                    int i3 = this.mCurItem;
                    if (i3 == itemInfo.position) {
                        i = Math.max(0, Math.min(i3, this.mAdapter.getCount() - 1));
                    }
                    z3 = true;
                } else {
                    int i4 = itemInfo.position;
                    if (i4 != itemPosition) {
                        if (i4 == this.mCurItem) {
                            int i5 = itemPosition;
                        }
                        itemInfo.position = itemPosition;
                        z3 = true;
                    }
                    if (itemInfo.hasActionMenu == this.mAdapter.hasActionMenu(itemInfo.position)) {
                    }
                }
                i2++;
            } else if (itemInfo.hasActionMenu == this.mAdapter.hasActionMenu(itemInfo.position)) {
                i2++;
            }
            itemInfo.hasActionMenu = !itemInfo.hasActionMenu;
            z5 = true;
            i2++;
        }
        if (z4) {
            this.mAdapter.finishUpdate((ViewGroup) this);
        }
        Collections.sort(this.mItems, COMPARATOR);
        if (z3) {
            int childCount = getChildCount();
            for (int i6 = 0; i6 < childCount; i6++) {
                LayoutParams layoutParams = (LayoutParams) getChildAt(i6).getLayoutParams();
                if (!layoutParams.isDecor) {
                    layoutParams.widthFactor = 0.0f;
                }
            }
            setCurrentItemInternal(i, false, true);
            requestLayout();
        } else {
            z = z5;
        }
        if (z) {
            requestLayout();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent) || executeKeyEvent(keyEvent);
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() == 0) {
                ItemInfo infoForChild = infoForChild(childAt);
                if (infoForChild != null && infoForChild.position == this.mCurItem && childAt.dispatchPopulateAccessibilityEvent(accessibilityEvent)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public float distanceInfluenceForSnapDuration(float f) {
        return (float) Math.sin((double) ((float) (((double) (f - 0.5f)) * 0.4712389167638204d)));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0015, code lost:
        if (r0.getCount() > 1) goto L_0x0024;
     */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x00a8  */
    /* JADX WARNING: Removed duplicated region for block: B:18:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void draw(Canvas canvas) {
        super.draw(canvas);
        int overScrollMode = getOverScrollMode();
        boolean z = false;
        if (overScrollMode != 0) {
            if (overScrollMode == 1) {
                PagerAdapter pagerAdapter = this.mAdapter;
                if (pagerAdapter != null) {
                }
            }
            this.mLeftEdge.finish();
            this.mRightEdge.finish();
            if (!z) {
                postInvalidateOnAnimation();
                return;
            }
            return;
        }
        if (!this.mLeftEdge.isFinished()) {
            int save = canvas.save();
            int height = (getHeight() - getPaddingTop()) - getPaddingBottom();
            int width = getWidth();
            canvas.rotate(270.0f);
            canvas.translate((float) ((-height) + getPaddingTop()), this.mFirstOffset * ((float) width));
            this.mLeftEdge.setSize(height, width);
            z = this.mLeftEdge.draw(canvas);
            canvas.restoreToCount(save);
        }
        if (!this.mRightEdge.isFinished()) {
            int save2 = canvas.save();
            int width2 = getWidth();
            int height2 = (getHeight() - getPaddingTop()) - getPaddingBottom();
            canvas.rotate(90.0f);
            canvas.translate((float) (-getPaddingTop()), (-(this.mLastOffset + 1.0f)) * ((float) width2));
            this.mRightEdge.setSize(height2, width2);
            z |= this.mRightEdge.draw(canvas);
            canvas.restoreToCount(save2);
        }
        if (!z) {
        }
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = this.mMarginDrawable;
        if (drawable != null && drawable.isStateful()) {
            drawable.setState(getDrawableState());
        }
    }

    public void endFakeDrag() {
        if (this.mFakeDragging) {
            VelocityTracker velocityTracker = this.mVelocityTracker;
            velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
            int xVelocity = (int) velocityTracker.getXVelocity(this.mActivePointerId);
            this.mPopulatePending = true;
            int width = getWidth();
            int scrollX = getScrollX();
            ItemInfo infoForCurrentScrollPosition = infoForCurrentScrollPosition();
            setCurrentItemInternal(determineTargetPage(infoForCurrentScrollPosition.position, ((((float) scrollX) / ((float) width)) - infoForCurrentScrollPosition.offset) / infoForCurrentScrollPosition.widthFactor, xVelocity, (int) (this.mLastMotionX - this.mInitialMotionX)), true, true, xVelocity);
            endDrag();
            this.mFakeDragging = false;
            return;
        }
        throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
    }

    public boolean executeKeyEvent(KeyEvent keyEvent) {
        int i;
        if (keyEvent.getAction() == 0) {
            int keyCode = keyEvent.getKeyCode();
            if (keyCode == 21) {
                i = 17;
            } else if (keyCode == 22) {
                i = 66;
            } else if (keyCode == 61 && VERSION.SDK_INT >= 11) {
                if (keyEvent.hasNoModifiers()) {
                    i = 2;
                } else if (keyEvent.hasModifiers(1)) {
                    return arrowScroll(1);
                }
            }
            return arrowScroll(i);
        }
        return false;
    }

    public void fakeDragBy(float f) {
        if (this.mFakeDragging) {
            this.mLastMotionX += f;
            float scrollX = ((float) getScrollX()) - f;
            float width = (float) getWidth();
            float f2 = this.mFirstOffset * width;
            float f3 = this.mLastOffset * width;
            ItemInfo itemInfo = (ItemInfo) this.mItems.get(0);
            ArrayList arrayList = this.mItems;
            ItemInfo itemInfo2 = (ItemInfo) arrayList.get(arrayList.size() - 1);
            if (itemInfo.position != 0) {
                f2 = itemInfo.offset * width;
            }
            if (itemInfo2.position != this.mAdapter.getCount() - 1) {
                f3 = itemInfo2.offset * width;
            }
            if (scrollX < f2) {
                scrollX = f2;
            } else if (scrollX > f3) {
                scrollX = f3;
            }
            int i = (int) scrollX;
            this.mLastMotionX += scrollX - ((float) i);
            scrollTo(i, getScrollY());
            pageScrolled(i, true);
            MotionEvent obtain = MotionEvent.obtain(this.mFakeDragBeginTime, SystemClock.uptimeMillis(), 2, this.mLastMotionX, 0.0f, 0);
            this.mVelocityTracker.addMovement(obtain);
            obtain.recycle();
            return;
        }
        throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
    }

    /* access modifiers changed from: protected */
    public android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* access modifiers changed from: protected */
    public android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return generateDefaultLayoutParams();
    }

    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public int getPageMargin() {
        return this.mPageMargin;
    }

    /* access modifiers changed from: 0000 */
    public int getSplitActionBarHeight() {
        ActionBarOverlayLayout actionBarOverlayLayout = (ActionBarOverlayLayout) ActionBarUtils.getActionBarOverlayLayout(this);
        if (actionBarOverlayLayout == null || actionBarOverlayLayout.getActionBarView() == null) {
            return 0;
        }
        return actionBarOverlayLayout.getActionBarView().getSplitActionBarHeight(true);
    }

    /* access modifiers changed from: 0000 */
    public ItemInfo infoForAnyChild(View view) {
        while (true) {
            ViewParent parent = view.getParent();
            if (parent == this) {
                return infoForChild(view);
            }
            if (!(parent instanceof View)) {
                return null;
            }
            view = (View) parent;
        }
    }

    /* access modifiers changed from: 0000 */
    public ItemInfo infoForChild(View view) {
        Iterator it = this.mItems.iterator();
        while (it.hasNext()) {
            ItemInfo itemInfo = (ItemInfo) it.next();
            if (this.mAdapter.isViewFromObject(view, itemInfo.object)) {
                return itemInfo;
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public ItemInfo infoForPosition(int i) {
        Iterator it = this.mItems.iterator();
        while (it.hasNext()) {
            ItemInfo itemInfo = (ItemInfo) it.next();
            if (itemInfo.position == i) {
                return itemInfo;
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public void initViewPager() {
        setWillNotDraw(false);
        setDescendantFocusability(262144);
        setFocusable(true);
        Context context = getContext();
        this.mScroller = new SpringScroller();
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
        this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mLeftEdge = new EdgeEffect(context);
        this.mRightEdge = new EdgeEffect(context);
        float f = context.getResources().getDisplayMetrics().density;
        this.mFlingDistance = (int) (25.0f * f);
        this.mCloseEnough = (int) (2.0f * f);
        this.mDefaultGutterSize = (int) (f * 16.0f);
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
    }

    public boolean isFakeDragging() {
        return this.mFakeDragging;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        float f;
        float f2;
        super.onDraw(canvas);
        if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
            int scrollX = getScrollX();
            int width = getWidth();
            float f3 = (float) width;
            float f4 = ((float) this.mPageMargin) / f3;
            int i = 0;
            ItemInfo itemInfo = (ItemInfo) this.mItems.get(0);
            float f5 = itemInfo.offset;
            int size = this.mItems.size();
            int i2 = itemInfo.position;
            int i3 = ((ItemInfo) this.mItems.get(size - 1)).position;
            while (i2 < i3) {
                while (i2 > itemInfo.position && i < size) {
                    i++;
                    itemInfo = (ItemInfo) this.mItems.get(i);
                }
                if (i2 == itemInfo.position) {
                    float f6 = itemInfo.offset;
                    float f7 = itemInfo.widthFactor;
                    f = (f6 + f7) * f3;
                    f5 = f6 + f7 + f4;
                } else {
                    float pageWidth = this.mAdapter.getPageWidth(i2);
                    f = (f5 + pageWidth) * f3;
                    f5 += pageWidth + f4;
                }
                int i4 = this.mPageMargin;
                if (((float) i4) + f > ((float) scrollX)) {
                    f2 = f4;
                    this.mMarginDrawable.setBounds((int) f, this.mTopPageBounds, (int) (((float) i4) + f + 0.5f), this.mBottomPageBounds);
                    this.mMarginDrawable.draw(canvas);
                } else {
                    Canvas canvas2 = canvas;
                    f2 = f4;
                }
                if (f <= ((float) (scrollX + width))) {
                    i2++;
                    f4 = f2;
                } else {
                    return;
                }
            }
        }
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(ViewPager.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(ViewPager.class.getName());
        PagerAdapter pagerAdapter = this.mAdapter;
        boolean z = pagerAdapter != null && pagerAdapter.getCount() > 1;
        accessibilityNodeInfo.setScrollable(z);
        PagerAdapter pagerAdapter2 = this.mAdapter;
        if (pagerAdapter2 != null) {
            int i = this.mCurItem;
            if (i >= 0 && i < pagerAdapter2.getCount() - 1) {
                accessibilityNodeInfo.addAction(4096);
            }
        }
        PagerAdapter pagerAdapter3 = this.mAdapter;
        if (pagerAdapter3 != null) {
            int i2 = this.mCurItem;
            if (i2 > 0 && i2 < pagerAdapter3.getCount()) {
                accessibilityNodeInfo.addAction(8192);
            }
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        MotionEvent motionEvent2 = motionEvent;
        if (!this.mDragEnabled) {
            return false;
        }
        int action = motionEvent.getAction() & 255;
        if (action == 3 || action == 1) {
            resetTouch();
            return false;
        }
        if (action != 0) {
            if (this.mIsBeingDragged) {
                return true;
            }
            if (this.mIsUnableToDrag) {
                return false;
            }
        }
        if (action == 0) {
            float x = motionEvent.getX();
            this.mInitialMotionX = x;
            this.mLastMotionX = x;
            this.mLastMotionY = motionEvent.getY();
            this.mActivePointerId = motionEvent2.getPointerId(0);
            this.mIsUnableToDrag = false;
            this.mScroller.computeScrollOffset();
            if (this.mScrollState != 2 || Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) <= this.mCloseEnough) {
                completeScroll();
                this.mIsBeingDragged = false;
            } else {
                this.mScroller.abortAnimation();
                this.mPopulatePending = false;
                populate();
                this.mIsBeingDragged = true;
                setScrollState(1);
            }
        } else if (action == 2) {
            int i = this.mActivePointerId;
            if (i != -1) {
                int findPointerIndex = motionEvent2.findPointerIndex(i);
                if (findPointerIndex >= 0 && findPointerIndex < motionEvent.getPointerCount()) {
                    float x2 = motionEvent2.getX(findPointerIndex);
                    float f = x2 - this.mLastMotionX;
                    float abs = Math.abs(f);
                    float y = motionEvent2.getY(findPointerIndex);
                    float abs2 = Math.abs(y - this.mLastMotionY);
                    int i2 = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
                    if (i2 != 0 && !isGutterDrag(this.mLastMotionX, f)) {
                        if (canScroll(this, false, (int) f, (int) x2, (int) y)) {
                            this.mLastMotionX = x2;
                            this.mInitialMotionX = x2;
                            this.mLastMotionY = y;
                            this.mIsUnableToDrag = true;
                            return false;
                        }
                    }
                    if (abs > ((float) this.mTouchSlop) && abs > abs2) {
                        this.mIsBeingDragged = true;
                        setScrollState(1);
                        float f2 = this.mInitialMotionX;
                        float f3 = (float) this.mTouchSlop;
                        this.mLastMotionX = i2 > 0 ? f2 + f3 : f2 - f3;
                        setScrollingCacheEnabled(true);
                    } else if (abs2 > ((float) this.mTouchSlop)) {
                        this.mIsUnableToDrag = true;
                    }
                    if (this.mIsBeingDragged && performDrag(x2)) {
                        postInvalidateOnAnimation();
                    }
                }
            }
        } else if (action == 6) {
            onSecondaryPointerUp(motionEvent);
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent2);
        return this.mIsBeingDragged;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0085  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00a7  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int i8;
        int i9 = 1;
        this.mInLayout = true;
        populate();
        this.mInLayout = false;
        int childCount = getChildCount();
        int i10 = i3 - i;
        int i11 = i4 - i2;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int scrollX = getScrollX();
        int i12 = paddingLeft;
        int i13 = paddingRight;
        int i14 = 0;
        int i15 = 0;
        while (true) {
            i5 = 8;
            if (i14 >= childCount) {
                break;
            }
            View childAt = getChildAt(i14);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.isDecor) {
                    int absoluteGravity = Gravity.getAbsoluteGravity(layoutParams.gravity, getLayoutDirection()) & 7;
                    int i16 = layoutParams.gravity & 112;
                    if (absoluteGravity != i9) {
                        if (absoluteGravity == 3) {
                            i6 = childAt.getMeasuredWidth() + i12;
                        } else if (absoluteGravity != 5) {
                            i6 = i12;
                        } else {
                            i8 = (i10 - i13) - childAt.getMeasuredWidth();
                            i13 += childAt.getMeasuredWidth();
                        }
                        if (i16 != 16) {
                            i7 = Math.max((i11 - childAt.getMeasuredHeight()) / 2, paddingTop);
                        } else if (i16 == 48) {
                            i7 = paddingTop;
                            paddingTop = childAt.getMeasuredHeight() + paddingTop;
                        } else if (i16 != 80) {
                            i7 = paddingTop;
                        } else {
                            i7 = (i11 - paddingBottom) - childAt.getMeasuredHeight();
                            paddingBottom += childAt.getMeasuredHeight();
                        }
                        int i17 = i12 + scrollX;
                        childAt.layout(i17, i7, childAt.getMeasuredWidth() + i17, i7 + childAt.getMeasuredHeight());
                        i15++;
                        i12 = i6;
                    } else {
                        i8 = Math.max((i10 - childAt.getMeasuredWidth()) / 2, i12);
                    }
                    int i18 = i12;
                    i12 = i8;
                    i6 = i18;
                    if (i16 != 16) {
                    }
                    int i172 = i12 + scrollX;
                    childAt.layout(i172, i7, childAt.getMeasuredWidth() + i172, i7 + childAt.getMeasuredHeight());
                    i15++;
                    i12 = i6;
                }
            }
            i14++;
            i9 = 1;
        }
        int i19 = 0;
        while (i19 < childCount) {
            View childAt2 = getChildAt(i19);
            if (childAt2.getVisibility() != i5) {
                LayoutParams layoutParams2 = (LayoutParams) childAt2.getLayoutParams();
                if (!layoutParams2.isDecor) {
                    ItemInfo infoForChild = infoForChild(childAt2);
                    if (infoForChild != null) {
                        int i20 = ((int) (((float) i10) * infoForChild.offset)) + i12;
                        if (this.mForceReplayout || layoutParams2.needsMeasure) {
                            layoutParams2.needsMeasure = false;
                            int makeMeasureSpec = MeasureSpec.makeMeasureSpec((int) (((float) ((i10 - i12) - i13)) * layoutParams2.widthFactor), 1073741824);
                            this.mForceReplayout = false;
                            childAt2.measure(makeMeasureSpec, MeasureSpec.makeMeasureSpec((int) (((float) ((i11 - paddingTop) - paddingBottom)) - (((float) (infoForChild.hasActionMenu ? getSplitActionBarHeight() : 0)) * (1.0f - this.mBottomMarginProgress))), 1073741824));
                        }
                        childAt2.layout(i20, paddingTop, childAt2.getMeasuredWidth() + i20, childAt2.getMeasuredHeight() + paddingTop);
                    }
                }
            }
            i19++;
            i5 = 8;
        }
        this.mTopPageBounds = paddingTop;
        this.mBottomPageBounds = i11 - paddingBottom;
        this.mDecorChildCount = i15;
        this.mFirstLayout = false;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        setMeasuredDimension(ViewGroup.getDefaultSize(0, i), ViewGroup.getDefaultSize(0, i2));
        int measuredWidth = getMeasuredWidth();
        this.mGutterSize = Math.min(measuredWidth / 10, this.mDefaultGutterSize);
        int paddingLeft = (measuredWidth - getPaddingLeft()) - getPaddingRight();
        int measuredHeight = (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom();
        int childCount = getChildCount();
        int i4 = measuredHeight;
        int i5 = paddingLeft;
        int i6 = 0;
        while (true) {
            boolean z = true;
            int i7 = 1073741824;
            if (i6 >= childCount) {
                break;
            }
            View childAt = getChildAt(i6);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams != null && layoutParams.isDecor) {
                    int absoluteGravity = Gravity.getAbsoluteGravity(layoutParams.gravity, getLayoutDirection()) & 7;
                    int i8 = layoutParams.gravity & 112;
                    boolean z2 = i8 == 48 || i8 == 80;
                    if (!(absoluteGravity == 3 || absoluteGravity == 5)) {
                        z = false;
                    }
                    int i9 = Integer.MIN_VALUE;
                    if (z2) {
                        i3 = Integer.MIN_VALUE;
                        i9 = 1073741824;
                    } else {
                        i3 = z ? 1073741824 : Integer.MIN_VALUE;
                    }
                    int i10 = layoutParams.width;
                    if (i10 != -2) {
                        if (i10 == -1) {
                            i10 = i5;
                        }
                        i9 = 1073741824;
                    } else {
                        i10 = i5;
                    }
                    int i11 = layoutParams.height;
                    if (i11 == -2) {
                        i11 = i4;
                        i7 = i3;
                    } else if (i11 == -1) {
                        i11 = i4;
                    }
                    childAt.measure(MeasureSpec.makeMeasureSpec(i10, i9), MeasureSpec.makeMeasureSpec(i11, i7));
                    if (z2) {
                        i4 -= childAt.getMeasuredHeight();
                    } else if (z) {
                        i5 -= childAt.getMeasuredWidth();
                    }
                }
            }
            i6++;
        }
        this.mChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec(i5, 1073741824);
        this.mChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(i4, 1073741824);
        this.mInLayout = true;
        populate();
        this.mInLayout = false;
        int childCount2 = getChildCount();
        for (int i12 = 0; i12 < childCount2; i12++) {
            View childAt2 = getChildAt(i12);
            if (childAt2.getVisibility() != 8) {
                LayoutParams layoutParams2 = (LayoutParams) childAt2.getLayoutParams();
                if (!layoutParams2.isDecor) {
                    int makeMeasureSpec = MeasureSpec.makeMeasureSpec((int) (((float) i5) * layoutParams2.widthFactor), 1073741824);
                    ItemInfo infoForChild = infoForChild(childAt2);
                    int splitActionBarHeight = (infoForChild == null || !infoForChild.hasActionMenu) ? i4 : i4 - ((int) (((float) getSplitActionBarHeight()) * (1.0f - this.mBottomMarginProgress)));
                    childAt2.measure(makeMeasureSpec, MeasureSpec.makeMeasureSpec(splitActionBarHeight, 1073741824));
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x006b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onPageScrolled(int i, float f, int i2) {
        int i3;
        int left;
        int i4;
        if (this.mDecorChildCount > 0) {
            int scrollX = getScrollX();
            int paddingLeft = getPaddingLeft();
            int paddingRight = getPaddingRight();
            int width = getWidth();
            int childCount = getChildCount();
            for (int i5 = 0; i5 < childCount; i5++) {
                View childAt = getChildAt(i5);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.isDecor) {
                    int absoluteGravity = Gravity.getAbsoluteGravity(layoutParams.gravity, getLayoutDirection()) & 7;
                    if (absoluteGravity != 1) {
                        if (absoluteGravity == 3) {
                            i3 = childAt.getWidth() + paddingLeft;
                        } else if (absoluteGravity != 5) {
                            i3 = paddingLeft;
                        } else {
                            i4 = (width - paddingRight) - childAt.getMeasuredWidth();
                            paddingRight += childAt.getMeasuredWidth();
                        }
                        left = (paddingLeft + scrollX) - childAt.getLeft();
                        if (left != 0) {
                            childAt.offsetLeftAndRight(left);
                        }
                        paddingLeft = i3;
                    } else {
                        i4 = Math.max((width - childAt.getMeasuredWidth()) / 2, paddingLeft);
                    }
                    int i6 = i4;
                    i3 = paddingLeft;
                    paddingLeft = i6;
                    left = (paddingLeft + scrollX) - childAt.getLeft();
                    if (left != 0) {
                    }
                    paddingLeft = i3;
                }
            }
        }
        OnPageChangeListener onPageChangeListener = this.mOnPageChangeListener;
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrolled(i, f, i2);
        }
        OnPageChangeListener onPageChangeListener2 = this.mInternalPageChangeListener;
        if (onPageChangeListener2 != null) {
            onPageChangeListener2.onPageScrolled(i, f, i2);
        }
        this.mCalledSuper = true;
    }

    /* access modifiers changed from: protected */
    public boolean onRequestFocusInDescendants(int i, Rect rect) {
        int i2;
        int i3;
        int childCount = getChildCount();
        int i4 = -1;
        if ((i & 2) != 0) {
            i4 = childCount;
            i3 = 0;
            i2 = 1;
        } else {
            i3 = childCount - 1;
            i2 = -1;
        }
        while (i3 != i4) {
            View childAt = getChildAt(i3);
            if (childAt.getVisibility() == 0) {
                ItemInfo infoForChild = infoForChild(childAt);
                if (infoForChild != null && infoForChild.position == this.mCurItem && childAt.requestFocus(i, rect)) {
                    return true;
                }
            }
            i3 += i2;
        }
        return false;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter != null) {
            pagerAdapter.restoreState(savedState.adapterState, null);
            setCurrentItemInternal(savedState.position, false, true);
        } else {
            this.mRestoredCurItem = savedState.position;
            this.mRestoredAdapterState = savedState.adapterState;
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.position = this.mCurItem;
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter != null) {
            savedState.adapterState = pagerAdapter.saveState();
        }
        return savedState;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (i != i3) {
            int i5 = this.mPageMargin;
            recomputeScrollPosition(i, i3, i5, i5);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:57:0x013a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int i;
        boolean z = false;
        if (!this.mDragEnabled) {
            return false;
        }
        if (this.mFakeDragging) {
            return true;
        }
        if (motionEvent.getAction() == 0 && motionEvent.getEdgeFlags() != 0) {
            return false;
        }
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter == null || pagerAdapter.getCount() == 0) {
            return false;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        int action = motionEvent.getAction() & 255;
        if (action != 0) {
            if (action == 1) {
                if (this.mIsBeingDragged) {
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                    int xVelocity = (int) velocityTracker.getXVelocity(this.mActivePointerId);
                    this.mPopulatePending = true;
                    int width = getWidth();
                    int scrollX = getScrollX();
                    ItemInfo infoForCurrentScrollPosition = infoForCurrentScrollPosition();
                    setCurrentItemInternal(determineTargetPage(infoForCurrentScrollPosition.position, ((((float) scrollX) / ((float) width)) - infoForCurrentScrollPosition.offset) / infoForCurrentScrollPosition.widthFactor, xVelocity, (int) (motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId)) - this.mInitialMotionX)), true, true, xVelocity);
                }
                if (z) {
                }
                return true;
            } else if (action != 2) {
                if (action != 3) {
                    if (action == 5) {
                        int actionIndex = motionEvent.getActionIndex();
                        this.mLastMotionX = motionEvent.getX(actionIndex);
                        i = motionEvent.getPointerId(actionIndex);
                    } else if (action == 6) {
                        onSecondaryPointerUp(motionEvent);
                        this.mLastMotionX = motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId));
                    }
                } else if (this.mIsBeingDragged) {
                    setCurrentItemInternal(this.mCurItem, true, true);
                }
                if (z) {
                    postInvalidateOnAnimation();
                }
                return true;
            } else {
                if (!this.mIsBeingDragged) {
                    int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (findPointerIndex != -1) {
                        float x = motionEvent.getX(findPointerIndex);
                        float abs = Math.abs(x - this.mLastMotionX);
                        float abs2 = Math.abs(motionEvent.getY(findPointerIndex) - this.mLastMotionY);
                        int i2 = this.mTouchSlop;
                        if (abs > ((float) i2) && abs > abs2) {
                            this.mIsBeingDragged = true;
                            float f = this.mInitialMotionX;
                            this.mLastMotionX = x - f > 0.0f ? f + ((float) i2) : f - ((float) i2);
                            setScrollState(1);
                            setScrollingCacheEnabled(true);
                        }
                    }
                }
                if (this.mIsBeingDragged) {
                    z = performDrag(motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId)));
                }
                if (z) {
                }
                return true;
            }
            z = resetTouch();
            if (z) {
            }
            return true;
        }
        this.mScroller.abortAnimation();
        this.mPopulatePending = false;
        populate();
        this.mIsBeingDragged = true;
        setScrollState(1);
        float x2 = motionEvent.getX();
        this.mInitialMotionX = x2;
        this.mLastMotionX = x2;
        i = motionEvent.getPointerId(0);
        this.mActivePointerId = i;
        if (z) {
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public boolean pageLeft() {
        int i = this.mCurItem;
        if (i <= 0) {
            return false;
        }
        setCurrentItem(i - 1, true);
        return true;
    }

    /* access modifiers changed from: 0000 */
    public boolean pageRight() {
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter == null || this.mCurItem >= pagerAdapter.getCount() - 1) {
            return false;
        }
        setCurrentItem(this.mCurItem + 1, true);
        return true;
    }

    public boolean performAccessibilityAction(int i, Bundle bundle) {
        int i2;
        if (super.performAccessibilityAction(i, bundle)) {
            return true;
        }
        if (i == 4096) {
            PagerAdapter pagerAdapter = this.mAdapter;
            if (pagerAdapter != null) {
                int i3 = this.mCurItem;
                if (i3 >= 0 && i3 < pagerAdapter.getCount() - 1) {
                    i2 = this.mCurItem + 1;
                }
            }
            return false;
        } else if (i != 8192) {
            return false;
        } else {
            PagerAdapter pagerAdapter2 = this.mAdapter;
            if (pagerAdapter2 != null) {
                int i4 = this.mCurItem;
                if (i4 > 0 && i4 < pagerAdapter2.getCount()) {
                    i2 = this.mCurItem - 1;
                }
            }
            return false;
        }
        setCurrentItem(i2);
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void populate() {
        populate(this.mCurItem);
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0056, code lost:
        if (r9 == r10) goto L_0x005d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00a8, code lost:
        if (r15 >= 0) goto L_0x00c6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00b6, code lost:
        if (r15 >= 0) goto L_0x00c6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00c4, code lost:
        if (r15 >= 0) goto L_0x00c6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00cf, code lost:
        r3 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x0117, code lost:
        if (r4 < r0.mItems.size()) goto L_0x0119;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0122, code lost:
        r7 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x0135, code lost:
        if (r4 < r0.mItems.size()) goto L_0x0119;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x0147, code lost:
        if (r4 < r0.mItems.size()) goto L_0x0119;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void populate(int i) {
        ItemInfo itemInfo;
        ItemInfo itemInfo2;
        int i2 = i;
        int i3 = this.mCurItem;
        if (i3 != i2) {
            itemInfo = infoForPosition(i3);
            this.mCurItem = i2;
        } else {
            itemInfo = null;
        }
        if (this.mAdapter != null && !this.mPopulatePending && getWindowToken() != null) {
            this.mAdapter.startUpdate((ViewGroup) this);
            int i4 = this.mOffscreenPageLimit;
            int max = Math.max(0, this.mCurItem - i4);
            int count = this.mAdapter.getCount();
            int min = Math.min(count - 1, this.mCurItem + i4);
            int i5 = 0;
            while (true) {
                if (i5 >= this.mItems.size()) {
                    break;
                }
                itemInfo2 = (ItemInfo) this.mItems.get(i5);
                int i6 = itemInfo2.position;
                int i7 = this.mCurItem;
                if (i6 < i7) {
                    i5++;
                }
            }
            itemInfo2 = null;
            if (itemInfo2 == null && count > 0) {
                itemInfo2 = addNewItem(this.mCurItem, i5);
            }
            if (itemInfo2 != null) {
                int i8 = i5 - 1;
                ItemInfo itemInfo3 = i8 >= 0 ? (ItemInfo) this.mItems.get(i8) : null;
                float f = 2.0f - itemInfo2.widthFactor;
                int i9 = this.mCurItem - 1;
                int i10 = i8;
                int i11 = i5;
                float f2 = 0.0f;
                while (i9 >= 0) {
                    if (f2 < f || i9 >= max) {
                        if (itemInfo3 == null || i9 != itemInfo3.position) {
                            f2 += addNewItem(i9, i10 + 1).widthFactor;
                            i11++;
                        } else {
                            f2 += itemInfo3.widthFactor;
                            i10--;
                        }
                    } else if (itemInfo3 == null) {
                        break;
                    } else {
                        if (i9 == itemInfo3.position && !itemInfo3.scrolling) {
                            this.mItems.remove(i10);
                            this.mAdapter.destroyItem((ViewGroup) this, i9, itemInfo3.object);
                            i10--;
                            i11--;
                        }
                        i9--;
                    }
                    ItemInfo itemInfo4 = (ItemInfo) this.mItems.get(i10);
                    itemInfo3 = itemInfo4;
                    i9--;
                }
                float f3 = itemInfo2.widthFactor;
                int i12 = i11 + 1;
                if (f3 < 2.0f) {
                    ItemInfo itemInfo5 = i12 < this.mItems.size() ? (ItemInfo) this.mItems.get(i12) : null;
                    int i13 = this.mCurItem;
                    while (true) {
                        i13++;
                        if (i13 >= count) {
                            break;
                        }
                        if (f3 < 2.0f || i13 <= min) {
                            if (itemInfo5 == null || i13 != itemInfo5.position) {
                                ItemInfo addNewItem = addNewItem(i13, i12);
                                i12++;
                                f3 += addNewItem.widthFactor;
                            } else {
                                f3 += itemInfo5.widthFactor;
                                i12++;
                            }
                        } else if (itemInfo5 == null) {
                            break;
                        } else if (i13 == itemInfo5.position && !itemInfo5.scrolling) {
                            this.mItems.remove(i12);
                            this.mAdapter.destroyItem((ViewGroup) this, i13, itemInfo5.object);
                        }
                        itemInfo5 = (ItemInfo) this.mItems.get(i12);
                    }
                }
                calculatePageOffsets(itemInfo2, i11, itemInfo);
            }
            this.mAdapter.setPrimaryItem((ViewGroup) this, this.mCurItem, itemInfo2 != null ? itemInfo2.object : null);
            this.mAdapter.finishUpdate((ViewGroup) this);
            int childCount = getChildCount();
            for (int i14 = 0; i14 < childCount; i14++) {
                View childAt = getChildAt(i14);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (!layoutParams.isDecor && layoutParams.widthFactor == 0.0f) {
                    ItemInfo infoForChild = infoForChild(childAt);
                    if (infoForChild != null) {
                        layoutParams.widthFactor = infoForChild.widthFactor;
                    }
                }
            }
            if (hasFocus()) {
                View findFocus = findFocus();
                ItemInfo infoForAnyChild = findFocus != null ? infoForAnyChild(findFocus) : null;
                if (infoForAnyChild == null || infoForAnyChild.position != this.mCurItem) {
                    for (int i15 = 0; i15 < getChildCount(); i15++) {
                        View childAt2 = getChildAt(i15);
                        ItemInfo infoForChild2 = infoForChild(childAt2);
                        if (infoForChild2 != null && infoForChild2.position == this.mCurItem && childAt2.requestFocus(2)) {
                            break;
                        }
                    }
                }
            }
        }
    }

    public void setAdapter(PagerAdapter pagerAdapter) {
        PagerAdapter pagerAdapter2 = this.mAdapter;
        if (pagerAdapter2 != null) {
            pagerAdapter2.unregisterDataSetObserver(this.mObserver);
            this.mAdapter.startUpdate((ViewGroup) this);
            Iterator it = this.mItems.iterator();
            while (it.hasNext()) {
                ItemInfo itemInfo = (ItemInfo) it.next();
                this.mAdapter.destroyItem((ViewGroup) this, itemInfo.position, itemInfo.object);
            }
            this.mAdapter.finishUpdate((ViewGroup) this);
            this.mItems.clear();
            removeNonDecorViews();
            this.mCurItem = 0;
            scrollTo(0, 0);
        }
        PagerAdapter pagerAdapter3 = this.mAdapter;
        this.mAdapter = pagerAdapter;
        if (this.mAdapter != null) {
            if (this.mObserver == null) {
                this.mObserver = new PagerObserver();
            }
            this.mAdapter.registerDataSetObserver(this.mObserver);
            this.mPopulatePending = false;
            this.mFirstLayout = true;
            if (this.mRestoredCurItem >= 0) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, null);
                setCurrentItemInternal(this.mRestoredCurItem, false, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
            } else {
                populate();
            }
        }
        OnAdapterChangeListener onAdapterChangeListener = this.mAdapterChangeListener;
        if (onAdapterChangeListener != null && pagerAdapter3 != pagerAdapter) {
            onAdapterChangeListener.onAdapterChanged(pagerAdapter3, pagerAdapter);
        }
    }

    public void setBottomMarginProgress(float f) {
        this.mBottomMarginProgress = f;
        this.mForceReplayout = true;
        requestLayout();
        invalidate();
    }

    public void setCurrentItem(int i) {
        this.mPopulatePending = false;
        setCurrentItemInternal(i, !this.mFirstLayout, false);
    }

    public void setCurrentItem(int i, boolean z) {
        this.mPopulatePending = false;
        setCurrentItemInternal(i, z, false);
    }

    /* access modifiers changed from: 0000 */
    public void setCurrentItemInternal(int i, boolean z, boolean z2) {
        setCurrentItemInternal(i, z, z2, 0);
    }

    /* access modifiers changed from: 0000 */
    public void setCurrentItemInternal(int i, boolean z, boolean z2, int i2) {
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter == null || pagerAdapter.getCount() <= 0) {
            setScrollingCacheEnabled(false);
        } else if (z2 || this.mCurItem != i || this.mItems.size() == 0) {
            boolean z3 = true;
            if (i < 0) {
                i = 0;
            } else if (i >= this.mAdapter.getCount()) {
                i = this.mAdapter.getCount() - 1;
            }
            int i3 = this.mOffscreenPageLimit;
            int i4 = this.mCurItem;
            if (i > i4 + i3 || i < i4 - i3) {
                Iterator it = this.mItems.iterator();
                while (it.hasNext()) {
                    ((ItemInfo) it.next()).scrolling = true;
                }
            }
            if (this.mCurItem == i) {
                z3 = false;
            }
            populate(i);
            ItemInfo infoForPosition = infoForPosition(i);
            int width = infoForPosition != null ? (int) (((float) getWidth()) * Math.max(this.mFirstOffset, Math.min(infoForPosition.offset, this.mLastOffset))) : 0;
            if (z) {
                smoothScrollTo(width, 0, i2);
                if (z3) {
                    OnPageChangeListener onPageChangeListener = this.mOnPageChangeListener;
                    if (onPageChangeListener != null) {
                        onPageChangeListener.onPageSelected(i);
                    }
                }
                if (z3) {
                    OnPageChangeListener onPageChangeListener2 = this.mInternalPageChangeListener;
                    if (onPageChangeListener2 != null) {
                        onPageChangeListener2.onPageSelected(i);
                    }
                }
            } else {
                if (z3) {
                    OnPageChangeListener onPageChangeListener3 = this.mOnPageChangeListener;
                    if (onPageChangeListener3 != null) {
                        onPageChangeListener3.onPageSelected(i);
                    }
                }
                if (z3) {
                    OnPageChangeListener onPageChangeListener4 = this.mInternalPageChangeListener;
                    if (onPageChangeListener4 != null) {
                        onPageChangeListener4.onPageSelected(i);
                    }
                }
                completeScroll();
                scrollTo(width, 0);
            }
        } else {
            setScrollingCacheEnabled(false);
        }
    }

    public void setDraggable(boolean z) {
        this.mDragEnabled = z;
    }

    public OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener onPageChangeListener) {
        OnPageChangeListener onPageChangeListener2 = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = onPageChangeListener;
        return onPageChangeListener2;
    }

    public void setOffscreenPageLimit(int i) {
        if (i < 3) {
            StringBuilder sb = new StringBuilder();
            sb.append("Requested offscreen page limit ");
            sb.append(i);
            sb.append(" too small; defaulting to ");
            sb.append(3);
            Log.w(TAG, sb.toString());
            i = 3;
        }
        if (i != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = i;
            populate();
        }
    }

    /* access modifiers changed from: 0000 */
    public void setOnAdapterChangeListener(OnAdapterChangeListener onAdapterChangeListener) {
        this.mAdapterChangeListener = onAdapterChangeListener;
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener;
    }

    public void setPageMargin(int i) {
        int i2 = this.mPageMargin;
        this.mPageMargin = i;
        int width = getWidth();
        recomputeScrollPosition(width, width, i, i2);
        requestLayout();
    }

    public void setPageMarginDrawable(int i) {
        setPageMarginDrawable(getContext().getResources().getDrawable(i));
    }

    public void setPageMarginDrawable(Drawable drawable) {
        this.mMarginDrawable = drawable;
        if (drawable != null) {
            refreshDrawableState();
        }
        setWillNotDraw(drawable == null);
        invalidate();
    }

    /* access modifiers changed from: 0000 */
    public void smoothScrollTo(int i, int i2) {
        smoothScrollTo(i, i2, 0);
    }

    /* access modifiers changed from: 0000 */
    public void smoothScrollTo(int i, int i2, int i3) {
        if (getChildCount() == 0) {
            setScrollingCacheEnabled(false);
            return;
        }
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        int i4 = i2 - scrollY;
        if (i - scrollX == 0 && i4 == 0) {
            completeScroll();
            populate();
            setScrollState(0);
            return;
        }
        setScrollingCacheEnabled(true);
        setScrollState(2);
        this.mScroller.startScroll((float) scrollX, (float) i, (float) scrollY, (float) i2, (float) (-i3));
        postInvalidateOnAnimation();
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.mMarginDrawable;
    }
}

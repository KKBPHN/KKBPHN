package miui.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;

public class ListPopupWindow {
    private static final boolean DEBUG = false;
    private static final int EXPAND_LIST_TIMEOUT = 250;
    public static final int INPUT_METHOD_FROM_FOCUSABLE = 0;
    public static final int INPUT_METHOD_NEEDED = 1;
    public static final int INPUT_METHOD_NOT_NEEDED = 2;
    public static final int MATCH_PARENT = -1;
    public static final int POSITION_PROMPT_ABOVE = 0;
    public static final int POSITION_PROMPT_BELOW = 1;
    private static final String TAG = "ListPopupWindow";
    public static final int WRAP_CONTENT = -2;
    private ListAdapter mAdapter;
    private Context mContext;
    private boolean mDropDownAlwaysVisible;
    private View mDropDownAnchorView;
    private int mDropDownHeight;
    private int mDropDownHorizontalOffset;
    /* access modifiers changed from: private */
    public DropDownListView mDropDownList;
    private Drawable mDropDownListHighlight;
    private int mDropDownVerticalOffset;
    private boolean mDropDownVerticalOffsetSet;
    private int mDropDownWidth;
    private boolean mForceIgnoreOutsideTouch;
    /* access modifiers changed from: private */
    public Handler mHandler;
    private final ListSelectorHider mHideSelector;
    private OnItemClickListener mItemClickListener;
    private OnItemSelectedListener mItemSelectedListener;
    int mListItemExpandMaximum;
    private boolean mModal;
    private DataSetObserver mObserver;
    /* access modifiers changed from: private */
    public ArrowPopupWindow mPopup;
    private int mPromptPosition;
    private View mPromptView;
    /* access modifiers changed from: private */
    public final ResizePopupRunnable mResizePopupRunnable;
    private final PopupScrollListener mScrollListener;
    private Runnable mShowDropDownRunnable;
    private Rect mTempRect;
    private final PopupTouchInterceptor mTouchInterceptor;

    class DropDownListView extends ListView {
        public static final int INVALID_POSITION = -1;
        static final int NO_POSITION = -1;
        private boolean mHijackFocus;
        /* access modifiers changed from: private */
        public boolean mListSelectionHidden;

        public DropDownListView(Context context, boolean z) {
            super(context, null, 16842861);
            this.mHijackFocus = z;
            setCacheColorHint(0);
        }

        /* access modifiers changed from: private */
        public int lookForSelectablePosition(int i, boolean z) {
            int i2;
            ListAdapter adapter = getAdapter();
            if (adapter != null && !isInTouchMode()) {
                int count = adapter.getCount();
                if (!getAdapter().areAllItemsEnabled()) {
                    if (z) {
                        i2 = Math.max(0, i);
                        while (i2 < count && !adapter.isEnabled(i2)) {
                            i2++;
                        }
                    } else {
                        int min = Math.min(i, count - 1);
                        while (i2 >= 0 && !adapter.isEnabled(i2)) {
                            min = i2 - 1;
                        }
                    }
                    if (i2 < 0 || i2 >= count) {
                        return -1;
                    }
                    return i2;
                } else if (i < 0 || i >= count) {
                    return -1;
                } else {
                    return i;
                }
            }
            return -1;
        }

        public boolean hasFocus() {
            return this.mHijackFocus || super.hasFocus();
        }

        public boolean hasWindowFocus() {
            return this.mHijackFocus || super.hasWindowFocus();
        }

        public boolean isFocused() {
            return this.mHijackFocus || super.isFocused();
        }

        public boolean isInTouchMode() {
            return (this.mHijackFocus && this.mListSelectionHidden) || super.isInTouchMode();
        }

        /* access modifiers changed from: 0000 */
        public final int measureHeightOfChildrenCompact(int i, int i2, int i3, int i4, int i5) {
            int listPaddingTop = getListPaddingTop();
            int listPaddingBottom = getListPaddingBottom();
            int dividerHeight = getDividerHeight();
            Drawable divider = getDivider();
            ListAdapter adapter = getAdapter();
            int i6 = listPaddingTop + listPaddingBottom;
            if (adapter == null) {
                return i6;
            }
            if (dividerHeight <= 0 || divider == null) {
                dividerHeight = 0;
            }
            int count = adapter.getCount();
            int i7 = i6;
            int i8 = 0;
            int i9 = 0;
            int i10 = 0;
            View view = null;
            while (i8 < count) {
                int itemViewType = adapter.getItemViewType(i8);
                if (itemViewType != i9) {
                    view = null;
                    i9 = itemViewType;
                }
                view = adapter.getView(i8, view, this);
                int i11 = view.getLayoutParams().height;
                view.measure(i, i11 > 0 ? MeasureSpec.makeMeasureSpec(i11, 1073741824) : MeasureSpec.makeMeasureSpec(0, 0));
                if (i8 > 0) {
                    i7 += dividerHeight;
                }
                i7 += view.getMeasuredHeight();
                if (i7 >= i4) {
                    if (i5 >= 0 && i8 > i5 && i10 > 0 && i7 != i4) {
                        i4 = i10;
                    }
                    return i4;
                }
                if (i5 >= 0 && i8 >= i5) {
                    i10 = i7;
                }
                i8++;
            }
            return i7;
        }
    }

    class ListSelectorHider implements Runnable {
        private ListSelectorHider() {
        }

        public void run() {
            ListPopupWindow.this.clearListSelection();
        }
    }

    class PopupDataSetObserver extends DataSetObserver {
        private PopupDataSetObserver() {
        }

        public void onChanged() {
            if (ListPopupWindow.this.isShowing()) {
                ListPopupWindow.this.show();
            }
        }

        public void onInvalidated() {
            ListPopupWindow.this.dismiss(true);
        }
    }

    class PopupScrollListener implements OnScrollListener {
        private PopupScrollListener() {
        }

        public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        }

        public void onScrollStateChanged(AbsListView absListView, int i) {
            if (i == 1 && !ListPopupWindow.this.isInputMethodNotNeeded() && ListPopupWindow.this.mPopup.getContentView() != null) {
                ListPopupWindow.this.mHandler.removeCallbacks(ListPopupWindow.this.mResizePopupRunnable);
                ListPopupWindow.this.mResizePopupRunnable.run();
            }
        }
    }

    class PopupTouchInterceptor implements OnTouchListener {
        private PopupTouchInterceptor() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            if (action == 0 && ListPopupWindow.this.mPopup != null && ListPopupWindow.this.mPopup.isShowing() && x >= 0 && x < ListPopupWindow.this.mPopup.getContentWidth() && y >= 0 && y < ListPopupWindow.this.mPopup.getContentHeight()) {
                ListPopupWindow.this.mHandler.postDelayed(ListPopupWindow.this.mResizePopupRunnable, 250);
            } else if (action == 1) {
                ListPopupWindow.this.mHandler.removeCallbacks(ListPopupWindow.this.mResizePopupRunnable);
            }
            return false;
        }
    }

    class ResizePopupRunnable implements Runnable {
        private ResizePopupRunnable() {
        }

        public void run() {
            if (ListPopupWindow.this.mDropDownList != null && ListPopupWindow.this.mDropDownList.getCount() > ListPopupWindow.this.mDropDownList.getChildCount()) {
                int childCount = ListPopupWindow.this.mDropDownList.getChildCount();
                ListPopupWindow listPopupWindow = ListPopupWindow.this;
                if (childCount <= listPopupWindow.mListItemExpandMaximum) {
                    listPopupWindow.mPopup.setInputMethodMode(2);
                    ListPopupWindow.this.show();
                }
            }
        }
    }

    public ListPopupWindow(Context context) {
        this(context, null, 16843519);
    }

    public ListPopupWindow(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16843519);
    }

    public ListPopupWindow(Context context, AttributeSet attributeSet, int i) {
        this.mResizePopupRunnable = new ResizePopupRunnable();
        this.mTouchInterceptor = new PopupTouchInterceptor();
        this.mScrollListener = new PopupScrollListener();
        this.mHideSelector = new ListSelectorHider();
        this.mListItemExpandMaximum = Integer.MAX_VALUE;
        this.mDropDownHeight = -2;
        this.mDropDownWidth = -2;
        this.mDropDownAlwaysVisible = false;
        this.mForceIgnoreOutsideTouch = false;
        this.mPromptPosition = 0;
        this.mHandler = new Handler();
        this.mTempRect = new Rect();
        this.mContext = context;
        this.mPopup = new ArrowPopupWindow(context, attributeSet, i);
    }

    /* JADX WARNING: Removed duplicated region for block: B:45:0x0144  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int buildDropDown() {
        int i;
        int i2;
        int makeMeasureSpec;
        int measureHeightOfChildrenCompact;
        View view;
        int i3 = Integer.MIN_VALUE;
        boolean z = false;
        if (this.mDropDownList == null) {
            Context context = this.mContext;
            this.mShowDropDownRunnable = new Runnable() {
                public void run() {
                    View anchorView = ListPopupWindow.this.getAnchorView();
                    if (anchorView != null && anchorView.getWindowToken() != null) {
                        ListPopupWindow.this.show();
                    }
                }
            };
            this.mDropDownList = new DropDownListView(context, !this.mModal);
            Drawable drawable = this.mDropDownListHighlight;
            if (drawable != null) {
                this.mDropDownList.setSelector(drawable);
            }
            this.mDropDownList.setAdapter(this.mAdapter);
            this.mDropDownList.setOnItemClickListener(this.mItemClickListener);
            this.mDropDownList.setFocusable(true);
            this.mDropDownList.setFocusableInTouchMode(true);
            this.mDropDownList.setOnItemSelectedListener(new OnItemSelectedListener() {
                public void onItemSelected(AdapterView adapterView, View view, int i, long j) {
                    if (i != -1) {
                        DropDownListView access$700 = ListPopupWindow.this.mDropDownList;
                        if (access$700 != null) {
                            access$700.mListSelectionHidden = false;
                        }
                    }
                }

                public void onNothingSelected(AdapterView adapterView) {
                }
            });
            this.mDropDownList.setOnScrollListener(this.mScrollListener);
            OnItemSelectedListener onItemSelectedListener = this.mItemSelectedListener;
            if (onItemSelectedListener != null) {
                this.mDropDownList.setOnItemSelectedListener(onItemSelectedListener);
            }
            View view2 = this.mDropDownList;
            View view3 = this.mPromptView;
            if (view3 != null) {
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(1);
                LayoutParams layoutParams = new LayoutParams(-1, 0, 1.0f);
                int i4 = this.mPromptPosition;
                if (i4 == 0) {
                    linearLayout.addView(view3);
                    linearLayout.addView(view2, layoutParams);
                } else if (i4 != 1) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Invalid hint position ");
                    sb.append(this.mPromptPosition);
                    Log.e(TAG, sb.toString());
                } else {
                    linearLayout.addView(view2, layoutParams);
                    linearLayout.addView(view3);
                }
                view3.measure(MeasureSpec.makeMeasureSpec(this.mDropDownWidth, Integer.MIN_VALUE), 0);
                LayoutParams layoutParams2 = (LayoutParams) view3.getLayoutParams();
                i = layoutParams2.bottomMargin + view3.getMeasuredHeight() + layoutParams2.topMargin;
                view = linearLayout;
            } else {
                i = 0;
                view = view2;
            }
            this.mPopup.setContentView(view);
        } else {
            View view4 = this.mPromptView;
            if (view4 != null) {
                LayoutParams layoutParams3 = (LayoutParams) view4.getLayoutParams();
                i = view4.getMeasuredHeight() + layoutParams3.topMargin + layoutParams3.bottomMargin;
            } else {
                i = 0;
            }
        }
        Drawable background = this.mPopup.getBackground();
        if (background != null) {
            background.getPadding(this.mTempRect);
            Rect rect = this.mTempRect;
            int i5 = rect.top;
            i2 = rect.bottom + i5;
            if (!this.mDropDownVerticalOffsetSet) {
                this.mDropDownVerticalOffset = -i5;
            }
        } else {
            this.mTempRect.setEmpty();
            i2 = 0;
        }
        if (this.mPopup.getInputMethodMode() == 2) {
            z = true;
        }
        int maxAvailableHeight = getMaxAvailableHeight(getAnchorView(), this.mDropDownVerticalOffset, z);
        if (this.mDropDownAlwaysVisible || this.mDropDownHeight == -1) {
            return maxAvailableHeight + i2;
        }
        int i6 = this.mDropDownWidth;
        if (i6 != -2) {
            i3 = 1073741824;
            if (i6 != -1) {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec(i6, 1073741824);
                measureHeightOfChildrenCompact = this.mDropDownList.measureHeightOfChildrenCompact(makeMeasureSpec, 0, -1, maxAvailableHeight - i, -1);
                if (measureHeightOfChildrenCompact > 0) {
                    i += i2;
                }
                return measureHeightOfChildrenCompact + i;
            }
        }
        int i7 = this.mContext.getResources().getDisplayMetrics().widthPixels;
        Rect rect2 = this.mTempRect;
        makeMeasureSpec = MeasureSpec.makeMeasureSpec(i7 - (rect2.left + rect2.right), i3);
        measureHeightOfChildrenCompact = this.mDropDownList.measureHeightOfChildrenCompact(makeMeasureSpec, 0, -1, maxAvailableHeight - i, -1);
        if (measureHeightOfChildrenCompact > 0) {
        }
        return measureHeightOfChildrenCompact + i;
    }

    private void removePromptView() {
        View view = this.mPromptView;
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(this.mPromptView);
            }
        }
    }

    public void clearListSelection() {
        DropDownListView dropDownListView = this.mDropDownList;
        if (dropDownListView != null) {
            dropDownListView.mListSelectionHidden = true;
            dropDownListView.requestLayout();
        }
    }

    public void dismiss() {
        dismiss(true);
    }

    public void dismiss(boolean z) {
        this.mPopup.dismiss(z);
        removePromptView();
        this.mDropDownList = null;
        this.mHandler.removeCallbacks(this.mResizePopupRunnable);
    }

    public View getAnchorView() {
        return this.mDropDownAnchorView;
    }

    public int getAnimationStyle() {
        return this.mPopup.getAnimationStyle();
    }

    public Drawable getBackground() {
        return this.mPopup.getBackground();
    }

    public int getHeight() {
        return this.mDropDownHeight;
    }

    public int getHorizontalOffset() {
        return this.mDropDownHorizontalOffset;
    }

    public int getInputMethodMode() {
        return this.mPopup.getInputMethodMode();
    }

    public ListView getListView() {
        return this.mDropDownList;
    }

    public int getMaxAvailableHeight(View view, int i, boolean z) {
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        int i2 = rect.bottom;
        if (z) {
            i2 = view.getContext().getResources().getDisplayMetrics().heightPixels;
        }
        int max = Math.max((i2 - (iArr[1] + view.getHeight())) - i, (iArr[1] - rect.top) + i);
        if (this.mPopup.getBackground() == null) {
            return max;
        }
        this.mPopup.getBackground().getPadding(this.mTempRect);
        Rect rect2 = this.mTempRect;
        return max - (rect2.top + rect2.bottom);
    }

    public ArrowPopupWindow getPopupWindow() {
        return this.mPopup;
    }

    public int getPromptPosition() {
        return this.mPromptPosition;
    }

    public Object getSelectedItem() {
        if (!isShowing()) {
            return null;
        }
        return this.mDropDownList.getSelectedItem();
    }

    public long getSelectedItemId() {
        if (!isShowing()) {
            return Long.MIN_VALUE;
        }
        return this.mDropDownList.getSelectedItemId();
    }

    public int getSelectedItemPosition() {
        if (!isShowing()) {
            return -1;
        }
        return this.mDropDownList.getSelectedItemPosition();
    }

    public View getSelectedView() {
        if (!isShowing()) {
            return null;
        }
        return this.mDropDownList.getSelectedView();
    }

    public int getSoftInputMode() {
        return this.mPopup.getSoftInputMode();
    }

    public int getVerticalOffset() {
        if (!this.mDropDownVerticalOffsetSet) {
            return 0;
        }
        return this.mDropDownVerticalOffset;
    }

    public int getWidth() {
        return this.mDropDownWidth;
    }

    public boolean isDropDownAlwaysVisible() {
        return this.mDropDownAlwaysVisible;
    }

    public boolean isInputMethodNotNeeded() {
        return this.mPopup.getInputMethodMode() == 2;
    }

    public boolean isModal() {
        return this.mModal;
    }

    public boolean isShowing() {
        return this.mPopup.isShowing();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        int i2;
        if (isShowing() && i != 62 && (this.mDropDownList.getSelectedItemPosition() >= 0 || !(i == 66 || i == 23))) {
            int selectedItemPosition = this.mDropDownList.getSelectedItemPosition();
            boolean z = !this.mPopup.isAboveAnchor();
            ListAdapter listAdapter = this.mAdapter;
            int i3 = Integer.MAX_VALUE;
            if (listAdapter != null) {
                boolean areAllItemsEnabled = listAdapter.areAllItemsEnabled();
                int access$600 = areAllItemsEnabled ? 0 : this.mDropDownList.lookForSelectablePosition(0, true);
                i2 = areAllItemsEnabled ? listAdapter.getCount() - 1 : this.mDropDownList.lookForSelectablePosition(listAdapter.getCount() - 1, false);
                i3 = access$600;
            } else {
                i2 = Integer.MIN_VALUE;
            }
            if ((!z || i != 19 || selectedItemPosition > i3) && (z || i != 20 || selectedItemPosition < i2)) {
                this.mDropDownList.mListSelectionHidden = false;
                if (this.mDropDownList.onKeyDown(i, keyEvent)) {
                    this.mPopup.setInputMethodMode(2);
                    this.mDropDownList.requestFocusFromTouch();
                    show();
                    if (i == 19 || i == 20 || i == 23 || i == 66) {
                        return true;
                    }
                } else if (!z || i != 20) {
                    return !z && i == 19 && selectedItemPosition == i3;
                } else {
                    if (selectedItemPosition == i2) {
                        return true;
                    }
                }
            } else {
                clearListSelection();
                this.mPopup.setInputMethodMode(1);
                show();
                return true;
            }
        }
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (!isShowing() || this.mDropDownList.getSelectedItemPosition() < 0) {
            return false;
        }
        boolean onKeyUp = this.mDropDownList.onKeyUp(i, keyEvent);
        if (onKeyUp && (i == 23 || i == 66)) {
            dismiss(true);
        }
        return onKeyUp;
    }

    public boolean performItemClick(int i) {
        if (!isShowing()) {
            return false;
        }
        if (this.mItemClickListener != null) {
            DropDownListView dropDownListView = this.mDropDownList;
            int i2 = i;
            this.mItemClickListener.onItemClick(dropDownListView, dropDownListView.getChildAt(i - dropDownListView.getFirstVisiblePosition()), i2, dropDownListView.getAdapter().getItemId(i));
        }
        return true;
    }

    public void postShow() {
        this.mHandler.post(this.mShowDropDownRunnable);
    }

    public void setAdapter(ListAdapter listAdapter) {
        DataSetObserver dataSetObserver = this.mObserver;
        if (dataSetObserver == null) {
            this.mObserver = new PopupDataSetObserver();
        } else {
            ListAdapter listAdapter2 = this.mAdapter;
            if (listAdapter2 != null) {
                listAdapter2.unregisterDataSetObserver(dataSetObserver);
            }
        }
        this.mAdapter = listAdapter;
        if (this.mAdapter != null) {
            listAdapter.registerDataSetObserver(this.mObserver);
        }
        DropDownListView dropDownListView = this.mDropDownList;
        if (dropDownListView != null) {
            dropDownListView.setAdapter(this.mAdapter);
        }
    }

    public void setAnchorView(View view) {
        this.mDropDownAnchorView = view;
    }

    public void setAnimationStyle(int i) {
        this.mPopup.setAnimationStyle(i);
    }

    public void setBackgroundDrawable(Drawable drawable) {
        this.mPopup.setBackgroundDrawable(drawable);
    }

    public void setContentWidth(int i) {
        Drawable background = this.mPopup.getBackground();
        if (background != null) {
            background.getPadding(this.mTempRect);
            Rect rect = this.mTempRect;
            this.mDropDownWidth = rect.left + rect.right + i;
            return;
        }
        setWidth(i);
    }

    public void setDropDownAlwaysVisible(boolean z) {
        this.mDropDownAlwaysVisible = z;
    }

    public void setForceIgnoreOutsideTouch(boolean z) {
        this.mForceIgnoreOutsideTouch = z;
    }

    public void setHeight(int i) {
        this.mDropDownHeight = i;
    }

    public void setHorizontalOffset(int i) {
        this.mDropDownHorizontalOffset = i;
    }

    public void setInputMethodMode(int i) {
        this.mPopup.setInputMethodMode(i);
    }

    /* access modifiers changed from: 0000 */
    public void setListItemExpandMax(int i) {
        this.mListItemExpandMaximum = i;
    }

    public void setListSelector(Drawable drawable) {
        this.mDropDownListHighlight = drawable;
    }

    public void setModal(boolean z) {
        this.mModal = true;
        this.mPopup.setFocusable(z);
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.mPopup.setOnDismissListener(onDismissListener);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.mItemSelectedListener = onItemSelectedListener;
    }

    public void setPromptPosition(int i) {
        this.mPromptPosition = i;
    }

    public void setPromptView(View view) {
        boolean isShowing = isShowing();
        if (isShowing) {
            removePromptView();
        }
        this.mPromptView = view;
        if (isShowing) {
            show();
        }
    }

    public void setSelection(int i) {
        DropDownListView dropDownListView = this.mDropDownList;
        if (isShowing() && dropDownListView != null) {
            dropDownListView.mListSelectionHidden = false;
            dropDownListView.setSelection(i);
            if (dropDownListView.getChoiceMode() != 0) {
                dropDownListView.setItemChecked(i, true);
            }
        }
    }

    public void setSoftInputMode(int i) {
        this.mPopup.setSoftInputMode(i);
    }

    public void setVerticalOffset(int i) {
        this.mDropDownVerticalOffset = i;
        this.mDropDownVerticalOffsetSet = true;
    }

    public void setWidth(int i) {
        this.mDropDownWidth = i;
    }

    public void show() {
        int buildDropDown = buildDropDown();
        int i = this.mDropDownWidth;
        if (i != -1) {
            if (i == -2) {
                this.mPopup.setContentWidth(getAnchorView().getWidth());
            } else {
                this.mPopup.setContentWidth(i);
            }
        }
        int i2 = this.mDropDownHeight;
        if (i2 != -1) {
            if (i2 == -2) {
                this.mPopup.setContentHeight(buildDropDown);
            } else {
                this.mPopup.setContentHeight(i2);
            }
        }
        boolean z = true;
        this.mPopup.setFocusable(true);
        if (this.mPopup.isShowing()) {
            ArrowPopupWindow arrowPopupWindow = this.mPopup;
            if (this.mForceIgnoreOutsideTouch || this.mDropDownAlwaysVisible) {
                z = false;
            }
            arrowPopupWindow.setOutsideTouchable(z);
            this.mPopup.update(getAnchorView(), this.mDropDownHorizontalOffset, this.mDropDownVerticalOffset, -1, -1);
            return;
        }
        this.mPopup.setWindowLayoutMode(-1, -1);
        ArrowPopupWindow arrowPopupWindow2 = this.mPopup;
        if (this.mForceIgnoreOutsideTouch || this.mDropDownAlwaysVisible) {
            z = false;
        }
        arrowPopupWindow2.setOutsideTouchable(z);
        this.mPopup.setTouchInterceptor(this.mTouchInterceptor);
        this.mPopup.show(getAnchorView(), this.mDropDownHorizontalOffset, this.mDropDownVerticalOffset);
        this.mDropDownList.setSelection(-1);
        if (!this.mModal || this.mDropDownList.isInTouchMode()) {
            clearListSelection();
        }
        if (!this.mModal) {
            this.mHandler.post(this.mHideSelector);
        }
    }
}

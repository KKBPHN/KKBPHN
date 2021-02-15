package androidx.appcompat.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.ColorStateList;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.ThemedSpinnerAdapter;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.R;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.core.view.TintableBackgroundView;
import androidx.core.view.ViewCompat;

public class AppCompatSpinner extends Spinner implements TintableBackgroundView {
    private static final int[] ATTRS_ANDROID_SPINNERMODE = {16843505};
    private static final int MAX_ITEMS_MEASURED = 15;
    private static final int MODE_DIALOG = 0;
    private static final int MODE_DROPDOWN = 1;
    private static final int MODE_THEME = -1;
    private static final String TAG = "AppCompatSpinner";
    private final AppCompatBackgroundHelper mBackgroundTintHelper;
    int mDropDownWidth;
    private ForwardingListener mForwardingListener;
    private SpinnerPopup mPopup;
    private final Context mPopupContext;
    private final boolean mPopupSet;
    private SpinnerAdapter mTempAdapter;
    final Rect mTempRect;

    @VisibleForTesting
    class DialogPopup implements SpinnerPopup, OnClickListener {
        private ListAdapter mListAdapter;
        @VisibleForTesting
        AlertDialog mPopup;
        private CharSequence mPrompt;

        DialogPopup() {
        }

        public void dismiss() {
            AlertDialog alertDialog = this.mPopup;
            if (alertDialog != null) {
                alertDialog.dismiss();
                this.mPopup = null;
            }
        }

        public Drawable getBackground() {
            return null;
        }

        public CharSequence getHintText() {
            return this.mPrompt;
        }

        public int getHorizontalOffset() {
            return 0;
        }

        public int getVerticalOffset() {
            return 0;
        }

        public boolean isShowing() {
            AlertDialog alertDialog = this.mPopup;
            if (alertDialog != null) {
                return alertDialog.isShowing();
            }
            return false;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            AppCompatSpinner.this.setSelection(i);
            if (AppCompatSpinner.this.getOnItemClickListener() != null) {
                AppCompatSpinner.this.performItemClick(null, i, this.mListAdapter.getItemId(i));
            }
            dismiss();
        }

        public void setAdapter(ListAdapter listAdapter) {
            this.mListAdapter = listAdapter;
        }

        public void setBackgroundDrawable(Drawable drawable) {
            Log.e(AppCompatSpinner.TAG, "Cannot set popup background for MODE_DIALOG, ignoring");
        }

        public void setHorizontalOffset(int i) {
            Log.e(AppCompatSpinner.TAG, "Cannot set horizontal offset for MODE_DIALOG, ignoring");
        }

        public void setPromptText(CharSequence charSequence) {
            this.mPrompt = charSequence;
        }

        public void setVerticalOffset(int i) {
            Log.e(AppCompatSpinner.TAG, "Cannot set vertical offset for MODE_DIALOG, ignoring");
        }

        public void show(int i, int i2) {
            if (this.mListAdapter != null) {
                Builder builder = new Builder(AppCompatSpinner.this.getPopupContext());
                CharSequence charSequence = this.mPrompt;
                if (charSequence != null) {
                    builder.setTitle(charSequence);
                }
                this.mPopup = builder.setSingleChoiceItems(this.mListAdapter, AppCompatSpinner.this.getSelectedItemPosition(), (OnClickListener) this).create();
                ListView listView = this.mPopup.getListView();
                if (VERSION.SDK_INT >= 17) {
                    listView.setTextDirection(i);
                    listView.setTextAlignment(i2);
                }
                this.mPopup.show();
            }
        }
    }

    class DropDownAdapter implements ListAdapter, SpinnerAdapter {
        private SpinnerAdapter mAdapter;
        private ListAdapter mListAdapter;

        public DropDownAdapter(@Nullable SpinnerAdapter spinnerAdapter, @Nullable Theme theme) {
            this.mAdapter = spinnerAdapter;
            if (spinnerAdapter instanceof ListAdapter) {
                this.mListAdapter = (ListAdapter) spinnerAdapter;
            }
            if (theme == null) {
                return;
            }
            if (VERSION.SDK_INT >= 23 && (spinnerAdapter instanceof ThemedSpinnerAdapter)) {
                ThemedSpinnerAdapter themedSpinnerAdapter = (ThemedSpinnerAdapter) spinnerAdapter;
                if (themedSpinnerAdapter.getDropDownViewTheme() != theme) {
                    themedSpinnerAdapter.setDropDownViewTheme(theme);
                }
            } else if (spinnerAdapter instanceof ThemedSpinnerAdapter) {
                ThemedSpinnerAdapter themedSpinnerAdapter2 = (ThemedSpinnerAdapter) spinnerAdapter;
                if (themedSpinnerAdapter2.getDropDownViewTheme() == null) {
                    themedSpinnerAdapter2.setDropDownViewTheme(theme);
                }
            }
        }

        public boolean areAllItemsEnabled() {
            ListAdapter listAdapter = this.mListAdapter;
            if (listAdapter != null) {
                return listAdapter.areAllItemsEnabled();
            }
            return true;
        }

        public int getCount() {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter == null) {
                return 0;
            }
            return spinnerAdapter.getCount();
        }

        public View getDropDownView(int i, View view, ViewGroup viewGroup) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter == null) {
                return null;
            }
            return spinnerAdapter.getDropDownView(i, view, viewGroup);
        }

        public Object getItem(int i) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter == null) {
                return null;
            }
            return spinnerAdapter.getItem(i);
        }

        public long getItemId(int i) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter == null) {
                return -1;
            }
            return spinnerAdapter.getItemId(i);
        }

        public int getItemViewType(int i) {
            return 0;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            return getDropDownView(i, view, viewGroup);
        }

        public int getViewTypeCount() {
            return 1;
        }

        public boolean hasStableIds() {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            return spinnerAdapter != null && spinnerAdapter.hasStableIds();
        }

        public boolean isEmpty() {
            return getCount() == 0;
        }

        public boolean isEnabled(int i) {
            ListAdapter listAdapter = this.mListAdapter;
            if (listAdapter != null) {
                return listAdapter.isEnabled(i);
            }
            return true;
        }

        public void registerDataSetObserver(DataSetObserver dataSetObserver) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter != null) {
                spinnerAdapter.registerDataSetObserver(dataSetObserver);
            }
        }

        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter != null) {
                spinnerAdapter.unregisterDataSetObserver(dataSetObserver);
            }
        }
    }

    class DropdownPopup extends ListPopupWindow implements SpinnerPopup {
        ListAdapter mAdapter;
        private CharSequence mHintText;
        private final Rect mVisibleRect = new Rect();

        public DropdownPopup(Context context, AttributeSet attributeSet, int i) {
            super(context, attributeSet, i);
            setAnchorView(AppCompatSpinner.this);
            setModal(true);
            setPromptPosition(0);
            setOnItemClickListener(new OnItemClickListener(AppCompatSpinner.this) {
                public void onItemClick(AdapterView adapterView, View view, int i, long j) {
                    AppCompatSpinner.this.setSelection(i);
                    if (AppCompatSpinner.this.getOnItemClickListener() != null) {
                        DropdownPopup dropdownPopup = DropdownPopup.this;
                        AppCompatSpinner.this.performItemClick(view, i, dropdownPopup.mAdapter.getItemId(i));
                    }
                    DropdownPopup.this.dismiss();
                }
            });
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Removed duplicated region for block: B:21:0x008d  */
        /* JADX WARNING: Removed duplicated region for block: B:22:0x0095  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void computeContentWidth() {
            int i;
            Drawable background = getBackground();
            int i2 = 0;
            if (background != null) {
                background.getPadding(AppCompatSpinner.this.mTempRect);
                i2 = ViewUtils.isLayoutRtl(AppCompatSpinner.this) ? AppCompatSpinner.this.mTempRect.right : -AppCompatSpinner.this.mTempRect.left;
            } else {
                Rect rect = AppCompatSpinner.this.mTempRect;
                rect.right = 0;
                rect.left = 0;
            }
            int paddingLeft = AppCompatSpinner.this.getPaddingLeft();
            int paddingRight = AppCompatSpinner.this.getPaddingRight();
            int width = AppCompatSpinner.this.getWidth();
            AppCompatSpinner appCompatSpinner = AppCompatSpinner.this;
            int i3 = appCompatSpinner.mDropDownWidth;
            if (i3 == -2) {
                int compatMeasureContentWidth = appCompatSpinner.compatMeasureContentWidth((SpinnerAdapter) this.mAdapter, getBackground());
                int i4 = AppCompatSpinner.this.getContext().getResources().getDisplayMetrics().widthPixels;
                Rect rect2 = AppCompatSpinner.this.mTempRect;
                int i5 = (i4 - rect2.left) - rect2.right;
                if (compatMeasureContentWidth > i5) {
                    compatMeasureContentWidth = i5;
                }
                i = Math.max(compatMeasureContentWidth, (width - paddingLeft) - paddingRight);
            } else if (i3 == -1) {
                i = (width - paddingLeft) - paddingRight;
            } else {
                setContentWidth(i3);
                setHorizontalOffset(!ViewUtils.isLayoutRtl(AppCompatSpinner.this) ? i2 + ((width - paddingRight) - getWidth()) : i2 + paddingLeft);
            }
            setContentWidth(i);
            setHorizontalOffset(!ViewUtils.isLayoutRtl(AppCompatSpinner.this) ? i2 + ((width - paddingRight) - getWidth()) : i2 + paddingLeft);
        }

        public CharSequence getHintText() {
            return this.mHintText;
        }

        /* access modifiers changed from: 0000 */
        public boolean isVisibleToUser(View view) {
            return ViewCompat.isAttachedToWindow(view) && view.getGlobalVisibleRect(this.mVisibleRect);
        }

        public void setAdapter(ListAdapter listAdapter) {
            super.setAdapter(listAdapter);
            this.mAdapter = listAdapter;
        }

        public void setPromptText(CharSequence charSequence) {
            this.mHintText = charSequence;
        }

        public void show(int i, int i2) {
            boolean isShowing = isShowing();
            computeContentWidth();
            setInputMethodMode(2);
            super.show();
            ListView listView = getListView();
            listView.setChoiceMode(1);
            if (VERSION.SDK_INT >= 17) {
                listView.setTextDirection(i);
                listView.setTextAlignment(i2);
            }
            setSelection(AppCompatSpinner.this.getSelectedItemPosition());
            if (!isShowing) {
                ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
                if (viewTreeObserver != null) {
                    final AnonymousClass2 r6 = new OnGlobalLayoutListener() {
                        public void onGlobalLayout() {
                            DropdownPopup dropdownPopup = DropdownPopup.this;
                            if (!dropdownPopup.isVisibleToUser(AppCompatSpinner.this)) {
                                DropdownPopup.this.dismiss();
                                return;
                            }
                            DropdownPopup.this.computeContentWidth();
                            DropdownPopup.super.show();
                        }
                    };
                    viewTreeObserver.addOnGlobalLayoutListener(r6);
                    setOnDismissListener(new OnDismissListener() {
                        public void onDismiss() {
                            ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
                            if (viewTreeObserver != null) {
                                viewTreeObserver.removeGlobalOnLayoutListener(r6);
                            }
                        }
                    });
                }
            }
        }
    }

    class SavedState extends BaseSavedState {
        public static final Creator CREATOR = new Creator() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        boolean mShowDropdown;

        SavedState(Parcel parcel) {
            super(parcel);
            this.mShowDropdown = parcel.readByte() != 0;
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeByte(this.mShowDropdown ? (byte) 1 : 0);
        }
    }

    @VisibleForTesting
    interface SpinnerPopup {
        void dismiss();

        Drawable getBackground();

        CharSequence getHintText();

        int getHorizontalOffset();

        int getVerticalOffset();

        boolean isShowing();

        void setAdapter(ListAdapter listAdapter);

        void setBackgroundDrawable(Drawable drawable);

        void setHorizontalOffset(int i);

        void setPromptText(CharSequence charSequence);

        void setVerticalOffset(int i);

        void show(int i, int i2);
    }

    public AppCompatSpinner(Context context) {
        this(context, (AttributeSet) null);
    }

    public AppCompatSpinner(Context context, int i) {
        this(context, null, R.attr.spinnerStyle, i);
    }

    public AppCompatSpinner(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.spinnerStyle);
    }

    public AppCompatSpinner(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, -1);
    }

    public AppCompatSpinner(Context context, AttributeSet attributeSet, int i, int i2) {
        this(context, attributeSet, i, i2, null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0046, code lost:
        if (r11 != null) goto L_0x0048;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0048, code lost:
        r11.recycle();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x005a, code lost:
        if (r11 != null) goto L_0x0048;
     */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x0036 A[SYNTHETIC, Splitter:B:10:0x0036] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0060  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0067  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00a1  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00bb  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00d4  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public AppCompatSpinner(Context context, AttributeSet attributeSet, int i, int i2, Theme theme) {
        CharSequence[] textArray;
        SpinnerAdapter spinnerAdapter;
        TypedArray typedArray;
        ContextThemeWrapper contextThemeWrapper;
        super(context, attributeSet, i);
        this.mTempRect = new Rect();
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, R.styleable.Spinner, i, 0);
        this.mBackgroundTintHelper = new AppCompatBackgroundHelper(this);
        if (theme != null) {
            contextThemeWrapper = new ContextThemeWrapper(context, theme);
        } else {
            int resourceId = obtainStyledAttributes.getResourceId(R.styleable.Spinner_popupTheme, 0);
            if (resourceId != 0) {
                contextThemeWrapper = new ContextThemeWrapper(context, resourceId);
            } else {
                this.mPopupContext = context;
                if (i2 == -1) {
                    try {
                        typedArray = context.obtainStyledAttributes(attributeSet, ATTRS_ANDROID_SPINNERMODE, i, 0);
                        try {
                            if (typedArray.hasValue(0)) {
                                i2 = typedArray.getInt(0, 0);
                            }
                        } catch (Exception e) {
                            e = e;
                            try {
                                Log.i(TAG, "Could not read android:spinnerMode", e);
                            } catch (Throwable th) {
                                th = th;
                                if (typedArray != null) {
                                }
                                throw th;
                            }
                        }
                    } catch (Exception e2) {
                        e = e2;
                        TypedArray typedArray2 = null;
                        Log.i(TAG, "Could not read android:spinnerMode", e);
                    } catch (Throwable th2) {
                        th = th2;
                        typedArray = null;
                        if (typedArray != null) {
                            typedArray.recycle();
                        }
                        throw th;
                    }
                }
                if (i2 != 0) {
                    this.mPopup = new DialogPopup();
                    this.mPopup.setPromptText(obtainStyledAttributes.getString(R.styleable.Spinner_android_prompt));
                } else if (i2 == 1) {
                    final DropdownPopup dropdownPopup = new DropdownPopup(this.mPopupContext, attributeSet, i);
                    TintTypedArray obtainStyledAttributes2 = TintTypedArray.obtainStyledAttributes(this.mPopupContext, attributeSet, R.styleable.Spinner, i, 0);
                    this.mDropDownWidth = obtainStyledAttributes2.getLayoutDimension(R.styleable.Spinner_android_dropDownWidth, -2);
                    dropdownPopup.setBackgroundDrawable(obtainStyledAttributes2.getDrawable(R.styleable.Spinner_android_popupBackground));
                    dropdownPopup.setPromptText(obtainStyledAttributes.getString(R.styleable.Spinner_android_prompt));
                    obtainStyledAttributes2.recycle();
                    this.mPopup = dropdownPopup;
                    this.mForwardingListener = new ForwardingListener(this) {
                        public ShowableListMenu getPopup() {
                            return dropdownPopup;
                        }

                        @SuppressLint({"SyntheticAccessor"})
                        public boolean onForwardingStarted() {
                            if (!AppCompatSpinner.this.getInternalPopup().isShowing()) {
                                AppCompatSpinner.this.showPopup();
                            }
                            return true;
                        }
                    };
                }
                textArray = obtainStyledAttributes.getTextArray(R.styleable.Spinner_android_entries);
                if (textArray != null) {
                    ArrayAdapter arrayAdapter = new ArrayAdapter(context, 17367048, textArray);
                    arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    setAdapter((SpinnerAdapter) arrayAdapter);
                }
                obtainStyledAttributes.recycle();
                this.mPopupSet = true;
                spinnerAdapter = this.mTempAdapter;
                if (spinnerAdapter != null) {
                    setAdapter(spinnerAdapter);
                    this.mTempAdapter = null;
                }
                this.mBackgroundTintHelper.loadFromAttributes(attributeSet, i);
            }
        }
        this.mPopupContext = contextThemeWrapper;
        if (i2 == -1) {
        }
        if (i2 != 0) {
        }
        textArray = obtainStyledAttributes.getTextArray(R.styleable.Spinner_android_entries);
        if (textArray != null) {
        }
        obtainStyledAttributes.recycle();
        this.mPopupSet = true;
        spinnerAdapter = this.mTempAdapter;
        if (spinnerAdapter != null) {
        }
        this.mBackgroundTintHelper.loadFromAttributes(attributeSet, i);
    }

    /* access modifiers changed from: 0000 */
    public int compatMeasureContentWidth(SpinnerAdapter spinnerAdapter, Drawable drawable) {
        int i = 0;
        if (spinnerAdapter == null) {
            return 0;
        }
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 0);
        int makeMeasureSpec2 = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 0);
        int max = Math.max(0, getSelectedItemPosition());
        int min = Math.min(spinnerAdapter.getCount(), max + 15);
        int i2 = 0;
        View view = null;
        for (int max2 = Math.max(0, max - (15 - (min - max))); max2 < min; max2++) {
            int itemViewType = spinnerAdapter.getItemViewType(max2);
            if (itemViewType != i) {
                view = null;
                i = itemViewType;
            }
            view = spinnerAdapter.getView(max2, view, this);
            if (view.getLayoutParams() == null) {
                view.setLayoutParams(new LayoutParams(-2, -2));
            }
            view.measure(makeMeasureSpec, makeMeasureSpec2);
            i2 = Math.max(i2, view.getMeasuredWidth());
        }
        if (drawable != null) {
            drawable.getPadding(this.mTempRect);
            Rect rect = this.mTempRect;
            i2 += rect.left + rect.right;
        }
        return i2;
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.applySupportBackgroundTint();
        }
    }

    public int getDropDownHorizontalOffset() {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            return spinnerPopup.getHorizontalOffset();
        }
        if (VERSION.SDK_INT >= 16) {
            return super.getDropDownHorizontalOffset();
        }
        return 0;
    }

    public int getDropDownVerticalOffset() {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            return spinnerPopup.getVerticalOffset();
        }
        if (VERSION.SDK_INT >= 16) {
            return super.getDropDownVerticalOffset();
        }
        return 0;
    }

    public int getDropDownWidth() {
        if (this.mPopup != null) {
            return this.mDropDownWidth;
        }
        if (VERSION.SDK_INT >= 16) {
            return super.getDropDownWidth();
        }
        return 0;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public final SpinnerPopup getInternalPopup() {
        return this.mPopup;
    }

    public Drawable getPopupBackground() {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            return spinnerPopup.getBackground();
        }
        if (VERSION.SDK_INT >= 16) {
            return super.getPopupBackground();
        }
        return null;
    }

    public Context getPopupContext() {
        return this.mPopupContext;
    }

    public CharSequence getPrompt() {
        SpinnerPopup spinnerPopup = this.mPopup;
        return spinnerPopup != null ? spinnerPopup.getHintText() : super.getPrompt();
    }

    @Nullable
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public ColorStateList getSupportBackgroundTintList() {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            return appCompatBackgroundHelper.getSupportBackgroundTintList();
        }
        return null;
    }

    @Nullable
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public Mode getSupportBackgroundTintMode() {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            return appCompatBackgroundHelper.getSupportBackgroundTintMode();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null && spinnerPopup.isShowing()) {
            this.mPopup.dismiss();
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.mPopup != null && MeasureSpec.getMode(i) == Integer.MIN_VALUE) {
            setMeasuredDimension(Math.min(Math.max(getMeasuredWidth(), compatMeasureContentWidth(getAdapter(), getBackground())), MeasureSpec.getSize(i)), getMeasuredHeight());
        }
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        if (savedState.mShowDropdown) {
            ViewTreeObserver viewTreeObserver = getViewTreeObserver();
            if (viewTreeObserver != null) {
                viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        if (!AppCompatSpinner.this.getInternalPopup().isShowing()) {
                            AppCompatSpinner.this.showPopup();
                        }
                        ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
                        if (viewTreeObserver == null) {
                            return;
                        }
                        if (VERSION.SDK_INT >= 16) {
                            viewTreeObserver.removeOnGlobalLayoutListener(this);
                        } else {
                            viewTreeObserver.removeGlobalOnLayoutListener(this);
                        }
                    }
                });
            }
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        SpinnerPopup spinnerPopup = this.mPopup;
        boolean z = spinnerPopup != null && spinnerPopup.isShowing();
        savedState.mShowDropdown = z;
        return savedState;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        ForwardingListener forwardingListener = this.mForwardingListener;
        if (forwardingListener == null || !forwardingListener.onTouch(this, motionEvent)) {
            return super.onTouchEvent(motionEvent);
        }
        return true;
    }

    public boolean performClick() {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup == null) {
            return super.performClick();
        }
        if (!spinnerPopup.isShowing()) {
            showPopup();
        }
        return true;
    }

    public void setAdapter(SpinnerAdapter spinnerAdapter) {
        if (!this.mPopupSet) {
            this.mTempAdapter = spinnerAdapter;
            return;
        }
        super.setAdapter(spinnerAdapter);
        if (this.mPopup != null) {
            Context context = this.mPopupContext;
            if (context == null) {
                context = getContext();
            }
            this.mPopup.setAdapter(new DropDownAdapter(spinnerAdapter, context.getTheme()));
        }
    }

    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.onSetBackgroundDrawable(drawable);
        }
    }

    public void setBackgroundResource(@DrawableRes int i) {
        super.setBackgroundResource(i);
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.onSetBackgroundResource(i);
        }
    }

    public void setDropDownHorizontalOffset(int i) {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            spinnerPopup.setHorizontalOffset(i);
        } else if (VERSION.SDK_INT >= 16) {
            super.setDropDownHorizontalOffset(i);
        }
    }

    public void setDropDownVerticalOffset(int i) {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            spinnerPopup.setVerticalOffset(i);
        } else if (VERSION.SDK_INT >= 16) {
            super.setDropDownVerticalOffset(i);
        }
    }

    public void setDropDownWidth(int i) {
        if (this.mPopup != null) {
            this.mDropDownWidth = i;
        } else if (VERSION.SDK_INT >= 16) {
            super.setDropDownWidth(i);
        }
    }

    public void setPopupBackgroundDrawable(Drawable drawable) {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            spinnerPopup.setBackgroundDrawable(drawable);
        } else if (VERSION.SDK_INT >= 16) {
            super.setPopupBackgroundDrawable(drawable);
        }
    }

    public void setPopupBackgroundResource(@DrawableRes int i) {
        setPopupBackgroundDrawable(AppCompatResources.getDrawable(getPopupContext(), i));
    }

    public void setPrompt(CharSequence charSequence) {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            spinnerPopup.setPromptText(charSequence);
        } else {
            super.setPrompt(charSequence);
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public void setSupportBackgroundTintList(@Nullable ColorStateList colorStateList) {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.setSupportBackgroundTintList(colorStateList);
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public void setSupportBackgroundTintMode(@Nullable Mode mode) {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.setSupportBackgroundTintMode(mode);
        }
    }

    /* access modifiers changed from: 0000 */
    public void showPopup() {
        if (VERSION.SDK_INT >= 17) {
            this.mPopup.show(getTextDirection(), getTextAlignment());
        } else {
            this.mPopup.show(-1, -1);
        }
    }
}

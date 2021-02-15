package miui.external.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SpinnerAdapter;
import android.widget.ThemedSpinnerAdapter;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import java.lang.reflect.Field;
import miui.app.AlertDialog;
import miui.app.AlertDialog.Builder;
import miui.external.adapter.SpinnerCheckableArrayAdapter;
import miui.external.adapter.SpinnerCheckableArrayAdapter.CheckedStateProvider;
import miui.external.adapter.SpinnerDoubleLineContentAdapter;
import miui.external.graphics.TaggingDrawableUtil;
import miui.util.HapticFeedbackUtil;
import miui.view.MiuiHapticFeedbackConstants;
import miui.widget.ImmersionListPopupWindow;
import miuix.compat.R;

public class Spinner extends android.widget.Spinner {
    private static Field FORWARDING_LISTENER = null;
    private static final int MAX_ITEMS_MEASURED = 15;
    private static final int MODE_DIALOG = 0;
    private static final int MODE_DROPDOWN = 1;
    private static final int MODE_THEME = -1;
    private static final String TAG = "Spinner";
    int mDropDownMinWidth;
    int mDropDownWidth;
    private OnSpinnerDismissListener mOnSpinnerDismissListener;
    /* access modifiers changed from: private */
    public SpinnerPopup mPopup;
    private final Context mPopupContext;
    private final boolean mPopupSet;
    private SpinnerAdapter mTempAdapter;
    final Rect mTempRect;
    private float mTouchX;
    private float mTouchY;

    class DialogPopup implements SpinnerPopup, OnClickListener {
        private ListAdapter mListAdapter;
        AlertDialog mPopup;
        private CharSequence mPrompt;

        private DialogPopup() {
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

        public int getHorizontalOriginalOffset() {
            return 0;
        }

        public int getVerticalOffset() {
            return 0;
        }

        public boolean isShowing() {
            AlertDialog alertDialog = this.mPopup;
            return alertDialog != null && alertDialog.isShowing();
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            Spinner.this.setSelection(i);
            if (Spinner.this.getOnItemClickListener() != null) {
                Spinner.this.performItemClick(null, i, this.mListAdapter.getItemId(i));
            }
            dismiss();
        }

        public void setAdapter(ListAdapter listAdapter) {
            this.mListAdapter = listAdapter;
        }

        public void setBackgroundDrawable(Drawable drawable) {
            Log.e(Spinner.TAG, "Cannot set popup background for MODE_DIALOG, ignoring");
        }

        public void setHorizontalOffset(int i) {
            Log.e(Spinner.TAG, "Cannot set horizontal offset for MODE_DIALOG, ignoring");
        }

        public void setHorizontalOriginalOffset(int i) {
            Log.e(Spinner.TAG, "Cannot set horizontal (original) offset for MODE_DIALOG, ignoring");
        }

        public void setPromptText(CharSequence charSequence) {
            this.mPrompt = charSequence;
        }

        public void setVerticalOffset(int i) {
            Log.e(Spinner.TAG, "Cannot set vertical offset for MODE_DIALOG, ignoring");
        }

        public void show(int i, int i2) {
            if (this.mListAdapter != null) {
                Builder builder = new Builder(Spinner.this.getPopupContext());
                CharSequence charSequence = this.mPrompt;
                if (charSequence != null) {
                    builder.setTitle(charSequence);
                }
                this.mPopup = builder.setSingleChoiceItems(this.mListAdapter, Spinner.this.getSelectedItemPosition(), (OnClickListener) this).setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialogInterface) {
                        Spinner.this.notifySpinnerDismiss();
                    }
                }).create();
                ListView listView = this.mPopup.getListView();
                listView.setTextDirection(i);
                listView.setTextAlignment(i2);
                this.mPopup.show();
            }
        }

        public void show(int i, int i2, float f, float f2) {
            show(i, i2);
        }
    }

    class DialogPopupAdapter extends DropDownAdapter {
        DialogPopupAdapter(@Nullable SpinnerAdapter spinnerAdapter, @Nullable Theme theme) {
            super(spinnerAdapter, theme);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            View view2 = super.getView(i, view, viewGroup);
            TaggingDrawableUtil.updateBackgroundState(view2, TaggingDrawableUtil.STATE_SET_MIDDLE);
            return view2;
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
            } else if (spinnerAdapter instanceof ThemedAdapter) {
                ThemedAdapter themedAdapter = (ThemedAdapter) spinnerAdapter;
                if (themedAdapter.getDropDownViewTheme() == null) {
                    themedAdapter.setDropDownViewTheme(theme);
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

    class DropDownPopupAdapter extends DropDownAdapter {
        DropDownPopupAdapter(@Nullable SpinnerAdapter spinnerAdapter, @Nullable Theme theme) {
            super(spinnerAdapter, theme);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            View view2 = super.getView(i, view, viewGroup);
            TaggingDrawableUtil.updateItemPadding(view2, i, getCount());
            return view2;
        }
    }

    class DropdownPopup extends ImmersionListPopupWindow implements SpinnerPopup {
        private static final float SCREEN_MARGIN_BOTTOM_PROPORTION = 0.1f;
        private static final float SCREEN_MARGIN_TOP_PROPORTION = 0.1f;
        ListAdapter mAdapter;
        private CharSequence mHintText;
        private int mMarginScreen = 40;
        private int mOriginalHorizontalOffset;
        private final Rect mVisibleRect = new Rect();

        public DropdownPopup(Context context, AttributeSet attributeSet, int i) {
            super(context);
            setOnItemClickListener(new OnItemClickListener(Spinner.this) {
                public void onItemClick(AdapterView adapterView, View view, int i, long j) {
                    Spinner.this.setSelection(i);
                    Spinner.this.vibrate(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_MESH_NORMAL);
                    if (Spinner.this.getOnItemClickListener() != null) {
                        DropdownPopup dropdownPopup = DropdownPopup.this;
                        Spinner.this.performItemClick(view, i, dropdownPopup.mAdapter.getItemId(i));
                    }
                    DropdownPopup.this.dismiss();
                }
            });
        }

        private void changeWindowBackground(View view, float f) {
            if (view != null) {
                LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                if (layoutParams != null) {
                    layoutParams.flags |= 2;
                    layoutParams.dimAmount = f;
                    ((WindowManager) view.getContext().getSystemService("window")).updateViewLayout(view, layoutParams);
                }
            } else {
                Log.w(Spinner.TAG, "can't change window dim with null view");
            }
        }

        private int getListViewHeight() {
            if (getContentView() instanceof ListView) {
                ListAdapter adapter = ((ListView) getContentView()).getAdapter();
                int i = 0;
                for (int i2 = 0; i2 < adapter.getCount(); i2++) {
                    View view = adapter.getView(i2, null, (ListView) getContentView());
                    view.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
                    i += view.getMeasuredHeight();
                }
                return i;
            }
            getContentView().measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
            return getContentView().getMeasuredHeight() + 0;
        }

        private void initListView(int i, int i2) {
            if (getContentView() instanceof ListView) {
                ListView listView = (ListView) getContentView();
                listView.setChoiceMode(1);
                listView.setTextDirection(i);
                listView.setTextAlignment(i2);
                int selectedItemPosition = Spinner.this.getSelectedItemPosition();
                listView.setSelection(selectedItemPosition);
                listView.setItemChecked(selectedItemPosition, true);
            }
        }

        private void showWithAnchor(View view, float f, float f2) {
            int[] iArr = new int[2];
            view.getLocationInWindow(iArr);
            int i = (int) f;
            boolean z = true;
            int i2 = iArr[1];
            View rootView = view.getRootView();
            rootView.getLocationInWindow(iArr);
            if (i > rootView.getWidth() / 2) {
                z = false;
            }
            int width = z ? this.mMarginScreen : (rootView.getWidth() - this.mMarginScreen) - getWidth();
            int listViewHeight = getListViewHeight();
            float f3 = (float) i2;
            if (f3 < ((float) rootView.getHeight()) * 0.1f) {
                f3 = ((float) rootView.getHeight()) * 0.1f;
            }
            float f4 = (float) listViewHeight;
            if (f3 + f4 > ((float) rootView.getHeight()) * 0.9f) {
                f3 = (((float) rootView.getHeight()) * 0.9f) - f4;
            }
            if (f3 < ((float) rootView.getHeight()) * 0.1f) {
                f3 = ((float) rootView.getHeight()) * 0.1f;
                setHeight((int) (((float) rootView.getHeight()) * 0.79999995f));
            }
            showAtLocation(view, 0, width, (int) f3);
        }

        public CharSequence getHintText() {
            return this.mHintText;
        }

        public int getHorizontalOffset() {
            return 0;
        }

        public int getHorizontalOriginalOffset() {
            return this.mOriginalHorizontalOffset;
        }

        public int getVerticalOffset() {
            return 0;
        }

        /* access modifiers changed from: 0000 */
        public boolean isVisibleToUser(View view) {
            return ViewCompat.isAttachedToWindow(view) && view.getGlobalVisibleRect(this.mVisibleRect);
        }

        public void setAdapter(ListAdapter listAdapter) {
            super.setAdapter(listAdapter);
            this.mAdapter = listAdapter;
        }

        public void setHorizontalOffset(int i) {
            Log.w(Spinner.TAG, "setHorizontalOffset do nothing");
        }

        public void setHorizontalOriginalOffset(int i) {
            this.mOriginalHorizontalOffset = i;
        }

        public void setPromptText(CharSequence charSequence) {
            this.mHintText = charSequence;
        }

        public void setVerticalOffset(int i) {
            Log.w(Spinner.TAG, "setVerticalOffset do nothing");
        }

        public void show(int i, int i2) {
            show(i, i2, 0.0f, 0.0f);
        }

        public void show(int i, int i2, float f, float f2) {
            boolean isShowing = isShowing();
            setInputMethodMode(2);
            show((View) Spinner.this, (ViewGroup) null, f, f2);
            initListView(i, i2);
            if (!isShowing) {
                setOnDismissListener(new PopupWindow.OnDismissListener() {
                    public void onDismiss() {
                        Spinner.this.notifySpinnerDismiss();
                    }
                });
            }
        }

        public void show(View view, ViewGroup viewGroup, float f, float f2) {
            if (prepareShow(view, viewGroup)) {
                showWithAnchor(view, f, f2);
                changeWindowBackground(getContentView().getRootView(), 0.3f);
            }
        }
    }

    public interface OnSpinnerDismissListener {
        void onSpinnerDismiss();
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

    class SpinnerCheckedProvider implements CheckedStateProvider {
        private Spinner mSpinner;

        public SpinnerCheckedProvider(Spinner spinner) {
            this.mSpinner = spinner;
        }

        public boolean isChecked(int i) {
            return this.mSpinner.getSelectedItemPosition() == i;
        }
    }

    interface SpinnerPopup {
        void dismiss();

        Drawable getBackground();

        CharSequence getHintText();

        int getHorizontalOffset();

        int getHorizontalOriginalOffset();

        int getVerticalOffset();

        boolean isShowing();

        void setAdapter(ListAdapter listAdapter);

        void setBackgroundDrawable(Drawable drawable);

        void setHorizontalOffset(int i);

        void setHorizontalOriginalOffset(int i);

        void setPromptText(CharSequence charSequence);

        void setVerticalOffset(int i);

        void show(int i, int i2);

        void show(int i, int i2, float f, float f2);
    }

    public interface ThemedAdapter extends SpinnerAdapter {
        @Nullable
        Theme getDropDownViewTheme();

        void setDropDownViewTheme(@Nullable Theme theme);
    }

    static {
        try {
            FORWARDING_LISTENER = android.widget.Spinner.class.getDeclaredField("mForwardingListener");
            FORWARDING_LISTENER.setAccessible(true);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "static initializer: ", e);
        }
    }

    public Spinner(Context context) {
        this(context, (AttributeSet) null);
    }

    public Spinner(Context context, int i) {
        this(context, null, R.attr.miuiSpinnerStyle, i);
    }

    public Spinner(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.miuiSpinnerStyle);
    }

    public Spinner(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, -1);
    }

    public Spinner(Context context, AttributeSet attributeSet, int i, int i2) {
        this(context, attributeSet, i, i2, null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0035  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x00a2  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00bd  */
    /* JADX WARNING: Removed duplicated region for block: B:29:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Spinner(Context context, AttributeSet attributeSet, int i, int i2, Theme theme) {
        CharSequence[] textArray;
        SpinnerAdapter spinnerAdapter;
        ContextThemeWrapper contextThemeWrapper;
        super(context, attributeSet, i);
        this.mTempRect = new Rect();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.Spinner, i, 0);
        if (theme == null || VERSION.SDK_INT < 23) {
            int resourceId = obtainStyledAttributes.getResourceId(R.styleable.Spinner_popupTheme, 0);
            if (resourceId != 0) {
                contextThemeWrapper = new ContextThemeWrapper(context, resourceId);
            } else {
                this.mPopupContext = context;
                if (i2 == -1) {
                    i2 = obtainStyledAttributes.getInt(R.styleable.Spinner_spinnerModeCompat, 1);
                }
                if (i2 != 0) {
                    this.mPopup = new DialogPopup();
                    this.mPopup.setPromptText(obtainStyledAttributes.getString(R.styleable.Spinner_android_prompt));
                } else if (i2 == 1) {
                    DropdownPopup dropdownPopup = new DropdownPopup(this.mPopupContext, attributeSet, i);
                    TypedArray obtainStyledAttributes2 = this.mPopupContext.obtainStyledAttributes(attributeSet, R.styleable.Spinner, i, 0);
                    this.mDropDownWidth = obtainStyledAttributes2.getLayoutDimension(R.styleable.Spinner_android_dropDownWidth, -2);
                    this.mDropDownMinWidth = obtainStyledAttributes2.getLayoutDimension(R.styleable.Spinner_dropDownMinWidth, -2);
                    int resourceId2 = obtainStyledAttributes2.getResourceId(R.styleable.Spinner_android_popupBackground, 0);
                    if (resourceId2 != 0) {
                        setPopupBackgroundResource(resourceId2);
                    } else {
                        dropdownPopup.setBackgroundDrawable(obtainStyledAttributes2.getDrawable(R.styleable.Spinner_android_popupBackground));
                    }
                    dropdownPopup.setPromptText(obtainStyledAttributes.getString(R.styleable.Spinner_android_prompt));
                    obtainStyledAttributes2.recycle();
                    this.mPopup = dropdownPopup;
                    makeSupperForwardingListenerInvalid();
                }
                textArray = obtainStyledAttributes.getTextArray(R.styleable.Spinner_android_entries);
                if (textArray != null) {
                    ArrayAdapter arrayAdapter = new ArrayAdapter(context, R.layout.miuix_compat_simple_spinner_layout, 16908308, textArray);
                    arrayAdapter.setDropDownViewResource(R.layout.miuix_compat_simple_spinner_dropdown_item);
                    setAdapter((SpinnerAdapter) arrayAdapter);
                }
                obtainStyledAttributes.recycle();
                this.mPopupSet = true;
                spinnerAdapter = this.mTempAdapter;
                if (spinnerAdapter == null) {
                    setAdapter(spinnerAdapter);
                    this.mTempAdapter = null;
                    return;
                }
                return;
            }
        } else {
            contextThemeWrapper = new ContextThemeWrapper(context, theme);
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
        if (spinnerAdapter == null) {
        }
    }

    private void clearCachedSize() {
        SpinnerPopup spinnerPopup = this.mPopup;
        if ((spinnerPopup instanceof DropdownPopup) && ((DropdownPopup) spinnerPopup).getHeight() > 0) {
            ((DropdownPopup) this.mPopup).setHeight(-2);
            ((DropdownPopup) this.mPopup).setWidth(-2);
        }
    }

    private int compatMeasureSelectItemWidth(SpinnerAdapter spinnerAdapter, Drawable drawable) {
        if (spinnerAdapter == null || spinnerAdapter.getCount() == 0) {
            return 0;
        }
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 0);
        int makeMeasureSpec2 = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 0);
        View view = spinnerAdapter.getView(Math.max(0, getSelectedItemPosition()), null, this);
        if (view.getLayoutParams() == null) {
            view.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        }
        view.measure(makeMeasureSpec, makeMeasureSpec2);
        int max = Math.max(0, view.getMeasuredWidth());
        if (drawable != null) {
            drawable.getPadding(this.mTempRect);
            Rect rect = this.mTempRect;
            max += rect.left + rect.right;
        }
        return max;
    }

    private void makeSupperForwardingListenerInvalid() {
        Field field = FORWARDING_LISTENER;
        if (field != null) {
            try {
                field.set(this, null);
            } catch (IllegalAccessException e) {
                Log.e(TAG, "makeSupperForwardingListenerInvalid: ", e);
            }
        }
    }

    /* access modifiers changed from: private */
    public void notifySpinnerDismiss() {
        OnSpinnerDismissListener onSpinnerDismissListener = this.mOnSpinnerDismissListener;
        if (onSpinnerDismissListener != null) {
            onSpinnerDismissListener.onSpinnerDismiss();
        }
    }

    private boolean superViewPerformClick() {
        sendAccessibilityEvent(1);
        return false;
    }

    /* access modifiers changed from: private */
    public void vibrate(int i) {
        if (HapticFeedbackUtil.isSupportLinearMotorVibrate(i)) {
            performHapticFeedback(i);
        }
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
                view.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
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

    public int getDropDownHorizontalOffset() {
        SpinnerPopup spinnerPopup = this.mPopup;
        return spinnerPopup != null ? spinnerPopup.getHorizontalOffset() : super.getDropDownHorizontalOffset();
    }

    public int getDropDownVerticalOffset() {
        SpinnerPopup spinnerPopup = this.mPopup;
        return spinnerPopup != null ? spinnerPopup.getVerticalOffset() : super.getDropDownVerticalOffset();
    }

    public int getDropDownWidth() {
        return this.mPopup != null ? this.mDropDownWidth : super.getDropDownWidth();
    }

    public Drawable getPopupBackground() {
        SpinnerPopup spinnerPopup = this.mPopup;
        return spinnerPopup != null ? spinnerPopup.getBackground() : super.getPopupBackground();
    }

    public Context getPopupContext() {
        return this.mPopupContext;
    }

    public CharSequence getPrompt() {
        SpinnerPopup spinnerPopup = this.mPopup;
        return spinnerPopup != null ? spinnerPopup.getHintText() : super.getPrompt();
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
            setMeasuredDimension(Math.min(Math.min(getMeasuredWidth(), compatMeasureSelectItemWidth(getAdapter(), getBackground())), MeasureSpec.getSize(i)), getMeasuredHeight());
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
                        if (!Spinner.this.mPopup.isShowing()) {
                            Spinner.this.showPopup();
                        }
                        ViewTreeObserver viewTreeObserver = Spinner.this.getViewTreeObserver();
                        if (viewTreeObserver != null) {
                            viewTreeObserver.removeOnGlobalLayoutListener(this);
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
        if (motionEvent.getAction() == 1) {
            this.mTouchX = motionEvent.getX();
            this.mTouchY = motionEvent.getY();
        }
        return super.onTouchEvent(motionEvent);
    }

    public boolean performClick() {
        int[] iArr = new int[2];
        getLocationInWindow(iArr);
        return performClick((float) iArr[0], (float) iArr[1]);
    }

    public boolean performClick(float f, float f2) {
        vibrate(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_POPUP_LIGHT);
        this.mTouchX = f;
        this.mTouchY = f2;
        if (superViewPerformClick()) {
            return true;
        }
        if (this.mPopup == null) {
            return super.performClick();
        }
        clearCachedSize();
        if (!this.mPopup.isShowing()) {
            showPopup(this.mTouchX, this.mTouchY);
        }
        return true;
    }

    public void setAdapter(SpinnerAdapter spinnerAdapter) {
        ListAdapter dropDownPopupAdapter;
        if (!this.mPopupSet) {
            this.mTempAdapter = spinnerAdapter;
            return;
        }
        super.setAdapter(spinnerAdapter);
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup instanceof DialogPopup) {
            dropDownPopupAdapter = new DialogPopupAdapter(spinnerAdapter, getPopupContext().getTheme());
        } else {
            if (spinnerPopup instanceof DropdownPopup) {
                dropDownPopupAdapter = new DropDownPopupAdapter(spinnerAdapter, getPopupContext().getTheme());
            }
        }
        spinnerPopup.setAdapter(dropDownPopupAdapter);
    }

    public void setDoubleLineContentAdapter(SpinnerDoubleLineContentAdapter spinnerDoubleLineContentAdapter) {
        setAdapter((SpinnerAdapter) new SpinnerCheckableArrayAdapter(getContext(), R.layout.miuix_compat_simple_spinner_layout, spinnerDoubleLineContentAdapter, new SpinnerCheckedProvider(this)));
    }

    public void setDropDownHorizontalOffset(int i) {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            spinnerPopup.setHorizontalOriginalOffset(i);
            this.mPopup.setHorizontalOffset(i);
            return;
        }
        super.setDropDownHorizontalOffset(i);
    }

    public void setDropDownVerticalOffset(int i) {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            spinnerPopup.setVerticalOffset(i);
        } else {
            super.setDropDownVerticalOffset(i);
        }
    }

    public void setDropDownWidth(int i) {
        if (this.mPopup != null) {
            this.mDropDownWidth = i;
        } else {
            super.setDropDownWidth(i);
        }
    }

    public void setOnSpinnerDismissListener(OnSpinnerDismissListener onSpinnerDismissListener) {
        this.mOnSpinnerDismissListener = onSpinnerDismissListener;
    }

    public void setPopupBackgroundDrawable(Drawable drawable) {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            spinnerPopup.setBackgroundDrawable(drawable);
        } else {
            super.setPopupBackgroundDrawable(drawable);
        }
    }

    public void setPopupBackgroundResource(@DrawableRes int i) {
        setPopupBackgroundDrawable(VERSION.SDK_INT >= 21 ? getPopupContext().getDrawable(i) : getPopupContext().getResources().getDrawable(i));
    }

    public void setPrompt(CharSequence charSequence) {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            spinnerPopup.setPromptText(charSequence);
        } else {
            super.setPrompt(charSequence);
        }
    }

    /* access modifiers changed from: 0000 */
    public void showPopup() {
        this.mPopup.show(getTextDirection(), getTextAlignment());
    }

    /* access modifiers changed from: 0000 */
    public void showPopup(float f, float f2) {
        this.mPopup.show(getTextDirection(), getTextAlignment(), f, f2);
    }
}

package miui.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.DialogInterface.OnShowListener;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.miui.internal.variable.AlertControllerWrapper;
import com.miui.internal.variable.AlertControllerWrapper.AlertParams;
import com.miui.internal.variable.AlertControllerWrapper.AlertParams.ActionItem;
import java.util.ArrayList;
import miui.R;
import miui.util.AttributeResolver;
import miui.util.HapticFeedbackUtil;
import miui.view.MiuiHapticFeedbackConstants;

public class AlertDialog extends Dialog implements DialogInterface {
    public static final int THEME_DARK = 2;
    public static final int THEME_DARK_EDIT = 4;
    public static final int THEME_DARK_EDIT_DEFAULT = 6;
    public static final int THEME_DAYNIGHT = 8;
    public static final int THEME_DAYNIGHT_EDIT = 9;
    public static final int THEME_DAYNIGHT_EDIT_DEFAULT = 10;
    public static final int THEME_LIGHT = 3;
    public static final int THEME_LIGHT_EDIT = 5;
    public static final int THEME_LIGHT_EDIT_DEFAULT = 7;
    /* access modifiers changed from: private */
    public AlertControllerWrapper mAlert;

    public class Builder {
        private AlertParams P;
        private int mTheme;

        public Builder(Context context) {
            this(context, AlertDialog.resolveDialogTheme(context, 0));
        }

        public Builder(Context context, int i) {
            this.P = new AlertParams(new ContextThemeWrapper(context, AlertDialog.resolveDialogTheme(context, i)));
            AlertParams alertParams = this.P;
            boolean z = i >= 4 && i <= 7;
            alertParams.mEditMode = z;
            this.mTheme = i;
        }

        public Builder addActionItem(int i, int i2, int i3) {
            return addActionItem(this.P.mContext.getText(i), i2, i3);
        }

        public Builder addActionItem(CharSequence charSequence, int i, int i2) {
            AlertParams alertParams = this.P;
            if (alertParams.mActionItems == null) {
                alertParams.mActionItems = new ArrayList();
            }
            this.P.mActionItems.add(new ActionItem(charSequence, i, i2));
            return this;
        }

        public AlertDialog create() {
            AlertDialog alertDialog = new AlertDialog(this.P.mContext, this.mTheme);
            this.P.apply(alertDialog.mAlert);
            alertDialog.setCancelable(this.P.mCancelable);
            if (this.P.mCancelable) {
                alertDialog.setCanceledOnTouchOutside(true);
            }
            alertDialog.setOnCancelListener(this.P.mOnCancelListener);
            alertDialog.setOnDismissListener(this.P.mOnDismissListener);
            alertDialog.setOnShowListener(this.P.mOnShowListener);
            OnKeyListener onKeyListener = this.P.mOnKeyListener;
            if (onKeyListener != null) {
                alertDialog.setOnKeyListener(onKeyListener);
            }
            return alertDialog;
        }

        public Context getContext() {
            return this.P.mContext;
        }

        public Builder setAdapter(ListAdapter listAdapter, OnClickListener onClickListener) {
            AlertParams alertParams = this.P;
            alertParams.mAdapter = listAdapter;
            alertParams.mOnClickListener = onClickListener;
            return this;
        }

        public Builder setCancelable(boolean z) {
            this.P.mCancelable = z;
            return this;
        }

        public Builder setCheckBox(boolean z, CharSequence charSequence) {
            AlertParams alertParams = this.P;
            alertParams.mIsChecked = z;
            alertParams.mCheckBoxMessage = charSequence;
            return this;
        }

        public Builder setCursor(Cursor cursor, OnClickListener onClickListener, String str) {
            AlertParams alertParams = this.P;
            alertParams.mCursor = cursor;
            alertParams.mLabelColumn = str;
            alertParams.mOnClickListener = onClickListener;
            return this;
        }

        public Builder setCustomTitle(View view) {
            this.P.mCustomTitleView = view;
            return this;
        }

        public Builder setHapticFeedbackEnabled(boolean z) {
            this.P.mHapticFeedbackEnabled = z;
            return this;
        }

        public Builder setIcon(int i) {
            this.P.mIconId = i;
            return this;
        }

        public Builder setIcon(Drawable drawable) {
            this.P.mIcon = drawable;
            return this;
        }

        public Builder setIconAttribute(int i) {
            AlertParams alertParams = this.P;
            alertParams.mIconId = AttributeResolver.resolve(alertParams.mContext, i);
            return this;
        }

        public Builder setItems(int i, OnClickListener onClickListener) {
            AlertParams alertParams = this.P;
            alertParams.mItems = alertParams.mContext.getResources().getTextArray(i);
            this.P.mOnClickListener = onClickListener;
            return this;
        }

        public Builder setItems(CharSequence[] charSequenceArr, OnClickListener onClickListener) {
            AlertParams alertParams = this.P;
            alertParams.mItems = charSequenceArr;
            alertParams.mOnClickListener = onClickListener;
            return this;
        }

        public Builder setMessage(int i) {
            AlertParams alertParams = this.P;
            alertParams.mMessage = alertParams.mContext.getText(i);
            return this;
        }

        public Builder setMessage(CharSequence charSequence) {
            this.P.mMessage = charSequence;
            return this;
        }

        public Builder setMultiChoiceAdapter(ListAdapter listAdapter, OnMultiChoiceClickListener onMultiChoiceClickListener) {
            AlertParams alertParams = this.P;
            alertParams.mAdapter = listAdapter;
            alertParams.mIsMultiChoice = true;
            alertParams.mOnCheckboxClickListener = onMultiChoiceClickListener;
            return this;
        }

        public Builder setMultiChoiceItems(int i, boolean[] zArr, OnMultiChoiceClickListener onMultiChoiceClickListener) {
            AlertParams alertParams = this.P;
            alertParams.mItems = alertParams.mContext.getResources().getTextArray(i);
            AlertParams alertParams2 = this.P;
            alertParams2.mOnCheckboxClickListener = onMultiChoiceClickListener;
            alertParams2.mCheckedItems = zArr;
            alertParams2.mIsMultiChoice = true;
            return this;
        }

        public Builder setMultiChoiceItems(Cursor cursor, String str, String str2, OnMultiChoiceClickListener onMultiChoiceClickListener) {
            AlertParams alertParams = this.P;
            alertParams.mCursor = cursor;
            alertParams.mOnCheckboxClickListener = onMultiChoiceClickListener;
            alertParams.mIsCheckedColumn = str;
            alertParams.mLabelColumn = str2;
            alertParams.mIsMultiChoice = true;
            return this;
        }

        public Builder setMultiChoiceItems(CharSequence[] charSequenceArr, boolean[] zArr, OnMultiChoiceClickListener onMultiChoiceClickListener) {
            AlertParams alertParams = this.P;
            alertParams.mItems = charSequenceArr;
            alertParams.mOnCheckboxClickListener = onMultiChoiceClickListener;
            alertParams.mCheckedItems = zArr;
            alertParams.mIsMultiChoice = true;
            return this;
        }

        public Builder setNegativeButton(int i, OnClickListener onClickListener) {
            AlertParams alertParams = this.P;
            alertParams.mNegativeButtonText = alertParams.mContext.getText(i);
            this.P.mNegativeButtonListener = onClickListener;
            return this;
        }

        public Builder setNegativeButton(CharSequence charSequence, OnClickListener onClickListener) {
            AlertParams alertParams = this.P;
            alertParams.mNegativeButtonText = charSequence;
            alertParams.mNegativeButtonListener = onClickListener;
            return this;
        }

        public Builder setNeutralButton(int i, OnClickListener onClickListener) {
            AlertParams alertParams = this.P;
            alertParams.mNeutralButtonText = alertParams.mContext.getText(i);
            this.P.mNeutralButtonListener = onClickListener;
            return this;
        }

        public Builder setNeutralButton(CharSequence charSequence, OnClickListener onClickListener) {
            AlertParams alertParams = this.P;
            alertParams.mNeutralButtonText = charSequence;
            alertParams.mNeutralButtonListener = onClickListener;
            return this;
        }

        public Builder setOnActionItemClickListener(OnClickListener onClickListener) {
            this.P.mOnActionItemClickListener = onClickListener;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            this.P.mOnCancelListener = onCancelListener;
            return this;
        }

        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            this.P.mOnDismissListener = onDismissListener;
            return this;
        }

        public Builder setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
            this.P.mOnItemSelectedListener = onItemSelectedListener;
            return this;
        }

        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            this.P.mOnKeyListener = onKeyListener;
            return this;
        }

        public Builder setOnShowListener(OnShowListener onShowListener) {
            this.P.mOnShowListener = onShowListener;
            return this;
        }

        public Builder setPositiveButton(int i, OnClickListener onClickListener) {
            AlertParams alertParams = this.P;
            alertParams.mPositiveButtonText = alertParams.mContext.getText(i);
            this.P.mPositiveButtonListener = onClickListener;
            return this;
        }

        public Builder setPositiveButton(CharSequence charSequence, OnClickListener onClickListener) {
            AlertParams alertParams = this.P;
            alertParams.mPositiveButtonText = charSequence;
            alertParams.mPositiveButtonListener = onClickListener;
            return this;
        }

        public Builder setSingleChoiceItems(int i, int i2, OnClickListener onClickListener) {
            AlertParams alertParams = this.P;
            alertParams.mItems = alertParams.mContext.getResources().getTextArray(i);
            AlertParams alertParams2 = this.P;
            alertParams2.mOnClickListener = onClickListener;
            alertParams2.mCheckedItem = i2;
            alertParams2.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(Cursor cursor, int i, String str, OnClickListener onClickListener) {
            AlertParams alertParams = this.P;
            alertParams.mCursor = cursor;
            alertParams.mOnClickListener = onClickListener;
            alertParams.mCheckedItem = i;
            alertParams.mLabelColumn = str;
            alertParams.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(ListAdapter listAdapter, int i, OnClickListener onClickListener) {
            AlertParams alertParams = this.P;
            alertParams.mAdapter = listAdapter;
            alertParams.mOnClickListener = onClickListener;
            alertParams.mCheckedItem = i;
            alertParams.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(CharSequence[] charSequenceArr, int i, OnClickListener onClickListener) {
            AlertParams alertParams = this.P;
            alertParams.mItems = charSequenceArr;
            alertParams.mOnClickListener = onClickListener;
            alertParams.mCheckedItem = i;
            alertParams.mIsSingleChoice = true;
            return this;
        }

        public Builder setTitle(int i) {
            AlertParams alertParams = this.P;
            alertParams.mTitle = alertParams.mContext.getText(i);
            return this;
        }

        public Builder setTitle(CharSequence charSequence) {
            this.P.mTitle = charSequence;
            return this;
        }

        public Builder setView(int i) {
            AlertParams alertParams = this.P;
            alertParams.mView = alertParams.mInflater.inflate(i, null);
            return this;
        }

        public Builder setView(View view) {
            this.P.mView = view;
            return this;
        }

        public AlertDialog show() {
            AlertDialog create = create();
            create.show();
            return create;
        }
    }

    protected AlertDialog(Context context) {
        this(context, resolveDialogTheme(context, 0));
    }

    protected AlertDialog(Context context, int i) {
        super(context, resolveDialogTheme(context, i));
        this.mAlert = new AlertControllerWrapper(context, this, getWindow());
    }

    static int resolveDialogTheme(Context context, int i) {
        switch (i) {
            case 2:
                return R.style.Theme_Dark_Dialog_Alert;
            case 3:
                return R.style.Theme_Light_Dialog_Alert;
            case 4:
                return com.miui.internal.R.style.Theme_Dark_Dialog_Edit;
            case 5:
                return com.miui.internal.R.style.Theme_Light_Dialog_Edit;
            case 6:
                return com.miui.internal.R.style.Theme_Dark_Dialog_Edit_Default;
            case 7:
                return com.miui.internal.R.style.Theme_Light_Dialog_Edit_Default;
            case 8:
                return R.style.Theme_DayNight_Dialog_Alert;
            case 9:
                return com.miui.internal.R.style.Theme_DayNight_Dialog_Edit;
            case 10:
                return com.miui.internal.R.style.Theme_DayNight_Dialog_Edit_Default;
            default:
                if (i >= 16777216) {
                    return i;
                }
                TypedValue typedValue = new TypedValue();
                context.getTheme().resolveAttribute(16843529, typedValue, true);
                return typedValue.resourceId;
        }
    }

    public Button getButton(int i) {
        return this.mAlert.getButton(i);
    }

    public boolean[] getCheckedItems() {
        return this.mAlert.getCheckedItems();
    }

    public ListView getListView() {
        return this.mAlert.getListView();
    }

    public TextView getMessageView() {
        return this.mAlert.getMessageView();
    }

    public ActionBar getMiuiActionBar() {
        return this.mAlert.getImpl().getActionBar();
    }

    public boolean isChecked() {
        return this.mAlert.isChecked();
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        View decorView = getWindow().getDecorView();
        if (decorView != null && this.mAlert.mHapticFeedbackEnabled && HapticFeedbackUtil.isSupportLinearMotorVibrate(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_POPUP_NORMAL)) {
            decorView.performHapticFeedback(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_POPUP_NORMAL);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mAlert.installContent();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return this.mAlert.onKeyDown(i, keyEvent) || super.onKeyDown(i, keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        return this.mAlert.onKeyUp(i, keyEvent) || super.onKeyUp(i, keyEvent);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        this.mAlert.onStart();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        this.mAlert.onStop();
    }

    public void setButton(int i, CharSequence charSequence, OnClickListener onClickListener) {
        this.mAlert.setButton(i, charSequence, onClickListener, null);
    }

    public void setButton(int i, CharSequence charSequence, Message message) {
        this.mAlert.setButton(i, charSequence, null, message);
    }

    public void setCustomTitle(View view) {
        this.mAlert.setCustomTitle(view);
    }

    public void setHapticFeedbackEnabled(boolean z) {
        this.mAlert.mHapticFeedbackEnabled = z;
    }

    public void setIcon(int i) {
        this.mAlert.setIcon(i);
    }

    public void setIcon(Drawable drawable) {
        this.mAlert.setIcon(drawable);
    }

    public void setIconAttribute(int i) {
        this.mAlert.setIcon(AttributeResolver.resolve(getContext(), i));
    }

    public void setMessage(CharSequence charSequence) {
        this.mAlert.setMessage(charSequence);
    }

    public void setTitle(CharSequence charSequence) {
        super.setTitle(charSequence);
        this.mAlert.setTitle(charSequence);
    }

    public void setView(View view) {
        this.mAlert.setView(view);
    }
}

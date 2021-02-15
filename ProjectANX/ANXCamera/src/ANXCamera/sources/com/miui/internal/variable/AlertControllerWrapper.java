package com.miui.internal.variable;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.android.internal.app.AlertController;
import com.miui.internal.app.AlertControllerImpl;
import java.util.ArrayList;

public class AlertControllerWrapper extends AlertController {
    AlertControllerImpl mAlertControllerImpl;
    public boolean mHapticFeedbackEnabled;

    public class AlertParams extends com.android.internal.app.AlertController.AlertParams {
        public ArrayList mActionItems;
        public CharSequence mCheckBoxMessage;
        public boolean mEditMode;
        public boolean mHapticFeedbackEnabled;
        public boolean mIsChecked;
        public OnClickListener mOnActionItemClickListener;
        public OnDismissListener mOnDismissListener;
        public OnShowListener mOnShowListener;

        public class ActionItem {
            public int icon;
            public int id;
            public CharSequence label;

            public ActionItem(CharSequence charSequence, int i, int i2) {
                this.label = charSequence;
                this.icon = i;
                this.id = i2;
            }
        }

        public AlertParams(Context context) {
            super(context);
        }

        private ListAdapter createListAdapter(int i, AlertControllerImpl alertControllerImpl) {
            Cursor cursor = this.mCursor;
            if (cursor == null) {
                ListAdapter listAdapter = this.mAdapter;
                return listAdapter != null ? listAdapter : new ArrayAdapter(this.mContext, i, 16908308, this.mItems);
            }
            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this.mContext, i, cursor, new String[]{this.mLabelColumn}, new int[]{16908308});
            return simpleCursorAdapter;
        }

        /* JADX WARNING: Removed duplicated region for block: B:20:0x005c  */
        /* JADX WARNING: Removed duplicated region for block: B:23:0x0063  */
        /* JADX WARNING: Removed duplicated region for block: B:25:0x0068  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void createListView(AlertController alertController) {
            ListAdapter listAdapter;
            OnItemSelectedListener onItemSelectedListener;
            int i;
            OnItemClickListener r1;
            final AlertControllerImpl impl = ((AlertControllerWrapper) alertController).getImpl();
            final ListView listView = (ListView) this.mInflater.inflate(impl.getListLayout(), null);
            if (listView != null) {
                if (this.mIsMultiChoice) {
                    listAdapter = createMultiChoiceListAdapter(listView, impl.getMultiChoiceItemLayout(), impl);
                } else {
                    listAdapter = createListAdapter(this.mIsSingleChoice ? impl.getSingleChoiceItemLayout() : impl.getListItemLayout(), impl);
                }
                impl.setAdapter(listAdapter);
                impl.setCheckedItem(this.mCheckedItem);
                impl.setCheckedItems(this.mCheckedItems);
                if (this.mOnClickListener != null) {
                    r1 = new OnItemClickListener() {
                        public void onItemClick(AdapterView adapterView, View view, int i, long j) {
                            AlertParams.this.mOnClickListener.onClick(impl.getDialogInterface(), i);
                            if (!AlertParams.this.mIsSingleChoice) {
                                impl.getDialogInterface().dismiss();
                            }
                        }
                    };
                } else {
                    if (this.mOnCheckboxClickListener != null) {
                        r1 = new OnItemClickListener() {
                            public void onItemClick(AdapterView adapterView, View view, int i, long j) {
                                boolean[] zArr = AlertParams.this.mCheckedItems;
                                if (zArr != null) {
                                    zArr[i] = listView.isItemChecked(i);
                                }
                                AlertParams.this.mOnCheckboxClickListener.onClick(impl.getDialogInterface(), i, listView.isItemChecked(i));
                            }
                        };
                    }
                    onItemSelectedListener = this.mOnItemSelectedListener;
                    if (onItemSelectedListener != null) {
                        listView.setOnItemSelectedListener(onItemSelectedListener);
                    }
                    if (!this.mIsSingleChoice) {
                        i = 1;
                    } else {
                        if (this.mIsMultiChoice) {
                            i = 2;
                        }
                        impl.setListView(listView);
                    }
                    listView.setChoiceMode(i);
                    impl.setListView(listView);
                }
                listView.setOnItemClickListener(r1);
                onItemSelectedListener = this.mOnItemSelectedListener;
                if (onItemSelectedListener != null) {
                }
                if (!this.mIsSingleChoice) {
                }
                listView.setChoiceMode(i);
                impl.setListView(listView);
            }
        }

        private ListAdapter createMultiChoiceListAdapter(ListView listView, int i, AlertControllerImpl alertControllerImpl) {
            Cursor cursor = this.mCursor;
            if (cursor == null) {
                if (this.mEditMode) {
                    ListAdapter listAdapter = this.mAdapter;
                    if (listAdapter != null) {
                        return listAdapter;
                    }
                }
                final ListView listView2 = listView;
                AnonymousClass1 r0 = new ArrayAdapter(this.mContext, i, 16908308, this.mItems) {
                    public View getView(int i, View view, ViewGroup viewGroup) {
                        View view2 = super.getView(i, view, viewGroup);
                        AlertParams alertParams = AlertParams.this;
                        if (!alertParams.mEditMode) {
                            boolean[] zArr = alertParams.mCheckedItems;
                            if (zArr != null && zArr[i]) {
                                listView2.setItemChecked(i, true);
                            }
                        }
                        return view2;
                    }
                };
                return r0;
            }
            final ListView listView3 = listView;
            final int i2 = i;
            AnonymousClass2 r02 = new CursorAdapter(this.mContext, cursor, false) {
                private final int mIsCheckedIndex;
                private final int mLabelIndex;

                {
                    Cursor cursor = getCursor();
                    this.mLabelIndex = cursor.getColumnIndexOrThrow(AlertParams.this.mLabelColumn);
                    this.mIsCheckedIndex = cursor.getColumnIndexOrThrow(AlertParams.this.mIsCheckedColumn);
                }

                public void bindView(View view, Context context, Cursor cursor) {
                    ((CheckedTextView) view.findViewById(16908308)).setText(cursor.getString(this.mLabelIndex));
                    if (!AlertParams.this.mEditMode) {
                        ListView listView = listView3;
                        int position = cursor.getPosition();
                        int i = cursor.getInt(this.mIsCheckedIndex);
                        boolean z = true;
                        if (i != 1) {
                            z = false;
                        }
                        listView.setItemChecked(position, z);
                    }
                }

                public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
                    return AlertParams.this.mInflater.inflate(i2, null);
                }
            };
            return r02;
        }

        public void apply(AlertController alertController) {
            View view = this.mCustomTitleView;
            if (view != null) {
                alertController.setCustomTitle(view);
            } else {
                CharSequence charSequence = this.mTitle;
                if (charSequence != null) {
                    alertController.setTitle(charSequence);
                }
            }
            Drawable drawable = this.mIcon;
            if (drawable != null) {
                alertController.setIcon(drawable);
            }
            int i = this.mIconId;
            if (i != 0) {
                alertController.setIcon(i);
            }
            CharSequence charSequence2 = this.mMessage;
            if (charSequence2 != null) {
                alertController.setMessage(charSequence2);
            }
            if (this.mCheckBoxMessage != null) {
                ((AlertControllerWrapper) alertController).getImpl().setCheckBox(this.mIsChecked, this.mCheckBoxMessage);
            }
            CharSequence charSequence3 = this.mPositiveButtonText;
            if (charSequence3 != null) {
                alertController.setButton(-1, charSequence3, this.mPositiveButtonListener, null);
            }
            CharSequence charSequence4 = this.mNegativeButtonText;
            if (charSequence4 != null) {
                alertController.setButton(-2, charSequence4, this.mNegativeButtonListener, null);
            }
            CharSequence charSequence5 = this.mNeutralButtonText;
            if (charSequence5 != null) {
                alertController.setButton(-3, charSequence5, this.mNeutralButtonListener, null);
            }
            if (!(this.mItems == null && this.mCursor == null && this.mAdapter == null)) {
                createListView(alertController);
            }
            View view2 = this.mView;
            if (view2 != null) {
                alertController.setView(view2);
            }
            if (this.mActionItems != null) {
                ((AlertControllerWrapper) alertController).getImpl().setActionItems(this.mActionItems, this.mOnActionItemClickListener);
            }
            ((AlertControllerWrapper) alertController).mHapticFeedbackEnabled = this.mHapticFeedbackEnabled;
        }
    }

    public AlertControllerWrapper(Context context, DialogInterface dialogInterface, Window window) {
        super(context, dialogInterface, window);
        this.mAlertControllerImpl = new AlertControllerImpl(context, dialogInterface, window);
    }

    public Button getButton(int i) {
        return this.mAlertControllerImpl.getButton(i);
    }

    public boolean[] getCheckedItems() {
        return this.mAlertControllerImpl.getCheckedItems();
    }

    public AlertControllerImpl getImpl() {
        return this.mAlertControllerImpl;
    }

    public ListView getListView() {
        return this.mAlertControllerImpl.getListView();
    }

    public TextView getMessageView() {
        return this.mAlertControllerImpl.getMessageView();
    }

    public void installContent() {
        this.mAlertControllerImpl.installContent();
    }

    public boolean isChecked() {
        return this.mAlertControllerImpl.isChecked();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return this.mAlertControllerImpl.onKeyDown(i, keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        return this.mAlertControllerImpl.onKeyUp(i, keyEvent);
    }

    public void onStart() {
        this.mAlertControllerImpl.onStart();
    }

    public void onStop() {
        this.mAlertControllerImpl.onStop();
    }

    public void setButton(int i, CharSequence charSequence, OnClickListener onClickListener, Message message) {
        this.mAlertControllerImpl.setButton(i, charSequence, onClickListener, message);
    }

    public void setCheckBox(boolean z, CharSequence charSequence) {
        this.mAlertControllerImpl.setCheckBox(z, charSequence);
    }

    public void setCustomTitle(View view) {
        this.mAlertControllerImpl.setCustomTitle(view);
    }

    public void setIcon(int i) {
        this.mAlertControllerImpl.setIcon(i);
    }

    public void setIcon(Drawable drawable) {
        this.mAlertControllerImpl.setIcon(drawable);
    }

    public void setInverseBackgroundForced(boolean z) {
    }

    public void setMessage(CharSequence charSequence) {
        this.mAlertControllerImpl.setMessage(charSequence);
    }

    public void setTitle(CharSequence charSequence) {
        this.mAlertControllerImpl.setTitle(charSequence);
    }

    public void setView(View view) {
        this.mAlertControllerImpl.setView(view);
    }
}

package miui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.miui.internal.widget.EditableListViewDelegate;
import com.miui.internal.widget.EditableListViewDelegate.UpdateListener;

public class EditableListView extends ListView {
    private EditableListViewDelegate mDelegate;

    public interface ItemCheckFilter {
        int getCheckableItemCount();

        boolean isItemCheckable(int i);
    }

    public interface MultiChoiceModeListener extends android.widget.AbsListView.MultiChoiceModeListener {
        void onAllItemCheckedStateChanged(ActionMode actionMode, boolean z);
    }

    public EditableListView(Context context) {
        super(context);
        getDelegate();
    }

    public EditableListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getDelegate();
    }

    public EditableListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        getDelegate();
    }

    private EditableListViewDelegate getDelegate() {
        if (this.mDelegate == null) {
            this.mDelegate = new EditableListViewDelegate();
            this.mDelegate.initialize(this, ListView.class);
            this.mDelegate.setUpdateListener(new UpdateListener() {
                public void updateCheckStatus(ActionMode actionMode) {
                    EditableListView.this.updateCheckStatus(actionMode);
                }

                public void updateOnScreenCheckedView(View view, int i, long j) {
                    EditableListView.this.updateOnScreenCheckedView(view, i, j);
                }
            });
        }
        return this.mDelegate;
    }

    public void clearChoices() {
        getDelegate().clearChoices();
    }

    public SparseBooleanArray getCheckedItemPositions() {
        return getDelegate().getCheckedItemPositions();
    }

    public boolean isAllItemsChecked() {
        return getDelegate().isAllItemsChecked();
    }

    public boolean isInActionMode() {
        return getDelegate().isInActionMode();
    }

    public boolean isItemChecked(int i) {
        return getDelegate().isItemChecked(i);
    }

    public boolean isItemIdChecked(long j) {
        return getDelegate().isItemIdChecked(j);
    }

    public void setAdapter(ListAdapter listAdapter) {
        getDelegate().setAdapter(listAdapter);
    }

    public void setAllItemsChecked(boolean z) {
        getDelegate().setAllItemsChecked(z);
    }

    public void setItemCheckFilter(ItemCheckFilter itemCheckFilter) {
        getDelegate().setItemCheckFilter(itemCheckFilter);
    }

    public void setItemChecked(int i, boolean z) {
        getDelegate().setItemChecked(i, z);
    }

    public void setMultiChoiceModeListener(android.widget.AbsListView.MultiChoiceModeListener multiChoiceModeListener) {
        getDelegate().setMultiChoiceModeListener(multiChoiceModeListener);
    }

    /* access modifiers changed from: protected */
    public void updateCheckStatus(ActionMode actionMode) {
        getDelegate().updateCheckStatus(actionMode);
    }

    /* access modifiers changed from: protected */
    public void updateOnScreenCheckedView(View view, int i, long j) {
        getDelegate().updateOnScreenCheckedView(view, i, j);
    }
}

package com.miui.internal.widget;

import android.content.res.Resources;
import android.database.DataSetObserver;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.RecyclerListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.WrapperListAdapter;
import java.util.ArrayList;
import java.util.List;
import miui.R;
import miui.reflect.Method;
import miui.view.ActionModeAnimationListener;
import miui.view.EditActionMode;
import miui.widget.EditableListView.ItemCheckFilter;
import miui.widget.EditableListView.MultiChoiceModeListener;

public class EditableListViewDelegate {
    private static final Method CLEAR_CHOICES = Method.of(AbsListView.class, "clearChoices", "()V");
    private static final Method GET_CHECKED_ITEM_POSITIONS = Method.of(AbsListView.class, "getCheckedItemPositions", "()Landroid/util/SparseBooleanArray;");
    private static final Method IS_ITEM_CHECKED = Method.of(AbsListView.class, "isItemChecked", "(I)Z");
    private static final int KEY_CHECKBOX = Integer.MAX_VALUE;
    private static final Method SET_ADAPTER = Method.of(AbsListView.class, "setAdapter", "(Landroid/widget/ListAdapter;)V");
    private static final Method SET_ITEM_CHECKED = Method.of(AbsListView.class, "setItemChecked", "(IZ)V");
    private static final Method SET_MULTI_CHOICE_MODE_LISTENER = Method.of(AbsListView.class, "setMultiChoiceModeListener", "(Landroid/widget/AbsListView$MultiChoiceModeListener;)V");
    /* access modifiers changed from: private */
    public AbsListView mAbsListView;
    private Class mAbsListViewClass;
    /* access modifiers changed from: private */
    public ActionMode mActionMode;
    private List mCheckBoxList = new ArrayList();
    /* access modifiers changed from: private */
    public int mCheckedItemCount;
    /* access modifiers changed from: private */
    public UpdateListener mDefaultUpdateListener = new UpdateListener() {
        public void updateCheckStatus(ActionMode actionMode) {
            EditableListViewDelegate.this.updateCheckStatus(actionMode);
        }

        public void updateOnScreenCheckedView(View view, int i, long j) {
            EditableListViewDelegate.this.updateOnScreenCheckedView(view, i, j);
        }
    };
    private ItemCheckFilter mItemCheckFilter;
    /* access modifiers changed from: private */
    public int mLastBottom;
    private ListAdapterWrapper mListAdapterWrapper;
    private MultiChoiceModeListenerWrapper mMultiChoiceModeListenerWrapper;
    /* access modifiers changed from: private */
    public boolean mPreventDispatchItemCheckedStateChange;
    private RecyclerListener mRecyclerListener = new RecyclerListener() {
        public void onMovedToScrapHeap(View view) {
            CheckBox access$000 = EditableListViewDelegate.this.findCheckBoxByView(view);
            if (access$000 != null) {
                access$000.setAlpha(1.0f);
                access$000.setTranslationX(0.0f);
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mSupportHeaderView;

    class ListAdapterWrapper extends BaseAdapter implements WrapperListAdapter {
        private ListAdapter mWrapped;

        public ListAdapterWrapper(ListAdapter listAdapter) {
            this.mWrapped = listAdapter;
        }

        public boolean areAllItemsEnabled() {
            return this.mWrapped.areAllItemsEnabled();
        }

        public int getCount() {
            return this.mWrapped.getCount();
        }

        public View getDropDownView(int i, View view, ViewGroup viewGroup) {
            ListAdapter listAdapter = this.mWrapped;
            return listAdapter instanceof BaseAdapter ? ((BaseAdapter) listAdapter).getDropDownView(i, view, viewGroup) : super.getDropDownView(i, view, viewGroup);
        }

        public Object getItem(int i) {
            return this.mWrapped.getItem(i);
        }

        public long getItemId(int i) {
            return this.mWrapped.getItemId(i);
        }

        public int getItemViewType(int i) {
            return this.mWrapped.getItemViewType(i);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:8:0x002e, code lost:
            if (r0 == 1) goto L_0x0030;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view2 = this.mWrapped.getView(i, view, viewGroup);
            CheckBox access$000 = EditableListViewDelegate.this.findCheckBoxByView(view2);
            if (access$000 != null) {
                int choiceMode = EditableListViewDelegate.this.mAbsListView.getChoiceMode();
                boolean isItemChecked = EditableListViewDelegate.this.isItemChecked(i);
                int i2 = 8;
                if (choiceMode == 3) {
                    if (EditableListViewDelegate.this.mActionMode != null) {
                        i2 = 0;
                    }
                }
                access$000.setVisibility(i2);
                if (access$000.getVisibility() == 0 && !(access$000.getTranslationX() == 0.0f && access$000.getAlpha() == 1.0f)) {
                    access$000.setTranslationX(0.0f);
                    access$000.setAlpha(1.0f);
                }
                access$000.setChecked(isItemChecked);
            }
            return view2;
        }

        public int getViewTypeCount() {
            return this.mWrapped.getViewTypeCount();
        }

        public ListAdapter getWrappedAdapter() {
            return this.mWrapped;
        }

        public boolean hasStableIds() {
            return this.mWrapped.hasStableIds();
        }

        public boolean isEmpty() {
            return this.mWrapped.isEmpty();
        }

        public boolean isEnabled(int i) {
            return this.mWrapped.isEnabled(i);
        }

        public void registerDataSetObserver(DataSetObserver dataSetObserver) {
            this.mWrapped.registerDataSetObserver(dataSetObserver);
        }

        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
            this.mWrapped.unregisterDataSetObserver(dataSetObserver);
        }
    }

    class MultiChoiceModeListenerWrapper implements MultiChoiceModeListener {
        private ActionModeAnimationListener mActionModeAnimationListener = new ActionModeAnimationListener() {
            public void onStart(boolean z) {
            }

            public void onStop(boolean z) {
                if (!z) {
                    EditableListViewDelegate.this.mActionMode = null;
                }
            }

            public void onUpdate(boolean z, float f) {
                if (!z) {
                    f = 1.0f - f;
                }
                if (z && f == 1.0f) {
                    int height = EditableListViewDelegate.this.mAbsListView.getHeight();
                    if (EditableListViewDelegate.this.mLastBottom > height) {
                        EditableListViewDelegate.this.mAbsListView.smoothScrollBy(EditableListViewDelegate.this.mLastBottom - height, 100);
                    }
                }
            }
        };
        private AbsListView.MultiChoiceModeListener mWrapped;

        public MultiChoiceModeListenerWrapper() {
        }

        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if (menuItem.getItemId() == 16908313) {
                actionMode.finish();
            } else if (menuItem.getItemId() == 16908314) {
                EditableListViewDelegate editableListViewDelegate = EditableListViewDelegate.this;
                editableListViewDelegate.setAllItemsChecked(!editableListViewDelegate.isAllItemsChecked());
            }
            return this.mWrapped.onActionItemClicked(actionMode, menuItem);
        }

        public void onAllItemCheckedStateChanged(ActionMode actionMode, boolean z) {
            AbsListView.MultiChoiceModeListener multiChoiceModeListener = this.mWrapped;
            if (multiChoiceModeListener instanceof MultiChoiceModeListener) {
                ((MultiChoiceModeListener) multiChoiceModeListener).onAllItemCheckedStateChanged(EditableListViewDelegate.this.mActionMode, z);
            }
        }

        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            EditableListViewDelegate.this.mCheckedItemCount = 0;
            actionMode.setTitle(R.string.select_item);
            if (!this.mWrapped.onCreateActionMode(actionMode, menu)) {
                return false;
            }
            EditableListViewDelegate.this.mActionMode = actionMode;
            ((EditActionMode) EditableListViewDelegate.this.mActionMode).addAnimationListener(this.mActionModeAnimationListener);
            EditableListViewDelegate.this.mDefaultUpdateListener.updateCheckStatus(actionMode);
            EditableListViewDelegate.this.mLastBottom = -1;
            return true;
        }

        public void onDestroyActionMode(ActionMode actionMode) {
            EditableListViewDelegate.this.mCheckedItemCount = 0;
            this.mWrapped.onDestroyActionMode(actionMode);
        }

        public void onItemCheckedStateChanged(ActionMode actionMode, int i, long j, boolean z) {
            if (!EditableListViewDelegate.this.mPreventDispatchItemCheckedStateChange) {
                int headerViewsCount = EditableListViewDelegate.this.mSupportHeaderView ? ((ListView) EditableListViewDelegate.this.mAbsListView).getHeaderViewsCount() : 0;
                int count = EditableListViewDelegate.this.getAdapter().getCount();
                if (i >= headerViewsCount && i < count + headerViewsCount && i <= EditableListViewDelegate.this.mAbsListView.getLastVisiblePosition() && i >= EditableListViewDelegate.this.mAbsListView.getFirstVisiblePosition() && EditableListViewDelegate.this.isItemCheckable(i - headerViewsCount)) {
                    EditableListViewDelegate.access$412(EditableListViewDelegate.this, z ? 1 : -1);
                    View childAt = EditableListViewDelegate.this.mAbsListView.getChildAt(i - EditableListViewDelegate.this.mAbsListView.getFirstVisiblePosition());
                    EditableListViewDelegate.this.mDefaultUpdateListener.updateOnScreenCheckedView(childAt, i, j);
                    EditableListViewDelegate.this.mDefaultUpdateListener.updateCheckStatus(actionMode);
                    if (childAt != null) {
                        childAt.sendAccessibilityEvent(1);
                    }
                    this.mWrapped.onItemCheckedStateChanged(actionMode, i, j, z);
                    if (EditableListViewDelegate.this.mLastBottom == -1) {
                        EditableListViewDelegate.this.mLastBottom = EditableListViewDelegate.this.mAbsListView.getChildAt(i - EditableListViewDelegate.this.mAbsListView.getFirstVisiblePosition()).getBottom();
                    }
                }
            }
        }

        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return this.mWrapped.onPrepareActionMode(actionMode, menu);
        }

        public void setWrapped(AbsListView.MultiChoiceModeListener multiChoiceModeListener) {
            this.mWrapped = multiChoiceModeListener;
        }
    }

    public interface UpdateListener {
        void updateCheckStatus(ActionMode actionMode);

        void updateOnScreenCheckedView(View view, int i, long j);
    }

    static /* synthetic */ int access$412(EditableListViewDelegate editableListViewDelegate, int i) {
        int i2 = editableListViewDelegate.mCheckedItemCount + i;
        editableListViewDelegate.mCheckedItemCount = i2;
        return i2;
    }

    /* access modifiers changed from: private */
    public CheckBox findCheckBoxByView(View view) {
        CheckBox checkBox = (CheckBox) view.getTag(Integer.MAX_VALUE);
        if (checkBox == null) {
            checkBox = (CheckBox) view.findViewById(16908289);
            if (checkBox != null) {
                view.setTag(Integer.MAX_VALUE, checkBox);
            }
        }
        return checkBox;
    }

    private List getCheckBoxesInListView() {
        int childCount = this.mAbsListView.getChildCount();
        List list = this.mCheckBoxList;
        list.clear();
        for (int i = 0; i < childCount; i++) {
            CheckBox findCheckBoxByView = findCheckBoxByView(this.mAbsListView.getChildAt(i));
            if (findCheckBoxByView != null) {
                list.add(findCheckBoxByView);
            }
        }
        return list;
    }

    private int getCheckableItemCount() {
        ItemCheckFilter itemCheckFilter = this.mItemCheckFilter;
        if (itemCheckFilter != null) {
            return itemCheckFilter.getCheckableItemCount();
        }
        if (getAdapter() != null) {
            return getAdapter().getCount();
        }
        return 0;
    }

    /* access modifiers changed from: private */
    public boolean isItemCheckable(int i) {
        ItemCheckFilter itemCheckFilter = this.mItemCheckFilter;
        return itemCheckFilter == null || itemCheckFilter.isItemCheckable(i);
    }

    private void updateOnScreenCheckedViews() {
        int firstVisiblePosition = this.mAbsListView.getFirstVisiblePosition();
        int childCount = this.mAbsListView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            int i2 = firstVisiblePosition + i;
            this.mDefaultUpdateListener.updateOnScreenCheckedView(this.mAbsListView.getChildAt(i), i2, getAdapter().getItemId(i2));
        }
    }

    public void clearChoices() {
        CLEAR_CHOICES.invoke(this.mAbsListViewClass, this.mAbsListView, new Object[0]);
        this.mCheckedItemCount = 0;
        this.mDefaultUpdateListener.updateCheckStatus(this.mActionMode);
        updateOnScreenCheckedViews();
        this.mCheckedItemCount = 0;
    }

    public ListAdapter getAdapter() {
        ListAdapterWrapper listAdapterWrapper = this.mListAdapterWrapper;
        if (listAdapterWrapper != null) {
            return listAdapterWrapper.getWrappedAdapter();
        }
        return null;
    }

    public SparseBooleanArray getCheckedItemPositions() {
        return ((SparseBooleanArray) GET_CHECKED_ITEM_POSITIONS.invokeObject(this.mAbsListViewClass, this.mAbsListView, new Object[0])).clone();
    }

    public AbsListView getListView() {
        return this.mAbsListView;
    }

    public void initialize(AbsListView absListView, Class cls) {
        if (absListView != null) {
            this.mAbsListView = absListView;
            this.mAbsListViewClass = cls;
            this.mAbsListView.setChoiceMode(3);
            this.mAbsListView.setRecyclerListener(this.mRecyclerListener);
            AbsListView absListView2 = this.mAbsListView;
            this.mSupportHeaderView = absListView2 instanceof ListView;
            if (absListView2.getAdapter() != null) {
                ListAdapter listAdapter = (ListAdapter) this.mAbsListView.getAdapter();
                SET_ADAPTER.invoke(this.mAbsListViewClass, this.mAbsListView, null);
                setAdapter(listAdapter);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("absListView is null");
    }

    public boolean isAllItemsChecked() {
        return this.mCheckedItemCount != 0 && getCheckableItemCount() == this.mCheckedItemCount;
    }

    public boolean isInActionMode() {
        return this.mActionMode != null;
    }

    public boolean isItemChecked(int i) {
        return IS_ITEM_CHECKED.invokeBoolean(this.mAbsListViewClass, this.mAbsListView, Integer.valueOf(i + (this.mSupportHeaderView ? ((ListView) this.mAbsListView).getHeaderViewsCount() : 0)));
    }

    public boolean isItemIdChecked(long j) {
        long[] checkedItemIds = this.mAbsListView.getCheckedItemIds();
        int length = checkedItemIds.length;
        for (int i = 0; i < length; i++) {
            if (j == checkedItemIds[i]) {
                return true;
            }
        }
        return false;
    }

    public void setAdapter(ListAdapter listAdapter) {
        if (listAdapter == getAdapter()) {
            return;
        }
        if (listAdapter == null) {
            this.mListAdapterWrapper = null;
            SET_ADAPTER.invoke(this.mAbsListViewClass, this.mAbsListView, null);
            return;
        }
        this.mListAdapterWrapper = new ListAdapterWrapper(listAdapter);
        SET_ADAPTER.invoke(this.mAbsListViewClass, this.mAbsListView, this.mListAdapterWrapper);
    }

    public void setAllItemsChecked(boolean z) {
        this.mPreventDispatchItemCheckedStateChange = true;
        int count = getAdapter().getCount();
        for (int i = 0; i < count; i++) {
            if (isItemCheckable(i)) {
                setItemChecked(i, z);
            }
        }
        this.mCheckedItemCount = z ? getCheckableItemCount() : 0;
        this.mPreventDispatchItemCheckedStateChange = false;
        this.mDefaultUpdateListener.updateCheckStatus(this.mActionMode);
        updateOnScreenCheckedViews();
        MultiChoiceModeListenerWrapper multiChoiceModeListenerWrapper = this.mMultiChoiceModeListenerWrapper;
        if (multiChoiceModeListenerWrapper != null) {
            multiChoiceModeListenerWrapper.onAllItemCheckedStateChanged(this.mActionMode, z);
        }
    }

    public void setItemCheckFilter(ItemCheckFilter itemCheckFilter) {
        this.mItemCheckFilter = itemCheckFilter;
    }

    public void setItemChecked(int i, boolean z) {
        SET_ITEM_CHECKED.invoke(this.mAbsListViewClass, this.mAbsListView, Integer.valueOf(i + (this.mSupportHeaderView ? ((ListView) this.mAbsListView).getHeaderViewsCount() : 0)), Boolean.valueOf(z));
    }

    public void setMultiChoiceModeListener(AbsListView.MultiChoiceModeListener multiChoiceModeListener) {
        if (this.mMultiChoiceModeListenerWrapper == null) {
            this.mMultiChoiceModeListenerWrapper = new MultiChoiceModeListenerWrapper();
        }
        this.mMultiChoiceModeListenerWrapper.setWrapped(multiChoiceModeListener);
        SET_MULTI_CHOICE_MODE_LISTENER.invoke(this.mAbsListViewClass, this.mAbsListView, this.mMultiChoiceModeListenerWrapper);
    }

    public void setUpdateListener(UpdateListener updateListener) {
        if (updateListener != null) {
            this.mDefaultUpdateListener = updateListener;
        }
    }

    public void updateCheckStatus(ActionMode actionMode) {
        if (actionMode != null) {
            int i = this.mCheckedItemCount;
            Resources resources = this.mAbsListView.getResources();
            boolean z = true;
            actionMode.setTitle(i == 0 ? resources.getString(R.string.select_item) : String.format(resources.getQuantityString(R.plurals.items_selected, i), new Object[]{Integer.valueOf(i)}));
            ((EditActionMode) actionMode).setButton((int) EditActionMode.BUTTON2, isAllItemsChecked() ? R.string.deselect_all : R.string.select_all);
            Menu menu = actionMode.getMenu();
            if (i == 0) {
                z = false;
            }
            menu.setGroupEnabled(0, z);
        }
    }

    public void updateOnScreenCheckedView(View view, int i, long j) {
        if (view != null) {
            boolean z = ((SparseBooleanArray) GET_CHECKED_ITEM_POSITIONS.invokeObject(this.mAbsListViewClass, this.mAbsListView, new Object[0])).get(i);
            CheckBox findCheckBoxByView = findCheckBoxByView(view);
            if (findCheckBoxByView != null) {
                findCheckBoxByView.setChecked(z);
            } else if (view instanceof Checkable) {
                ((Checkable) view).setChecked(z);
            }
        }
    }
}

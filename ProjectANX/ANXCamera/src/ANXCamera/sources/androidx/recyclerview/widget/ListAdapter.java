package androidx.recyclerview.widget;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AsyncDifferConfig.Builder;
import androidx.recyclerview.widget.AsyncListDiffer.ListListener;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import java.util.List;

public abstract class ListAdapter extends Adapter {
    final AsyncListDiffer mDiffer;
    private final ListListener mListener = new ListListener() {
        public void onCurrentListChanged(@NonNull List list, @NonNull List list2) {
            ListAdapter.this.onCurrentListChanged(list, list2);
        }
    };

    protected ListAdapter(@NonNull AsyncDifferConfig asyncDifferConfig) {
        this.mDiffer = new AsyncListDiffer((ListUpdateCallback) new AdapterListUpdateCallback(this), asyncDifferConfig);
        this.mDiffer.addListListener(this.mListener);
    }

    protected ListAdapter(@NonNull ItemCallback itemCallback) {
        this.mDiffer = new AsyncListDiffer((ListUpdateCallback) new AdapterListUpdateCallback(this), new Builder(itemCallback).build());
        this.mDiffer.addListListener(this.mListener);
    }

    @NonNull
    public List getCurrentList() {
        return this.mDiffer.getCurrentList();
    }

    /* access modifiers changed from: protected */
    public Object getItem(int i) {
        return this.mDiffer.getCurrentList().get(i);
    }

    public int getItemCount() {
        return this.mDiffer.getCurrentList().size();
    }

    public void onCurrentListChanged(@NonNull List list, @NonNull List list2) {
    }

    public void submitList(@Nullable List list) {
        this.mDiffer.submitList(list);
    }

    public void submitList(@Nullable List list, @Nullable Runnable runnable) {
        this.mDiffer.submitList(list, runnable);
    }
}

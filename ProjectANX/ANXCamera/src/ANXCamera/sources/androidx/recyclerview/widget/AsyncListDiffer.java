package androidx.recyclerview.widget;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AsyncDifferConfig.Builder;
import androidx.recyclerview.widget.DiffUtil.Callback;
import androidx.recyclerview.widget.DiffUtil.DiffResult;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

public class AsyncListDiffer {
    private static final Executor sMainThreadExecutor = new MainThreadExecutor();
    final AsyncDifferConfig mConfig;
    @Nullable
    private List mList;
    private final List mListeners;
    Executor mMainThreadExecutor;
    int mMaxScheduledGeneration;
    @NonNull
    private List mReadOnlyList;
    private final ListUpdateCallback mUpdateCallback;

    public interface ListListener {
        void onCurrentListChanged(@NonNull List list, @NonNull List list2);
    }

    class MainThreadExecutor implements Executor {
        final Handler mHandler = new Handler(Looper.getMainLooper());

        MainThreadExecutor() {
        }

        public void execute(@NonNull Runnable runnable) {
            this.mHandler.post(runnable);
        }
    }

    public AsyncListDiffer(@NonNull ListUpdateCallback listUpdateCallback, @NonNull AsyncDifferConfig asyncDifferConfig) {
        this.mListeners = new CopyOnWriteArrayList();
        this.mReadOnlyList = Collections.emptyList();
        this.mUpdateCallback = listUpdateCallback;
        this.mConfig = asyncDifferConfig;
        this.mMainThreadExecutor = asyncDifferConfig.getMainThreadExecutor() != null ? asyncDifferConfig.getMainThreadExecutor() : sMainThreadExecutor;
    }

    public AsyncListDiffer(@NonNull Adapter adapter, @NonNull ItemCallback itemCallback) {
        this((ListUpdateCallback) new AdapterListUpdateCallback(adapter), new Builder(itemCallback).build());
    }

    private void onCurrentListChanged(@NonNull List list, @Nullable Runnable runnable) {
        for (ListListener onCurrentListChanged : this.mListeners) {
            onCurrentListChanged.onCurrentListChanged(list, this.mReadOnlyList);
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    public void addListListener(@NonNull ListListener listListener) {
        this.mListeners.add(listListener);
    }

    @NonNull
    public List getCurrentList() {
        return this.mReadOnlyList;
    }

    /* access modifiers changed from: 0000 */
    public void latchList(@NonNull List list, @NonNull DiffResult diffResult, @Nullable Runnable runnable) {
        List list2 = this.mReadOnlyList;
        this.mList = list;
        this.mReadOnlyList = Collections.unmodifiableList(list);
        diffResult.dispatchUpdatesTo(this.mUpdateCallback);
        onCurrentListChanged(list2, runnable);
    }

    public void removeListListener(@NonNull ListListener listListener) {
        this.mListeners.remove(listListener);
    }

    public void submitList(@Nullable List list) {
        submitList(list, null);
    }

    public void submitList(@Nullable List list, @Nullable Runnable runnable) {
        final int i = this.mMaxScheduledGeneration + 1;
        this.mMaxScheduledGeneration = i;
        final List list2 = this.mList;
        if (list == list2) {
            if (runnable != null) {
                runnable.run();
            }
            return;
        }
        List list3 = this.mReadOnlyList;
        if (list == null) {
            int size = list2.size();
            this.mList = null;
            this.mReadOnlyList = Collections.emptyList();
            this.mUpdateCallback.onRemoved(0, size);
            onCurrentListChanged(list3, runnable);
        } else if (list2 == null) {
            this.mList = list;
            this.mReadOnlyList = Collections.unmodifiableList(list);
            this.mUpdateCallback.onInserted(0, list.size());
            onCurrentListChanged(list3, runnable);
        } else {
            Executor backgroundThreadExecutor = this.mConfig.getBackgroundThreadExecutor();
            final List list4 = list;
            final Runnable runnable2 = runnable;
            AnonymousClass1 r1 = new Runnable() {
                public void run() {
                    final DiffResult calculateDiff = DiffUtil.calculateDiff(new Callback() {
                        public boolean areContentsTheSame(int i, int i2) {
                            Object obj = list2.get(i);
                            Object obj2 = list4.get(i2);
                            if (obj != null && obj2 != null) {
                                return AsyncListDiffer.this.mConfig.getDiffCallback().areContentsTheSame(obj, obj2);
                            }
                            if (obj == null && obj2 == null) {
                                return true;
                            }
                            throw new AssertionError();
                        }

                        public boolean areItemsTheSame(int i, int i2) {
                            Object obj = list2.get(i);
                            Object obj2 = list4.get(i2);
                            if (obj != null && obj2 != null) {
                                return AsyncListDiffer.this.mConfig.getDiffCallback().areItemsTheSame(obj, obj2);
                            }
                            boolean z = obj == null && obj2 == null;
                            return z;
                        }

                        @Nullable
                        public Object getChangePayload(int i, int i2) {
                            Object obj = list2.get(i);
                            Object obj2 = list4.get(i2);
                            if (obj != null && obj2 != null) {
                                return AsyncListDiffer.this.mConfig.getDiffCallback().getChangePayload(obj, obj2);
                            }
                            throw new AssertionError();
                        }

                        public int getNewListSize() {
                            return list4.size();
                        }

                        public int getOldListSize() {
                            return list2.size();
                        }
                    });
                    AsyncListDiffer.this.mMainThreadExecutor.execute(new Runnable() {
                        public void run() {
                            AnonymousClass1 r0 = AnonymousClass1.this;
                            AsyncListDiffer asyncListDiffer = AsyncListDiffer.this;
                            if (asyncListDiffer.mMaxScheduledGeneration == i) {
                                asyncListDiffer.latchList(list4, calculateDiff, runnable2);
                            }
                        }
                    });
                }
            };
            backgroundThreadExecutor.execute(r1);
        }
    }
}

package androidx.recyclerview.widget;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.collection.ArrayMap;
import androidx.collection.LongSparseArray;
import androidx.core.util.Pools.Pool;
import androidx.core.util.Pools.SimplePool;
import androidx.recyclerview.widget.RecyclerView.ItemAnimator.ItemHolderInfo;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

class ViewInfoStore {
    private static final boolean DEBUG = false;
    @VisibleForTesting
    final ArrayMap mLayoutHolderMap = new ArrayMap();
    @VisibleForTesting
    final LongSparseArray mOldChangedHolders = new LongSparseArray();

    class InfoRecord {
        static final int FLAG_APPEAR = 2;
        static final int FLAG_APPEAR_AND_DISAPPEAR = 3;
        static final int FLAG_APPEAR_PRE_AND_POST = 14;
        static final int FLAG_DISAPPEARED = 1;
        static final int FLAG_POST = 8;
        static final int FLAG_PRE = 4;
        static final int FLAG_PRE_AND_POST = 12;
        static Pool sPool = new SimplePool(20);
        int flags;
        @Nullable
        ItemHolderInfo postInfo;
        @Nullable
        ItemHolderInfo preInfo;

        private InfoRecord() {
        }

        static void drainCache() {
            while (sPool.acquire() != null) {
            }
        }

        static InfoRecord obtain() {
            InfoRecord infoRecord = (InfoRecord) sPool.acquire();
            return infoRecord == null ? new InfoRecord() : infoRecord;
        }

        static void recycle(InfoRecord infoRecord) {
            infoRecord.flags = 0;
            infoRecord.preInfo = null;
            infoRecord.postInfo = null;
            sPool.release(infoRecord);
        }
    }

    interface ProcessCallback {
        void processAppeared(ViewHolder viewHolder, @Nullable ItemHolderInfo itemHolderInfo, ItemHolderInfo itemHolderInfo2);

        void processDisappeared(ViewHolder viewHolder, @NonNull ItemHolderInfo itemHolderInfo, @Nullable ItemHolderInfo itemHolderInfo2);

        void processPersistent(ViewHolder viewHolder, @NonNull ItemHolderInfo itemHolderInfo, @NonNull ItemHolderInfo itemHolderInfo2);

        void unused(ViewHolder viewHolder);
    }

    ViewInfoStore() {
    }

    private ItemHolderInfo popFromLayoutStep(ViewHolder viewHolder, int i) {
        ItemHolderInfo itemHolderInfo;
        int indexOfKey = this.mLayoutHolderMap.indexOfKey(viewHolder);
        if (indexOfKey < 0) {
            return null;
        }
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.valueAt(indexOfKey);
        if (infoRecord != null) {
            int i2 = infoRecord.flags;
            if ((i2 & i) != 0) {
                infoRecord.flags = (~i) & i2;
                if (i == 4) {
                    itemHolderInfo = infoRecord.preInfo;
                } else if (i == 8) {
                    itemHolderInfo = infoRecord.postInfo;
                } else {
                    throw new IllegalArgumentException("Must provide flag PRE or POST");
                }
                if ((infoRecord.flags & 12) == 0) {
                    this.mLayoutHolderMap.removeAt(indexOfKey);
                    InfoRecord.recycle(infoRecord);
                }
                return itemHolderInfo;
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public void addToAppearedInPreLayoutHolders(ViewHolder viewHolder, ItemHolderInfo itemHolderInfo) {
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.get(viewHolder);
        if (infoRecord == null) {
            infoRecord = InfoRecord.obtain();
            this.mLayoutHolderMap.put(viewHolder, infoRecord);
        }
        infoRecord.flags |= 2;
        infoRecord.preInfo = itemHolderInfo;
    }

    /* access modifiers changed from: 0000 */
    public void addToDisappearedInLayout(ViewHolder viewHolder) {
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.get(viewHolder);
        if (infoRecord == null) {
            infoRecord = InfoRecord.obtain();
            this.mLayoutHolderMap.put(viewHolder, infoRecord);
        }
        infoRecord.flags |= 1;
    }

    /* access modifiers changed from: 0000 */
    public void addToOldChangeHolders(long j, ViewHolder viewHolder) {
        this.mOldChangedHolders.put(j, viewHolder);
    }

    /* access modifiers changed from: 0000 */
    public void addToPostLayout(ViewHolder viewHolder, ItemHolderInfo itemHolderInfo) {
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.get(viewHolder);
        if (infoRecord == null) {
            infoRecord = InfoRecord.obtain();
            this.mLayoutHolderMap.put(viewHolder, infoRecord);
        }
        infoRecord.postInfo = itemHolderInfo;
        infoRecord.flags |= 8;
    }

    /* access modifiers changed from: 0000 */
    public void addToPreLayout(ViewHolder viewHolder, ItemHolderInfo itemHolderInfo) {
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.get(viewHolder);
        if (infoRecord == null) {
            infoRecord = InfoRecord.obtain();
            this.mLayoutHolderMap.put(viewHolder, infoRecord);
        }
        infoRecord.preInfo = itemHolderInfo;
        infoRecord.flags |= 4;
    }

    /* access modifiers changed from: 0000 */
    public void clear() {
        this.mLayoutHolderMap.clear();
        this.mOldChangedHolders.clear();
    }

    /* access modifiers changed from: 0000 */
    public ViewHolder getFromOldChangeHolders(long j) {
        return (ViewHolder) this.mOldChangedHolders.get(j);
    }

    /* access modifiers changed from: 0000 */
    public boolean isDisappearing(ViewHolder viewHolder) {
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.get(viewHolder);
        return (infoRecord == null || (infoRecord.flags & 1) == 0) ? false : true;
    }

    /* access modifiers changed from: 0000 */
    public boolean isInPreLayout(ViewHolder viewHolder) {
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.get(viewHolder);
        return (infoRecord == null || (infoRecord.flags & 4) == 0) ? false : true;
    }

    /* access modifiers changed from: 0000 */
    public void onDetach() {
        InfoRecord.drainCache();
    }

    public void onViewDetached(ViewHolder viewHolder) {
        removeFromDisappearedInLayout(viewHolder);
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public ItemHolderInfo popFromPostLayout(ViewHolder viewHolder) {
        return popFromLayoutStep(viewHolder, 8);
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public ItemHolderInfo popFromPreLayout(ViewHolder viewHolder) {
        return popFromLayoutStep(viewHolder, 4);
    }

    /* access modifiers changed from: 0000 */
    public void process(ProcessCallback processCallback) {
        ItemHolderInfo itemHolderInfo;
        ItemHolderInfo itemHolderInfo2;
        for (int size = this.mLayoutHolderMap.size() - 1; size >= 0; size--) {
            ViewHolder viewHolder = (ViewHolder) this.mLayoutHolderMap.keyAt(size);
            InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.removeAt(size);
            int i = infoRecord.flags;
            if ((i & 3) != 3) {
                if ((i & 1) != 0) {
                    itemHolderInfo = infoRecord.preInfo;
                    if (itemHolderInfo != null) {
                        itemHolderInfo2 = infoRecord.postInfo;
                    }
                } else {
                    if ((i & 14) != 14) {
                        if ((i & 12) == 12) {
                            processCallback.processPersistent(viewHolder, infoRecord.preInfo, infoRecord.postInfo);
                        } else if ((i & 4) != 0) {
                            itemHolderInfo = infoRecord.preInfo;
                            itemHolderInfo2 = null;
                        } else if ((i & 8) == 0) {
                        }
                        InfoRecord.recycle(infoRecord);
                    }
                    processCallback.processAppeared(viewHolder, infoRecord.preInfo, infoRecord.postInfo);
                    InfoRecord.recycle(infoRecord);
                }
                processCallback.processDisappeared(viewHolder, itemHolderInfo, itemHolderInfo2);
                InfoRecord.recycle(infoRecord);
            }
            processCallback.unused(viewHolder);
            InfoRecord.recycle(infoRecord);
        }
    }

    /* access modifiers changed from: 0000 */
    public void removeFromDisappearedInLayout(ViewHolder viewHolder) {
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.get(viewHolder);
        if (infoRecord != null) {
            infoRecord.flags &= -2;
        }
    }

    /* access modifiers changed from: 0000 */
    public void removeViewHolder(ViewHolder viewHolder) {
        int size = this.mOldChangedHolders.size() - 1;
        while (true) {
            if (size < 0) {
                break;
            } else if (viewHolder == this.mOldChangedHolders.valueAt(size)) {
                this.mOldChangedHolders.removeAt(size);
                break;
            } else {
                size--;
            }
        }
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.remove(viewHolder);
        if (infoRecord != null) {
            InfoRecord.recycle(infoRecord);
        }
    }
}

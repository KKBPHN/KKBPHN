package androidx.recyclerview.widget;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.recyclerview.widget.ThreadUtil.BackgroundCallback;
import androidx.recyclerview.widget.ThreadUtil.MainThreadCallback;
import androidx.recyclerview.widget.TileList.Tile;

public class AsyncListUtil {
    static final boolean DEBUG = false;
    static final String TAG = "AsyncListUtil";
    boolean mAllowScrollHints;
    private final BackgroundCallback mBackgroundCallback = new BackgroundCallback() {
        private int mFirstRequiredTileStart;
        private int mGeneration;
        private int mItemCount;
        private int mLastRequiredTileStart;
        final SparseBooleanArray mLoadedTiles = new SparseBooleanArray();
        private Tile mRecycledRoot;

        private Tile acquireTile() {
            Tile tile = this.mRecycledRoot;
            if (tile != null) {
                this.mRecycledRoot = tile.mNext;
                return tile;
            }
            AsyncListUtil asyncListUtil = AsyncListUtil.this;
            return new Tile(asyncListUtil.mTClass, asyncListUtil.mTileSize);
        }

        private void addTile(Tile tile) {
            this.mLoadedTiles.put(tile.mStartPosition, true);
            AsyncListUtil.this.mMainThreadProxy.addTile(this.mGeneration, tile);
        }

        private void flushTileCache(int i) {
            int maxCachedTiles = AsyncListUtil.this.mDataCallback.getMaxCachedTiles();
            while (this.mLoadedTiles.size() >= maxCachedTiles) {
                int keyAt = this.mLoadedTiles.keyAt(0);
                SparseBooleanArray sparseBooleanArray = this.mLoadedTiles;
                int keyAt2 = sparseBooleanArray.keyAt(sparseBooleanArray.size() - 1);
                int i2 = this.mFirstRequiredTileStart - keyAt;
                int i3 = keyAt2 - this.mLastRequiredTileStart;
                if (i2 > 0 && (i2 >= i3 || i == 2)) {
                    removeTile(keyAt);
                } else if (i3 <= 0) {
                    return;
                } else {
                    if (i2 < i3 || i == 1) {
                        removeTile(keyAt2);
                    } else {
                        return;
                    }
                }
            }
        }

        private int getTileStart(int i) {
            return i - (i % AsyncListUtil.this.mTileSize);
        }

        private boolean isTileLoaded(int i) {
            return this.mLoadedTiles.get(i);
        }

        private void log(String str, Object... objArr) {
            StringBuilder sb = new StringBuilder();
            sb.append("[BKGR] ");
            sb.append(String.format(str, objArr));
            Log.d(AsyncListUtil.TAG, sb.toString());
        }

        private void removeTile(int i) {
            this.mLoadedTiles.delete(i);
            AsyncListUtil.this.mMainThreadProxy.removeTile(this.mGeneration, i);
        }

        private void requestTiles(int i, int i2, int i3, boolean z) {
            int i4 = i;
            while (i4 <= i2) {
                AsyncListUtil.this.mBackgroundProxy.loadTile(z ? (i2 + i) - i4 : i4, i3);
                i4 += AsyncListUtil.this.mTileSize;
            }
        }

        public void loadTile(int i, int i2) {
            if (!isTileLoaded(i)) {
                Tile acquireTile = acquireTile();
                acquireTile.mStartPosition = i;
                acquireTile.mItemCount = Math.min(AsyncListUtil.this.mTileSize, this.mItemCount - acquireTile.mStartPosition);
                AsyncListUtil.this.mDataCallback.fillData(acquireTile.mItems, acquireTile.mStartPosition, acquireTile.mItemCount);
                flushTileCache(i2);
                addTile(acquireTile);
            }
        }

        public void recycleTile(Tile tile) {
            AsyncListUtil.this.mDataCallback.recycleData(tile.mItems, tile.mItemCount);
            tile.mNext = this.mRecycledRoot;
            this.mRecycledRoot = tile;
        }

        public void refresh(int i) {
            this.mGeneration = i;
            this.mLoadedTiles.clear();
            this.mItemCount = AsyncListUtil.this.mDataCallback.refreshData();
            AsyncListUtil.this.mMainThreadProxy.updateItemCount(this.mGeneration, this.mItemCount);
        }

        public void updateRange(int i, int i2, int i3, int i4, int i5) {
            if (i <= i2) {
                int tileStart = getTileStart(i);
                int tileStart2 = getTileStart(i2);
                this.mFirstRequiredTileStart = getTileStart(i3);
                this.mLastRequiredTileStart = getTileStart(i4);
                if (i5 == 1) {
                    requestTiles(this.mFirstRequiredTileStart, tileStart2, i5, true);
                    requestTiles(tileStart2 + AsyncListUtil.this.mTileSize, this.mLastRequiredTileStart, i5, false);
                } else {
                    requestTiles(tileStart, this.mLastRequiredTileStart, i5, false);
                    requestTiles(this.mFirstRequiredTileStart, tileStart - AsyncListUtil.this.mTileSize, i5, true);
                }
            }
        }
    };
    final BackgroundCallback mBackgroundProxy;
    final DataCallback mDataCallback;
    int mDisplayedGeneration = 0;
    int mItemCount = 0;
    private final MainThreadCallback mMainThreadCallback = new MainThreadCallback() {
        private boolean isRequestedGeneration(int i) {
            return i == AsyncListUtil.this.mRequestedGeneration;
        }

        private void recycleAllTiles() {
            for (int i = 0; i < AsyncListUtil.this.mTileList.size(); i++) {
                AsyncListUtil asyncListUtil = AsyncListUtil.this;
                asyncListUtil.mBackgroundProxy.recycleTile(asyncListUtil.mTileList.getAtIndex(i));
            }
            AsyncListUtil.this.mTileList.clear();
        }

        public void addTile(int i, Tile tile) {
            if (!isRequestedGeneration(i)) {
                AsyncListUtil.this.mBackgroundProxy.recycleTile(tile);
                return;
            }
            Tile addOrReplace = AsyncListUtil.this.mTileList.addOrReplace(tile);
            if (addOrReplace != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("duplicate tile @");
                sb.append(addOrReplace.mStartPosition);
                Log.e(AsyncListUtil.TAG, sb.toString());
                AsyncListUtil.this.mBackgroundProxy.recycleTile(addOrReplace);
            }
            int i2 = tile.mStartPosition + tile.mItemCount;
            int i3 = 0;
            while (i3 < AsyncListUtil.this.mMissingPositions.size()) {
                int keyAt = AsyncListUtil.this.mMissingPositions.keyAt(i3);
                if (tile.mStartPosition > keyAt || keyAt >= i2) {
                    i3++;
                } else {
                    AsyncListUtil.this.mMissingPositions.removeAt(i3);
                    AsyncListUtil.this.mViewCallback.onItemLoaded(keyAt);
                }
            }
        }

        public void removeTile(int i, int i2) {
            if (isRequestedGeneration(i)) {
                Tile removeAtPos = AsyncListUtil.this.mTileList.removeAtPos(i2);
                if (removeAtPos == null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("tile not found @");
                    sb.append(i2);
                    Log.e(AsyncListUtil.TAG, sb.toString());
                    return;
                }
                AsyncListUtil.this.mBackgroundProxy.recycleTile(removeAtPos);
            }
        }

        public void updateItemCount(int i, int i2) {
            if (isRequestedGeneration(i)) {
                AsyncListUtil asyncListUtil = AsyncListUtil.this;
                asyncListUtil.mItemCount = i2;
                asyncListUtil.mViewCallback.onDataRefresh();
                AsyncListUtil asyncListUtil2 = AsyncListUtil.this;
                asyncListUtil2.mDisplayedGeneration = asyncListUtil2.mRequestedGeneration;
                recycleAllTiles();
                AsyncListUtil asyncListUtil3 = AsyncListUtil.this;
                asyncListUtil3.mAllowScrollHints = false;
                asyncListUtil3.updateRange();
            }
        }
    };
    final MainThreadCallback mMainThreadProxy;
    final SparseIntArray mMissingPositions = new SparseIntArray();
    final int[] mPrevRange = new int[2];
    int mRequestedGeneration = this.mDisplayedGeneration;
    private int mScrollHint = 0;
    final Class mTClass;
    final TileList mTileList;
    final int mTileSize;
    final int[] mTmpRange = new int[2];
    final int[] mTmpRangeExtended = new int[2];
    final ViewCallback mViewCallback;

    public abstract class DataCallback {
        @WorkerThread
        public abstract void fillData(@NonNull Object[] objArr, int i, int i2);

        @WorkerThread
        public int getMaxCachedTiles() {
            return 10;
        }

        @WorkerThread
        public void recycleData(@NonNull Object[] objArr, int i) {
        }

        @WorkerThread
        public abstract int refreshData();
    }

    public abstract class ViewCallback {
        public static final int HINT_SCROLL_ASC = 2;
        public static final int HINT_SCROLL_DESC = 1;
        public static final int HINT_SCROLL_NONE = 0;

        @UiThread
        public void extendRangeInto(@NonNull int[] iArr, @NonNull int[] iArr2, int i) {
            int i2 = (iArr[1] - iArr[0]) + 1;
            int i3 = i2 / 2;
            iArr2[0] = iArr[0] - (i == 1 ? i2 : i3);
            int i4 = iArr[1];
            if (i != 2) {
                i2 = i3;
            }
            iArr2[1] = i4 + i2;
        }

        @UiThread
        public abstract void getItemRangeInto(@NonNull int[] iArr);

        @UiThread
        public abstract void onDataRefresh();

        @UiThread
        public abstract void onItemLoaded(int i);
    }

    public AsyncListUtil(@NonNull Class cls, int i, @NonNull DataCallback dataCallback, @NonNull ViewCallback viewCallback) {
        this.mTClass = cls;
        this.mTileSize = i;
        this.mDataCallback = dataCallback;
        this.mViewCallback = viewCallback;
        this.mTileList = new TileList(this.mTileSize);
        MessageThreadUtil messageThreadUtil = new MessageThreadUtil();
        this.mMainThreadProxy = messageThreadUtil.getMainThreadProxy(this.mMainThreadCallback);
        this.mBackgroundProxy = messageThreadUtil.getBackgroundProxy(this.mBackgroundCallback);
        refresh();
    }

    private boolean isRefreshPending() {
        return this.mRequestedGeneration != this.mDisplayedGeneration;
    }

    @Nullable
    public Object getItem(int i) {
        if (i < 0 || i >= this.mItemCount) {
            StringBuilder sb = new StringBuilder();
            sb.append(i);
            sb.append(" is not within 0 and ");
            sb.append(this.mItemCount);
            throw new IndexOutOfBoundsException(sb.toString());
        }
        Object itemAt = this.mTileList.getItemAt(i);
        if (itemAt == null && !isRefreshPending()) {
            this.mMissingPositions.put(i, 0);
        }
        return itemAt;
    }

    public int getItemCount() {
        return this.mItemCount;
    }

    /* access modifiers changed from: 0000 */
    public void log(String str, Object... objArr) {
        StringBuilder sb = new StringBuilder();
        sb.append("[MAIN] ");
        sb.append(String.format(str, objArr));
        Log.d(TAG, sb.toString());
    }

    public void onRangeChanged() {
        if (!isRefreshPending()) {
            updateRange();
            this.mAllowScrollHints = true;
        }
    }

    public void refresh() {
        this.mMissingPositions.clear();
        BackgroundCallback backgroundCallback = this.mBackgroundProxy;
        int i = this.mRequestedGeneration + 1;
        this.mRequestedGeneration = i;
        backgroundCallback.refresh(i);
    }

    /* access modifiers changed from: 0000 */
    public void updateRange() {
        this.mViewCallback.getItemRangeInto(this.mTmpRange);
        int[] iArr = this.mTmpRange;
        if (iArr[0] <= iArr[1] && iArr[0] >= 0 && iArr[1] < this.mItemCount) {
            if (this.mAllowScrollHints) {
                int i = iArr[0];
                int[] iArr2 = this.mPrevRange;
                if (i <= iArr2[1] && iArr2[0] <= iArr[1]) {
                    if (iArr[0] < iArr2[0]) {
                        this.mScrollHint = 1;
                    } else if (iArr[0] > iArr2[0]) {
                        this.mScrollHint = 2;
                    }
                    int[] iArr3 = this.mPrevRange;
                    int[] iArr4 = this.mTmpRange;
                    iArr3[0] = iArr4[0];
                    iArr3[1] = iArr4[1];
                    this.mViewCallback.extendRangeInto(iArr4, this.mTmpRangeExtended, this.mScrollHint);
                    int[] iArr5 = this.mTmpRangeExtended;
                    iArr5[0] = Math.min(this.mTmpRange[0], Math.max(iArr5[0], 0));
                    int[] iArr6 = this.mTmpRangeExtended;
                    iArr6[1] = Math.max(this.mTmpRange[1], Math.min(iArr6[1], this.mItemCount - 1));
                    BackgroundCallback backgroundCallback = this.mBackgroundProxy;
                    int[] iArr7 = this.mTmpRange;
                    int i2 = iArr7[0];
                    int i3 = iArr7[1];
                    int[] iArr8 = this.mTmpRangeExtended;
                    backgroundCallback.updateRange(i2, i3, iArr8[0], iArr8[1], this.mScrollHint);
                }
            }
            this.mScrollHint = 0;
            int[] iArr32 = this.mPrevRange;
            int[] iArr42 = this.mTmpRange;
            iArr32[0] = iArr42[0];
            iArr32[1] = iArr42[1];
            this.mViewCallback.extendRangeInto(iArr42, this.mTmpRangeExtended, this.mScrollHint);
            int[] iArr52 = this.mTmpRangeExtended;
            iArr52[0] = Math.min(this.mTmpRange[0], Math.max(iArr52[0], 0));
            int[] iArr62 = this.mTmpRangeExtended;
            iArr62[1] = Math.max(this.mTmpRange[1], Math.min(iArr62[1], this.mItemCount - 1));
            BackgroundCallback backgroundCallback2 = this.mBackgroundProxy;
            int[] iArr72 = this.mTmpRange;
            int i22 = iArr72[0];
            int i32 = iArr72[1];
            int[] iArr82 = this.mTmpRangeExtended;
            backgroundCallback2.updateRange(i22, i32, iArr82[0], iArr82[1], this.mScrollHint);
        }
    }
}

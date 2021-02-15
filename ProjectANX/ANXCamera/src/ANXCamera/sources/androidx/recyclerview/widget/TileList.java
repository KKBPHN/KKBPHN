package androidx.recyclerview.widget;

import android.util.SparseArray;
import java.lang.reflect.Array;

class TileList {
    Tile mLastAccessedTile;
    final int mTileSize;
    private final SparseArray mTiles = new SparseArray(10);

    public class Tile {
        public int mItemCount;
        public final Object[] mItems;
        Tile mNext;
        public int mStartPosition;

        public Tile(Class cls, int i) {
            this.mItems = (Object[]) Array.newInstance(cls, i);
        }

        /* access modifiers changed from: 0000 */
        public boolean containsPosition(int i) {
            int i2 = this.mStartPosition;
            return i2 <= i && i < i2 + this.mItemCount;
        }

        /* access modifiers changed from: 0000 */
        public Object getByPosition(int i) {
            return this.mItems[i - this.mStartPosition];
        }
    }

    public TileList(int i) {
        this.mTileSize = i;
    }

    public Tile addOrReplace(Tile tile) {
        int indexOfKey = this.mTiles.indexOfKey(tile.mStartPosition);
        if (indexOfKey < 0) {
            this.mTiles.put(tile.mStartPosition, tile);
            return null;
        }
        Tile tile2 = (Tile) this.mTiles.valueAt(indexOfKey);
        this.mTiles.setValueAt(indexOfKey, tile);
        if (this.mLastAccessedTile == tile2) {
            this.mLastAccessedTile = tile;
        }
        return tile2;
    }

    public void clear() {
        this.mTiles.clear();
    }

    public Tile getAtIndex(int i) {
        return (Tile) this.mTiles.valueAt(i);
    }

    public Object getItemAt(int i) {
        Tile tile = this.mLastAccessedTile;
        if (tile == null || !tile.containsPosition(i)) {
            int indexOfKey = this.mTiles.indexOfKey(i - (i % this.mTileSize));
            if (indexOfKey < 0) {
                return null;
            }
            this.mLastAccessedTile = (Tile) this.mTiles.valueAt(indexOfKey);
        }
        return this.mLastAccessedTile.getByPosition(i);
    }

    public Tile removeAtPos(int i) {
        Tile tile = (Tile) this.mTiles.get(i);
        if (this.mLastAccessedTile == tile) {
            this.mLastAccessedTile = null;
        }
        this.mTiles.delete(i);
        return tile;
    }

    public int size() {
        return this.mTiles.size();
    }
}

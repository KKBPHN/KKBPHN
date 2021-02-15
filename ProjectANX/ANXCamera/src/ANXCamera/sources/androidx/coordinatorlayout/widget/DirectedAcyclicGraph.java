package androidx.coordinatorlayout.widget;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.collection.SimpleArrayMap;
import androidx.core.util.Pools.Pool;
import androidx.core.util.Pools.SimplePool;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RestrictTo({Scope.LIBRARY})
public final class DirectedAcyclicGraph {
    private final SimpleArrayMap mGraph = new SimpleArrayMap();
    private final Pool mListPool = new SimplePool(10);
    private final ArrayList mSortResult = new ArrayList();
    private final HashSet mSortTmpMarked = new HashSet();

    private void dfs(Object obj, ArrayList arrayList, HashSet hashSet) {
        if (!arrayList.contains(obj)) {
            if (!hashSet.contains(obj)) {
                hashSet.add(obj);
                ArrayList arrayList2 = (ArrayList) this.mGraph.get(obj);
                if (arrayList2 != null) {
                    int size = arrayList2.size();
                    for (int i = 0; i < size; i++) {
                        dfs(arrayList2.get(i), arrayList, hashSet);
                    }
                }
                hashSet.remove(obj);
                arrayList.add(obj);
                return;
            }
            throw new RuntimeException("This graph contains cyclic dependencies");
        }
    }

    @NonNull
    private ArrayList getEmptyList() {
        ArrayList arrayList = (ArrayList) this.mListPool.acquire();
        return arrayList == null ? new ArrayList() : arrayList;
    }

    private void poolList(@NonNull ArrayList arrayList) {
        arrayList.clear();
        this.mListPool.release(arrayList);
    }

    public void addEdge(@NonNull Object obj, @NonNull Object obj2) {
        if (!this.mGraph.containsKey(obj) || !this.mGraph.containsKey(obj2)) {
            throw new IllegalArgumentException("All nodes must be present in the graph before being added as an edge");
        }
        ArrayList arrayList = (ArrayList) this.mGraph.get(obj);
        if (arrayList == null) {
            arrayList = getEmptyList();
            this.mGraph.put(obj, arrayList);
        }
        arrayList.add(obj2);
    }

    public void addNode(@NonNull Object obj) {
        if (!this.mGraph.containsKey(obj)) {
            this.mGraph.put(obj, null);
        }
    }

    public void clear() {
        int size = this.mGraph.size();
        for (int i = 0; i < size; i++) {
            ArrayList arrayList = (ArrayList) this.mGraph.valueAt(i);
            if (arrayList != null) {
                poolList(arrayList);
            }
        }
        this.mGraph.clear();
    }

    public boolean contains(@NonNull Object obj) {
        return this.mGraph.containsKey(obj);
    }

    @Nullable
    public List getIncomingEdges(@NonNull Object obj) {
        return (List) this.mGraph.get(obj);
    }

    @Nullable
    public List getOutgoingEdges(@NonNull Object obj) {
        int size = this.mGraph.size();
        ArrayList arrayList = null;
        for (int i = 0; i < size; i++) {
            ArrayList arrayList2 = (ArrayList) this.mGraph.valueAt(i);
            if (arrayList2 != null && arrayList2.contains(obj)) {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                arrayList.add(this.mGraph.keyAt(i));
            }
        }
        return arrayList;
    }

    @NonNull
    public ArrayList getSortedList() {
        this.mSortResult.clear();
        this.mSortTmpMarked.clear();
        int size = this.mGraph.size();
        for (int i = 0; i < size; i++) {
            dfs(this.mGraph.keyAt(i), this.mSortResult, this.mSortTmpMarked);
        }
        return this.mSortResult;
    }

    public boolean hasOutgoingEdges(@NonNull Object obj) {
        int size = this.mGraph.size();
        for (int i = 0; i < size; i++) {
            ArrayList arrayList = (ArrayList) this.mGraph.valueAt(i);
            if (arrayList != null && arrayList.contains(obj)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public int size() {
        return this.mGraph.size();
    }
}

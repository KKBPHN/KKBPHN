package androidx.recyclerview.widget;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DiffUtil {
    private static final Comparator SNAKE_COMPARATOR = new Comparator() {
        public int compare(Snake snake, Snake snake2) {
            int i = snake.x - snake2.x;
            return i == 0 ? snake.y - snake2.y : i;
        }
    };

    public abstract class Callback {
        public abstract boolean areContentsTheSame(int i, int i2);

        public abstract boolean areItemsTheSame(int i, int i2);

        @Nullable
        public Object getChangePayload(int i, int i2) {
            return null;
        }

        public abstract int getNewListSize();

        public abstract int getOldListSize();
    }

    public class DiffResult {
        private static final int FLAG_CHANGED = 2;
        private static final int FLAG_IGNORE = 16;
        private static final int FLAG_MASK = 31;
        private static final int FLAG_MOVED_CHANGED = 4;
        private static final int FLAG_MOVED_NOT_CHANGED = 8;
        private static final int FLAG_NOT_CHANGED = 1;
        private static final int FLAG_OFFSET = 5;
        public static final int NO_POSITION = -1;
        private final Callback mCallback;
        private final boolean mDetectMoves;
        private final int[] mNewItemStatuses;
        private final int mNewListSize;
        private final int[] mOldItemStatuses;
        private final int mOldListSize;
        private final List mSnakes;

        DiffResult(Callback callback, List list, int[] iArr, int[] iArr2, boolean z) {
            this.mSnakes = list;
            this.mOldItemStatuses = iArr;
            this.mNewItemStatuses = iArr2;
            Arrays.fill(this.mOldItemStatuses, 0);
            Arrays.fill(this.mNewItemStatuses, 0);
            this.mCallback = callback;
            this.mOldListSize = callback.getOldListSize();
            this.mNewListSize = callback.getNewListSize();
            this.mDetectMoves = z;
            addRootSnake();
            findMatchingItems();
        }

        private void addRootSnake() {
            Snake snake = this.mSnakes.isEmpty() ? null : (Snake) this.mSnakes.get(0);
            if (snake == null || snake.x != 0 || snake.y != 0) {
                Snake snake2 = new Snake();
                snake2.x = 0;
                snake2.y = 0;
                snake2.removal = false;
                snake2.size = 0;
                snake2.reverse = false;
                this.mSnakes.add(0, snake2);
            }
        }

        /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<androidx.recyclerview.widget.DiffUtil$PostponedUpdate>, for r7v0, types: [java.util.List, java.util.List<androidx.recyclerview.widget.DiffUtil$PostponedUpdate>] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void dispatchAdditions(List<PostponedUpdate> list, ListUpdateCallback listUpdateCallback, int i, int i2, int i3) {
            if (!this.mDetectMoves) {
                listUpdateCallback.onInserted(i, i2);
                return;
            }
            for (int i4 = i2 - 1; i4 >= 0; i4--) {
                int i5 = i3 + i4;
                int i6 = this.mNewItemStatuses[i5] & 31;
                if (i6 == 0) {
                    listUpdateCallback.onInserted(i, 1);
                    for (PostponedUpdate postponedUpdate : list) {
                        postponedUpdate.currentPos++;
                    }
                } else if (i6 == 4 || i6 == 8) {
                    int i7 = this.mNewItemStatuses[i5] >> 5;
                    listUpdateCallback.onMoved(removePostponedUpdate(list, i7, true).currentPos, i);
                    if (i6 == 4) {
                        listUpdateCallback.onChanged(i, 1, this.mCallback.getChangePayload(i7, i5));
                    }
                } else if (i6 == 16) {
                    list.add(new PostponedUpdate(i5, i, false));
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("unknown flag for pos ");
                    sb.append(i5);
                    sb.append(" ");
                    sb.append(Long.toBinaryString((long) i6));
                    throw new IllegalStateException(sb.toString());
                }
            }
        }

        /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<androidx.recyclerview.widget.DiffUtil$PostponedUpdate>, for r9v0, types: [java.util.List, java.util.List<androidx.recyclerview.widget.DiffUtil$PostponedUpdate>] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void dispatchRemovals(List<PostponedUpdate> list, ListUpdateCallback listUpdateCallback, int i, int i2, int i3) {
            if (!this.mDetectMoves) {
                listUpdateCallback.onRemoved(i, i2);
                return;
            }
            for (int i4 = i2 - 1; i4 >= 0; i4--) {
                int i5 = i3 + i4;
                int i6 = this.mOldItemStatuses[i5] & 31;
                if (i6 == 0) {
                    listUpdateCallback.onRemoved(i + i4, 1);
                    for (PostponedUpdate postponedUpdate : list) {
                        postponedUpdate.currentPos--;
                    }
                } else if (i6 == 4 || i6 == 8) {
                    int i7 = this.mOldItemStatuses[i5] >> 5;
                    PostponedUpdate removePostponedUpdate = removePostponedUpdate(list, i7, false);
                    listUpdateCallback.onMoved(i + i4, removePostponedUpdate.currentPos - 1);
                    if (i6 == 4) {
                        listUpdateCallback.onChanged(removePostponedUpdate.currentPos - 1, 1, this.mCallback.getChangePayload(i5, i7));
                    }
                } else if (i6 == 16) {
                    list.add(new PostponedUpdate(i5, i + i4, true));
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("unknown flag for pos ");
                    sb.append(i5);
                    sb.append(" ");
                    sb.append(Long.toBinaryString((long) i6));
                    throw new IllegalStateException(sb.toString());
                }
            }
        }

        private void findAddition(int i, int i2, int i3) {
            if (this.mOldItemStatuses[i - 1] == 0) {
                findMatchingItem(i, i2, i3, false);
            }
        }

        private boolean findMatchingItem(int i, int i2, int i3, boolean z) {
            int i4;
            int i5;
            if (z) {
                i2--;
                i5 = i;
                i4 = i2;
            } else {
                i5 = i - 1;
                i4 = i5;
            }
            while (i3 >= 0) {
                Snake snake = (Snake) this.mSnakes.get(i3);
                int i6 = snake.x;
                int i7 = snake.size;
                int i8 = i6 + i7;
                int i9 = snake.y + i7;
                int i10 = 8;
                if (z) {
                    for (int i11 = i5 - 1; i11 >= i8; i11--) {
                        if (this.mCallback.areItemsTheSame(i11, i4)) {
                            if (!this.mCallback.areContentsTheSame(i11, i4)) {
                                i10 = 4;
                            }
                            this.mNewItemStatuses[i4] = (i11 << 5) | 16;
                            this.mOldItemStatuses[i11] = (i4 << 5) | i10;
                            return true;
                        }
                    }
                    continue;
                } else {
                    for (int i12 = i2 - 1; i12 >= i9; i12--) {
                        if (this.mCallback.areItemsTheSame(i4, i12)) {
                            if (!this.mCallback.areContentsTheSame(i4, i12)) {
                                i10 = 4;
                            }
                            int i13 = i - 1;
                            this.mOldItemStatuses[i13] = (i12 << 5) | 16;
                            this.mNewItemStatuses[i12] = (i13 << 5) | i10;
                            return true;
                        }
                    }
                    continue;
                }
                i5 = snake.x;
                i2 = snake.y;
                i3--;
            }
            return false;
        }

        private void findMatchingItems() {
            int i = this.mOldListSize;
            int i2 = this.mNewListSize;
            for (int size = this.mSnakes.size() - 1; size >= 0; size--) {
                Snake snake = (Snake) this.mSnakes.get(size);
                int i3 = snake.x;
                int i4 = snake.size;
                int i5 = i3 + i4;
                int i6 = snake.y + i4;
                if (this.mDetectMoves) {
                    while (i > i5) {
                        findAddition(i, i2, size);
                        i--;
                    }
                    while (i2 > i6) {
                        findRemoval(i, i2, size);
                        i2--;
                    }
                }
                for (int i7 = 0; i7 < snake.size; i7++) {
                    int i8 = snake.x + i7;
                    int i9 = snake.y + i7;
                    int i10 = this.mCallback.areContentsTheSame(i8, i9) ? 1 : 2;
                    this.mOldItemStatuses[i8] = (i9 << 5) | i10;
                    this.mNewItemStatuses[i9] = (i8 << 5) | i10;
                }
                i = snake.x;
                i2 = snake.y;
            }
        }

        private void findRemoval(int i, int i2, int i3) {
            if (this.mNewItemStatuses[i2 - 1] == 0) {
                findMatchingItem(i, i2, i3, true);
            }
        }

        private static PostponedUpdate removePostponedUpdate(List list, int i, boolean z) {
            int size = list.size() - 1;
            while (size >= 0) {
                PostponedUpdate postponedUpdate = (PostponedUpdate) list.get(size);
                if (postponedUpdate.posInOwnerList == i && postponedUpdate.removal == z) {
                    list.remove(size);
                    while (size < list.size()) {
                        PostponedUpdate postponedUpdate2 = (PostponedUpdate) list.get(size);
                        postponedUpdate2.currentPos += z ? 1 : -1;
                        size++;
                    }
                    return postponedUpdate;
                }
                size--;
            }
            return null;
        }

        public int convertNewPositionToOld(@IntRange(from = 0) int i) {
            if (i < 0 || i >= this.mNewListSize) {
                StringBuilder sb = new StringBuilder();
                sb.append("Index out of bounds - passed position = ");
                sb.append(i);
                sb.append(", new list size = ");
                sb.append(this.mNewListSize);
                throw new IndexOutOfBoundsException(sb.toString());
            }
            int i2 = this.mNewItemStatuses[i];
            if ((i2 & 31) == 0) {
                return -1;
            }
            return i2 >> 5;
        }

        public int convertOldPositionToNew(@IntRange(from = 0) int i) {
            if (i < 0 || i >= this.mOldListSize) {
                StringBuilder sb = new StringBuilder();
                sb.append("Index out of bounds - passed position = ");
                sb.append(i);
                sb.append(", old list size = ");
                sb.append(this.mOldListSize);
                throw new IndexOutOfBoundsException(sb.toString());
            }
            int i2 = this.mOldItemStatuses[i];
            if ((i2 & 31) == 0) {
                return -1;
            }
            return i2 >> 5;
        }

        public void dispatchUpdatesTo(@NonNull ListUpdateCallback listUpdateCallback) {
            BatchingListUpdateCallback batchingListUpdateCallback = listUpdateCallback instanceof BatchingListUpdateCallback ? (BatchingListUpdateCallback) listUpdateCallback : new BatchingListUpdateCallback(listUpdateCallback);
            ArrayList arrayList = new ArrayList();
            int i = this.mOldListSize;
            int i2 = this.mNewListSize;
            for (int size = this.mSnakes.size() - 1; size >= 0; size--) {
                Snake snake = (Snake) this.mSnakes.get(size);
                int i3 = snake.size;
                int i4 = snake.x + i3;
                int i5 = snake.y + i3;
                if (i4 < i) {
                    dispatchRemovals(arrayList, batchingListUpdateCallback, i4, i - i4, i4);
                }
                if (i5 < i2) {
                    dispatchAdditions(arrayList, batchingListUpdateCallback, i4, i2 - i5, i5);
                }
                for (int i6 = i3 - 1; i6 >= 0; i6--) {
                    int[] iArr = this.mOldItemStatuses;
                    int i7 = snake.x;
                    if ((iArr[i7 + i6] & 31) == 2) {
                        batchingListUpdateCallback.onChanged(i7 + i6, 1, this.mCallback.getChangePayload(i7 + i6, snake.y + i6));
                    }
                }
                i = snake.x;
                i2 = snake.y;
            }
            batchingListUpdateCallback.dispatchLastEvent();
        }

        public void dispatchUpdatesTo(@NonNull Adapter adapter) {
            dispatchUpdatesTo((ListUpdateCallback) new AdapterListUpdateCallback(adapter));
        }

        /* access modifiers changed from: 0000 */
        @VisibleForTesting
        public List getSnakes() {
            return this.mSnakes;
        }
    }

    public abstract class ItemCallback {
        public abstract boolean areContentsTheSame(@NonNull Object obj, @NonNull Object obj2);

        public abstract boolean areItemsTheSame(@NonNull Object obj, @NonNull Object obj2);

        @Nullable
        public Object getChangePayload(@NonNull Object obj, @NonNull Object obj2) {
            return null;
        }
    }

    class PostponedUpdate {
        int currentPos;
        int posInOwnerList;
        boolean removal;

        public PostponedUpdate(int i, int i2, boolean z) {
            this.posInOwnerList = i;
            this.currentPos = i2;
            this.removal = z;
        }
    }

    class Range {
        int newListEnd;
        int newListStart;
        int oldListEnd;
        int oldListStart;

        public Range() {
        }

        public Range(int i, int i2, int i3, int i4) {
            this.oldListStart = i;
            this.oldListEnd = i2;
            this.newListStart = i3;
            this.newListEnd = i4;
        }
    }

    class Snake {
        boolean removal;
        boolean reverse;
        int size;
        int x;
        int y;

        Snake() {
        }
    }

    private DiffUtil() {
    }

    @NonNull
    public static DiffResult calculateDiff(@NonNull Callback callback) {
        return calculateDiff(callback, true);
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x00ae  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00c7  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @NonNull
    public static DiffResult calculateDiff(@NonNull Callback callback, boolean z) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int oldListSize = callback.getOldListSize();
        int newListSize = callback.getNewListSize();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(new Range(0, oldListSize, 0, newListSize));
        int abs = Math.abs(oldListSize - newListSize) + oldListSize + newListSize;
        int i6 = abs * 2;
        int[] iArr = new int[i6];
        int[] iArr2 = new int[i6];
        ArrayList arrayList3 = new ArrayList();
        while (!arrayList2.isEmpty()) {
            Range range = (Range) arrayList2.remove(arrayList2.size() - 1);
            Snake diffPartial = diffPartial(callback, range.oldListStart, range.oldListEnd, range.newListStart, range.newListEnd, iArr, iArr2, abs);
            if (diffPartial != null) {
                if (diffPartial.size > 0) {
                    arrayList.add(diffPartial);
                }
                diffPartial.x += range.oldListStart;
                diffPartial.y += range.newListStart;
                Range range2 = arrayList3.isEmpty() ? new Range() : (Range) arrayList3.remove(arrayList3.size() - 1);
                range2.oldListStart = range.oldListStart;
                range2.newListStart = range.newListStart;
                if (diffPartial.reverse) {
                    i2 = diffPartial.x;
                } else if (diffPartial.removal) {
                    i2 = diffPartial.x - 1;
                } else {
                    range2.oldListEnd = diffPartial.x;
                    i = diffPartial.y - 1;
                    range2.newListEnd = i;
                    arrayList2.add(range2);
                    if (diffPartial.reverse) {
                        int i7 = diffPartial.x;
                        i4 = diffPartial.size;
                        i5 = i7 + i4;
                    } else if (diffPartial.removal) {
                        int i8 = diffPartial.x;
                        i4 = diffPartial.size;
                        i5 = i8 + i4 + 1;
                    } else {
                        int i9 = diffPartial.x;
                        int i10 = diffPartial.size;
                        range.oldListStart = i9 + i10;
                        i3 = diffPartial.y + i10 + 1;
                        range.newListStart = i3;
                        arrayList2.add(range);
                    }
                    range.oldListStart = i5;
                    i3 = diffPartial.y + i4;
                    range.newListStart = i3;
                    arrayList2.add(range);
                }
                range2.oldListEnd = i2;
                i = diffPartial.y;
                range2.newListEnd = i;
                arrayList2.add(range2);
                if (diffPartial.reverse) {
                }
                range.oldListStart = i5;
                i3 = diffPartial.y + i4;
                range.newListStart = i3;
                arrayList2.add(range);
            } else {
                arrayList3.add(range);
            }
        }
        Collections.sort(arrayList, SNAKE_COMPARATOR);
        DiffResult diffResult = new DiffResult(callback, arrayList, iArr, iArr2, z);
        return diffResult;
    }

    /* JADX WARNING: type inference failed for: r10v0 */
    /* JADX WARNING: type inference failed for: r10v1 */
    /* JADX WARNING: type inference failed for: r10v2 */
    /* JADX WARNING: type inference failed for: r5v3 */
    /* JADX WARNING: type inference failed for: r5v4 */
    /* JADX WARNING: type inference failed for: r10v5 */
    /* JADX WARNING: type inference failed for: r14v0, types: [boolean] */
    /* JADX WARNING: type inference failed for: r5v6 */
    /* JADX WARNING: type inference failed for: r14v1 */
    /* JADX WARNING: type inference failed for: r14v2 */
    /* JADX WARNING: type inference failed for: r14v5, types: [boolean] */
    /* JADX WARNING: type inference failed for: r10v7 */
    /* JADX WARNING: type inference failed for: r14v6 */
    /* JADX WARNING: type inference failed for: r14v7 */
    /* JADX WARNING: type inference failed for: r10v14 */
    /* JADX WARNING: type inference failed for: r10v15 */
    /* JADX WARNING: type inference failed for: r10v16 */
    /* JADX WARNING: type inference failed for: r5v16 */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0042, code lost:
        if (r1[r13 - 1] < r1[r13 + r5]) goto L_0x004d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00ba, code lost:
        if (r2[r13 - 1] < r2[r13 + 1]) goto L_0x00c7;
     */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r10v1
  assigns: []
  uses: []
  mth insns count: 161
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 8 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static Snake diffPartial(Callback callback, int i, int i2, int i3, int i4, int[] iArr, int[] iArr2, int i5) {
        ? r14;
        int i6;
        int i7;
        int i8;
        int i9;
        ? r142;
        int i10;
        int i11;
        Callback callback2 = callback;
        int[] iArr3 = iArr;
        int[] iArr4 = iArr2;
        int i12 = i2 - i;
        int i13 = i4 - i3;
        int i14 = 1;
        if (i12 < 1 || i13 < 1) {
            return null;
        }
        int i15 = i12 - i13;
        int i16 = ((i12 + i13) + 1) / 2;
        int i17 = (i5 - i16) - 1;
        int i18 = i5 + i16 + 1;
        ? r10 = 0;
        Arrays.fill(iArr3, i17, i18, 0);
        Arrays.fill(iArr4, i17 + i15, i18 + i15, i12);
        boolean z = i15 % 2 != 0;
        int i19 = 0;
        ? r102 = r10;
        while (i19 <= i16) {
            int i20 = -i19;
            int i21 = i20;
            ? r103 = r102;
            while (i21 <= i19) {
                if (i21 != i20) {
                    if (i21 != i19) {
                        int i22 = i5 + i21;
                    }
                    i10 = iArr3[(i5 + i21) - i14] + i14;
                    r142 = i14;
                    i11 = i10 - i21;
                    while (i10 < i12 && i11 < i13 && callback2.areItemsTheSame(i + i10, i3 + i11)) {
                        i10++;
                        i11++;
                    }
                    int i23 = i5 + i21;
                    iArr3[i23] = i10;
                    if (z || i21 < (i15 - i19) + 1 || i21 > (i15 + i19) - 1 || iArr3[i23] < iArr4[i23]) {
                        i21 += 2;
                        r103 = 0;
                        i14 = 1;
                    } else {
                        Snake snake = new Snake();
                        snake.x = iArr4[i23];
                        snake.y = snake.x - i21;
                        snake.size = iArr3[i23] - iArr4[i23];
                        snake.removal = r142;
                        snake.reverse = false;
                        return snake;
                    }
                }
                i10 = iArr3[i5 + i21 + i14];
                r142 = r103;
                i11 = i10 - i21;
                while (i10 < i12) {
                    i10++;
                    i11++;
                }
                int i232 = i5 + i21;
                iArr3[i232] = i10;
                if (z) {
                }
                i21 += 2;
                r103 = 0;
                i14 = 1;
            }
            ? r5 = r103;
            int i24 = i20;
            while (i24 <= i19) {
                int i25 = i24 + i15;
                if (i25 != i19 + i15) {
                    if (i25 != i20 + i15) {
                        int i26 = i5 + i25;
                        i9 = 1;
                    } else {
                        i9 = 1;
                    }
                    i6 = iArr4[(i5 + i25) + i9] - i9;
                    r14 = i9;
                    i7 = i6 - i25;
                    while (true) {
                        if (i6 > 0 && i7 > 0) {
                            i8 = i12;
                            if (!callback2.areItemsTheSame((i + i6) - 1, (i3 + i7) - 1)) {
                                break;
                            }
                            i6--;
                            i7--;
                            i12 = i8;
                        } else {
                            i8 = i12;
                        }
                    }
                    i8 = i12;
                    int i27 = i5 + i25;
                    iArr4[i27] = i6;
                    if (!z || i25 < i20 || i25 > i19 || iArr3[i27] < iArr4[i27]) {
                        i24 += 2;
                        i12 = i8;
                        r5 = 0;
                    } else {
                        Snake snake2 = new Snake();
                        snake2.x = iArr4[i27];
                        snake2.y = snake2.x - i25;
                        snake2.size = iArr3[i27] - iArr4[i27];
                        snake2.removal = r14;
                        snake2.reverse = true;
                        return snake2;
                    }
                } else {
                    i9 = 1;
                }
                i6 = iArr4[(i5 + i25) - i9];
                r14 = r5;
                i7 = i6 - i25;
                while (true) {
                    if (i6 > 0) {
                        break;
                    }
                    break;
                    i6--;
                    i7--;
                    i12 = i8;
                }
                i8 = i12;
                int i272 = i5 + i25;
                iArr4[i272] = i6;
                if (!z) {
                }
                i24 += 2;
                i12 = i8;
                r5 = 0;
            }
            i19++;
            i14 = 1;
            i12 = i12;
            r102 = 0;
        }
        throw new IllegalStateException("DiffUtil hit an unexpected case while trying to calculate the optimal path. Please make sure your data is not changing during the diff calculation.");
    }
}

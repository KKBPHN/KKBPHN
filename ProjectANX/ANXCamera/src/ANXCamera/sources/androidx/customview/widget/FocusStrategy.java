package androidx.customview.widget;

import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class FocusStrategy {

    public interface BoundsAdapter {
        void obtainBounds(Object obj, Rect rect);
    }

    public interface CollectionAdapter {
        Object get(Object obj, int i);

        int size(Object obj);
    }

    class SequentialComparator implements Comparator {
        private final BoundsAdapter mAdapter;
        private final boolean mIsLayoutRtl;
        private final Rect mTemp1 = new Rect();
        private final Rect mTemp2 = new Rect();

        SequentialComparator(boolean z, BoundsAdapter boundsAdapter) {
            this.mIsLayoutRtl = z;
            this.mAdapter = boundsAdapter;
        }

        public int compare(Object obj, Object obj2) {
            int i;
            int i2;
            Rect rect = this.mTemp1;
            Rect rect2 = this.mTemp2;
            this.mAdapter.obtainBounds(obj, rect);
            this.mAdapter.obtainBounds(obj2, rect2);
            int i3 = rect.top;
            int i4 = rect2.top;
            int i5 = -1;
            if (i3 < i4) {
                return -1;
            }
            if (i3 > i4) {
                return 1;
            }
            int i6 = rect.left;
            int i7 = rect2.left;
            if (i6 < i7) {
                if (this.mIsLayoutRtl) {
                    i5 = 1;
                }
                return i5;
            } else if (i6 > i7) {
                if (!this.mIsLayoutRtl) {
                    i5 = 1;
                }
                return i5;
            } else {
                int i8 = rect.bottom;
                int i9 = rect2.bottom;
                if (i8 < i9) {
                    return -1;
                }
                if (i8 > i9) {
                    return 1;
                }
                int i10 = rect.right;
                int i11 = rect2.right;
                if (i10 < i11) {
                    if (this.mIsLayoutRtl) {
                        i2 = 1;
                    }
                    return i2;
                } else if (i10 <= i11) {
                    return 0;
                } else {
                    if (!this.mIsLayoutRtl) {
                        i = 1;
                    }
                    return i;
                }
            }
        }
    }

    private FocusStrategy() {
    }

    private static boolean beamBeats(int i, @NonNull Rect rect, @NonNull Rect rect2, @NonNull Rect rect3) {
        boolean beamsOverlap = beamsOverlap(i, rect, rect2);
        if (beamsOverlap(i, rect, rect3) || !beamsOverlap) {
            return false;
        }
        boolean z = true;
        if (!isToDirectionOf(i, rect, rect3)) {
            return true;
        }
        if (!(i == 17 || i == 66 || majorAxisDistance(i, rect, rect2) < majorAxisDistanceToFarEdge(i, rect, rect3))) {
            z = false;
        }
        return z;
    }

    private static boolean beamsOverlap(int i, @NonNull Rect rect, @NonNull Rect rect2) {
        boolean z = true;
        if (i != 17) {
            if (i != 33) {
                if (i != 66) {
                    if (i != 130) {
                        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                    }
                }
            }
            if (rect2.right < rect.left || rect2.left > rect.right) {
                z = false;
            }
            return z;
        }
        if (rect2.bottom < rect.top || rect2.top > rect.bottom) {
            z = false;
        }
        return z;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x004d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static Object findNextFocusInAbsoluteDirection(@NonNull Object obj, @NonNull CollectionAdapter collectionAdapter, @NonNull BoundsAdapter boundsAdapter, @Nullable Object obj2, @NonNull Rect rect, int i) {
        int size;
        int i2;
        int i3;
        Rect rect2 = new Rect(rect);
        if (i != 17) {
            if (i == 33) {
                i3 = rect.height() + 1;
            } else if (i == 66) {
                i2 = -(rect.width() + 1);
            } else if (i == 130) {
                i3 = -(rect.height() + 1);
            } else {
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }
            rect2.offset(0, i3);
            Object obj3 = null;
            size = collectionAdapter.size(obj);
            Rect rect3 = new Rect();
            for (int i4 = 0; i4 < size; i4++) {
                Object obj4 = collectionAdapter.get(obj, i4);
                if (obj4 != obj2) {
                    boundsAdapter.obtainBounds(obj4, rect3);
                    if (isBetterCandidate(i, rect, rect3, rect2)) {
                        rect2.set(rect3);
                        obj3 = obj4;
                    }
                }
            }
            return obj3;
        }
        i2 = rect.width() + 1;
        rect2.offset(i2, 0);
        Object obj32 = null;
        size = collectionAdapter.size(obj);
        Rect rect32 = new Rect();
        while (i4 < size) {
        }
        return obj32;
    }

    public static Object findNextFocusInRelativeDirection(@NonNull Object obj, @NonNull CollectionAdapter collectionAdapter, @NonNull BoundsAdapter boundsAdapter, @Nullable Object obj2, int i, boolean z, boolean z2) {
        int size = collectionAdapter.size(obj);
        ArrayList arrayList = new ArrayList(size);
        for (int i2 = 0; i2 < size; i2++) {
            arrayList.add(collectionAdapter.get(obj, i2));
        }
        Collections.sort(arrayList, new SequentialComparator(z, boundsAdapter));
        if (i == 1) {
            return getPreviousFocusable(obj2, arrayList, z2);
        }
        if (i == 2) {
            return getNextFocusable(obj2, arrayList, z2);
        }
        throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD}.");
    }

    private static Object getNextFocusable(Object obj, ArrayList arrayList, boolean z) {
        int size = arrayList.size();
        int lastIndexOf = (obj == null ? -1 : arrayList.lastIndexOf(obj)) + 1;
        if (lastIndexOf < size) {
            return arrayList.get(lastIndexOf);
        }
        if (!z || size <= 0) {
            return null;
        }
        return arrayList.get(0);
    }

    private static Object getPreviousFocusable(Object obj, ArrayList arrayList, boolean z) {
        int size = arrayList.size();
        int indexOf = (obj == null ? size : arrayList.indexOf(obj)) - 1;
        if (indexOf >= 0) {
            return arrayList.get(indexOf);
        }
        if (!z || size <= 0) {
            return null;
        }
        return arrayList.get(size - 1);
    }

    private static int getWeightedDistanceFor(int i, int i2) {
        return (i * 13 * i) + (i2 * i2);
    }

    private static boolean isBetterCandidate(int i, @NonNull Rect rect, @NonNull Rect rect2, @NonNull Rect rect3) {
        boolean z = false;
        if (!isCandidate(rect, rect2, i)) {
            return false;
        }
        if (!isCandidate(rect, rect3, i) || beamBeats(i, rect, rect2, rect3)) {
            return true;
        }
        if (beamBeats(i, rect, rect3, rect2)) {
            return false;
        }
        if (getWeightedDistanceFor(majorAxisDistance(i, rect, rect2), minorAxisDistance(i, rect, rect2)) < getWeightedDistanceFor(majorAxisDistance(i, rect, rect3), minorAxisDistance(i, rect, rect3))) {
            z = true;
        }
        return z;
    }

    private static boolean isCandidate(@NonNull Rect rect, @NonNull Rect rect2, int i) {
        boolean z;
        boolean z2;
        boolean z3 = true;
        if (i == 17) {
            int i2 = rect.right;
            int i3 = rect2.right;
            if ((i2 <= i3 && rect.left < i3) || rect.left <= rect2.left) {
                z = false;
            }
            return z;
        } else if (i == 33) {
            int i4 = rect.bottom;
            int i5 = rect2.bottom;
            if ((i4 <= i5 && rect.top < i5) || rect.top <= rect2.top) {
                z2 = false;
            }
            return z2;
        } else if (i == 66) {
            int i6 = rect.left;
            int i7 = rect2.left;
            if ((i6 >= i7 && rect.right > i7) || rect.right >= rect2.right) {
                z3 = false;
            }
            return z3;
        } else if (i == 130) {
            int i8 = rect.top;
            int i9 = rect2.top;
            if ((i8 >= i9 && rect.bottom > i9) || rect.bottom >= rect2.bottom) {
                z3 = false;
            }
            return z3;
        } else {
            throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    private static boolean isToDirectionOf(int i, @NonNull Rect rect, @NonNull Rect rect2) {
        boolean z;
        boolean z2;
        boolean z3 = true;
        if (i == 17) {
            if (rect.left < rect2.right) {
                z = false;
            }
            return z;
        } else if (i == 33) {
            if (rect.top < rect2.bottom) {
                z2 = false;
            }
            return z2;
        } else if (i == 66) {
            if (rect.right > rect2.left) {
                z3 = false;
            }
            return z3;
        } else if (i == 130) {
            if (rect.bottom > rect2.top) {
                z3 = false;
            }
            return z3;
        } else {
            throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    private static int majorAxisDistance(int i, @NonNull Rect rect, @NonNull Rect rect2) {
        return Math.max(0, majorAxisDistanceRaw(i, rect, rect2));
    }

    private static int majorAxisDistanceRaw(int i, @NonNull Rect rect, @NonNull Rect rect2) {
        int i2;
        int i3;
        if (i == 17) {
            i2 = rect.left;
            i3 = rect2.right;
        } else if (i == 33) {
            i2 = rect.top;
            i3 = rect2.bottom;
        } else if (i == 66) {
            i2 = rect2.left;
            i3 = rect.right;
        } else if (i == 130) {
            i2 = rect2.top;
            i3 = rect.bottom;
        } else {
            throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
        return i2 - i3;
    }

    private static int majorAxisDistanceToFarEdge(int i, @NonNull Rect rect, @NonNull Rect rect2) {
        return Math.max(1, majorAxisDistanceToFarEdgeRaw(i, rect, rect2));
    }

    private static int majorAxisDistanceToFarEdgeRaw(int i, @NonNull Rect rect, @NonNull Rect rect2) {
        int i2;
        int i3;
        if (i == 17) {
            i2 = rect.left;
            i3 = rect2.left;
        } else if (i == 33) {
            i2 = rect.top;
            i3 = rect2.top;
        } else if (i == 66) {
            i2 = rect2.right;
            i3 = rect.right;
        } else if (i == 130) {
            i2 = rect2.bottom;
            i3 = rect.bottom;
        } else {
            throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
        return i2 - i3;
    }

    private static int minorAxisDistance(int i, @NonNull Rect rect, @NonNull Rect rect2) {
        int height;
        int i2;
        int height2;
        if (i != 17) {
            if (i != 33) {
                if (i != 66) {
                    if (i != 130) {
                        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                    }
                }
            }
            height = rect.left + (rect.width() / 2);
            i2 = rect2.left;
            height2 = rect2.width();
            return Math.abs(height - (i2 + (height2 / 2)));
        }
        height = rect.top + (rect.height() / 2);
        i2 = rect2.top;
        height2 = rect2.height();
        return Math.abs(height - (i2 + (height2 / 2)));
    }
}

package com.miui.internal.variable;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import com.miui.internal.util.ClassProxy;
import com.miui.internal.util.async.ConcurrentWeakHashMap;
import miui.R;

public abstract class Android_View_View_class extends ClassProxy implements IManagedClassProxy {
    private static final ConcurrentWeakHashMap sCustomizedValues = new ConcurrentWeakHashMap();
    private static final SparseIntArray sStateAttributeIndexes = new SparseIntArray();
    private static final SparseArray sViewStates = new SparseArray();

    public class CustomizedValue {
        public boolean mHasInitViewSequenceStates = false;
        public int mHorizontalState = 0;
        public boolean mTagChildrenSequenceState = false;
        public int mVerticalState = 0;
    }

    public class Factory extends AbsClassFactory {
        private Android_View_View_class Android_View_View_class;

        class Holder {
            static final Factory INSTANCE = new Factory();

            private Holder() {
            }
        }

        private Factory() {
            this.Android_View_View_class = (Android_View_View_class) create("Android_View_View_class");
        }

        public static Factory getInstance() {
            return Holder.INSTANCE;
        }

        public Android_View_View_class get() {
            return this.Android_View_View_class;
        }
    }

    static {
        sStateAttributeIndexes.put(16842908, 1);
        SparseIntArray sparseIntArray = sStateAttributeIndexes;
        sparseIntArray.put(16842910, 1 << sparseIntArray.size());
        SparseIntArray sparseIntArray2 = sStateAttributeIndexes;
        sparseIntArray2.put(16842911, 1 << sparseIntArray2.size());
        SparseIntArray sparseIntArray3 = sStateAttributeIndexes;
        sparseIntArray3.put(16842912, 1 << sparseIntArray3.size());
        SparseIntArray sparseIntArray4 = sStateAttributeIndexes;
        sparseIntArray4.put(16842913, 1 << sparseIntArray4.size());
        SparseIntArray sparseIntArray5 = sStateAttributeIndexes;
        sparseIntArray5.put(16842914, 1 << sparseIntArray5.size());
        SparseIntArray sparseIntArray6 = sStateAttributeIndexes;
        sparseIntArray6.put(16842915, 1 << sparseIntArray6.size());
        SparseIntArray sparseIntArray7 = sStateAttributeIndexes;
        sparseIntArray7.put(16842916, 1 << sparseIntArray7.size());
        SparseIntArray sparseIntArray8 = sStateAttributeIndexes;
        sparseIntArray8.put(16842917, 1 << sparseIntArray8.size());
        SparseIntArray sparseIntArray9 = sStateAttributeIndexes;
        sparseIntArray9.put(16842918, 1 << sparseIntArray9.size());
        SparseIntArray sparseIntArray10 = sStateAttributeIndexes;
        sparseIntArray10.put(16842919, 1 << sparseIntArray10.size());
        SparseIntArray sparseIntArray11 = sStateAttributeIndexes;
        sparseIntArray11.put(16842921, 1 << sparseIntArray11.size());
        SparseIntArray sparseIntArray12 = sStateAttributeIndexes;
        sparseIntArray12.put(16843518, 1 << sparseIntArray12.size());
        sStateAttributeIndexes.put(R.attr.state_first_h, 1 << sStateAttributeIndexes.size());
        sStateAttributeIndexes.put(R.attr.state_middle_h, 1 << sStateAttributeIndexes.size());
        sStateAttributeIndexes.put(R.attr.state_last_h, 1 << sStateAttributeIndexes.size());
        sStateAttributeIndexes.put(R.attr.state_single_h, 1 << sStateAttributeIndexes.size());
        sStateAttributeIndexes.put(R.attr.state_first_v, 1 << sStateAttributeIndexes.size());
        sStateAttributeIndexes.put(R.attr.state_middle_v, 1 << sStateAttributeIndexes.size());
        sStateAttributeIndexes.put(R.attr.state_last_v, 1 << sStateAttributeIndexes.size());
        sStateAttributeIndexes.put(R.attr.state_single_v, 1 << sStateAttributeIndexes.size());
    }

    public Android_View_View_class() {
        super(View.class);
    }

    private static synchronized int getIndexOfStates(int i) {
        int i2;
        synchronized (Android_View_View_class.class) {
            i2 = 0;
            if (i != 0) {
                i2 = sStateAttributeIndexes.get(i, 0);
                if (i2 == 0) {
                    if (sStateAttributeIndexes.size() < 32) {
                        i2 = 1 << sStateAttributeIndexes.size();
                        sStateAttributeIndexes.put(i, i2);
                    } else {
                        throw new IllegalArgumentException("State attribute cannot exceed 32!");
                    }
                }
            }
        }
        return i2;
    }

    private static int getIndexOfStates(int[] iArr) {
        if (iArr == null) {
            return 0;
        }
        int i = 0;
        for (int indexOfStates : iArr) {
            i |= getIndexOfStates(indexOfStates);
        }
        return i;
    }

    public abstract Bitmap createSnapshot(View view, Config config, int i, boolean z);

    /* access modifiers changed from: protected */
    public CustomizedValue getCustomizedValue(View view) {
        CustomizedValue customizedValue = (CustomizedValue) sCustomizedValues.get(view);
        if (customizedValue != null) {
            return customizedValue;
        }
        CustomizedValue customizedValue2 = new CustomizedValue();
        sCustomizedValues.put(view, customizedValue2);
        return customizedValue2;
    }

    public int getHorizontalState(View view) {
        return getCustomizedValue(view).mHorizontalState;
    }

    public int getVerticalState(View view) {
        return getCustomizedValue(view).mVerticalState;
    }

    public boolean hasInitViewSequenceStates(View view) {
        return getCustomizedValue(view).mHasInitViewSequenceStates;
    }

    /* access modifiers changed from: protected */
    public int[] onCreateDrawableState(View view, int[] iArr, int i) {
        int[] iArr2;
        int horizontalState = getHorizontalState(view);
        int verticalState = getVerticalState(view);
        if (horizontalState == 0 || verticalState == 0) {
            iArr2 = iArr;
        } else {
            int indexOfStates = getIndexOfStates(iArr) | getIndexOfStates(horizontalState) | getIndexOfStates(verticalState);
            synchronized (Android_View_View_class.class) {
                iArr2 = (int[]) sViewStates.get(indexOfStates);
            }
            if (iArr2 == null) {
                int length = iArr != null ? iArr.length : 0;
                iArr2 = new int[(length + 2)];
                if (length != 0) {
                    System.arraycopy(iArr, 0, iArr2, 0, length);
                }
                iArr2[length] = horizontalState;
                iArr2[length + 1] = verticalState;
                synchronized (Android_View_View_class.class) {
                    if (sViewStates.get(indexOfStates) == null) {
                        sViewStates.put(indexOfStates, iArr2);
                    }
                }
            }
        }
        if (i == 0) {
            return iArr2;
        }
        if (iArr2 == null) {
            return new int[i];
        }
        int[] iArr3 = new int[(iArr2.length + i)];
        System.arraycopy(iArr2, 0, iArr3, 0, iArr2.length);
        return iArr3;
    }

    public abstract void relayout(View view);

    public void setHasInitViewSequenceStates(View view, boolean z) {
        getCustomizedValue(view).mHasInitViewSequenceStates = z;
    }

    public void setHorizontalState(View view, int i) {
        getCustomizedValue(view).mHorizontalState = i;
    }

    public abstract void setImportantForAccessibilityNoHideDescendants(View view);

    public abstract void setLeftDirectly(View view, int i);

    public abstract void setRightDirectly(View view, int i);

    public abstract void setScrollXDirectly(View view, int i);

    public abstract void setScrollYDirectly(View view, int i);

    public void setVerticalState(View view, int i) {
        getCustomizedValue(view).mVerticalState = i;
    }
}

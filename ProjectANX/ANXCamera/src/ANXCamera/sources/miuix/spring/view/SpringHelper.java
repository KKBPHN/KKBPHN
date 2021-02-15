package miuix.spring.view;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class SpringHelper {
    private AxisHandler mHorizontal = new AxisHandler(0) {
        /* access modifiers changed from: protected */
        public boolean canScroll() {
            return SpringHelper.this.canScrollHorizontally();
        }

        /* access modifiers changed from: protected */
        public int getSize() {
            return SpringHelper.this.getWidth();
        }

        /* access modifiers changed from: 0000 */
        public void onFlingReachEdge() {
            SpringHelper.this.vibrate();
        }
    };
    private AxisHandler mVertical = new AxisHandler(1) {
        /* access modifiers changed from: protected */
        public boolean canScroll() {
            return SpringHelper.this.canScrollVertically();
        }

        /* access modifiers changed from: protected */
        public int getSize() {
            return SpringHelper.this.getHeight();
        }

        /* access modifiers changed from: 0000 */
        public void onFlingReachEdge() {
            SpringHelper.this.vibrate();
        }
    };

    abstract class AxisHandler {
        private static final float DRAG_RATE = 0.5f;
        private static final float INVERSE_DRAG_RATE = 2.0f;
        private static final float SQUARE1 = 0.33333334f;
        private static final float SQUARE2 = 0.6666667f;
        float mAllDistance;
        int mAmount;
        int mAxis;
        float mDistance;

        AxisHandler(int i) {
            this.mAxis = i;
        }

        private float obtainSpringBackDistance(float f) {
            int size = getSize();
            if (size == 0) {
                return Math.abs(f) * 0.5f;
            }
            float f2 = (float) size;
            double min = (double) Math.min(Math.abs(f) / f2, 1.0f);
            return ((float) (((Math.pow(min, 3.0d) / 3.0d) - Math.pow(min, 2.0d)) + min)) * f2;
        }

        private float overScrollWeight() {
            float f = (float) (-Math.pow((double) (Math.abs(((float) this.mAmount) / ((float) getSize())) - 1.0f), 3.0d));
            if (f < 0.0f) {
                f = 0.0f;
            }
            return f / 1.5f;
        }

        private void pull(int i, int[] iArr, boolean z) {
            if (i != 0 && canScroll()) {
                float f = (float) i;
                this.mAllDistance += f;
                if (z) {
                    this.mDistance = Math.signum(this.mAllDistance) * obtainSpringBackDistance(Math.abs(this.mAllDistance));
                } else {
                    if (this.mDistance == 0.0f) {
                        onFlingReachEdge();
                    }
                    this.mDistance += f;
                    this.mAllDistance = Math.signum(this.mDistance) * unObtainSpringBackDistance(Math.abs(this.mDistance));
                }
                int i2 = this.mAxis;
                iArr[i2] = iArr[i2] + i;
            }
        }

        private int release(int i, int[] iArr, boolean z) {
            float f = this.mDistance;
            float f2 = this.mAllDistance;
            float signum = Math.signum(f);
            this.mAllDistance += (float) i;
            if (z) {
                this.mDistance = Math.signum(this.mAllDistance) * obtainSpringBackDistance(Math.abs(this.mAllDistance));
                int i2 = this.mAxis;
                iArr[i2] = iArr[i2] + (i - i);
            }
            int i3 = (int) (this.mDistance + (this.mAllDistance - f2));
            float f3 = (float) i3;
            if (signum * f3 >= 0.0f) {
                if (!z) {
                    this.mDistance = f3;
                }
                iArr[this.mAxis] = i;
            } else {
                this.mDistance = 0.0f;
                int i4 = this.mAxis;
                iArr[i4] = (int) (((float) iArr[i4]) + f);
            }
            if (this.mDistance == 0.0f) {
                this.mAllDistance = 0.0f;
            }
            if (!z) {
                this.mAllDistance = Math.signum(this.mDistance) * unObtainSpringBackDistance(Math.abs(this.mDistance));
            }
            return i3;
        }

        private float unObtainSpringBackDistance(float f) {
            int size = getSize();
            if (size == 0) {
                return Math.abs(f) * 2.0f;
            }
            float f2 = (float) size;
            if (Math.abs(f) / f2 > SQUARE1) {
                return f * 3.0f;
            }
            double d = (double) size;
            return (float) (d - (Math.pow(d, 0.6666666865348816d) * Math.pow((double) (f2 - (Math.abs(f) * 3.0f)), 0.3333333432674408d)));
        }

        public abstract boolean canScroll();

        public abstract int getSize();

        /* access modifiers changed from: 0000 */
        public boolean handleNestedPreScroll(@NonNull int[] iArr, @NonNull int[] iArr2, boolean z) {
            int i = iArr[this.mAxis];
            if (i != 0 && canScroll()) {
                float f = this.mDistance;
                if (f == 0.0f || Integer.signum((int) f) * i > 0) {
                    return false;
                }
                iArr[this.mAxis] = release(i, iArr2, z);
                return true;
            }
            return false;
        }

        /* access modifiers changed from: 0000 */
        public void handleNestedScroll(int i, @Nullable int[] iArr, int i2, @NonNull int[] iArr2) {
            if (SpringHelper.this.springAvailable()) {
                pull(i, iArr2, i2 == 0);
            }
        }

        public abstract void onFlingReachEdge();
    }

    public abstract boolean canScrollHorizontally();

    public abstract boolean canScrollVertically();

    public abstract boolean dispatchNestedPreScroll(int i, int i2, @Nullable int[] iArr, @Nullable int[] iArr2, int i3);

    public abstract void dispatchNestedScroll(int i, int i2, int i3, int i4, @Nullable int[] iArr, int i5, @Nullable int[] iArr2);

    public abstract int getHeight();

    public int getHorizontalDistance() {
        return (int) this.mHorizontal.mDistance;
    }

    public int getVerticalDistance() {
        return (int) this.mVertical.mDistance;
    }

    public abstract int getWidth();

    public boolean handleNestedPreScroll(int i, int i2, @Nullable int[] iArr, @Nullable int[] iArr2, int i3) {
        boolean z;
        int i4;
        int i5;
        int[] iArr3 = {0, 0};
        if (springAvailable()) {
            boolean z2 = i3 == 0;
            int[] iArr4 = {i, i2};
            boolean handleNestedPreScroll = this.mVertical.handleNestedPreScroll(iArr4, iArr3, z2) | this.mHorizontal.handleNestedPreScroll(iArr4, iArr3, z2);
            i4 = iArr4[0];
            i5 = iArr4[1];
            z = handleNestedPreScroll;
        } else {
            i4 = i;
            i5 = i2;
            z = false;
        }
        if (z) {
            i4 -= iArr3[0];
            i5 -= iArr3[1];
        }
        boolean dispatchNestedPreScroll = dispatchNestedPreScroll(i4, i5, iArr, iArr2, i3) | z;
        if (iArr != null) {
            iArr[0] = iArr[0] + iArr3[0];
            iArr[1] = iArr[1] + iArr3[1];
        }
        return dispatchNestedPreScroll;
    }

    public void handleNestedScroll(int i, int i2, int i3, int i4, @Nullable int[] iArr, int i5, @Nullable int[] iArr2) {
        if (iArr2 == null) {
            iArr2 = new int[]{0, 0};
        }
        dispatchNestedScroll(i, i2, i3, i4, iArr, i5, iArr2);
        int i6 = i3 - iArr2[0];
        int i7 = i4 - iArr2[1];
        if (i6 != 0 || i7 != 0) {
            this.mHorizontal.handleNestedScroll(i6, iArr, i5, iArr2);
            this.mVertical.handleNestedScroll(i7, iArr, i5, iArr2);
        }
    }

    public abstract boolean springAvailable();

    @Keep
    public abstract void vibrate();
}

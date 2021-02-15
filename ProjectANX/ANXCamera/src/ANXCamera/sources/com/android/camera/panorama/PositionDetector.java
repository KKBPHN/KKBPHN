package com.android.camera.panorama;

import android.graphics.Rect;
import android.graphics.RectF;
import android.view.ViewGroup;
import com.android.camera.Util;
import com.android.camera.log.Log;
import com.android.camera.panorama.MorphoPanoramaGP3.InitParam;

public class PositionDetector {
    public static final int COMPLETED = 1;
    public static final int ERROR_IDLE = -1;
    public static final int ERROR_OUT_OF_RANGE = -3;
    public static final int ERROR_REVERSE = -2;
    private static final int IDLE_THRES_RATIO = 2;
    private static final long IDLE_TIME = 3000000000L;
    public static final int OK = 0;
    private static final float PREVIEW_LONG_SIDE_CROP_RATIO = 0.8f;
    private static final int REVERSE_THRES_RATIO = 7;
    private static final int SPEED_CHECK_CONTINUOUSLY_TIMES = 5;
    private static final int SPEED_CHECK_IGNORE_TIMES = 15;
    private static final int SPEED_CHECK_MODE = 1;
    private static final int SPEED_CHECK_MODE_AVERAGE = 1;
    private static final String TAG = "PositionDetector";
    private static final double TOO_FAST_THRES_RATIO = 0.8d;
    private static final double TOO_SLOW_THRES_RATIO = 0.05d;
    public static final int WARNING_TOO_FAST = 2;
    public static final int WARNING_TOO_SLOW = 3;
    private long count;
    private volatile double cur_x;
    private volatile double cur_y;
    private final int direction;
    private final RectF frame_rect = new RectF();
    private RectF idle_rect = null;
    private long idle_start_time;
    private double idle_thres;
    private int mCameraOrientation;
    private final DiffManager mDiffManager = new DiffManager();
    private InitParam mInitParam;
    private boolean mIsFrontCamera;
    private Rect mPreviewFrameRect = new Rect();
    private int mPreviewHeight;
    private int mPreviewViewHeight;
    private int mPreviewViewWidth;
    private int mPreviewWidth;
    private final int output_height;
    private final int output_width;
    private double peak;
    private double prev_x;
    private double prev_y;
    private boolean reset_idle_timer;
    private double reverse_thres;
    private double reverse_thres2;
    private int too_fast_count;
    private double too_fast_thres;
    private int too_slow_count;
    private double too_slow_thres;

    class DiffManager {
        private static final int NUM = 5;
        private int add_num;
        private double ave;
        private int index;
        private final double[] pos = new double[5];

        public DiffManager() {
            clear();
        }

        private void calc() {
            double d = 0.0d;
            int i = 0;
            while (true) {
                int i2 = this.add_num;
                if (i < i2) {
                    d += this.pos[i];
                    i++;
                } else {
                    this.ave = d / ((double) i2);
                    return;
                }
            }
        }

        public void add(double d) {
            double[] dArr = this.pos;
            int i = this.index;
            dArr[i] = d;
            this.index = i + 1;
            if (this.index >= 5) {
                this.index = 0;
            }
            int i2 = this.add_num;
            if (i2 < 5) {
                this.add_num = i2 + 1;
            }
            calc();
        }

        public void clear() {
            for (int i = 0; i < 5; i++) {
                this.pos[i] = 0.0d;
            }
            this.index = 0;
            this.add_num = 0;
        }

        public double getDiff() {
            return this.ave;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0086, code lost:
        if (((r7 + r10) % defpackage.m.cQ) != 180) goto L_0x009e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0098, code lost:
        if (((r7 + r10) % defpackage.m.cQ) != 180) goto L_0x009b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x00bf, code lost:
        if (((r7 + r10) % defpackage.m.cQ) != 180) goto L_0x00d7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x00d1, code lost:
        if (((r7 + r10) % defpackage.m.cQ) != 180) goto L_0x00d4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public PositionDetector(InitParam initParam, ViewGroup viewGroup, boolean z, int i, int i2, int i3, int i4, int i5, int i6) {
        double d;
        this.mInitParam = initParam;
        viewGroup.getGlobalVisibleRect(this.mPreviewFrameRect);
        this.mPreviewViewWidth = viewGroup.getWidth();
        this.mPreviewViewHeight = viewGroup.getHeight();
        this.mIsFrontCamera = z;
        this.mCameraOrientation = i;
        this.mPreviewWidth = i2;
        this.mPreviewHeight = i3;
        this.count = 0;
        this.direction = i4;
        this.output_width = i5;
        this.output_height = i6;
        this.reset_idle_timer = true;
        this.too_fast_count = 0;
        this.too_slow_count = 0;
        this.prev_y = 0.0d;
        this.prev_x = 0.0d;
        this.cur_y = 0.0d;
        this.cur_x = 0.0d;
        int i7 = this.direction;
        if (i7 == 0) {
            int i8 = this.mInitParam.output_rotation;
            int i9 = this.mCameraOrientation;
            if ((i8 + i9) % m.cQ != 90) {
            }
            this.peak = (double) i5;
            float f = (float) i5;
            this.reverse_thres = (double) (0.07f * f);
            this.reverse_thres2 = (double) (0.8f * f);
            this.idle_thres = (double) (f * 0.02f);
            d = (double) i5;
            this.too_slow_thres = 5.0E-4d * d;
            this.too_fast_thres = d * 0.008d;
        } else if (i7 != 1) {
            if (i7 == 2) {
                int i10 = this.mInitParam.output_rotation;
                int i11 = this.mCameraOrientation;
                if ((i10 + i11) % m.cQ != 90) {
                }
                this.peak = (double) i6;
                float f2 = (float) i6;
                this.reverse_thres = (double) (0.07f * f2);
                this.reverse_thres2 = (double) (0.8f * f2);
                this.idle_thres = (double) (f2 * 0.02f);
                d = (double) i6;
                this.too_slow_thres = 5.0E-4d * d;
                this.too_fast_thres = d * 0.008d;
            } else if (i7 == 3) {
                int i12 = this.mInitParam.output_rotation;
                int i13 = this.mCameraOrientation;
                if ((i12 + i13) % m.cQ != 90) {
                }
            } else {
                return;
            }
            this.peak = 0.0d;
            float f22 = (float) i6;
            this.reverse_thres = (double) (0.07f * f22);
            this.reverse_thres2 = (double) (0.8f * f22);
            this.idle_thres = (double) (f22 * 0.02f);
            d = (double) i6;
            this.too_slow_thres = 5.0E-4d * d;
            this.too_fast_thres = d * 0.008d;
        } else {
            int i14 = this.mInitParam.output_rotation;
            int i15 = this.mCameraOrientation;
            if ((i14 + i15) % m.cQ != 90) {
            }
        }
        this.peak = 0.0d;
        float f3 = (float) i5;
        this.reverse_thres = (double) (0.07f * f3);
        this.reverse_thres2 = (double) (0.8f * f3);
        this.idle_thres = (double) (f3 * 0.02f);
        d = (double) i5;
        this.too_slow_thres = 5.0E-4d * d;
        this.too_fast_thres = d * 0.008d;
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0044  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x004a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int checkSpeed() {
        double d;
        double d2;
        int i = this.direction;
        int i2 = 3;
        if (i == 2 || i == 3) {
            d2 = this.cur_y;
            d = this.prev_y;
        } else {
            d2 = this.cur_x;
            d = this.prev_x;
        }
        this.mDiffManager.add(Math.abs(d2 - d));
        if (15 < this.count) {
            if (this.mDiffManager.getDiff() >= this.too_slow_thres) {
                if (this.mDiffManager.getDiff() > this.too_fast_thres) {
                    i2 = 2;
                }
            }
            if (this.too_slow_count > 0) {
                this.too_slow_count = 0;
            }
            if (this.too_fast_count > 0) {
                this.too_fast_count = 0;
            }
            return i2;
        }
        i2 = 0;
        if (this.too_slow_count > 0) {
        }
        if (this.too_fast_count > 0) {
        }
        return i2;
    }

    private boolean isComplete() {
        double d;
        int i;
        boolean z;
        boolean z2;
        int i2 = this.direction;
        if (i2 == 2 || i2 == 3) {
            d = this.cur_y;
            i = this.output_height;
        } else {
            d = this.cur_x;
            i = this.output_width;
        }
        int i3 = this.mPreviewWidth / 2;
        int i4 = this.direction;
        boolean z3 = false;
        if (i4 == 1 || i4 == 3) {
            int i5 = this.mInitParam.output_rotation;
            if (i5 == 90 || i5 == 0) {
                if (d > ((double) (i - (i3 / 2)))) {
                    z = true;
                }
                return z;
            }
            if (d < ((double) (i3 / 2))) {
                z2 = true;
            }
            return z2;
        }
        int i6 = this.mInitParam.output_rotation;
        if (i6 == 90 || i6 == 0) {
            if (d < ((double) (i3 / 2))) {
                z3 = true;
            }
            return z3;
        }
        if (d > ((double) (i - (i3 / 2)))) {
            z3 = true;
        }
        return z3;
    }

    private boolean isIdle() {
        long nanoTime = System.nanoTime();
        if (this.reset_idle_timer) {
            this.reset_idle_timer = false;
            this.idle_start_time = nanoTime;
        }
        if (this.idle_rect == null) {
            double d = this.idle_thres / 2.0d;
            this.idle_rect = new RectF((float) (this.cur_x - d), (float) (this.cur_y - d), (float) (this.cur_x + d), (float) (this.cur_y + d));
        }
        if (IDLE_TIME < nanoTime - this.idle_start_time) {
            return true;
        }
        if (!this.idle_rect.contains((float) this.cur_x, (float) this.cur_y)) {
            this.reset_idle_timer = true;
            this.idle_rect = null;
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0054, code lost:
        if (((r7 + r12) % defpackage.m.cQ) != 270) goto L_0x006a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0067, code lost:
        if (((r7 + r12) % defpackage.m.cQ) != 180) goto L_0x006a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x006a, code lost:
        r7 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0090, code lost:
        if (r5 != 3) goto L_0x009b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00af, code lost:
        if (r5 != 3) goto L_0x009b;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isReverse() {
        double d;
        double d2;
        int i;
        boolean z;
        StringBuilder sb = new StringBuilder();
        sb.append("cur_x = ");
        sb.append(this.cur_x);
        sb.append(", cur_y = ");
        sb.append(this.cur_y);
        Log.v(TAG, sb.toString());
        int i2 = this.direction;
        if (i2 == 2 || i2 == 3) {
            d2 = this.cur_y;
            d = this.prev_y;
            i = this.output_height;
        } else {
            d2 = this.cur_x;
            d = this.prev_x;
            i = this.output_width;
        }
        int i3 = this.direction;
        boolean z2 = false;
        if (i3 == 1 || i3 == 3) {
            int i4 = this.mInitParam.output_rotation;
            int i5 = this.mCameraOrientation;
            if ((i4 + i5) % m.cQ != 90) {
            }
        } else {
            int i6 = this.mInitParam.output_rotation;
            int i7 = this.mCameraOrientation;
            if ((i6 + i7) % m.cQ != 0) {
            }
        }
        boolean z3 = false;
        if (z3) {
            if (d - d2 > this.reverse_thres2) {
                return true;
            }
            int i8 = this.mInitParam.output_rotation;
            int i9 = this.mCameraOrientation;
            if ((i8 + i9) % m.cQ == 90 || (i8 + i9) % m.cQ == 180) {
                int i10 = this.direction;
                if (i10 != 0) {
                    if (i10 != 1) {
                        if (i10 != 2) {
                        }
                    }
                    this.peak = Math.max(this.peak, d2);
                    z = true;
                    if (!z ? !(d2 <= ((double) i) && d2 - this.peak <= this.reverse_thres) : !(d2 <= ((double) i) && this.peak - d2 <= this.reverse_thres)) {
                        z2 = true;
                    }
                }
            } else {
                int i11 = this.direction;
                if (i11 != 0) {
                    if (i11 != 1) {
                        if (i11 != 2) {
                        }
                    }
                }
                this.peak = Math.max(this.peak, d2);
                z = true;
            }
            this.peak = Math.min(this.peak, d2);
            z = false;
        } else if (d2 - d > this.reverse_thres2) {
            return true;
        } else {
            if (d2 > this.peak) {
                this.peak = d2;
            }
            if (d2 < 0.0d || this.peak - d2 > this.reverse_thres) {
                return true;
            }
        }
        return z2;
    }

    private boolean isYOutOfRange() {
        boolean z;
        boolean z2;
        boolean z3 = true;
        if (this.cur_x >= 0.0d && this.cur_y >= 0.0d) {
            int min = Math.min(this.output_width, this.output_height);
            int max = Math.max(this.output_width, this.output_height);
            InitParam initParam = this.mInitParam;
            int i = initParam.direction;
            if (i == 3 || i == 4) {
                if (this.mInitParam.output_rotation % 180 == 90) {
                    if (this.cur_y <= ((double) min)) {
                        z2 = false;
                    }
                    return z2;
                } else if (this.cur_x <= ((double) min)) {
                    z = false;
                }
            } else if (initParam.output_rotation % 180 == 90) {
                if (this.cur_y <= ((double) max)) {
                    z3 = false;
                }
                return z3;
            } else {
                if (this.cur_x <= ((double) max)) {
                    z3 = false;
                }
                return z3;
            }
        }
        return z;
    }

    private void updateFrame() {
        float f;
        float f2;
        float f3;
        float f4;
        int i;
        double d;
        float f5;
        double d2;
        int i2;
        float f6;
        float f7;
        double d3;
        double d4;
        double d5;
        int i3;
        if (this.mPreviewFrameRect.width() > 0) {
            InitParam initParam = this.mInitParam;
            int i4 = initParam.output_rotation;
            if (i4 == 90) {
                int i5 = initParam.direction;
                if (i5 == 3 || i5 == 4 || i5 == 1) {
                    f6 = ((float) this.mPreviewViewWidth) / ((float) this.output_width);
                    d3 = this.cur_x;
                    f7 = (float) d3;
                    d5 = this.cur_y;
                    i3 = this.output_height;
                    f3 = (float) ((d5 / ((double) i3)) * ((double) this.mPreviewFrameRect.height()));
                    f2 = (((float) this.mInitParam.input_height) / 2.0f) * f6;
                    f = ((float) this.mPreviewFrameRect.height()) / 2.0f;
                    f4 = f7 * f6;
                    this.frame_rect.set(f4 - f2, f3 - f, f4 + f2, f3 + f);
                }
                f5 = ((float) this.mPreviewViewHeight) / ((float) this.output_height);
                f4 = (float) ((this.cur_x / ((double) this.output_width)) * ((double) this.mPreviewViewWidth));
                d2 = this.cur_y;
                d4 = (double) f5;
            } else {
                if (i4 == 180) {
                    int i6 = initParam.direction;
                    if (i6 == 3 || i6 == 4 || i6 == 1) {
                        f6 = ((float) this.mPreviewViewWidth) / ((float) this.output_height);
                        f7 = (float) this.cur_y;
                        d5 = this.cur_x;
                        i3 = this.output_width;
                        f3 = (float) ((d5 / ((double) i3)) * ((double) this.mPreviewFrameRect.height()));
                        f2 = (((float) this.mInitParam.input_height) / 2.0f) * f6;
                        f = ((float) this.mPreviewFrameRect.height()) / 2.0f;
                        f4 = f7 * f6;
                        this.frame_rect.set(f4 - f2, f3 - f, f4 + f2, f3 + f);
                    }
                    f5 = ((float) this.mPreviewViewHeight) / ((float) this.output_width);
                    f4 = ((float) (this.cur_y / ((double) this.output_height))) * ((float) this.mPreviewViewWidth);
                    d2 = (((double) this.output_width) - this.cur_x) / ((double) this.output_width);
                    i2 = this.mPreviewFrameRect.height();
                } else {
                    int i7 = initParam.direction;
                    if (i4 == 270) {
                        if (i7 == 3 || i7 == 4 || i7 == 1) {
                            float f8 = (float) this.mPreviewViewWidth;
                            int i8 = this.output_width;
                            f6 = f8 / ((float) i8);
                            d3 = ((double) i8) - this.cur_x;
                            f7 = (float) d3;
                            d5 = this.cur_y;
                            i3 = this.output_height;
                            f3 = (float) ((d5 / ((double) i3)) * ((double) this.mPreviewFrameRect.height()));
                            f2 = (((float) this.mInitParam.input_height) / 2.0f) * f6;
                            f = ((float) this.mPreviewFrameRect.height()) / 2.0f;
                            f4 = f7 * f6;
                            this.frame_rect.set(f4 - f2, f3 - f, f4 + f2, f3 + f);
                        }
                        f5 = ((float) this.mPreviewViewHeight) / ((float) this.output_height);
                        f4 = (float) ((this.cur_x / ((double) this.output_width)) * ((double) this.mPreviewViewWidth));
                        d = ((double) this.output_height) - this.cur_y;
                        i = this.output_height;
                    } else if (i7 == 3 || i7 == 4 || i7 == 1) {
                        f6 = ((float) this.mPreviewViewWidth) / ((float) this.output_height);
                        f3 = (float) ((this.cur_x / ((double) this.output_width)) * ((double) this.mPreviewFrameRect.height()));
                        f7 = (float) (((double) this.output_height) - this.cur_y);
                        f2 = (((float) this.mInitParam.input_height) / 2.0f) * f6;
                        f = ((float) this.mPreviewFrameRect.height()) / 2.0f;
                        f4 = f7 * f6;
                        this.frame_rect.set(f4 - f2, f3 - f, f4 + f2, f3 + f);
                    } else {
                        f5 = ((float) this.mPreviewViewHeight) / ((float) this.output_width);
                        f4 = (float) ((this.cur_y / ((double) this.output_height)) * ((double) this.mPreviewViewWidth));
                        d = this.cur_x;
                        i = this.output_width;
                    }
                    d2 = d / ((double) i);
                    i2 = this.mPreviewViewHeight;
                }
                d4 = (double) i2;
            }
            f3 = (float) (d2 * d4);
            f2 = ((float) this.mPreviewFrameRect.width()) / 2.0f;
            f = (((float) this.mInitParam.input_width) / 2.0f) * f5;
            this.frame_rect.set(f4 - f2, f3 - f, f4 + f2, f3 + f);
        }
    }

    public int detect(double d, double d2) {
        this.count++;
        if (!Util.isEqualsZero(this.cur_x) || !Util.isEqualsZero(this.prev_x)) {
            this.prev_x = this.cur_x;
        } else {
            this.prev_x = d;
        }
        this.cur_x = d;
        if (!Util.isEqualsZero(this.cur_y) || !Util.isEqualsZero(this.prev_y)) {
            this.prev_y = this.cur_y;
        } else {
            this.prev_y = d2;
        }
        this.cur_y = d2;
        boolean isYOutOfRange = isYOutOfRange();
        String str = TAG;
        if (isYOutOfRange) {
            Log.d(str, "isYOutOfRange");
            return -3;
        } else if (isReverse()) {
            Log.d(str, "isReverse");
            return -2;
        } else if (isComplete()) {
            Log.d(str, "isComplete");
            return 1;
        } else {
            int checkSpeed = checkSpeed();
            updateFrame();
            return checkSpeed;
        }
    }

    public RectF getFrameRect() {
        return this.frame_rect;
    }
}
